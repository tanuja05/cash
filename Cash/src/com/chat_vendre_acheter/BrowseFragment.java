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
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnCloseListener;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;
import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;
import com.chat_vendre_acheter.extra.ImageLoader;
import com.fw.twitter.TwitterShare;

public class BrowseFragment extends Fragment {
	ActionBar act_Bar;
	static Context ctx;
	public static GridView gridBrowse;
	MyAdapter myAdapter;
	ConnectionDetector cd;
	ImageLoader imageLoader;
	String likeErr, likemsg;
	TextView txtChange_loc, txt_location;
	static double latitude;
	static double longitude;
	static ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	Button btn_add_post;
	GPSTracker gps;
	PopupWindow popupWindowGroups;
	String bndl, country, city;
	SharedPreferences sharedPreferences_loginDetail;
	String login_id, name;
	static String s, error;
	static String distance;
	static String price;
	static String product_name;
	static String product_image_url;
	public static String categ, cate1, cate2, searchKord;
	String location;
	static String loginid;
	static String post_id;
	String adpp;
	static String like_count, like_status;
	static String created_day;
	static ArrayList<String> ll = new ArrayList<String>();
	Geocoder geocoder;
	String popUpContents[];
	List<Address> addresses = null;
	Slider ss;
	Dialog dialog;
	String profile_pic_url;
	static MyAdapter madapter;
	SharedPreferences preferences_location;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_browse_fragment,
				container, false);
		list.clear();
		sharedPreferences_loginDetail = getActivity().getSharedPreferences(
				"LOGIN_PREFS_NAME", 1);
		preferences_location = getActivity().getSharedPreferences("LOC", 1);
		login_id = sharedPreferences_loginDetail.getString("USERNAME", "noo");
		loginid = sharedPreferences_loginDetail.getString("ID", "0");
		profile_pic_url = sharedPreferences_loginDetail.getString(
				"PROFILE_IMAGE", "picccccc");

		txt_location = (TextView) rootView.findViewById(R.id.textView2);
		txtChange_loc = (TextView) rootView.findViewById(R.id.textView3);

		madapter = new MyAdapter();

		geocoder = new Geocoder(getActivity(), Locale.getDefault());

		gps = new GPSTracker(getActivity());

		// check if GPS enabled
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();
			Log.e("LATITUDE", latitude + "####" + longitude);
			gpsReverseCoding(latitude, longitude);
		} else {
			// can't get location
			// GPS or Network is not enabled
			// Ask user to enable GPS/network in settings
			gps.showSettingsAlert();
		}

		txt_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), location, 1).show();
			}
		});

		txtChange_loc.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent change_loc = new Intent(getActivity(),
						LocationActivity.class);
				change_loc.putExtra("splash", "browse");
				startActivity(change_loc);
			}
		});
		imageLoader = new ImageLoader(getActivity());
		gridBrowse = (GridView) rootView.findViewById(R.id.gridView1);
		cd = new ConnectionDetector(getActivity());

		if (cd.isConnectingToInternet()) {
			AllData();
		} else {
			errorDialog("Oops something is wrong wwith Internet Connection");
		}
		btn_add_post = (Button) rootView.findViewById(R.id.btn_add_post);

		btn_add_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (login_id.equals("noo")) {
					Intent i = new Intent(getActivity(), SignInActivity.class);
					startActivity(i);

				} else {
					Intent i = new Intent(getActivity(),
							AddPostingActivity.class);
					startActivity(i);
				}
			}
		});

		gridBrowse.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent grid_intent = new Intent(getActivity(),
						GridDetailsActivity.class);

				grid_intent.putExtra("post_id",
						list.get(position).get("post_id"));
				// grid_intent.putExtra("dist", list.get(position).get("dis"));

				startActivity(grid_intent);

			}
		});

		return rootView;
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

	public void notifuData() {

		HttpClient httpClient = new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			// ProgressDialog progressDialog = new
			// ProgressDialog(getActivity());

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/ads/index/"
						+ loginid;
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
						// Log.i("RESPONSE", s.toString());
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
					list.clear();
					try {
						JSONObject jsonObjectJson = new JSONObject(s);
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONArray jsonArrayList = jsonObjectJson
									.getJSONArray("list");

							HashMap<String, String> distance_hasmap;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								distance_hasmap = new HashMap<String, String>();
								distance_hasmap.put("ADAPTER", "AllData");
								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								JSONObject jsonObjectDis = jsonObject
										.getJSONObject("0");
								distance = jsonObjectDis.getString("distance");
								distance_hasmap.put("dis", distance);

								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");
								post_id = jsonObjectAd.getString("id");
								price = jsonObjectAd.getString("price");
								product_name = jsonObjectAd.getString("name");
								like_count = jsonObjectAd
										.getString("like_count");
								like_status = jsonObjectAd.getString("liked");
								created_day = jsonObjectAd.getString("created");
								distance_hasmap.put("price", price);
								distance_hasmap.put("product_name",
										product_name);
								distance_hasmap.put("like_status", like_status);

								distance_hasmap.put("post_id", post_id);

								distance_hasmap.put("like_count", like_count);
								distance_hasmap.put("created", created_day);

								ll.add(product_name);
								JSONArray jsonArrayAdfile = jsonObject
										.getJSONArray("Adfile");
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {

									JSONObject jsonObjectAdfile = jsonArrayAdfile
											.getJSONObject(j);
									product_image_url = jsonObjectAdfile
											.getString("file");
									if (jsonArrayAdfile.length() == 1) {
										distance_hasmap.put(
												"product_image_url0",
												product_image_url);

									} else if (jsonArrayAdfile.length() > 1) {
										distance_hasmap.put("product_image_url"
												+ j, product_image_url);

									}

								}
								list.add(distance_hasmap);

							}

						}
						madapter.notifyDataSetChanged();
						gridBrowse.setAdapter(madapter);
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

				String url = "http://www.cashcash.today/cash/api/ads/index/"
						+ loginid;
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
						Log.i("RESPONSE", s.toString());
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
					list.clear();
					try {
						JSONObject jsonObjectJson = new JSONObject(s);
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONArray jsonArrayList = jsonObjectJson
									.getJSONArray("list");

							HashMap<String, String> distance_hasmap;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								distance_hasmap = new HashMap<String, String>();
								distance_hasmap.put("ADAPTER", "AllData");
								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								JSONObject jsonObjectDis = jsonObject
										.getJSONObject("0");
								distance = jsonObjectDis.getString("distance");
								distance_hasmap.put("dis", distance);

								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");
								post_id = jsonObjectAd.getString("id");
								price = jsonObjectAd.getString("price");
								product_name = jsonObjectAd.getString("name");
								like_count = jsonObjectAd
										.getString("like_count");

								created_day = jsonObjectAd.getString("created");
								distance_hasmap.put("price", price);
								distance_hasmap.put("product_name",
										product_name);
								like_status = jsonObjectAd.getString("liked");
								distance_hasmap.put("like_status", like_status);

								distance_hasmap.put("post_id", post_id);

								distance_hasmap.put("like_count", like_count);
								distance_hasmap.put("created", created_day);

								ll.add(product_name);
								JSONArray jsonArrayAdfile = jsonObject
										.getJSONArray("Adfile");
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {

									JSONObject jsonObjectAdfile = jsonArrayAdfile
											.getJSONObject(j);
									product_image_url = jsonObjectAdfile
											.getString("file");
									if (jsonArrayAdfile.length() == 1) {
										distance_hasmap.put(
												"product_image_url0",
												product_image_url);

									} else if (jsonArrayAdfile.length() > 1) {
										distance_hasmap.put("product_image_url"
												+ j, product_image_url);

									}

								}
								list.add(distance_hasmap);

							}

						}
						gridBrowse.setAdapter(madapter);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				progressDialog.dismiss();

			}

		};

		asyncTask.execute((Void[]) null);
	}

	public class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = convertView;
			rowView = inflater.inflate(R.layout.browsegridinflate, parent,
					false);
			final RelativeLayout lytLike = (RelativeLayout) rowView
					.findViewById(R.id.lytLike);
			TextView txtPrice = (TextView) rowView.findViewById(R.id.textView1);
			TextView txtName = (TextView) rowView.findViewById(R.id.textView3);
			final TextView txtLike = (TextView) rowView
					.findViewById(R.id.textView2);
			TextView txtDistance = (TextView) rowView
					.findViewById(R.id.textView5);
			ImageView imgProduct = (ImageView) rowView
					.findViewById(R.id.imageView1);
			final ImageView imglikeUnlike = (ImageView) rowView
					.findViewById(R.id.imageView2);

			TextView txtDay = (TextView) rowView.findViewById(R.id.textView4);
			txtPrice.setText(list.get(position).get("price"));
			txtName.setText(list.get(position).get("product_name"));
			txtLike.setText(list.get(position).get("like_count"));
			String cre_day = list.get(position).get("created");
			String likest = list.get(position).get("like_status");
			if (likest.equals("0")) {

				lytLike.setBackgroundResource(R.drawable.unlike);
				imglikeUnlike.setBackgroundResource(R.drawable.unlike_icon);
			} else {
				lytLike.setBackgroundResource(R.drawable.like_bg);
				imglikeUnlike.setBackgroundResource(R.drawable.like_icon);
			}
			if (cre_day.contains(",")) {
				String[] day1 = cre_day.split(",");
				cre_day = day1[0];
			}
			if (cre_day.contains("ago")) {
				cre_day = cre_day.replace("ago", "");
			}
			txtDay.setText(cre_day + " Ago");

			Float float1 = Float.parseFloat(list.get(position).get("dis"));
			String dis1 = String.format("%.1f", float1);
			txtDistance.setText(dis1 + " " + "km");
			imageLoader.DisplayImage(
					list.get(position).get("product_image_url0"), imgProduct);

			lytLike.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("Postion", "" + position);
					loginid = sharedPreferences_loginDetail.getString("ID",
							"noo");
					if (loginid.equals("noo")) {
						Intent i = new Intent(getActivity(),
								SignInActivity.class);
						startActivity(i);

					} else {
						String likest = list.get(position).get("like_status");
						if (likest.equals("0")) {

							String adid = list.get(position).get("post_id");
							adpp = list.get(position).get("ADAPTER");
							like(adid, adpp);

						} else {

							// like = like - 1;
							adpp = list.get(position).get("ADAPTER");
							String adid = list.get(position).get("post_id");
							unLike(adid, adpp);

						}
					}
				}
			});

			return rowView;

		}
	}

	public void unLike(final String adid, final String adapter) {

		// ddd
		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(getActivity());

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

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

					Log.i("LOGINN", loginid + "");
					multipartEntity.addPart("data[Like][user_id]",
							new StringBody(loginid));
					multipartEntity.addPart("data[Like][ad_id]",
							new StringBody(adid));

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
						Log.e("adid   ", "" + adid + "..." + s.toString());
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
				if (adapter.equals("AllData")) {
					notifuData();
				} else if (adapter.equals("SearchData")) {
					notifySearchData();
				}

				else if (adapter.equals("categoryData")) {
					notifycategoryData();
				}

			}

		};

		asyncTask.execute((Void[]) null);

	}

	public void like(final String adid, final String adapter) {

		// ddd
		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(getActivity());

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

					Log.i("LOGINN", loginid + "");
					multipartEntity.addPart("data[Like][user_id]",
							new StringBody(loginid));
					multipartEntity.addPart("data[Like][ad_id]",
							new StringBody(adid));

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
						Log.e("adid   ", "" + adid + "..." + s.toString());
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
				if (adapter.equals("AllData")) {
					notifuData();
				} else if (adapter.equals("SearchData")) {
					notifySearchData();
				}

				else if (adapter.equals("categoryData")) {
					notifycategoryData();
				}

			}

		};

		asyncTask.execute((Void[]) null);

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH) @Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		popUpContents = new String[] { "Settings", "About", "Login", "Logout",
				"Share" };

		ctx = activity;
		final Activity act = activity;
		act_Bar = activity.getActionBar();
		act_Bar.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#4b0082")));
		
		 
		
		 
		imageLoader = new ImageLoader(getActivity());

		act_Bar.setCustomView(R.layout.actionbar_search);
		act_Bar.setDisplayShowCustomEnabled(true);
		act_Bar.setDisplayHomeAsUpEnabled(false);
		act_Bar.setHomeButtonEnabled(false);
		act_Bar.setDisplayShowHomeEnabled(false);
		View rowview = act_Bar.getCustomView();
		ss = new Slider(act);
		ImageView menu = (ImageView) rowview.findViewById(R.id.imageView1);
		SearchView search = (SearchView) rowview.findViewById(R.id.searchView1);
		final ImageView overflowMenu = (ImageView) rowview
				.findViewById(R.id.over_flow_menu);
		final ImageView profile_pic = (ImageView) rowview
				.findViewById(R.id.profile_pic);

		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				overflowMenu.setVisibility(View.GONE);
				profile_pic.setVisibility(View.GONE);
			}
		});

		search.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				if (cd.isConnectingToInternet()) {
					SearchData(query);
				} else {

					errorDialog("Oops something is wrong wwith Internet Connection");
				}
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {

				return false;
			}
		});
		menu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Slider.homebtn();
			}
		});
		search.setOnCloseListener(new OnCloseListener() {

			@Override
			public boolean onClose() {
				if (cd.isConnectingToInternet() == true) {
					overflowMenu.setVisibility(View.VISIBLE);
					profile_pic.setVisibility(View.VISIBLE);
					AllData();
				} else {
					errorDialog("Oops something is wrong wwith Internet Connection");
				}
				return false;
			}
		});

		profile_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (login_id.equals("noo")) {
					Intent intent = new Intent(getActivity(),
							SignInActivity.class);
					startActivity(intent);
				} else {
					Intent intent1 = new Intent(getActivity(), Profile.class);

					startActivity(intent1);
				}
			}
		});

		overflowMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				popupWindowGroups.showAsDropDown(v, -10, 0);

			}
		});

		popupWindowGroups = popupWindowGroups();
	}

	public void notifySearchData() {

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/ads/search/"
						+ loginid;

				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				try {

					multipartEntity.addPart("data[Ad][keyword]",
							new StringBody(searchKord));
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
						Log.i("RESPONSE", s.toString());
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
					list.clear();
					try {
						JSONObject jsonObjectJson = new JSONObject(s);
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONArray jsonArrayList = jsonObjectJson
									.getJSONArray("list");

							HashMap<String, String> distance_hasmap;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								distance_hasmap = new HashMap<String, String>();

								distance_hasmap.put("ADAPTER", "SearchData");
								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								JSONObject jsonObjectDis = jsonObject
										.getJSONObject("0");
								distance = jsonObjectDis.getString("distance");
								distance_hasmap.put("dis", distance);

								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");
								post_id = jsonObjectAd.getString("id");
								price = jsonObjectAd.getString("price");
								product_name = jsonObjectAd.getString("name");
								like_count = jsonObjectAd
										.getString("like_count");
								created_day = jsonObjectAd.getString("created");
								like_status = jsonObjectAd.getString("liked");
								distance_hasmap.put("like_status", like_status);
								distance_hasmap.put("price", price);
								distance_hasmap.put("product_name",
										product_name);
								distance_hasmap.put("post_id", post_id);

								distance_hasmap.put("like_count", like_count);
								distance_hasmap.put("created", created_day);

								ll.add(product_name);
								JSONArray jsonArrayAdfile = jsonObject
										.getJSONArray("Adfile");
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {

									JSONObject jsonObjectAdfile = jsonArrayAdfile
											.getJSONObject(j);
									product_image_url = jsonObjectAdfile
											.getString("file");
									if (jsonArrayAdfile.length() == 1) {
										distance_hasmap.put(
												"product_image_url0",
												product_image_url);

									} else if (jsonArrayAdfile.length() > 1) {
										distance_hasmap.put("product_image_url"
												+ j, product_image_url);

									}

								}
								list.add(distance_hasmap);

							}

						}
						if (list.size() > 0) {
							madapter.notifyDataSetChanged();
							gridBrowse.setAdapter(madapter);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

			}

		};

		asyncTask.execute((Void[]) null);
	}

	public void SearchData(final String searchKeyword) {

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(getActivity());

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				searchKord = searchKeyword;
				progressDialog.setMessage("Wait...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/ads/search/"
						+ loginid;

				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				try {

					multipartEntity.addPart("data[Ad][keyword]",
							new StringBody(searchKeyword));
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
						Log.i("RESPONSE", s.toString());
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
					list.clear();
					try {
						JSONObject jsonObjectJson = new JSONObject(s);
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONArray jsonArrayList = jsonObjectJson
									.getJSONArray("list");

							HashMap<String, String> distance_hasmap;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								distance_hasmap = new HashMap<String, String>();

								distance_hasmap.put("ADAPTER", "SearchData");
								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								JSONObject jsonObjectDis = jsonObject
										.getJSONObject("0");
								distance = jsonObjectDis.getString("distance");
								distance_hasmap.put("dis", distance);

								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");
								post_id = jsonObjectAd.getString("id");
								price = jsonObjectAd.getString("price");
								product_name = jsonObjectAd.getString("name");
								like_count = jsonObjectAd
										.getString("like_count");
								created_day = jsonObjectAd.getString("created");

								like_status = jsonObjectAd.getString("liked");
								distance_hasmap.put("like_status", like_status);
								distance_hasmap.put("price", price);
								distance_hasmap.put("product_name",
										product_name);
								distance_hasmap.put("post_id", post_id);

								distance_hasmap.put("like_count", like_count);
								distance_hasmap.put("created", created_day);

								ll.add(product_name);
								JSONArray jsonArrayAdfile = jsonObject
										.getJSONArray("Adfile");
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {

									JSONObject jsonObjectAdfile = jsonArrayAdfile
											.getJSONObject(j);
									product_image_url = jsonObjectAdfile
											.getString("file");
									if (jsonArrayAdfile.length() == 1) {
										distance_hasmap.put(
												"product_image_url0",
												product_image_url);

									} else if (jsonArrayAdfile.length() > 1) {
										distance_hasmap.put("product_image_url"
												+ j, product_image_url);

									}

								}
								list.add(distance_hasmap);

							}

						}

						gridBrowse.setAdapter(madapter);
					} catch (Exception e) {
						// TODO: handle exception
					}
				}

				progressDialog.dismiss();

			}

		};

		asyncTask.execute((Void[]) null);
	}

	private PopupWindow popupWindowGroups() {

		PopupWindow popupWindow = new PopupWindow(ctx);
		// the drop down list is a list view
		ListView menuList = new ListView(getActivity());
		menuList.setAdapter(new Myadapter1());
		popupWindow.setFocusable(true);
		popupWindow.setWidth(130);
		popupWindow.setHeight(400);
		popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		// set the list view as pop up window content
		popupWindow.setContentView(menuList);
		return popupWindow;
	}

	public class Myadapter1 extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return popUpContents.length;
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
			convertView = inflater.inflate(R.layout.pop_menu_inflate, null);

			TextView txtflight = (TextView) convertView
					.findViewById(R.id.txt_popup_flight);

			txtflight.setText(popUpContents[position]);
			txtflight.setTag(position);

			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					switch (position) {
					case 0:
						if (login_id.equals("noo")) {
							Intent intent = new Intent(getActivity(),
									SignInActivity.class);
							startActivity(intent);
							popupWindowGroups.dismiss();

						} else {
							Intent intent = new Intent(getActivity(),
									Settings.class);
							startActivity(intent);
							popupWindowGroups.dismiss();
						}

						break;
					case 1:
						Toast.makeText(getActivity(), "About", 1).show();
						popupWindowGroups.dismiss();
						break;
					case 2:
						if (login_id.equals("noo")) {
							Intent ii = new Intent(getActivity(),
									SignInActivity.class);
							startActivity(ii);
							popupWindowGroups.dismiss();
						} else {

							Toast.makeText(getActivity(), "Already Login", 1)
									.show();
							popupWindowGroups.dismiss();
						}

						break;
					case 3:
						if (login_id.equals("noo")) {
							Toast.makeText(getActivity(),
									"Please Firstly Login", 1).show();
							popupWindowGroups.dismiss();
						} else {
							AlertDialog.Builder builder = new AlertDialog.Builder(
									getActivity());
							builder.setTitle("Log Out")
									.setMessage("Are you sure!")
									.setPositiveButton(
											"Yes",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {

													sharedPreferences_loginDetail
															.edit().clear()
															.commit();
													Intent intent = new Intent(
															getActivity(),
															MainActivity.class);
													startActivity(intent);
													// finish();
													dialog.dismiss();
													popupWindowGroups.dismiss();
												}
											})
									.setNegativeButton(
											"No",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													dialog.dismiss();
													popupWindowGroups.dismiss();
												}
											});
							builder.show();
						}
						break;
					case 4:
						dialog = new Dialog(getActivity(), R.style.DialogTheme);
						dialog.setContentView(R.layout.share_fragment);
						dialog.setCancelable(true);
						popupWindowGroups.dismiss();
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
						Button btn_email = (Button) dialog
								.findViewById(R.id.email);
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
										"Hi! Check out Cash, it's a cool app for buying and selling. It has a built-in chat.");
								startActivity(intent2);
							}
						});

						btn_facebook.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent i = new Intent(getActivity(),
										Facebook_Share.class);
								i.putExtra("Browse", "Browse");
								startActivity(i);
								dialog.dismiss();
							}
						});

						btn_google_plus
								.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										Intent i = new Intent(getActivity(),
												Google_Plus_Share.class);
										i.putExtra("Browse", "Browse");
										startActivity(i);
										dialog.dismiss();
									}
								});

						btn_whatapp.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								PackageManager pm = getActivity()
										.getPackageManager();
								try {

									Intent waIntent = new Intent(
											Intent.ACTION_SEND);
									waIntent.setType("text/plain");
									String text = "Cash Application \n"
											+ "Hi! Check out Cash, it's a cool app for buying and selling. It has a built-in chat.\n\n"
											+ "https://www.facebook.com/profile.php?id=100006562237232";

									PackageInfo info = pm.getPackageInfo(
											"com.whatsapp",
											PackageManager.GET_META_DATA);
									// Check if package exists or not. If not
									// then code
									// in catch block will be called
									waIntent.setPackage("com.whatsapp");

									waIntent.putExtra(Intent.EXTRA_TEXT, text);
									startActivity(Intent.createChooser(
											waIntent, "Share with"));

								} catch (NameNotFoundException e) {
									Toast.makeText(getActivity(),
											"WhatsApp not Installed",
											Toast.LENGTH_SHORT).show();
								}
							}
						});

						btn_twitter.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent = new Intent(getActivity(),
										TwitterShare.class);
								intent.putExtra("Browse", "Browse");
								intent.putExtra(
										"Status",
										"Cash Application \n"
												+ "Hi! Check out Cash, it's a cool app for buying and selling. It has a built-in chat.\n\n"
												+ "https://www.facebook.com/profile.php?id=100006562237232");
								startActivity(intent);
								dialog.dismiss();
							}
						});

						lytHead.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								popupWindowGroups.dismiss();
							}
						});
						Display mDisplay = getActivity().getWindowManager()
								.getDefaultDisplay();
						final int width = mDisplay.getWidth();
						final int height = mDisplay.getHeight();
						WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
						lp.copyFrom(dialog.getWindow().getAttributes());
						lp.width = width;
						lp.height = height;
						dialog.getWindow().setAttributes(lp);
						dialog.show();
						break;

					default:
						break;
					}
				}
			});
			return convertView;
		}
	}

	public static void categoryData(final String cat, final String cat1,
			final String cat2) {

		new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(ctx);

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				categ = cat;
				cate1 = cat1;
				cate2 = cat2;
				progressDialog.setMessage("Wait...");
				progressDialog.setIndeterminate(true);
				progressDialog.setCancelable(true);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/ads/category/"
						+ loginid;
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					multipartEntity.addPart("data[Ad][latitude]",
							new StringBody("" + latitude));
					multipartEntity.addPart("data[Ad][longitude]",
							new StringBody("" + longitude));
					multipartEntity.addPart("data[Ad][cat] ", new StringBody(""
							+ cat));
					multipartEntity.addPart("data[Ad][cat1]", new StringBody(""
							+ cat1));

					multipartEntity.addPart("data[Ad][cat2] ", new StringBody(
							"" + cat2));

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
						Log.e("RESPONCSS", s.toString());
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
				Slider.homebtn();
				if (s != null) {
					list.clear();
					try {
						JSONObject jsonObjectJson = new JSONObject(s);
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONArray jsonArrayList = jsonObjectJson
									.getJSONArray("list");

							HashMap<String, String> distance_hasmap;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								distance_hasmap = new HashMap<String, String>();
								distance_hasmap.put("ADAPTER", "categoryData");
								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								JSONObject jsonObjectDis = jsonObject
										.getJSONObject("0");
								distance = jsonObjectDis.getString("distance");
								distance_hasmap.put("dis", distance);

								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");
								post_id = jsonObjectAd.getString("id");
								price = jsonObjectAd.getString("price");
								product_name = jsonObjectAd.getString("name");
								like_count = jsonObjectAd
										.getString("like_count");
								created_day = jsonObjectAd.getString("created");
								like_status = jsonObjectAd.getString("liked");
								distance_hasmap.put("like_status", like_status);
								distance_hasmap.put("price", price);
								distance_hasmap.put("product_name",
										product_name);
								distance_hasmap.put("post_id", post_id);

								distance_hasmap.put("like_count", like_count);
								distance_hasmap.put("created", created_day);

								ll.add(product_name);
								JSONArray jsonArrayAdfile = jsonObject
										.getJSONArray("Adfile");
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {

									JSONObject jsonObjectAdfile = jsonArrayAdfile
											.getJSONObject(j);
									product_image_url = jsonObjectAdfile
											.getString("file");
									if (jsonArrayAdfile.length() == 1) {
										distance_hasmap.put(
												"product_image_url0",
												product_image_url);

									} else if (jsonArrayAdfile.length() > 1) {
										distance_hasmap.put("product_image_url"
												+ j, product_image_url);

									}

								}
								list.add(distance_hasmap);

							}

						}
						if (list.size() > 0) {
							gridBrowse.setAdapter(madapter);
						} else {
							AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
									ctx);

							// set title
							alertDialogBuilder1.setTitle("Cash");
							alertDialogBuilder1.setIcon(R.drawable.ic_launcher);
							// set dialog message
							alertDialogBuilder1
									.setMessage("No Data Found")
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													// if this button is
													// clicked, close
													// current activity

													dialog.dismiss();
												}
											});

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder1
									.create();

							// show it
							alertDialog.show();
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

	public static void notifycategoryData() {

		new DefaultHttpClient();

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			ProgressDialog progressDialog = new ProgressDialog(ctx);

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub

				String url = "http://www.cashcash.today/cash/api/ads/category/"
						+ loginid;
				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					Log.e("CATEGORYY", latitude + "::" + longitude + ">>"
							+ categ + "::" + cate1 + ">" + cate2);
					multipartEntity.addPart("data[Ad][latitude]",
							new StringBody("" + latitude));
					multipartEntity.addPart("data[Ad][longitude]",
							new StringBody("" + longitude));
					multipartEntity.addPart("data[Ad][cat] ", new StringBody(""
							+ categ));
					multipartEntity.addPart("data[Ad][cat1]", new StringBody(""
							+ cate1));

					multipartEntity.addPart("data[Ad][cat2] ", new StringBody(
							"" + cate2));

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
				// Slider.homebtn();
				if (s != null) {
					list.clear();
					try {
						JSONObject jsonObjectJson = new JSONObject(s);
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONArray jsonArrayList = jsonObjectJson
									.getJSONArray("list");

							HashMap<String, String> distance_hasmap;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								distance_hasmap = new HashMap<String, String>();
								distance_hasmap.put("ADAPTER", "categoryData");
								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								JSONObject jsonObjectDis = jsonObject
										.getJSONObject("0");
								distance = jsonObjectDis.getString("distance");
								distance_hasmap.put("dis", distance);

								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");

								post_id = jsonObjectAd.getString("id");
								price = jsonObjectAd.getString("price");
								product_name = jsonObjectAd.getString("name");
								like_count = jsonObjectAd
										.getString("like_count");
								created_day = jsonObjectAd.getString("created");
								like_status = jsonObjectAd.getString("liked");
								distance_hasmap.put("like_status", like_status);
								distance_hasmap.put("price", price);
								distance_hasmap.put("product_name",
										product_name);
								distance_hasmap.put("post_id", post_id);

								distance_hasmap.put("like_count", like_count);
								distance_hasmap.put("created", created_day);

								ll.add(product_name);
								JSONArray jsonArrayAdfile = jsonObject
										.getJSONArray("Adfile");
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {

									JSONObject jsonObjectAdfile = jsonArrayAdfile
											.getJSONObject(j);
									product_image_url = jsonObjectAdfile
											.getString("file");
									if (jsonArrayAdfile.length() == 1) {
										distance_hasmap.put(
												"product_image_url0",
												product_image_url);

									} else if (jsonArrayAdfile.length() > 1) {
										distance_hasmap.put("product_image_url"
												+ j, product_image_url);

									}

								}
								list.add(distance_hasmap);

							}

						}
						if (list.size() > 0) {
							madapter.notifyDataSetChanged();
							gridBrowse.setAdapter(madapter);
						} else {
							AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
									ctx);

							// set title
							alertDialogBuilder1.setTitle("Cash");
							alertDialogBuilder1.setIcon(R.drawable.ic_launcher);
							// set dialog message
							alertDialogBuilder1
									.setMessage("No Data Found")
									.setCancelable(false)
									.setPositiveButton(
											"OK",
											new DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													// if this button is
													// clicked, close
													// current activity

													dialog.dismiss();
												}
											});

							// create alert dialog
							AlertDialog alertDialog = alertDialogBuilder1
									.create();

							// show it
							alertDialog.show();
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
	
	public void gpsReverseCoding(final double lat, final double lang){
		//public void gpsReverseCoding(double ){
			Log.e("TST", "test");
			AsyncTask<Void, Void, Void> asyncTask1 = new AsyncTask<Void, Void, Void>(){
				
				String  gpsRes;
				
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
				}

				@Override
				protected Void doInBackground(Void... params) {
					
					 try {
					String gpsUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
							+lat+","+lang+
							"&key=AIzaSyCafKLFkhIzrKCkPuDMvpdVgmzN6iMh700";
					
					Log.e("GPS", gpsUrl);
					 HttpClient client = new DefaultHttpClient();
				     HttpPost httpPost = new HttpPost(gpsUrl);
				     HttpResponse httpResponse = client.execute(httpPost);
				     gpsRes = EntityUtils.toString(httpResponse.getEntity());
				     
				     Log.e("GPS>>RES", gpsRes.toString());
				    } catch (Exception e) {
				     e.printStackTrace();
				    }
					return null;
				}
				
				@Override
				protected void onPostExecute(Void result) {
					// TODO Auto-generated method stub
					super.onPostExecute(result);
					
					try {
						JSONObject jsonObject = new JSONObject(gpsRes);	
						JSONArray array = jsonObject.getJSONArray("results");
						String address = array.getJSONObject(0).getString(
							       "formatted_address");
						txt_location.setText(address);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			};
			
			 asyncTask1.execute();
		}
}