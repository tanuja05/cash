package com.chat_vendre_acheter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;
import com.chat_vendre_acheter.extra.ImageLoader;
import com.chat_vendre_acheter.extra.ServiceHandler;
import com.fw.twitter.TwitterShare;

public class GridDetailsActivity extends FragmentActivity {

	TextView txt_user_name, txt_price, txt_desc, txt_cati, txt_location,
			txt_product_name, txt_day, txt_dis, txt_like;
	ImageView profile_pic, img_back, img_report, imageView_Like, img_share;
	// ImageView viewPager, arrow, arrow1, review_send;
	ImageView arrow, arrow1, review_send;
	ViewPager viewPager;
	// String[] img_url_array = new String[3];
	ArrayList<String> img_url_array = new ArrayList<String>();
	String bndl, country, city;
	byte[] b_one, b_two, b_three;
	Button btn_map, btn_contact;
	String review_user_name, review_user_review, review_user_pic_url,
			review_post_ago, review_resposne, review_error, review_user_id,
			review_id, edt_str_review;
	String login_name, user_name, usr_id, user_id, dis, ad_id, price, desc,
			cati, location, product_name, user_profile_url, product_id,
			like_count, like_statusnew, like_status, day, cat, cat1, cat2, lat,
			longi, product_image, lat_ad, longi_ad, device_token;
	SharedPreferences preferences;
	ImageLoader imageLoader;
	SharedPreferences preferences_product_user;
	String s, error;
	RelativeLayout rel_desc, rel_desc_contain, rel_review, rel_review_contain,
			rel_loc;
	boolean isFirstViewClick = false;
	boolean isSecondViewClick = false;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	EditText edt_review;
	ArrayList<Bitmap> bitmaps = new ArrayList<Bitmap>();
	double latitude;
	double longitude;
	GPSTracker gps;
	Geocoder geocoder;
	String review;
	View mLinearView;
	String likestatus;
	Dialog dialog;
	RelativeLayout lytLike;
	LinearLayout mLinearListView, scrol_lin;
	List<Address> addresses = null;
	ArrayList<String> arrayList = new ArrayList<String>();
	ScrollView scrollView;
	ConnectionDetector cd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grid_details_activity);
		imageLoader = new ImageLoader(GridDetailsActivity.this);

		list.clear();

		scrollView = (ScrollView) findViewById(R.id.scrollView1);
		scrol_lin = (LinearLayout) findViewById(R.id.lin1);
		lytLike = (RelativeLayout) findViewById(R.id.lytLike);
		cd = new ConnectionDetector(GridDetailsActivity.this);
		scrol_lin.setOrientation(LinearLayout.VERTICAL);
		preferences_product_user = this.getSharedPreferences("PRO_USER", 1);

		preferences = this.getSharedPreferences("LOGIN_PREFS_NAME", 1);
		usr_id = preferences.getString("ID", "0");
		login_name = preferences.getString("USERNAME", "noo");
		imageView_Like = (ImageView) findViewById(R.id.imageView_Like);
		// viewPager = (ImageView) findViewById(R.id.viewPagerMy);
		viewPager = (ViewPager) findViewById(R.id.viewPagerMy);
		btn_map = (Button) findViewById(R.id.button1);
		btn_contact = (Button) findViewById(R.id.button2);
		txt_user_name = (TextView) findViewById(R.id.textView4);
		txt_price = (TextView) findViewById(R.id.textView8);
		txt_cati = (TextView) findViewById(R.id.textView3);
		txt_location = (TextView) findViewById(R.id.textView5);
		txt_desc = (TextView) findViewById(R.id.editText1);
		img_back = (ImageView) findViewById(R.id.img_back);
		txt_product_name = (TextView) findViewById(R.id.textView2);
		txt_day = (TextView) findViewById(R.id.textView6);
		txt_dis = (TextView) findViewById(R.id.textView7);
		txt_like = (TextView) findViewById(R.id.textView_Like);
		edt_review = (EditText) findViewById(R.id.editText11);
		profile_pic = (ImageView) findViewById(R.id.imageView2);
		arrow = (ImageView) findViewById(R.id.imageView10);
		arrow1 = (ImageView) findViewById(R.id.imageView11);
		img_share = (ImageView) findViewById(R.id.imageView8);
		review_send = (ImageView) findViewById(R.id.img_send);
		rel_desc = (RelativeLayout) findViewById(R.id.lytFragmentDescription);
		rel_desc_contain = (RelativeLayout) findViewById(R.id.cntnr_postreview);
		rel_loc = (RelativeLayout) findViewById(R.id.rel_loc);
		rel_review = (RelativeLayout) findViewById(R.id.lytFragmentreee);
		rel_review_contain = (RelativeLayout) findViewById(R.id.cntnr_postreview1);
		img_report = (ImageView) findViewById(R.id.imageView7);

		Intent intent = getIntent();
		product_id = intent.getStringExtra("post_id");

		if (cd.isConnectingToInternet()) {
			AllData();
			gps = new GPSTracker(GridDetailsActivity.this);
			if (gps.canGetLocation()) {

				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
				Log.e("product_id", product_id + "%%%" + usr_id + "  "
						+ latitude + "  " + longitude);
			} else {
				gps.showSettingsAlert();
			}

		}

		geocoder = new Geocoder(this, Locale.getDefault());

		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GridDetailsActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		lytLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				usr_id = preferences.getString("ID", "0");
				Log.e("IDD", "" + like_statusnew);
				if (usr_id.equals("0")) {
					Intent i = new Intent(GridDetailsActivity.this,
							SignInActivity.class);
					startActivity(i);
				} else {
					if (like_statusnew.equals("0")) {
						if (cd.isConnectingToInternet() == true) {
							like();
						}

					} else {
						if (cd.isConnectingToInternet() == true) {
							unlike();
						}
					}
				}
			}
		});
		rel_loc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent intent = new Intent(GridDetailsActivity.this,
						FollowUnfollw.class);
				intent.putExtra("user_name", user_name);
				intent.putExtra("login_id", usr_id);
				intent.putExtra("user_id", user_id);
				intent.putExtra("user_pic_url", user_profile_url);
				intent.putExtra("city", city);
				intent.putExtra("country", country);
				startActivity(intent);

			}
		});

		img_report.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (login_name.equals("noo")) {
					Intent ii = new Intent(GridDetailsActivity.this,
							SignInActivity.class);
					startActivity(ii);
				} else {
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							GridDetailsActivity.this);

					// set title
					alertDialogBuilder.setTitle("Cash");
					alertDialogBuilder.setIcon(R.drawable.ic_launcher);
					// set dialog message
					alertDialogBuilder
							.setMessage(
									""
											+ "Is there something wrong with this post? Are you sure you want to report it as inappropriate?")
							.setCancelable(false)
							.setPositiveButton("Yes",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											// if this button is clicked, close
											// current activity
											dialog.dismiss();
											reportAdd();
										}
									});
					alertDialogBuilder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();

				}
			}
		});

		/*
		 * viewPager.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub Intent i = new Intent(GridDetailsActivity.this,
		 * FullImageActivity.class); i.putExtra("product_pic_url",
		 * product_image); startActivity(i); } });
		 */

		img_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (login_name.equals("noo")) {
					Intent i = new Intent(GridDetailsActivity.this,
							SignInActivity.class);
					startActivity(i);

				} else {

					dialog = new Dialog(GridDetailsActivity.this,
							R.style.DialogTheme);
					dialog.setContentView(R.layout.share_fragment);
					dialog.setCancelable(true);
					RelativeLayout lytHead = (RelativeLayout) dialog
							.findViewById(R.id.lytHead);
					Button btn_whatapp = (Button) dialog
							.findViewById(R.id.whatsapp);
					Button btn_facebook = (Button) dialog
							.findViewById(R.id.facebook);
					Button btn_google_plus = (Button) dialog
							.findViewById(R.id.google_plus);
					Button btn_twitter = (Button) dialog
							.findViewById(R.id.twitter);
					Button btn_email = (Button) dialog.findViewById(R.id.email);
					Button btn_copy_link = (Button) dialog
							.findViewById(R.id.copy_link);

					btn_email.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							dialog.dismiss();
							Intent intent2 = new Intent();
							intent2.setAction(Intent.ACTION_SEND);
							intent2.setType("message/rfc822");
							intent2.putExtra(Intent.EXTRA_SUBJECT,
									"Cash Application");
							intent2.putExtra(
									Intent.EXTRA_TEXT,
									"Look what I found on Cash!\n\n"
											+ product_name
											+ "-"
											+ price
											+ "\n\n Download Cash today to see my item. (Tip: Search by item name to find it quickly)\n\n"
											+ "https://www.dummy.com\n\n"
											+ product_image);
							intent2.putExtra(Intent.EXTRA_STREAM,
									Uri.parse(product_image));
							startActivity(intent2);
						}
					});

					btn_facebook.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(GridDetailsActivity.this,
									Facebook_Share.class);
							i.putExtra("Browse", "Adpost");
							i.putExtra("product_name", product_name);
							i.putExtra("product_price", price);
							i.putExtra("product_image", product_image);
							startActivity(i);
							dialog.dismiss();
						}
					});

					btn_google_plus.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(GridDetailsActivity.this,
									Google_Plus_Share.class);
							i.putExtra("Browse", "Adpost");
							i.putExtra("product_name", product_name);
							i.putExtra("product_price", price);
							i.putExtra("product_image", product_image);
							startActivity(i);
							dialog.dismiss();
						}
					});

					btn_whatapp.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							PackageManager pm = GridDetailsActivity.this
									.getPackageManager();
							try {

								Intent waIntent = new Intent(Intent.ACTION_SEND);
								waIntent.setType("text/plain");
								String text = "Cash Application \n\n"
										+ "Look what I found on Cash!\n\n"
										+ product_name
										+ "-"
										+ price
										+ "\n\n Download Cash today to see my item. (Tip: Search by item name to find it quickly)"
										+ "\n\n https://www.dummy.com\n\n"
										+ product_image;

								PackageInfo info = pm.getPackageInfo(
										"com.whatsapp",
										PackageManager.GET_META_DATA);
								// Check if package exists or not. If not then
								// code
								// in catch block will be called
								waIntent.setPackage("com.whatsapp");

								waIntent.putExtra(Intent.EXTRA_TEXT, text);
								// waIntent.putExtra(Intent.EXTRA_STREAM,
								// Uri.parse(product_image));
								startActivity(Intent.createChooser(waIntent,
										"Share with"));

							} catch (NameNotFoundException e) {
								Toast.makeText(GridDetailsActivity.this,
										"WhatsApp not Installed",
										Toast.LENGTH_SHORT).show();
							}
						}
					});

					btn_twitter.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent i = new Intent(GridDetailsActivity.this,
									TwitterShare.class);
							i.putExtra("Browse", "Adpost");
							i.putExtra("product_name", product_name);
							i.putExtra("product_price", price);
							i.putExtra("product_image", product_image);
							startActivity(i);
							dialog.dismiss();
						}
					});

					lytHead.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});

					Display mDisplay = GridDetailsActivity.this
							.getWindowManager().getDefaultDisplay();
					final int width = mDisplay.getWidth();
					final int height = mDisplay.getHeight();
					WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
					lp.copyFrom(dialog.getWindow().getAttributes());
					lp.width = width;
					lp.height = height;
					dialog.getWindow().setAttributes(lp);
					dialog.show();
				}
			}
		});

		btn_map.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent map = new Intent(GridDetailsActivity.this,
						ProductMap.class);
				map.putExtra("User Name", user_name);
				map.putExtra("Product Price", price);
				map.putExtra("Product_name", product_name);
				map.putExtra("day", day);
				map.putExtra("product_image_url", product_image);
				map.putExtra("user_image_url", user_profile_url);
				map.putExtra("lat_ad", lat_ad);
				map.putExtra("longi_ad", longi_ad);
				startActivity(map);

			}
		});

		btn_contact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharedPreferences sharedPreferences = GridDetailsActivity.this
						.getSharedPreferences("LOGIN_PREFS_NAME", 1);
				String loged_in_id = sharedPreferences.getString("ID", "use");

				if (login_name.equals("noo")) {
					Intent ii = new Intent(GridDetailsActivity.this,
							SignInActivity.class);
					startActivity(ii);
				} else {

					if (user_id.equals(loged_in_id)) {
						Intent contact1 = new Intent(GridDetailsActivity.this,
								MainActivity.class);
						contact1.putExtra("DefaultTab", 2);
						startActivity(contact1);
					} else {

						Intent contact = new Intent(GridDetailsActivity.this,
								ChatActivity.class);
						contact.putExtra("User Name", user_name);
						contact.putExtra("Product Price", price);
						contact.putExtra("Product_name", product_name);
						contact.putExtra("day", day);
						contact.putExtra("ad_id", ad_id);
						contact.putExtra("PRO_USER_ID", user_id);
						contact.putExtra("PRO_USER_NAME", user_name);
						contact.putExtra("PRO_TOKEN", device_token);
						contact.putExtra("product_image_url", product_image);
						contact.putExtra("user_image_url", user_profile_url);
						startActivity(contact);
					}
				}
			}
		});

		review_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (login_name.equals("noo")) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							GridDetailsActivity.this);
					builder.setTitle("Cash");
					builder.setMessage("Please Login");
					builder.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									Intent intent = new Intent(
											GridDetailsActivity.this,
											SignInActivity.class);
									startActivity(intent);
								}
							});

					builder.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});

					AlertDialog alertDialog = builder.create();
					builder.show();
				} else if (edt_review.getText().toString().equals("")) {
					Toast.makeText(GridDetailsActivity.this,
							"Please Write Review", 1).show();
				} else {
					postReview();
					edt_review.setHint("Post Review");
					;
					arrow1.setBackgroundResource(R.drawable.down);
					rel_review_contain.setVisibility(View.VISIBLE);
					Toast.makeText(GridDetailsActivity.this, " Review", 1)
							.show();
				}

			}
		});

		rel_desc.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (isFirstViewClick == false) {
					isFirstViewClick = true;
					txt_desc.setText(desc);
					arrow.setBackgroundResource(R.drawable.up);
					rel_desc_contain.setVisibility(View.VISIBLE);
				} else {
					isFirstViewClick = false;
					arrow.setBackgroundResource(R.drawable.down);
					rel_desc_contain.setVisibility(View.GONE);
				}
				return false;
			}
		});

		rel_review.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (isSecondViewClick == false) {
					isSecondViewClick = true;

					arrow1.setBackgroundResource(R.drawable.up);

					rel_review_contain.setVisibility(View.VISIBLE);
				} else {
					isSecondViewClick = false;
					arrow1.setBackgroundResource(R.drawable.down);
					rel_review_contain.setVisibility(View.GONE);
					scrol_lin.setVisibility(View.GONE);
				}
				return false;
			}
		});

	}

	public void like() {

		// ddd
		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				// progressDialog.setMessage("Wait...");
				// progressDialog.setIndeterminate(true);
				// progressDialog.setCancelable(true);
				// progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String likeUrl = "http://www.cashcash.today/cash/api/likes/add";

				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(likeUrl);
				try {

					Log.i("LOGINN", usr_id + "");
					multipartEntity.addPart("data[Like][user_id]",
							new StringBody(usr_id));
					multipartEntity.addPart("data[Like][ad_id]",
							new StringBody(ad_id));

					httpPost.setEntity(multipartEntity);

					HttpResponse httpResponse = null;

					try {

						httpResponse = httpClient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					try {
						s = EntityUtils.toString(httpResponse.getEntity());
						Log.e("adid   ", "" + ad_id + "..." + s.toString());
					} catch (ParseException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				if (s != null) {
					try {
						JSONObject jsonObjectJson = new JSONObject(s);

						String error = jsonObjectJson.getString("error");
						if (error.equals("0")) {
							AllData1();
						}
					} catch (Exception e) {
					}

				}

			}

		};

		asyncTask.execute((Void[]) null);

	}

	public void unlike() {

		// ddd
		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				// progressDialog.setMessage("Wait...");
				// progressDialog.setIndeterminate(true);
				// progressDialog.setCancelable(true);
				// progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String likeUrl = "http://www.cashcash.today/cash/api/likes/delete/";
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(likeUrl);
				try {

					Log.i("LOGINN", usr_id + "");
					multipartEntity.addPart("data[Like][user_id]",
							new StringBody(usr_id));
					multipartEntity.addPart("data[Like][ad_id]",
							new StringBody(ad_id));

					httpPost.setEntity(multipartEntity);

					HttpResponse httpResponse = null;

					try {

						httpResponse = httpClient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					try {
						s = EntityUtils.toString(httpResponse.getEntity());
						Log.e("adid   ", "" + ad_id + "..." + s.toString());
					} catch (ParseException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				if (s != null) {
					try {
						JSONObject jsonObjectJson = new JSONObject(s);

						String error = jsonObjectJson.getString("error");
						if (error.equals("0")) {
							AllData1();
						}
					} catch (Exception e) {
					}

				}
			}

		};

		asyncTask.execute((Void[]) null);

	}

	public void postReview() {

		HttpClient httpClient1 = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask1 = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog1 = new ProgressDialog(
					GridDetailsActivity.this);

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog1.setMessage("Wait Review post....");
				progressDialog1.setIndeterminate(true);
				progressDialog1.setCancelable(true);
				progressDialog1.show();
				list.clear();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/reviews/add";
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					multipartEntity.addPart("data[Review][user_id]",
							new StringBody(usr_id));
					multipartEntity.addPart("data[Review][ad_id]",
							new StringBody(product_id));
					multipartEntity.addPart("data[Review][review]",
							new StringBody(edt_review.getText().toString()));
					multipartEntity.addPart("data[Review][file]",
							new StringBody("file"));
					multipartEntity.addPart("data[Review][status]",
							new StringBody("1"));

					httpPost.setEntity(multipartEntity);

					HttpResponse httpResponse = null;
					try {

						httpResponse = httpClient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					try {
						review_resposne = EntityUtils.toString(httpResponse
								.getEntity());

					} catch (ParseException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				if (review_resposne != null) {
					try {
						JSONObject jsonObjectJson = new JSONObject(
								review_resposne);
						review_error = jsonObjectJson.getString("error");

						if (review_error.equals("0")) {

							JSONObject jsonObjectList = jsonObjectJson
									.getJSONObject("list");
							JSONArray jsonArrayReview = jsonObjectList
									.getJSONArray("Review");

							HashMap<String, String> hashMap_review;

							for (int i = 0; i < jsonArrayReview.length(); i++) {
								hashMap_review = new HashMap<String, String>();

								JSONObject jsonObject = jsonArrayReview
										.getJSONObject(i);
								review_id = jsonObject.getString("id");
								review_user_review = jsonObject
										.getString("review");
								review_post_ago = jsonObject.getString("time");

								JSONObject jsonObjectUser = jsonObject
										.getJSONObject("User");
								review_user_id = jsonObjectUser.getString("id");
								review_user_name = jsonObjectUser
										.getString("username");
								review_user_pic_url = jsonObjectUser
										.getString("image");

								hashMap_review.put("review_id", review_id);
								hashMap_review.put("review_user_review",
										review_user_review);
								hashMap_review.put("review_post_ago",
										review_post_ago);
								hashMap_review.put("review_user_id",
										review_user_id);
								hashMap_review.put("review_user_name",
										review_user_name);
								hashMap_review.put("review_user_pic_url",
										review_user_pic_url);

								list.add(hashMap_review);

							}

							for (int j = 0; j < list.size(); j++) {

								LayoutInflater inflater = null;
								inflater = (LayoutInflater) getApplicationContext()
										.getSystemService(
												Context.LAYOUT_INFLATER_SERVICE);

								mLinearView = inflater.inflate(
										R.layout.review_view, null);

								final ImageView pic_url = (ImageView) mLinearView
										.findViewById(R.id.profile_pic);
								final TextView user_name = (TextView) mLinearView
										.findViewById(R.id.user_txt);
								final TextView review = (TextView) mLinearView
										.findViewById(R.id.review_txt);
								final TextView day_ago = (TextView) mLinearView
										.findViewById(R.id.create_txt);
								imageLoader.DisplayImage(
										list.get(j).get("review_user_pic_url"),
										pic_url);
								user_name.setText(list.get(j).get(
										"review_user_name"));
								review.setText(list.get(j).get(
										"review_user_review"));
								day_ago.setText(list.get(j).get(
										"review_post_ago"));
								scrol_lin.addView(mLinearView);

							}

							scrollView.addView(scrol_lin);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

				progressDialog1.cancel();
			}

		};
		asyncTask1.execute((Void[]) null);
	}

	public void AllData1() {

		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(
					GridDetailsActivity.this);

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				// progressDialog.setMessage("Wait...");
				// progressDialog.setIndeterminate(true);
				// progressDialog.setCancelable(true);
				// progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/ads/view/"
						+ usr_id + "/" + product_id;
				ServiceHandler serviceHandler = new ServiceHandler();
				s = serviceHandler.makeServiceCall(url, ServiceHandler.GET);

				return null;
			}

			protected void onPostExecute(Void result) {

				if (s != null) {

					try {
						JSONObject jsonObjectJson = new JSONObject(s);
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONObject jsonObjectList = jsonObjectJson
									.getJSONObject("list");
							JSONObject jsonObjectAd = jsonObjectList
									.getJSONObject("Ad");
							ad_id = jsonObjectAd.getString("id");
							user_id = jsonObjectAd.getString("user_id");
							product_name = jsonObjectAd.getString("name");
							desc = jsonObjectAd.getString("description");
							price = jsonObjectAd.getString("price");
							like_count = jsonObjectAd.getString("like_count");
							like_status = jsonObjectAd.getString("liked");
							like_statusnew = like_status;
							day = jsonObjectAd.getString("created");
							cat = jsonObjectAd.getString("cat");
							cat1 = jsonObjectAd.getString("cat1");
							cat2 = jsonObjectAd.getString("cat2");
							lat_ad = jsonObjectAd.getString("latitude");
							longi_ad = jsonObjectAd.getString("longitude");

							JSONObject jsonObjectUser = jsonObjectList
									.getJSONObject("User");
							user_name = jsonObjectUser.getString("username");
							user_profile_url = jsonObjectUser
									.getString("image");
							lat = jsonObjectUser.getString("latitude");
							longi = jsonObjectUser.getString("longitude");
							device_token = jsonObjectUser
									.getString("device_token");

							latitude = Double.parseDouble(lat);
							longitude = Double.parseDouble(longi);
							/*
							 * if (gps.canGetLocation()) {
							 * 
							 * latitude = gps.getLatitude(); longitude =
							 * gps.getLongitude();
							 */

							try {
								addresses = geocoder.getFromLocation(latitude,
										longitude, 1);
								String address = addresses.get(0)
										.getAddressLine(0);
								city = addresses.get(0).getAddressLine(1);
								country = addresses.get(0).getAddressLine(2);

								txt_location.setText(city + "," + country);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							/*
							 * } else {
							 * 
							 * gps.showSettingsAlert(); }
							 */

							JSONArray jsonArrayAdfile = jsonObjectList
									.getJSONArray("Adfile");
							for (int i = 0; i < jsonArrayAdfile.length(); i++) {
								JSONObject jsonObjectImg = jsonArrayAdfile
										.getJSONObject(i);
								product_image = jsonObjectImg.getString("file");

							}

							JSONArray jsonArrayReviewOncreate = jsonObjectList
									.getJSONArray("Review");
							HashMap<String, String> hashMap_review_oncreate;
							for (int j = 0; j < jsonArrayReviewOncreate
									.length(); j++) {
								JSONObject jsonObjectReviewOnCreate = jsonArrayReviewOncreate
										.getJSONObject(j);

								hashMap_review_oncreate = new HashMap<String, String>();

								review_id = jsonObjectReviewOnCreate
										.getString("id");
								review_user_review = jsonObjectReviewOnCreate
										.getString("review");
								review_post_ago = jsonObjectReviewOnCreate
										.getString("time");

								JSONObject jsonObjectUser_oncreate = jsonObjectReviewOnCreate
										.getJSONObject("User");
								review_user_id = jsonObjectUser_oncreate
										.getString("id");
								review_user_name = jsonObjectUser_oncreate
										.getString("username");
								review_user_pic_url = jsonObjectUser_oncreate
										.getString("image");

								hashMap_review_oncreate.put("review_id",
										review_id);
								hashMap_review_oncreate.put(
										"review_user_review",
										review_user_review);
								hashMap_review_oncreate.put("review_post_ago",
										review_post_ago);
								hashMap_review_oncreate.put("review_user_id",
										review_user_id);
								hashMap_review_oncreate.put("review_user_name",
										review_user_name);
								hashMap_review_oncreate.put(
										"review_user_pic_url",
										review_user_pic_url);

								list.add(hashMap_review_oncreate);
							}
							if (like_status.equals("0")) {
								imageView_Like
										.setBackgroundResource(R.drawable.unlike_icon);
								txt_like.setText("" + like_count);
								lytLike.setBackgroundResource(R.drawable.unlike);
							} else {
								imageView_Like
										.setBackgroundResource(R.drawable.like_icon);
								lytLike.setBackgroundResource(R.drawable.like_bg);
								txt_like.setText("" + like_count);
							}

						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				// progressDialog.dismiss();

			}

		};

		asyncTask.execute((Void[]) null);
	}

	public void AllData() {

		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(
					GridDetailsActivity.this);

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				progressDialog.setMessage("Wait...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/ads/view/"
						+ usr_id + "/" + product_id;

				Log.i("VALUES", latitude + "**" + longitude + "##" + usr_id
						+ "&&" + product_id);

				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					multipartEntity.addPart("data[User][latitude]",
							new StringBody("" + latitude));

					multipartEntity.addPart("data[User][longitude]",
							new StringBody("" + longitude));

					httpPost.setEntity(multipartEntity);

					HttpResponse httpResponse = null;

					try {

						httpResponse = httpClient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					try {
						s = EntityUtils.toString(httpResponse.getEntity());
						Log.e("RES>>", s.toString());

					} catch (ParseException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			protected void onPostExecute(Void result) {

				if (s != null) {

					try {
						JSONObject jsonObjectJson = new JSONObject(s);
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONObject jsonObjectList = jsonObjectJson
									.getJSONObject("list");

							JSONObject disObj = jsonObjectList
									.getJSONObject("0");
							dis = disObj.getString("distance");

							JSONObject jsonObjectAd = jsonObjectList
									.getJSONObject("Ad");

							ad_id = jsonObjectAd.getString("id");
							user_id = jsonObjectAd.getString("user_id");
							product_name = jsonObjectAd.getString("name");
							desc = jsonObjectAd.getString("description");
							price = jsonObjectAd.getString("price");
							like_count = jsonObjectAd.getString("like_count");
							like_status = jsonObjectAd.getString("liked");
							like_statusnew = like_status;
							day = jsonObjectAd.getString("created");
							cat = jsonObjectAd.getString("cat");
							cat1 = jsonObjectAd.getString("cat1");
							cat2 = jsonObjectAd.getString("cat2");
							lat_ad = jsonObjectAd.getString("latitude");
							longi_ad = jsonObjectAd.getString("longitude");

							JSONObject jsonObjectUser = jsonObjectList
									.getJSONObject("User");
							user_name = jsonObjectUser.getString("username");
							user_profile_url = jsonObjectUser
									.getString("image");
							lat = jsonObjectUser.getString("latitude");
							longi = jsonObjectUser.getString("longitude");
							device_token = jsonObjectUser
									.getString("device_token");
							Log.e("Dist", "" + dis);
							latitude = Double.parseDouble(lat);
							longitude = Double.parseDouble(longi);
							/*
							 * if (gps.canGetLocation()) {
							 * 
							 * latitude = gps.getLatitude(); longitude =
							 * gps.getLongitude();
							 */

							try {
								addresses = geocoder.getFromLocation(latitude,
										longitude, 1);
								String address = addresses.get(0)
										.getAddressLine(0);
								city = addresses.get(0).getAddressLine(1);
								country = addresses.get(0).getAddressLine(2);

								txt_location.setText(city + "," + country);

							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							/*
							 * } else {
							 * 
							 * gps.showSettingsAlert(); }
							 */

							JSONArray jsonArrayAdfile = jsonObjectList
									.getJSONArray("Adfile");

							for (int ii = 0; ii < jsonArrayAdfile.length(); ii++) {
								JSONObject jsonObjectImg = jsonArrayAdfile
										.getJSONObject(ii);

								product_image = jsonObjectImg.getString("file");
								img_url_array.add(product_image);

							}

							txt_product_name.setText(product_name);
							txt_desc.setText(desc);
							txt_price.setText(price);
							txt_user_name.setText(user_name);
							txt_cati.setText(cat + "/" + cat1 + "/" + cat2);

							if (like_status.equals("0")) {
								imageView_Like
										.setBackgroundResource(R.drawable.unlike_icon);
								txt_like.setText("" + like_count);
								lytLike.setBackgroundResource(R.drawable.unlike);
							} else {
								imageView_Like
										.setBackgroundResource(R.drawable.like_icon);
								lytLike.setBackgroundResource(R.drawable.like_bg);
								txt_like.setText("" + like_count);
							}

							imageLoader.DisplayImage(user_profile_url,
									profile_pic);
							/*
							 * imageLoader.DisplayImage(product_image,
							 * viewPager);
							 */
							viewPager.setAdapter(new PostPrePagerAdapter());
							Float float1 = Float.parseFloat(dis);
							String dis1 = String.format("%.1f", float1);
							txt_dis.setText(dis1 + " " + "km");

							if (day.contains(",")) {
								String[] day1 = day.split(",");
								day = day1[0];
							}

							if (day.contains("ago")) {
								day = day.replace("ago", "");
							}
							txt_day.setText(day);

							JSONArray jsonArrayReviewOncreate = jsonObjectList
									.getJSONArray("Review");
							HashMap<String, String> hashMap_review_oncreate;
							for (int j = 0; j < jsonArrayReviewOncreate
									.length(); j++) {
								JSONObject jsonObjectReviewOnCreate = jsonArrayReviewOncreate
										.getJSONObject(j);

								hashMap_review_oncreate = new HashMap<String, String>();

								review_id = jsonObjectReviewOnCreate
										.getString("id");
								review_user_review = jsonObjectReviewOnCreate
										.getString("review");
								review_post_ago = jsonObjectReviewOnCreate
										.getString("time");

								JSONObject jsonObjectUser_oncreate = jsonObjectReviewOnCreate
										.getJSONObject("User");
								review_user_id = jsonObjectUser_oncreate
										.getString("id");
								review_user_name = jsonObjectUser_oncreate
										.getString("username");
								review_user_pic_url = jsonObjectUser_oncreate
										.getString("image");

								hashMap_review_oncreate.put("review_id",
										review_id);
								hashMap_review_oncreate.put(
										"review_user_review",
										review_user_review);
								hashMap_review_oncreate.put("review_post_ago",
										review_post_ago);
								hashMap_review_oncreate.put("review_user_id",
										review_user_id);
								hashMap_review_oncreate.put("review_user_name",
										review_user_name);
								hashMap_review_oncreate.put(
										"review_user_pic_url",
										review_user_pic_url);

								list.add(hashMap_review_oncreate);
							}

							for (int k = 0; k < list.size(); k++) {

								LayoutInflater inflater = null;
								inflater = (LayoutInflater) getApplicationContext()
										.getSystemService(
												Context.LAYOUT_INFLATER_SERVICE);

								mLinearView = inflater.inflate(
										R.layout.review_view, null);

								final ImageView pic_url = (ImageView) mLinearView
										.findViewById(R.id.profile_pic);
								final TextView user_name = (TextView) mLinearView
										.findViewById(R.id.user_txt);
								final TextView review = (TextView) mLinearView
										.findViewById(R.id.review_txt);
								final TextView day_ago = (TextView) mLinearView
										.findViewById(R.id.create_txt);
								imageLoader.DisplayImage(
										list.get(k).get("review_user_pic_url"),
										pic_url);
								user_name.setText(list.get(k).get(
										"review_user_name"));
								review.setText(list.get(k).get(
										"review_user_review"));
								day_ago.setText(list.get(k).get(
										"review_post_ago"));
								scrol_lin.addView(mLinearView);

							}

							scrollView.addView(scrol_lin);

						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				progressDialog.dismiss();

			}

		};

		asyncTask.execute((Void[]) null);
	}

	public void reportAdd() {

		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(
					GridDetailsActivity.this);

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				progressDialog.setMessage("logging in...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/reports/add";
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					multipartEntity.addPart("data[Report][user_id]",
							new StringBody(usr_id));

					multipartEntity.addPart("data[Report][ad_id] ",
							new StringBody(ad_id));

					httpPost.setEntity(multipartEntity);

					HttpResponse httpResponse = null;

					try {

						httpResponse = httpClient.execute(httpPost);
					} catch (ClientProtocolException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO: handle exception
						e.printStackTrace();
					}

					try {
						s = EntityUtils.toString(httpResponse.getEntity());

					} catch (ParseException e) {
						// TODO: handle exception
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} catch (IOException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				progressDialog.dismiss();

			}

		};

		asyncTask.execute((Void[]) null);

	}

	public class PostPrePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return img_url_array.size();
		}

		public Object instantiateItem(View collection, final int position) {
			ImageView view = new ImageView(GridDetailsActivity.this);
			view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT));

			view.setScaleType(ScaleType.FIT_XY);
			imageLoader.DisplayImage(img_url_array.get(position), view);
			((ViewPager) collection).addView(view, 0);

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(GridDetailsActivity.this,
							FullImageActivity.class);
					Log.e("url", img_url_array.toString());
					i.putStringArrayListExtra("img", img_url_array);
					i.putExtra("pos", position);
					startActivity(i);
				}
			});
			return view;

		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView((View) arg2);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == ((View) arg1);
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

	}

}
