package com.chat_vendre_acheter;

import java.util.ArrayList;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

public class Settings extends FragmentActivity{

	ArrayList<String> setting_item_list = new ArrayList<String>();
	ListView setting_listView; 
	Spinner spin;
	ImageView edt_profile,back;
	SettingAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.settings_menu);
	//	spin=(Spinner)findViewById(R.id.spin);
	//	spin.setVisibility(View.GONE);
		//edt_profile=(ImageView)findViewById(R.id.edt_profile);
		back=(ImageView)findViewById(R.id.back);
		setting_listView = (ListView) findViewById(R.id.list_view);
		adapter = new SettingAdapter();
		
		setting_item_list.add("Links to Social Network");
		setting_item_list.add("Language");
		setting_item_list.add("Location Sevices");
		setting_item_list.add("Manage Blocked Users");
		setting_item_list.add("Email Notification");
		setting_item_list.add("Push Notification");
//		setting_item_list.add("Watch Tutorial");
//		setting_item_list.add("Chat With Customes Service");
		setting_item_list.add("Community And Privacy Policy");
		
		setting_listView.setAdapter(adapter);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	
	public class SettingAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return setting_item_list.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			
			
			LayoutInflater inflater = getLayoutInflater();
			View rootView  = inflater.inflate(R.layout.setting_list_item, null);
			
			TextView setting_txt = (TextView) rootView.findViewById(R.id.setting_item_text);
			ImageView setting_img = (ImageView) rootView.findViewById(R.id.setting_item_img1);
			ToggleButton setting_toggle = (ToggleButton) rootView.findViewById(R.id.setting_itemtoggleButton1);
			
			setting_txt.setText(setting_item_list.get(arg0));
			if (arg0 == 1) {
				setting_img.setVisibility(View.VISIBLE);
				setting_toggle.setVisibility(View.GONE);
			} else if (arg0 == 2) {
				setting_img.setVisibility(View.GONE);
				setting_toggle.setVisibility(View.VISIBLE);
			} else {
				setting_img.setVisibility(View.GONE);
				setting_toggle.setVisibility(View.GONE);
			}
			
			rootView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(arg0==6)
					{
						
						Intent i=new Intent(Settings.this, PrivacyPolicyActivity.class);
						startActivity(i);
					}
				}
			});
			return rootView;
		}
		
	}
}



