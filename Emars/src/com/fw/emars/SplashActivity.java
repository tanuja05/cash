package com.fw.emars;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class SplashActivity extends Activity {

	NumberAdapter mNum;
	private static int SPLASH_TIME_OUT = 3000;

	ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		mNum = new NumberAdapter(getApplicationContext());
		mNum.open();
		myList = mNum.fetchNumbers();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if (myList.size() < 1) {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							SplashActivity.this);
					// set title
					alertDialogBuilder.setTitle("Energy Demand Alert");
					// set dialog message
					alertDialogBuilder
							.setMessage(
									"You are using first time this app. To proceed tap ok to add numbers")
							.setCancelable(false)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											dialog.dismiss();
											Intent i = new Intent(
													SplashActivity.this,
													NumberScreen.class);
											startActivity(i);
											finish();

										}
									});
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();
					// show it
					alertDialog.show();
				}
				Intent i = new Intent(SplashActivity.this, NumberScreen.class);
				startActivity(i);
				finish();
			}

		}, SPLASH_TIME_OUT);
	}

}
