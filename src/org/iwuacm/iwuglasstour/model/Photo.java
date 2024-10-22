package org.iwuacm.iwuglasstour.model;

import java.io.Serializable;
import java.util.Objects;

public class Photo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private final int drawableId;
	private final String description;

	private Photo(Builder builder) {
		this.drawableId = builder.drawableId;
		this.description = builder.description;
	}
	
	/**
	 * Returns the photo's drawable resource ID.
	 */
	public int getDrawableId() {
		return drawableId;
	}
	
	/**
	 * Returns the photo's description (if present).
	 */
	public String getDescription() {
		return description;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Photo)) {
			return false;
		}
		
		Photo other = (Photo) o;
		
		return (drawableId == other.drawableId) && Objects.equals(description, other.description);
	}
	
	@Override
	public String toString() {
		return "Photo [drawableId=" + drawableId + ", description=" + description + "]";
	}

	/**
	 * Creates a new {@link Builder}.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Builder to create a new instance of {@link Photo}.
	 */
	public static class Builder {
		
		private Integer drawableId;
		private String description;
		
		private Builder() {}
		
		/**
		 * Sets the drawable ID of the photo.
		 */
		public Builder withDrawableId(int drawableId) {
			this.drawableId = drawableId;
			return this;
		}
		
		/**
		 * Sets the description of the photo.
		 */
		public Builder withDescription(String description) {
			this.description = description;
			return this;
		}
		
		/**
		 * Returns a {@link Photo} from this {@link Builder}.
		 * 
		 * @throws IllegalArgumentException if no drawable ID is provided
		 */
		public Photo build() {
			if (drawableId == null) {
				throw new IllegalArgumentException();
			}

			return new Photo(this);
		}
	}
}
