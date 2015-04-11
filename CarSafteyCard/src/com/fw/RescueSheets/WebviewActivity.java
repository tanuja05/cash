package com.fw.RescueSheets;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class WebviewActivity extends Activity {
WebView webview1;
String url;
ImageView back_img;
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.activity_webview);	
		webview1=(WebView)findViewById(R.id.webView1);
		back_img=(ImageView)findViewById(R.id.back_img);
		Bundle bndl=getIntent().getExtras();
		if(bndl!=null)
		{
			url=bndl.getString("url");
			
			webview1.getSettings().setJavaScriptEnabled(true);
			webview1.getSettings().setPluginState(PluginState.ON);
			webview1.setWebViewClient(new Callback());
			final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
			String urlEncoded = Uri.encode(url, ALLOWED_URI_CHARS);
			Log.e("urll","@@@@@"+urlEncoded);
			webview1.loadUrl("http://docs.google.com/gview?embedded=true&url="+urlEncoded);
		
		}
		
back_img.setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
	}
});

		
	}
	private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(
                WebView view, String url) {
            return(false);
        }
 }
}
