package com.chat_vendre_acheter;

import java.io.File;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;

public class Google_Plus_Share extends Activity implements //OnClickListener,
		ConnectionCallbacks, OnConnectionFailedListener {
	
	String browser, grid_post, product_name,product_price, product_image_url;
	
	private static final int RC_SIGN_IN = 0;
	// Logcat tag
	private static final String TAG = "MainActivity";

	// Profile pic image size in pixels
	private static final int PROFILE_PIC_SIZE = 400;

	// Google client to interact with Google API
	private GoogleApiClient mGoogleApiClient;

	/**
	 * A flag indicating that a PendingIntent is in progress and prevents us
	 * from starting further intents.
	 */
	private boolean mIntentInProgress;

	private boolean mSignInClicked;

	private ConnectionResult mConnectionResult;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		browser = getIntent().getStringExtra("Browse");
		product_name = getIntent().getStringExtra("product_name");
		product_price = getIntent().getStringExtra("product_price");
		product_image_url = getIntent().getStringExtra("product_image");
		
		mGoogleApiClient = new GoogleApiClient.Builder(this)
		.addConnectionCallbacks(this)
		.addOnConnectionFailedListener(this).addApi(Plus.API)
		.addScope(Plus.SCOPE_PLUS_LOGIN).build();
		
		
		signInWithGplus();
	
	}
	
	

	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
		
	}

	protected void onStop() {
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	/**
	 * Method to resolve any signin errors
	 * */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
			} catch (SendIntentException e) {
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		

		if (!mIntentInProgress) {
			// Store the ConnectionResult for later usage
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to
				// resolve all
				// errors until the user is signed in, or they cancel.
				
				resolveSignInError();
			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int responseCode,
			Intent intent) {
		getPost();
		
		
		if (requestCode == RC_SIGN_IN) {
			if (responseCode != RESULT_OK) {
				mSignInClicked = false;
			}

			mIntentInProgress = false;

			if (!mGoogleApiClient.isConnecting()) {
				mGoogleApiClient.connect();
			}
		}
		
	
	}

	@Override
	public void onConnected(Bundle arg0) {
		mSignInClicked = false;
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	}

	

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();

	}

	
	
	/**
	 * Sign-in into google
	 * */
	private void signInWithGplus() {
		if (!mGoogleApiClient.isConnecting()) {
			mSignInClicked = true;
	//		resolveSignInError();
		}
	}

	
	@SuppressLint("SdCardPath")
	private void getPost() {
		
		getPostCode();
	
}

private void getPostCode() {
	// TODO Auto-generated method stub
	
	if (browser.equals("Browse")) {
		 Intent shareIntent = new PlusShare.Builder(this)
	     .setType("text/plain")
	     .setText("Cash Application \n"+"Hi! Check out Cash, it's a cool app for buying and selling. It has a built-in chat.\n\n"+"https://www.facebook.com/profile.php?id=100006562237232")
	     .getIntent();
		 startActivity(shareIntent);
	} else if (browser.equals("Adpost")) {
		Intent shareIntent = new PlusShare.Builder(this)
	     .setType("text/plain")
	     .setText("Cash Application \n\n" + "Look what I found on Cash!\n\n" + product_name +"-"+ product_price + "\n\n Download Cash today to see my item. (Tip: Search by item name to find it quickly)"+ "\n\n https://www.dummy.com\n\n"+ "Product Pic - "+ product_image_url)
	     .getIntent();
		 startActivity(shareIntent);
	}
	
    // .setStream(Uri.parse("file:" + image))
     

 //startActivityForResult(shareIntent, 0);
	
	 
	 finish();
}
	

}

