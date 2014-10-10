package org.iwuacm.iwuglasstour.model;

/**
 * Represents a building on the IWU campus.
 */
public class Building {
	
	private final String name;
	private final String description;
	private final RectangularLocation location;
	
	// TODO: Add photos.

	public Building(String name, String description, RectangularLocation location) {
		this.name = name;
		this.description = description;
		this.location = location;
	}
	
	/**
	 * Returns the name of the building.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a description of the building.
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns the rectangular location of the building.
	 */
	public RectangularLocation getLocation() {
		return location;
	}
}
