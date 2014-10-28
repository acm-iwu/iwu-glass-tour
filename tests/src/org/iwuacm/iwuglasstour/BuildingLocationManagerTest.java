package org.iwuacm.iwuglasstour;

import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.model.BuildingWithLocation;
import org.iwuacm.iwuglasstour.model.Buildings;
import org.iwuacm.iwuglasstour.model.RectangularLocation;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;

import android.location.Location;
import android.test.AndroidTestCase;

/**
 * Tests for {@link BuildingLocationManager}.
 */
public class BuildingLocationManagerTest extends AndroidTestCase {
	
	private static final double SQUARE_SIZE = 0.0005;
	
	private static final double LATITUDE = 0.0;
	private static final double LONGITUDE = 0.0;
	private static final float HEADING = 0.0f;
	
	private static final Predicate<Building> WITHIN_CONE_OF_VISUAL_ATTENTION =
			new Predicate<Building>() {
				@Override
				public boolean apply(Building building) {
					org.iwuacm.iwuglasstour.model.Location location =
							new org.iwuacm.iwuglasstour.model.Location(LATITUDE, LONGITUDE);

					double headingOffset =
							building.getLocation().computeHeadingOffset(location, HEADING);
					
					return Math.abs(headingOffset)
							<= BuildingLocationManager.CONE_OF_VISUAL_ATTENTION / 2.0;
				}
			};
	
	@Mock Buildings buildings;
	@Mock BuildingLocationManager.Listener listener;
	@Mock OrientationManager orientationManager;
	@Captor ArgumentCaptor<OrientationManager.OnChangedListener> orientationManagerListener;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		// Mockito fix. See: https://code.google.com/p/dexmaker/issues/detail?id=2
		System.setProperty("dexmaker.dexcache", getContext().getCacheDir().toString());

		MockitoAnnotations.initMocks(this);
		
		doNothing()
				.when(orientationManager)
				.addOnChangedListener(orientationManagerListener.capture());
		when(orientationManager.hasLocation()).thenReturn(true);
		mockLocationAndHeading(LATITUDE, LONGITUDE, HEADING);
	}
	
	public void testStartTracking() {
		createBuildingLocationManager();
		
		verify(orientationManager)
				.addOnChangedListener(any(OrientationManager.OnChangedListener.class));
		verify(orientationManager).start();
	}
	
	public void testStopTracking() {
		BuildingLocationManager locationManager = createBuildingLocationManager();

		locationManager.stopTracking();
		
		verify(orientationManager).stop();
		verify(orientationManager)
				.removeOnChangedListener(orientationManagerListener.getValue());
	}
	
	/**
	 * Tests two buildings directly in front of user. Should return closest.
	 */
	public void testUpdateLocationState_withTwoFront() {
		final Building front = createBuilding(createSquareLocation(0.01, 0.0));
		final Building back = createBuilding(createSquareLocation(0.01 + SQUARE_SIZE * 2, 0.0));
		
		verifyUpdateLocationState(
				null,
				front,
				null,
				ImmutableList.of(front, back),
				ImmutableList.<Building>of());
	}
	
	/**
	 * Tests a building directly in front of the user with two in front of that building on its
	 * sides.
	 */
	public void testUpdateLocationState_withFront_withSidesInFront() {
		final Building front = createBuilding(createSquareLocation(0.01, 0.0));
		final Building left =
				createBuilding(createSquareLocation(0.01 - 2.0 * SQUARE_SIZE, -2.0 * SQUARE_SIZE));
		final Building right =
				createBuilding(createSquareLocation(0.01 - 2.0 * SQUARE_SIZE, 2.0 * SQUARE_SIZE));
		
		verifyUpdateLocationState(
				left,
				front,
				right,
				ImmutableList.of(left, front, right),
				ImmutableList.<Building>of());
	}
	
	/**
	 * Tests three buildings side by side.
	 */
	public void testUpdateLocationState_withFront_withSidesToSide() {
		final Building left = createBuilding(createSquareLocation(0.01, -2.0 * SQUARE_SIZE));
		final Building front = createBuilding(createSquareLocation(0.01, 0.0));
		final Building right = createBuilding(createSquareLocation(0.01, 2.0 * SQUARE_SIZE));

		verifyUpdateLocationState(
				left,
				front,
				right,
				ImmutableList.of(left, front, right),
				ImmutableList.<Building>of());
	}
	
	/**
	 * Creates a large building in front of the user with two directly behind it that are
	 * obstructed but would otherwise be the left and right. Makes sure that these are not declared
	 * as the left and right building.
	 */
	public void testUpdateLocationState_withFront_withSidesObstructed() {
		final Building blockedLeft =
				createBuilding(createSquareLocation(0.01 + 2.0 * SQUARE_SIZE, -SQUARE_SIZE / 2.0));
		final Building front = createBuilding(createRectangleLocation(0.01, 0.0, 2.0, 1.0));
		final Building blockedRight =
				createBuilding(createSquareLocation(0.01 + 2.0 * SQUARE_SIZE, SQUARE_SIZE / 2.0));

		verifyUpdateLocationState(
				null,
				front,
				null,
				ImmutableList.of(blockedLeft, front, blockedRight),
				ImmutableList.<Building>of());
	}

	/**
	 * Like {@link #testUpdateLocationState_withFront_withSidesObstructed} but with a building to
	 * the left and right outside of the field of vision.
	 */
	public void testUpdateLocationState_withFront_withSidesObstructedAndToSides() {
		final Building left = createBuilding(createSquareLocation(SQUARE_SIZE, -1.0));
		final Building blockedLeft =
				createBuilding(createSquareLocation(0.01 + 2.0 * SQUARE_SIZE, -SQUARE_SIZE / 2.0));
		final Building front = createBuilding(createRectangleLocation(0.01, 0.0, 2.0, 1.0));
		final Building right = createBuilding(createSquareLocation(SQUARE_SIZE, 1.0));
		final Building blockedRight =
				createBuilding(createSquareLocation(0.01 + 2.0 * SQUARE_SIZE, SQUARE_SIZE / 2.0));

		verifyUpdateLocationState(
				left,
				front,
				right,
				ImmutableList.of(blockedLeft, front, blockedRight),
				ImmutableList.of(left, right));
	}

	/**
	 * Tests a building in the front and to the sides but below the hemisphere of the heading.
	 */
	public void testUpdateLocationState_withFront_withSidesOutsideHemisphere() {
		final Building left =
				createBuilding(createSquareLocation(-2.0 * SQUARE_SIZE, -2.0 * SQUARE_SIZE));
		final Building front = createBuilding(createSquareLocation(0.01, 0.0));
		final Building right =
				createBuilding(createSquareLocation(-2.0 * SQUARE_SIZE, 2.0 * SQUARE_SIZE));

		verifyUpdateLocationState(
				null,
				front,
				null,
				ImmutableList.of(front),
				ImmutableList.of(left, right));
	}
	
	/**
	 * Mocks the available {@link Building}s. The arguments are separated by whether in cone of
	 * vision to make sure that the tests are accurate.
	 */
	private void mockBuildings(
			List<Building> buildingsInConeOfVisualAttention,
			List<Building> buildingsOutsideConeOfVisualAttention) {
		
		Preconditions.checkArgument(
				FluentIterable.from(buildingsInConeOfVisualAttention)
						.allMatch(WITHIN_CONE_OF_VISUAL_ATTENTION),
				"Some buildings unexpectedly not within cone of vision");
		Preconditions.checkArgument(
				FluentIterable.from(buildingsOutsideConeOfVisualAttention)
						.allMatch(Predicates.not(WITHIN_CONE_OF_VISUAL_ATTENTION)),
				"Some buildings unexpectedly within cone of vision");
		
		List<Building> allBuildings = ImmutableList.<Building>builder()
				.addAll(buildingsInConeOfVisualAttention)
				.addAll(buildingsOutsideConeOfVisualAttention)
				.build();

		when(buildings.getAll()).thenReturn(allBuildings);
	}
	
	private void mockLocationAndHeading(double latitude, double longitude, float heading) {
		Location location = new Location(""); // Provider not necessary.
		location.setLatitude(latitude);
		location.setLongitude(longitude);

		when(orientationManager.getLocation()).thenReturn(location);
		when(orientationManager.getHeading()).thenReturn(heading);
	}
	
	private BuildingLocationManager createBuildingLocationManager() {
		BuildingLocationManager locationManager =
				new BuildingLocationManager(buildings, orientationManager);
		locationManager.addListener(listener);
		locationManager.startTracking();
		
		return locationManager;
	}
	
	private void verifyUpdateLocationState(
			Building expectedLeftBuilding,
			Building expectedFrontBuilding,
			Building expectedRightBuilding,
			List<Building> buildingsInConeOfVisualAttention,
			List<Building> buildingsOutsideConeOfVisualAttention) {
		
		mockBuildings(buildingsInConeOfVisualAttention, buildingsOutsideConeOfVisualAttention);
		
		createBuildingLocationManager();
		orientationManagerListener.getValue().onLocationChanged(orientationManager);
		
		BuildingWithLocation expectedLeft = expectedLeftBuilding == null
				? null
				: new BuildingWithLocation(
						expectedLeftBuilding,
						new org.iwuacm.iwuglasstour.model.Location(LATITUDE, LONGITUDE),
						HEADING);
		BuildingWithLocation expectedFront = expectedFrontBuilding == null
				? null
				: new BuildingWithLocation(
						expectedFrontBuilding,
						new org.iwuacm.iwuglasstour.model.Location(LATITUDE, LONGITUDE),
						HEADING);
		BuildingWithLocation expectedRight = expectedRightBuilding == null
				? null
				: new BuildingWithLocation(
						expectedRightBuilding,
						new org.iwuacm.iwuglasstour.model.Location(LATITUDE, LONGITUDE),
						HEADING);
		verify(listener).onNearbyBuildingsChange(
				expectedLeft == null ? isNull(BuildingWithLocation.class) : eq(expectedLeft),
				expectedFront == null ? isNull(BuildingWithLocation.class) : eq(expectedFront),
				expectedRight == null ? isNull(BuildingWithLocation.class) : eq(expectedRight));
	}
	
	/**
	 * Creates a {@link Building} with a {@link RectangularLocation}.
	 */
	private static Building createBuilding(RectangularLocation location) {
		return Building.builder()
				.withName("Name")
				.withShortName("ShortName")
				.withLocation(location)
				.build();
	}

	/**
	 * Creates a square {@link RectangularLocation} that is centered at the given {@code latitude}
	 * and {@code longitude} with sides of length {@link #SQUARE_SIZE}.
	 */
	private static RectangularLocation createSquareLocation(double latitude, double longitude) {
		double halfSquareSize = SQUARE_SIZE / 2.0;
		
		return new RectangularLocation(
				new org.iwuacm.iwuglasstour.model.Location(
						latitude - halfSquareSize,
						longitude - halfSquareSize),
				new org.iwuacm.iwuglasstour.model.Location(
						latitude + halfSquareSize,
						longitude - halfSquareSize),
				new org.iwuacm.iwuglasstour.model.Location(
						latitude + halfSquareSize,
						longitude + halfSquareSize),
				new org.iwuacm.iwuglasstour.model.Location(
						latitude - halfSquareSize,
						longitude + halfSquareSize));
	}

	/**
	 * Creates a {@link RectangularLocation} that is centered at the given {@code latitude}
	 * and {@code longitude} with sides of length {@link #SQUARE_SIZE} times {@code widthFactor} for
	 * the width and {@code heightFactor} for the height.
	 */
	private static RectangularLocation createRectangleLocation(
			double latitude,
			double longitude,
			double widthFactor,
			double heightFactor) {

		double halfWidth = SQUARE_SIZE / 2.0 * widthFactor;
		double halfHeight = SQUARE_SIZE / 2.0 * heightFactor;
		
		return new RectangularLocation(
				new org.iwuacm.iwuglasstour.model.Location(
						latitude - halfHeight,
						longitude - halfWidth),
				new org.iwuacm.iwuglasstour.model.Location(
						latitude + halfHeight,
						longitude - halfWidth),
				new org.iwuacm.iwuglasstour.model.Location(
						latitude + halfHeight,
						longitude + halfWidth),
				new org.iwuacm.iwuglasstour.model.Location(
						latitude - halfHeight,
						longitude + halfWidth));
	}
}
