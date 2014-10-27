package org.iwuacm.iwuglasstour.model;

import java.util.Arrays;
import java.util.List;

import org.iwuacm.iwuglasstour.util.MathUtils;

import junit.framework.TestCase;

public class RectangularLocationTest extends TestCase {
	
	private static final double DOUBLE_TOLERANCE = 0.0001;
	private static final Location NORTH_WEST = new Location(20.0, -30.0);
	private static final Location NORTH_EAST = new Location(20.0, 30.0);
	private static final Location SOUTH_WEST = new Location(-20.0, -30.0);
	private static final Location SOUTH_EAST = new Location(-20.0, 30.0);
	
	private RectangularLocation location;
	
	@Override
	protected void setUp() throws Exception {
		location = new RectangularLocation(NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST);
	}
	
	/**
	 * Tests that the constructor creates the location and selects the corners correctly.
	 */
	public void testConstructor() {
		assertEquals(NORTH_WEST, location.getNorthWestCorner());
		assertEquals(NORTH_EAST, location.getNorthEastCorner());
		assertEquals(SOUTH_WEST, location.getSouthWestCorner());
		assertEquals(SOUTH_EAST, location.getSouthEastCorner());
		
		// Create in different order to ensure correct construction.
		RectangularLocation otherLocation =
				new RectangularLocation(NORTH_EAST, SOUTH_WEST, SOUTH_EAST, NORTH_WEST);
		assertEquals(location.getNorthWestCorner(), otherLocation.getNorthWestCorner());
		assertEquals(location.getNorthEastCorner(), otherLocation.getNorthEastCorner());
		assertEquals(location.getSouthWestCorner(), otherLocation.getSouthWestCorner());
		assertEquals(location.getSouthEastCorner(), otherLocation.getSouthEastCorner());
	}
	
	/**
	 * Tests that the constructor throws an IllegalArgumentException when the corners are not
	 * parallel to the horizontal and vertical axes. These bad locations are parallelograms, so it
	 * is important that they be deemed invalid.
	 */
	public void testConstructor_badLatitudes() {
		try {
			new RectangularLocation(
					new Location(2.0, 2.0),
					new Location(0.0, 2.0),
					new Location(1.0, -2.0),
					new Location(-1.0, -2.0));
			
			fail("Missing exception.");
		} catch (IllegalArgumentException e) {}
	}

	public void testConstructor_badLongitudes() {
		try {
			new RectangularLocation(
					new Location(1.0, 1.0),
					new Location(-1.0, 2.0),
					new Location(1.0, -1.0),
					new Location(-1.0, 0.0));
			
			fail("Missing exception.");
		} catch (IllegalArgumentException e) {}
	}

	/**
	 * Tests case where the north west corner is expected.
	 */
	public void testFindClosestPointWithin_locationNorthWest() {
		verifyClosestPoint(NORTH_WEST, new Location(21.0, -31.0));
	}

	/**
	 * Tests case where the north east corner is expected.
	 */
	public void testFindClosestPointWithin_locationNorthEast() {
		verifyClosestPoint(NORTH_EAST, new Location(21.0, 31.0));
	}

	/**
	 * Tests case where the south west corner is expected.
	 */
	public void testFindClosestPointWithin_locationSouthWest() {
		verifyClosestPoint(SOUTH_WEST, new Location(-21.0, -31.0));
	}
	
	/**
	 * Tests case where the south east corner is expected.
	 */
	public void testFindClosestPointWithin_locationSouthEast() {
		verifyClosestPoint(SOUTH_EAST, new Location(-21.0, 31.0));
	}
	
	/**
	 * Tests case where a point along the southern perimeter is expected.
	 */
	public void testFindClosestPointWithin_locationSouthernPerimeter() {
		verifyClosestPoint(new Location(-20.0, 1.0), new Location(-21.0, 1.0));
	}
	
	/**
	 * Tests case where a point along the eastern perimeter is expected.
	 */
	public void testFindClosestPointWithin_locationEasternPerimeter() {
		verifyClosestPoint(new Location(2.0, 30.0), new Location(2.0, 31.0));
	}
	
	/**
	 * Tests case where a point inside is expected.
	 */
	public void testFindClosestPointWithin_locationWithin() {
		final Location locationWithin = new Location(1.0, 2.0);

		verifyClosestPoint(locationWithin, locationWithin);
	}
	
	public void testComputeHeadingOffset_locatedSouth_facingNorth() {
		final Location withLocation = new Location(-21.0, 0.0);
		final double withHeading = 0.0;
		final double expectedOffset = 0.0;

		verifyHeadingOffset(expectedOffset, withLocation, withHeading);
	}
	
	public void testComputeHeadingOffset_locatedWest_facingEast() {
		final Location withLocation = new Location(0.0, -31.0);
		final double withHeading = 90.0;
		final double expectedOffset = 0.0;

		verifyHeadingOffset(expectedOffset, withLocation, withHeading);
	}
	
	public void testComputeHeadingOffset_locatedNorth_facingSouth() {
		final Location withLocation = new Location(21.0, 0.0);
		final double withHeading = 180.0;
		final double expectedOffset = 0.0;

		verifyHeadingOffset(expectedOffset, withLocation, withHeading);
	}
	
	public void testComputeHeadingOffset_locatedEast_facingWest() {
		final Location withLocation = new Location(0.0, 31.0);
		final double withHeading = 270.0;
		final double expectedOffset = 0.0;

		verifyHeadingOffset(expectedOffset, withLocation, withHeading);
	}
	
	/**
	 * Tests the heading offset when facing north, east of the north-east corner. At first, this may
	 * appear to be zero, but because of the non-planar nature of how latitudes and longitudes work
	 * (I think), this is actually the bearing from that location to the north-west corner.
	 */
	public void testComputeHeadingOffset_locatedEast_facingNorth() {
		final Location withLocation = new Location(20.0, 31.0);
		final double withHeading = 0.0;
		final double expectedOffset = -360.0 + MathUtils.getBearing(
				withLocation.getLatitude(),
				withLocation.getLongitude(),
				NORTH_WEST.getLatitude(),
				NORTH_WEST.getLongitude());

		verifyHeadingOffset(expectedOffset, withLocation, withHeading);
	}

	public void testIsLocationContained_notContained() {
		final List<Location> locations = Arrays.asList(
				new Location(-21.0, 1.0),
				new Location(21.0, 1.0),
				new Location(2.0, -31.0),
				new Location(2.0, 31.0),
				new Location(21.0, 31.0));

		for (Location locationNotContained : locations) {
			assertFalse(location.isLocationContained(locationNotContained));
		}
	}
	
	public void testIsLocationContained_contained() {
		final List<Location> locations = Arrays.asList(
				new Location(-20.0, 1.0),
				new Location(20.0, 1.0),
				new Location(2.0, -30.0),
				new Location(2.0, 30.0),
				new Location(1.0, 2.0));

		for (Location locationContained : locations) {
			assertTrue(location.isLocationContained(locationContained));
		}
	}
	
	private void verifyClosestPoint(Location expected, Location otherLocation) {
		Location closestPoint = location.findClosestPointWithin(otherLocation);
		
		assertEquals(expected.getLatitude(), closestPoint.getLatitude(), DOUBLE_TOLERANCE);
		assertEquals(expected.getLongitude(), closestPoint.getLongitude(), DOUBLE_TOLERANCE);
	}
	
	private void verifyHeadingOffset(double expected, Location otherLocation, double heading) {
		double headingOffset = location.computeHeadingOffset(otherLocation, heading);
		
		assertEquals(expected, headingOffset, DOUBLE_TOLERANCE);
	}
}
