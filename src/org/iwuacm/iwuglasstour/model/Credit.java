package org.iwuacm.iwuglasstour.model;

import java.util.Arrays;
import java.util.List;

import android.support.annotation.Nullable;

/**
 * A credit for the creation of this application.
 */
public class Credit {

	private final String firstName;
	private final String lastName;
	private final String contributions;
	@Nullable private final Photo photo;

	private Credit(Builder builder) {
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.contributions = builder.contributions;
		this.photo = builder.photo;
	}
	
	/**
	 * Returns the first name of the person associated with this credit.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns the last name of the person associated with this credit.
	 */
	public String getLastName() {
		return lastName;
	}
	
	/**
	 * Returns the full name of the person associated with this credit.
	 */
	public String getFullName() {
		return firstName + " " + lastName;
	}
	
	/**
	 * Returns the contributions the person associated with this credit has made.
	 */
	public String getContributions() {
		return contributions;
	}
	
	/**
	 * Optionally returns a photo of the person associated with this credit.
	 */
	@Nullable
	public Photo getPhoto() {
		return photo;
	}
	
	/**
	 * Returns a {@link Builder} for this object.
	 */
	public static Builder builder() {
		return new Builder();
	}
	
	/**
	 * Builder for {@link Credit}.
	 */
	public static class Builder {
		
		private String firstName;
		private String lastName;
		private String contributions;
		private Photo photo;
		
		private Builder() {}
		
		/**
		 * Sets the first name of the person associated with this credit.
		 */
		public Builder withFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		 * Sets the last name of the person associated with this credit.
		 */
		public Builder withLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		/**
		 * Sets the contributions the person associated with this credit has made.
		 */
		public Builder withContributions(String contributions) {
			this.contributions = contributions;
			return this;
		}
		
		/**
		 * Sets the photo of the person associated with this credit.
		 */
		public Builder withPhoto(Photo photo) {
			this.photo = photo;
			return this;
		}
		
		/**
		 * Builds this {@link Credit}.
		 */
		public Credit build() {
			List<Object> requiredFields = Arrays.<Object>asList(firstName, lastName, contributions);
			for (Object field : requiredFields) {
				if (field == null) {
					throw new IllegalArgumentException();
				}
			}
			
			return new Credit(this);
		}
	}
}
