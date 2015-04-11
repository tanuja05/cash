package com.chat_vendre_acheter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;
import com.chat_vendre_acheter.extra.GraphicsUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SignUpActivity extends FragmentActivity {
	EditText etName, etPassword, etConfirmPassword, et_phone, etEmail;
	Button btn_SignUp;
	CheckBox chk_terms;
	ImageView imgadd_image, img_setImage;
	boolean agree;
	private Uri picUri;
	HttpResponse httpResponse;
	ConnectionDetector cd;
	byte[] ba;
	Dialog dialog;
	String picturePath, error, s, msgg;
	final int CAMERA_CAPTURE = 1;
	final int GALLERY_IMG = 2;
	final int PIC_CROP_CAMERA = 3;
	final int PIC_CROP_GALLERY = 4;
	ProgressDialog progressDialog;
	GPSTracker gps;

	double latitude, longitude;
	String token;
	GoogleCloudMessaging gcm;
	SharedPreferences sharedPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_up);

		sharedPreferences = this.getSharedPreferences("LOGIN_TOKEN", 1);
		findIds();
		dialog = new Dialog(SignUpActivity.this, R.style.DialogTheme);
		dialog.setContentView(R.layout.share_fragment);
		dialog.setCancelable(true);
		cd = new ConnectionDetector(SignUpActivity.this);
		progressDialog = new ProgressDialog(SignUpActivity.this);

		gps = new GPSTracker(SignUpActivity.this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

		imgadd_image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectimages();
			}
		});
		chk_terms.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (agree == false) {
					agree = true;
				} else {
					agree = false;
				}
			}
		});
		btn_SignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = etName.getText().toString();
				String phone = et_phone.getText().toString();
				String email = etEmail.getText().toString();
				String password = etPassword.getText().toString();
				String confirmpass = etConfirmPassword.getText().toString();
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

				} else if (agree == false) {
					errorDialog("Please accept the terms and condition to proceed");

				} else if (ba == null) {
					errorDialog("Help make chat a friendlier place by adding your avatar Thanks");

				}

				else {

					if (cd.isConnectingToInternet()) {
						signUp();
					} else {
						errorDialog("Oops, Something is wrong with internet connection");

					}
				}
			}

		});
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
		Display mDisplay = SignUpActivity.this.getWindowManager()
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

	public void signUp() {

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

				gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
				try {
					token = gcm.register(CommonUtilities.SENDER_ID);

				} catch (IOException e1) {

					token = "";
					e1.printStackTrace();
				}

				String url = "http://www.cashcash.today/cash/api/users/registration";
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					multipartEntity.addPart("data[User][username]",
							new StringBody(etName.getText().toString()));
					multipartEntity.addPart("data[User][email]",
							new StringBody(etEmail.getText().toString()));
					multipartEntity.addPart("data[User][password]",
							new StringBody(etPassword.getText().toString()));
					multipartEntity.addPart("data[User][phone]",
							new StringBody(et_phone.getText().toString()));
					multipartEntity.addPart("data[User][device_token]",
							new StringBody(token));

					if (ba != null) 
					{
						multipartEntity.addPart("data[User][image]",
								new ByteArrayBody(ba, "Pic" + ".png"));
					}

					multipartEntity.addPart("data[User][latitude]",
							new StringBody("" + latitude));
					multipartEntity.addPart("data[User][longitude]",
							new StringBody("" + longitude));

					Editor editor = sharedPreferences.edit();
					editor.putString("TOKEN", token);
					editor.commit();

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
						Log.e("seee", s.toString());
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
					Intent i = new Intent(SignUpActivity.this,
							MainActivity.class);
					startActivity(i);
				}

				progressDialog.dismiss();
			}

		};
		asyncTask.execute((Void[]) null);

	}

	private void findIds() {
		etName = (EditText) findViewById(R.id.etName);
		et_phone = (EditText) findViewById(R.id.et_Phone);
		etEmail = (EditText) findViewById(R.id.et_Email);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
		btn_SignUp = (Button) findViewById(R.id.btn_SignUp);
		imgadd_image = (ImageView) findViewById(R.id.imgadd_image);
		img_setImage = (ImageView) findViewById(R.id.imgSetimage);
		chk_terms = (CheckBox) findViewById(R.id.chk_terms);
	}

	public boolean validateEmail(String email) 
	{

		Pattern pattern;
		Matcher matcher;
		String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		return matcher.matches();

	}

	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				SignUpActivity.this);

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

}
