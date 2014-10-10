package org.iwuacm.iwuglasstour;

import java.util.LinkedHashSet;
import java.util.Set;

import org.iwuacm.iwuglasstour.model.Building;

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
	private final BuildingLocationManager locationManager;
	private final Set<Listener> listeners;
	
	public BuildingLocationManager(BuildingLocationManager locationManager) {
		this.locationManager = locationManager;
		this.listeners = new LinkedHashSet<Listener>();
	}
	
	public void addListener(Listener listener) {
		listeners.add(listener);
	}
}
