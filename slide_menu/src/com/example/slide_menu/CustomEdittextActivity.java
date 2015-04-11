package com.example.slide_menu;

import com.example.slide_menu.CustomEditText.DrawableClickListener;
import com.example.slide_menu.CustomEditText.DrawableClickListener.DrawablePosition;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class CustomEdittextActivity extends Activity {
	CustomEditText et;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_custom_edittext);
		et = (CustomEditText) this.findViewById(R.id.editText1);
		et.setDrawableClickListener(new DrawableClickListener() {

			public void onClick(DrawablePosition target) {
				switch (target) {
				case LEFT:
					Toast.makeText(getApplicationContext(), "Hello", 1).show();;
					break;
				case RIGHT:
					Toast.makeText(getApplicationContext(), "Hello right", 1).show();;
					break;
				default:
					break;
				}
			}

		});
	}
}
