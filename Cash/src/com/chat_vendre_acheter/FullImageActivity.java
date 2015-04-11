package com.chat_vendre_acheter;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.ImageLoader;

public class FullImageActivity extends FragmentActivity {
	ImageView back;
	RelativeLayout relativeLayout_back;
	ViewPager viewPager;
	ImageLoader imageLoader;
	String product_image;
	int pos;
	ConnectionDetector cd;
	ArrayList<String> img_array = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.full_image_activity);
		cd=new ConnectionDetector(FullImageActivity.this);
		viewPager = (ViewPager) findViewById(R.id.viewPagerMy);
		imageLoader = new ImageLoader(FullImageActivity.this);
		relativeLayout_back = (RelativeLayout) findViewById(R.id.rel);
		Bundle bndl=getIntent().getExtras();
		if(bndl!=null){
			pos=bndl.getInt("pos");
			
			img_array = bndl.getStringArrayList("img");
			Log.e("url", img_array+"");
		}
		
		viewPager.setAdapter(new PostPrePagerAdapter());
		viewPager.setCurrentItem(pos);
		
	
		
		relativeLayout_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.e("fin", "finish");
				finish();
			}
		});
		
		
	}
	
	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				FullImageActivity.this);

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
public class PostPrePagerAdapter extends PagerAdapter{

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return img_array.size();
		}
		
		public Object instantiateItem(View collection,final int position) {
			  ImageView view = new ImageView(FullImageActivity.this);
			  view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
			    LayoutParams.WRAP_CONTENT));
			if(cd.isConnectingToInternet()==true){
			
			  imageLoader.DisplayImage(img_array.get(position), view);}
			else {
				errorDialog("Oops, Something is wrong with internet connection");
			}
			  ((ViewPager) collection).addView(view, 0);
				
			  
			  return view;
			 }
		

		 @Override
		 public void destroyItem(View arg0, int arg1, Object arg2) {
		  ((ViewPager) arg0).removeView((View) arg2);
		 }

		 @Override
		 public boolean isViewFromObject(View arg0, Object arg1) {
		  return arg0 == ((View) arg1);
		 }

		 @Override
		 public Parcelable saveState() {
		  return null;
		 }

	}
}
