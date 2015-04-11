package com.fw.emars;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReciever extends BroadcastReceiver {
	NumberAdapter mNum;
	Handler handler = new Handler();
	int count = 0;
	ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
	HashMap<String, String> hashMap;
	@SuppressWarnings("rawtypes")
	ArrayList sms;
	String strmessage = "", data1, number, body;
	long time;
	String KVAhr, KWhr, joule, litres;

	MediaPlayer mp = null;
	Runnable runnable;
	Context ctx;
	RecordAdapter recAdap;
	GetReadingAdapter gadap;
	private Typeface ttf1;
	private Typeface ttf;

	@Override
	public void onReceive(Context context, Intent intent) {
		ctx = context;
		mNum = new NumberAdapter(ctx);
		mNum.open();
		ttf = Typeface.createFromAsset(ctx.getAssets(),
				"Transformers Movie.ttf");
		ttf1 = Typeface.createFromAsset(ctx.getAssets(), "TradeGothic.ttf");
		arrayList = mNum.fetchNumbers();
		mNum.close();
		recAdap = new RecordAdapter(ctx);
		recAdap.open();
		gadap = new GetReadingAdapter(ctx);
		gadap.open();
		Bundle bundle = intent.getExtras();
		SmsMessage[] message = null;

		if (bundle != null) {
			Object[] objects = (Object[]) bundle.get("pdus");
			message = new SmsMessage[objects.length];

			for (int i = 0; i < message.length; i++) {
				message[i] = SmsMessage.createFromPdu((byte[]) objects[i]);
				number = message[i].getOriginatingAddress();
				body = message[i].getMessageBody();
				time = message[i].getTimestampMillis();
			}
			if (arrayList.size() <= 0) {
				Log.e("No number", "DB empty");
			} else {

				String[] fist_no = arrayList.get(0).get("first").split("-");
				String[] sec_no = arrayList.get(0).get("sec").split("-");
				String[] third_no = arrayList.get(0).get("third").split("-");
				String f_no = fist_no[0] + fist_no[1];
				String s_no = sec_no[0] + sec_no[1];
				String t_no = third_no[0] + third_no[1];

				if (number.equals(f_no) || number.equals(s_no)
						|| number.equals(t_no)) {
					Log.e("hghfhgf receiver", body);
					if (body.contains("! DEMAND CAPACITY ALERT !")) {

						runnable = new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								playDefaultNotificationSound(ctx);
								count++;
							}
						};

						playDefaultNotificationSound(context);
						if (NumberScreen.ctx != null) {
							NumberScreen.ctx.finish();
						}
						if (newResult.ctx != null) {
							newResult.ctx.finish();
						}
						if (Settings.ctx != null) {
							Settings.ctx.finish();
						}
						if (MainActivity.ctx != null) {
							MainActivity.ctx.finish();
						}
						if (ResultSettingActivity.ctx != null) {
							ResultSettingActivity.ctx.finish();
						}

						Intent i = new Intent();
						String currentDateTimeString = DateFormat
								.getDateTimeInstance().format(new Date());
						recAdap.imessg = body;
						recAdap.inumber = number;
						recAdap.idate = currentDateTimeString;
						recAdap.itime = "" + time;
						recAdap.createRecord();

						i.setClassName("com.fw.emars",
								"com.fw.emars.NumberScreen");
						i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						i.putExtra("number", number);
						i.putExtra("body", body);
						context.startActivity(i);

					}

					else if (body.contains("%CNT: time slot")) {

						DateFormat dateFormat = new SimpleDateFormat(
								"dd/MM/yyyy HH:mm:ss");
						Calendar cal = Calendar.getInstance();
						String currentDateTimeString = dateFormat.format(cal
								.getTime());
						Log.e("Time broadcasttt",
								GetMeterReadingActivity.getread + ""
										+ currentDateTimeString);
						gadap.imessg_r = body;
						gadap.inumber_r = number;
						gadap.idate_r = currentDateTimeString;
						gadap.itime_r = "" + time;
						gadap.createRecords();

						if (GetMeterReadingActivity.getread == true)

						{
							GetMeterReadingActivity.txt_power_result
									.setTypeface(ttf1);
							String msg = body.substring(5);
							String date = currentDateTimeString
									.substring(0, 10);
							String timee = currentDateTimeString.substring(11);
							GetMeterReadingActivity.textView3.setText(timee);
							GetMeterReadingActivity.textView2.setText(date);
							GetMeterReadingActivity.textView2.setTypeface(ttf1);
							GetMeterReadingActivity.textView3.setTypeface(ttf1);
							String[] msges = msg.split(",");
							String KVAhrarr[] = msges[0].split(" ");
							KVAhr = KVAhrarr[6];
							GetMeterReadingActivity.txt_voltage_result
									.setText(KVAhr);
							GetMeterReadingActivity.txt_voltage_result
									.setTypeface(ttf1);
							String KWhrarr[] = msges[1].split(" ");
							KWhr = KWhrarr[2];
							GetMeterReadingActivity.txt_amp_result
									.setText(KWhr);
							GetMeterReadingActivity.txt_amp_result
									.setTypeface(ttf1);
							String[] Litresarr = msges[2].split(" ");
							litres = Litresarr[2];
							GetMeterReadingActivity.txt_a10Value
									.setText(litres);
							GetMeterReadingActivity.txt_a10Value
									.setTypeface(ttf1);
							String[] joulearr = msges[3].split(" ");
							joule = joulearr[2];
							GetMeterReadingActivity.txt_a12Value.setText(joule);

							float pf = Float.parseFloat(KWhr) / Float.parseFloat(KVAhr);
							 DecimalFormat df = new DecimalFormat("##.##");
							GetMeterReadingActivity.txt_power_result.setText(""+df.format(pf));
							GetMeterReadingActivity.txt_a12Value
									.setTypeface(ttf1);

						} else {

							Toast.makeText(ctx, "not possible", 1).show();
						}

					}
				}
			}

		}

	}

	private void playDefaultNotificationSound(Context context) {
		Uri notif = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(context, notif);
		r.play();
		if (count < 9) {
			handler.postDelayed(runnable, 2 * 1000);
		}
	}

}