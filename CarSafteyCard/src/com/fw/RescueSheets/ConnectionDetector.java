package com.fw.RescueSheets;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
	
	private Context _context;
	
	public ConnectionDetector(Context context){
		this._context = context;
	}

	public boolean isConnectingToInternet(){
		ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		  if (connectivity != null) 
		  {
			  NetworkInfo[] info = connectivity.getAllNetworkInfo();
			  if (info != null) 
				  for (int i = 0; i < info.length; i++) 
					  if (info[i].getState() == NetworkInfo.State.CONNECTED)
					  {
						  return true;
					  }

		  }
		  return false;
	}
}
// The last thing is the download 
//function of the whole database to the mobile device, which is not running. After showing message 
//“Rettungskarten – Data has been schedule to download. Make sure your internet speed should be high to download
//complete data (cancel / ok)” nothing happened.
//
//Here should come a message like “data is downloading” after pressing button “ok” and if it´s already downloaded “Data is 
//already saved on your mobile device” or something like that.
// 
//And if all data is saved on mobile device it´s important, that searching function directly uses the saved files 
//and not the online version, because the app has to work also, when no data connection is possible.
// 
//Thank you very much in advance!
// 
// 
//Best regards,