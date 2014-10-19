package org.iwuacm.iwuglasstour.view.common;

import java.util.List;

import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.model.Photo;

import android.content.Context;

import com.google.android.glass.widget.CardBuilder;

/**
 * Common functions and cards for {@link CardBuilder}.
 */
public class CardBuilders {

	/**
	 * Creates a card that describes a {@link Building} with its name, description, and photos.
	 */
	public static CardBuilder getBuildingDescriptionCard(Building building, Context context) {
		CardBuilder card = new CardBuilder(context, CardBuilder.Layout.TEXT)
				.setFootnote(building.getName());
		
		if (building.getDescription() != null) {
			card.setText(building.getDescription());
		}

		addPhotos(building.getPhotos(), card);
		
		return card;
	}
	
	/**
	 * Adds photos to a {@link CardBuilder}.
	 */
	public static void addPhotos(List<Photo> photos, CardBuilder card) {
		for (Photo photo : photos) {
			card.addImage(photo.getDrawableId());
		}
	}
}
