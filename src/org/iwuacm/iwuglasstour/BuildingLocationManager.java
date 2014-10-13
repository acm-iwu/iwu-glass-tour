package org.iwuacm.iwuglasstour;

import java.util.LinkedHashSet;
import java.util.Set;

import org.iwuacm.iwuglasstour.model.Building;

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
	}
	
	// TODO: Use.
	@SuppressWarnings("unused")
	private final LocationManager locationManager;
	private final Set<Listener> listeners;
	
	public BuildingLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
		this.listeners = new LinkedHashSet<Listener>();
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}

	public void removeListener(Listener listener) {
		listeners.remove(listener);
	}
	
	// TODO: Implement.
	public void startTracking() {}
	
	// TODO: Implement.
	public void stopTracking() {}
	
	public boolean isInsideBuilding() {
		// TODO: Implement.
		return false;
	}
	
	public Building getLeftBuilding() {
		// TODO: Implement.
		return null;
	}
	
	public Building getFrontBuilding() {
		// TODO: Implement.
		return null;
	}
	
	public Building getRightBuilding() {
		// TODO: Implement.
		return null;
	}
}
