package org.iwuacm.iwuglasstour;

import org.iwuacm.iwuglasstour.model.Building;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class TourMenuActivity extends Activity {
	
	/**
	 * The {@link Intent} extra that contains the active building to provide detailed information
	 * for.
	 */
	public static final String ACTIVE_BUILDING_MODEL = "activeBuildingModel";
	
	/**
	 * The {@link Intent} extra that specifies whether a user is inside a building.
	 */
	public static final String IS_INSIDE = "isInside";
	
	private final Handler handler;
	
	private Building activeBuilding;
	private boolean isInside;
	
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

		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		MenuItem infoItem = menu.findItem(R.id.building_info);

		Intent intent = getIntent();
		activeBuilding = (Building) intent.getSerializableExtra(ACTIVE_BUILDING_MODEL);
		isInside = intent.getBooleanExtra(IS_INSIDE, false);
		if (activeBuilding == null) {
			infoItem.setVisible(false);
		} else {
			infoItem.setTitle(activeBuilding.getName());
			infoItem.setVisible(true);
		}

		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.building_info:
				if (activeBuilding == null) {
					// Shouldn't be able to select this option.
					throw new IllegalStateException();
				}

				handler.post(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(TourMenuActivity.this, InfoActivity.class);
						intent.putExtra(InfoActivity.BUILDING_MODEL, activeBuilding);
						intent.putExtra(InfoActivity.IS_INSIDE, isInside);
						startActivity(intent);
					}
				});

				return true;
				
			case R.id.credits:
				handler.post(new Runnable() {
					@Override
					public void run() {
						Intent intent = new Intent(TourMenuActivity.this, CreditsActivity.class);
						startActivity(intent);
					}
				});
				
				return true;

			case R.id.stop_this:
				// Perform at end of message queue for proper menu animation.
				handler.post(new Runnable() {
					@Override
					public void run() {
						stopService(new Intent(TourMenuActivity.this, TourService.class));
					}
				});

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