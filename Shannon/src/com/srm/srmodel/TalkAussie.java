package com.srm.srmodel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


public class TalkAussie extends Activity {
	TextView txtmakemetak, txtSmile;
	ImageView img_Hello, img_sexylady, img_nolie, img_lethv;
	Typeface ttf;
	MediaPlayer mplay;

	@SuppressWarnings("static-access")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_talk_aussie);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		txtmakemetak = (TextView) findViewById(R.id.txtmakemetak);
		txtSmile = (TextView) findViewById(R.id.textView1);
		img_Hello = (ImageView) findViewById(R.id.imageView1);
		img_sexylady = (ImageView) findViewById(R.id.imageView2);
		img_nolie = (ImageView) findViewById(R.id.imageView3);
		img_lethv = (ImageView) findViewById(R.id.imageView4);
		ttf = Typeface.createFromAsset(this.getAssets(),
				"Always In My Heart.ttf");
		txtSmile.setTypeface(ttf);
		txtmakemetak.setTypeface(ttf);
		
		txtmakemetak.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playAudio(R.raw.make_me_talk_intro1_01);

			}
		});
		txtSmile.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Toast.makeText(getApplicationContext(), "Shannon ;-) xq",
				// 1).show();
				Intent startMain = new Intent(Intent.ACTION_MAIN);
				startMain.addCategory(Intent.CATEGORY_HOME);
				startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(startMain);
				finish();
			}
		});

		img_Hello.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				playAudio(R.raw.g_day_audio_01);
			}
		});

		img_lethv.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playAudio(R.raw.lets_have_sex_audio_01);
			}
		});
		img_nolie.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playAudio(R.raw.no_lie_audio_01);
				

			}
		});
		img_sexylady.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playAudio(R.raw.you_are_a_sexy_lady_audio_01);
			}
		});

	}
	public void playAudio(int resId)
	{
		
		if(mplay!=null){
			if (mplay.isPlaying()==true) {
				mplay.stop();
				
			}}
			mplay = new MediaPlayer().create(TalkAussie.this,
					resId);

			mplay.start();

			mplay.setOnCompletionListener(new OnCompletionListener() {
				
				@Override
				public void onCompletion(MediaPlayer mp) {
					// TODO Auto-generated method stub
					
				}
			});

		
	}
}
