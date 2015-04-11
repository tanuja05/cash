package com.fw.emars;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.SmsManager;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GetMeterReadingActivity extends Activity {
	Button btn_clear;
	TextView textView1;
	Typeface ttf, ttf1;
	ImageView imageView1;
	public static String number, body, KFctor, time;
	String KVAhr, KWhr, joule, litres;

	public static boolean getread;
	public static TextView textView3, txt_a12Value, txt_a10Value, textView2,
			txt_power_result, txt_amp_result, txt_voltage_result;
	Button btn_Getreading;
	GetReadingAdapter gadp;
	HashMap<String, String> hashMap;
	ArrayList<HashMap<String, String>> myList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> arrayListDB = new ArrayList<HashMap<String, String>>();
	public static TextView txt_voltage, txt_amps, txt_power, txt_a12, txt_a10;
	NumberAdapter adap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.get_meter_reading_activity);
		getread = true;
		textView1 = (TextView) findViewById(R.id.textView1);
		btn_clear = (Button) findViewById(R.id.btn_clear);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		txt_a10Value = (TextView) findViewById(R.id.txt_a10Value);
		txt_voltage_result = (TextView) findViewById(R.id.txt_voltage_result);
		txt_amp_result = (TextView) findViewById(R.id.txt_amp_result);
		txt_power_result = (TextView) findViewById(R.id.txt_power_result);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		txt_a12Value = (TextView) findViewById(R.id.txt_a12Value);
		txt_a10Value = (TextView) findViewById(R.id.txt_a10Value);
		btn_Getreading = (Button) findViewById(R.id.btn_Getreading);
		ttf = Typeface.createFromAsset(getAssets(), "Transformers Movie.ttf");
		ttf1 = Typeface.createFromAsset(getAssets(), "TradeGothic.ttf");
		textView1.setTypeface(ttf);
		adap = new NumberAdapter(getApplicationContext());
		adap.open();
		txt_voltage = (TextView) findViewById(R.id.txt_voltage);
		txt_voltage.setTypeface(ttf1);
		txt_amps = (TextView) findViewById(R.id.txt_amps);
		txt_amps.setTypeface(ttf1);
		txt_power = (TextView) findViewById(R.id.txt_power);
		txt_power.setTypeface(ttf1);
		txt_a12 = (TextView) findViewById(R.id.txt_a12);
		txt_a12.setTypeface(ttf1);
		txt_a10 = (TextView) findViewById(R.id.txt_a10);
		txt_a10.setTypeface(ttf1);

		Bundle bndl = getIntent().getExtras();
		if (bndl != null) {
			String from = bndl.getString("from");

			if (from.equals("main")) {

				number = bndl.getString("number");
				body = bndl.getString("body");
				KFctor = bndl.getString("K_factor");
				txt_power_result.setTypeface(ttf1);
				String msg = body.substring(5);
				time = bndl.getString("time");
				String date = time.substring(0, 10);
				String timee = time.substring(11);
				textView3.setText(timee);
				textView2.setText(date);
				textView2.setTypeface(ttf1);
				textView3.setTypeface(ttf1);
				String[] msges = msg.split(",");
				String KVAhrarr[] = msges[0].split(" ");
				KVAhr = KVAhrarr[6];
				txt_voltage_result.setText(KVAhr);
				txt_voltage_result.setTypeface(ttf1);
				String KWhrarr[] = msges[1].split(" ");
				KWhr = KWhrarr[2];
				txt_amp_result.setText(KWhr);
				txt_amp_result.setTypeface(ttf1);
				String[] Litresarr = msges[2].split(" ");
				litres = Litresarr[2];
				txt_a10Value.setText(litres);
				txt_a10Value.setTypeface(ttf1);
				String[] joulearr = msges[3].split(" ");
				joule = joulearr[2];
				txt_a12Value.setText(joule);
				float pf = Float.parseFloat(KWhr) / Float.parseFloat(KVAhr);
				DecimalFormat df = new DecimalFormat("##.##");
				txt_power_result.setText("" + df.format(pf));
				txt_a12Value.setTypeface(ttf1);
				btn_Getreading.setTypeface(ttf1);
			}

			else if (from.equals("settings")) {

				if (fetchCNT().size() != 0) {

					myList = adap.fetchNumbers();
					if (myList.size() == 1) {

						myList.get(0).get("first").split("-");
						myList.get(0).get("sec").split("-");
						myList.get(0).get("third").split("-");
						myList.get(0).get("K_factor");
						adap.close();

						DateFormat dateFormat = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm:ss");
						Calendar cal = Calendar.getInstance();

						time = dateFormat.format(cal.getTime());
						String date = time.substring(0, 10);
						String timee = time.substring(11);
						textView3.setText(timee);
						textView2.setText(date);

						// body = arrayList.get(0).get("msg");
						// number = arrayList.get(0).get("number");
						// String msg = body.substring(5);
						// time = arrayList.get(0).get("time");
						// String date = time.substring(0, 12);
						// String timee = time.substring(13);
						// textView3.setText(timee);
						// textView2.setText(date);
						// textView2.setTypeface(ttf1);
						// textView3.setTypeface(ttf1);
						// String[] msges = msg.split(",");
						// String KVAhrarr[] = msges[0].split(" ");
						// KVAhr = KVAhrarr[5];
						// txt_voltage_result.setText(KVAhr);
						// txt_voltage_result.setTypeface(ttf1);
						// String KWhrarr[] = msges[1].split(" ");
						// KWhr = KWhrarr[2];
						// txt_amp_result.setText(KWhr);
						// txt_amp_result.setTypeface(ttf1);
						// String[] Litresarr = msges[2].split(" ");
						// litres = Litresarr[2];
						// txt_a10Value.setText(litres);
						// txt_a10Value.setTypeface(ttf1);
						// String[] joulearr = msges[3].split(" ");
						// joule = joulearr[2];
						// txt_a12Value.setText(joule);
						// txt_a12Value.setTypeface(ttf1);
						// btn_Getreading.setTypeface(ttf1);
						// txt_power_result.setText("" + k_factr);
						// txt_power_result.setTypeface(ttf1);

					}
				}

				else {

					myList = adap.fetchNumbers();

					if (myList.size() == 1) {
						String[] fist_no = myList.get(0).get("first")
								.split("-");
						myList.get(0).get("sec").split("-");
						myList.get(0).get("third").split("-");
						myList.get(0).get("K_factor");
						adap.close();
						number = fist_no[0] + fist_no[1];
						DateFormat dateFormat = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm:ss");
						Calendar cal = Calendar.getInstance();

						time = dateFormat.format(cal.getTime());
						String date = time.substring(0, 10);
						String timee = time.substring(11);
						textView3.setText(timee);
						textView2.setText(date);
						textView2.setTypeface(ttf1);
						textView3.setTypeface(ttf1);
					}

				}

			}

		}
		btn_Getreading.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						GetMeterReadingActivity.this);

				// set title
				alertDialogBuilder.setTitle("Energy Demand Alert");
				// set dialog message
				alertDialogBuilder
						.setMessage("Are you sure ?")
						// you want to Get Current meter Readings?
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {

										try {
											SmsManager smsManager = SmsManager
													.getDefault();

											smsManager.sendTextMessage(number,
													null, "%CNT", null, null);

										} catch (Exception e) {
											Toast.makeText(
													getApplicationContext(),
													"SMS faild, please try again later!",
													Toast.LENGTH_LONG).show();
											e.printStackTrace();
										}

									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.cancel();

									}
								});
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				// show it
				alertDialog.show();
			}
		});
		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(GetMeterReadingActivity.this,
						Settings.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<HashMap<String, String>> fetchCNT() {
		gadp = new GetReadingAdapter(getApplicationContext());
		gadp.open();
		arrayListDB.clear();
		arrayListDB = gadp.fetchMessage();
		new ArrayList();

		if (arrayListDB.size() > 0) {

			String address = arrayListDB.get(0).get("number");
			String body = arrayListDB.get(0).get("message");
			String time1 = arrayListDB.get(0).get("time");
			hashMap = new HashMap<String, String>();
			hashMap.put("number", address);
			hashMap.put("msg", body);
			hashMap.put("time", time1);

			arrayList.add(hashMap);

		}

		return arrayList;

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		getread = false;
	}
}
