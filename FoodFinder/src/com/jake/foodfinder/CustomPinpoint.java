package com.jake.foodfinder;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class CustomPinpoint extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> pinPoints = new ArrayList<OverlayItem>();
	@SuppressWarnings("unused")
	private Context c;

	public CustomPinpoint(Drawable arg0) {
		super(boundCenter(arg0));
		// TODO Auto-generated constructor stub
	}

	public CustomPinpoint(Drawable m, Context context) {
		this(m);
		c = context;
		
		
	}
	
	

	@Override
	protected OverlayItem createItem(int i) {
		// TODO Auto-generated method stub
		return pinPoints.get(i);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return pinPoints.size();
	}

	public void insertPoint(OverlayItem item) {

		pinPoints.add(item);
		populate();
		
		
	}
	

	@Override
	public boolean onTap(final GeoPoint p, final MapView mapView) {
		boolean tapped = super.onTap(p, mapView);
		if (tapped) {
			System.out.println("GeoTap");

			return false;

		} else {
			// do what you want to do when you DON'T hit an item
		}
		return true;
	}

	@Override
	protected boolean onTap(int index) {

		System.out.println("IndexTap");
		OverlayItem item = pinPoints.get(index);
		AlertDialog.Builder dialog = new AlertDialog.Builder(c);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.show();
		return true;

		// Here I know what marker been clicked...
	}

}
