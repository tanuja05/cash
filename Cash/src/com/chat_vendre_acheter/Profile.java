package com.chat_vendre_acheter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.ImageLoader;
import com.chat_vendre_acheter.extra.ServiceHandler;

public class Profile extends FragmentActivity {
	ImageView imageView1;
	Spinner spinner;
	private ArrayList<String> navSpinner;
	private TitleNavigationAdapter adapter;
	ImageView edit_icon;
	TextView txtLocation, txtFollowers, txtUsername;
	ProgressDialog progressDialog;
	String str, url, user_id, error, msgg, folower_count, img_url, user_name,
			user_img_url, lati, langi;
	SharedPreferences sharedPreferences;
	ImageLoader imLoader;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile_lyt);
		imLoader = new ImageLoader(Profile.this);
		sharedPreferences = Profile.this.getSharedPreferences(
				"LOGIN_PREFS_NAME", 1);
		user_id = sharedPreferences.getString("ID", "10");
		user_name = sharedPreferences.getString("USERNAME", "1201");
		user_img_url = sharedPreferences.getString("PROFILE_IMAGE", "imgggg");
		progressDialog = new ProgressDialog(Profile.this);
		spinner = (Spinner) findViewById(R.id.spin);
		edit_icon = (ImageView) findViewById(R.id.edt_profile);
		txtLocation = (TextView) findViewById(R.id.textView1);
		txtFollowers = (TextView) findViewById(R.id.textView2);
		txtUsername = (TextView) findViewById(R.id.user_name);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		txtUsername.setText(user_name);
		imLoader.DisplayImage(user_img_url, imageView1);
		cd = new ConnectionDetector(Profile.this);
		edit_icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent edit_profile = new Intent(Profile.this,
						EditProfile.class);
				startActivity(edit_profile);
			}
		});

		navSpinner = new ArrayList<String>();
		navSpinner.add("REVIEWES");
		navSpinner.add("FAVORITES");
		navSpinner.add("LISTINGS");
		navSpinner.add("FOLLOWERS");

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Profile.this,
				android.R.layout.simple_spinner_dropdown_item, navSpinner);
		spinner.setAdapter(adapter);
if(cd.isConnectingToInternet()==true){
		getData();}
else{
	errorDialog("Oops, Something is wrong with internet connection");
}
	}
	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				Profile.this);

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

	public void getData() {

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog.setMessage("Wait...");
				progressDialog.setIndeterminate(true);
				progressDialog.show();

			}

			@Override
			protected Void doInBackground(Void... params) {
				url = "http://www.cashcash.today/cash/api/users/user/"
						+ user_id;
				ServiceHandler sh = new ServiceHandler();
				str = sh.makeServiceCall(url, ServiceHandler.GET);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				if (str != null) {
					try {
						JSONObject jsonObject = new JSONObject(str);
						error = jsonObject.getString("error");
						msgg = jsonObject.getString("msg");
						if (error.equals("0")) {

							JSONObject jobj = jsonObject.getJSONObject("list");
							JSONObject jUser = jobj.getJSONObject("User");
							folower_count = jUser.getString("like_count");
							lati = jUser.getString("latitude");
							langi = jUser.getString("longitude");

							double lat1, longi1;
							lat1 = Double.parseDouble(lati);
							longi1 = Double.parseDouble(langi);

							Geocoder geocoder;
							List<Address> addresses = null;
							geocoder = new Geocoder(Profile.this,
									Locale.getDefault());
							try {
								addresses = geocoder.getFromLocation(lat1,
										longi1, 1);
								String address = addresses.get(0)
										.getAddressLine(0);
								String city = addresses.get(0)
										.getAddressLine(1);
								String country = addresses.get(0)
										.getAddressLine(2);

								if (country.contains(",")) {
									String[] day1 = country.split(",");
									country = day1[0];
								}

								txtLocation.setText(city + "," + country);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					} catch (JSONException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				}
				progressDialog.dismiss();
			}

		};

		asyncTask.execute((Void[]) null);

	}
}
