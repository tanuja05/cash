package com.example.slide_menu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class CalenderEventActivity extends Activity {
	Button button1, editText2, editText3;
	EditText editText1;
	String et_text, time, date;
	private int hour;
	private int minute;
	static final int TIME_DIALOG_ID = 999;
	static final int DATE_DIALOG_ID = 100;
	private int year;
	private int month;
	private int day;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_calender_event);
		editText1 = (EditText) findViewById(R.id.editText1);
		button1 = (Button) findViewById(R.id.button1);
		editText2 = (Button) findViewById(R.id.editText2);
		editText3 = (Button) findViewById(R.id.editText3);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_text = editText1.getText().toString();
				if (et_text.equals("")) {
					Toast.makeText(getApplicationContext(), "Enter text", 1)
							.show();
				} else {

				}

			}
		});

		editText2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
			}
		});
		editText3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialog(TIME_DIALOG_ID);
			}
		});
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, timePickerListener, hour, minute,
					false);
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datepickerListener, year, month,
					day);
		}

		return null;

	}

	private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {

		public void onTimeSet(TimePicker view, int selectedHour,
				int selectedMinute) {

			hour = selectedHour;
			minute = selectedMinute;
			// set current time into textview
			Log.e("TIMEEEE", ""
					+ new StringBuilder().append(padding_str(hour)).append(":")
							.append(padding_str(minute)));
			// set current time into timepicker
			editText2.setText(new StringBuilder().append(padding_str(hour))
					.append(":").append(padding_str(minute)));

			// timePicker.setCurrentHour(hour);
			//
			// timePicker.setCurrentMinute(minute);

		}

	};
	private DatePickerDialog.OnDateSetListener datepickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		@Override
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {

			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			Log.e("Date",
					""	+ new StringBuilder().append(month + 1).append("-")
									.append(day).append("-").append(year)
									.append(" "));
			// Show selected date
			editText3.setText(new StringBuilder().append(month + 1).append("-")
					.append(day).append("-").append(year).append(" "));
		}
	};

	private static String padding_str(int c) {

		if (c >= 10)

			return String.valueOf(c);

		else

			return "0" + String.valueOf(c);

	}

}
