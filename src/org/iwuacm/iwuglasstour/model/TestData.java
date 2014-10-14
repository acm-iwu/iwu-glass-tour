package org.iwuacm.iwuglasstour.model;

import java.util.Arrays;
import java.util.List;

/**
 * Test data for testing out the app before everything is implemented.
 */
class TestData {
	
	public static final String TEST_DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur "
			+ "adipiscing elit. Aenean tempus vel leo nec feugiat. Nullam ullamcorper ipsum "
			+ "gravida erat maximus, at mollis lacus eleifend.";

	/**
	 * Sample list of buildings. Their locations use {@link #createRectangularLocation}, so they
	 * will just be equivalently sized squares centered at about where they're actually centered.
	 */
	public static final List<Building> BUILDINGS = Arrays.asList(
			Building.builder()
				.withName("Center for Natural Sciences")
				.withDescription(TEST_DESCRIPTION)
				.withLocation(createRectangularLocation(40.49157691582489, -88.99231284856796))
				.build(),
			Building.builder()
				.withName("Center for Liberal Arts")
				.withDescription(TEST_DESCRIPTION)
				.withLocation(createRectangularLocation(40.49180537570156, -88.9907893538475))
				.build(),
			Building.builder()
				.withName("Memorial Center")
				.withDescription(TEST_DESCRIPTION)
				.withLocation(createRectangularLocation(40.49081809708109, -88.99275809526443))
				.build(),
			Building.builder()
				.withName("Buck Memorial Library")
				.withDescription(TEST_DESCRIPTION)
				.withLocation(createRectangularLocation(40.48987160146062, -88.99198561906815))
				.build(),
			Building.builder()
				.withName("State Farm Hall")
				.withDescription(TEST_DESCRIPTION)
				.withLocation(createRectangularLocation(40.49126686189105, -88.99119704961777))
				.build(),
			Building.builder()
				.withName("The Ames Library")
				.withDescription(TEST_DESCRIPTION)
				.withLocation(createRectangularLocation(40.48890469344348, -88.99117559194565))
				.build(),
			Building.builder()
				.withName("Presser Hall")
				.withDescription(TEST_DESCRIPTION)
				.withLocation(createRectangularLocation(40.48967169335051, -88.99041920900345))
				.build()
			);
	
	private static final double LOCATION_SIZE_IN_CARTESIAN = 0.001;

	/**
	 * Creates a square {@link RectangularLocation} that is centered at the given {@code latitude}
	 * and {@code longitude} with sides of length {@link #LOCATION_SIZE_IN_CARTESIAN}.
	 */
	private static RectangularLocation createRectangularLocation(
			double latitude, double longitude) {

		double halfLocationSize = LOCATION_SIZE_IN_CARTESIAN / 2.0;
		
		return new RectangularLocation(
				new Location(latitude - halfLocationSize, longitude - halfLocationSize),
				new Location(latitude + halfLocationSize, longitude - halfLocationSize),
				new Location(latitude + halfLocationSize, longitude + halfLocationSize),
				new Location(latitude - halfLocationSize, longitude + halfLocationSize));
	}
}
