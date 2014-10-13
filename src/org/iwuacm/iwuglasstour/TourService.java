package org.iwuacm.iwuglasstour;

import com.google.android.glass.timeline.LiveCard;
import com.google.android.glass.timeline.LiveCard.PublishMode;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.IBinder;

/**
 * Main application service. Manages cards and services.
 */
public class TourService extends Service {
	
	private static final String LIVE_CARD_TAG = "iwu-tour";
	
	private BuildingLocationManager buildingLocationManager;
	private LiveCard liveCard;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		LocationManager locationManager =
				(LocationManager) getSystemService(Context.LOCATION_SERVICE);
		this.buildingLocationManager = new BuildingLocationManager(locationManager);
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
            Intent menuIntent = new Intent(this, TourMenuActivity.class);
            menuIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            liveCard.setAction(PendingIntent.getActivity(this, 0, menuIntent, 0));
			liveCard.attach(this);
			
			// Only show card if started explicitly. It's possible it was restarted automatically,
			// and we don't want to show it in that case.
            liveCard.publish((intent == null) ? PublishMode.SILENT : PublishMode.REVEAL);
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
}
