package com.jake.foodfinder;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.jake.foodfinder.model.ListOfPlaces;
import com.jake.foodfinder.model.Place;

public class ProcessPlace extends ListActivity {

	Button settings, search, places, back;
	Place place;
	String placeData;
	int size;
	ArrayList<Place> parsedPlaces = new ArrayList<Place>();
	ArrayList<String> names = new ArrayList<String>();
	double myLat, myLong;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specific_place_list);
		
		buttonConfig();
		// get data from the previous page
		Intent intent = getIntent();
		ListOfPlaces myPlaces = (ListOfPlaces) intent
				.getSerializableExtra("objBundle");
		// placeData = intent.getCharSequenceExtra("placeData").toString();
		size = myPlaces.getSize();
		parsedPlaces = myPlaces.getAll();
		myLat = intent.getDoubleExtra("myLat", 0.0);
		myLong = intent.getDoubleExtra("myLong", 0.0);
		System.out.println("Lat: " + myLat + "|| Long: " + myLong);
		// System.out.println(placeData);

		getPlaceNames();

		SimpleAdapter adapter = new SimpleAdapter(this, list,
				R.layout.custom_row_view, new String[] { "Name", "Open",
						"Distance" }, new int[] { R.id.text1, R.id.text2,
						R.id.text3 });

		populateList();
		setListAdapter(adapter);

		// Instantiate a list of near by places which are of the type

		// setListAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, nearByPlaces));

	}



	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);

		Toast.makeText(getApplicationContext(),
				parsedPlaces.get(position).getName(), Toast.LENGTH_SHORT)
				.show();

		Intent intent = new Intent(ProcessPlace.this, RouteMap.class);
		Bundle basket = new Bundle();
		basket.putSerializable("place", parsedPlaces.get(position));
		intent.putExtras(basket);
		startActivity(intent);
		
	}

	static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	private void getPlaceNames() {

		for (int i = 0; i < parsedPlaces.size(); i++) {

			names.add(parsedPlaces.get(i).getName());
		}

	}

	private void populateList() {

		// setListAdapter(new ArrayAdapter<String>(this,
		// android.R.layout.simple_list_item_1, names));
		double tempLat;
		double tempLong;

		for (int i = 0; i < parsedPlaces.size(); i++) {

			tempLat = parsedPlaces.get(i).getLat();
			tempLong = parsedPlaces.get(i).getLongi();
			HashMap<String, String> temp = new HashMap<String, String>();
			temp.put("Name", parsedPlaces.get(i).getName());

			String isOpen = parsedPlaces.get(i).getIsOpenString();
			if (isOpen == null) {
				isOpen = "Unknown";
			}
			temp.put("Open", "Retrieved opening hours: " + isOpen);

			double distance = (this.getDistanceFromMe(tempLat, tempLong) / 1000);
			double distance2 = Double.parseDouble(new DecimalFormat("#.##")
					.format(distance));
			String finalDistance = String.valueOf(distance2);
			parsedPlaces.get(i).setDistanceFromMe(distance2);
			temp.put("Distance", "Distance: " + finalDistance + " Kilometers");
			list.add(temp);
		}

	}

	private void buttonConfig() {

	}

	private double getDistanceFromMe(double lat0, double long0) {

		try {
			float[] result = new float[3];
			// Log.e("lat long : ", c.getDouble(1) + " : " + c.getDouble(2) +
			// " : " + latitude + " : " + longitude);
			Location.distanceBetween(myLat, myLong, lat0, long0, result);
			double distance = ((double) result[0]);

			return distance;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0.0;

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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onPause();
		list.clear();

	}

}
