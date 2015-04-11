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
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;
import com.chat_vendre_acheter.extra.ImageLoader;
import com.chat_vendre_acheter.extra.ServiceHandler;

public class ActivityFragment extends Fragment {
	CheckBox chk_all, chk_chats, chk_reviews, chk_likes;
	ListView listView, lstLike, lstReviews;
	SharedPreferences preferences;
	ArrayList<HashMap<String, String>> arrayList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> arrayListLike = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> arrayReviewLike = new ArrayList<HashMap<String, String>>();
	String login_id, message, login_name, s, error, ad_id, user_id, ad_name,
			price, day, user_name, user_profile_url, device_token,
			ad_image_url, myurl;;
	ImageLoader imageLoader;
	double latitude, longitude;
	GPSTracker gps;
	ConnectionDetector cd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_fragment, container,
				false);

		preferences = getActivity().getSharedPreferences("LOGIN_PREFS_NAME", 1);
		login_id = preferences.getString("ID", "0");
		login_name = preferences.getString("USERNAME", "noo");
		myurl = preferences.getString("PROFILE_IMAGE", "00");
		cd = new ConnectionDetector(getActivity());
		if (login_id.equals("0")) {
			Intent i = new Intent(getActivity(), SignInActivity.class);
			startActivity(i);

		} else {

			arrayList.clear();
			if (cd.isConnectingToInternet() == true) {
				recentChat();
			} else {
				errorDialog("Oops something is wrong wwith Internet Connection");
			}
			gps = new GPSTracker(getActivity());

			// check if GPS enabled
			if (gps.canGetLocation()) {

				latitude = gps.getLatitude();
				longitude = gps.getLongitude();
			} else {

				gps.showSettingsAlert();
			}

			imageLoader = new ImageLoader(getActivity());
			// chk_all = (CheckBox) rootView.findViewById(R.id.chk_all);
			chk_chats = (CheckBox) rootView.findViewById(R.id.chk_chats);
			chk_reviews = (CheckBox) rootView.findViewById(R.id.chk_reviews);
			chk_likes = (CheckBox) rootView.findViewById(R.id.chk_likes);
			listView = (ListView) rootView.findViewById(R.id.listView1);
			lstLike = (ListView) rootView.findViewById(R.id.listViewLike);
			lstReviews = (ListView) rootView.findViewById(R.id.listViewReview);
			chk_chats.setChecked(true);
			chk_likes.setChecked(false);
			chk_reviews.setChecked(false);

			chk_chats.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					if (cd.isConnectingToInternet() == true) {
						recentChat();
						listView.setVisibility(View.VISIBLE);
						lstLike.setVisibility(View.GONE);
						lstReviews.setVisibility(View.GONE);
					} else {
						errorDialog("Oops something is wrong wwith Internet Connection");
					}
					return false;
				}
			});
			chk_likes.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					if (cd.isConnectingToInternet() == true) {
						myLikes();

						listView.setVisibility(View.GONE);
						lstLike.setVisibility(View.VISIBLE);
						lstReviews.setVisibility(View.GONE);
					} else {
						errorDialog("Oops something is wrong wwith Internet Connection");
					}
					return false;
				}
			});
			chk_reviews.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					if (cd.isConnectingToInternet() == true) {
						myLikes();

						myReviews();

						listView.setVisibility(View.GONE);
						lstLike.setVisibility(View.GONE);
						lstReviews.setVisibility(View.VISIBLE);

					} else {
						errorDialog("Oops something is wrong wwith Internet Connection");
					}
					return false;
				}
			});

			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int pos, long arg3) {
					// TODO Auto-generated method stub
					Intent contact = new Intent(getActivity(),
							ChatActivity.class);
					contact.putExtra("User Name",
							arrayList.get(pos).get("user_name"));
					contact.putExtra("Product Price",
							arrayList.get(pos).get("price"));
					contact.putExtra("Product_name",
							arrayList.get(pos).get("ad_name"));
					String cre_day = arrayList.get(pos).get("day");

					if (cre_day.contains(",")) {
						String[] day1 = cre_day.split(",");
						cre_day = day1[0];
					}
					if (cre_day.contains("ago")) {
						cre_day = cre_day.replace("ago", "");
					}
					contact.putExtra("day", cre_day);
					contact.putExtra("ad_id", arrayList.get(pos).get("ad_id"));
					contact.putExtra("PRO_USER_ID",
							arrayList.get(pos).get("user_id"));
					contact.putExtra("PRO_USER_NAME",
							arrayList.get(pos).get("user_name"));
					contact.putExtra("PRO_TOKEN",
							arrayList.get(pos).get("device_token"));

					Log.e("LIST_ELSE",
							"click     "
									+ arrayList.get(pos).get("device_token"));
					contact.putExtra("product_image_url", arrayList.get(pos)
							.get("ad_image_url"));
					contact.putExtra("user_image_url",
							arrayList.get(pos).get("user_profile_url"));
					startActivity(contact);
				}
			});

		}

		return rootView;

	}

	public void recentChat() {

		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(getActivity());

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

				String url = "http://www.cashcash.today/cash/api/reviews/chat/"
						+ login_id;
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

							JSONArray listAr = jsonObjectJson
									.getJSONArray("list");
							HashMap<String, String> hashMap_chat;
							for (int i = 0; i < listAr.length(); i++) {
								hashMap_chat = new HashMap<String, String>();
								JSONObject jsonObjectChat = listAr
										.getJSONObject(i);
								JSONObject jsonObjectLastChat = jsonObjectChat
										.getJSONObject("Lastchat");
								user_id = jsonObjectLastChat
										.getString("user_id");
								ad_id = jsonObjectLastChat.getString("ad_id");
								message = jsonObjectLastChat
										.getString("message");
								hashMap_chat.put("user_id", user_id);
								hashMap_chat.put("ad_id", ad_id);
								hashMap_chat.put("message", message);
								Log.e("Log User", user_id + "    " + login_id);
								if (login_id.equals(user_id)) {

									JSONObject jsonToUserDetail = jsonObjectChat
											.getJSONObject("ToUser");

									user_name = jsonToUserDetail
											.getString("username");
									user_profile_url = jsonToUserDetail
											.getString("image");
									device_token = jsonToUserDetail
											.getString("device_token");

									hashMap_chat.put("user_name", user_name);
									hashMap_chat.put("user_profile_url",
											user_profile_url);
									hashMap_chat.put("device_token",
											device_token);
								}

								else {

									JSONObject jsonObjectUserDetail = jsonObjectChat
											.getJSONObject("User");

									user_name = jsonObjectUserDetail
											.getString("username");
									user_profile_url = jsonObjectUserDetail
											.getString("image");
									device_token = jsonObjectUserDetail
											.getString("device_token");

									hashMap_chat.put("user_name", user_name);
									hashMap_chat.put("user_profile_url",
											user_profile_url);
									hashMap_chat.put("device_token",
											device_token);
								}
								JSONObject jsonObjectAdDetail = jsonObjectChat
										.getJSONObject("Ad");

								ad_id = jsonObjectAdDetail.getString("id");
								ad_name = jsonObjectAdDetail.getString("name");
								price = jsonObjectAdDetail.getString("price");
								day = jsonObjectAdDetail.getString("created");
								hashMap_chat.put("ad_id", ad_id);
								hashMap_chat.put("ad_name", ad_name);
								hashMap_chat.put("price", price);
								hashMap_chat.put("day", day);

								JSONArray jsonArrayAdfile = jsonObjectAdDetail
										.getJSONArray("Adfile");
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {

									JSONObject jsonObjectAdfile = jsonArrayAdfile
											.getJSONObject(j);
									ad_image_url = jsonObjectAdfile
											.getString("file");
									hashMap_chat.put("ad_image_url",
											ad_image_url);
								}

								arrayList.add(hashMap_chat);

							}
							listView.setAdapter(new MyAdapter());
						} else {
							Toast.makeText(getActivity(), "No data found", 1)
									.show();
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				chk_chats.setChecked(true);
				chk_likes.setChecked(false);
				chk_reviews.setChecked(false);
				progressDialog.dismiss();

			}

		};

		asyncTask.execute((Void[]) null);
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.main_activity_all_chat_list_item, null);

			ImageView user_img = (ImageView) rowView
					.findViewById(R.id.user_img);
			ImageView ad_img = (ImageView) rowView.findViewById(R.id.ad_img);
			TextView user_name_txt = (TextView) rowView
					.findViewById(R.id.user_name_txt);
			TextView message = (TextView) rowView.findViewById(R.id.message);
			TextView message_created = (TextView) rowView
					.findViewById(R.id.message_created);

			imageLoader.DisplayImage(
					arrayList.get(position).get("user_profile_url"), user_img);
			imageLoader.DisplayImage(arrayList.get(position)
					.get("ad_image_url"), ad_img);
			user_name_txt.setText(arrayList.get(position).get("user_name"));
			message.setText(arrayList.get(position).get("message"));
			// message_created.setText(arrayList.get(position).get("day"));
			String cre_day = arrayList.get(position).get("day");

			if (cre_day.contains(",")) {
				String[] day1 = cre_day.split(",");
				cre_day = day1[0];
			}
			if (cre_day.contains("ago")) {
				cre_day = cre_day.replace("ago", "");
			}
			message_created.setText(cre_day + " Ago");

			return rowView;
		}

	}

	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

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

	public void myLikes() {
		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(getActivity());

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

				String likeurl = "http://www.cashcash.today/cash/api/likes/myLike/"
						+ login_id;
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(likeurl);

				try {
					//

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
						// Log.e("RES Like", s.toString());
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
							arrayListLike.clear();
							JSONArray listAr = jsonObjectJson
									.getJSONArray("list");
							HashMap<String, String> hashMap_like;
							for (int i = 0; i < listAr.length(); i++) {
								hashMap_like = new HashMap<String, String>();
								JSONObject jsonObjectChat = listAr
										.getJSONObject(i);

								JSONObject jsonObjectAd = jsonObjectChat
										.getJSONObject("Ad");
								String ad_id = jsonObjectAd.getString("id");
								hashMap_like.put("ad_id", ad_id);

								JSONArray jobj = jsonObjectAd
										.getJSONArray("Adfile");

								for (int ii = 0; ii < jobj.length(); ii++) {
									JSONObject jsonObjectfile = jobj
											.getJSONObject(ii);
									String ad_url = jsonObjectfile
											.getString("file");
									hashMap_like.put("ad_url", ad_url);
								}
								//
								JSONObject jobjLike = jsonObjectChat
										.getJSONObject("Like");

								String created = jobjLike.getString("created");
								hashMap_like.put("created", created);

								arrayListLike.add(hashMap_like);
							}

							lstLike.setAdapter(new MylikeAdapter());
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				progressDialog.dismiss();
				chk_chats.setChecked(false);
				chk_likes.setChecked(true);
				chk_reviews.setChecked(false);
			}

		};

		asyncTask.execute((Void[]) null);

	}

	public void myReviews() {
		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(getActivity());

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
						+ login_id;
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
							arrayReviewLike.clear();
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

								arrayReviewLike.add(hashMap_rev);
							}

							lstReviews.setAdapter(new MyReviewAdapter());
						}

					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				chk_chats.setChecked(false);
				chk_likes.setChecked(false);
				chk_reviews.setChecked(true);
				progressDialog.dismiss();

			}

		};

		asyncTask.execute((Void[]) null);

	}

	public class MylikeAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayListLike.size();
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
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.main_activity_all_chat_list_item, null);

			ImageView user_img = (ImageView) rowView
					.findViewById(R.id.user_img);
			ImageView ad_img = (ImageView) rowView.findViewById(R.id.ad_img);
			TextView user_name_txt = (TextView) rowView
					.findViewById(R.id.user_name_txt);
			TextView message = (TextView) rowView.findViewById(R.id.message);
			TextView message_created = (TextView) rowView
					.findViewById(R.id.message_created);

			imageLoader.DisplayImage(myurl, user_img);

			imageLoader.DisplayImage(arrayListLike.get(position).get("ad_url"),
					ad_img);
			user_name_txt.setText(login_name);
			message.setText("" + "Like it");
			// message_created.setText(arrayList.get(position).get("day"));
			String cre_day = arrayListLike.get(position).get("created");

			if (cre_day.contains(",")) {
				String[] day1 = cre_day.split(",");
				cre_day = day1[0];
			}
			if (cre_day.contains("ago")) {
				cre_day = cre_day.replace("ago", "");
			}
			message_created.setText(cre_day + " ago");
			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent grid_intent = new Intent(getActivity(),
							GridDetailsActivity.class);

					grid_intent.putExtra("post_id", arrayListLike.get(position)
							.get("ad_id"));

					startActivity(grid_intent);
				}
			});
			return rowView;
		}
	}

	public class MyReviewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayReviewLike.size();
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
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					R.layout.main_activity_all_chat_list_item, null);

			ImageView user_img = (ImageView) rowView
					.findViewById(R.id.user_img);
			ImageView ad_img = (ImageView) rowView.findViewById(R.id.ad_img);
			TextView user_name_txt = (TextView) rowView
					.findViewById(R.id.user_name_txt);
			TextView message = (TextView) rowView.findViewById(R.id.message);
			TextView message_created = (TextView) rowView
					.findViewById(R.id.message_created);

			imageLoader.DisplayImage(myurl, user_img);

			imageLoader.DisplayImage(arrayReviewLike.get(position)
					.get("ad_url"), ad_img);
			user_name_txt.setText(login_name);
			message.setText("" + arrayReviewLike.get(position).get("review"));
			String cre_day = arrayReviewLike.get(position).get("created");

			if (cre_day.contains(",")) {
				String[] day1 = cre_day.split(",");
				cre_day = day1[0];
			}
			if (cre_day.contains("ago")) {
				cre_day = cre_day.replace("ago", "");
			}
			message_created.setText(cre_day + " ago");
			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent grid_intent = new Intent(getActivity(),
							GridDetailsActivity.class);

					grid_intent.putExtra("post_id",
							arrayReviewLike.get(position).get("ad_id"));

					startActivity(grid_intent);
				}
			});
			return rowView;
		}
	}

}
