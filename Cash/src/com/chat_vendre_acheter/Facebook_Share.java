package com.chat_vendre_acheter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class Facebook_Share extends Activity {
	String[] PERMISSIONS = { "offline_access", "publish_stream", "user_photos",
			   "publish_checkins",  "publish_actions",
			   "read_stream" };
	Facebook facebook;
	String browser, grid_post, product_name,product_price, product_image_url;
	
	@SuppressLint("NewApi")
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.fblogin);
		
		browser = getIntent().getStringExtra("Browse");
//		grid_post = getIntent().getStringExtra("Adpost");
		product_name = getIntent().getStringExtra("product_name");
		product_price = getIntent().getStringExtra("product_price");
		product_image_url = getIntent().getStringExtra("product_image");
		
		facebook = new Facebook(getResources().getString(
				R.string.facebook_app_id));
		
		if (facebook.isSessionValid()) {

			//facebookSharing("testing sharing on fb");
			postWall();
			Log.e("vvvv", "testing sharing on fb");
		} else {
			facebook.authorize(Facebook_Share.this, PERMISSIONS, -1,
					new FbLoginDialogListener());
		}

	}
	
	
	
	private void postWall(){
			try {
				AsyncFacebookRunner asyncFacebookRunner = new AsyncFacebookRunner(
						facebook);
			//	Bundle bundle = new Bundle();

				/*if (browser.equals("Browser")) {
					bundle.putString("message", "Cash Application \n"+"Hi! Check out Cash, it's a cool app for buying and selling. It has a built-in chat.\n\n"+"https://www.facebook.com/profile.php?id=100006562237232");
				} else if (grid_post.equals("Adpost")) {*/
				//	bundle.putString("message", "Cash Application \n\n" + "Look what I found on Melltoo!\n\n" + product_name +"-"+ product_price + "\n\n Download Melltoo today to see my item. (Tip: Search by item name to find it quickly)"+ "\n\n https://play.google.com/store/apps/details?id=com.ta.ak.melltoo.activity\n\n"+ product_image_url);
					
			//	}
			
					if (browser.equals("Browse")) {
						Log.e("BRO", "bro");
						Bundle bundle = new Bundle();
						bundle.putString("message", "Cash Application \n"+"Hi! Check out Cash, it's a cool app for buying and selling. It has a built-in chat.\n\n"+"https://www.facebook.com/profile.php?id=100006562237232");
						asyncFacebookRunner.request("me/feed", bundle, "POST",new WallPostListener());
					}  else if (browser.equals("Adpost")) {
						Log.e("BRO1", "bro1");
						Bundle bundle1 = new Bundle();
							bundle1.putString("message", "Cash Application \n\n" + "Look what I found on Cash!\n\n" + product_name +"-"+ product_price + "\n\n Download Cash today to see my item. (Tip: Search by item name to find it quickly)"+ "\n\n https://www.dummy.com\n\n"+ product_image_url);
							asyncFacebookRunner.request("me/feed", bundle1, "POST",new WallPostListener());
							
							}
					
					
//				asyncFacebookRunner.request("me/feed", bundle, "POST",
//						new WallPostListener());
			} catch (Exception e) {
				e.printStackTrace();
			}

			

		
	}
private final class FbLoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
		//	SessionStore.save(facebook, getActivity());

			//facebookSharing("share on facebook by frootfal.");
			postWall();
		}

		public void onFacebookError(FacebookError error) {
			Toast.makeText(Facebook_Share.this, "Facebook connection failed",
					Toast.LENGTH_SHORT).show();

		}

		public void onError(DialogError error) {
			Toast.makeText(Facebook_Share.this, "Facebook connection failed",
					Toast.LENGTH_SHORT).show();

		}

		public void onCancel() {

		}
	}

private final class WallPostListener extends BaseRequestListener {
	private Handler mRunOnUi = new Handler();

	public void onComplete(final String response) {
		mRunOnUi.post(new Runnable() {
			public void run() {
				 Toast.makeText(Facebook_Share.this, "Posted to Facebook",
				 Toast.LENGTH_SHORT).show();
				finish();
			}
		});
	}
}
}
