package com.jake.foodfinder;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class PlaceList extends ListActivity {

	String classes[] = { "Pizza", "Kebab", "Chips",
			"Chinese",  "Indian" };
	Button search, settings, places, back;
	MainMap main;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, classes));
		setContentView(R.layout.place_list);
		buttonConfig();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		

		String selection = classes[position];
		Bundle placeData = new Bundle();
		placeData.putString("searchFor", selection);
		Intent sendToSearch = new Intent(PlaceList.this, MainMap.class);
		sendToSearch.putExtras(placeData);
		startActivity(sendToSearch);

	}

	
	private void buttonConfig() {
		
		
		
	
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

	
	
	
}
