package org.iwuacm.iwuglasstour;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.model.Buildings;
import org.iwuacm.iwuglasstour.model.Location;
import org.iwuacm.iwuglasstour.model.RectangularLocation;
import org.iwuacm.iwuglasstour.util.MathUtils;

import android.hardware.SensorManager;
import android.location.LocationManager;

/**
 * Keeps track of the user's location and provides access to nearby buildings.
 */
public class BuildingLocationManager {

	public interface Listener {
		/**
		 * Called when the building in front or to the sides have changed.
		 */
		void onNearbyBuildingsChange(Building left, Building front, Building right);
		
		/**
		 * Called when the user enters a building.
		 */
		void onEnterBuilding(Building building);
		
		/**
		 * Called when the user leaves a building.
		 */
		void onExitBuilding();
		
		/**
		 * Called when Glass detects interference interference has changed.
		 * 
		 * @param hasInterference indicates whether there is interference now
		 */
		void onCompassInterference(boolean hasInterference);
	}
	
	/**
	 * This is approximately the field of view of the human eye (in degrees), not including
	 * peripheral vision, so it's the amount of stuff that we can focus on at a time.
	 */
	private static final float CONE_OF_VISUAL_ATTENTION = 55.0f;
	
	private final Buildings buildings;
	private final OrientationManager orientationManager;
	private final Set<Listener> listeners;
	
	private final OrientationManager.OnChangedListener orientationListener =
			new OrientationManager.OnChangedListener() {
				@Override
				public void onOrientationChanged(OrientationManager orientationManager) {
					updateLocationState();
				}
		
				@Override
				public void onLocationChanged(OrientationManager orientationManager) {
					updateLocationState();
				}
		
				@Override
				public void onAccuracyChanged(OrientationManager orientationManager) {
					boolean hasInterferenceNow = orientationManager.hasInterference();

					if (hasInterference != hasInterferenceNow) {
						hasInterference = hasInterferenceNow;
						notifyCompassInterference(hasInterference);
					}
				}
			};
			
	private Building left;
	private Building front;
	private Building right;
	private Building inside;
	private boolean hasInterference;
	
	public BuildingLocationManager(
			Buildings buildings,
			SensorManager sensorManager,
			LocationManager locationManager) {

		this.buildings = buildings;
		this.orientationManager = new OrientationManager(sensorManager, locationManager);
		this.listeners = new LinkedHashSet<Listener>();
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	public void startTracking() {
		orientationManager.addOnChangedListener(orientationListener);
		orientationManager.start();
	}
	
	public void stopTracking() {
		orientationManager.stop();
		orientationManager.removeOnChangedListener(orientationListener);
	}
	
	public boolean isInsideBuilding() {
		return inside != null;
	}

	/**
	 * Returns the building that the user is within.
	 * 
	 * @throws IllegalStateException if the user is not within a building (see
	 * {@link #isInsideBuilding()})
	 */
	public Building getBuildingInside() {
		if (!isInsideBuilding()) {
			throw new IllegalStateException();
		}
		
		return inside;
	}
	
	public Building getLeftBuilding() {
		return left;
	}
	
	public Building getFrontBuilding() {
		return front;
	}
	
	public Building getRightBuilding() {
		return right;
	}
	
	/**
	 * Calculates the buildings in front and to the sides or the building the user is within.
	 */
	private void updateLocationState() {
		// May not have location right away.
		// TODO: Notify of unavailable location?
		if (!orientationManager.hasLocation()) {
			return;
		}

		float heading = orientationManager.getHeading();
		Location location =
				MathUtils.androidLocationToModelLocation(orientationManager.getLocation());
		
		List<Building> buildingsSortedByDistance = new ArrayList<Building>(buildings.getAll());
		if (buildingsSortedByDistance.isEmpty()) {
			return;
		}
		Collections.sort(buildingsSortedByDistance, getComparatorByDistanceTo(location));
		
		// See if location is within first building.
		Building closestBuilding = buildingsSortedByDistance.get(0);
		if (closestBuilding.getLocation().isLocationContained(location)) {
			if (inside != closestBuilding) {
				inside = closestBuilding;
				notifyEnterBuilding();
			}
			
			return;
		}
		
		// Fill in each of these by finding the first building that matches.
		Building newLeft = null;
		Building newFront = null;
		Building newRight = null;
		for (Building building : buildingsSortedByDistance) {
			// Find out which side (within, right, or left) of the cone of visual attention that
			// the building is within.
			Location closestPoint = building.getLocation().getClosestPointWithin(location);
			float bearing = MathUtils.getBearing(
					location.getLatitude(),
					location.getLongitude(),
					closestPoint.getLatitude(),
					closestPoint.getLongitude());
			
			float headingOffset = bearing - heading;
			if (Math.abs(headingOffset) < CONE_OF_VISUAL_ATTENTION / 2.0f) {
				if (newFront == null) {
					newFront = building;
				}
			} else if (headingOffset > 0.0f) {
				if (newRight == null) {
					newRight = building;
				}
			} else if (newLeft == null) {
				newLeft = building;
			}

			if ((newLeft != null) && (newFront != null) && (newRight != null)) {
				break;
			}
			
		}
		
		if (inside != null) {
			notifyExitBuilding();
			inside = null;
		}

		if ((newLeft != left) || (newFront != front) || (newRight != right)) {
			left = newLeft;
			front = newFront;
			right = newRight;
			
			notifyNearbyBuildingsChange();
		}
	}
	
	/**
	 * Compares {@link Building}s by how far they are from {@code location} using
	 * {@link RectangularLocation#getClosestPointWithin} to find optimal distances.
	 */
	private Comparator<Building> getComparatorByDistanceTo(final Location location) {
		return new Comparator<Building>() {
			@Override
			public int compare(Building lhs, Building rhs) {
				Location left = lhs.getLocation().getClosestPointWithin(location);
				Location right = rhs.getLocation().getClosestPointWithin(location);

				return Double.compare(
						MathUtils.getDistance(
								left.getLatitude(),
								left.getLongitude(),
								location.getLatitude(),
								location.getLongitude()),
						MathUtils.getDistance(
								right.getLatitude(),
								right.getLongitude(),
								location.getLatitude(),
								location.getLongitude()));
			}
		};
	}
	
	/**
	 * Notifies the listeners of a nearby building change. This uses the current left, front, and
	 * right buildings as stored in {@link #left}, {@link #front}, and {@link #right}.
	 */
	private void notifyNearbyBuildingsChange() {
		for (Listener listener : listeners) {
			listener.onNearbyBuildingsChange(left, front, right);
		}
	}
	
	/**
	 * Notifies the listeners of entering a building. This uses the current inside building as
	 * stored in {@link #inside}.
	 */
	private void notifyEnterBuilding() {
		if (inside == null) {
			throw new IllegalStateException();
		}

		for (Listener listener : listeners) {
			listener.onEnterBuilding(inside);
		}
	}
	
	/**
	 * Notifies the listeners of exiting a building.
	 */
	private void notifyExitBuilding() {
		for (Listener listener : listeners) {
			listener.onExitBuilding();
		}
	}
	
	/**
	 * Notifies the listeners of the beginning or end of compass interference.
	 */
	private void notifyCompassInterference(boolean hasInterference) {
		for (Listener listener : listeners) {
			listener.onCompassInterference(hasInterference);
		}
	}
}
