package org.iwuacm.iwuglasstour.view;

import java.text.DecimalFormat;
import java.util.List;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.Building;
import org.iwuacm.iwuglasstour.model.BuildingWithLocation;
import org.iwuacm.iwuglasstour.model.Photo;
import org.iwuacm.iwuglasstour.util.MathUtils;

import com.google.common.base.Optional;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * A view for a building in the {@link OutsideView}. Includes a photo of the building, its short
 * name, and how far it is away.
 */
public class BuildingLocationView extends RelativeLayout {
	
	/**
	 * When formatting a distance, if it is less than this many miles away, it will display in feet.
	 */
	private static final double MILES_THRESHOLD = 0.1;
	
	/**
	 * The angle relative to up that the arrow image is rotated at. 
	 */
	private static final double ARROW_ROTATION_ANGLE = 90.0;
	
	private static final DecimalFormat MILES_FORMAT = new DecimalFormat("0.00");
	private static final DecimalFormat FEET_FORMAT = new DecimalFormat("0");
	
	private final Handler handler;

	private final ImageView photoView;
	private final TextView nameView;
	private final TextView distanceView;
	private final ImageView arrowView;

	private final String milesUnit;
	private final String feetUnit;

	public BuildingLocationView(Context context) {
		this(context, null, 0);
	}

	public BuildingLocationView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BuildingLocationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater.from(context).inflate(R.layout.building_location, this);
		
		this.handler = new Handler();
		
		this.photoView = (ImageView) findViewById(R.id.building_location_photo);
		this.nameView = (TextView) findViewById(R.id.building_location_name);
		this.distanceView = (TextView) findViewById(R.id.building_location_distance);
		this.arrowView = (ImageView) findViewById(R.id.building_location_arrow_right);
		
		this.milesUnit = context.getResources().getString(R.string.miles_unit);
		this.feetUnit = context.getResources().getString(R.string.feet_unit);
		
		setVisibility(View.GONE);
	}
	
	/**
	 * Updates the building to be displayed with its relative location.
	 */
	public void setBuildingWithLocation(
			Optional<BuildingWithLocation> buildingWithLocationOptional) {

		if (!buildingWithLocationOptional.isPresent()) {
			post(new Runnable() {
				@Override
				public void run() {
					setVisibility(View.GONE);
				}
			});

			return;
		}
		
		BuildingWithLocation buildingWithLocation = buildingWithLocationOptional.get();
		Building building = buildingWithLocation.getBuilding();

		List<Photo> photos = building.getPhotos();
		final int photoResource =
				photos.isEmpty() ? R.drawable.ic_building : photos.get(0).getDrawableId();
		
		final String nameText = building.getShortName();
		final String distanceText = formatDistance(buildingWithLocation.getDistance());

		final float arrowRotation =
				(float) (buildingWithLocation.getHeadingOffset() - ARROW_ROTATION_ANGLE);

		post(new Runnable() {
			@Override
			public void run() {
				photoView.setImageResource(photoResource);

				nameView.setText(nameText);
				distanceView.setText(distanceText);
				
				arrowView.setRotation(arrowRotation);

				setVisibility(View.VISIBLE);
			}
		});
	}
	
	@Override
	public boolean post(Runnable action) {
		return handler.post(action);
	}
	
	// TODO: Internationalize.
	private String formatDistance(double kmDistance) {
		double miles = MathUtils.convertKilometersToMiles(kmDistance);
		
		String number;
		String unit;
		if (miles > MILES_THRESHOLD) {
			number = MILES_FORMAT.format(miles);
			unit = milesUnit;
		} else {
			double feet = MathUtils.convertMilesToFeet(miles);
			number = FEET_FORMAT.format(feet);
			unit = feetUnit;
		}
		
		return number + " " + unit;
	}
}
