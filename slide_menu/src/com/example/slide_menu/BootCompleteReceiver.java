package com.example.slide_menu;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootCompleteReceiver extends BroadcastReceiver {
Context ctx;
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e("STarteeessss", "onreceive callledddddddddddddddddddd");
		Intent i=new Intent(context,ServiceStartboot.class);
	   context.startService(i);
	}
		
	

}
