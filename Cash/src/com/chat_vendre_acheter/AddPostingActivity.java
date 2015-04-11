package com.chat_vendre_acheter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chat_vendre_acheter.extra.ConnectionDetector;
import com.chat_vendre_acheter.extra.GPSTracker;

public class AddPostingActivity extends FragmentActivity {
	ImageView imgCategory, img_select, imgCoverPic, img_one, img_two,
			img_three, img_postImage, img_back, img_currency;
	TextView txt_selectimg, txt_loc_change;
	EditText et_category;
	RelativeLayout relImages, relCoverPic, relPre;
	static Bitmap bit_one, bit_two, bit_three;
	String error, msgg, s;
	static byte[] ba_one,ba_two, ba_three;
	//byte[] b_one, b_two, b_three;
	Dialog dialog;
	Uri picUri;
	String currency;
	String cate;
	String catgeory_item, cat, cat1, cat2, location, user_id, country, city;
	boolean imgone, im2, im3;
	ConnectionDetector cd;
	HttpResponse httpResponse;
	String[] catArray;
	SharedPreferences preferences, sharedPreferences;
	String url = "http://www.cashcash.today/cash/api/ads/add";
	SharedPreferences spfBitmap;
	GPSTracker gps;
	int PIC_CROP_ONE = 11, PIC_CROP_TWO = 22, PIC_CROP_THREE = 33,
			CAMERA_CAPTURE_ONE = 1, CAMERA_CAPTURE_TWO = 2,
			CAMERA_CAPTURE_THREE = 3;
	int GALLERY_CAPTURE_ONE = 10, GALLERY_CAPTURE_TWO = 20,
			GALLERY_CAPTURE_THREE = 30;
	ProgressDialog progressDialog;
	EditText et_name, et_place, et_price, et_description, et_currency;
	ArrayList<String> ll = new ArrayList<String>();
	Geocoder geocoder;
	List<Address> addresses = null;
	double latitude, longitude;
	final String[] items = {"CFA", "EURO", "USD"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.addpost_new);
		findIds();
		progressDialog = new ProgressDialog(AddPostingActivity.this);
		cd = new ConnectionDetector(AddPostingActivity.this);
		dialog = new Dialog(AddPostingActivity.this, R.style.DialogTheme);
		dialog.setContentView(R.layout.share_fragment);
		dialog.setCancelable(true);
		preferences = AddPostingActivity.this.getSharedPreferences("LOC", 1);
		sharedPreferences = AddPostingActivity.this.getSharedPreferences(
				"LOGIN_PREFS_NAME", 1);
		user_id = sharedPreferences.getString("ID", "10");

		geocoder = new Geocoder(AddPostingActivity.this, Locale.getDefault());

		gps = new GPSTracker(AddPostingActivity.this);

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

		img_select.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				selectimages(CAMERA_CAPTURE_ONE);

			}
		});

		txt_loc_change.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent change_loc = new Intent(AddPostingActivity.this,
						LocationActivity.class);
				change_loc.putExtra("splash", "addPost");
				startActivityForResult(change_loc, 4000);
			}
		});

		img_two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (im2 == true && bit_two != null) {
					imgCoverPic.setImageBitmap(bit_two);
				} else {
					selectimages(CAMERA_CAPTURE_TWO);
					im2 = true;
				}
			}
		});

		img_one.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (bit_one != null) {
					imgCoverPic.setImageBitmap(bit_one);
				}

			}
		});
		img_three.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (im3 == true && bit_three != null) {
					imgCoverPic.setImageBitmap(bit_three);
				} else {
					im3 = true;
					selectimages(CAMERA_CAPTURE_THREE);

				}
			}
		});

		imgCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(AddPostingActivity.this,
						Category_Inflate_Activity.class);
				startActivityForResult(i, 3000);
			}
		});

		img_currency.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(AddPostingActivity.this);
				builder.setTitle("Select Currency")
				    .setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialogInterface, int item) {
				            
				            et_currency.setText(items[item]);
				            currency=items[item];
				            Toast.makeText(getApplicationContext(),  currency, Toast.LENGTH_SHORT).show();
				            dialogInterface.dismiss();
				        }
				    });
				 
				builder.create().show();
			}
		});
		
		img_postImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (cd.isConnectingToInternet() == false) {
					errorDialog("Oops, Something is wrong with internet connection");
				} else {

					AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(
							AddPostingActivity.this);

					// set title
					alertDialogBuilder1.setTitle("Cash");
					alertDialogBuilder1.setIcon(R.drawable.ic_launcher);
					// set dialog message
					alertDialogBuilder1
							.setMessage("Ready to post?")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
										public void onClick(
												DialogInterface dialog, int id) {
											addPost();
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
			}

		});

		/*relPre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent pre = new Intent(AddPostingActivity.this,
						PostPreviewActivity.class);
				pre.putExtra("product_name", et_name.getText().toString());
				pre.putExtra("cateigory", cate);
				pre.putExtra("price", et_price.getText().toString());
				pre.putExtra("description", et_description.getText().toString());
				pre.putExtra("location", location);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				if (bit_one != null) {
					bit_one.compress(Bitmap.CompressFormat.PNG, 100, stream);
					b_one = stream.toByteArray();
					pre.putExtra("image1", b_one);
				}
				if (bit_two != null) {
					stream = new ByteArrayOutputStream();
					bit_two.compress(Bitmap.CompressFormat.PNG, 100, stream);
					b_two = stream.toByteArray();
					pre.putExtra("image2", b_two);
				}

				if (bit_three != null) {
					stream = new ByteArrayOutputStream();
					bit_three.compress(Bitmap.CompressFormat.PNG, 100, stream);
					b_three = stream.toByteArray();
					pre.putExtra("image3", b_three);

				}
				startActivity(pre);
			}
		});*/

		img_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});

	}

	public void findIds() {
		txt_loc_change = (TextView) findViewById(R.id.textView4);
		et_name = (EditText) findViewById(R.id.edt_name);
		et_place = (EditText) findViewById(R.id.editText3);
		et_price = (EditText) findViewById(R.id.edt_Price);
		et_description = (EditText) findViewById(R.id.et_description);
		et_category = (EditText) findViewById(R.id.edt_category);
		img_postImage = (ImageView) findViewById(R.id.imageView1);
		txt_selectimg = (TextView) findViewById(R.id.txt_selectimg);
		relImages = (RelativeLayout) findViewById(R.id.relImages);
		relCoverPic = (RelativeLayout) findViewById(R.id.relCoverPic);
		//relPre = (RelativeLayout) findViewById(R.id.pre_rel);
		imgCategory = (ImageView) findViewById(R.id.imgCategory);
		img_select = (ImageView) findViewById(R.id.img_select);
		img_currency = (ImageView) findViewById(R.id.img_curency);
		et_currency = (EditText) findViewById(R.id.edt_pricecurncey);
		imgCoverPic = (ImageView) findViewById(R.id.imgCoverPic);
		img_one = (ImageView) findViewById(R.id.img_first);
		img_three = (ImageView) findViewById(R.id.img_third);
		img_two = (ImageView) findViewById(R.id.img_second);
		img_back = (ImageView) findViewById(R.id.imageView3);
	}

	private void selectimages(final int reqcode) {

		RelativeLayout lytHead = (RelativeLayout) dialog
				.findViewById(R.id.lytHead);
		Button btn_whatapp = (Button) dialog.findViewById(R.id.whatsapp);
		Button btn_facebook = (Button) dialog.findViewById(R.id.facebook);
		Button btn_google_plus = (Button) dialog.findViewById(R.id.google_plus);
		Button btn_twitter = (Button) dialog.findViewById(R.id.twitter);
		Button btn_email = (Button) dialog.findViewById(R.id.email);
		Button btn_copy_link = (Button) dialog.findViewById(R.id.copy_link);
		btn_whatapp.setVisibility(View.GONE);
		btn_facebook.setVisibility(View.GONE);
		btn_google_plus.setVisibility(View.GONE);
		btn_twitter.setText("CAMERA");
		btn_email.setText("GALLERY");
		btn_copy_link.setText("CANCEL");
		btn_twitter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					// use standard intent to capture an image
					Intent captureIntent = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);

					// we will handle the returned data in onActivityResult
					startActivityForResult(captureIntent, reqcode);
					dialog.dismiss();
				} catch (ActivityNotFoundException anfe) {
					// display an error message
					String errorMessage = "Whoops - your device doesn't support capturing images!";
					Toast toast = Toast.makeText(AddPostingActivity.this,
							errorMessage, Toast.LENGTH_SHORT);
					toast.show();
					dialog.dismiss();
				}
			}
		});
		btn_email.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					// use standard intent to capture an image
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					intent.setType("image/*");

					// we will handle the returned data in onActivityResult
					try {

						intent.putExtra("return-data", true);
						startActivityForResult(Intent.createChooser(intent,
								"Complete action using"), reqcode);

					} catch (ActivityNotFoundException e) {
						// Do nothing for now
					}
					dialog.dismiss();
				} catch (ActivityNotFoundException anfe) {
					// display an error message
					String errorMessage = "Whoops - your device doesn't support capturing images!";
					Toast toast = Toast.makeText(AddPostingActivity.this,
							errorMessage, Toast.LENGTH_SHORT);
					toast.show();
					dialog.dismiss();
				}
			}
		});
		btn_copy_link.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
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
		Display mDisplay = AddPostingActivity.this.getWindowManager()
				.getDefaultDisplay();
		final int width = mDisplay.getWidth();
		final int height = mDisplay.getHeight();
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
		lp.copyFrom(dialog.getWindow().getAttributes());
		lp.width = width;
		lp.height = height;
		dialog.getWindow().setAttributes(lp);
		dialog.show();

	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// user is returning from capturing an image using the camera
			if (requestCode == CAMERA_CAPTURE_ONE) {
				// get the Uri for the captured image

				picUri = data.getData();
				// carry out the crop operation
				performCrop(PIC_CROP_ONE);

			} else if (requestCode == CAMERA_CAPTURE_TWO) {

				// get the Uri for the captured image
				picUri = data.getData();
				// carry out the crop operation
				performCrop(PIC_CROP_TWO);
			} else if (requestCode == CAMERA_CAPTURE_THREE) {

				// get the Uri for the captured image
				picUri = data.getData();
				// carry out the crop operation

				performCrop(PIC_CROP_THREE);
			}

			else if (requestCode == PIC_CROP_ONE) {
				// get the returned data

				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic1 = extras.getParcelable("data");
				bit_one = thePic1;

				dialog.dismiss();
				txt_selectimg.setVisibility(View.GONE);
				img_select.setVisibility(View.GONE);
				img_one.setVisibility(View.VISIBLE);

				img_two.setVisibility(View.VISIBLE);
				img_one.setImageBitmap(bit_one);
				relImages.setVisibility(View.VISIBLE);
				imgCoverPic.setVisibility(View.VISIBLE);
				imgCoverPic.setImageBitmap(bit_one);

			} else if (requestCode == PIC_CROP_TWO) {
				// get the returned data

				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				bit_two = thePic;
				//
				dialog.dismiss();

				img_select.setVisibility(View.GONE);
				txt_selectimg.setVisibility(View.GONE);
				img_one.setVisibility(View.VISIBLE);
				img_two.setVisibility(View.VISIBLE);
				img_three.setVisibility(View.VISIBLE);
				img_one.setImageBitmap(bit_one);
				img_two.setImageBitmap(bit_two);

				relImages.setVisibility(View.VISIBLE);
				imgCoverPic.setVisibility(View.VISIBLE);
				imgCoverPic.setImageBitmap(bit_two);

			} else if (requestCode == PIC_CROP_THREE) {
				// get the returned data

				Bundle extras = data.getExtras();
				// get the cropped bitmap
				Bitmap thePic = extras.getParcelable("data");
				bit_three = thePic;

				dialog.dismiss();
				img_select.setVisibility(View.GONE);
				img_one.setVisibility(View.VISIBLE);
				img_two.setVisibility(View.VISIBLE);
				img_three.setVisibility(View.VISIBLE);
				txt_selectimg.setVisibility(View.GONE);

				img_one.setImageBitmap(bit_one);
				img_two.setImageBitmap(bit_two);
				img_three.setImageBitmap(bit_three);

				relImages.setVisibility(View.VISIBLE);
				imgCoverPic.setVisibility(View.VISIBLE);
				imgCoverPic.setImageBitmap(bit_three);

			} else if (requestCode == 3000) {

				cate = data.getStringExtra("category");
				et_category.setText(cate);
				catArray = cate.split("-");
				if (catArray.length == 2) {
					cat = catArray[0];
					cat1 = catArray[1];
					cat2 = "N/A";

				} else {
					cat = catArray[0];
					cat1 = catArray[1];
					cat2 = catArray[2];
				}

			} else if (requestCode == 4000) {
				location = data.getStringExtra("Location");
				et_place.setText(location);
			}

		}
	}

	private void performCrop(int PIC_CROP) {
		// take care of exceptions
		try {

			Intent cropIntent = new Intent("com.android.camera.action.CROP");
			// indicate image type and Uri
			cropIntent.setDataAndType(picUri, "image/*");
			// set crop properties
			cropIntent.putExtra("crop", "true");
			// indicate aspect of desired crop
			cropIntent.putExtra("aspectX", 1);
			cropIntent.putExtra("aspectY", 1);
			// indicate output X and Y
			cropIntent.putExtra("outputX", 256);
			cropIntent.putExtra("outputY", 256);
			// retrieve data on return
			cropIntent.putExtra("return-data", true);
			// start the activity - we handle returning in onActivityResult
			startActivityForResult(cropIntent, PIC_CROP);
		}
		// respond to users whose devices do not support the crop action
		catch (ActivityNotFoundException anfe) {
			// display an error message
			String errorMessage = "Whoops - your device doesn't support the crop action!";
			Toast toast = Toast
					.makeText(this, errorMessage, Toast.LENGTH_SHORT);
			toast.show();
		}
	}

	public void addPost() {

		AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				progressDialog.setMessage("Please Wait...");
				progressDialog.setIndeterminate(true);
				progressDialog.show();
			}

			@Override
			protected Void doInBackground(Void... params) {

				MultipartEntity multipartEntity = new MultipartEntity(
						HttpMultipartMode.BROWSER_COMPATIBLE);
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);

				try {

					multipartEntity.addPart("data[Ad][category_id]",
							new StringBody("111"));
					multipartEntity.addPart("data[Ad][user_id]",
							new StringBody(user_id));
					multipartEntity.addPart("data[Ad][name] ", new StringBody(
							et_name.getText().toString()));
					multipartEntity.addPart("data[Ad][price]", new StringBody(
							et_price.getText().toString()+" "+currency));
					multipartEntity
							.addPart("data[Ad][description]", new StringBody(
									et_description.getText().toString()));
					Log.e("BA", bit_one+"");
					if (bit_one != null) {
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
						bit_one.compress(Bitmap.CompressFormat.PNG, 100, stream);
						ba_one = stream.toByteArray();
						multipartEntity.addPart("data[Ad][Adfile][]",
								new ByteArrayBody(ba_one, "Pic" + ".png"));
					}
					
					
					if (bit_two != null) {
						ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
						bit_two.compress(Bitmap.CompressFormat.PNG, 100, stream1);
						ba_two = stream1.toByteArray();
						multipartEntity.addPart("data[Ad][Adfile][]",
								new ByteArrayBody(ba_two, "Pic" + ".png"));
					}
					
					
					if (bit_three != null) {
						ByteArrayOutputStream stream2 = new ByteArrayOutputStream();
						bit_three.compress(Bitmap.CompressFormat.PNG, 100, stream2);
						ba_three = stream2.toByteArray();
						multipartEntity.addPart("data[Ad][Adfile][]",
								new ByteArrayBody(ba_three, "Pic" + ".png"));
					}

					multipartEntity.addPart("data[Ad][latitude]",
							new StringBody("" + latitude));
					multipartEntity.addPart("data[Ad][longitude]",
							new StringBody("" + longitude));
					multipartEntity.addPart("data[Ad][cat]",
							new StringBody(cat));
					multipartEntity.addPart("data[Ad][cat1]", new StringBody(
							cat1));
					multipartEntity.addPart("data[Ad][cat2]", new StringBody(
							cat2));
					multipartEntity.addPart("data[Ad][status] ",
							new StringBody("1"));

					httpPost.setEntity(multipartEntity);
					httpResponse = null;

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

				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				try {
					JSONObject jsonObject = new JSONObject(s);
					error = jsonObject.getString("error");
					msgg = jsonObject.getString("msg");
					if (error.equals("1")) {

						errorDialog("" + msgg);

					} else {
						Intent i = new Intent(AddPostingActivity.this,
								MainActivity.class);
						startActivity(i);
					}
				} catch (JSONException e) {
					// TODO: handle exception
					e.printStackTrace();
				}

				progressDialog.dismiss();
			}

		};
		asyncTask.execute((Void[]) null);

	}

	public void errorDialog(String str) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				AddPostingActivity.this);

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
	
	public void gpsReverseCoding(final double lat, final double lang) {
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
					String address = array.getJSONObject(0).getString(
							"formatted_address");
					et_place.setText(address);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		};

		asyncTask1.execute();
	}

}
