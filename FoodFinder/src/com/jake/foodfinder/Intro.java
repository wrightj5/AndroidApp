package com.jake.foodfinder;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class Intro extends Activity {
	
	String username = "Welcome ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.intro);
		SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
		username += getPrefs.getString("name", "new user");
		TextView welcome = (TextView)findViewById(R.id.welcome);
		welcome.setText(username);
		
		
		Thread timer = new Thread(){
			public void run(){
				try{
					
					sleep(2000);
					
				}catch(InterruptedException e){
					e.printStackTrace();
				
				}finally{
					
					Intent i = new Intent(Intro.this, PlaceList.class);
					startActivity(i);
					
				}
				
			}
		};
		timer.start();
		
		
		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.finish();
		
	}
	
	
	

}