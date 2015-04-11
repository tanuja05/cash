package com.chat_vendre_acheter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.ImageLoader;

public class ChatFullImage extends FragmentActivity{

	ImageView imageView, back;
	ImageLoader imageLoader;
	ConnectionDetector cd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_full_image);
		cd=new ConnectionDetector(ChatFullImage.this);
		Intent intent = getIntent();
		String chat_image_url = intent.getStringExtra("chat_image");
		if(cd.isConnectingToInternet()==true)
		{
		imageLoader = new ImageLoader(ChatFullImage.this);
		}
		else
		{
			
		}
		imageView = (ImageView) findViewById(R.id.chat_image);
		 back = (ImageView) findViewById(R.id.back);
		if(cd.isConnectingToInternet()==true){
		imageLoader.DisplayImage(chat_image_url, imageView);
		} else 
		{
			errorDialog("Oops something is wrong wwith Internet Connection");
		}
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			finish();	
			}
		});
		
		
	}
	public void errorDialog(String str) 
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ChatFullImage.this);
		// set title
		alertDialogBuilder.setTitle("Cash");
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		// set dialog message
		alertDialogBuilder.setMessage("" + str).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						dialog.dismiss();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
}
