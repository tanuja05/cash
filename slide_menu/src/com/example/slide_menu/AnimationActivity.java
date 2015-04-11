package com.example.slide_menu;

import android.animation.Animator;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AnimationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_animation);
		
		Button btn=new Button(AnimationActivity.this);
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				View myView = findViewById(R.id.view1);
//
//				// get the center for the clipping circle
//				int cx = (myView.getLeft() + myView.getRight()) / 2;
//				int cy = (myView.getTop() + myView.getBottom()) / 2;
//
//				// get the final radius for the clipping circle
//				int finalRadius = Math.max(myView.getWidth(), myView.getHeight());
//
//				// create the animator for this view (the start radius is zero)
//				Animator anim =
//				    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
//
//				// make the view visible and start the animation
//				myView.setVisibility(View.VISIBLE);
//				anim.start();
			}
		});
		
	}
}
