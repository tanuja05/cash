package com.chat_vendre_acheter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class SignInActivity extends FragmentActivity implements OnClickListener {
	EditText et_email, et_password;
	Button btn_signin, btn_facebook;
	TextView txtNewReg, txtForgotPassword;
	String email, password;
	ConnectionDetector cd;
	String msg, errmsg, s, forgotString, errormsg;
	SharedPreferences sharedPreferences_loginDetail;
	String token;
	GoogleCloudMessaging gcm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_sign_in);

		sharedPreferences_loginDetail = this.getSharedPreferences(
				"LOGIN_PREFS_NAME", 1);

		findIds();
		cd = new ConnectionDetector(SignInActivity.this);
		txtForgotPassword.setOnClickListener(SignInActivity.this);
		btn_signin.setOnClickListener(SignInActivity.this);
		txtNewReg.setOnClickListener(SignInActivity.this);
		txtNewReg.setPaintFlags(txtNewReg.getPaintFlags()
				| Paint.UNDERLINE_TEXT_FLAG);
	}

	private void findIds() {
		// TODO Auto-generated method stub
		et_email = (EditText) findViewById(R.id.et_email);
		et_password = (EditText) findViewById(R.id.et_Password);
		txtNewReg = (TextView) findViewById(R.id.txtNewRegistration);
		txtForgotPassword = (TextView) findViewById(R.id.txtForgotPasswrd);
		btn_signin = (Button) findViewById(R.id.btn_Signin);
		btn_facebook = (Button) findViewById(R.id.btn_facebook);
		
		btn_facebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				Intent i = new Intent(SignInActivity.this, TestConnect.class);
				startActivity(i);
			}
		});
//		btn_google_plus = (Button) findViewById(R.id.btn_google_plus);
	}

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		if (v.getId() == R.id.btn_Signin) {
			email = et_email.getText().toString();
			password = et_password.getText().toString();
			boolean abc = validateEmail(email);
			if (email.equals("") && et_password.equals("")) {
				errorDialog("Fields are empty");
			} else if (email.equals("") || abc == false) {

				errorDialog("Please Insert valid email-id");
			}

			if (cd.isConnectingToInternet()) {
				login();

			} else {
				errorDialog("No Internet Connection,You don't have internet connection");
			}
		}

		else if (v.getId() == R.id.txtNewRegistration) {
			Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
			startActivity(i);

		}
		

		else if (v.getId() == R.id.txtForgotPasswrd) {
		
			AlertDialog.Builder forgot = new AlertDialog.Builder(
					SignInActivity.this);
			forgot.setTitle("Forgot Password!");
			forgot.setMessage("Please enter Email id....");

			final EditText email_input = new EditText(SignInActivity.this);
			forgot.setView(email_input);

			forgot.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							forgotString = email_input.getText().toString();
							boolean ema = validateEmail(forgotString);
							if (ema == false) {
								Toast.makeText(SignInActivity.this,
										"Enter Valid Email", Toast.LENGTH_SHORT)
										.show();
							} else {
								forgotPassword();
							}

							dialog.dismiss();
						}
					});

			forgot.setNegativeButton("Cancle",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}
					});

			AlertDialog alertDialog = forgot.create();
			alertDialog.show();
		}

	}

	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				SignInActivity.this);

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

	public void login() {

		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(
					SignInActivity.this);

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				progressDialog.setMessage("Wait...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
				try {
					token = gcm.register(CommonUtilities.SENDER_ID);

				} catch (IOException e1) {

					token = "";
					e1.printStackTrace();
				}

				String url = "http://www.cashcash.today/cash/api/users/login";
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				try {

					multipartEntity.addPart("data[User][username]",
							new StringBody(et_email.getText().toString()));

					multipartEntity.addPart("data[User][password]",
							new StringBody(et_password.getText().toString()));
					multipartEntity.addPart("data[User][device_token]",
							new StringBody(token));
					httpPost.setEntity(multipartEntity);
					HttpResponse httpResponse = null;

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

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				progressDialog.dismiss();
				JSONObject jsonObject = null;
				try {
					jsonObject = new JSONObject(s);
					msg = jsonObject.getString("error");

					if (msg.equals("0")) {
					}
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				if (msg.equals("0")) {

					try {
						JSONObject jsonList = jsonObject.getJSONObject("list");
						JSONObject jsonUser = jsonList.getJSONObject("User");
						String id = jsonUser.getString("id");
						String profile_image = jsonUser.getString("image");
						String username = jsonUser.getString("username");
						String email = jsonUser.getString("email");
						String phone = jsonUser.getString("phone");
						String latitude = jsonUser.getString("latitude");
						String longitude = jsonUser.getString("longitude");

						Intent i = new Intent(SignInActivity.this,
								MainActivity.class);
						startActivity(i);

						Editor editor = sharedPreferences_loginDetail.edit();
						editor.putString("ID", id);
						editor.putString("PROFILE_IMAGE", profile_image);
						editor.putString("USERNAME", username);
						editor.putString("USERPASSWORD", et_password.getText()
								.toString());
						editor.putString("LAT", latitude);
						editor.putString("LONG", longitude);
						editor.commit();

					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}

				} else {
				}

			}

		};

		asyncTask.execute((Void[]) null);
	}

	void forgotPassword() {
		HttpClient httpclient = new DefaultHttpClient();
		AsyncTask<Void, Void, Void> Forgotpass = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(SignInActivity.this);

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setMessage("Waiting... ");
				dialog.setIndeterminate(true);
				dialog.setCancelable(false);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

				String url = "http://www.cashcash.today/cash/api/users/forgetpwd";

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				try {

					List<NameValuePair> nameValueForgot = new ArrayList<NameValuePair>(
							7);
					nameValueForgot.add(new BasicNameValuePair(
							"data[User][email]", forgotString));
					httppost.setEntity(new UrlEncodedFormEntity(nameValueForgot));
					HttpResponse response = httpclient.execute(httppost);
					s = EntityUtils.toString(response.getEntity());

				} catch (ClientProtocolException e) {
				} catch (IOException e) {

				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// what to do when background task is completed
				try {
					JSONObject ja = new JSONObject(s);
					msg = ja.getString("error");

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if (msg.equals("0")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SignInActivity.this);

					builder.setMessage("Email does not exists")
							.setCancelable(false)
							.setNegativeButton("Ok",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {

											dialog.dismiss();
										}
									});

					builder.show();

				} else {

					AlertDialog.Builder builder = new AlertDialog.Builder(
							SignInActivity.this);

					builder.setMessage(
							"Reset password link has been sent to your mail!")
							.setCancelable(false)
							.setNegativeButton("Ok",
									new DialogInterface.OnClickListener() {

										public void onClick(
												DialogInterface dialog,
												int which) {

										}
									});

					builder.show();

				}
				dialog.dismiss();

			}
		};
		Forgotpass.execute((Void[]) null);

	}

}
