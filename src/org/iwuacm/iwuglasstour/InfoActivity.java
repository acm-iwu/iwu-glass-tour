package org.iwuacm.iwuglasstour;

import org.iwuacm.iwuglasstour.model.Building;

import android.app.Activity;
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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Building building = (Building) getIntent().getSerializableExtra(BUILDING_MODEL);

		setContentView(new InfoView(building, this));
	}
}
