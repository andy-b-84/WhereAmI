package com.example.whereami;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
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
		
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		criteria.setCostAllowed(true);
		String provider = locationManager.getBestProvider(criteria, true);
		
		Location l = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		updateWithNewLocation(l);
		
		locationManager.requestLocationUpdates(provider, 1000, 1, locationListener);
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

	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateWithNewLocation(location);
		}
		
		@Override
		public void onProviderDisabled(String provider) {}
		
		@Override
		public void onProviderEnabled(String provider) {}
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
	};
}
