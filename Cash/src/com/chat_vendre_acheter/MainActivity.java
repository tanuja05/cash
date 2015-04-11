package com.chat_vendre_acheter;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.ImageLoader;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {
	Fragment fragment = null;
	SharedPreferences shPref;
	private CharSequence mDrawerTitle;
	String data;
	private CharSequence mTitle;
	ActionBar act_Bar;
	String login_id, imgurl;
	ImageLoader iLoader;
	SharedPreferences sharedPreferences_loginDetail;
	ConnectionDetector cd;

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragment = new TabFragment();
		cd = new ConnectionDetector(MainActivity.this);
		sharedPreferences_loginDetail = MainActivity.this.getSharedPreferences(
				"LOGIN_PREFS_NAME", 1);
		login_id = sharedPreferences_loginDetail.getString("USERNAME", "noo");
		imgurl = sharedPreferences_loginDetail.getString("PROFILE_IMAGE",
				"piccccccc");

		act_Bar = getActionBar();
		act_Bar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#4b0082")));
		act_Bar.setCustomView(R.layout.actionbar_search);
		act_Bar.setDisplayShowCustomEnabled(true);
		if (fragment != null) 
		{
			FragmentManager manager = getSupportFragmentManager();

			FragmentTransaction fragmentTransaction = manager
					.beginTransaction();
			manager.beginTransaction().replace(R.id.frame_container, fragment)
					.commit();

		} else {
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

	}
}
