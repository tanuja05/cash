package com.fw.emars;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint({ "SimpleDateFormat", "ViewHolder", "InflateParams" })
public class MainActivity extends Activity {
	String f_no, s_no, t_no;
	TextView textView1;
	Typeface ttf, ttf1;
	static Activity ctx;
	ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> arrayListDB = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> hashMap;
	HashMap<String, String> hashMapDB;
	RecordAdapter recAdap;// =new RecordAdapter(MainActivity.this);
	GetReadingAdapter gadp;
	CustomAdapter adapter1;
	CustomreadingAdapter adapter;
	ImageView imageView1;
	String k_factr, currentDateTimeString;
	String popUpContents[];
	TextView button1, button2;
	ListView lViewSMS, lstCNt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		ctx = this;
		recAdap = new RecordAdapter(MainActivity.this);
		gadp = new GetReadingAdapter(MainActivity.this);
		ttf = Typeface.createFromAsset(getAssets(), "Transformers Movie.ttf");
		ttf1 = Typeface.createFromAsset(getAssets(), "TradeGothic.ttf");
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		textView1 = (TextView) findViewById(R.id.textView1);
		textView1.setTypeface(ttf);
		button1 = (TextView) findViewById(R.id.button1);
		button2 = (TextView) findViewById(R.id.button2);
		currentDateTimeString = DateFormat.getDateTimeInstance().format(
				new Date());

		NumberAdapter adap = new NumberAdapter(getApplicationContext());
		adap.open();

		myList = adap.fetchNumbers();
		if (myList.size() == 1) {
			String[] fist_no = myList.get(0).get("first").split("-");
			String[] sec_no = myList.get(0).get("sec").split("-");
			String[] third_no = myList.get(0).get("third").split("-");
			k_factr = myList.get(0).get("K_factor");
			f_no = fist_no[0] + fist_no[1];
			s_no = sec_no[0] + sec_no[1];
			t_no = third_no[0] + third_no[1];
			adap.close();

			adapter1 = new CustomAdapter(this);
			adapter = new CustomreadingAdapter(this);
			lViewSMS = (ListView) findViewById(R.id.listViewSMS);
			lstCNt = (ListView) findViewById(R.id.listViewCNT);
			imageView1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent i = new Intent(MainActivity.this, Settings.class);
					startActivity(i);
					finish();
				}
			});
			button1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					lstCNt.setVisibility(View.GONE);
					lViewSMS.setVisibility(View.VISIBLE);
					if (fetchInbox() != null) {
					
						lViewSMS.setAdapter(adapter1);
					}
				}
			});
			button2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					lstCNt.setVisibility(View.VISIBLE);
					lViewSMS.setVisibility(View.GONE);
					if (fetchCNT() != null) {
					

						lstCNt.setAdapter(adapter);
					}
				}
			});

			if (fetchInbox() != null) {
			
				lViewSMS.setAdapter(adapter1);
			}

		} else {
			Intent i = new Intent(MainActivity.this, NumberScreen.class);
			startActivity(i);
		}
	}

	@SuppressWarnings("rawtypes")
	public ArrayList fetchInbox() {
		recAdap.open();
		arrayListDB.clear();
		arrayListDB = recAdap.fetchMessage();
		ArrayList sms = new ArrayList();

		arrayList.clear();

		if (arrayListDB.size() > 0) {

			for (int k = 0; k < arrayListDB.size(); k++) {
				String address = arrayListDB.get(k).get("number");
				String body = arrayListDB.get(k).get("message");
				String time1 = arrayListDB.get(k).get("time");
				String id = arrayListDB.get(k).get("id");
				hashMap = new HashMap<String, String>();
				hashMap.put("number", address);
				hashMap.put("msg", body);
				hashMap.put("time", time1);
				String gh[] = { f_no, s_no, t_no };

				if (body.contains("! DEMAND CAPACITY ALERT !")) {
					for (int i = 0; i < gh.length; i++) {
						if (address.startsWith(gh[i])) {
							if (time1.equals("31 Oct 2014 17:09:42")) {
								recAdap.deleteExpenseCategory(Long
										.parseLong(id));
							} else {
								arrayList.add(hashMap);
							}

						}
					}
				}
			}
		}

		return sms;

	}

	@SuppressWarnings("rawtypes")
	public ArrayList fetchCNT() {
		gadp.open();
		arrayListDB.clear();
		arrayListDB = gadp.fetchMessage();
		ArrayList sms = new ArrayList();

		if (arrayListDB.size() > 0) {

			for (int k = 0; k < arrayListDB.size(); k++) {
				String address = arrayListDB.get(k).get("number");
				String body = arrayListDB.get(k).get("message");
				String time1 = arrayListDB.get(k).get("time");
				hashMap = new HashMap<String, String>();
				hashMap.put("number", address);
				hashMap.put("msg", body);
				hashMap.put("time", time1);
				arrayList.add(hashMap);

				
			}

		}

		return sms;

	}

	class CustomAdapter extends BaseAdapter {
		public CustomAdapter(Context context) {

		}

		@Override
		public int getCount() {

			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View view = inflater.inflate(R.layout.sms_item, null);

			TextView txt_no = (TextView) view.findViewById(R.id.textView1);
			TextView txt_msg = (TextView) view.findViewById(R.id.textView2);
			TextView textView3 = (TextView) view.findViewById(R.id.textView3);
			textView3.setText(arrayList.get(position).get("time") + " ");
			txt_no.setText(arrayList.get(position).get("number"));
			txt_msg.setText(arrayList.get(position).get("msg") + "\n");
			textView3.setTypeface(ttf1);
			txt_no.setTypeface(ttf1);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(MainActivity.this,
							ResultSettingActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("number", arrayList.get(position).get("number"));
					i.putExtra("body", arrayList.get(position).get("msg"));
					i.putExtra("K_factor", k_factr);
					startActivity(i);
					finish();
				}
			});
			return view;
		}
	}

	class CustomreadingAdapter extends BaseAdapter {
		public CustomreadingAdapter(Context context) {
		}

		@Override
		public int getCount() {

			return arrayListDB.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = LayoutInflater
					.from(getApplicationContext());
			View view = inflater.inflate(R.layout.cnt_inflate, null);

			TextView txt_no = (TextView) view.findViewById(R.id.textView1);
			TextView txt_msg = (TextView) view.findViewById(R.id.textView2);
			TextView textView3 = (TextView) view.findViewById(R.id.textView3);

			textView3.setText(arrayListDB.get(position).get("time") + " ");
			txt_no.setText(arrayListDB.get(position).get("number"));
			txt_msg.setText(arrayListDB.get(position).get("message") + "\n");
			textView3.setTypeface(ttf1);
			txt_no.setTypeface(ttf1);
			view.setOnClickListener(new OnClickListener() {
			//	%CNT: time slot 15 (07:30) KVAhr 49, KWhr 41, KLitres 20, MJoules 12
				@Override
				public void onClick(View v) {
					Intent i = new Intent(MainActivity.this,
							GetMeterReadingActivity.class);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
					i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					i.putExtra("from", "main");
					i.putExtra("number", arrayListDB.get(position)
							.get("number"));
					i.putExtra("body", arrayListDB.get(position).get("message"));
					i.putExtra("time", arrayListDB.get(position).get("time"));

					i.putExtra("K_factor", k_factr);
					startActivity(i);
					finish();

				}
			});

			return view;
		}
	}

}
