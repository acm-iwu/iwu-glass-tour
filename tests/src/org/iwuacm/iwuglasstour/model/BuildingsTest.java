package org.iwuacm.iwuglasstour.model;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import android.content.res.Resources;
import android.test.AndroidTestCase;
import android.test.mock.MockContext;
import android.test.mock.MockResources;
import android.util.JsonWriter;

/**
 * Tests for {@link Buildings}.
 */
public class BuildingsTest extends AndroidTestCase {
	
	private static final String FAKE_DRAWABLE_PREFIX = "drawable-";
	private static final String PACKAGE_NAME = "package.name";

	private static final RectangularLocation LOCATION_1 = new RectangularLocation(
			new Location(-1.0, -2.0),
			new Location(1.0, -2.0),
			new Location(-1.0, 2.0),
			new Location(1.0, 2.0));
	private static final RectangularLocation LOCATION_2 = new RectangularLocation(
			new Location(-3.0, -4.0),
			new Location(3.0, -4.0),
			new Location(-3.0, 4.0),
			new Location(3.0, 4.0));
	
	private static final Photo PHOTO_1 = Photo.builder()
			.withDrawableId(1)
			.build();
	private static final Photo PHOTO_2 = Photo.builder()
			.withDrawableId(2)
			.withDescription("Photo 2")
			.build();
	private static final Photo PHOTO_3 = Photo.builder()
			.withDrawableId(3)
			.build();
	private static final Photo PHOTO_4 = Photo.builder()
			.withDrawableId(4)
			.withDescription("Photo 4")
			.build();
	
	private static final Attraction ATTRACTION_1 = Attraction.builder()
			.withName("Attraction 1")
			.build();
	private static final Attraction ATTRACTION_2 = Attraction.builder()
			.withName("Attraction 2")
			.withDescription("Description 2")
			.addPhoto(PHOTO_3)
			.addPhoto(PHOTO_4)
			.build();
	
	private static final Building BUILDING_1 = Building.builder()
			.withName("Buliding 1")
			.withShortName("B1")
			.withLocation(LOCATION_1)
			.build();
	private static final Building BUILDING_2 = Building.builder()
			.withName("Building 2")
			.withShortName("B2")
			.withLocation(LOCATION_2)
			.addPhoto(PHOTO_1)
			.addPhoto(PHOTO_2)
			.addAttraction(ATTRACTION_1)
			.addAttraction(ATTRACTION_2)
			.build();
	private static final List<Building> BUILDINGS = Arrays.asList(BUILDING_1, BUILDING_2);
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		mockBuildings(BUILDINGS);
	}
	
	public void testGetBuildings() {
		List<Building> buildings = Buildings.getBuildings().getAll();
		
		assertEquals(BUILDINGS.size(), buildings.size());
		
		Iterator<Building> expectedBuildingsIterator = BUILDINGS.iterator();
		Iterator<Building> actualBuildingsIterator = buildings.iterator();
		while (expectedBuildingsIterator.hasNext()) {
			assertEquals(expectedBuildingsIterator.next(), actualBuildingsIterator.next());
		}
	}
	
	/**
	 * Mocks the given {@link Building}s by creating their JSON content and creating a MockResources
	 * (using {@link #mockBuildingsResource} so that {@link Buildings} attempts to read {@code
	 * buildings}.
	 */
	private void mockBuildings(List<Building> buildings) {
		StringWriter stringWriter = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(stringWriter);
		
		try {
			jsonWriter.beginArray();
			
			for (Building building : buildings) {
				writeBuilding(building, jsonWriter);
			}
		
			jsonWriter.endArray();
			jsonWriter.flush();
		} catch (Exception e) {
			fail("Received exception when writing JSON: " + e.getMessage());
		}
		
		mockBuildingsResource(stringWriter.toString());
	}
	
	/**
	 * Mocks the resources and context to provide the string {@code mockFileContents} instead of
	 * the raw resource contents.
	 */
	private void mockBuildingsResource(final String mockFileContents) {
		final MockResources mockResources = new MockResources() {
			@Override
			public InputStream openRawResource(int id) throws NotFoundException {
				if (id == Buildings.BUILDINGS_RAW_RESOURCE) {
					return new ByteArrayInputStream(mockFileContents.getBytes());
				}
				
				return super.openRawResource(id);
			}
			
			@Override
			public int getIdentifier(
					String name,
					String defType,
					String defPackage) {
				
				
				if (!defType.equals("drawable") || !defPackage.equals(PACKAGE_NAME)) {
					return super.getIdentifier(name, defType, defPackage);
				}
				
				try {
					return retrieveDrawableIdFromFakeDrawableName(name);
				} catch (Exception e) {
					return super.getIdentifier(name, defType, defPackage);
				}
			}
		};
		
		MockContext mockContext = new MockContext() {
			@Override
			public Resources getResources() {
				return mockResources;
			}
			
			@Override
			public String getPackageName() {
				return PACKAGE_NAME;
			}
		};
		
		setContext(mockContext);
	}
	
	/**
	 * Writes a {@link Building} to a {@link JsonWriter}.
	 */
	private static void writeBuilding(Building building, JsonWriter writer) throws IOException {
		writer.beginObject();
		
		writer.name("name");
		writer.value(building.getName());
		
		writer.name("shortName");
		writer.value(building.getShortName());
		
		if (building.getDescription() != null) {
			writer.name("description");
			writer.value(building.getDescription());
		}
		
		writer.name("location");
		writer.beginArray();

		writer.beginObject();
		writer.name("latitude");
		writer.value(building.getLocation().getNorthEastCorner().getLatitude());
		writer.name("longitude");
		writer.value(building.getLocation().getNorthEastCorner().getLongitude());
		writer.endObject();
		
		writer.beginObject();
		writer.name("latitude");
		writer.value(building.getLocation().getSouthWestCorner().getLatitude());
		writer.name("longitude");
		writer.value(building.getLocation().getSouthWestCorner().getLongitude());
		writer.endObject();
		
		// End location.
		writer.endArray();
		
		writePhotos(building.getPhotos(), writer);
		writeAttractions(building.getAttractions(), writer);

		writer.endObject();
	}
	
	/**
	 * Writes a list of {@link Photo}s to a {@link JsonWriter}.
	 */
	private static void writePhotos(List<Photo> photos, JsonWriter writer) throws IOException {
		writer.name("photos");

		writer.beginArray();
		for (Photo photo : photos) {
			writer.beginObject();
			
			writer.name("drawableName");
			writer.value(createFakeDrawableName(photo.getDrawableId()));
			
			if (photo.getDescription() != null) {
				writer.name("description");
				writer.value(photo.getDescription());
			}
			
			writer.endObject();
		}
		writer.endArray();
	}
	
	/**
	 * Writes a list of {@link Attraction}s for a {@link Building} onto a {@link JsonWriter}.
	 */
	private static void writeAttractions(List<Attraction> attractions, JsonWriter writer)
			throws IOException {

		writer.name("attractions");
		
		writer.beginArray();
		for (Attraction attraction : attractions) {
			writer.beginObject();
			
			writer.name("name");
			writer.value(attraction.getName());
			
			if (attraction.getDescription() != null) {
				writer.name("description");
				writer.value(attraction.getDescription());
			}
			
			writePhotos(attraction.getPhotos(), writer);
			
			writer.endObject();
		}
		writer.endArray();
	}
	
	/**
	 * Creates a fake name for a drawable ID.
	 */
	private static String createFakeDrawableName(int id) {
		return FAKE_DRAWABLE_PREFIX + id;
	}
	
	/**
	 * Returns a drawable ID from a fake name created by {@link #createFakeDrawableName}.
	 * 
	 * @throws IllegalArgumentException if {@code fakeDrawableName} is not a valid fake drawable
	 * name
	 */
	private static int retrieveDrawableIdFromFakeDrawableName(String fakeDrawableName) {
		String prefix = fakeDrawableName.substring(0, FAKE_DRAWABLE_PREFIX.length());

		if (!prefix.equals(FAKE_DRAWABLE_PREFIX)) {
			throw new IllegalArgumentException("Invalid fakeDrawableName");
		}
		
		return Integer.valueOf(fakeDrawableName.substring(FAKE_DRAWABLE_PREFIX.length()));
	}
}
