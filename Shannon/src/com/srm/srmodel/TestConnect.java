package com.srm.srmodel;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;

public class TestConnect extends Activity {

	private Facebook mFacebook;
	private ProgressDialog mProgress;
	private Handler mRunOnUi = new Handler();
	protected String s;
	String name = "", lname = "", fname = "", gender = "";
	String email = "";
	String id = "";
	String login;
	SharedPreferences spf;
	String st;
	String[] PERMISSIONS = { "offline_access", "publish_stream", "user_photos",
			"publish_checkins", "photo_upload", "publish_actions",
			"read_stream" };
	String regId, work, postdata, postFile;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") @Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.fblogin);
		
		
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
		.detectNetwork() // or .detectAll() for all detectable problems
		.penaltyDialog() // show a dialog
		.build());


		spf = TestConnect.this.getSharedPreferences("login", 1);
	//	login = spf.getString("loginname", "0");
		mProgress = new ProgressDialog(this);
		mFacebook = new Facebook(getResources().getString(R.string.app_id));

		SessionStore.restore(mFacebook, this);

		if (mFacebook.isSessionValid()) 
		{

			String name = SessionStore.getName(this);
			name = (name.equals("")) ? "Unknown" : name;
			
		}

		onFacebookClick();

		// IntentFilter intentFilter = new IntentFilter();
		// intentFilter.addAction("com.package.ACTION_LOGOUT");
		// registerReceiver(log, intentFilter);
	}

	// LogOutReciever log = new LogOutReciever();

	protected String error;

	//
	// @Override
	// protected void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	// unregisterReceiver(log);
	// }

	private void onFacebookClick() {
		if (mFacebook.isSessionValid()) 
		{
			Editor edit = spf.edit();
			edit.putString("loginname", name);
			edit.commit();
			
			Intent i=new Intent(TestConnect.this,FirstActivity.class);
			startActivity(i);
			finish();
			
		} else {

			mFacebook.authorize(this, PERMISSIONS, -1,
					new FbLoginDialogListener());
			
		}
	}

	private final class FbLoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionStore.save(mFacebook, TestConnect.this);
			Editor edit = spf.edit();
			edit.putString("loginname", name);
			edit.commit();
			
			Intent i=new Intent(TestConnect.this,FirstActivity.class);
			startActivity(i);
			finish();
			
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(TestConnect.this, "Facebook connection failed",
					Toast.LENGTH_SHORT).show();

		}

		public void onError(DialogError error) {
			Toast.makeText(TestConnect.this, "Facebook connection failed",
					Toast.LENGTH_SHORT).show();

		}

		public void onCancel() {

		}
	}

	String imageURL;
	private byte[] ba2;

	public Bitmap getUserPic(String userID) {

		Bitmap bitmap = null;
		// Log.d("TAG", "Loading Picture");
		imageURL = "http://graph.facebook.com/" + userID
				+ "/picture?type=small";
		// Log.i("images fb", "" + imageURL);
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(imageURL)
					.getContent());
		} catch (Exception e) {
			// Log.d("TAG", "Loading Picture FAILED");
			e.printStackTrace();
		}
		return bitmap;
	}

	private String dob;

	private void post() {
		// TODO Auto-generated method stub
		AsyncTask<Void, Void, Void> updateTask1 = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(TestConnect.this);
			String url = null;

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setTitle("Register you Shannon Robinsons");
				dialog.setCancelable(false);
				dialog.setIndeterminate(true);
				dialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					String me = mFacebook.request("me");

					JSONObject jsonObj = (JSONObject) new JSONTokener(me)
							.nextValue();
					name = jsonObj.getString("name");
					fname = jsonObj.getString("first_name");
					lname = jsonObj.getString("last_name");
					id = jsonObj.getString("id");
					email = jsonObj.getString("email");
					dob = jsonObj.getString("birthday");
					gender = jsonObj.getString("gender");

					getUserPic(id);
					Log.e("imageURL", "" + imageURL);

					ba2 = imageURL.getBytes();
					Log.e("fb", name + "  ==  " + email + "  ==  " + id
							+ "  ===  " + dob + "  ===  ");
					Log.e("ba2", "" + ba2);
					// what = 0;
					// post();

				} catch (Exception ex) {
					ex.printStackTrace();
				}
		

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// what to do when background task is completed

				dialog.dismiss();


			}
		};
		// if ((DetectNetwork.hasConnection(getApplicationContext())))
		updateTask1.execute((Void[]) null);

	}

	String getDeviceDetail() {

		return Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);

	}

	private void fbLogout() {
		mProgress.setMessage("Disconnecting from Facebook");
		mProgress.show();

		new Thread() {
			@Override
			public void run() {
				SessionStore.clear(TestConnect.this);

				int what = 1;

				try {
					mFacebook.logout(TestConnect.this);

					what = 0;
				} catch (Exception ex) {
					ex.printStackTrace();
				}

				mHandler.sendMessage(mHandler.obtainMessage(what));
			}
		}.start();
	}

	private Handler mFbHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			if (msg.what == 0) {
				String username = (String) msg.obj;
				username = (username.equals("")) ? "No Name" : username;

				SessionStore.saveName(username, TestConnect.this);

				Toast.makeText(TestConnect.this,
						"Connected to Facebook as " + username,
						Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(TestConnect.this, "Connected to Facebook",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			if (msg.what == 1) {
				Toast.makeText(TestConnect.this, "Facebook logout failed",
						Toast.LENGTH_SHORT).show();
			} else {

				Toast.makeText(TestConnect.this, "Disconnected from Facebook",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void postToFacebook() {
		mProgress.setMessage("Posting ...");
		mProgress.show();

		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);

		Bundle parameters = new Bundle();
		parameters.putString("access_token", mFacebook.getAccessToken());
		// parameters.putString("name", "Where's The Run App");
		parameters.putString("link", "https://www.facebook.com/pages/Shannon-Robinson/150261808475125?ref=bookmarks");
	/*	if (!postdata.equals("NA")) {
			Log.e("postData &&&&&&&&", postFile);
			parameters.putString("message", postdata);
			if (!postFile.equals("NA")) {
				if (postFile.endsWith(".png") || postFile.endsWith(".jpg")) {
					parameters.putString("picture", postFile);

					Log.e("postFile img data", postFile);
				} else {
					if (postFile.length() == 11) {
						String url = "http://www.youtube.com/embed/" + postFile;
						Log.e("postFile videooo link url", url);
						parameters.putString("link", url);
					} else {
						Log.e("postFile videooo link", postFile);
						parameters.putString("link", postFile);
					}
				}
			}
		} else if (!postFile.equals("NA")) {
			if (!postdata.equals("NA")) {
				Log.e("postData &&&&&&&&", postFile);
				parameters.putString("message", postdata);

			}
			Log.e("postFile********** ", postFile);
			Log.e("image bytes", postFile.getBytes() + "");
			if (postFile.endsWith(".png") || postFile.endsWith(".jpg")) {
				parameters.putString("picture", postFile);

				Log.e("postFile img", postFile);
			} else {

				if (postFile.length() == 11) 
				{
					String url = "http://www.youtube.com/embed/" + postFile;
					Log.e("postFile videooo link url", url);
					parameters.putString("link", url);
				} else {

					Log.e("postFile videooo link", postFile);
					parameters.putString("link", postFile);
				}
				// parameters.putByteArray("video", postFile.getBytes());
			}
		}*/

		// parameters.putString("name",
		// "Name of your application/ any name you want to post");
		// parameters.putString("caption", " caption if any!");
		// /parameters.putString("picture", "Link to your image");

		try {

			mFacebook.request("me");
			String response = mFacebook.request("me/feed", parameters, "POST");
			Log.e("Tests", "got response: " + response);
			mProgress.cancel();
			// call webservice here for share
			
			Intent intent = new Intent(TestConnect.this, FirstActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} catch (Exception e) {
			mProgress.cancel();
			// Log.e("Tests", e.printStackTrace());
			e.printStackTrace();
		}
		// mAsyncFbRunner.request("me/feed", params, "POST",
		// new WallPostListener());
	}

	String rsp;

	void share(final String pId) {
		AsyncTask<Void, Void, Void> likePost = new AsyncTask<Void, Void, Void>() {
			// ProgressDialog dialog = new
			// ProgressDialog(MessageViewActivity.this);

			@Override
			protected void onPreExecute() {
				// what to do before background task
				// dialog.setMessage("Validating... ");
				// dialog.setIndeterminate(true);
				// dialog.show();
			}

			@SuppressLint("SimpleDateFormat")
			@Override
			protected Void doInBackground(Void... params) {
				String url = "abc";
				// String url = "getResources().getString(R.string.url)
				// + "shares/app_shareadds/" + pId + "/" + u_id + "/"
				// + "facebook"";
				Log.e("like url", url);
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				// Add your data
				try {

					HttpResponse response;

					response = httpclient.execute(httppost);
					rsp = EntityUtils.toString(response.getEntity());
					Log.e("test connect", rsp);
					// System.out.println("login fff  " + rsp);
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@SuppressLint("NewApi")
			@Override
			protected void onPostExecute(Void result) {
				// what to do when background task is completed
				// edSms.setText("");
				try {
					JSONObject jObject = new JSONObject(rsp);

					String rsp_msg = jObject.getString("msg");
					String err = jObject.getString("error");
					// Log.v("error val", rsp_msg);

					if (err.equals("0")) {
						Toast.makeText(TestConnect.this, rsp_msg, Toast.LENGTH_SHORT).show();
						
						// listPosts();
					} else if (err.equals("1")) {
						Toast.makeText(TestConnect.this, rsp_msg,Toast.LENGTH_SHORT).show();
					}

				} catch (JSONException e) {
					// Log.e("error", "EXCEPTION^^^^");
					e.printStackTrace();
				}

				// dialog.dismiss();

			}
		};
		// if ((DetectNetwork.hasConnection(getApplicationContext())))
		likePost.execute((Void[]) null);
	}


}