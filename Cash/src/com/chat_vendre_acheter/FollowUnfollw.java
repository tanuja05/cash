package com.chat_vendre_acheter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;
import com.chat_vendre_acheter.extra.ImageLoader;
import com.chat_vendre_acheter.extra.ServiceHandler;

public class FollowUnfollw extends FragmentActivity {
	String s, error, user_name, user_pic_url, city, country, user_id, login_id,ad_id,
			ad_name, ad_price, created, ad_pic_url;
	TextView txt_user_name, txt_location, txt_follow, txt_unFollow,
			txt_listings, txt_all_review;
	ImageView img_user_pic, img_back;
	ImageLoader imageLoader;
	ListView listView;
	ArrayList<HashMap<String, String>> arrayList_all_post = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> arrayList_all_review = new ArrayList<HashMap<String, String>>();
	double latitude, longitude;
	GPSTracker gps;
ConnectionDetector cd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.follow_unfollow_lyt);
		imageLoader = new ImageLoader(FollowUnfollw.this);
		Intent i = getIntent();
		user_name = i.getStringExtra("user_name");
		user_pic_url = i.getStringExtra("user_pic_url");
		city = i.getStringExtra("city");
		country = i.getStringExtra("country");
		user_id = i.getStringExtra("user_id");
		login_id = i.getStringExtra("login_id");
cd=new ConnectionDetector(FollowUnfollw.this);
		gps = new GPSTracker(FollowUnfollw.this);

		// check if GPS enabled
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
		} else {

			gps.showSettingsAlert();
		}

		findId();
		setData();
		click();
		if(cd.isConnectingToInternet()==true){
		AllPost();
		}
		else{
			errorDialog("Oops, Something is wrong with internet connection");
		}
	}
	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				FollowUnfollw.this);

		// set title
		alertDialogBuilder.setTitle("Cash");
		alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		// set dialog message
		alertDialogBuilder.setMessage("" + str).setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						dialog.dismiss();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	public void findId() {

		img_back = (ImageView) findViewById(R.id.back);
		img_user_pic = (ImageView) findViewById(R.id.img_user_pic);
		txt_user_name = (TextView) findViewById(R.id.user_name);
		txt_location = (TextView) findViewById(R.id.location);
		// txt_follow = (TextView) findViewById(R.id.follow);
		txt_unFollow = (TextView) findViewById(R.id.un_follow);
		txt_listings = (TextView) findViewById(R.id.txt_listings);
		txt_all_review = (TextView) findViewById(R.id.txt_all_review);
		listView = (ListView) findViewById(R.id.listView1);
	}

	public void setData() {

		imageLoader.DisplayImage(user_pic_url, img_user_pic);
		txt_user_name.setText(user_name);
		txt_location.setText("Location : " + city + "," + country);
	}

	public void click() {
		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		/*
		 * txt_follow.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub follow(); txt_follow.setVisibility(View.GONE);
		 * txt_unFollow.setVisibility(View.VISIBLE); } });
		 */

		txt_listings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AllPost();
			}
		});

		txt_all_review.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				myReviews();
			}
		});

	}

	public void follow() {
		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
			ProgressDialog progressDialog = new ProgressDialog(
					FollowUnfollw.this);

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
			protected Void doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				String url = "http://www.cashcash.today/cash/api/followers/add";
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					multipartEntity.addPart("data[Follower][user_id]",
							new StringBody(login_id));

					multipartEntity.addPart("data[Follower][loggedid]",
							new StringBody(user_id));

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
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				progressDialog.dismiss();
			}

		};

		asyncTask.execute((Void[]) null);
	}

	public void AllPost() {

		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
			ProgressDialog progressDialog = new ProgressDialog(
					FollowUnfollw.this);

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

				String url = "http://www.cashcash.today/cash/api/ads/review/"
						+ user_id;
				ServiceHandler serviceHandler = new ServiceHandler();
				s = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
			

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				if (s != null) {
					Log.e("Res####", s.toString());
					try {
						JSONObject jsonObjectMain = new JSONObject(s);
						Log.e("jsonObjectMain####", jsonObjectMain.toString());
						error = jsonObjectMain.getString("error");
						Log.e("error", error);
						if (error.equals("0")) {
							arrayList_all_post.clear();
							
							JSONArray jsonArrayList = jsonObjectMain
									.getJSONArray("list");
							Log.e("error1", error);
							HashMap<String, String> hashMap_all_post;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								hashMap_all_post = new HashMap<String, String>();
								Log.e("error2", error);
								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								Log.e("error3", error);
								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");
								Log.e("error4", error);
								user_id = jsonObjectAd.getString("user_id");
								ad_name = jsonObjectAd.getString("name");
								ad_price = jsonObjectAd.getString("price");
								created = jsonObjectAd.getString("created");
								
								ad_id = jsonObjectAd.getString("id");
								Log.e("error5", error);
								Log.e("ad_id", ad_id);
								hashMap_all_post.put("user_id", user_id);
								hashMap_all_post.put("ad_name", ad_name);
								hashMap_all_post.put("ad_price", ad_price);
								hashMap_all_post.put("created", created);
								hashMap_all_post.put("ad_id", ad_id);

								JSONArray jsonArrayAdfile = jsonObject
										.getJSONArray("Adfile");
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {
									JSONObject jsonObject_adFile = jsonArrayAdfile
											.getJSONObject(0);
									ad_pic_url = jsonObject_adFile
											.getString("file");
									hashMap_all_post.put("ad_pic_url",
											ad_pic_url);

								}
								arrayList_all_post.add(hashMap_all_post);
							}

							listView.setAdapter(new MyPostAdapter());
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

	public void myReviews() {
		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(
					FollowUnfollw.this);

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

				String likeurl = "http://www.cashcash.today/cash/api/reviews/myreview/"
						+ user_id;
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(likeurl);

				try {

					multipartEntity.addPart("data[Ad][latitude]",
							new StringBody("" + latitude));

					multipartEntity.addPart("data[Ad][longitude]",
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
							arrayList_all_review.clear();
							JSONArray listAr = jsonObjectJson
									.getJSONArray("list");
							HashMap<String, String> hashMap_rev;
							for (int i = 0; i < listAr.length(); i++) {
								hashMap_rev = new HashMap<String, String>();

								JSONObject jsonObjectChat = listAr
										.getJSONObject(i);

								JSONObject jsonObjectReview = jsonObjectChat
										.getJSONObject("Review");
								String ad_id = jsonObjectReview
										.getString("ad_id");
								hashMap_rev.put("ad_id", ad_id);
								String time = jsonObjectReview
										.getString("time");
								hashMap_rev.put("created", time);
								String review = jsonObjectReview
										.getString("review");
								hashMap_rev.put("review", review);

								JSONObject jsonAd = jsonObjectChat
										.getJSONObject("Ad");

								JSONArray jsonAdfile = jsonAd
										.getJSONArray("Adfile");

								JSONObject adfile = jsonAdfile.getJSONObject(0);

								String ad_url = adfile.getString("file");
								hashMap_rev.put("ad_url", ad_url);

								arrayList_all_review.add(hashMap_rev);
							}

							listView.setAdapter(new MyReviewAdapter());
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
	
	
	public void deletePost(){
		
		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
			ProgressDialog progressDialog = new ProgressDialog(
					FollowUnfollw.this);

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

				String url = "http://www.cashcash.today/cash/api/ads/delete/"
						+ ad_id;
				ServiceHandler serviceHandler = new ServiceHandler();
				s = serviceHandler.makeServiceCall(url, ServiceHandler.GET);
Log.e("RES", s.toString());
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				if (s != null) {
					try {
						JSONObject jsonObjectMain = new JSONObject(s);
						error = jsonObjectMain.getString("error");
						if (error.equals("0")) {
						//	arrayList_all_post.clear();
							listView.setAdapter(new MyPostAdapter());
							
							

							
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
	
	

	public class MyPostAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList_all_post.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
	
			LayoutInflater inflater = (LayoutInflater) FollowUnfollw.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.user_all_post, null);

			ImageView img_product_pic = (ImageView) rowView
					.findViewById(R.id.img_product_pic);
			ImageView img_delete = (ImageView) rowView
					.findViewById(R.id.del_img);

			TextView txt_product_name = (TextView) rowView
					.findViewById(R.id.txt_product_name);
			TextView txt_product_price = (TextView) rowView
					.findViewById(R.id.txt_product_price);
			TextView txt_product_create = (TextView) rowView
					.findViewById(R.id.txt_product_create);
			String post_user_id = arrayList_all_post.get(position).get(
					"user_id");
			String img_url = arrayList_all_post.get(position).get("ad_pic_url");
			String product_name = arrayList_all_post.get(position).get(
					"ad_name");
			String product_price = arrayList_all_post.get(position).get(
					"ad_price");
			String cre_day = arrayList_all_post.get(position).get("created");
			if (login_id.equals(user_id)) {
				img_delete.setVisibility(View.VISIBLE);
			} else {
				img_delete.setVisibility(View.GONE);
			}

			if (cre_day.contains(",")) {
				String[] day1 = cre_day.split(",");
				cre_day = day1[0];
			}
			if (cre_day.contains("ago")) {
				cre_day = cre_day.replace("ago", "");

			}
			txt_product_create.setText(cre_day + " ago");
			txt_product_name.setText(product_name);
			;
			txt_product_price.setText(product_price);
			imageLoader.DisplayImage(img_url, img_product_pic);
			
			img_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
							FollowUnfollw.this);

					// set title
					alertDialogBuilder1.setTitle("Cash");
					alertDialogBuilder1.setIcon(R.drawable.ic_launcher);
					// set dialog message
					alertDialogBuilder1
							.setMessage("Delete Post?")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											arrayList_all_post.remove(position);
											deletePost();
											dialog.dismiss();
										}
									});
					alertDialogBuilder1.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder1.create();

					// show it
					alertDialog.show();
				}
			});

			return rowView;
		}

	}

	public class MyReviewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList_all_review.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) FollowUnfollw.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.user_all_review, null);

			ImageView img_product_pic = (ImageView) rowView
					.findViewById(R.id.img_product_pic);

			TextView txt_review = (TextView) rowView
					.findViewById(R.id.txt_review);

			TextView txt_review_create = (TextView) rowView
					.findViewById(R.id.txt_review_create);

			String pro_url = arrayList_all_review.get(position).get("ad_url");
			String review = arrayList_all_review.get(position).get("review");
			String cre_day = arrayList_all_review.get(position).get("created");

			txt_review.setText(review);
			imageLoader.DisplayImage(pro_url, img_product_pic);

			if (cre_day.contains(",")) {
				String[] day1 = cre_day.split(",");
				cre_day = day1[0];
			}
			if (cre_day.contains("ago")) {
				cre_day = cre_day.replace("ago", "");

			}
			txt_review_create.setText(cre_day + " ago");

			return rowView;
		}
	}

}
