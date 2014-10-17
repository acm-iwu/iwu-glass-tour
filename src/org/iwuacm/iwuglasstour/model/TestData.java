package org.iwuacm.iwuglasstour.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.iwuacm.iwuglasstour.R;

/**
 * Test data for testing out the app before everything is implemented.
 */
class TestData {

	// Private stuff first because it's used by the helper methods.
	/**
	 * Length of random descriptions in sentences.
	 */
	private static final int RANDOM_DESCRIPTION_LENGTH = 2;
	private static final List<String> LOREM_IPSUM_SENTENCES = Arrays.asList(
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
			"Maecenas nec est volutpat, aliquet eros ac, interdum diam.",
			"Maecenas pulvinar, dolor eu dignissim facilisis, est arcu interdum massa, nec sodales "
					+ "ligula tellus in urna.",
			"Cras feugiat ligula elit, eu laoreet lacus sagittis ut.",
			"Aliquam hendrerit erat odio, sed bibendum tellus pellentesque vitae.",
			"Proin erat sapien, vulputate sed ultrices eu, sagittis ac elit.",
			"In hac habitasse platea dictumst.",
			"Aliquam erat volutpat.",
			"Etiam maximus scelerisque gravida.",
			"Ut a urna nibh.",
			"Phasellus id leo sed massa commodo hendrerit sed sit amet odio.",
			"Ut vel elit et elit consectetur lacinia efficitur ac ligula.");
	private static final Random random = new Random();
	
	private static final double LOCATION_SIZE_IN_CARTESIAN = 0.001;
	
	public static final Photo PHOTO_1 = Photo.builder()
			.withDrawableId(R.drawable.test_1)
			.withDescription(generateRandomDescription())
			.build();
	public static final Photo PHOTO_2 = Photo.builder()
			.withDrawableId(R.drawable.test_2)
			.withDescription(generateRandomDescription())
			.build();
	public static final Photo PHOTO_3 = Photo.builder()
			.withDrawableId(R.drawable.test_3)
			.withDescription(generateRandomDescription())
			.build();
	public static final Photo PHOTO_4 = Photo.builder()
			.withDrawableId(R.drawable.test_4)
			.withDescription(generateRandomDescription())
			.build();
	
	public static final Attraction ATTRACTION_1 = Attraction.builder()
			.withName("Lab")
			.withDescription(generateRandomDescription())
			.addPhoto(PHOTO_1)
			.addPhoto(PHOTO_2)
			.build();
	public static final Attraction ATTRACTION_2 = Attraction.builder()
			.withName("Classroom")
			.withDescription(generateRandomDescription())
			.addPhoto(PHOTO_3)
			.build();
	public static final Attraction ATTRACTION_3 = Attraction.builder()
			.withName("ACM Lounge")
			.withDescription(generateRandomDescription())
			.addPhoto(PHOTO_4)
			.build();

	/**
	 * Sample list of buildings. Their locations use {@link #createRectangularLocation}, so they
	 * will just be equivalently sized squares centered at about where they're actually centered.
	 */
	public static final List<Building> BUILDINGS = Arrays.asList(
			getCommonBuilder()
				.withName("Center for Natural Sciences")
				.withShortName("CNS")
				.withLocation(createRectangularLocation(40.49157691582489, -88.99231284856796))
				.build(),
			getCommonBuilder()
				.withName("Center for Liberal Arts")
				.withShortName("CLA")
				.withLocation(createRectangularLocation(40.49180537570156, -88.9907893538475))
				.build(),
			getCommonBuilder()
				.withName("Memorial Center")
				.withShortName("Memorial Ctr.")
				.withLocation(createRectangularLocation(40.49081809708109, -88.99275809526443))
				.build(),
			getCommonBuilder()
				.withName("Buck Memorial Library")
				.withShortName("Buck")
				.withLocation(createRectangularLocation(40.48987160146062, -88.99198561906815))
				.build(),
			getCommonBuilder()
				.withName("State Farm Hall")
				.withShortName("SFH")
				.withLocation(createRectangularLocation(40.49126686189105, -88.99119704961777))
				.build(),
			getCommonBuilder()
				.withName("The Ames Library")
				.withShortName("Ames")
				.withLocation(createRectangularLocation(40.48890469344348, -88.99117559194565))
				.build(),
			getCommonBuilder()
				.withName("Presser Hall")
				.withShortName("Presser")
				.withLocation(createRectangularLocation(40.48967169335051, -88.99041920900345))
				.build()
			);

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
				.withDescription(generateRandomDescription())
				.addAttraction(ATTRACTION_1)
				.addAttraction(ATTRACTION_2)
				.addAttraction(ATTRACTION_3)
				.addPhoto(PHOTO_1)
				.addPhoto(PHOTO_2)
				.addPhoto(PHOTO_3)
				.addPhoto(PHOTO_4);
	}
	
	/**
	 * Generates a random description of {@link #RANDOM_DESCRIPTION_LENGTH} consecutive sentences
	 * from Lorem Ipsum.
	 */
	private static String generateRandomDescription() {
		int firstSentenceIndex =
				random.nextInt(LOREM_IPSUM_SENTENCES.size() + 1 - RANDOM_DESCRIPTION_LENGTH);

		StringBuilder description = new StringBuilder();
		for (int i = 0; i < RANDOM_DESCRIPTION_LENGTH; i++) {
			if (i != 0) {
				description.append(" ");
			}

			description.append(LOREM_IPSUM_SENTENCES.get(firstSentenceIndex + i));
		}
		
		return description.toString();
	}
}
