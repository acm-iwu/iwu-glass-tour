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
}
