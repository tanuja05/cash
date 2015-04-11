package com.chat_vendre_acheter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class SplashScreen extends FragmentActivity {
	SharedPreferences shPreferences;
	String location;
	boolean fst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		shPreferences = SplashScreen.this.getSharedPreferences("LOC", 1);
		location = shPreferences.getString("locati", "0");
		fst = shPreferences.getBoolean("first", false);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {

				if (fst == false) {
					Intent intent = new Intent(SplashScreen.this,
							LocationActivity.class);
					intent.putExtra("splash", "splash");
					startActivity(intent);
					finish();

				} else {
					Intent intent = new Intent(SplashScreen.this,
							MainActivity.class);

					intent.putExtra("Location", location);
					startActivity(intent);
					finish();

				}

			}

		}, 3000);

	}
}
