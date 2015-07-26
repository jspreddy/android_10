/*********************************
 * HW #5
 * FileName: MainActivity.java
 *********************************
 * Team Members:
 * Richa Kandlikar
 * Sai Phaninder Reddy Jonnala
 * *******************************
 */


package com.example.inclass5;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity implements LocationListener {

	int start = 0;
	GoogleMap gmap;

	private Context context;

	private Location location;
	private double latitude;
	private double longitude;

	private boolean isGPSEnabled = false;
	private boolean isNetworkEnabled = false;
	private boolean canGetLocation = false;
	Location startLocation, endLocation;

	private long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
	private long MIN_TIME_BW_UPDATES = 1000 * 15;
	List<LatLng> locationList;
	Polyline line;

	private LocationManager locationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();

		gmap.setMyLocationEnabled(true);
		
		locationList = new ArrayList<LatLng>();
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
		Criteria criteria = new Criteria();
		String provider = locationManager.getBestProvider(criteria, false);
		startLocation = locationManager.getLastKnownLocation(provider);

		if (startLocation != null) {
			double latitude = startLocation.getLatitude();
			double longitude = startLocation.getLongitude();
			Log.d("DEBUG", "Loc = " + latitude + longitude);
			gmap.addMarker(new MarkerOptions().position(
					new LatLng(latitude, longitude)).title("Start Location"));
		}

		
		gmap.setOnMapLongClickListener(new OnMapLongClickListener() {

			@Override
			public void onMapLongClick(LatLng arg0) {
				if (start == 0) {
					start = 1;
					locationList.add(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()));
					Toast.makeText(MainActivity.this, "Tracking started",
							Toast.LENGTH_SHORT).show();
										
				} else if (start == 1) {
					start = 0;
					Toast.makeText(MainActivity.this, "Tracking stopped",
							Toast.LENGTH_SHORT).show();
					
					gmap.addMarker(new MarkerOptions().position(locationList.get(locationList.size()-1)).title("End Location"));

				}

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			locationList.add(new LatLng(location.getLatitude(),location.getLongitude()));
		}
		line.setPoints(locationList);

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}
	  public boolean active(){
	    	return canGetLocation;
	    } 

	    public Location getLocation() {
	        try {
	            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
	            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	 
	            if (!isGPSEnabled && !isNetworkEnabled) {
	            } else {
	                this.canGetLocation = true;
	                if (isNetworkEnabled) {
	                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                    if (locationManager != null) {
	                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	                        if (location != null) {
	                            latitude = location.getLatitude();
	                            longitude = location.getLongitude();
	                        }
	                    }
	                }
	                if (isGPSEnabled) {
	                    if (location == null) {
	                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
	                        if (locationManager != null) {
	                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	                            if (location != null) {
	                                latitude = location.getLatitude();
	                                longitude = location.getLongitude();
	                            }
	                        }
	                    }
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	 
	        return location;
	    }
	    
	    public double getLatitude(){
	        if(location != null){
	            latitude = location.getLatitude();
	        }
	        return latitude;
	    }
		
		public double getLongitude(){
	        if(location != null){
	            longitude = location.getLongitude();
	        }
	        return longitude;
	    }

}
