package com.chat_vendre_acheter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;
import com.chat_vendre_acheter.extra.GraphicsUtil;
import com.chat_vendre_acheter.extra.ImageLoader;
import com.chat_vendre_acheter.extra.ServiceHandler;

public class EditProfile extends FragmentActivity {
	String user_id, user_name, profile_image, email, phone, lat, longi;
	Dialog dialog;
	ProgressDialog progressDialog;
	HttpResponse httpResponse;
	ConnectionDetector cd;
	String picturePath, error, s, msgg;
	final int CAMERA_CAPTURE = 1;
	final int GALLERY_IMG = 2;
	final int PIC_CROP_CAMERA = 3;
	final int PIC_CROP_GALLERY = 4;
	SharedPreferences preferences_login_detail;
	public static final String PREFS_NAME = "MyApp_Settings";
	GPSTracker gps;
	byte[] ba;
	EditText edt_user_name, edt_pass, edt_conf_pass, edt_email, edt_phone;
	TextView location;
	ImageView img_setImage, img_done;
	ImageLoader imageLoader;
	ServiceHandler serviceHandler;
	boolean agree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_edit_profile);
		gps = new GPSTracker(EditProfile.this);

		imageLoader = new ImageLoader(EditProfile.this);
		preferences_login_detail = getSharedPreferences("LOGIN_PREFS_NAME",
				MODE_PRIVATE);
		dialog = new Dialog(EditProfile.this, R.style.DialogTheme);
		dialog.setContentView(R.layout.share_fragment);
		dialog.setCancelable(true);
		cd = new ConnectionDetector(EditProfile.this);
		user_id = preferences_login_detail.getString("ID", "Hi");

		edt_user_name = (EditText) findViewById(R.id.etName);
		edt_pass = (EditText) findViewById(R.id.etPassword);
		edt_conf_pass = (EditText) findViewById(R.id.etConfirmPassword);
		edt_email = (EditText) findViewById(R.id.et_Email);
		edt_phone = (EditText) findViewById(R.id.et_Phone);
		location = (TextView) findViewById(R.id.et_location);
		img_setImage = (ImageView) findViewById(R.id.imgSetimage);
		img_done = (ImageView) findViewById(R.id.imageView1);

		img_setImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectimages();
			}
		});

		img_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = edt_user_name.getText().toString();
				String phone = edt_phone.getText().toString();
				String email = edt_email.getText().toString();
				String password = edt_pass.getText().toString();
				String confirmpass = edt_conf_pass.getText().toString();
				boolean abc = validateEmail(email);

				if (name.equals("") && phone.equals("") && email.equals("")
						&& password.equals("") && confirmpass.equals("")) {
					errorDialog("Fields are empty");

				} else if (name.equals("")) {
					errorDialog("Enter user name");

				} else if (password.equals("")) {
					errorDialog("Enter Password");

				}

				else if (!password.equals(confirmpass)) {

					errorDialog("Password is not matched");

				} else if (email.equals("") || (abc == false)) {
					errorDialog("Enter Valid Email");

				} else if (phone.equals("")) {
					errorDialog("Enter Phone number");

				}

				else {

					if (cd.isConnectingToInternet()) {
						doneEdit();
					} else {
						errorDialog("Oops, Something is wrong with internet connection");

					}
				}
			}

		});
		new getEditProfile().execute();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 1) {
				Bundle extras = data.getExtras();
				if (extras != null) {
					Bitmap photo = extras.getParcelable("data");
					GraphicsUtil gpUtil = new GraphicsUtil();
					dialog.dismiss();
					photo = gpUtil.getCircleBitmap(photo, 16);
					img_setImage.setImageBitmap(photo);

					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
					ba = stream.toByteArray();
				}

			} else if (requestCode == 2) {
				Bundle extras2 = data.getExtras();
				if (extras2 != null) {
					Bitmap photo = extras2.getParcelable("data");
					dialog.dismiss();
					GraphicsUtil gpUtil = new GraphicsUtil();
					photo = gpUtil.getCircleBitmap(photo, 16);
					img_setImage.setImageBitmap(photo);
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
					ba = stream.toByteArray();
				}
			}
		}

	}

	private void selectimages() {

		RelativeLayout lytHead = (RelativeLayout) dialog
				.findViewById(R.id.lytHead);
		Button btn_whatapp = (Button) dialog.findViewById(R.id.whatsapp);
		Button btn_facebook = (Button) dialog.findViewById(R.id.facebook);
		Button btn_google_plus = (Button) dialog.findViewById(R.id.google_plus);
		Button btn_twitter = (Button) dialog.findViewById(R.id.twitter);
		Button btn_email = (Button) dialog.findViewById(R.id.email);
		Button btn_copy_link = (Button) dialog.findViewById(R.id.copy_link);
		btn_whatapp.setVisibility(View.GONE);
		btn_facebook.setVisibility(View.GONE);
		btn_google_plus.setVisibility(View.GONE);
		btn_twitter.setText("CAMERA");
		btn_email.setText("GALLERY");
		btn_copy_link.setText("CANCEL");
		btn_copy_link.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		btn_twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

				intent.putExtra(MediaStore.EXTRA_OUTPUT,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
				// ******** code for crop image
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 0);
				intent.putExtra("aspectY", 0);
				intent.putExtra("outputX", 200);
				intent.putExtra("outputY", 150);

				try {

					intent.putExtra("return-data", true);
					startActivityForResult(intent, 1);

				} catch (ActivityNotFoundException e) {
					// Do nothing for now
				}
			}
		});
		btn_email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");

				// ******** code for crop image
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 0);
				intent.putExtra("aspectY", 0);
				intent.putExtra("outputX", 200);
				intent.putExtra("outputY", 150);

				try {

					intent.putExtra("return-data", true);
					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), 2);

				} catch (ActivityNotFoundException e) {
					// Do nothing for now
				}

			}
		});
		lytHead.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		Display mDisplay = EditProfile.this.getWindowManager()
				.getDefaultDisplay();
		final int width = mDisplay.getWidth();
		final int height = mDisplay.getHeight();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = width;
		lp.height = height;
		dialog.getWindow().setAttributes(lp);
		dialog.show();

	}

	private class getEditProfile extends AsyncTask<Void, Void, Void> {
		String url = "http://www.cashcash.today/cash/api/users/user/" + user_id;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(EditProfile.this);
			progressDialog.setTitle("Loading....");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			ServiceHandler serviceHandler = new ServiceHandler();
			String jsonResponse = serviceHandler.makeServiceCall(url,
					ServiceHandler.GET);

			if (jsonResponse != null) {

				try {
					JSONObject jsonObjectJson = new JSONObject(jsonResponse);
					String error1 = jsonObjectJson.getString("error");
					if (error1.equals("0")) {
						JSONObject jsonObjectList = jsonObjectJson
								.getJSONObject("list");
						JSONObject jsonObjectUser = jsonObjectList
								.getJSONObject("User");
						user_id = jsonObjectUser.getString("id");
						user_name = jsonObjectUser.getString("username");
						email = jsonObjectUser.getString("email");
						phone = jsonObjectUser.getString("phone");
						lat = jsonObjectUser.getString("latitude");
						longi = jsonObjectUser.getString("longitude");
						profile_image = jsonObjectUser.getString("image");
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			imageLoader.DisplayImage(profile_image, img_setImage);
			edt_user_name.setText(user_name);
			edt_email.setText(email);
			edt_phone.setText(phone);

			double lat1, longi1;
			lat1 = Double.parseDouble(lat);
			longi1 = Double.parseDouble(longi);

			Geocoder geocoder;
			List<Address> addresses = null;
			geocoder = new Geocoder(EditProfile.this, Locale.getDefault());
			try {
				addresses = geocoder.getFromLocation(lat1, longi1, 1);
				String address = addresses.get(0).getAddressLine(0);
				String city = addresses.get(0).getAddressLine(1);
				String country = addresses.get(0).getAddressLine(2);
				location.setText(city + "," + country);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			progressDialog.dismiss();
		}

	}

	public void doneEdit() {

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog.setMessage("Wait...");
				progressDialog.setIndeterminate(true);
				progressDialog.show();

				if (ba == null) {
					Bitmap bitmap2 = imageLoader.bitmap1;
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					bitmap2.compress(Bitmap.CompressFormat.PNG, 100, stream);
					ba = stream.toByteArray();
				}
			}

			@Override
			protected Void doInBackground(Void... params) {

				String url = "http://www.cashcash.today/cash/api/users/edit/"
						+ user_id;
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					multipartEntity.addPart("data[User][username]",
							new StringBody(edt_user_name.getText().toString()));
					multipartEntity.addPart("data[User][email]",
							new StringBody(edt_email.getText().toString()));
					multipartEntity.addPart("data[User][password]",
							new StringBody(edt_pass.getText().toString()));
					multipartEntity.addPart("data[User][phone]",
							new StringBody(edt_phone.getText().toString()));
					multipartEntity.addPart("data[User][name]", new StringBody(
							"hello"));

					if (ba != null) {

						multipartEntity.addPart("data[User][image]",
								new ByteArrayBody(ba, "Pic" + ".png"));
					}

					httpPost.setEntity(multipartEntity);
					httpResponse = null;

					try {

						httpResponse = httpClient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					try {
						s = EntityUtils.toString(httpResponse.getEntity());
					} catch (ParseException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				try {
					JSONObject jsonObject = new JSONObject(s);
					error = jsonObject.getString("error");
					msgg = jsonObject.getString("msg");

				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				if (error.equals("1")) {

					errorDialog("" + msgg);

				} else {
					Intent i = new Intent(EditProfile.this, MainActivity.class);
					startActivity(i);
				}

				progressDialog.dismiss();
			}

		};
		asyncTask.execute((Void[]) null);
	}

	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				EditProfile.this);

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

	public boolean validateEmail(String email) {

		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}

}
