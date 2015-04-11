package com.srm.srmodel;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;



public class RotateWallpaperService extends Service
{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
@Override
public void onStart(Intent intent, int startId) {
	// TODO Auto-generated method stub
	super.onStart(intent, startId);
}
@Override
public int onStartCommand(Intent intent, int flags, int startId) {
	// TODO Auto-generated method stub
	return super.onStartCommand(intent, flags, startId);
}
@Override
public void onDestroy() {
	// TODO Auto-generated method stub
	super.onDestroy();
}

}
