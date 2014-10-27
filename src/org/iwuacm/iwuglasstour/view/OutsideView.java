package org.iwuacm.iwuglasstour.view;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.Building;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Draws buildings in front and to the sides. Displays when the user is outside of a building.
 */
public class OutsideView extends RelativeLayout {

	private final TextView outsideFront;
	private final TextView outsideLeft;
	private final TextView outsideRight;
	
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
        
        this.outsideFront = (TextView) findViewById(R.id.outside_front);
        this.outsideLeft = (TextView) findViewById(R.id.outside_left);
        this.outsideRight = (TextView) findViewById(R.id.outside_right);
    }
    
    public void setNearbyBuildings(Building left, Building front, Building right) {
    	outsideFront.setText(front.getName());
    	outsideLeft.setText(left.getShortName());
    	outsideRight.setText(right.getShortName());

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
