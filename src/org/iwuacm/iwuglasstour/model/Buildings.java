package org.iwuacm.iwuglasstour.model;

import java.util.List;

import org.iwuacm.iwuglasstour.R;

/**
 * A collection of {@link Building}s.
 */
public class Buildings {
	
	/**
	 * The resource for the buildings JSON file. This is visible for testing.
	 */
	static final int BUILDINGS_RAW_RESOURCE = R.raw.buildings;

	private final List<Building> buildings;
	
	private Buildings(List<Building> buildings) {
		this.buildings = buildings;
	}
	
	public List<Building> getAll() {
		return buildings;
	}
	
	/**
	 * Reads in the buildings resource and returns an instance of {@link Buildings} containing them.
	 */
	public static Buildings getBuildings() {
		// TODO: Read in buildings.
		
		// TODO: Replace TestData buildings with actual buildings.
		return new Buildings(TestData.BUILDINGS);
	}
}
