package org.iwuacm.iwuglasstour;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TourMenuActivity extends Activity {
	
	private final Handler handler;
	
	public TourMenuActivity() {
		this.handler = new Handler();
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		openOptionsMenu();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.tour, menu);
		
		// TODO: Change building info option name to include the building name. Also, only display
		// the option if the user is not in a building.

		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.stop_this:
                // Stop the service at the end of the message queue for proper options menu
                // animation. This is only needed when starting a new Activity or stopping a Service
                // that published a LiveCard.
				handler.post(new Runnable() {
					@Override
					public void run() {
						stopService(new Intent(TourMenuActivity.this, TourService.class));
					}
				});

				return true;
			
			case R.id.building_info:
				// TODO: Implement.

				return true;
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onOptionsMenuClosed(Menu menu) {
		finish();
	}
}