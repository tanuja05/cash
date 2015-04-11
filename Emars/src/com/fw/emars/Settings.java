package com.fw.emars;

import java.util.ArrayList;
import java.util.HashMap;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ViewHolder", "InflateParams" })
public class Settings extends Activity {
	String[] popUpContents;
	ListView listView1;
	Typeface ttf;
	TextView textView1;
	String uri = null;
	static Activity ctx;
	NumberAdapter mNum;
	String number;
	ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_settings);
		mNum = new NumberAdapter(Settings.this);
		mNum.open();
		arrayList = mNum.fetchNumbers();
		mNum.close();
		textView1 = (TextView) findViewById(R.id.textView1);
		popUpContents = new String[] { "Add or update number", "Event Logs",
				"Set Alert Tones", "About Energy Demand Alert", "Get Log",
				"Get Meter Readings", "Exit" };
		listView1 = (ListView) findViewById(R.id.listView1);
		listView1.setAdapter(new MyAdapter());
		ttf = Typeface.createFromAsset(getAssets(), "Transformers Movie.ttf");
		textView1.setTypeface(ttf);
		ctx = this;
		listView1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position12, long arg3) {
				if (position12 == 0) {
					Intent i = new Intent(Settings.this, NumberScreen.class);
					startActivity(i);

					finish();
				} else if (position12 == 1) {
					if (arrayList.size() == 0) {
						Intent i = new Intent(Settings.this, NumberScreen.class);
						startActivity(i);
						finish();
					} else {

						Intent i = new Intent(Settings.this, MainActivity.class);
						startActivity(i);

						finish();
					}
				} else if (position12 == 2) {
					Intent intent = new Intent(
							RingtoneManager.ACTION_RINGTONE_PICKER);
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
							RingtoneManager.TYPE_NOTIFICATION);
					intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
							"Select sound");

					if (uri != null) {
						intent.putExtra(
								RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
								Uri.parse(uri));
					}

					else {
						intent.putExtra(
								RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
								(Uri) null);
					}
					startActivityForResult(intent, 0);
				} else if (position12 == 3) {
					Intent i = new Intent(Settings.this,
							AboutEnergyActivity.class);
					startActivity(i);
					finish();

				} else if (position12 == 4) {
					if (arrayList.size() == 0) {
						Intent i = new Intent(Settings.this, NumberScreen.class);
						startActivity(i);
						finish();
					} else {

						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
								Settings.this);

						// set title
						alertDialogBuilder.setTitle("Energy Demand Alert");
						// set dialog message
						alertDialogBuilder
								.setMessage("Are you sure you want to Get Log?")
								.setCancelable(false)
								.setPositiveButton("Yes",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												try {

													SmsManager smsManager = SmsManager
															.getDefault();

													if (arrayList.get(0)
															.get("first")
															.contains("-")) {
														String number = arrayList
																.get(0)
																.get("first")
																.replace("-",
																		"");
														Log.e("nnnLOGGG",
																number);

														smsManager
																.sendTextMessage(
																		number,
																		null,
																		"%LOG",
																		null,
																		null);
													}

												} catch (Exception e) {
													Toast.makeText(
															getApplicationContext(),
															"SMS faild, please try again later!",
															Toast.LENGTH_LONG)
															.show();
													e.printStackTrace();
												}

											}
										})
								.setNegativeButton("No",
										new DialogInterface.OnClickListener() {
											public void onClick(
													DialogInterface dialog,
													int id) {
												dialog.cancel();

											}
										});
						// create alert dialog
						AlertDialog alertDialog = alertDialogBuilder.create();
						// show it
						alertDialog.show();
					}
				} else if (position12 == 5) {

					if (arrayList.size() == 0) {
						Intent i = new Intent(Settings.this, NumberScreen.class);
						startActivity(i);
						finish();
					} else {
						Intent i = new Intent(Settings.this,
								GetMeterReadingActivity.class);
						i.putExtra("from", "settings");
						startActivity(i);
						finish();

						// AlertDialog.Builder alertDialogBuilder = new
						// AlertDialog.Builder(
						// Settings.this);
						//
						// // set title
						// alertDialogBuilder.setTitle("Energy Demand Alert");
						// // set dialog message
						// alertDialogBuilder
						// .setMessage("Are you sure you want to Get Current meter Readings?")
						// .setCancelable(false)
						// .setPositiveButton("Yes",
						// new DialogInterface.OnClickListener() {
						// public void onClick(
						// DialogInterface dialog, int id) {
						// try {
						// SmsManager smsManager = SmsManager
						// .getDefault();
						// String[] fist_no = arrayList
						// .get(0).get("first")
						// .split("-");
						//
						// smsManager.sendTextMessage(fist_no[1],
						// null, "%CNT", null, null);
						//
						//
						// } catch (Exception e) {
						// Toast.makeText(
						// getApplicationContext(),
						// "SMS faild, please try again later!",
						// Toast.LENGTH_LONG)
						// .show();
						// e.printStackTrace();
						// }
						//
						// }
						// })
						// .setNegativeButton("No",
						// new DialogInterface.OnClickListener() {
						// public void onClick(
						// DialogInterface dialog, int id) {
						// dialog.cancel();
						//
						// }
						// });
						// // create alert dialog
						// AlertDialog alertDialog =
						// alertDialogBuilder.create();
						// // show it
						// alertDialog.show();

					}
				} else if (position12 == 6) {
					finish();

					System.exit(0);
				}
			}
		});
	}

	public class MyAdapter extends BaseAdapter {

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
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.popup_inflate, null);
			TextView txt = (TextView) convertView.findViewById(R.id.textView1);
			txt.setText(popUpContents[position]);
			return convertView;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, intent);
		if (resultCode == RESULT_OK) {
			Uri uri = intent
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if (uri != null) {
				@SuppressWarnings("unused")
				String ringTonePath = uri.toString();
				// Set the Ringtone to Alarm
				RingtoneManager.setActualDefaultRingtoneUri(Settings.this,
						RingtoneManager.TYPE_NOTIFICATION, uri);

			}
		}
	}

}
