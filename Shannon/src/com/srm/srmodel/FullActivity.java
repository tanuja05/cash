package com.srm.srmodel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.WallpaperManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.fw.Database.PurchaseAdapter;
import com.fw.GalleryWidget.BasePagerAdapter.OnItemChangeListener;
import com.fw.GalleryWidget.FilePagerAdapter;
import com.fw.GalleryWidget.GalleryViewPager;
import com.fw.TouchView.FileTouchImageView;

public class FullActivity extends Activity {
	// ViewPager viewPager;
	private GalleryViewPager mViewPager;
	int[] images;
	public static int pos, p = -1;
	Button button1;
	Typeface ttf;
	String status, imname;
	PurchaseAdapter pAdap;
	ArrayList<HashMap<String, String>> purchase_statusList = new ArrayList<HashMap<String, String>>();
	List<Integer> items = new ArrayList<Integer>();
	List<String> items1 = new ArrayList<String>();
	public static boolean first = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_full);
		System.gc();
		button1 = (Button) findViewById(R.id.button1);
		ttf = Typeface.createFromAsset(this.getAssets(),
				"Always In My Heart.ttf");
		button1.setTypeface(ttf);
		Bundle bndl = getIntent().getExtras();
		if (bndl != null) {			
			imname = bndl.getString("imname");			
		}
		pAdap = new PurchaseAdapter(FullActivity.this);
		items.add(R.drawable.screen1);
		items.add(R.drawable.screen2);
		items.add(R.drawable.screen3);
		items.add(R.drawable.screen4);
		items.add(R.drawable.screen5);
		items.add(R.drawable.screen6);
		items.add(R.drawable.screen7);
		items.add(R.drawable.screen8);
		items.add(R.drawable.screen9);
		items.add(R.drawable.screen10);
		items.add(R.drawable.screen11);
		items.add(R.drawable.screen12);

		items1.add("screen1");
		items1.add("screen2");
		items1.add("screen3");
		items1.add("screen4");
		items1.add("screen5");
		items1.add("screen6");
		items1.add("screen7");
		items1.add("screen8");
		items1.add("screen9");
		items1.add("screen10");
		items1.add("screen11");
		items1.add("screen12");

		pAdap.open();

		purchase_statusList = pAdap
				.searchPurchase("com.srm.srmodel.shannon_001");
		if (purchase_statusList.size() > 0) {

			if (purchase_statusList.get(0).get("purchase_status")
					.equals("unlock")) {
				items.add(R.drawable.screen13);
				items1.add("screen13");
			}

		}
		purchase_statusList = pAdap
				.searchPurchase("com.srm.srmodel.shannon_002");
		if (purchase_statusList.size() > 0)

		{

			if (purchase_statusList.get(0).get("purchase_status")
					.equals("unlock")) {
				items.add(R.drawable.screen14);
				items1.add("screen14");
			}
		}
		purchase_statusList = pAdap
				.searchPurchase("com.srm.srmodel.shannon_003");
		if (purchase_statusList.size() > 0) {

			if (purchase_statusList.get(0).get("purchase_status")
					.equals("unlock")) {
				items.add(R.drawable.screen15);
				items1.add("screen15");
			}
		}
		purchase_statusList = pAdap
				.searchPurchase("com.srm.srmodel.shannon_004");
		if (purchase_statusList.size() > 0) {

			if (purchase_statusList.get(0).get("purchase_status")
					.equals("unlock")) {
				items.add(R.drawable.screen16);
				items1.add("screen16");
			}
		}
		purchase_statusList = pAdap
				.searchPurchase("com.srm.srmodel.shannon_005");
		if (purchase_statusList.size() > 0) {

			if (purchase_statusList.get(0).get("purchase_status")
					.equals("unlock")) {
				items.add(R.drawable.screen17);
				items1.add("screen17");
			}
		}
		purchase_statusList = pAdap
				.searchPurchase("com.srm.srmodel.shannon_007");
		if (purchase_statusList.size() > 0) {

			if (purchase_statusList.get(0).get("purchase_status")
					.equals("unlock")) {
				items.add(R.drawable.screen18);
				items1.add("screen18");
			}
		}

	
		for (int i = 0; i < items.size(); i++) {
			if (imname.equals(items1.get(i).toString().trim())) {

			
				pos = i;

				break;
			}

		}
		FilePagerAdapter pagerAdapter = new FilePagerAdapter(this, items);
		pagerAdapter.setOnItemChangeListener(new OnItemChangeListener() {
			@Override
			public void onItemChange(int currentPosition) {
				
				if(currentPosition==0){}
				else{
				p = 1;}
				//Toast.makeText(getApplicationContext(), "Helloo"+p, 1).show();
			}
		});

		mViewPager = (GalleryViewPager) findViewById(R.id.viewPagerMy);
		mViewPager.setOffscreenPageLimit(items.size());	
		mViewPager.setCurrentItem(pos);

		mViewPager.setAdapter(pagerAdapter);

		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				WallpaperManager wallManager = WallpaperManager
						.getInstance(FullActivity.this);
				try {
					Display display = getWindowManager().getDefaultDisplay();
					int SCREEN_WIDTH = display.getWidth();
					int SCREEN_HEIGHT = display.getHeight();//
					wallManager.setWallpaperOffsetSteps(1, 1);
					wallManager.suggestDesiredDimensions(SCREEN_WIDTH,
							SCREEN_HEIGHT);
				
					if (p < 0) 
					{						
						wallManager.setResource(items.get(FullActivity.pos));
						p = 1;
					}

					else {						
						wallManager.setResource(items.get(mViewPager
								.getCurrentItem()));
					}
					Toast.makeText(FullActivity.this,
							"Wallpaper Set Successfully!!", Toast.LENGTH_SHORT)
							.show();
				} catch (IOException e) {
					Toast.makeText(FullActivity.this,
							"Setting WallPaper Failed!!", Toast.LENGTH_SHORT)
							.show();
				}

			}
		});

	}

	@Override
	public void onBackPressed() {
		first = true;
		p=-1;
		super.onBackPressed();
	}

}
