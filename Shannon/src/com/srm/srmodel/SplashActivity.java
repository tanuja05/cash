package com.srm.srmodel;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class SplashActivity extends Activity {
WebView webView1;
String url="https://www.facebook.com/pages/Shannon-Robinson/150261808475125?ref=bookmarks";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		webView1=(WebView)findViewById(R.id.webView1);
		webView1.setWebViewClient(new MyBrowser());
		webView1.getSettings().setLoadsImagesAutomatically(true);
		webView1.getSettings().setJavaScriptEnabled(true);
		webView1.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		
		webView1.loadUrl(url);
	}
	private class MyBrowser extends WebViewClient {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         return true;
	      }
	   }

	  
}
