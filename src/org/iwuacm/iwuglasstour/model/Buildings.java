package org.iwuacm.iwuglasstour.model;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.iwuacm.iwuglasstour.R;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

/**
 * A collection of {@link Building}s.
 */
public class Buildings {
	
	private static final String TAG = Buildings.class.getSimpleName();
	
	/**
	 * The resource for the buildings JSON file. This is visible for testing.
	 */
	static final int BUILDINGS_RAW_RESOURCE = R.raw.buildings;
	
	private static Buildings instance;

	private final List<Building> buildings;

	private Buildings(List<Building> buildings) {
		this.buildings = buildings;
	}

	public List<Building> getAll() {
		return buildings;
	}

	/**
	 * Reads in the buildings resource and returns an instance of {@link Buildings} containing them.
	 */
	public static Buildings getBuildings(Context context) {
		if (instance == null) {
			InputStream buildingsInputStream =
					context.getResources().openRawResource(R.raw.buildings);

			JsonReader reader = null;
			try {
				reader = getJsonReader(buildingsInputStream);
				instance = new Buildings(readBuildingsArray(reader, context));
			} catch (IOException e) {
				Log.wtf(TAG, "Could not read buildings: " + e.getMessage());
			} finally {
				try {
					reader.close();
				} catch (Exception e) {
					// Probably did not need to be closed.
				}
			}
		}
		
		return instance;
	}

	private static JsonReader getJsonReader(InputStream in) throws UnsupportedEncodingException {
		return new JsonReader(new InputStreamReader(in, "UTF-8"));
	}

	private static List<Building> readBuildingsArray(JsonReader reader, Context context)
			throws IOException {

		List<Building> buildings = new ArrayList<Building>();

		reader.beginArray();
		while (reader.hasNext()) {
			buildings.add(readBuilding(reader, context));
		}
		reader.endArray();
		return buildings;
	}

	private static Building readBuilding(JsonReader reader, Context context) throws IOException {
		Building.Builder myBuilder=Building.builder();
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			if (name.equals("name")) {
				String buildingName = reader.nextString();
				myBuilder.withName(buildingName);
			} else if (name.equals("shortName")) {
				String shortName = reader.nextString();
				myBuilder.withShortName(shortName);
			} else if (name.equals("description")) {
				String description = reader.nextString();
				myBuilder.withDescription(description);
			} else if (name.equals("location")) {
				RectangularLocation location = readLocation(reader);
				myBuilder.withLocation(location);
			} else if (name.equals("photos") && reader.peek() != JsonToken.NULL) {
				List<Photo> photos = readPhotosArray(reader, context);
				for (Photo photo : photos) {
					myBuilder.addPhoto(photo);
				}
			} else if (name.equals("attractions") && reader.peek() != JsonToken.NULL){
				List<Attraction> attractions = readAttractionsArray(reader, context);
				for (Attraction attraction: attractions){
					myBuilder.addAttraction(attraction);
				}
			} else {
				reader.skipValue();
			}
		}
		reader.endObject();

		Building myBuilding=myBuilder.build();
		return myBuilding;
	}

	private static List<Attraction> readAttractionsArray(JsonReader reader, Context context)
			throws IOException {

		List<Attraction> result=new ArrayList<Attraction>();
		
		reader.beginArray();
		while (reader.hasNext()) {
			Attraction.Builder builder=Attraction.builder();
			
			reader.beginObject();
			while(reader.hasNext()){
				String name = reader.nextName();
				if (name.equals("name")) {
					builder.withName(reader.nextString());
				}  else if (name.equals("description")) {
					builder.withDescription(reader.nextString());
				}  else if (name.equals("photos") && reader.peek() != JsonToken.NULL) {
					List<Photo> photos = readPhotosArray(reader, context);
					for (Photo photo : photos) {
						builder.addPhoto(photo);
					}
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
			
			result.add(builder.build());
		}

		reader.endArray();
		return result;
	}

	private static List<Photo> readPhotosArray(JsonReader reader, Context context)
			throws IOException {

		List<Photo> photos = new ArrayList<Photo>();
		
		reader.beginArray();
		while (reader.hasNext()) {
			Photo.Builder builder = Photo.builder();
			
			reader.beginObject();
			while(reader.hasNext()){
				String name = reader.nextName();
				if (name.equals("drawableName")) {
					builder.withDrawableId(getPhotoId(reader.nextString(), context));
				} else if (name.equals("description")) {
					builder.withDescription(reader.nextString());
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
			
			photos.add(builder.build());
		}
		reader.endArray();
		
		return photos;
	}
	
	private static int getPhotoId(String drawableName, Context context) {		
		return context.getResources()
				.getIdentifier(drawableName, "drawable", context.getPackageName());
	}

	private static RectangularLocation readLocation(JsonReader reader) throws IOException {
		List<Double> latitude=new ArrayList<Double>();
		List<Double> longitude=new ArrayList<Double>();

		reader.beginArray();
		while (reader.hasNext()) {
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if (name.equals("latitude")) {
					latitude.add(reader.nextDouble());
				} else if (name.equals("longitude")) {
					longitude.add(reader.nextDouble());
				} else {
					reader.skipValue();
				}
			}
			reader.endObject();
		}
		reader.endArray();

		Location a=new Location(latitude.get(0), longitude.get(0));
		Location b=new Location(latitude.get(0), longitude.get(1));
		Location c=new Location(latitude.get(1), longitude.get(0));
		Location d=new Location(latitude.get(1), longitude.get(1));
		return new RectangularLocation(a,b,c,d);
	}
}
