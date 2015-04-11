package com.srm.srmodel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class SecondScreenActivity extends Activity {
	Typeface ttf,ttf1;
	ImageView imageView2;
	TextView txtShannon, txtoptions, txtgallery, txtassu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_second_screen);
//		ttf = Typeface.createFromAsset(this.getAssets(),
//				"LinotypeZapfino One.ttf");
		ttf1 = Typeface.createFromAsset(this.getAssets(), "Angelique ma douce Colombe.ttf");
		ttf = Typeface.createFromAsset(this.getAssets(), "METAL KINGDOM.ttf");
		imageView2 = (ImageView) findViewById(R.id.imageView2);
		txtShannon = (TextView) findViewById(R.id.txtShannon);
		txtoptions = (TextView) findViewById(R.id.txtOptions);
		txtgallery = (TextView) findViewById(R.id.txtGallery);
		txtassu = (TextView) findViewById(R.id.txtassu);

		txtassu.setTypeface(ttf);
		txtoptions.setTypeface(ttf);
		txtgallery.setTypeface(ttf);
		txtShannon.setTypeface(ttf1);
	txtgallery.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(SecondScreenActivity.this,
					GalleryActivity.class);
			startActivity(i);
			finish();
		}
	});
		txtassu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SecondScreenActivity.this,
						TalkAussie.class);
				startActivity(i);
				finish();
			}
		});
		txtoptions.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SecondScreenActivity.this,
						OptionsActivity.class);
				startActivity(i);
			
			}
		});
		txtShannon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent ii = new Intent(SecondScreenActivity.this,
						SplashActivity.class);

				startActivity(ii);
			
			}
		});
		imageView2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SecondScreenActivity.this,
						ShareOnFacebook.class);
				startActivity(i);
				
			}
		});
	}
}
