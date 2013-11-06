package com.example.whereami;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.TextView;

public class WhereAmI extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		LocationManager locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		
		Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		updateWithNewLocation(l);
	}
	
	private void updateWithNewLocation (Location location) {
		TextView myLocationText = (TextView)findViewById(R.id.myLocationText);
		
		String latLongString = "You seem to be nowhere.";
		if (null != location) {
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			
			latLongString = "Lat : " + lat + "\nLong : " + lon;
		}
		
		myLocationText.setText(latLongString);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.where_am_i, menu);
		return true;
	}

}
