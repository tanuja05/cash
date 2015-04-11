package com.fw.twitter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chat_vendre_acheter.R;

public class TwitterLogin extends Activity {
	public static final String TAG = TwitterLogin.class.getSimpleName();
	private TwitterSession session;
	private SharedPreferences mSharedPreferences;
	static String PREFERENCE_NAME = "twitter_oauth";
	static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
	static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
	@SuppressLint("NewApi")
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.twlogin);
		session = new TwitterSession(this);
		mSharedPreferences = getApplicationContext().getSharedPreferences("MyPref1", 0);
		 StrictMode.ThreadPolicy policy = new
		 StrictMode.ThreadPolicy.Builder()
		 .permitAll().build();
		 StrictMode.setThreadPolicy(policy);
		 
		 
		 
		WebView webView = (WebView) findViewById(R.id.twitterlogin);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				boolean result = true;
				if (url != null && url.startsWith(Const.CALLBACK_URL)) {
					Uri uri = Uri.parse(url);
					Log.v(TAG, url);
					if (uri.getQueryParameter("denied") != null) {
						setResult(RESULT_CANCELED);
						finish();
					} else {
						String oauthToken = uri
								.getQueryParameter("oauth_token");
						String oauthVerifier = uri
								.getQueryParameter("oauth_verifier");
						
						
						Log.i(oauthToken, oauthVerifier);
						Editor e = mSharedPreferences.edit();

						// After getting access token, access token secret
						// store them in application preferences
						//e.putString(PREF_KEY_OAUTH_TOKEN, oauthToken);
						//e.putString(PREF_KEY_OAUTH_SECRET,oauthVerifier);
						// Store login status - true
						//e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
						e.commit(); 
						Intent intent = getIntent();
						intent.putExtra(Const.IEXTRA_OAUTH_TOKEN, oauthToken);
						intent.putExtra(Const.IEXTRA_OAUTH_VERIFIER,
								oauthVerifier);

//						intent = new Intent(TwitterLogin.this, Home.class);

						setResult(RESULT_OK, intent);
						finish();
					}
				} else {
					result = super.shouldOverrideUrlLoading(view, url);
				}
				return result;
			}
		});
		webView.loadUrl(this.getIntent().getExtras().getString("auth_url"));
		
		
		
		

	}
}