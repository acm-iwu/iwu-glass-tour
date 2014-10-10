package org.iwuacm.iwuglasstour;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Displays information about a building. Gets displayed when a user is inside a building or asks
 * for information about a building.
 */
public class InfoView extends View {

    public InfoView(Context context) {
        this(context, null, 0);
    }

    public InfoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}