package com.example.slide_menu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DemoLayoutinflateActivity extends Activity {
	LinearLayout lyt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_demo_layoutinflate);
		lyt = (LinearLayout) findViewById(R.id.lyt);
		Display display = getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = screenWidth / 3;
		screenWidth = screenWidth / 3;

		for (int i = 0; i <= 10; i++) {
			View childInner = getLayoutInflater().inflate(
					R.layout.horzontallyt, null);
			LinearLayout lytmainhor = (LinearLayout) childInner
					.findViewById(R.id.lytMainhor);
			for (int j = 0; j <= 3; j++) {

				View child = getLayoutInflater().inflate(R.layout.lytinflt,
						null);

				LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(
						screenWidth, screenHeight);

				LinearLayout lytmain = (LinearLayout) child
						.findViewById(R.id.lytmain);
				TextView textView1=(TextView)child.findViewById(R.id.textView1);
				textView1.setText("Text"+i+""+j);
				lytmain.setLayoutParams(parms);
				lytmainhor.addView(child);
			}

			
			lyt.addView(lytmainhor);

		}
	}
}
