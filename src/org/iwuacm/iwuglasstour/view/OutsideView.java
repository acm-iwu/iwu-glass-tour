package org.iwuacm.iwuglasstour.view;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.Building;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Draws a viewfinder with an overlay of buildings in front and to the sides. Displays when the user
 * is outside of a building.
 */
public class OutsideView extends RelativeLayout {

	private final TextView sampleText;
	
	private ViewChangeListener listener;

    public OutsideView(Context context) {
        this(context, null, 0);
    }

    public OutsideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutsideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.outside, this);
        
        this.sampleText = (TextView) findViewById(R.id.outside_front);

    	// TODO: This is just here to show the view is working. Please remove.
    	sampleText.setText("Outside view.");
    }
    
    /**
     * Starts rendering the view, updating it regularly.
     */
    // TODO: Implement.
    public void startRendering() {}
    
    /**
     * Stops rendering the view. Stops any regularly scheduled rendering.
     */
    // TODO: Implement.
    public void stopRendering() {}
    
    public void setNearbyBuildings(Building left, Building front, Building right) {
    	// TODO: Implement.
    	
    	if (listener != null) {
    		listener.onChange();
    	}
    }
    
    // TODO: Implement.
    public void setHasCompassInterference(boolean hasInterference) {}
    
    public void setListener(ViewChangeListener listener) {
    	this.listener = listener;
    }
}
