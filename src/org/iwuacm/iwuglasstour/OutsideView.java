package org.iwuacm.iwuglasstour;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Draws a viewfinder with an overlay of buildings in front and to the sides. Displays when the user
 * is outside of a building.
 */
public class OutsideView extends View {

    public OutsideView(Context context) {
        this(context, null, 0);
    }

    public OutsideView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OutsideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
