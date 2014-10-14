package org.iwuacm.iwuglasstour.view;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.Building;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Displays information about a building. Gets displayed when a user is inside a building.
 */
public class InsideView extends FrameLayout {
	
	private final TextView sampleText;
	
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
        
        this.sampleText = (TextView) findViewById(R.id.inside_sample_text);

    	// TODO: This is just here to show the view is working. Please remove.
        sampleText.setText("Inside view.");
    }
    
    public void setBuilding(Building building) {
    	// TODO: Implement.
    	
    	if (listener != null) {
    		listener.onChange();
    	}
    }

	public void setListener(ViewChangeListener listener) {
		this.listener = listener;
	}
}
