package org.iwuacm.iwuglasstour.view;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.BuildingWithLocation;

import com.google.common.base.Optional;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Draws buildings in front and to the sides. Displays when the user is outside of a building.
 */
public class OutsideView extends RelativeLayout {

	private final Handler handler;
	private final BuildingLocationView frontView;
	private final BuildingLocationView leftView;
	private final BuildingLocationView rightView;
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
        
        this.handler = new Handler();

        this.frontView = (BuildingLocationView) findViewById(R.id.outside_front);
        this.leftView = (BuildingLocationView) findViewById(R.id.outside_left);
        this.rightView = (BuildingLocationView) findViewById(R.id.outside_right);

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

    	leftView.setBuildingWithLocation(Optional.fromNullable(left));
    	frontView.setBuildingWithLocation(Optional.fromNullable(front));
    	rightView.setBuildingWithLocation(Optional.fromNullable(right));

    	handleChange();
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
    
    @Override
    public boolean post(Runnable action) {
    	return handler.post(action);
    }
    
    /**
     * If no location is present, it displays the location message, otherwise it displays the
     * compass interference message if there is compass interference.
     */
    private void updateStatusMessage() {
    	final Integer newStatusString;

    	if (!hasLocation) {
    		newStatusString = R.string.location_unavailable;
    	} else if (hasCompassInterference) {
    		newStatusString = R.string.magnetic_interference;
    	} else {
    		newStatusString = null;
    	}
    	
    	if (newStatusString != statusString) {
    		statusString = newStatusString;
    		
    		handler.post(new Runnable() {
				@Override
				public void run() {
					if (newStatusString == null) {
						statusTextView.setText("");
					} else {
						statusTextView.setText(newStatusString);
					}
				}
			});
    		
    		handleChange();
    	}
    }
	
	/**
	 * The layout was not refreshed when {@link setBuilding} was called, and eventually I discovered
	 * adding this function would fix it. It has something to do with the fact we are adding an item
	 * to the view after it was initialized. Hopefully I've made my unfamiliarity of Android obvious
	 * by this comment. ;-) 
	 */
	private void redoLayout() {
		int measuredWidth =
				View.MeasureSpec.makeMeasureSpec(getWidth(), View.MeasureSpec.EXACTLY);
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
