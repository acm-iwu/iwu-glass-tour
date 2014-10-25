package org.iwuacm.iwuglasstour.model.common;

import android.content.Context;
import android.content.res.Resources;

/**
 * Helpers for {@link Resources}.
 */
public class ResourceHelpers {

	/**
	 * Returns the id of a photo when given a drawable name.
	 */
	public static int getPhotoId(String drawableName, Context context) {		
		return context.getResources()
				.getIdentifier(drawableName, "drawable", context.getPackageName());
	}
}
