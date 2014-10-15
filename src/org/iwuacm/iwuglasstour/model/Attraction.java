package org.iwuacm.iwuglasstour.model;

import java.io.Serializable;

/**
 * An attraction or interesting place within a {@link Building}.
 */
public class Attraction implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final String description;
	
	// TODO: Add photos.

	public Attraction(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
}
