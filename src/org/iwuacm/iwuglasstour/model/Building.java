package org.iwuacm.iwuglasstour.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a building on the IWU campus.
 */
public class Building implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private final String name;
	private final String description;
	private final RectangularLocation location;
	private final List<Photo> photos;
	private final List<Attraction> attractions;
	
	private Building(Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
		this.location = builder.location;
		this.photos = builder.photos;
		this.attractions = builder.attractions;
	}
	
	/**
	 * Returns the name of the building.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a description of the building (if present).
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
	
	/**
	 * Returns the {@link Photo}s of this building.
	 */
	public List<Photo> getPhotos() {
		return photos;
	}
	
	/**
	 * Returns the attractions in the building.
	 */
	public List<Attraction> getAttractions() {
		return attractions;
	}
	
	/**
	 * Returns a new {@link Builder}.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	public static class Builder {
		
		private String name;
		private String description;
		private RectangularLocation location;
		private List<Photo> photos;
		private List<Attraction> attractions;

		private Builder() {
			photos = new ArrayList<Photo>();
			attractions = new ArrayList<Attraction>();
		}
		
		/**
		 * Sets the name of the building.
		 */
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * Sets the description of the building.
		 */
		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}
		
		/**
		 * Sets the location of the building as a {@link RectangularLocation}.
		 */
		public Builder withLocation(RectangularLocation location) {
			this.location = location;
			return this;
		}
		
		/**
		 * Adds a {@link Photo} to the building.
		 */
		public Builder addPhoto(Photo photo) {
			photos.add(photo);
			return this;
		}
		
		/**
		 * Adds an {@link Attraction} to the building.
		 */
		public Builder addAttraction(Attraction attraction) {
			attractions.add(attraction);
			return this;
		}
		
		/**
		 * Creates a new {@link Building} from this {@link Builder}.
		 *
		 * @throws IllegalArgumentException if any necessary fields are not included
		 */
		public Building build() {
			List<Object> requiredFields = Arrays.<Object>asList(name, location);
			for (Object field : requiredFields) {
				if (field == null) {
					throw new IllegalArgumentException();
				}
			}

			return new Building(this);
		}
	}
}
