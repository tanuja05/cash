package com.fw.emars;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NumberScreen extends Activity {
	Typeface ttf;
	EditText et_1, et_2, et_3, et_4, editText31, editText32, editText33;
	Button btn_save, btn_update;
	TextView textView1;
	NumberAdapter adap;
	ImageView imageView1, imag_settings;
	ArrayList<HashMap<String, String>> myList;
	String number, body;
	Dialog mDialog;

	static Activity ctx;

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_number_screen);
		et_1 = (EditText) findViewById(R.id.editText1);
		et_2 = (EditText) findViewById(R.id.editText2);
		et_3 = (EditText) findViewById(R.id.editText4);
		editText31 = (EditText) findViewById(R.id.editText31);
		editText32 = (EditText) findViewById(R.id.editText32);
		editText33 = (EditText) findViewById(R.id.editText33);
		et_4 = (EditText) findViewById(R.id.editText5);

		imag_settings = (ImageView) findViewById(R.id.imageView1);
		textView1 = (TextView) findViewById(R.id.textView1);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_update = (Button) findViewById(R.id.button1);
		ttf = Typeface.createFromAsset(getAssets(), "Transformers Movie.ttf");
		textView1.setTypeface(ttf);
		adap = new NumberAdapter(getApplicationContext());
		adap.open();
		myList = adap.fetchNumbers();
		Bundle bndl = getIntent().getExtras();

		if (myList.size() < 1) 
		{
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					NumberScreen.this);
			// set title
			alertDialogBuilder.setTitle("Energy Demand Alert");
			// set dialog message
			alertDialogBuilder
					.setMessage(
							"You are using Demand Alert first time, tap ok to add numbers and K factor.")
					.setCancelable(false)
					.setPositiveButton("Ok",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.dismiss();
								}
							});
			// create alert dialog
			AlertDialog alertDialog = alertDialogBuilder.create();
			// show it
			alertDialog.show();
		} else {

			if (bndl != null)
			{

				body = bndl.getString("body");
				number = bndl.getString("number");
				String[] fist_no = myList.get(0).get("first").split("-");
				String[] sec_no = myList.get(0).get("sec").split("-");
				String[] third_no = myList.get(0).get("third").split("-");
				String k_factr = myList.get(0).get("K_factor");
				String f_no = fist_no[0] + fist_no[1];
				String s_no = sec_no[0] + sec_no[1];
				String t_no = third_no[0] + third_no[1];

				if (number.equals(f_no) || number.equals(s_no)
						|| number.equals(t_no)) {

					Intent i = new Intent(NumberScreen.this, newResult.class);
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("number", number);
					i.putExtra("body", body);
					i.putExtra("K_factor", k_factr);
					startActivity(i);
					finish();
				}

			}
		}

		imag_settings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), Settings.class);
				startActivity(i);
				finish();
			}
		});

		if (myList.size() == 1) 
		{

			String cod_f[] = myList.get(0).get("first").split("-");
			String cod_s[] = myList.get(0).get("sec").split("-");
			String cod_t[] = myList.get(0).get("third").split("-");
			String[] cod_f_pl = cod_f[0].split("\\+"); // Log.i("my hhhhh",""+cod_f_pl);
			String[] cod_s_pl = cod_s[0].split("\\+");
			String[] cod_t_pl = cod_t[0].split("\\+");
			et_1.setText(cod_f[1]);
			et_2.setText(cod_s[1]);
			et_3.setText(cod_t[1]);
			editText31.setText(cod_f_pl[1]);
			editText32.setText(cod_s_pl[1]);
			editText33.setText(cod_t_pl[1]);
			
			et_4.setText(myList.get(0).get("K_factor"));
			btn_save.setText("Update");
		} else {
			btn_save.setText("Save");
		}
		btn_save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String first_no, second_no, third_no, k_factor, code_first_no, code_second_no, code_third_no;
				first_no = et_1.getText().toString();
				second_no = et_2.getText().toString();
				third_no = et_3.getText().toString();
				k_factor = et_4.getText().toString();
				code_first_no = editText31.getText().toString();
				code_second_no = editText32.getText().toString();
				code_third_no = editText33.getText().toString();
				k_factor = et_4.getText().toString();
				myList = adap.fetchNumbers();
				
				if (myList.size() == 1) 
				{
					
					for (int i = 0; i < myList.size(); i++) {
						
						String id = myList.get(i).get("id");
						Long newId = Long.parseLong(id);

						adap.ifirst = "+" + code_first_no + "-" + first_no;
						adap.isec = "+" + code_second_no + "-" + second_no;
						adap.ithird = "+" + code_third_no + "-" + third_no;
						adap.ikFactor = k_factor;
						adap.updateExpCategory(newId);
						myList = adap.fetchNumbers();
						
						if (myList.size() == 1) {
							String cod_f[] = myList.get(0).get("first")
									.split("-");
							String cod_s[] = myList.get(0).get("sec")
									.split("-");
							String cod_t[] = myList.get(0).get("third")
									.split("-");
							String[] cod_f_pl = cod_f[0].split("\\+");
							String[] cod_s_pl = cod_s[0].split("\\+");
							String[] cod_t_pl = cod_t[0].split("\\+");
							et_1.setText(cod_f[1]);
							et_2.setText(cod_s[1]);
							et_3.setText(cod_t[1]);
							editText31.setText(cod_f_pl[1]);
							editText32.setText(cod_s_pl[1]);
							editText33.setText(cod_t_pl[1]);
							
							
							et_4.setText(myList.get(0).get("K_factor"));
							btn_save.setText("Update");
						}
						else{
							Toast.makeText(getApplicationContext(),
									"could not fetch", 1).show();
							
						}
						Toast.makeText(getApplicationContext(),
								"Numbers updated successfully", 1).show();

					}

				}

				else {
					if (first_no.equals("") && (second_no.equals(""))
							&& (third_no.equals(""))) {
						Toast.makeText(getApplicationContext(),
								"Please enter at least one number ", 1).show();
					}
					
					else if (code_first_no.equals("")
							|| (code_second_no.equals(""))
							|| (code_third_no.equals(""))) {
						Toast.makeText(getApplicationContext(),
								"Enter a country code ", 1).show();
					}

					else {

						adap.ifirst = "+" + code_first_no + "-" + first_no;
						adap.isec = "+" + code_second_no + "-" + second_no;
						adap.ithird = "+" + code_third_no + "-" + third_no;
						adap.ikFactor = k_factor;
						adap.createGroup();
						et_1.setText("");
						et_2.setText("");
						editText31.setText("");
						editText32.setText("");
						editText33.setText("");
						et_4.setText("");
						btn_save.setText("Update");
						myList = adap.fetchNumbers();

						if (myList.size() == 1) {
							String cod_f[] = myList.get(0).get("first")
									.split("-");
							String cod_s[] = myList.get(0).get("sec")
									.split("-");
							String cod_t[] = myList.get(0).get("third")
									.split("-");
							String[] cod_f_pl = cod_f[0].split("\\+"); 
							String[] cod_s_pl = cod_s[0].split("\\+");
							String[] cod_t_pl = cod_t[0].split("\\+");
							et_1.setText(cod_f[1]);
							et_2.setText(cod_s[1]);
							et_3.setText(cod_t[1]);
							editText31.setText(cod_f_pl[1]);
							editText32.setText(cod_s_pl[1]);
							editText33.setText(cod_t_pl[1]);
							et_4.setText(myList.get(0).get("K_factor"));
							btn_save.setText("Update");

						}
						Toast.makeText(getApplicationContext(),
								"Numbers saved successfully ", 1).show();
					}
				}

			}
		});

	}

}
