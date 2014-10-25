package org.iwuacm.iwuglasstour;

import org.iwuacm.iwuglasstour.model.Credits;
import org.iwuacm.iwuglasstour.view.CreditsView;

import android.app.Activity;
import android.os.Bundle;

/**
 * Activity that contains the {@link CreditsView}.
 */
public class CreditsActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Credits credits = Credits.getCredits(getApplicationContext());
		setContentView(new CreditsView(credits, this));
	}
}
