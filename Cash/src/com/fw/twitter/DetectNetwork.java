package com.fw.twitter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class DetectNetwork {
	
	
	
	public static boolean hasConnection(Context con) {
    	ConnectivityManager connectivityManager = (ConnectivityManager) con
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected())
		{
			return true;
		}

        return false;
      }
}
