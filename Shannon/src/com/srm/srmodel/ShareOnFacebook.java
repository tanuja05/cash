package com.srm.srmodel;



import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;

public class ShareOnFacebook extends Activity {

	private Facebook mFacebook;
	private ProgressDialog mProgress;
	private Handler mRunOnUi = new Handler();
	protected String s;
	String name = "", lname = "", fname = "", gender = "";
	String email = "";
	String id = "";

	String st;
	String[] PERMISSIONS = { "offline_access", "publish_stream", "user_photos",
			"publish_checkins", "photo_upload", "publish_actions",
			"read_stream" };
	String regId, work, postdata, postFile;

	@TargetApi(Build.VERSION_CODES.GINGERBREAD) @SuppressLint("NewApi") @Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.fblogin);



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


	private void onFacebookClick() {
		
		if (mFacebook.isSessionValid()) 
		{
			
			//post();			
			postToFacebook();
//			Intent i=new Intent(ShareOnFacebook.this,FirstActivity.class);
//			startActivity(i);
//			finish();
		} else {
			
			mFacebook.authorize(this, PERMISSIONS, -1,
					new FbLoginDialogListener());
		}
	}

	private final class FbLoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			SessionStore.save(mFacebook, ShareOnFacebook.this);
		
			postToFacebook();
			
		}

		public void onFacebookError(FacebookError error) {
		

		}

		public void onError(DialogError error) {
			
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

	private void post() 
	{
		// TODO Auto-generated method stub
		AsyncTask<Void, Void, Void> updateTask1 = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(ShareOnFacebook.this);
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
					Log.e("ME",me);
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
					

					ba2 = imageURL.getBytes();
				
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
				SessionStore.clear(ShareOnFacebook.this);

				int what = 1;

				try {
					mFacebook.logout(ShareOnFacebook.this);

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

				SessionStore.saveName(username, ShareOnFacebook.this);

			} else {
				
			}
		}
	};

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			mProgress.dismiss();

			
		}
	};

	private void postToFacebook() {
		
		mProgress.setMessage("Posting ...");
		mProgress.show();

		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);

		Bundle parameters = new Bundle();
		parameters.putString("access_token", mFacebook.getAccessToken());
		Log.e("ACESSE",  mFacebook.getAccessToken()+"");
		parameters.putString("link", "https://www.facebook.com/pages/Shannon-Robinson/150261808475125?ref=bookmarks");
		 mAsyncFbRunner.request("me/feed", parameters, "POST",
				 new WallPostListener());

//		mFacebook.dialog(ShareOnFacebook.this,"feed",parameters,new DialogListener() {
//
//		        @Override
//		        public void onFacebookError(FacebookError arg0) {
//		        //Display your message for facebook error   
//
//		        }
//
//		        @Override
//		        public void onError(DialogError arg0) {
//		        //Display your message for error    
//
//		        }
//
//		        @Override
//		        public void onComplete(Bundle arg0) {
//		        //Display your message on share scuccessful
//
//		        }
//
//		        @Override
//		        public void onCancel() {
//		        //Display your message on dialog cancel
//
//		        }
//		    });
	
	}
	private final class WallPostListener extends BaseRequestListener {
		  private Handler mRunOnUi = new Handler();

		  public void onComplete(final String response) {
		   mRunOnUi.post(new Runnable() {
		    public void run() {
		    mProgress.dismiss();
//		      Toast.makeText(ShareOnFacebook.this, "Posted to Facebook",
//		      Toast.LENGTH_SHORT).show();
		     finish();
		    }
		   });
		  }

		  public void onIOException(IOException e) {
		   // TODO Auto-generated method stub

		  }

		  public void onFileNotFoundException(FileNotFoundException e) {
		   // TODO Auto-generated method stub

		  }

		  public void onMalformedURLException(MalformedURLException e) {
		   // TODO Auto-generated method stub

		  }

		  public void onFacebookError(FacebookError e) {
		   // TODO Auto-generated method stub

		  }
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
			
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				// Add your data
				try {

					HttpResponse response;

					response = httpclient.execute(httppost);
					rsp = EntityUtils.toString(response.getEntity());
				
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
					
						// listPosts();
					} else if (err.equals("1")) {
						
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