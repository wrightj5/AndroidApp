package com.jake.foodfinder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.jake.foodfinder.model.Place;

public class RouteMap extends MapActivity implements LocationListener {

	LocationManager lm;
	String towers;
	Location location;
	double lati, longi;
	GeoPoint point;
	MapController controller;
	Drawable d;
	List<Overlay> overlayList;
	Place processPlace;
	MapView map;
	MyLocationOverlay compass;
	GeoPoint touchedPoint, newPoint;
	int x, y;
	long start, stop;
	MainMap main;
	ToggleButton toggleMap2;
	Uri uri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.route_map_view);
		//toggleMap2 = (ToggleButton) findViewById(R.id.toggleMap2);
		//mapToggle(toggleMap2);
		Bundle getSearchBundle = getIntent().getExtras();
		processPlace = (Place) getSearchBundle.getSerializable("place");
		addSinglePlace(processPlace);
		mapConfig();
		getPlaceLocation();
		getMyLocation();
		uri = Uri.parse("http://maps.google.com/maps?&saddr=" + point.getLatitudeE6() / 1E6 + ","
				+ point.getLongitudeE6()/1E6 + "&daddr=" + newPoint.getLatitudeE6() / 1E6 + ","
				+ newPoint.getLongitudeE6() / 1E6);
		
		System.out.println("URI =========" + uri);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(intent);
		finish();
	}

	// mapConfig();
	// getMyLocation();
	// getPlaceLocation();

	public void mapToggle(ToggleButton toggle) {

		toggleMap2.setChecked(MainMap.isChecked());
		toggleMap2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (toggleMap2.isChecked()) {

					MainMap.setChecked(true);
					map.setSatellite(true);
					map.setStreetView(false);
				} else {
					MainMap.setChecked(false);
					map.setSatellite(false);
					map.setStreetView(true);
				}
			}
		});

	}

	private void mapConfig() {
		// find the map
		//map = (MapView) findViewById(R.id.mvRoute);
		// set the zoom
		//map.setBuiltInZoomControls(true);
		// create a touchy
		//Touchy t = new Touchy();

		// get the overlays
		//overlayList = map.getOverlays();
		// add the touchy to the overlay
		//overlayList.add(t);

		//compass = new MyLocationOverlay(RouteMap.this, map);
		//overlayList.add(compass);
		//controller = map.getController();
		point = new GeoPoint((int) (lati * 1E6), (int) (longi * 1E6));

	}

	private void getMyLocation() {

		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria crit = new Criteria();
		towers = lm.getBestProvider(crit, false);
		location = lm.getLastKnownLocation(towers);
		if (location != null) {
			lati = (int) (location.getLatitude() * 1E6);
			longi = (int) (location.getLongitude() * 1E6);

			point = new GeoPoint((int) lati, (int) longi);
			System.out.println("AAAAAAAAAAAAA    " + point.getLatitudeE6()
					+ "  " + point.getLongitudeE6());
//			d = getResources().getDrawable(R.drawable.mark_blue);
//			OverlayItem overlayItem = new OverlayItem(point, "sup", "hollaaa");
//			CustomPinpoint custom = new CustomPinpoint(d, RouteMap.this);
//			custom.insertPoint(overlayItem);
//			overlayList.add(custom);

		} else {

			Toast.makeText(RouteMap.this, "Couldn't get provider",
					Toast.LENGTH_SHORT).show();
		}

	}

	private void getPlaceLocation() {

		double tempLat = (processPlace.getLat() * 1E6);
		double tempLong = (processPlace.getLongi() * 1E6);
		newPoint = new GeoPoint((int) tempLat, (int) tempLong);
//		OverlayItem overlayItem = new OverlayItem(newPoint, "sup", "hollaaa");
//		CustomPinpoint custom0 = new CustomPinpoint(getResources().getDrawable(
//				R.drawable.mark_red), RouteMap.this);
//		custom0.insertPoint(overlayItem);
//		overlayList.add(custom0);
//		controller.animateTo(newPoint);
//		controller.setZoom(14);

	}

	class Touchy extends Overlay {

		public boolean ontouchEvent(MotionEvent e, MapView m) {

			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = map.getProjection().fromPixels(x, y);
			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}

			AlertDialog alert = new AlertDialog.Builder(RouteMap.this).create();
			alert.setTitle("Pick an Option");
			alert.setMessage("I told you to pick an option!");
			alert.setButton("Place a marker",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

						}
					});

			alert.setButton2("Get Address",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub

							Geocoder geocoder = new Geocoder(getBaseContext(),
									Locale.getDefault());
							try {

								List<Address> address = geocoder.getFromLocation(
										touchedPoint.getLatitudeE6() / 1E6,
										touchedPoint.getLongitudeE6() / 1E6, 1);
								if (address.size() > 0) {
									String display = "";
									for (int i = 0; i < address.get(0)
											.getMaxAddressLineIndex(); i++) {

										display += address.get(0)
												.getAddressLine(i) + "\n";
									}
									Toast t = Toast.makeText(getBaseContext(),
											display, Toast.LENGTH_LONG);
									t.show();
								}
							} catch (IOException e) {
								e.printStackTrace();
							} finally {

							}

						}
					});
			alert.setButton3("Example", new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

				}
			});
			alert.show();

			return false;
		}
	}

	private void addSinglePlace(Place p) {

		Toast.makeText(getApplicationContext(), p.getName(), Toast.LENGTH_SHORT)
				.show();

		// Add a marker on the map using the lat/long data stored within this
		// place object
		// Configure routing

	}

	@Override
	public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		super.onCreateOptionsMenu(menu);
		MenuInflater blowup = getMenuInflater();
		blowup.inflate(R.menu.activity_map_view, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.favourites:

			break;
		case R.id.about:
			Intent i = new Intent("android.intent.action.ABOUT");
			startActivity(i);

			break;

		case R.id.preferences:
			Intent p = new Intent("android.intent.action.PREFS");
			startActivity(p);
			break;

		}
		return false;

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		compass.disableCompass();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		compass.enableCompass();
	}

}
