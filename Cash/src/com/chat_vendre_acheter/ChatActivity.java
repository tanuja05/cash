package com.chat_vendre_acheter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.ImageLoader;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class ChatActivity extends FragmentActivity {
	ImageView user_pic, product_pic, back, img_send, img_attach, img_gallery,
			img_location;
	TextView header_user_name, product_name, product_price, post_day,
			user_name;
	String str_user_pic, str_ad_id, str_product_pic, str_header_user_name,
			str_prduct_name, str_product_price, str_post_day, str_user_name,
			frnd_usr_name,address;
String chat_lat_loc, chat_lang_loc;
	 String result = null;
	EditText edt_msg;
	RelativeLayout rel_option;
	ImageLoader imageLoader;
	LinearLayout ll;
	ScrollView scrollView;
	byte[] ba;
	byte[] ba_blank;
	String str, rsp, resp, time;
	ConnectionDetector cd;
	String chat_id, chat_user_id, chat_msg, msg_create, chat_loged_in_id,
			chat_image_url, chat_loc_lat, chat_loc_lang;
	String chat_lat, chat_lang;
	SharedPreferences sharedPreferences, preferences;
	ArrayList<HashMap<String, String>> chatStore = new ArrayList<HashMap<String, String>>();
	String loged_in_id, frnd_id, frnd_token;
	Geocoder geocoder;
	List<Address> addresses = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat_activity);

		cd = new ConnectionDetector(ChatActivity.this);
		
		imageLoader = new ImageLoader(ChatActivity.this);

		sharedPreferences = ChatActivity.this.getSharedPreferences(
				"LOGIN_PREFS_NAME", 1);
		loged_in_id = sharedPreferences.getString("ID", "logooooooooo");
		preferences = ChatActivity.this.getSharedPreferences("PRO_USER", 1);

		Intent i = getIntent();
		str_header_user_name = i.getStringExtra("User Name");
		str_user_pic = i.getStringExtra("user_image_url");
		str_product_pic = i.getStringExtra("product_image_url");
		str_prduct_name = i.getStringExtra("Product_name");
		str_product_price = i.getStringExtra("Product Price");
		str_post_day = i.getStringExtra("day");
		str_ad_id = i.getStringExtra("ad_id");
		frnd_id = i.getStringExtra("PRO_USER_ID");
		frnd_token = i.getStringExtra("PRO_TOKEN");
		frnd_usr_name = i.getStringExtra("PRO_USER_NAME");
		chat();
		findId();
		dataSet();
		click();

	}

	public void findId() {
		ll = (LinearLayout) findViewById(R.id.lin_scroll);
		user_pic = (ImageView) findViewById(R.id.profile_img);
		back = (ImageView) findViewById(R.id.back);
		img_send = (ImageView) findViewById(R.id.img_send);
		edt_msg = (EditText) findViewById(R.id.edt_msg);
		product_pic = (ImageView) findViewById(R.id.product_img);
		img_attach = (ImageView) findViewById(R.id.img_attach);
		img_gallery = (ImageView) findViewById(R.id.img_gallery);
		img_location = (ImageView) findViewById(R.id.img_location);
		header_user_name = (TextView) findViewById(R.id.user_name_txt);
		product_name = (TextView) findViewById(R.id.product_name_txt);
		product_price = (TextView) findViewById(R.id.product_price_txt);
		post_day = (TextView) findViewById(R.id.today_txt);
		user_name = (TextView) findViewById(R.id.product_seller_txt);
		rel_option = (RelativeLayout) findViewById(R.id.rel_option);
		scrollView = (ScrollView) findViewById(R.id.scroll);
	}

	public void dataSet() {
		header_user_name.setText(str_header_user_name);
		product_name.setText(str_prduct_name);
		product_price.setText(str_product_price);
		user_name.setText("Seller: " + str_header_user_name);
		imageLoader.DisplayImage(str_user_pic, user_pic);
		imageLoader.DisplayImage(str_product_pic, product_pic);

		if (str_post_day.contains(",")) {
			String[] day1 = str_post_day.split(",");
			str_post_day = day1[0];
		}
		post_day.setText(str_post_day);

	}

	public void click() {

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		img_attach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				rel_option.setVisibility(View.VISIBLE);
			}
		});

		img_gallery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent.setType("image/*");

				try {
					intent.putExtra("return-data", true);
					startActivityForResult(
							Intent.createChooser(intent, "Choose image..."),
							500);
					rel_option.setVisibility(View.GONE);

				} catch (ActivityNotFoundException e) {
					// TODO: handle exception
				}
			}
		});

		img_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ChatActivity.this,
						ChatLocationActivity.class);
				intent.putExtra("key", "img_location");
				startActivityForResult(intent, 600);
				rel_option.setVisibility(View.GONE);
			}
		});

		img_send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (edt_msg.getText().toString().equals("")) {
				} else {
					str = edt_msg.getText().toString().trim();
					if(cd.isConnectingToInternet()==true){
					sendChat();}
					else{
						errorDialog("Oops something is wrong wwith Internet Connection");
					}
					// sendScroll();
				}
			}
		});

	}
	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				ChatActivity.this);

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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == 500) {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				Bitmap photo = BitmapFactory.decodeFile(picturePath);
				Log.e("PHOTO", photo + "");
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
				ba = stream.toByteArray();
				Log.e("OnAct11111", "ON111");
				if(cd.isConnectingToInternet()==true){
				sendImageChat();}
				else {
					errorDialog("Oops something is wrong wwith Internet Connection");
				}

			} else if (requestCode == 600) {
				double lat = data.getDoubleExtra("latitude", 6.4667);
				double lang = data.getDoubleExtra("longitude", 2.6000);
				Log.e("LAT-LANG", lat + "%%%%%%%%%%" + lang);
				chat_lat = new Double(lat).toString();
				chat_lang = new Double(lang).toString();
				Log.e("LAT-LANG", chat_lat + "**********" + chat_lang);
				if(cd.isConnectingToInternet()==true){
				sendLocationChat();
			}}
		}
	}

	private void sendScroll() {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				handler.post(new Runnable() {
					@Override
					public void run() {
						scrollView.fullScroll(View.FOCUS_DOWN);
					}
				});
			}
		}).start();
	}

	void sendChat() {
		AsyncTask<Void, Void, Void> allUserData = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(ChatActivity.this);

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setMessage("Validating... ");
				dialog.setIndeterminate(true);
				dialog.show();
			}

			@SuppressLint("SimpleDateFormat")
			@Override
			protected Void doInBackground(Void... params) {

				String url = "http://www.cashcash.today/cash/api/chats/add";
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				// Add your data
				try {
					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMdd_HHmmss");
					String currentDateandTime = sdf.format(new Date());
					entity.addPart("data[Chat][user_id]", new StringBody(
							loged_in_id));
					entity.addPart("data[Chat][loggedid]", new StringBody(
							frnd_id));
					entity.addPart("data[Chat][message]", new StringBody(str));
					entity.addPart("data[User][name] ", new StringBody(
							frnd_usr_name));
					entity.addPart("data[Chat][ad_id] ", new StringBody(
							str_ad_id));
					entity.addPart("data[User][device_token]  ",
							new StringBody(frnd_token));

					httppost.setEntity(entity);

					HttpResponse response;

					response = httpclient.execute(httppost);
					rsp = EntityUtils.toString(response.getEntity());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@SuppressLint("NewApi")
			@Override
			protected void onPostExecute(Void result) {
				// what to do when background task is completed
				edt_msg.setText("");
				chatStore.clear();
				try {
					JSONObject obj1 = new JSONObject(rsp);
					String status_msg = obj1.getString("list");
					String err = obj1.getString("error");

					if (err.equals("0")) {
					} else {
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				chat();
				dialog.dismiss();
			}
		};
		if ((cd.isConnectingToInternet()))
			allUserData.execute((Void[]) null);
	}

	protected void onDestroy() {
		super.onDestroy();
		this.unregisterReceiver(this.receive);
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.google.android.c2dm.intent.RECEIVE");
		filter.addCategory("m.si.mytscore");
		this.registerReceiver(this.receive, filter);

	}

	private BroadcastReceiver receive = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			GoogleCloudMessaging gcm = GoogleCloudMessaging
					.getInstance(context);
			String messageType = gcm.getMessageType(intent);
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
					.equals(messageType)) {
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
					.equals(messageType)) {
			} else {
				try {
					chatStore.clear();
					chat();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			setResultCode(Activity.RESULT_OK);
		}
	};

	void chat() {
		chatStore.clear();
		AsyncTask<Void, Void, Void> profileFeed = new AsyncTask<Void, Void, Void>() {

			HashMap<String, String> hashmap;

			@Override
			protected void onPreExecute() {
			}

			@Override
			protected Void doInBackground(Void... params) {

				String url = "http://www.cashcash.today/cash/api/chats";
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				// Add your data
				try {
					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);
					entity.addPart("data[Chat][user_id]", new StringBody(
							frnd_id));
					entity.addPart("data[Chat][loggedid]", new StringBody(
							loged_in_id));
					entity.addPart("data[Chat][ad_id]", new StringBody(
							str_ad_id));
					httppost.setEntity(entity);
					HttpResponse response;

					response = httpclient.execute(httppost);
					chatStore.clear();
					resp = EntityUtils.toString(response.getEntity());
					Log.e("CHAT", resp.toString());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				try {
					JSONObject obj = new JSONObject(resp);
					String val = obj.getString("error");
					if (val.equals("0")) {
						JSONArray arr = obj.getJSONArray("list");
						for (int i = 0; i < arr.length(); i++) {

							JSONObject obj2 = arr.getJSONObject(i);

							JSONObject jsonObjectChat = obj2
									.getJSONObject("Chat");

							chat_id = jsonObjectChat.getString("id");
							chat_user_id = jsonObjectChat.getString("user_id");
							chat_msg = jsonObjectChat.getString("message");
							msg_create = jsonObjectChat.getString("created");
							chat_loged_in_id = jsonObjectChat
									.getString("loggedid");
							chat_loc_lat = jsonObjectChat.getString("latitude");
							chat_loc_lang = jsonObjectChat
									.getString("longitude");
							chat_image_url = jsonObjectChat.getString("file");
							hashmap = new HashMap<String, String>();
							hashmap.put("chat_id", chat_id);
							hashmap.put("chat_user_id", chat_user_id);
							hashmap.put("chat_msg", chat_msg);
							hashmap.put("chat_loged_in_id", chat_loged_in_id);
							hashmap.put("msg_create", msg_create);
							hashmap.put("chat_image_url", chat_image_url);
							hashmap.put("chat_loc_lat", chat_loc_lat);
							hashmap.put("chat_loc_lang", chat_loc_lang);
							chatStore.add(hashmap);

						}
						setDataToView(chatStore);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		};
		if ((cd.isConnectingToInternet()))
			profileFeed.execute((Void[]) null);
	}

	public void setDataToView(ArrayList<HashMap<String, String>> chatArr) {
		ll.removeAllViews();
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		lp.setMargins(10, 10, 10, 10);

		for (int i = 0; i < chatArr.size(); i++) {
			final String chat_id = chatArr.get(i).get("chat_id");
			String chatFrom = chatArr.get(i).get("chat_loged_in_id");
			String textSms = chatArr.get(i).get("chat_msg");
			String date = chatArr.get(i).get("msg_create");
			final String chat_image = chatArr.get(i).get("chat_image_url");
			 chat_lat_loc = chatArr.get(i).get("chat_loc_lat");
			chat_lang_loc = chatArr.get(i).get("chat_loc_lang");
			Log.e("LOVVHB", chat_lat_loc + "" + chat_lang_loc);
			String[] dt = date.split(" ");
			String[] tt = dt[1].split(":");
			if (Integer.parseInt(tt[0]) > 12) {
				int h = Integer.parseInt(tt[0]) - 12;
				time = h + ":" + tt[1] + "PM";
			} else {
				int h = 12 - Integer.parseInt(tt[0]);
				time = h + ":" + tt[1] + "AM";
			}
			if (chatFrom.equals(loged_in_id)) {
				sendScroll();

				LinearLayout.LayoutParams linear = new LinearLayout.LayoutParams(
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				linear.setMargins(10, 10, 10, 10);
				RelativeLayout rl = new RelativeLayout(ChatActivity.this);
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				if (!textSms.equals("")
						&& chat_image.equals("")
						&& (chat_lat_loc.equals("") && chat_lang_loc.equals(""))) {

					final View menuLayout2 = inflater.inflate(
							R.layout.reciver_layout, rl, true);

					TextView tvFrndMsg = (TextView) menuLayout2
							.findViewById(R.id.frndmsg);
					TextView tvFrndTime = (TextView) menuLayout2
							.findViewById(R.id.frtime);

					tvFrndMsg.setText(textSms);
					tvFrndTime.setText(time);

					menuLayout2.setLayoutParams(linear);
					ll.addView(rl);

				} else if (textSms.equals("")
						&& !chat_image.equals("")
						&& (chat_lat_loc.equals("") && chat_lang_loc.equals(""))) {
					final View menuLayout3 = inflater.inflate(
							R.layout.reciver_image_layout, rl, true);

					ImageView reciver_image = (ImageView) menuLayout3
							.findViewById(R.id.img_reciver);

					imageLoader.DisplayImage(chat_image, reciver_image);

					menuLayout3.setLayoutParams(linear);
					ll.addView(rl);
					
					menuLayout3.setOnClickListener(new OnClickListener() {

					      @Override
					      public void onClick(View v) {
					       // TODO Auto-generated method stub
					       Intent intent = new Intent(ChatActivity.this,
					         ChatFullImage.class);
					       intent.putExtra("chat_image", chat_image);
					       startActivity(intent);
					      }
					     });
				} else if (textSms.equals("")
						&& chat_image.equals("")
						&& (!chat_lat_loc.equals("") && !chat_lang_loc
								.equals(""))) {
					final View menuLayout5 = inflater.inflate(
							R.layout.reciver_location_layout, rl, true);

					TextView reciver_location_txt = (TextView) menuLayout5
							.findViewById(R.id.loc_txt);
					final double lat1 = Double.parseDouble(chat_lat_loc);
					final double lang1 = Double.parseDouble(chat_lang_loc);
					String addres=	gpsReverseCoding(lat1, lang1);
					reciver_location_txt.setText(addres+"");
					
					
//					Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
//					
//					 try {
//						 List<Address> addressList = geocoder.getFromLocation(
//								 lat1, lang1, 1);
//						
//		                    if (addressList != null && addressList.size() > 0) {
//		                        Address address = addressList.get(0);
//		                        Log.e("Add", address.toString());
//		                        StringBuilder sb = new StringBuilder();
//		                        for (int ii = 0; ii < address.getMaxAddressLineIndex(); ii++) {
//		                            sb.append(address.getAddressLine(i)).append("\n");
//		                        }
//		                        result = address.getLocality()+","+address.getCountryName();
//		                 
//		                        reciver_location_txt.setText(result);
//						 } 
//					 }catch (IOException e) {
//						  // TODO Auto-generated catch block
//						  e.printStackTrace();
//						  reciver_location_txt.setText("Canont get Address!");
//						 }
					 menuLayout5.setLayoutParams(lp);
					 ll.addView(rl);
					 menuLayout5.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ChatActivity.this, ChatLocationActivity.class);
						intent.putExtra("key", "chat_loc");
						intent.putExtra("lat", lat1+"");
						intent.putExtra("lang", lang1+"");
						
						intent.putExtra("add",  result);
						
						startActivity(intent);
					}
				});

				}

			} else {
				RelativeLayout rl = new RelativeLayout(ChatActivity.this);
				LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				if (!textSms.equals("")
						&& chat_image.equals("")
						&& (chat_lat_loc.equals("") && chat_lang_loc.equals(""))) {
					final View menuLayout = inflater.inflate(
							R.layout.sender_layout, rl, true);

					TextView tvMsg = (TextView) menuLayout
							.findViewById(R.id.mymsg);
					TextView tvTime = (TextView) menuLayout
							.findViewById(R.id.mymsg_time);

					tvMsg.setText(textSms);
					tvTime.setText(time);
					menuLayout.setLayoutParams(lp);
					ll.addView(rl);
				} else if (textSms.equals("")
						&& !chat_image.equals("")
						&& (chat_lat_loc.equals("") && chat_lang_loc.equals(""))) {
					final View menuLayout4 = inflater.inflate(
							R.layout.sender_image_layout, rl, true);

					ImageView sender_image = (ImageView) menuLayout4
							.findViewById(R.id.my_image);

					imageLoader.DisplayImage(chat_image, sender_image);

					menuLayout4.setLayoutParams(lp);
					menuLayout4.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(ChatActivity.this,
									ChatFullImage.class);
							intent.putExtra("chat_image", chat_image);
							startActivity(intent);
						}
					});
					ll.addView(rl);
				} else if (textSms.equals("") && chat_image.equals("") && (!chat_lat_loc.equals("") && !chat_lang_loc
								.equals(""))) {
					Log.e("LOC", "loc");
				
					final View menuLayout6 = inflater.inflate(
							R.layout.sender_location_layout, rl, true);

					TextView sender_location_txt = (TextView) menuLayout6
							.findViewById(R.id.loc_txt);
					double lat1 = Double.parseDouble(chat_lat_loc);
					double lang1 = Double.parseDouble(chat_lang_loc);
					 
					Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
					
					 try {
						 List<Address> addressList = geocoder.getFromLocation(
								 lat1, lang1, 1);
						
		                    if (addressList != null && addressList.size() > 0) {
		                        Address address = addressList.get(0);
		                        Log.e("Add", address.toString());
		                        StringBuilder sb = new StringBuilder();
		                        for (int ii = 0; ii < address.getMaxAddressLineIndex(); ii++) {
		                            sb.append(address.getAddressLine(i)).append("\n");
		                        }
		                        result = address.getLocality()+","+address.getCountryName();
		                        Log.e("RES", result);
		                        sender_location_txt.setText(result);
						 } 
					 }catch (IOException e) {
						  // TODO Auto-generated catch block
						  e.printStackTrace();
						  sender_location_txt.setText("Canont get Address!");
						 }
				menuLayout6.setLayoutParams(lp);
				menuLayout6.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(ChatActivity.this, ChatLocationActivity.class);
						intent.putExtra("key", "chat_loc");
						intent.putExtra("lat", chat_lat_loc);
						intent.putExtra("lang", chat_lang_loc);
						Log.e("chat_loc", "chat_loc");
						intent.putExtra("add",  result);
						Log.e("LAT_LONG", chat_lat_loc+"__________"+chat_lang_loc);
						startActivity(intent);
					}
				});
					ll.addView(rl);

				}
			}

		}
	}

	// /////////////////////// Send Image In Chat ////////////////////////////

	void sendImageChat() {
		AsyncTask<Void, Void, Void> allUserData = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(ChatActivity.this);

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setMessage("Validating... ");
				dialog.setIndeterminate(true);
				dialog.show();
			}

			@SuppressLint("SimpleDateFormat")
			@Override
			protected Void doInBackground(Void... params) {

				String url = "http://www.cashcash.today/cash/api/chats/add";
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				// Add your data
				try {
					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMdd_HHmmss");
					String currentDateandTime = sdf.format(new Date());
					entity.addPart("data[Chat][user_id]", new StringBody(
							loged_in_id));
					entity.addPart("data[Chat][loggedid]", new StringBody(
							frnd_id));
					entity.addPart("data[Chat][message]", new StringBody(""));

					if (ba != null) {
						entity.addPart("data[Chat][file]", new ByteArrayBody(
								ba, "Pic" + ".png"));
					}

					entity.addPart("data[User][name] ", new StringBody(
							frnd_usr_name));
					entity.addPart("data[Chat][latitude]", new StringBody(""));
					entity.addPart("data[Chat][latitude]", new StringBody(""));
					entity.addPart("data[Chat][ad_id] ", new StringBody(
							str_ad_id));
					entity.addPart("data[User][device_token]  ",
							new StringBody(frnd_token));

					httppost.setEntity(entity);

					HttpResponse response;

					response = httpclient.execute(httppost);
					rsp = EntityUtils.toString(response.getEntity());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@SuppressLint("NewApi")
			@Override
			protected void onPostExecute(Void result) {
				// what to do when background task is completed

				chatStore.clear();
				try {
					JSONObject obj1 = new JSONObject(rsp);
					String status_img = obj1.getString("list");
					String err = obj1.getString("error");

					if (err.equals("0")) {
						chat();
					} else {
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				dialog.dismiss();
			}
		};
		if ((cd.isConnectingToInternet()))
			allUserData.execute((Void[]) null);
	}

	// ///////////////////// Send Location in Chat ///////////////////////

	void sendLocationChat() {
		AsyncTask<Void, Void, Void> allUserData = new AsyncTask<Void, Void, Void>() {
			ProgressDialog dialog = new ProgressDialog(ChatActivity.this);

			@Override
			protected void onPreExecute() {
				// what to do before background task
				dialog.setMessage("Validating...");
				dialog.setIndeterminate(true);
				dialog.show();
			}

			@SuppressLint("SimpleDateFormat")
			@Override
			protected Void doInBackground(Void... params) {

				String url = "http://www.cashcash.today/cash/api/chats/add";
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);

				// Add your data
				try {
					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMdd_HHmmss");
					String currentDateandTime = sdf.format(new Date());
					entity.addPart("data[Chat][user_id]", new StringBody(
							loged_in_id));
					entity.addPart("data[Chat][loggedid]", new StringBody(
							frnd_id));
					entity.addPart("data[Chat][message]", new StringBody(""));

					entity.addPart("data[User][name] ", new StringBody(
							frnd_usr_name));
					entity.addPart("data[Chat][latitude]", new StringBody(
							chat_lat));
					entity.addPart("data[Chat][longitude]", new StringBody(
							chat_lang));
					entity.addPart("data[Chat][ad_id] ", new StringBody(
							str_ad_id));
					entity.addPart("data[User][device_token]  ",
							new StringBody(frnd_token));
					httppost.setEntity(entity);

					HttpResponse response;

					response = httpclient.execute(httppost);
					rsp = EntityUtils.toString(response.getEntity());
					Log.e("CHAT_LOC_RES", rsp.toString());
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return null;
			}

			@SuppressLint("NewApi")
			@Override
			protected void onPostExecute(Void result) {
				// what to do when background task is completed

				chatStore.clear();
				try {
					JSONObject obj1 = new JSONObject(rsp);
					String status_img = obj1.getString("list");
					String err = obj1.getString("error");

					if (err.equals("0")) {
						chat();
					} else {
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

				dialog.dismiss();
			}
		};
		if ((cd.isConnectingToInternet()))
			allUserData.execute((Void[]) null);
	}
	public String gpsReverseCoding(final double lat, final double lang) {
		// public void gpsReverseCoding(double ){
		Log.e("TST", "test");
		AsyncTask<Void, Void, Void> asyncTask1 = new AsyncTask<Void, Void, Void>() {

			String gpsRes;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {

				try {
					String gpsUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
							+ lat
							+ ","
							+ lang
							+ "&key=AIzaSyCafKLFkhIzrKCkPuDMvpdVgmzN6iMh700";

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
					 address = array.getJSONObject(0).getString(
							"formatted_address");
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		asyncTask1.execute();
		return address;
	
	}
}
