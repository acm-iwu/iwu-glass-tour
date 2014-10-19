package org.iwuacm.iwuglasstour;

import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.model.Buildings;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.IBinder;

/**
 * Main application service. Manages cards and services.
 */
public class TourService extends Service {
	
	private static final String LIVE_CARD_TAG = "iwu-tour";
	
	private final BuildingLocationManager.Listener buildingLocationListener =
			new BuildingLocationManager.Listener() {
				@Override
				public void onNearbyBuildingsChange(
						Building left,
						Building front,
						Building right) {
					
					if (!buildingLocationManager.isInsideBuilding()) {
						setActiveBuilding(front);
					}
				}
				
				@Override
				public void onExitBuilding() {
					setActiveBuilding(buildingLocationManager.getFrontBuilding());
					setIsInside(false);
				}
				
				@Override
				public void onEnterBuilding(Building building) {
					setActiveBuilding(building);
					setIsInside(true);
				}

				@Override
				public void onCompassInterference(boolean hasInterference) {
					// Do not care here.
				}
			};
	
	private BuildingLocationManager buildingLocationManager;
	private LiveCard liveCard;
	private Intent menuIntent;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		Buildings buildings = Buildings.getBuildings();
		LocationManager locationManager =
				(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		SensorManager sensorManager =
				(SensorManager) getSystemService(Context.SENSOR_SERVICE);
		buildingLocationManager =
				new BuildingLocationManager(buildings, sensorManager, locationManager);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// No need to interact with app from outside here.
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (liveCard == null) {
			liveCard = new LiveCard(this, LIVE_CARD_TAG);
			
			TourRenderer renderer = new TourRenderer(this, buildingLocationManager);
			liveCard.setDirectRenderingEnabled(true).getSurfaceHolder().addCallback(renderer);
			
            // Display the options menu on tap.
            menuIntent = new Intent(this, TourMenuActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

			setActiveBuilding(
					buildingLocationManager.isInsideBuilding()
						? buildingLocationManager.getBuildingInside()
						: buildingLocationManager.getFrontBuilding(),
					false);
			setIsInside(buildingLocationManager.isInsideBuilding(), false);
            liveCard.setAction(updatePendingIntent());

			liveCard.attach(this);
			
			// Only show card if started explicitly. It's possible it was restarted automatically,
			// and we don't want to show it in that case.
            liveCard.publish((intent == null) ? PublishMode.SILENT : PublishMode.REVEAL);

			buildingLocationManager.addListener(buildingLocationListener);
		} else {
			liveCard.navigate();
		}
		
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		if ((liveCard != null) && liveCard.isPublished()) {
			liveCard.unpublish();
			liveCard = null;
		}
		
		buildingLocationManager.stopTracking();
		buildingLocationManager = null;

		super.onDestroy();
	}
	
	/**
	 * Changes the {@link Building} that the user can get detailed information for and updates the
	 * pending intent, so that the menu will reflect the changes.
	 */
	private void setActiveBuilding(Building activeBuilding) {
		setActiveBuilding(activeBuilding, true);
	}

	private void setActiveBuilding(Building activeBuilding, boolean updatePendingIntent) {
		if (activeBuilding == null) {
			menuIntent.removeExtra(TourMenuActivity.ACTIVE_BUILDING_MODEL);
		} else {
			menuIntent.putExtra(TourMenuActivity.ACTIVE_BUILDING_MODEL, activeBuilding);
		}

		if (updatePendingIntent) {
			updatePendingIntent();
		}
	}
	
	/**
	 * Changes whether the user is inside for the pending intent, so that the menu will reflect the
	 * changes.
	 */
	private void setIsInside(boolean isInside) {
		setIsInside(isInside, true);
	}

	private void setIsInside(boolean isInside, boolean updatePendingIntent) {
		menuIntent.putExtra(TourMenuActivity.IS_INSIDE, isInside);

		if (updatePendingIntent) {
			updatePendingIntent();
		}
	}
	
	/**
	 * Updates and returns a {@link PendingIntent} from the {@link #menuIntent}.
	 */
	private PendingIntent updatePendingIntent() {
		// FLAG_UPDATE_CURRENT makes sure that the previous intent is overriden.
		return PendingIntent.getActivity(this, 0, menuIntent, PendingIntent.FLAG_UPDATE_CURRENT);
	}
}
