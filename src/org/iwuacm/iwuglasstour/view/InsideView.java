package org.iwuacm.iwuglasstour.view;

import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.view.common.CardBuilders;

import com.google.android.glass.widget.CardBuilder;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Displays information about a building. Gets displayed when a user is inside a building.
 */
public class InsideView extends FrameLayout {
	
	private final Context context;
	
	private ViewChangeListener listener;
	
    public InsideView(Context context) {
        this(context, null, 0);
    }

    public InsideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InsideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        this.context = context;
    }
    
    public void setBuilding(Building building) {
    	removeAllViews();
    	CardBuilder cardBuilder = CardBuilders.createBuildingDescriptionCard(building, context);
    	addView(cardBuilder.getView());
    	if (listener != null)
    		listener.onChange();
    	
    }

	public void setListener(ViewChangeListener listener) {
		this.listener = listener;
	}
}
