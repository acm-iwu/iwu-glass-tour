package org.iwuacm.iwuglasstour;

import org.iwuacm.iwuglasstour.model.Building;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Displays detailed information about a building.
 */
public class InfoView extends FrameLayout {
	
	private final Building building;
	private final TextView sampleText;
	
    public InfoView(Building building, Context context) {
        this(building, context, null, 0);
    }

    public InfoView(Building building, Context context, AttributeSet attrs) {
        this(building, context, attrs, 0);
    }

    public InfoView(Building building, Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.info, this);
        
        this.building = building;
        this.sampleText = (TextView) findViewById(R.id.info_sample_text);
        
        buildLayout();
    }
    
    private void buildLayout() {
    	// TODO: This is just here to show the view is working. Please remove.
        sampleText.setText(building.getName());
    }
}