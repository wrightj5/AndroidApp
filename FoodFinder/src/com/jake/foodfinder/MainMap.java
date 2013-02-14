package com.jake.foodfinder;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.jake.foodfinder.model.ListOfPlaces;
import com.jake.foodfinder.model.Place;

public class MainMap extends MapActivity implements LocationListener {

	Drawable d, delta, zulu;
	MapView map;
	List<Overlay> overlayList;
	long start;
	long stop;
	MyLocationOverlay compass;
	MapController controller;
	int x, y;
	GeoPoint touchedPoint;
	Button places, search, settings, advanced;
	LocationManager lm;
	String towers;
	GeoPoint point;
	Location location;
	Place place, processPlace;
	ListOfPlaces arrayListOfPlaces;;
	String placeData;
	String storedPlaceData;
	static ToggleButton toggleMap;
	CustomPinpoint custom, custom0;
	//CustomOverlay t;

	float lati;
	float longi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map_view);
		arrayListOfPlaces = new ListOfPlaces();
		mapConfig();
		System.out.println("Set up map");
		buttonConfig();
		toggleMap = (ToggleButton) findViewById(R.id.toggleMap);
		mapToggle(toggleMap);
		System.out.println("Set up buttons");
		getMyLocation();
		System.out.println("Added pinpoint for my location");
		Bundle getSearchBundle = getIntent().getExtras();
		placeData = getSearchBundle.getString("searchFor");
		System.out.println("PLACE DATA ===== " + placeData);
		getNearbyLocations(placeData);
		storedPlaceData = placeData;
		

	}

	public void mapToggle(ToggleButton toggle) {

		toggleMap.setChecked(false);
		map.setStreetView(false);
		toggleMap.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (toggleMap.isChecked()) {

					map.setSatellite(true);
					map.setStreetView(false);
				} else {
					map.setSatellite(false);
					map.setStreetView(true);
				}
			}
		});

	}

	public static boolean isChecked() {

		return toggleMap.isChecked();
	}

	public static void setChecked(boolean check) {

		toggleMap.setChecked(check);
	}

	private void buttonConfig() {

		advanced = (Button) findViewById(R.id.bRefine);

		advanced.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent sendObjects = new Intent(MainMap.this,
						ProcessPlace.class);

				Bundle objBundle = new Bundle();
				objBundle.putSerializable("objBundle", arrayListOfPlaces);
				objBundle.putString("placeData", placeData);
				objBundle.putDouble("myLat", (lati / 1E6));
				objBundle.putDouble("myLong", (longi / 1E6));
				sendObjects.putExtras(objBundle);
				startActivity(sendObjects);

			}

		});

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

	private void mapConfig() {
		// find the map
		map = (MapView) findViewById(R.id.mvMain);
		// set the zoom
		map.setBuiltInZoomControls(true);
		// create a touchy
		//t = new CustomOverlay();
		//System.out.println("Touchy Created   " + t.toString());
		// get the overlays
		
		System.out.println("maps retrieved");
		// add the touchy to the overlay
		//map.getOverlays().add(t);
		System.out.println("Touchy added to list");

		compass = new MyLocationOverlay(MainMap.this, map);
		map.getOverlays().add(compass);
		controller = map.getController();
		point = new GeoPoint((int) (lati * 1E6), (int) (longi * 1E6));

	}

	public void getNearbyLocations(String searchPlace) {
		
		
		
		SharedPreferences getPrefs = PreferenceManager
				.getDefaultSharedPreferences(getBaseContext());
		String radius = getPrefs.getString("list", "1000");
		System.out.println("The radius is " + radius);
		int newRadius = Integer.parseInt(radius);
		newRadius = newRadius * 1000;
		double googlelat = location.getLatitude();
		double googlelon = location.getLongitude();
		String googleplacesapi = "https://maps.googleapis.com/maps/api/place/search/json?location="
				+ googlelat
				+ ","
				+ googlelon
				+ "&radius="
				+ newRadius
				+ "&sensor=true&types=food&name="
				+ searchPlace
				+ "&key=AIzaSyAUYXjtrnWYHc1_h44JBhmKyIb4RITmndM";

		System.out.println(googlelat);
		System.out.println(googlelon);
		System.out.println(googleplacesapi);

		try {
			JSONObject googleObject = StoresNearMe
					.getJSONfromURL(googleplacesapi);
			JSONArray parsedGoogle = googleObject.getJSONArray("results");
			if (parsedGoogle.equals(null)) {
				Toast.makeText(MainMap.this, "Unable to locate tower",
						Toast.LENGTH_SHORT).show();
			} else {
				for (int i = 0; i < parsedGoogle.length(); i++) {
					delta = getResources().getDrawable(R.drawable.mark_red);
					JSONObject parsedlocales = parsedGoogle.getJSONObject(i);
					// create the new place
					
					place = new Place();

					// get information to populate the object
					JSONObject geometrylocation = parsedlocales.getJSONObject(
							"geometry").getJSONObject("location");

					String placeName = parsedlocales.getString("name");
					place.setName(placeName);
					System.out.println("place name ===== " + placeName);
					place.setId(parsedlocales.getString("id"));

					try {
						place.setIsOpen(parsedlocales.getJSONObject(
								"opening_hours").getBoolean("open_now"));
						if (place.getIsOpen() == true) {
							place.setIsOpenString("Open");
						} else {
							place.setIsOpenString("Closed");
						}

						place.setRating(parsedlocales.getDouble("rating"));

					} catch (Exception e) {

					}

					double lat0 = (double) geometrylocation.getDouble("lat");
					double lng0 = (double) geometrylocation.getDouble("lng");

					double lng00 = (double) (lng0 * 1E6);
					double lat00 = (double) (lat0 * 1E6);
					place.setLat(lat0);
					place.setLongi(lng0);

					System.out.println(">>>>>" + lat0);

					GeoPoint resultLocation0 = new GeoPoint((int) (lat00),
							(int) (lng00));

					OverlayItem whiskey = new OverlayItem(resultLocation0,
							"What's Up", "Homie");
					custom0 = new CustomPinpoint(delta,
							MainMap.this);
				
					custom0.insertPoint(whiskey);

					
						
						
					map.getOverlays().add(custom0);
					

					System.out.println("Added " + i + " item at Lat: "
							+ resultLocation0.getLatitudeE6() + " | Long: "
							+ resultLocation0.getLongitudeE6());
					System.out.println("Overlay List: " + map.getOverlays().size());
					// add the place to the array list of places
					arrayListOfPlaces.put(place);
					
				}
				// add the array list of places to the ListOfPlaces class so
				// that we can send them across intents.
				
				System.out.println(arrayListOfPlaces.getSize());

				

			}
		} catch (JSONException e) {
			// Log.d("log_tag","JSON parsing error - Google Places Api:" +
			// e.getMessage());
		}

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
			controller.animateTo(point);
			controller.setZoom(14);
			d = getResources().getDrawable(R.drawable.mark_blue);
			OverlayItem overlayItem = new OverlayItem(point, "sup", "hollaaa");
			custom = new CustomPinpoint(d, MainMap.this);
			custom.insertPoint(overlayItem);
			
			map.getOverlays().add(custom);
			

		} else {

			Toast.makeText(MainMap.this, "Couldn't get provider",
					Toast.LENGTH_SHORT).show();
		}

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
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLocationChanged(Location l) {
		// TODO Auto-generated method stub
		lati = (int) (l.getLatitude() * 1E6);
		longi = (int) (l.getLongitude() * 1E6);
		point = new GeoPoint((int) lati, (int) longi);
		System.out.println("AAAAAAAAAAAAA    " + point.getLatitudeE6() + "  "
				+ point.getLongitudeE6());
		controller.animateTo(point);
		controller.setZoom(14);
		map.getOverlays().remove(custom);
		OverlayItem overlayItem = new OverlayItem(point, "sup", "hollaaa");
		custom.insertPoint(overlayItem);
		map.getOverlays().add(custom);

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
	protected void onPause() {
		// TODO Auto-generated method stub
		compass.disableCompass();
		super.onPause();

		lm.removeUpdates(this);

	}

	@Override
	protected void onResume() {
		compass.enableCompass();

		super.onResume();

		lm.requestLocationUpdates(towers, 500, 1, this);
	}

	
	
	
	
	
	
}
