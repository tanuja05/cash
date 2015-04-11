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
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@SuppressLint("NewApi") public class MapsFragment extends SupportMapFragment {
	GoogleMap googleMap;
	SupportMapFragment mSupportMapFragment;
	String snippetDay, snipptUrl;
	String address1;
	String city1;
	String create_day;
	static Context ctx;
	String add_post_id;
	Dialog dialog;
	ConnectionDetector cd;
	ActionBar act_Bar;
	PopupWindow popupWindowGroups;
	String s, error, distance, price, product_name, product_image_url,
			location, post_id, like_count, created_day, lati1, longi1, spin;
	// ArrayList<String> ll = new ArrayList<String>();
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	double latitude, longitude;
	private Marker marker;
	ImageLoader imageLoader;
	GPSTracker gps;
	LatLng ltng;
	String login_id, name, profile_pic_url;
	String popUpContents[];
	SharedPreferences sharedPreferences_loginDetail;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.map_layout_new_suport,
				container, false);
		cd = new ConnectionDetector(getActivity());
		sharedPreferences_loginDetail = getActivity().getSharedPreferences(
				"LOGIN_PREFS_NAME", 1);
		login_id = sharedPreferences_loginDetail.getString("USERNAME", "noo");
		profile_pic_url = sharedPreferences_loginDetail.getString(
				"PROFILE_IMAGE", "picccccc");

		imageLoader = new ImageLoader(getActivity());
		int status = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity());
		gps = new GPSTracker(getActivity());
		if (gps.canGetLocation()) {

			latitude = gps.getLatitude();
			longitude = gps.getLongitude();

			// \n is for new line

			if (status != ConnectionResult.SUCCESS) {
				int requestCode = 10;
				Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status,
						getActivity(), requestCode);
				dialog.show();

			} else {

			}
		}

		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentManager fm = getChildFragmentManager();
		mSupportMapFragment = (SupportMapFragment) fm
				.findFragmentById(R.id.frame_new);
		if (mSupportMapFragment == null) {
			mSupportMapFragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.frame_new, mSupportMapFragment)
					.commit();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (cd.isConnectingToInternet() == true) {

			AllData();
		} else {
			errorDialog("Oops something is wrong wwith Internet Connection");
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

	private class CustomInfoWindowAdapter implements InfoWindowAdapter {

		private View view;

		public CustomInfoWindowAdapter() {
			view = getActivity().getLayoutInflater().inflate(
					R.layout.lyt_mapinfo_window, null);

		}

		@Override
		public View getInfoContents(Marker marker) {

			if (MapsFragment.this.marker != null
					&& MapsFragment.this.marker.isInfoWindowShown()) {
				MapsFragment.this.marker.hideInfoWindow();
				MapsFragment.this.marker.showInfoWindow();
			}
			return null;
		}

		@Override
		public View getInfoWindow(final Marker marker) {
			MapsFragment.this.marker = marker;

			final String title = marker.getTitle();
			final TextView titleUi = ((TextView) view
					.findViewById(R.id.user_txt));
			if (title != null) {
				titleUi.setText(title);
			} else {
				titleUi.setText("");
			}

			final String snippet = marker.getSnippet();
			final TextView snippetUi = ((TextView) view
					.findViewById(R.id.review_txt));
			final ImageView snippetImage = ((ImageView) view
					.findViewById(R.id.profile_pic));

			if (snippet != null) {

				if (snippet.contains("-")) {
					String[] created = snippet.split("-");
					snippetDay = created[0];
					snipptUrl = created[1];
					add_post_id = created[2];

				}
				snippetUi.setText(snippetDay);
				imageLoader.DisplayImage(snipptUrl, snippetImage);
			} else {
				snippetUi.setText("");
			}

			googleMap
					.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

						@Override
						public void onInfoWindowClick(Marker arg0) {
							// TODO Auto-generated method stub

							Intent grid_intent = new Intent(getActivity(),
									GridDetailsActivity.class);
							grid_intent.putExtra("post_id", add_post_id);
							grid_intent.putExtra("dist", distance);
							startActivity(grid_intent);
						}
					});

			return view;
		}
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

				String url = "http://www.cashcash.today/cash/api/ads";
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
						// Log.e("Map Res>>", s.toString());
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
						error = jsonObjectJson.getString("error");

						if (error.equals("0")) {

							JSONArray jsonArrayList = jsonObjectJson
									.getJSONArray("list");

							HashMap<String, String> distance_hasmap;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								distance_hasmap = new HashMap<String, String>();

								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								JSONObject jsonObjectDis = jsonObject
										.getJSONObject("0");

								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");
								post_id = jsonObjectAd.getString("id");
								price = jsonObjectAd.getString("price");
								lati1 = jsonObjectAd.getString("latitude");
								longi1 = jsonObjectAd.getString("longitude");
								created_day = jsonObjectAd.getString("created");
								product_name = jsonObjectAd.getString("name");

								distance_hasmap.put("price", price);
								distance_hasmap.put("product_name",
										product_name);
								distance_hasmap.put("post_id", post_id);
								distance_hasmap.put("lati1", lati1);
								distance_hasmap.put("longi1", longi1);
								distance_hasmap.put("created", created_day);

								JSONArray jsonArrayAdfile = jsonObject
										.getJSONArray("Adfile");

								Log.e("jsonArrayAdfile",
										"" + jsonArrayAdfile.length());
								for (int j = 0; j < jsonArrayAdfile.length(); j++) {
									JSONObject jsonObjectAdfile = jsonArrayAdfile
											.getJSONObject(j);
									product_image_url = jsonObjectAdfile
											.getString("file");

									distance_hasmap.put("product_image_url0",
											product_image_url);

								}
								// else if (jsonArrayAdfile.length() > 1)
								// {
								// distance_hasmap.put("product_image_url"
								// + j, product_image_url);
								// }

								list.add(distance_hasmap);

							}

						}

						if (googleMap == null) {
							googleMap = mSupportMapFragment.getMap();

							LatLng latLng = new LatLng(latitude, longitude);

							googleMap.clear();
							// googleMap.setMyLocationEnabled(true);
							googleMap
									.setInfoWindowAdapter(new CustomInfoWindowAdapter());

							for (int i = 0; i < list.size(); i++) {
								String product_name_map = list.get(i).get(
										"product_name");
								String add_id = list.get(i).get("post_id");
								String cre_day = list.get(i).get("created");
								String pro_img_url = list.get(i).get(
										"product_image_url0");

								double lat1 = Double.parseDouble(list.get(i)
										.get("lati1"));

								double long1 = Double.parseDouble(list.get(i)
										.get("longi1"));

								// if (cre_day.contains(","))
								// {
								//
								// String[] day1 = cre_day.split(",");
								// create_day = day1[0];
								// Log.e("create_day ,,,,",""+create_day);
								// if (create_day.contains("ago")) {
								// create_day = create_day.replace("ago", "");
								// Log.e("create_day in",""+create_day);
								// }
								// }
								//

								spin = cre_day + "-" + pro_img_url + "-"
										+ add_id;
								Log.e("spin", "" + spin);
								ltng = new LatLng(lat1, long1);

								googleMap
										.addMarker(new MarkerOptions()
												.position(ltng)
												.title(product_name_map)
												.snippet(spin)
												.icon(BitmapDescriptorFactory
														.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

							}

							googleMap.animateCamera(
									CameraUpdateFactory.zoomTo(0), 0, null);

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

	@Override
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
				if (cd.isConnectingToInternet() == true) {
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
						/*
						 * btn_whatapp.setOnClickListener(new OnClickListener()
						 * {
						 * 
						 * @Override public void onClick(View v) { // TODO
						 * Auto-generated method stub Intent i; PackageManager
						 * manager = getActivity() .getPackageManager(); try { i
						 * = manager .getLaunchIntentForPackage("com.whatsapp");
						 * if (i == null) throw new
						 * PackageManager.NameNotFoundException();
						 * i.addCategory(Intent.CATEGORY_LAUNCHER);
						 * startActivity(i); popupWindowGroups.dismiss(); }
						 * catch (PackageManager.NameNotFoundException e) {
						 * 
						 * } } });
						 */
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

	public void SearchData(final String searchKeyword) {

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

				String url = "http://www.cashcash.today/cash/api/ads/search/";

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
						error = jsonObjectJson.getString("error");
						list.clear();
						if (error.equals("0")) {

							JSONArray jsonArrayList = jsonObjectJson
									.getJSONArray("list");

							HashMap<String, String> distance_hasmap;
							for (int i = 0; i < jsonArrayList.length(); i++) {
								distance_hasmap = new HashMap<String, String>();

								JSONObject jsonObject = jsonArrayList
										.getJSONObject(i);
								JSONObject jsonObjectDis = jsonObject
										.getJSONObject("0");

								JSONObject jsonObjectAd = jsonObject
										.getJSONObject("Ad");
								post_id = jsonObjectAd.getString("id");
								price = jsonObjectAd.getString("price");
								lati1 = jsonObjectAd.getString("latitude");
								longi1 = jsonObjectAd.getString("longitude");
								created_day = jsonObjectAd.getString("created");
								product_name = jsonObjectAd.getString("name");
								distance_hasmap.put("price", price);
								distance_hasmap.put("product_name",
										product_name);

								distance_hasmap.put("post_id", post_id);
								distance_hasmap.put("lati1", lati1);
								distance_hasmap.put("longi1", longi1);
								distance_hasmap.put("created", created_day);

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

							googleMap.clear();
							if (googleMap != null) {
								googleMap = mSupportMapFragment.getMap();
								latitude = Double.parseDouble(list.get(0).get(
										"lati1"));
								longitude = Double.parseDouble(list.get(0).get(
										"longi1"));
								LatLng latLng = new LatLng(latitude, longitude);

								googleMap.setMyLocationEnabled(true);
								googleMap
										.setInfoWindowAdapter(new CustomInfoWindowAdapter());

								for (int i = 0; i < list.size(); i++) {

									String product_name_map = list.get(i).get(
											"product_name");
									String add_id = list.get(i).get("post_id");
									String cre_day = list.get(i).get("created");

									String pro_img_url = list.get(i).get(
											"product_image_url0");

									double lat1 = Double.parseDouble(list
											.get(i).get("lati1"));
									double long1 = Double.parseDouble(list.get(
											i).get("longi1"));
									if (cre_day.contains(",")) {
										String[] day1 = cre_day.split(",");
										create_day = day1[0];
									}

									if (create_day.contains("ago")) {
										create_day = create_day.replace("ago",
												"");
									}

									spin = create_day + "-" + pro_img_url + "-"
											+ add_id;

									ltng = new LatLng(latitude, longitude);

									googleMap
											.addMarker(new MarkerOptions()
													.position(ltng)
													.title(product_name_map)
													.snippet(spin)
													.icon(BitmapDescriptorFactory
															.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));

								}

								googleMap.moveCamera(CameraUpdateFactory
										.newLatLngZoom(latLng, 0));
								googleMap.animateCamera(
										CameraUpdateFactory.zoomTo(0), 0, null);

							}
						} else {
							Toast.makeText(getActivity(), "NO DATA FOUND", 1)
									.show();
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
}
