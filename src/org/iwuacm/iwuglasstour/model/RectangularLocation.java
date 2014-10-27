package org.iwuacm.iwuglasstour.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.iwuacm.iwuglasstour.util.MathUtils;

/**
 * Defines the location of a place as a rectangle with four {@link Location}s as the corners. The
 * edges must be parallel to the horizontal and vertical axes, so there can only be two distinct
 * latitudes and two distinct longitudes.
 */
public class RectangularLocation implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final Comparator<Location> COMPARE_BY_LONGITUDE_THEN_LATITUDE =
			new Comparator<Location>() {
				@Override
				public int compare(Location lhs, Location rhs) {
					int compareValue = Double.compare(lhs.getLongitude(), rhs.getLongitude());
					
					if (compareValue == 0.0) {
						return Double.compare(lhs.getLatitude(), rhs.getLatitude());
					}
					
					return compareValue;
				}
			};
	
	private final Location northWest;
	private final Location northEast;
	private final Location southWest;
	private final Location southEast;

	/**
	 * Creates a {@link RectangularLocation} from four corners. There is no need to specify the
	 * corners in any order.
	 */
	public RectangularLocation(Location a, Location b, Location c, Location d) {
		List<Location> sortedLocations = new ArrayList<Location>(Arrays.asList(a, b, c, d));
		Collections.sort(sortedLocations, COMPARE_BY_LONGITUDE_THEN_LATITUDE);
		
		this.southWest = sortedLocations.get(0);
		this.northWest = sortedLocations.get(1);
		this.southEast = sortedLocations.get(2);
		this.northEast = sortedLocations.get(3);
		
		Set<Double> latitudes = new HashSet<Double>(Arrays.asList(
				southWest.getLatitude(),
				northWest.getLatitude(),
				southEast.getLatitude(),
				northEast.getLatitude()));
		if (latitudes.size() != 2) {
			throw new IllegalArgumentException("Must have only two distinct latitudes.");
		}
		
		Set<Double> longitudes = new HashSet<Double>(Arrays.asList(
				southWest.getLongitude(),
				northWest.getLongitude(),
				southEast.getLongitude(),
				northEast.getLongitude()));
		if (longitudes.size() != 2) {
			throw new IllegalArgumentException("Must have only two distinct longitudes.");
		}
	}
	
	/**
	 * Returns the north-western most corner.
	 */
	public Location getNorthWestCorner() {
		return northWest;
	}
	
	/**
	 * Returns the north-eastern most corner.
	 */
	public Location getNorthEastCorner() {
		return northEast;
	}
	
	/**
	 * Returns the south-western most corner.
	 */
	public Location getSouthWestCorner() {
		return southWest;
	}
	
	/**
	 * Returns the south-eastern most corner.
	 */
	public Location getSouthEastCorner() {
		return southEast;
	}
	
	/**
	 * Returns the closest location to this building by finding the closest point on its perimeter
	 * to the {@code location} or by returning {@code location} if {@code location} is within the
	 * building.
	 */
	public Location getClosestPointWithin(Location location) {
		Double latitude = null;
		Double longitude = null;
		
		if (MathUtils.isNumberWithin(
				location.getLatitude(), northEast.getLatitude(), southEast.getLatitude())) {
			latitude = location.getLatitude();
		}
		
		if (MathUtils.isNumberWithin(
				location.getLongitude(), northWest.getLongitude(), northEast.getLongitude())) {
			longitude = location.getLongitude();
		}
		
		if ((latitude != null) && (longitude != null)) {
			return new Location(latitude, longitude);
		}
		
		// The other coordinate has to be from a corner. Let's figure out the best corner.
		double minDistance = Double.MAX_VALUE;
		double latitudeThere = 0.0;
		double longitudeThere = 0.0;

		for (Location corner : Arrays.asList(northWest, northEast, southWest, southEast)) {
			double withLatitude = latitude == null ? corner.getLatitude() : latitude;
			double withLongitude = longitude == null ? corner.getLongitude() : longitude;

			double distance = MathUtils.getDistance(
					withLatitude,
					withLongitude,
					location.getLatitude(),
					location.getLongitude());

			if (distance < minDistance) {
				minDistance = distance;
				latitudeThere = withLatitude;
				longitudeThere = withLongitude;
			}
		}

		return new Location(latitudeThere, longitudeThere);
	}
	
	/**
	 * Returns whether {@code location} is within this {@link RectangularLocation}.
	 */
	public boolean isLocationContained(Location location) {
		return (location.getLatitude() >= southEast.getLatitude())
				&& (location.getLatitude() <= northEast.getLatitude())
				&& (location.getLongitude() >= northWest.getLongitude())
				&& (location.getLongitude() <= northEast.getLongitude());
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof RectangularLocation)) {
			return false;
		}
		
		RectangularLocation other = (RectangularLocation) o;
		
		return Objects.equals(northWest, other.northWest)
				&& Objects.equals(northEast, other.northEast)
				&& Objects.equals(southWest, other.southWest)
				&& Objects.equals(southEast, other.southEast);
	}

	@Override
	public String toString() {
		return "RectangularLocation [northWest=" + northWest + ", northEast=" + northEast
				+ ", southWest=" + southWest + ", southEast=" + southEast + "]";
	}
}
