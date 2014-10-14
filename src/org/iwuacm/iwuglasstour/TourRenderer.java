package org.iwuacm.iwuglasstour;

import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.view.InsideView;
import org.iwuacm.iwuglasstour.view.OutsideView;
import org.iwuacm.iwuglasstour.view.ViewChangeListener;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import com.google.android.glass.timeline.DirectRenderingCallback;

/**
 * Surface callback that provides rendering logic for the tour card. This takes care of both the
 * outside building and building information views. Also manages location tracking
 * lifespan.
 */
public class TourRenderer implements DirectRenderingCallback {
	
	private static final String TAG = TourRenderer.class.getSimpleName();
	
	private final BuildingLocationManager buildingLocationManager;
	private final OutsideView outsideView;
	private final InsideView infoView;
	
	private final BuildingLocationManager.Listener buildingLocationListener =
			new BuildingLocationManager.Listener() {
				@Override
				public void onNearbyBuildingsChange(
						Building left,
						Building front,
						Building right) {
					
					// TODO: Implement.
				}
				
				@Override
				public void onExitBuilding() {
					isInside = false;

					// TODO: Implement.

					outsideView.startRendering();
				}
				
				@Override
				public void onEnterBuilding(Building building) {
					isInside = true;
					outsideView.stopRendering();
					
					// TODO: Implement.
				}
			};
	
	private boolean renderingPaused;
	private boolean isRendering;
	private SurfaceHolder holder;
	private boolean isInside;
	
	public TourRenderer(Context context, BuildingLocationManager buildingLocationManager) {
		this.buildingLocationManager = buildingLocationManager;
		this.outsideView = new OutsideView(context);
		this.infoView = new InsideView(context);
		
		outsideView.setListener(createViewChangeListenerFor(outsideView));
		infoView.setListener(createViewChangeListenerFor(infoView));

		renderingPaused = false;
		isRendering = false;
		isInside = false;
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		renderingPaused = false;
		this.holder = holder;
		updateRenderingState();
	}

	@Override
	public void surfaceChanged(
			SurfaceHolder holder,
			int format,
			int width,
			int height) {
		
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
        
        infoView.measure(measuredWidth, measuredHeight);
        infoView.layout(
        		0, 0, infoView.getMeasuredWidth(), infoView.getMeasuredHeight());

        outsideView.measure(measuredWidth, measuredHeight);
        outsideView.layout(
        		0, 0, outsideView.getMeasuredWidth(), outsideView.getMeasuredHeight());

		draw();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		this.holder = null;
		updateRenderingState();
	}

	@Override
	public void renderingPaused(SurfaceHolder holder, boolean renderingPaused) {
		this.renderingPaused = renderingPaused;
		updateRenderingState();
	}
	
	/**
	 * Starts or stops rendering depending on the state (provided by {@link #renderingPaused} and
	 * {@link #holder}).
	 */
	private void updateRenderingState() {
		boolean shouldRender = (holder != null) && !renderingPaused;
		
		if (shouldRender == isRendering) {
			return;
		}
		isRendering = shouldRender;
		
		if (shouldRender) {
			buildingLocationManager.startTracking();
			buildingLocationManager.addListener(buildingLocationListener);

			isInside = buildingLocationManager.isInsideBuilding();
			if (!isInside) {
				outsideView.setNearbyBuildings(
						buildingLocationManager.getLeftBuilding(),
						buildingLocationManager.getFrontBuilding(),
						buildingLocationManager.getRightBuilding());
				outsideView.startRendering();
			}
		} else {
			buildingLocationManager.stopTracking();
			buildingLocationManager.removeListener(buildingLocationListener);

			outsideView.stopRendering();
		}
	}
	
	/**
	 * Repaints the active layout.
	 */
	private void draw() {
		Canvas canvas = null;
		try {
			canvas = holder.lockCanvas();
		} catch (RuntimeException e) {
            Log.d(TAG, "lockCanvas failed", e);
		}
		
		if (canvas == null) {
			return;
		}
		
		getActiveView().draw(canvas);
		
		try {
			holder.unlockCanvasAndPost(canvas);
		} catch (RuntimeException e) {
			Log.d(TAG, "unlockCanvasAndPost failed", e);
		}
	}
	
	/**
	 * Returns the active view depending on whether the user is inside or outside a building.
	 */
	private View getActiveView() {
        return isInside ? infoView : outsideView;
	}
	
	/**
	 * Creates a view change listener such that it will call {@link #draw} only if the view provided
	 * is the one that has been changed. This wastes potential redrawing for an inactive/invisible
	 * view.
	 */
	private ViewChangeListener createViewChangeListenerFor(final View view) {
		return new ViewChangeListener() {
			@Override
			public void onChange() {
				if ((isInside && (view == infoView)) || (!isInside && (view == outsideView))) {
					draw();
				}
			}
		};
	}
}
