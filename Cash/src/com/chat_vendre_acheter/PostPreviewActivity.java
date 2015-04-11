package com.chat_vendre_acheter;

import java.util.ArrayList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chat_vendre_acheter.extra.ImageLoader;
import com.chat_vendre_acheter.extra.PostPrePagerAdapter;

public class PostPreviewActivity extends FragmentActivity {

	TextView txt_user_name, txt_price, txt_desc, txt_cati, txt_location,
			txt_product_name;
	ImageView profile_pic, img_back, arrow;
	ViewPager viewPager;
	byte[] b_one, b_two, b_three;
	String user_name, price, desc, cati, location, product_name,
			user_profile_url;
	SharedPreferences preferences;
	ImageLoader imageLoader;
	RelativeLayout rel_desc, rel_desc_contain;
	boolean isFirstViewClick = false;
	ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.post_privew);

		imageLoader = new ImageLoader(PostPreviewActivity.this);
		preferences = this.getSharedPreferences("LOGIN_PREFS_NAME", 1);
		user_name = preferences.getString("USERNAME", "def").toString();
		user_profile_url = preferences.getString("PROFILE_IMAGE", "def12")
				.toString();

		txt_user_name = (TextView) findViewById(R.id.textView4);
		txt_price = (TextView) findViewById(R.id.textView8);
		txt_cati = (TextView) findViewById(R.id.textView3);
		txt_location = (TextView) findViewById(R.id.textView5);
		txt_desc = (TextView) findViewById(R.id.editText1);
		img_back = (ImageView) findViewById(R.id.img_back);
		txt_product_name = (TextView) findViewById(R.id.textView2);
		profile_pic = (ImageView) findViewById(R.id.imageView2);
		viewPager = (ViewPager) findViewById(R.id.viewPagerMy);
		arrow = (ImageView) findViewById(R.id.imageView10);
		rel_desc = (RelativeLayout) findViewById(R.id.lytFragmentDescription);
		rel_desc_contain = (RelativeLayout) findViewById(R.id.cntnr_postreview);

		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Intent intent = getIntent();
		b_one = intent.getByteArrayExtra("image1");
		b_two = intent.getByteArrayExtra("image2");
		b_three = intent.getByteArrayExtra("image3");
		price = intent.getStringExtra("price");
		desc = intent.getStringExtra("description");
		cati = intent.getStringExtra("cateigory");
		location = intent.getStringExtra("location");
		product_name = intent.getStringExtra("product_name");

		if (b_one != null) {

			Bitmap bitmap1 = BitmapFactory.decodeByteArray(b_one, 0,
					b_one.length);
			bitmaps.add(bitmap1);
		}

		if (b_two != null) {
			Bitmap bitmap2 = BitmapFactory.decodeByteArray(b_two, 0,
					b_two.length);
			bitmaps.add(bitmap2);
		}

		if (b_three != null) {
			Bitmap bitmap3 = BitmapFactory.decodeByteArray(b_three, 0,
					b_three.length);
			bitmaps.add(bitmap3);
		}

		PostPrePagerAdapter adapter = new PostPrePagerAdapter(this, bitmaps);
		viewPager.setAdapter(adapter);
		viewPager.setCurrentItem(0);

		txt_cati.setText(cati);
		txt_location.setText(location);
		txt_price.setText(price);
		txt_product_name.setText(product_name);
		txt_user_name.setText(user_name);
		if (!user_profile_url.equals("def12")) {
			imageLoader.DisplayImage(user_profile_url, profile_pic);
		}

		rel_desc.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (isFirstViewClick == false) {
					isFirstViewClick = true;
					txt_desc.setText(desc);
					arrow.setBackgroundResource(R.drawable.up);
					rel_desc_contain.setVisibility(View.VISIBLE);
				} else {
					isFirstViewClick = false;
					arrow.setBackgroundResource(R.drawable.down);
					rel_desc_contain.setVisibility(View.GONE);
				}
				return false;
			}
		});

	}
}
