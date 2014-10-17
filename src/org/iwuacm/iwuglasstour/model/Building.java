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
	private final String shortName;
	private final String description;
	private final RectangularLocation location;
	private final List<Photo> photos;
	private final List<Attraction> attractions;
	
	private Building(Builder builder) {
		this.name = builder.name;
		this.shortName = builder.shortName;
		this.description = builder.description;
		this.location = builder.location;
		this.photos = builder.photos;
		this.attractions = builder.attractions;
	}
	
	/**
	 * Returns the full name of the building.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a shorter version (e.g., acronym) of the building's name.
	 */
	public String getShortName() {
		return shortName;
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
	
	/**
	 * Builder to create a new instance of {@link Building}.
	 */
	public static class Builder {
		
		private final List<Photo> photos;
		private final List<Attraction> attractions;

		private String name;
		private String shortName;
		private String description;
		private RectangularLocation location;

		private Builder() {
			this.photos = new ArrayList<Photo>();
			this.attractions = new ArrayList<Attraction>();
		}
		
		/**
		 * Sets the full name of the building.
		 */
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * Sets a shorter version (e.g., acronym) of the building's name.
		 */
		public Builder withShortName(String shortName) {
			this.shortName = shortName;
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
			List<Object> requiredFields = Arrays.<Object>asList(name, shortName, location);
			for (Object field : requiredFields) {
				if (field == null) {
					throw new IllegalArgumentException();
				}
			}

			return new Building(this);
		}
	}
}
