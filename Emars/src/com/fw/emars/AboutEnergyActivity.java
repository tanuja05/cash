package com.fw.emars;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutEnergyActivity extends Activity {
TextView	txt_link,textView1;
ImageView img_settings;
Typeface ttf;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.about_energy);
		img_settings=(ImageView)findViewById(R.id.imageView1);
		img_settings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent in=new Intent(AboutEnergyActivity.this, Settings.class);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);				
				startActivity(in);
				finish();
			}
		});
		ttf= Typeface.createFromAsset(getAssets(), "Transformers Movie.ttf");
		textView1=(TextView)findViewById(R.id.textView1);
		textView1.setTypeface(ttf);
		txt_link=(TextView)findViewById(R.id.textView3);
		txt_link.setClickable(true);
		txt_link.setMovementMethod(LinkMovementMethod.getInstance());
		String text = "<a href='http://www.energellent.com.au'> www.energellent.com.au </a>";
		txt_link.setText(Html.fromHtml(text));
	}
}
