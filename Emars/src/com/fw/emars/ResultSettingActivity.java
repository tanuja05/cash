package com.fw.emars;

import java.text.DecimalFormat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ResultSettingActivity extends Activity {
	Button btn_clear;
	Typeface ttf, ttf1;
	ImageView imageView1;
	static Activity ctx;
	SharedPreferences spf;
	TextView textView1, txt_power, textView2, textView3, textView4,
			txt_nmi_result, txt_nmi, txt_voltage, txt_amps, txt_voltage_result,
			txt_amp_result, txt_power_result;
	String ids, kw, kv, dt, tm, limm, number, body, KFctor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_result);
		ttf = Typeface.createFromAsset(getAssets(), "Transformers Movie.ttf");
		ttf1 = Typeface.createFromAsset(getAssets(), "TradeGothic.ttf");
		textView1 = (TextView) findViewById(R.id.textView1);
		spf = this.getSharedPreferences("value", 1);
		ctx = this;
		txt_power = (TextView) findViewById(R.id.txt_power);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		textView3 = (TextView) findViewById(R.id.textView3);
		textView4 = (TextView) findViewById(R.id.textView4);
		txt_amps = (TextView) findViewById(R.id.txt_amps);
		txt_voltage = (TextView) findViewById(R.id.txt_voltage);
		btn_clear = (Button) findViewById(R.id.btn_clear);

		txt_nmi = (TextView) findViewById(R.id.txt_nmi);
		txt_power = (TextView) findViewById(R.id.txt_power);
		txt_amp_result = (TextView) findViewById(R.id.txt_amp_result);
		txt_voltage_result = (TextView) findViewById(R.id.txt_voltage_result);
		txt_power_result = (TextView) findViewById(R.id.txt_power_result);
		txt_nmi_result = (TextView) findViewById(R.id.txt_nmi_result);
		textView1.setTypeface(ttf);
		Bundle bndl = getIntent().getExtras();
		if(bndl!=null){
		number = bndl.getString("number");
		body = bndl.getString("body");
		KFctor = bndl.getString("K_factor");}
		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(ResultSettingActivity.this,
						Settings.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(i);
				finish();
			}
		});
		float k_fact = Float.parseFloat(KFctor);
		if (body.startsWith("! DEMAND CAPACITY ALERT !")) {
			String values[] = body.split("\\n");
			String date = values[2];
			String date1[] = date.split(" ");
			String tdt = date1[0];
			String datee[] = tdt.split(";");
			dt = datee[0];
			tm = date1[1];
			String idss = values[3];
			String id[] = idss.split(" ");
			ids = id[1];
			String lim = values[4];
			String limmm[] = lim.split(" ");
			String kvahr = values[5];
			String kwhr = values[6];
			String kvvv[] = kvahr.split(" ");
			String kww[] = kwhr.split(" ");
			limm = limmm[1];
			kv = kvvv[1];
			kw = kww[1];

		}
		DecimalFormat df1 = new DecimalFormat("##.###");

		Double k_v = (Double) (Double.parseDouble(kv) * k_fact);
		Double k_w = (Double) (Double.parseDouble(kw) * k_fact);
		txt_voltage_result.setText(df1.format(k_v) + "");
		txt_amp_result.setText(df1.format(k_w)+ "");
		float pf = Float.parseFloat(kw) / Float.parseFloat(kv);
	
		DecimalFormat df = new DecimalFormat("##.##");
		txt_amps.setTypeface(ttf1);
		txt_power_result.setText(df.format(pf));
		txt_voltage.setTypeface(ttf1);
		textView4.setTypeface(ttf1);
		textView2.setTypeface(ttf1);
		textView3.setTypeface(ttf1);
		txt_nmi_result.setTypeface(ttf1);
		txt_voltage_result.setTypeface(ttf1);
		txt_amp_result.setTypeface(ttf1);
		txt_power_result.setTypeface(ttf1);
		txt_power.setTypeface(ttf1);
		btn_clear.setTypeface(ttf1);

		textView2.setText(dt);
		textView3.setText(tm);

		txt_nmi_result.setText(ids);
		btn_clear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						ResultSettingActivity.this);

				// set title
				alertDialogBuilder.setTitle("Energy Demand Alert");
				// set dialog message
				alertDialogBuilder
						.setMessage(" Are you sure you want to clear alert? ")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										try {
											SmsManager smsManager = SmsManager
													.getDefault();
											smsManager.sendTextMessage(number,
													null, "%CLR", null, null);

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
	}

}
