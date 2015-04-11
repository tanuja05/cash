package com.srm.srmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;


public class FirstActivity extends Activity {
	TextView txt_swipe,textView1;
	Typeface ttf,ttf1;
	SharedPreferences spf;
	Dialog dialog;
	String login;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_first);
		txt_swipe = (TextView) findViewById(R.id.txt_swipe);
		textView1=(TextView)findViewById(R.id.textView1);
		ttf1 = Typeface.createFromAsset(this.getAssets(), "Angelique ma douce Colombe.ttf");
		ttf = Typeface.createFromAsset(this.getAssets(), "METAL KINGDOM.ttf");
		txt_swipe.setTypeface(ttf);
		textView1.setTypeface(ttf1);
		spf = FirstActivity.this.getSharedPreferences("login", 1);
		login = spf.getString("loginname", "0");
		// getting hash keyyyy
			/*  try {
			   PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
			   for (Signature signature : info.signatures) {
			    MessageDigest md;

			    md = MessageDigest.getInstance("SHA");
			    md.update(signature.toByteArray());
			    String something = new String(Base64.encode(md.digest(), 0));
			    Log.d("Hash key", something);
			   } 
			  }
			  catch (NameNotFoundException e1) {
			   // TODO Auto-generated catch block
			   Log.e("name not found", e1.toString());
			  }

			  catch (NoSuchAlgorithmException e) {
			   // TODO Auto-generated catch block
			   Log.e("no such an algorithm", e.toString());
			  }
			  catch (Exception e){
			   Log.e("exception", e.toString());
			  }*/



			
		
		
		
		if (login.equals("0")) 
		{
			txt_swipe.setVisibility(View.GONE);
			dialog = new Dialog(FirstActivity.this, R.style.DialogTheme);
			dialog.setContentView(R.layout.login_layout);
			dialog.setCancelable(false);
			ImageView btn_signin = (ImageView) dialog.findViewById(R.id.button1);

			btn_signin.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					Intent i=new Intent(FirstActivity.this,TestConnect.class);
					startActivity(i);
					dialog.dismiss();
//					Editor edit = spf.edit();
//					edit.putString("loginname", "testing");
//					edit.commit();
					txt_swipe.setVisibility(View.VISIBLE);
//					dialog.dismiss();
				}
			});
			Display mDisplay = FirstActivity.this.getWindowManager()
					.getDefaultDisplay();
			final int width = mDisplay.getWidth();
			final int height = mDisplay.getHeight();
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
			lp.copyFrom(dialog.getWindow().getAttributes());
			lp.width = width;
			lp.height = height;
			dialog.getWindow().setAttributes(lp);
			dialog.show();

		}

	
			
		txt_swipe.setVisibility(View.VISIBLE);
		txt_swipe.setOnTouchListener(new OnSwipeTouchListener(
				FirstActivity.this) {
			public void onSwipeTop() {
				
			}

			public void onSwipeRight() 
			{
				
				Intent i = new Intent(FirstActivity.this,
						SecondScreenActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				finish();

			}

			public void onSwipeLeft() 
			{
				Intent i = new Intent(FirstActivity.this,
						SecondScreenActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(i);
				finish();
			}

			public void onSwipeBottom() 
			{
				
			}

		});
		
	}

	public class OnSwipeTouchListener implements OnTouchListener {

		private final GestureDetector gestureDetector;

		public OnSwipeTouchListener(Context ctx) {
			gestureDetector = new GestureDetector(ctx, new GestureListener());
		}

		private final class GestureListener extends SimpleOnGestureListener {

			private static final int SWIPE_THRESHOLD = 100;
			private static final int SWIPE_VELOCITY_THRESHOLD = 100;

			@Override
			public boolean onDown(MotionEvent e) {
				return true;
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				boolean result = false;
				try {
					float diffY = e2.getY() - e1.getY();
					float diffX = e2.getX() - e1.getX();
					if (Math.abs(diffX) > Math.abs(diffY)) {
						if (Math.abs(diffX) > SWIPE_THRESHOLD
								&& Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
							if (diffX > 0) {
								onSwipeRight();
							} else {
								onSwipeLeft();
							}
						}
						result = true;
					} else if (Math.abs(diffY) > SWIPE_THRESHOLD
							&& Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
						if (diffY > 0) {
							onSwipeBottom();
						} else {
							onSwipeTop();
						}
					}
					result = true;

				} catch (Exception exception) {
					exception.printStackTrace();
				}
				return result;
			}
		}

		public void onSwipeRight() {
		}

		public void onSwipeLeft() {
		}

		public void onSwipeTop() {
		}

		public void onSwipeBottom() {
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return gestureDetector.onTouchEvent(event);
		}

	}
}
