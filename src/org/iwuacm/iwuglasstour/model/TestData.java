package org.iwuacm.iwuglasstour.model;

import java.util.Arrays;
import java.util.List;

import org.iwuacm.iwuglasstour.R;

/**
 * Test data for testing out the app before everything is implemented.
 */
class TestData {
	
	public static final String DESCRIPTION = "Lorem ipsum dolor sit amet, consectetur "
			+ "adipiscing elit. Aenean tempus vel leo nec feugiat. Nullam ullamcorper ipsum "
			+ "gravida erat maximus, at mollis lacus eleifend.";
	
	public static final Attraction ATTRACTION_1 = new Attraction("Lab", DESCRIPTION);
	public static final Attraction ATTRACTION_2 = new Attraction("Classroom", DESCRIPTION);
	public static final Attraction ATTRACTION_3 = new Attraction("ACM Lounge", DESCRIPTION);
	
	public static final Photo PHOTO_1 = Photo.builder()
			.withDrawableId(R.drawable.test_1)
			.withDescription(DESCRIPTION)
			.build();
	public static final Photo PHOTO_2 = Photo.builder()
			.withDrawableId(R.drawable.test_2)
			.withDescription(DESCRIPTION)
			.build();
	public static final Photo PHOTO_3 = Photo.builder()
			.withDrawableId(R.drawable.test_3)
			.withDescription(DESCRIPTION)
			.build();
	public static final Photo PHOTO_4 = Photo.builder()
			.withDrawableId(R.drawable.test_4)
			.withDescription(DESCRIPTION)
			.build();

	/**
	 * Sample list of buildings. Their locations use {@link #createRectangularLocation}, so they
	 * will just be equivalently sized squares centered at about where they're actually centered.
	 */
	public static final List<Building> BUILDINGS = Arrays.asList(
			getCommonBuilder()
				.withName("Center for Natural Sciences")
				.withLocation(createRectangularLocation(40.49157691582489, -88.99231284856796))
				.build(),
			getCommonBuilder()
				.withName("Center for Liberal Arts")
				.withLocation(createRectangularLocation(40.49180537570156, -88.9907893538475))
				.build(),
			getCommonBuilder()
				.withName("Memorial Center")
				.withLocation(createRectangularLocation(40.49081809708109, -88.99275809526443))
				.build(),
			getCommonBuilder()
				.withName("Buck Memorial Library")
				.withLocation(createRectangularLocation(40.48987160146062, -88.99198561906815))
				.build(),
			getCommonBuilder()
				.withName("State Farm Hall")
				.withLocation(createRectangularLocation(40.49126686189105, -88.99119704961777))
				.build(),
			getCommonBuilder()
				.withName("The Ames Library")
				.withLocation(createRectangularLocation(40.48890469344348, -88.99117559194565))
				.build(),
			getCommonBuilder()
				.withName("Presser Hall")
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
	
	/**
	 * Returns a common {@link Building.Builder} with data that's the same for each test
	 * {@link Building}.
	 */
	private static Building.Builder getCommonBuilder() {
		return Building.builder()
				.withDescription(DESCRIPTION)
				.addAttraction(ATTRACTION_1)
				.addAttraction(ATTRACTION_2)
				.addAttraction(ATTRACTION_3)
				.addPhoto(PHOTO_1)
				.addPhoto(PHOTO_2)
				.addPhoto(PHOTO_3)
				.addPhoto(PHOTO_4);
	}
}
