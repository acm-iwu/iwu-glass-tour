package org.iwuacm.iwuglasstour.model;

/**
 * A location defined by a latitude and longitude.
 */
public class Location {

	private final double latitude;
	private final double longitude;
	
	public Location(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}
