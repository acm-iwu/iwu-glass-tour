package org.iwuacm.iwuglasstour.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * An attraction or interesting place within a {@link Building}.
 */
public class Attraction implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final String name;
	private final String description;
	private final List<Photo> photos;
	
	private Attraction(Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
		this.photos = builder.photos;
	}
	
	/**
	 * Returns the name of this attraction.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns a description of this attraction(may be empty).
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Returns a list of photos (may be empty) of this attraction.
	 */
	public List<Photo> getPhotos() {
		return photos;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Attraction)) {
			return false;
		}
		
		Attraction other = (Attraction) o;
		
		return Objects.equals(name, other.name)
				&& Objects.equals(description, other.description)
				&& Objects.equals(photos, other.photos);
	}
	
	@Override
	public String toString() {
		return "Attraction [name=" + name + ", description=" + description + ", photos=" + photos
				+ "]";
	}

	/**
	 * Creates a new {@link Builder}.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Builder to create a new instance of {@link Attraction}.
	 */
	public static class Builder {

		private final List<Photo> photos;
		
		private String name;
		private String description;
		
		private Builder() {
			this.photos = new ArrayList<Photo>();
		}
		
		/**
		 * Sets the name of this attraction.
		 */
		public Builder withName(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * Sets the description of this building.
		 */
		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}
		
		/**
		 * Adds a {@link Photo} to this attraction.
		 */
		public Builder addPhoto(Photo photo) {
			photos.add(photo);
			return this;
		}
		
		/**
		 * Creates a new {@link Attraction} using this {@link Builder}.
		 * 
		 * @throws IllegalArgumentException if no name is given
		 */
		public Attraction build() {
			if (name == null) {
				throw new IllegalArgumentException();
			}

			return new Attraction(this);
		}
	}
}
