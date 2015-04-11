package com.chat_vendre_acheter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;
import com.chat_vendre_acheter.extra.PlaceJSONParser;

public class LocationActivity extends FragmentActivity {
	GPSTracker gps;
	Button btn_submit;
	String bndl, country, city;
	SharedPreferences sharedPreferences;
	RadioButton use_gps_radio, manual_radio;
	TextView edt_location;
	ImageView imageView1;
	ParserTask parserTask;
	PlacesTask placesTask;
	AutoCompleteTextView atvPlaces;
	double latitude;
	double longitude;
	ConnectionDetector cd;
	String location;
	String address;
	boolean gps_chek = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_location);

		cd = new ConnectionDetector(LocationActivity.this);
		// gpsReverseCoding();
		use_gps_radio = (RadioButton) findViewById(R.id.radioButton1);
		manual_radio = (RadioButton) findViewById(R.id.radioButton2);
		edt_location = (TextView) findViewById(R.id.editText1);
		imageView1=(ImageView)findViewById(R.id.imageView1);
		use_gps_radio.setChecked(true);
		gps_chek = true;
		if (cd.isConnectingToInternet()) {
			atvPlaces = (AutoCompleteTextView) findViewById(R.id.et_City);
			atvPlaces.setText("Benin");
			atvPlaces.setVisibility(View.INVISIBLE);
			atvPlaces.setThreshold(1);
			atvPlaces.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					placesTask = new PlacesTask();
					placesTask.execute(s.toString());
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					// TODO Auto-generated method stub
				}

				@Override
				public void afterTextChanged(Editable s) {
					// TODO Auto-generated method stub
				}
			});

			sharedPreferences = LocationActivity.this.getSharedPreferences(
					"LOC", 1);

			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				bndl = bundle.getString("splash");
			}

			btn_submit = (Button) findViewById(R.id.btn_Submit);
			gps = new GPSTracker(LocationActivity.this);

			// check if GPS enabled
			if (gps.canGetLocation()) {

				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				Log.e("LATITUDE", latitude + "####" + longitude);
				gpsReverseCoding(latitude, longitude);
			} else {
				// can't get location
				// GPS or Network is not enabled
				// Ask user to enable GPS/network in settings
				gps.showSettingsAlert();
			}
			
			imageView1.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (gps_chek == true) {
						//sdf
						location = address;
					} else {
						location = atvPlaces.getText().toString();
						reverseCoding(location);
					}
					if (bndl.equals("splash")) {
						Editor editor = sharedPreferences.edit();
						editor.putString("lat", "" + latitude);
						editor.putString("longi", "" + longitude);

						editor.putBoolean("first", true);
						editor.putString("locati", location);
						editor.commit();
						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);
						i.putExtra("Location", location);
						startActivity(i);
						finish();
					} else if (bndl.equals("browse")) {
						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);
						i.putExtra("Location", location);
						setResult(RESULT_OK, i);
						finish();
					} else if (bndl.equals("addPost")) {
						Intent i = new Intent(getApplicationContext(),
								AddPostingActivity.class);
						i.putExtra("Location", location);
						setResult(RESULT_OK, i);
						finish();
					}
				}
			});
			btn_submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					if (gps_chek == true) {
						//sdf
						location = address;
					} else {
						location = atvPlaces.getText().toString();
						reverseCoding(location);
					}
					if (bndl.equals("splash")) {
						Editor editor = sharedPreferences.edit();
						editor.putString("lat", "" + latitude);
						editor.putString("longi", "" + longitude);

						editor.putBoolean("first", true);
						editor.putString("locati", location);
						editor.commit();
						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);
						i.putExtra("Location", location);
						startActivity(i);
						finish();
					} else if (bndl.equals("browse")) {
						Intent i = new Intent(getApplicationContext(),
								MainActivity.class);
						i.putExtra("Location", location);
						setResult(RESULT_OK, i);
						finish();
					} else if (bndl.equals("addPost")) {
						Intent i = new Intent(getApplicationContext(),
								AddPostingActivity.class);
						i.putExtra("Location", location);
						setResult(RESULT_OK, i);
						finish();
					}

				}
			});
			manual_radio.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					use_gps_radio.setChecked(false);
					manual_radio.setChecked(true);
					edt_location.setVisibility(View.GONE);
					atvPlaces.setVisibility(View.VISIBLE);
					gps_chek = false;
					return false;
				}
			});
			use_gps_radio.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					use_gps_radio.setChecked(true);
					manual_radio.setChecked(false);
					edt_location.setVisibility(View.VISIBLE);
					atvPlaces.setVisibility(View.GONE);
					edt_location.setText(city + "," + country);
					gps_chek = true;
					return false;
				}
			});

		}

		else {
			errorDialog("No Internet Connection,You don't have internet connection");
		}
	}

	private class PlacesTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... place) {
			// For storing data from web service
			String data = "";

			// Obtain browser key from https://code.google.com/apis/console
			String key = "key=AIzaSyA5ZJkZwPhMV_5L7NFTikE6TVA-u0TNuTU";

			String input = "";

			try {
				input = "input=" + URLEncoder.encode(place[0], "utf-8");
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}

			// place type to be searched
			String types = "types=geocode";
			// Sensor enabled
			String sensor = "sensor=false";
			// Building the parameters to the web service
			String parameters = input + "&" + types + "&" + sensor + "&" + key;
			// Output format
			String output = "json";
			// Building the url to the web service
			String url = "https://maps.googleapis.com/maps/api/place/autocomplete/"
					+ output + "?" + parameters;

			try {
				// Fetching the data from we service
				data = downloadUrl(url);
				Log.i("DATa", "" + data.toString());
			} catch (Exception e) {
			}
			return data;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// Creating ParserTask
			parserTask = new ParserTask();
			// Starting Parsing the JSON string returned by Web Service
			parserTask.execute(result);
		}
	}

	private class ParserTask extends
			AsyncTask<String, Integer, List<HashMap<String, String>>> {

		JSONObject jObject;

		@Override
		protected List<HashMap<String, String>> doInBackground(
				String... jsonData) {

			List<HashMap<String, String>> places = null;

			PlaceJSONParser placeJsonParser = new PlaceJSONParser();

			try {
				jObject = new JSONObject(jsonData[0]);
				// Getting the parsed data as a List construct

				places = placeJsonParser.parse(jObject);

				Log.e("places", places.toString());
			} catch (Exception e) {
			}
			return places;
		}

		@Override
		protected void onPostExecute(List<HashMap<String, String>> result) {

			String[] from = new String[] { "description" };
			int[] to = new int[] { android.R.id.text1 };
			Log.e("places", "" + from);

			// Creating a SimpleAdapter for the AutoCompleteTextView
			SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result,
					android.R.layout.simple_list_item_1, from, to);

			// Setting the adapter
			atvPlaces.setAdapter(adapter);
		}
	}

	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();

			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {

		} finally {
			iStream.close();
			urlConnection.disconnect();
		}

		return data;
	}

	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				LocationActivity.this);

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

	private void reverseCoding(final String location_adrred) {

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
			String res;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				// googleMap.clear();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
							+ URLEncoder.encode(location_adrred, "UTF-8")
							+ "&key=AIzaSyCafKLFkhIzrKCkPuDMvpdVgmzN6iMh700";
					Log.e("TAG URL", url);
					HttpClient client = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(url);
					HttpResponse httpResponse = client.execute(httpPost);
					res = EntityUtils.toString(httpResponse.getEntity());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				super.onPostExecute(result);

				try {
					JSONObject jsonObject = new JSONObject(res);

					JSONArray array = jsonObject.getJSONArray("results");
					String address = array.getJSONObject(0).getString(
							"formatted_address");
					String latitude = array.getJSONObject(0)
							.getJSONObject("geometry")
							.getJSONObject("location").getString("lat");
					String longitude = array.getJSONObject(0)
							.getJSONObject("geometry")
							.getJSONObject("location").getString("lng");
					Log.e("Benin", latitude + "  And  " + longitude);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		asyncTask.execute();
	}

	public void gpsReverseCoding(final double lat, final double lang) {
		// public void gpsReverseCoding(double ){
	//	Log.e("TST", "test");
		AsyncTask<Void, Void, Void> asyncTask1 = new AsyncTask<Void, Void, Void>() {

			String gpsRes;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {

				try {
					String gpsUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
							+ lat
							+ ","
							+ lang
							+ "&key=AIzaSyCafKLFkhIzrKCkPuDMvpdVgmzN6iMh700";

				//	Log.e("GPS", gpsUrl);
					HttpClient client = new DefaultHttpClient();
					HttpPost httpPost = new HttpPost(gpsUrl);
					HttpResponse httpResponse = client.execute(httpPost);
					gpsRes = EntityUtils.toString(httpResponse.getEntity());

					//Log.e("GPS>>RES", gpsRes.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				try {
					JSONObject jsonObject = new JSONObject(gpsRes);
					JSONArray array = jsonObject.getJSONArray("results");
					 address = array.getJSONObject(0).getString(
							"formatted_address");
					edt_location.setText(address);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		asyncTask1.execute();
	}

}
