package com.fw.RescueSheets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

public class SplashScreen extends FragmentActivity {
Fragment fragment = null;


	@Override
	protected void onCreate( Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
	
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
			Intent intent = new Intent(SplashScreen.this, Search.class);
			startActivity(intent);
			finish();
			
				
			}
		
			
		}, 3000);
		
		
	}
}
