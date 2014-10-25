package org.iwuacm.iwuglasstour.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.common.ResourceHelpers;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

/**
 * A collection of {@link Credit}s.
 */
public class Credits {

	private static final String TAG = Credits.class.getSimpleName();
	
	/**
	 * The resource for the credits JSON file.
	 */
	private static final int CREDITS_RAW_RESOURCE = R.raw.credits;
	
	private static Credits instance;

	private final List<Credit> credits;

	private Credits(List<Credit> credits) {
		this.credits = credits;
	}

	public List<Credit> getAll() {
		return credits;
	}

	/**
	 * Reads in the credits resource and returns an instance of {@link Credits} containing them.
	 */
	public static Credits getCredits(Context context) {
		if (instance == null) {
			try {
				String json = getResourceJson(context);
				instance = new Credits(readCredits(json, context));
			} catch (Exception e) {
				Log.wtf(TAG, "Could not read credits: " + e.getMessage());
			}
		}
		
		return instance;
	}

	/**
	 * Returns a String with the JSON from the resource.
	 */
	private static String getResourceJson(Context context) throws IOException {
		InputStream inputStream = context.getResources().openRawResource(CREDITS_RAW_RESOURCE);
		
		Writer writer = new StringWriter();
		
		try {
			Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			
			int n;
			char[] buffer = new char[8192];
			while ((n = reader.read(buffer)) != -1) {
				writer.write(buffer, 0, n);
			}
		} finally {
			inputStream.close();
		}
		
		return writer.toString();
	}

	private static List<Credit> readCredits(String json, Context context) throws JSONException {
		List<Credit> credits = new ArrayList<Credit>();
		
		JSONArray array = new JSONArray(json);
		
		for (int i = 0; i < array.length(); i++) {
			Credit credit = readCredit(array.getJSONObject(i), context);
			credits.add(credit);
		}

		return credits;
	}

	private static Credit readCredit(JSONObject object, Context context) throws JSONException {
		Credit.Builder builder = Credit.builder()
				.withFirstName(object.getString("firstName"))
				.withLastName(object.getString("lastName"))
				.withContributions(object.getString("contributions"));
		
		if (object.has("photo")) {
			JSONObject photoJsonObject = object.getJSONObject("photo");

			int drawableId =
					ResourceHelpers.getPhotoId(photoJsonObject.getString("drawableName"), context);
			Photo.Builder photoBuilder = Photo.builder()
					.withDrawableId(drawableId);
			
			if (photoJsonObject.has("description")) {
				photoBuilder.withDescription(photoJsonObject.getString("description"));
			}
			
			builder.withPhoto(photoBuilder.build());
		}
		
		return builder.build();
	}
}
