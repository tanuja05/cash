package com.chat_vendre_acheter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.ImageLoader;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ProductMap extends FragmentActivity implements
		OnMarkerClickListener {
	ImageView product_pic, back;
	TextView product_name, product_price, post_day, user_name;
	String str_product_pic, str_prduct_name, str_product_price, str_post_day,
			str_user_name, str_lat, str_longi;
	ImageLoader imageLoader;
	double latitude, longitude;
	GoogleMap map;
	Marker markerOptions;
ConnectionDetector cd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.product_map);

		imageLoader = new ImageLoader(ProductMap.this);
cd=new ConnectionDetector(getApplicationContext());
		Intent i = getIntent();
		str_user_name = i.getStringExtra("User Name");
		str_product_pic = i.getStringExtra("product_image_url");
		str_prduct_name = i.getStringExtra("Product_name");
		str_product_price = i.getStringExtra("Product Price");
		str_post_day = i.getStringExtra("day");
		str_lat = i.getStringExtra("lat_ad");
		str_longi = i.getStringExtra("longi_ad");
		findId();
		if(cd.isConnectingToInternet()==true){
		dataSet();
		}
		else{
			errorDialog("No Internet Connection,You don't have internet connection");
		}
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	public void findId() {
		back = (ImageView) findViewById(R.id.back);
		product_pic = (ImageView) findViewById(R.id.product_img);
		product_name = (TextView) findViewById(R.id.product_name_txt);
		product_price = (TextView) findViewById(R.id.product_price_txt);
		post_day = (TextView) findViewById(R.id.today_txt);
		user_name = (TextView) findViewById(R.id.product_seller_txt);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
	}
	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ProductMap.this);

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

	public void dataSet() {

		product_name.setText(str_prduct_name);
		product_price.setText(str_product_price);
		user_name.setText("Seller: " + str_user_name);
		imageLoader.DisplayImage(str_product_pic, product_pic);

		if (str_post_day.contains(",")) {
			String[] day1 = str_post_day.split(",");
			str_post_day = day1[0];
		}
		post_day.setText(str_post_day);

		latitude = Double.parseDouble(str_lat);
		longitude = Double.parseDouble(str_longi);
		
		if(cd.isConnectingToInternet()==true){
		setMap();
	}
	else {
		errorDialog("No Internet Connection,You don't have internet connection");
	}	
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
				.title(str_prduct_name)
				.icon(BitmapDescriptorFactory
						.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
		markerOptions.showInfoWindow();
		map.setOnMarkerClickListener(this);

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

	@Override
	public boolean onMarkerClick(final Marker marker) {
		// TODO Auto-generated method stub
		if (marker.equals(markerOptions)) {
			markerOptions.setTitle(str_prduct_name);

		}
		return false;
	}
}
