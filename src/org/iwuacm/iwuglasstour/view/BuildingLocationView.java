package org.iwuacm.iwuglasstour.view;

import java.text.DecimalFormat;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.BuildingWithLocation;
import org.iwuacm.iwuglasstour.util.MathUtils;

import com.google.common.base.Optional;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
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
	
	private static final DecimalFormat MILES_FORMAT = new DecimalFormat("0.00");
	private static final DecimalFormat FEET_FORMAT = new DecimalFormat("0");
	
	private final TextView nameView;
	private final TextView distanceView;

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
		
		this.nameView = (TextView) findViewById(R.id.building_location_name);
		this.distanceView = (TextView) findViewById(R.id.building_location_distance);
		
		milesUnit = context.getResources().getString(R.string.miles_unit);
		feetUnit = context.getResources().getString(R.string.feet_unit);
	}
	
	/**
	 * Updates the building to be displayed with its relative location.
	 */
	public void setBuildingWithLocation(
			Optional<BuildingWithLocation> buildingWithLocationOptional) {

		if (!buildingWithLocationOptional.isPresent()) {
			nameView.setText("");
			return;
		}
		
		BuildingWithLocation buildingWithLocation = buildingWithLocationOptional.get();

		nameView.setText(buildingWithLocation.getBuilding().getShortName());
		distanceView.setText(formatDistance(buildingWithLocation.getDistance()));
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
