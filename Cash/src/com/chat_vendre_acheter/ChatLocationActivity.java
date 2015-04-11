package com.chat_vendre_acheter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ChatLocationActivity extends FragmentActivity{
GPSTracker gpsTracker;
double latitude, longitude;
String country, city;
GoogleMap map;
Marker markerOptions;
TextView txt_done;
ConnectionDetector cd;
SupportMapFragment mSupportMapFragment;
String img_location, chat_location, addre, lati, longi;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_location_activity);
		cd=new ConnectionDetector(ChatLocationActivity.this);
		if(cd.isConnectingToInternet()==false){
		
			errorDialog("Oops something is wrong wwith Internet Connection");
	
		}
		gpsTracker = new GPSTracker(ChatLocationActivity.this);
		FragmentManager fm = getSupportFragmentManager();
		mSupportMapFragment = (SupportMapFragment) fm
				.findFragmentById(R.id.map);
		 map= mSupportMapFragment.getMap();
		 txt_done = (TextView) findViewById(R.id.txt_send);
		Intent intent = getIntent();
		img_location = intent.getStringExtra("key");
		chat_location = intent.getStringExtra("key");
		addre = intent.getStringExtra("add");
		lati = intent.getStringExtra("lat");
		longi = intent.getStringExtra("lang");
		
		if (img_location.equals("img_location")) {
			
			if (gpsTracker.canGetLocation()) {

				latitude = gpsTracker.getLatitude();
				longitude = gpsTracker.getLongitude();
				setMap();  

			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gpsTracker.showSettingsAlert();
			}
			
			txt_done.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(ChatLocationActivity.this, ChatActivity.class);
					intent.putExtra("latitude", latitude);
					intent.putExtra("longitude", longitude);
					setResult(RESULT_OK, intent);
					finish();
				}
			});
		} else if (chat_location.equals("chat_loc")) {
			txt_done.setVisibility(View.GONE);
			getMap();
		}
		
		
		
	}
	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ChatLocationActivity.this);

		// set title
		alertDialogBuilder.setTitle("Cash");
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		// set dialog message
		alertDialogBuilder.setMessage("" + str).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						dialog.dismiss();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	
	public void setMap() {
		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
		
		LatLng latLng = new LatLng(latitude, longitude);

		drawCircle(new LatLng(latitude, longitude));
		// Drawing marker on the map
		drawMarker(new LatLng(latitude, longitude));
		map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
		map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

		markerOptions = map.addMarker(new MarkerOptions()
				.position(new LatLng(latitude, longitude))
			//	.title()
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
		markerOptions.showInfoWindow();
		

	}
	
	
	public void getMap() {
		latitude = Double.parseDouble(lati);
		longitude = Double.parseDouble(longi);
				map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				LatLng latLng = new LatLng(latitude, longitude);

				drawCircle(new LatLng(latitude, longitude));
				// Drawing marker on the map
				drawMarker(new LatLng(latitude, longitude));
				map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
				map.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

				markerOptions = map.addMarker(new MarkerOptions()
						.position(new LatLng(latitude, longitude))
						.title(addre)
						.icon(BitmapDescriptorFactory
								.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
				markerOptions.showInfoWindow();
				

			}
	

	private void drawMarker(LatLng point) {
		// Creating an instance of MarkerOptions
		MarkerOptions markerOptions = new MarkerOptions();

		// Setting latitude and longitude for the marker
		markerOptions.position(point);

		// Adding marker on the Google Map
		map.addMarker(markerOptions);

	}

	private void drawCircle(LatLng point) {

		// Instantiating CircleOptions to draw a circle around the marker
		CircleOptions circleOptions = new CircleOptions();

		// Specifying the center of the circle
		circleOptions.center(point);

		// Radius of the circle
		circleOptions.radius(20);

		// Border color of the circle
		circleOptions.strokeColor(Color.BLACK);

		// Fill color of the circle
		circleOptions.fillColor(0x30ff0000);

		// Border width of the circle
		circleOptions.strokeWidth(2);

		// Adding the circle to the GoogleMap
		map.addCircle(circleOptions);

	}
}
