package org.iwuacm.iwuglasstour.model;

import junit.framework.TestCase;

public class RectangularLocationTest extends TestCase {
	
	/**
	 * Tests that the constructor creates the location and selects the corners correctly.
	 */
	public void testConstructor() {
		final Location northWest = new Location(-20.0, 30.0);
		final Location northEast = new Location(20.0, 30.0);
		final Location southWest = new Location(-20.0, -30.0);
		final Location southEast = new Location(20.0, -30.0);
		
		RectangularLocation location =
				new RectangularLocation(northWest, northEast, southWest, southEast);
		
		assertEquals(northWest, location.getNorthWestCorner());
		assertEquals(northEast, location.getNorthEastCorner());
		assertEquals(southWest, location.getSouthWestCorner());
		assertEquals(southEast, location.getSouthEastCorner());
	}
}
