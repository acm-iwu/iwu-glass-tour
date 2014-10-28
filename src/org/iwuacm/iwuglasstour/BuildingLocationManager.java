package org.iwuacm.iwuglasstour;

import java.util.LinkedHashSet;
import java.util.Set;

import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.model.BuildingWithLocation;
import org.iwuacm.iwuglasstour.model.Buildings;
import org.iwuacm.iwuglasstour.model.Location;
import org.iwuacm.iwuglasstour.model.RectangularLocation;
import org.iwuacm.iwuglasstour.util.MathUtils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Ordering;

/**
 * Keeps track of the user's location and provides access to nearby buildings.
 */
public class BuildingLocationManager {

	public interface Listener {
		/**
		 * Called when the building in front or to the sides have changed.
		 */
		void onNearbyBuildingsChange(
				BuildingWithLocation left,
				BuildingWithLocation front,
				BuildingWithLocation right);
		
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
		
		/**
		 * Called when the state of having the user's location has changed.
		 * 
		 * @param hasLocation indicates whether the location is present
		 */
		void onHasLocationChange(boolean hasLocation);
	}
	
	/**
	 * This is approximately the field of view of the human eye (in degrees), not including
	 * peripheral vision, so it's the amount of stuff that we can focus on at a time.
	 */
	@VisibleForTesting
	static final float CONE_OF_VISUAL_ATTENTION = 55.0f;
	
	private final OrientationManager orientationManager;
	private final Set<Listener> listeners;
	private final FluentIterable<Building> buildingsFluentIterable;
	
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
						notifyHasCompassInterference(hasInterference);
					}
				}
			};
			
	private BuildingWithLocation left;
	private BuildingWithLocation front;
	private BuildingWithLocation right;
	private Building inside;
	private boolean hasInterference;
	private boolean hasLocation;
	
	public BuildingLocationManager(
			Buildings buildings,
			OrientationManager orientationManager) {

		this.orientationManager = orientationManager;
		this.listeners = new LinkedHashSet<Listener>();
		this.buildingsFluentIterable = FluentIterable.from(buildings.getAll());
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
	
	public BuildingWithLocation getLeftBuilding() {
		return left;
	}
	
	public BuildingWithLocation getFrontBuilding() {
		return front;
	}
	
	public BuildingWithLocation getRightBuilding() {
		return right;
	}
	
	public boolean hasCompassInterference() {
		return hasInterference;
	}
	
	public boolean hasLocation() {
		return hasLocation;
	}
	
	/**
	 * Calculates the buildings in front and to the sides or the building the user is within.
	 */
	private void updateLocationState() {
		// Cannot proceed without location.
		if (!orientationManager.hasLocation()) {
			if (hasLocation != false) {
				hasLocation = false;
				notifyHasLocation(false);
			}

			return;
		}
		
		if (hasLocation != true) {
			hasLocation = true;
			notifyHasLocation(true);
		}

		Location location =
				MathUtils.androidLocationToModelLocation(orientationManager.getLocation());

		// Check if inside building.
		Optional<Building> insideBuilding =
				buildingsFluentIterable.firstMatch(makeIsInsidePredicate(location));
		if (insideBuilding.isPresent()) {
			if (inside != insideBuilding.get()) {
				inside = insideBuilding.get();
				notifyEnterBuilding();
			}
			
			return;
		}
		
		if (inside != null) {
			inside = null;
			
			notifyExitBuilding();
		}
		
		// Retrieve nearby buildings.
		float heading = orientationManager.getHeading();
		
		Ordering<Building> ordering = makeOrderingByAbsoluteHeadingOffset(location, heading)
				.compound(makeOrderingByDistanceTo(location));
		FluentIterable<Building> sortedBuildings =
				FluentIterable.from(buildingsFluentIterable.toSortedList(ordering))
						.filter(makeIsWithinHemispherePredicate(location, heading));
		
		Building newLeft = null;
		Building newFront = null;
		Building newRight = null;

		Predicate<Building> isWithinConeOfVisualAttentionPredicate =
				makeIsWithinConeOfVisualAttentionPredicate(location, heading);
		FluentIterable<Building> buildingsInConeOfVisualAttention = sortedBuildings
				.filter(isWithinConeOfVisualAttentionPredicate);
		
		if (buildingsInConeOfVisualAttention.isEmpty()) {
			Optional<Building> optionalLeft = sortedBuildings.firstMatch(
					makeIsToSideOfHeadingOffsetPredicate(false, 0.0, location, heading));
			Optional<Building> optionalRight = sortedBuildings.firstMatch(
					makeIsToSideOfHeadingOffsetPredicate(true, 0.0, location, heading));
			
			if (optionalLeft.isPresent()) {
				newLeft = optionalLeft.get();
			}
			
			if (optionalRight.isPresent()) {
				newRight = optionalRight.get();
			}
		} else {
			newFront = buildingsInConeOfVisualAttention.first().get();
			double frontHeadingOffset =
					newFront.getLocation().computeHeadingOffset(location, heading);
			
			// Sort remaining buildings by distance.
			Ordering<Building> closestOrdering = makeOrderingByDistanceTo(location)
					.compound(makeOrderingByAbsoluteHeadingOffset(location, frontHeadingOffset));
			FluentIterable<Building> remaining = FluentIterable.from(
					buildingsInConeOfVisualAttention.skip(1).toSortedList(closestOrdering));
			
			Optional<Building> optionalLeft = remaining.firstMatch(
					makeIsToSideOfHeadingOffsetPredicate(
							false,
							frontHeadingOffset,
							location,
							heading));
			Optional<Building> optionalRight = remaining.firstMatch(
					makeIsToSideOfHeadingOffsetPredicate(
							true,
							frontHeadingOffset,
							location,
							heading));
			
			if (!optionalLeft.isPresent()) {
				optionalLeft = sortedBuildings
						.filter(Predicates.not(isWithinConeOfVisualAttentionPredicate))
						.firstMatch(makeIsToSideOfHeadingOffsetPredicate(
								false,
								0.0,
								location,
								heading));
			}
			
			if (!optionalRight.isPresent()) {
				optionalRight = sortedBuildings
						.filter(Predicates.not(isWithinConeOfVisualAttentionPredicate))
						.firstMatch(makeIsToSideOfHeadingOffsetPredicate(
								true,
								0.0,
								location,
								heading));
			}
			
			if (optionalLeft.isPresent()) {
				newLeft = optionalLeft.get();
			}
			
			if (optionalRight.isPresent()) {
				newRight = optionalRight.get();
			}
		}
		
		BuildingWithLocation newLocatedLeft =
				newLeft == null ? null : new BuildingWithLocation(newLeft, location, heading);
		BuildingWithLocation newLocatedFront =
				newFront == null ? null : new BuildingWithLocation(newFront, location, heading);
		BuildingWithLocation newLocatedRight =
				newRight == null ? null : new BuildingWithLocation(newRight, location, heading);
		
		if (!Objects.equal(newLocatedLeft, left)
				|| !Objects.equal(newLocatedFront, front)
				|| !Objects.equal(newLocatedRight, right)) {
			left = newLocatedLeft;
			front = newLocatedFront;
			right = newLocatedRight;
			
			notifyNearbyBuildingsChange();
		}
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
	private void notifyHasCompassInterference(boolean hasInterference) {
		for (Listener listener : listeners) {
			listener.onCompassInterference(hasInterference);
		}
	}
	
	/**
	 * Notifies the listeners of the beginning or end of having the user's location.
	 */
	private void notifyHasLocation(boolean hasLocation) {
		for (Listener listener : listeners) {
			listener.onHasLocationChange(hasLocation);
		}
	}
	
	/**
	 * Makes a {@link Predicate} for whether a {@link Location} is inside a {@link Building}.
	 */
	private static Predicate<Building> makeIsInsidePredicate(final Location location) {
		return new Predicate<Building>() {
			@Override
			public boolean apply(Building building) {
				return building.getLocation().isLocationContained(location);
			}
		};
	}
	
	/**
	 * Makes a {@link Predicate} for whether a building is within one's cone of visual attention.
	 */
	private static Predicate<Building> makeIsWithinConeOfVisualAttentionPredicate(
			final Location location,
			final float heading) {
		
		return new Predicate<Building>() {
			@Override
			public boolean apply(Building building) {
				double buildingHeadingOffset =
						building.getLocation().computeHeadingOffset(location, heading);

				return Math.abs(buildingHeadingOffset) <= CONE_OF_VISUAL_ATTENTION / 2.0;
			}
		};
	}
	
	/**
	 * Makes a {@link Predicate} that returns whether a building is to the left or right (depending
	 * on value of {@code toRight} of the provided heading offset.
	 */
	private static Predicate<Building> makeIsToSideOfHeadingOffsetPredicate(
			final boolean toRight,
			final double headingOffset,
			final Location location,
			final float heading) {
		
		return new Predicate<Building>() {
			@Override
			public boolean apply(Building building) {
				double buildingHeadingOffset =
						building.getLocation().computeHeadingOffset(location, heading);

				double difference = buildingHeadingOffset - headingOffset;
				
				// Note that the equal case is explicitly excluded, because a building with the same
				// heading offset is in front, not to the left nor right.
				return toRight ? difference > 0 : difference < 0;
			}
		};
	}
	
	/**
	 * Makes a {@link Predicate} that returns whether a building is within the same hemisphere as
	 * the heading (whether its heading offset is within 90 degrees).
	 */
	private static Predicate<Building> makeIsWithinHemispherePredicate(
			final Location location,
			final float heading) { 

		return new Predicate<Building>() {
			@Override
			public boolean apply(Building building) {
				double buildingHeadingOffset =
						building.getLocation().computeHeadingOffset(location, heading);
				return Math.abs(buildingHeadingOffset) <= 90.0;
			}
		};
	}

	/**
	 * Compares {@link Building}s by how far they are from {@code location} using {@link
	 * RectangularLocation#getClosestPointWithin} to find optimal distances.
	 */
	private static Ordering<Building> makeOrderingByDistanceTo(final Location location) {
		return new Ordering<Building>() {
			@Override
			public int compare(Building lhs, Building rhs) {
				Location left = lhs.getLocation().findClosestPointWithin(location);
				Location right = rhs.getLocation().findClosestPointWithin(location);

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
	 * Compares {@link Building}s by how far its bearing is from the given location and heading
	 * using the absolute value of {@link RectangularLocation#computeHeadingOffset}.
	 */
	private static Ordering<Building> makeOrderingByAbsoluteHeadingOffset(
			final Location location,
			final double heading) {
		
		return new Ordering<Building>() {
			@Override
			public int compare(Building lhs, Building rhs) {
				return Double.compare(
						Math.abs(lhs.getLocation().computeHeadingOffset(location, heading)),
						Math.abs(rhs.getLocation().computeHeadingOffset(location, heading)));
			}
		};
	}
}
