package org.iwuacm.iwuglasstour.model;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

public class RectangularLocationTest extends TestCase {
	
	private static final double DOUBLE_TOLERANCE = 0.0001;
	private static final Location NORTH_WEST = new Location(-20.0, 30.0);
	private static final Location NORTH_EAST = new Location(20.0, 30.0);
	private static final Location SOUTH_WEST = new Location(-20.0, -30.0);
	private static final Location SOUTH_EAST = new Location(20.0, -30.0);
	
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
	 * Tests case where the north west corner is expected.
	 */
	public void testGetClosestPointWithin_locationNorthWest() {
		verifyClosestPoint(NORTH_WEST, new Location(-21.0, 31.0));
	}

	/**
	 * Tests case where the north east corner is expected.
	 */
	public void testGetClosestPointWithin_locationNorthEast() {
		verifyClosestPoint(NORTH_EAST, new Location(21.0, 31.0));
	}

	/**
	 * Tests case where the south west corner is expected.
	 */
	public void testGetClosestPointWithin_locationSouthWest() {
		verifyClosestPoint(SOUTH_WEST, new Location(-21.0, -31.0));
	}
	
	/**
	 * Tests case where the south east corner is expected.
	 */
	public void testGetClosestPointWithin_locationSouthEast() {
		verifyClosestPoint(SOUTH_EAST, new Location(21.0, -31.0));
	}
	
	/**
	 * Tests case where a point along the southern perimeter is expected.
	 */
	public void testGetClosestPointWithin_locationSouthernPerimeter() {
		verifyClosestPoint(new Location(1.0, -30.0), new Location(1.0, -31.0));
	}
	
	/**
	 * Tests case where a point along the eastern perimeter is expected.
	 */
	public void testGetClosestPointWithin_locationEasternPerimeter() {
		verifyClosestPoint(new Location(20.0, 2.0), new Location(21.0, 2.0));
	}
	
	/**
	 * Tests case where a point inside is expected.
	 */
	public void testGetClosestPointWithin_locationWithin() {
		final Location locationWithin = new Location(1.0, 2.0);

		verifyClosestPoint(locationWithin, locationWithin);
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
		Location closestPoint = location.getClosestPointWithin(otherLocation);
		
		assertEquals(expected.getLatitude(), closestPoint.getLatitude(), DOUBLE_TOLERANCE);
		assertEquals(expected.getLongitude(), closestPoint.getLongitude(), DOUBLE_TOLERANCE);
	}
}
