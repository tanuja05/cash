package com.chat_vendre_acheter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

public class TabFragment extends Fragment implements OnTabChangeListener {
	Fragment fragment = null;
	FragmentTabHost mTabHost;
	ArrayList<String> spinValue;
	PopupWindow popupWindowGroups;
	String popUpContents[];
	String login_id;
	SharedPreferences sharedPreferences_loginDetail;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		popUpContents = new String[] { "All", "Chats", "Reviews",
				"Likes & Follows" };

		mTabHost = new FragmentTabHost(getActivity());

		mTabHost.setup(getActivity(), getChildFragmentManager(),
				R.id.realtabcontent);
		sharedPreferences_loginDetail = getActivity().getSharedPreferences(
				"LOGIN_PREFS_NAME", 1);
		
		login_id = sharedPreferences_loginDetail.getString("USERNAME", "noo");

		mTabHost.addTab(mTabHost.newTabSpec("Browse").setIndicator("Browse"),
				BrowseFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("Maps").setIndicator("Maps"),
				MapsFragment.class, null);
		
		
			mTabHost.addTab(mTabHost.newTabSpec("Activity")
					.setIndicator("Activity"), ActivityFragment.class, null);
		
		
		
		

		Bundle bundle = getActivity().getIntent().getExtras();
		if (bundle != null) {
			int abc = bundle.getInt("DefaultTab");
			mTabHost.setCurrentTab(abc);
		}

		for (int i = 0; i < mTabHost.getTabWidget().getChildCount(); i++) {
			View v = mTabHost.getTabWidget().getChildAt(i);
			v.setBackgroundResource(R.drawable.tabline);

		}
		return mTabHost;

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mTabHost = null;
	}

	private PopupWindow popupWindowGroups() {
		PopupWindow popupWindow = new PopupWindow(getActivity());
		// the drop down list is a list view
		ListView gViewDogs = new ListView(getActivity());
		gViewDogs.setAdapter(new Myadapter());
		// set the item click listener
		popupWindow.setFocusable(true);
		popupWindow.setWidth(150);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		// set the list view as pop up window content
		popupWindow.setContentView(gViewDogs);
		return popupWindow;
	}

	public class Myadapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return popUpContents.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.popup_inflate, null);
			TextView txt = (TextView) convertView.findViewById(R.id.textView1);

			txt.setText(popUpContents[position]);
			txt.setTag(position);
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					popupWindowGroups.dismiss();
				}
			});
			return convertView;
		}

	}

	@Override
	public void onTabChanged(String tabId) {

	}
}