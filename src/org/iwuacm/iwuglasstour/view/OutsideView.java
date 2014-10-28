package org.iwuacm.iwuglasstour.view;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.BuildingWithLocation;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Draws buildings in front and to the sides. Displays when the user is outside of a building.
 */
public class OutsideView extends RelativeLayout {

	private final TextView frontTextView;
	private final TextView leftTextView;
	private final TextView rightTextView;
	private final TextView statusTextView;
	
	private ViewChangeListener listener;
	private boolean hasCompassInterference;
	private boolean hasLocation;
	
	/**
	 * The resource ID of the currently displayed status string.
	 */
	private Integer statusString;

    public OutsideView(Context context) {
        this(context, null, 0);
    }

    public OutsideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutsideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.outside, this);
        
        this.frontTextView = (TextView) findViewById(R.id.outside_front);
        this.leftTextView = (TextView) findViewById(R.id.outside_left);
        this.rightTextView = (TextView) findViewById(R.id.outside_right);
        this.statusTextView = (TextView) findViewById(R.id.outside_status);
        
        hasCompassInterference = false;
        hasLocation = true;
        statusString = null;
    }
    
    /**
     * Updates the nearby buildings. Handles the case where the buildings are not present.
     */
    public void setNearbyBuildings(
    		@Nullable BuildingWithLocation left,
    		@Nullable BuildingWithLocation front,
    		@Nullable BuildingWithLocation right) {

    	frontTextView.setText((front == null) ? "" : front.getBuilding().getName());
    	leftTextView.setText((left == null) ? "" : left.getBuilding().getShortName());
    	rightTextView.setText((right == null) ? "" : right.getBuilding().getShortName());

    	notifyChange();
    }
    
    public void setHasCompassInterference(boolean hasCompassInterference) {
    	if (hasCompassInterference == this.hasCompassInterference) {
    		return;
    	}
    	
    	this.hasCompassInterference = hasCompassInterference;
    	
    	updateStatusMessage();
    }

	public void setHasLocation(boolean hasLocation) {
		if (hasLocation == this.hasLocation) {
			return;
		}
		
		this.hasLocation = hasLocation;
		
		updateStatusMessage();
	}
    
    public void setListener(ViewChangeListener listener) {
    	this.listener = listener;
    }
    
    /**
     * If no location is present, it displays the location message, otherwise it displays the
     * compass interference message if there is compass interference.
     */
    private void updateStatusMessage() {
    	Integer newStatusString = null;

    	if (!hasLocation) {
    		newStatusString = R.string.location_unavailable;
    	} else if (hasCompassInterference) {
    		newStatusString = R.string.magnetic_interference;
    	}
    	
    	if (newStatusString != statusString) {
    		statusString = newStatusString;
    		
    		if (newStatusString == null) {
    			statusTextView.setText("");
    		} else {
    			statusTextView.setText(newStatusString);
    		}
    		
    		notifyChange();
    	}
    }
    
    private void notifyChange() {
    	if (listener != null) {
    		listener.onChange();
    	}
    }
}
