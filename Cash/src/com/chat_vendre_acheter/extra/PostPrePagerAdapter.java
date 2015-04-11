package com.chat_vendre_acheter.extra;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class PostPrePagerAdapter extends PagerAdapter{

	FragmentActivity activity;
	ArrayList<Bitmap> arrayList = new ArrayList<Bitmap>();
	
	
	
	public PostPrePagerAdapter(FragmentActivity act, ArrayList<Bitmap> imgArra) {
		arrayList = imgArra;
		  activity = act;
		 }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrayList.size();
	}
	
	public Object instantiateItem(View collection, int position) {
		  ImageView view = new ImageView(activity);
		  view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		    LayoutParams.FILL_PARENT));
		  view.setScaleType(ScaleType.FIT_XY);
		  view.setImageBitmap(arrayList.get(position));
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
