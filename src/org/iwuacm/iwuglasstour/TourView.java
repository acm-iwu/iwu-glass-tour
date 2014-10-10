package org.iwuacm.iwuglasstour;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Displays a viewfinder with an overlay of buildings in front and to the sides.
 */
public class TourView extends View {

    public TourView(Context context) {
        this(context, null, 0);
    }

    public TourView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TourView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
