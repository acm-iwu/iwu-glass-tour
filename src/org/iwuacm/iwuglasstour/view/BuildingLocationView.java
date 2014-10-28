package org.iwuacm.iwuglasstour.view;

import org.iwuacm.iwuglasstour.R;
import org.iwuacm.iwuglasstour.model.BuildingWithLocation;

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
	
	private final TextView nameView;

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
	}
}
