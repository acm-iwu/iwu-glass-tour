package org.iwuacm.iwuglasstour.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Defines the location of a place as a rectangle with four {@link Location}s as the corners.
 */
public class RectangularLocation implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private static final Comparator<Location> COMPARE_BY_LATITUDE_THEN_LONGITUDE =
			new Comparator<Location>() {
				@Override
				public int compare(Location lhs, Location rhs) {
					int compareValue = Double.compare(lhs.getLatitude(), rhs.getLatitude());
					
					if (compareValue == 0.0) {
						return Double.compare(lhs.getLongitude(), rhs.getLongitude());
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
		Collections.sort(sortedLocations, COMPARE_BY_LATITUDE_THEN_LONGITUDE);
		
		this.southWest = sortedLocations.get(0);
		this.northWest = sortedLocations.get(1);
		this.southEast = sortedLocations.get(2);
		this.northEast = sortedLocations.get(3);
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
}
