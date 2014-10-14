package org.iwuacm.iwuglasstour.view;

import java.util.ArrayList;
import java.util.List;

import org.iwuacm.iwuglasstour.model.Building;

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
	};
	
	public InfoView(Building building, Context context) {
		this(building, context, null, 0);
	}
	
	public InfoView(Building building, Context context, AttributeSet attrs) {
		this(building, context, attrs, 0);
	}
	
	public InfoView(Building building, Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		this.cards = createCards(building, context);
		
		setAdapter(cardScrollAdapter);
		activate();
	}
	
	/**
	 * Creates the {@link CardBuilder}s that go into the scroll adapter for the provided {@link
	 * Building}.
	 */
	private List<CardBuilder> createCards(Building building, Context context) {
		List<CardBuilder> cards = new ArrayList<CardBuilder>();
		
		// TODO: Finish cards.
		cards.add(new CardBuilder(context, CardBuilder.Layout.TEXT)
				.setText(building.getName()));
		
		return cards;
	}
}
