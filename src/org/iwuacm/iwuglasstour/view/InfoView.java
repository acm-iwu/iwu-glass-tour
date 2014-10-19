package org.iwuacm.iwuglasstour.view;

import java.util.ArrayList;
import java.util.List;

import org.iwuacm.iwuglasstour.model.Attraction;
import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.model.Photo;
import org.iwuacm.iwuglasstour.view.common.CardBuilders;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Displays a multiple card view with detailed information about a building.
 */
public class InfoView extends CardScrollView {

	private final List<CardBuilder> cards;
	private final boolean showDescriptionCardFirst;
	
	private final CardScrollAdapter cardScrollAdapter = new CardScrollAdapter() {
		@Override
		public int getCount() {
			return cards.size();
		}

		@Override
		public Object getItem(int index) {
			return cards.get(index);
		}

		@Override
		public int getPosition(Object item) {
			return cards.indexOf(item);
		}

		@Override
		public View getView(int index, View convertView, ViewGroup parent) {
			return cards.get(index).getView(convertView, parent);
		}
		
		@Override
		public int getHomePosition() {
			return showDescriptionCardFirst ? 0 : 1;
		}
	};
	
	public InfoView(Building building, boolean showDescriptionCardFirst, Context context) {
		this(building, showDescriptionCardFirst, context, null, 0);
	}
	
	public InfoView(
			Building building,
			boolean showDescriptionCardFirst,
			Context context,
			AttributeSet attrs) {

		this(building, showDescriptionCardFirst, context, attrs, 0);
	}
	
	public InfoView(
			Building building,
			boolean showDescriptionCardFirst,
			Context context,
			AttributeSet attrs,
			int defStyle) {

		super(context, attrs, defStyle);
		
		this.cards = createCards(building, context);
		this.showDescriptionCardFirst = showDescriptionCardFirst;
		
		setAdapter(cardScrollAdapter);
		activate();
		setSelection(cardScrollAdapter.getHomePosition());
	}
	
	/**
	 * Creates the {@link CardBuilder}s that go into the scroll adapter for the provided {@link
	 * Building}.
	 */
	private List<CardBuilder> createCards(Building building, Context context) {
		List<CardBuilder> cards = new ArrayList<CardBuilder>();
		cards.add(CardBuilders.getBuildingDescriptionCard(building, context));

		List<CardBuilder> buildingPhotos = new ArrayList<CardBuilder>();
		for (Photo photo : building.getPhotos()) {
			if (photo.getDescription() != null) {
				CardBuilder captionedPhoto = new CardBuilder(context, CardBuilder.Layout.CAPTION)
						.addImage(photo.getDrawableId())
						.setText(photo.getDescription())
						.setFootnote(building.getName());
				buildingPhotos.add(captionedPhoto);
			}
		}

		cards.addAll(createAttractions(building.getAttractions(), context));
		cards.addAll(buildingPhotos);

		return cards;
	}
	/**
	 * Returns a list of {@link Attraction} cards
	 * @param attractions list of attractions for that building 
	 * @param context pass in a context
	 */
	private List<CardBuilder> createAttractions(List<Attraction> attractions, Context context){
		List<CardBuilder> attractionCards = new ArrayList<CardBuilder>();
		
		for(Attraction attraction : attractions){
			CardBuilder.Layout layout =
					attraction.getPhotos().isEmpty()
							? CardBuilder.Layout.TEXT
							: CardBuilder.Layout.COLUMNS;

			CardBuilder newCard = new CardBuilder(context, layout)
					.setFootnote(attraction.getName());

			if (attraction.getDescription() != null){
				newCard.setText(attraction.getDescription());
			}
			
			CardBuilders.addPhotos(attraction.getPhotos(), newCard);

			attractionCards.add(newCard);
		}
		return attractionCards;
	}
}

