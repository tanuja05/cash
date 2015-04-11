package com.srm.srmodel;

import java.io.IOException;

import android.app.Service;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;

public class WallpaperService extends Service{
	int[] wallarray;
	WallpaperManager wallManager ;
	 int prev=0;
	 Drawable drawable;
	public WallpaperService(){
		super();
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		wallarray = new int[] {R.drawable.screen1,
				R.drawable.screen2, R.drawable.screen3, R.drawable.screen4,
				R.drawable.screen5, R.drawable.screen6, R.drawable.screen7,
				R.drawable.screen8, R.drawable.screen9, R.drawable.screen10,
				R.drawable.screen11};
		
		Log.e("OnCreate", "oncreate");
		wallManager = WallpaperManager.getInstance(WallpaperService.this);
		
	}

	@Override
	@Deprecated
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		
		setWall();
				
	}
	
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		return super.onUnbind(intent);
	}
	
	public void setWall(){
		
		if (prev == 0) {
			drawable = getResources().getDrawable(R.drawable.screen1);
			prev = 1;
//			Log.e("LOG", "0,1");
		} else if (prev == 1) {
			drawable = getResources().getDrawable(R.drawable.screen2);
			prev = 2;
//			Log.e("LOG", "1,2");
		} else if (prev == 2) {
			drawable = getResources().getDrawable(R.drawable.screen3);
			prev = 3;
//			Log.e("LOG", "2,3");
		}else if (prev == 3) {
			drawable = getResources().getDrawable(R.drawable.screen4);
			prev = 4;
//			Log.e("LOG", "3,4");
		} else if (prev == 4) {
			drawable = getResources().getDrawable(R.drawable.screen5);
			prev = 5;
//			Log.e("LOG", "4,5");
		} else if (prev == 5) {
			drawable = getResources().getDrawable(R.drawable.screen6);
			prev = 6;
//			Log.e("LOG", "5,6");
		} else if (prev == 6) {
			drawable = getResources().getDrawable(R.drawable.screen7);
			prev = 7;
//			Log.e("LOG", "6,7");
		} else if (prev == 7) {
			drawable = getResources().getDrawable(R.drawable.screen8);
			prev = 8;
//			Log.e("LOG", "7,8");
		} else if (prev == 8) {
			drawable = getResources().getDrawable(R.drawable.screen9);
			prev = 9;
//			Log.e("LOG", "8,9");
		} else if (prev == 9) {
			drawable = getResources().getDrawable(R.drawable.screen10);
			prev = 10;
//			Log.e("LOG", "9,10");
		} else if (prev == 10) {
			drawable = getResources().getDrawable(R.drawable.screen11);
			prev = 11;
//			Log.e("LOG", "10,11");
		} 
		
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		try {
			wallManager.setBitmap(bitmap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
			//	Toast.makeText(WallpaperService.this, "Wallpaper Set Successfully!!", Toast.LENGTH_SHORT).show();
	
	}
	}
