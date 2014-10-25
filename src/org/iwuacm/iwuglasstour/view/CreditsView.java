package org.iwuacm.iwuglasstour.view;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.Credit;
import org.iwuacm.iwuglasstour.model.Credits;
import org.iwuacm.iwuglasstour.model.Photo;

import com.google.android.glass.widget.CardBuilder;
import com.google.android.glass.widget.CardScrollAdapter;
import com.google.android.glass.widget.CardScrollView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Displays a multiple card view with credits for the app.
 */
public class CreditsView extends CardScrollView {

	private static final Comparator<Credit> CREDIT_LAST_NAME_COMPARATOR = new Comparator<Credit>() {
		@Override
		public int compare(Credit lhs, Credit rhs) {
			return String.CASE_INSENSITIVE_ORDER.compare(lhs.getLastName(), rhs.getLastName());
		}
	};

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
	
	public CreditsView(Credits credits, Context context) {
		this(credits, context, null, 0);
	}
	
	public CreditsView(Credits credits, Context context, AttributeSet attrs) {
		this(credits, context, attrs, 0);
	}
	
	public CreditsView(Credits credits, Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		this.cards = createCards(credits, context);
		
		setAdapter(cardScrollAdapter);
		activate();
	}
	
	/**
	 * Creates the {@link CardBuilder}s for the given {@link Credits} that go into the scroll
	 * adapter.
	 */
	private List<CardBuilder> createCards(Credits credits, Context context) {
		List<CardBuilder> cards = new ArrayList<CardBuilder>();
		
		List<Credit> creditsSortedAlphabetically = new ArrayList<Credit>(credits.getAll());
		Collections.sort(creditsSortedAlphabetically, CREDIT_LAST_NAME_COMPARATOR);
		for (Credit credit : creditsSortedAlphabetically) {
			cards.add(createCreditCard(credit, context));
		}

		return cards;
	}
	
	/**
	 * Creates a {@link CardBuilder} for a {@link Credit}.
	 */
	private CardBuilder createCreditCard(Credit credit, Context context) {
		Photo photo = credit.getPhoto();

		return new CardBuilder(context, CardBuilder.Layout.AUTHOR)
					.setIcon((photo == null) ? R.drawable.ic_person : photo.getDrawableId())
					.setHeading(credit.getFullName())
					.setText(credit.getContributions());
	}
}
