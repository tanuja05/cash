package com.srm.srmodel;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class OptionsActivity extends Activity {
	public static PendingIntent pendingIntent1;
	TextView txtShare, txtcalender, txtunlock, txtRate;
	Typeface ttf,ttf1;
	ToggleButton togle_walppaper;
ImageView imageView1;
SharedPreferences spfwallrot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_options);
		ttf1 = Typeface.createFromAsset(this.getAssets(), "Angelique ma douce Colombe.ttf");
		ttf = Typeface.createFromAsset(this.getAssets(), "METAL KINGDOM.ttf");
		togle_walppaper = (ToggleButton) findViewById(R.id.textView3);
	//	txtshannon = (TextView) findViewById(R.id.txtshannon);
		txtShare = (TextView) findViewById(R.id.txtShare);
		txtcalender = (TextView) findViewById(R.id.txtcalender);
//		txtoptions = (TextView) findViewById(R.id.txtoptions);
		imageView1=(ImageView)findViewById(R.id.imageView1);
		txtunlock = (TextView) findViewById(R.id.txtunlock);
		txtRate = (TextView) findViewById(R.id.txtRate);
		spfwallrot=OptionsActivity.this.getSharedPreferences("rotation", 1);
		txtShare.setTypeface(ttf);
		txtcalender.setTypeface(ttf);
		txtRate.setTypeface(ttf);
		txtunlock.setTypeface(ttf);
		imageView1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent(OptionsActivity.this,ShareOnFacebook.class);
				startActivity(i);
				finish();
			}
		});
		txtRate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try
				{
				    final String pkgName = OptionsActivity.this.getPackageName();
				    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkgName)));
				} catch (Exception e) 
				{
				    Toast.makeText(getApplicationContext(), "Please Try again later",1).show();
				}
			}
		});
		togle_walppaper
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
					//	Toast.makeText(getApplicationContext(), "hee", Toast.LENGTH_SHORT).show();
						if (isChecked) 
						{
							alarm();
							Editor edit=spfwallrot.edit();
							edit.putBoolean("rot", true);
							edit.commit();
//							Log.e("infooooo", "Button2 is on!");							
						} else {
//							Log.e("infoooo", "Button2 is off!");
//							stopService(new Intent(OptionsActivity.this,WallpaperService.class));
						}
					}
				});

	}
	public void alarm() {
//		Log.v("!!!!", "alarm function");
		try {

			Intent myIntent = new Intent(OptionsActivity.this, WallpaperService.class);
			pendingIntent1 = PendingIntent
					.getService(OptionsActivity.this, 0, myIntent, 0);
			AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.HOUR, 1);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					calendar.getTimeInMillis(), 1*60*60 * 1000, pendingIntent1);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
