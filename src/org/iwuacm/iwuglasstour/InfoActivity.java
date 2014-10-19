package org.iwuacm.iwuglasstour;

import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.view.InfoView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Activity that contains the {@link InfoView}. Provides detailed information about a
 * {@link Building}.
 */
public class InfoActivity extends Activity {
	
	/**
	 * Key for the {@link Intent}'s building extra.
	 */
	public static final String BUILDING_MODEL = "buildingModel";
	
	/**
	 * Key for the {@link Intent}'s extra specifying whether the user is inside a building.
	 */
	public static final String IS_INSIDE = "isInside";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = getIntent();
		Building building = (Building) intent.getSerializableExtra(BUILDING_MODEL);
		boolean showDescriptionCardFirst = !intent.getBooleanExtra(IS_INSIDE, false);

		setContentView(new InfoView(building, showDescriptionCardFirst, this));
	}
}
