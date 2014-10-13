package org.iwuacm.iwuglasstour.model;

import java.io.Serializable;

/**
 * A location defined by a latitude and longitude.
 */
public class Location implements Serializable {

	private static final long serialVersionUID = 1L;

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
