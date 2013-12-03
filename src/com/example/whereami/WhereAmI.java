package com.example.whereami;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
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
		String addressString = "No address found.";
		if (null != location) {
			double lat = location.getLatitude();
			double lon = location.getLongitude();
			
			latLongString = "Lat : " + lat + "\nLong : " + lon;
			
			double latitude = location.getLatitude();
			double longitude = location.getLongitude();
			Geocoder gc = new Geocoder(this, Locale.getDefault());
			try {
				List<Address> addresses = gc.getFromLocation(latitude, longitude, 1);
				StringBuilder sb = new StringBuilder();
				if (addresses.size() > 0) {
					Address address = addresses.get(0);
					
					String addressLine = "";
					String postalCodeAndLocality = "";
					String countryName = "";
					
					for ( int i = 0; i < address.getMaxAddressLineIndex(); i++ ) {
						addressLine = address.getAddressLine(i);
						postalCodeAndLocality = address.getPostalCode() + " " + address.getLocality();
						countryName = address.getCountryName();
						
						if ( ( null != addressLine ) && ( "" != addressLine ) && !addressLine.equals(postalCodeAndLocality) ) {
							sb.append(addressLine).append("\n");
							
							sb.append(postalCodeAndLocality).append(", ");
							sb.append(countryName).append("\n\n");
						}
					}
					
					String newAddressString = sb.toString();
					if ( ( null != newAddressString ) && ( "" != newAddressString ) ) {
						addressString = newAddressString;
					} else if ( ( ( null != addressLine ) && ( "" != addressLine ) ) || ( ( null != postalCodeAndLocality ) && ( "" != postalCodeAndLocality ) ) ) {
						if ( ( null != addressLine ) && ( "" != addressLine ) ) {
							addressString = addressLine;
						} else {
							addressString = postalCodeAndLocality;
						}
						
						if ( ( null != countryName ) && ( "" != countryName ) ) {
							addressString = addressString + ", " + countryName;
						}
					}
				}
			} catch ( IOException e ) {}
		}
		
		myLocationText.setText("Your current position is :\n\n" + latLongString + "\n\n" + addressString);
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
