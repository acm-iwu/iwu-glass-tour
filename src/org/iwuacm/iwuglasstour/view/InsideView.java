package org.iwuacm.iwuglasstour.view;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.view.common.CardBuilders;

import com.google.android.glass.widget.CardBuilder;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Displays information about a building. Gets displayed when a user is inside a building.
 */
public class InsideView extends FrameLayout {
	
	private final Context context;
	private final Handler handler;
	
	private ViewChangeListener listener;
	
    public InsideView(Context context) {
        this(context, null, 0);
    }

    public InsideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.inside, this);

        this.context = context;
        this.handler = new Handler();
    }
    
    public void setBuilding(final Building building) {
    	post(new Runnable() {
			@Override
			public void run() {
				removeAllViews();

				CardBuilder cardBuilder = CardBuilders.createBuildingDescriptionCard(building, context);
				addView(cardBuilder.getView());
			}
		});
    	
    	handleChange();
    }

	public void setListener(ViewChangeListener listener) {
		this.listener = listener;
	}
	
	@Override
	public boolean post(Runnable action) {
		return handler.post(action);
	}
	
	/**
	 * The layout was not refreshed when {@link setBuilding} was called, and eventually I discovered
	 * adding this function would fix it. It has something to do with the fact we are adding an item
	 * to the view after it was initialized. Hopefully I've made my unfamiliarity of Android obvious
	 * by this comment. ;-) 
	 */
	private void redoLayout() {
		int measuredWidth = View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY);
		int measuredHeight =
				View.MeasureSpec.makeMeasureSpec(getHeight(), View.MeasureSpec.EXACTLY);

    	measure(measuredWidth, measuredHeight);
    	layout(0, 0, getMeasuredWidth(), getMeasuredHeight());
	}
    
	/**
	 * Redoes the layout and notifies any listeners that there has been a change to the layout.
	 */
    private void handleChange() {
    	post(new Runnable() {
			@Override
			public void run() {
				redoLayout();

				if (listener != null) {
					listener.onChange();
				}
			}
		});
    }
}
