package org.iwuacm.iwuglasstour.model;

import java.io.Serializable;
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
	
	// TODO: Add remainder of model.

	private Building(Builder builder) {
		this.name = builder.name;
		this.description = builder.description;
		this.location = builder.location;
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

		private Builder() {}
		
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
