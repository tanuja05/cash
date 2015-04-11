package com.fw.twitter;

import java.io.File;
import java.io.OutputStream;

import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class TwitterShare extends Activity {
	Twitter twitter;
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	String browser, grid_post, product_name,product_price, product_image_url;
	RequestToken requestToken;
	private File file;
	private Uri selectedItems;
	private static SharedPreferences mSharedPreferences;

	OutputStream output;
	String status,status1;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mSharedPreferences = getApplicationContext().getSharedPreferences(
				"MyPref1", 0);
		
		browser = getIntent().getStringExtra("Browse");
		product_name = getIntent().getStringExtra("product_name");
		product_price = getIntent().getStringExtra("product_price");
		product_image_url = getIntent().getStringExtra("product_image");
		status = getIntent().getStringExtra("Status");
		Log.e("STATUS", status);
		//status1 = "Cash Application \n\n" + "Look what I found on Melltoo!\n\n" + product_name +"-"+ product_price + "\n\n Download Melltoo today to see my item. (Tip: Search by item name to find it quickly)"+ "\n\n https://play.google.com/store/apps/details?id=com.ta.ak.melltoo.activity\n\n"+ "Product Pic - "+ product_image_url;
	status1 = "CASH";
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);

		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		if ((DetectNetwork.hasConnection(getApplicationContext()))) {
			Toast.makeText(getApplicationContext(),
					"Redirecting you on twitter", 2).show();
			ConfigurationBuilder confbuilder = new ConfigurationBuilder();
			Configuration conf = confbuilder
					.setOAuthConsumerKey(Const.CONSUMER_KEY)
					.setOAuthConsumerSecret(Const.CONSUMER_SECRET).build();
			twitter = new TwitterFactory(conf).getInstance();
			twitter.setOAuthAccessToken(null);
			try {
				requestToken = twitter.getOAuthRequestToken(Const.CALLBACK_URL);

				Intent intent = new Intent(getApplicationContext(),
						TwitterLogin.class);
				intent.putExtra(Const.IEXTRA_AUTH_URL,
						requestToken.getAuthorizationURL());
				startActivityForResult(intent, 0);

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 0 && resultCode == RESULT_OK) {

			AccessToken accessToken = null;
			try {
				String oauthVerifier = data.getExtras().getString(
						Const.IEXTRA_OAUTH_VERIFIER);
				accessToken = twitter.getOAuthAccessToken(requestToken,
						oauthVerifier);
				SharedPreferences pref = getSharedPreferences(Const.PREF_NAME,
						MODE_PRIVATE);
				SharedPreferences.Editor editor = pref.edit();
				editor.putString(Const.PREF_KEY_ACCESS_TOKEN,
						accessToken.getToken());
				editor.putString(Const.PREF_KEY_ACCESS_TOKEN_SECRET,
						accessToken.getTokenSecret());
				editor.commit();
				if (requestToken != null) {

					if (browser.equals("Browse")) {

						getPostCodeBrowse();
						} /*else if (browser.equals("Adpost")) {
						getPostCodeAd();
						}*/

				}
				Toast.makeText(this, "authorized", Toast.LENGTH_SHORT).show();

			} catch (TwitterException e) {
				e.printStackTrace();
			}
		}

		if (requestCode == 1 && resultCode == RESULT_OK) {
			
			if (browser.equals("Browse")) {

			getPostCodeBrowse();
			} /*else if (browser.equals("Adpost")) {
			getPostCodeAd();
			}*/
		}
	}

	private void getPostCodeBrowse() {

		if (status.trim().length() > 0) {
			{
				new updateTwitterStatus(status).execute();

			}

		} else {

		}
	}
	
	private void getPostCodeAd() {

		if (status1.trim().length() > 0) {
			{
				Log.e("Add", "Post");
				new updateTwitterStatus1(status1).execute();

			}

		} else {

		}
	}
	

	class updateTwitterStatus extends AsyncTask<String, String, String> {
		String msg = "Hello";
		private ProgressDialog pDialog;

		public updateTwitterStatus(String status) {
			msg = status;
			
		}
		
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TwitterShare.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(Const.CONSUMER_KEY);
				builder.setOAuthConsumerSecret(Const.CONSUMER_SECRET);
				Configuration config = builder.build();

				// Access Token
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				Log.i(access_token, access_token_secret);

				StatusUpdate status = new StatusUpdate(msg);
				twitter4j.Status response = twitter.updateStatus(status);
				Log.d("Status", "> " + response.getText());
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();
					finish();
					// Clearing EditText field
					// txtUpdate.setText("");
					// image.setImageResource(R.drawable.stub);
					// imageSelected=false;
				}
			});
		}

	}
	
	
	
	
	class updateTwitterStatus1 extends AsyncTask<String, String, String> {
		String msg = "Hello";
		private ProgressDialog pDialog;

		public updateTwitterStatus1(String status1) {
			msg = status1;
			
		}
		
		

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(TwitterShare.this);
			pDialog.setMessage("Updating to twitter...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {

			try {
				ConfigurationBuilder builder = new ConfigurationBuilder();
				builder.setOAuthConsumerKey(Const.CONSUMER_KEY);
				builder.setOAuthConsumerSecret(Const.CONSUMER_SECRET);
				Configuration config = builder.build();

				// Access Token
				String access_token = mSharedPreferences.getString(
						PREF_KEY_OAUTH_TOKEN, "");
				// Access Token Secret
				String access_token_secret = mSharedPreferences.getString(
						PREF_KEY_OAUTH_SECRET, "");

				Log.i(access_token, access_token_secret);

				StatusUpdate status = new StatusUpdate(msg);
				twitter4j.Status response = twitter.updateStatus(status);
				Log.d("Status", "> " + response.getText());
			} catch (TwitterException e) {
				// Error in updating status
				Log.d("Twitter Update Error", e.getMessage());
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog and show
		 * the data in UI Always use runOnUiThread(new Runnable()) to update UI
		 * from background thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"Status tweeted successfully", Toast.LENGTH_SHORT)
							.show();
					finish();
					// Clearing EditText field
					// txtUpdate.setText("");
					// image.setImageResource(R.drawable.stub);
					// imageSelected=false;
				}
			});
		}

	}
}
