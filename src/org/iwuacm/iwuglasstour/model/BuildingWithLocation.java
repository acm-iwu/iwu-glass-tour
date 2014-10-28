package org.iwuacm.iwuglasstour.model;

import org.iwuacm.iwuglasstour.util.MathUtils;

import com.google.common.base.Preconditions;

/**
 * Specifies a {@link Building} with its relative distance and heading.
 */
public class BuildingWithLocation {
	
	private final Building building;
	private final double headingOffset;
	
	/**
	 * The distance from the user's location to the {@link Building} in kilometers.
	 */
	private final double distance;

	/**
	 * Creates a {@link BuildingLocation} given a {@link Building} and the user's location and
	 * heading.
	 */
	public BuildingWithLocation(Building building, Location location, double userHeading) {
		Preconditions.checkNotNull(building);
		Preconditions.checkNotNull(location);

		this.building = building;

		RectangularLocation buildingLocation = building.getLocation();
		Location closestPoint = buildingLocation.findClosestPointWithin(location);
		this.distance = MathUtils.getDistance(
				location.getLatitude(),
				location.getLongitude(),
				closestPoint.getLatitude(),
				closestPoint.getLongitude());

		this.headingOffset = buildingLocation.computeHeadingOffset(location, userHeading);
	}
	
	/**
	 * Returns the {@link Building} associated with this object.
	 */
	public Building getBuilding() {
		return building;
	}
	
	/**
	 * Returns the distance to this {@link Building} from the user's location in kilometers.
	 */
	public double getDistance() {
		return distance;
	}
	
	/**
	 * Returns the heading offset between this {@link Building} and the user's heading, such that
	 * 0 means the building is directly in front, a negative value means it's to the left of the
	 * direction the user is facing, and a positive value means it's to the right.
	 */
	public double getHeadingOffset() {
		return headingOffset;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof BuildingWithLocation)) {
			return false;
		}
		
		BuildingWithLocation other = (BuildingWithLocation) o;
		
		return building.equals(other.building)
				&& (distance == other.distance)
				&& (headingOffset == other.headingOffset);
	}

	@Override
	public String toString() {
		return "BuildingWithLocation [building=" + building + ", headingOffset=" + headingOffset
				+ ", distance=" + distance + "]";
	}
	
	
}
