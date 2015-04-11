package com.example.slide_menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class SignatureToolActivity extends Activity {
	 public static final int SIGNATURE_ACTIVITY = 1;
	 Button b1;
	 ImageView signImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_signature_tool);
		
		b1 = (Button) findViewById(R.id.getSign);
        signImage = (ImageView) findViewById(R.id.imageView1);
        b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(SignatureToolActivity.this, CaptureSignature.class);
	            startActivityForResult(i, 0);
//	- See more at: http://www.oodlestechnologies.com/blogs/Capture-Signature-Using-FingerPaint-in-Android#sthash.TTWQpIF9.dpuf
			}
		});

	}
	  @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        // TODO Auto-generated method stub
	        if (resultCode == 1) {
	            Bitmap b = BitmapFactory.decodeByteArray(
	                    data.getByteArrayExtra("byteArray"), 0,
	                    data.getByteArrayExtra("byteArray").length);
	            signImage.setImageBitmap(b);
	        }
	    }
//	- See more at: http://www.oodlestechnologies.com/blogs/Capture-Signature-Using-FingerPaint-in-Android#sthash.TTWQpIF9.dpuf
}
