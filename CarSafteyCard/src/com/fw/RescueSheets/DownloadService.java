package com.fw.RescueSheets;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.kfz.Rettungskarten.database.CarPdfAdapter;

public class DownloadService extends Service {
	String pdf_url;
	String url, s, msg, modelnum, carname;
	CarPdfAdapter carAdap;
	int downloadedSize = 0;
	int totalSize = 0;
	Activity activity;
	static int pos = 0;
	String 	model;
	FileOutputStream fileOutput = null;
	InputStream inputStream = null;
	HttpURLConnection urlConnection = null;
ConnectionDetector cd;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() 
	{
		carAdap = new CarPdfAdapter(DownloadService.this);
		carAdap.open();
		
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
//		Log.e("Come post exe", "startttt");
		// AllCard(count);
		cd=new ConnectionDetector(DownloadService.this);
		if(cd.isConnectingToInternet()==true){
		getdetail(0);
		Notification();}
		else
		{
			Toast.makeText(DownloadService.this, "Please connect to your Intenet",1).show();			
		}
		return super.onStartCommand(intent, flags, startId);

	}

	protected void getdetail(final int page) {
		// TODO Auto-generated method stub

		AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {
			
			private HttpResponse response;
			private String s;

			@Override
			protected void onPreExecute() {
				// // what to do before background task
				// dialog.setMessage("Validating... ");
				// dialog.setIndeterminate(true);
				// dialog.show();

			}

			@Override
			protected Void doInBackground(Void... params) {
				
				
				
				// do your background operation here
				try {
					long milli = System.currentTimeMillis();
					String url = "http://congosolution.org/carsafety_card/Cars/single/"
							+ page;

					MultipartEntity entity = new MultipartEntity(
							HttpMultipartMode.BROWSER_COMPATIBLE);

					HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(url);

					httppost.setEntity(entity);

					response = httpclient.execute(httppost);

					s = EntityUtils.toString(response.getEntity());
					Log.e("fhgfhj", s.toString()+"@@@@"+pos);

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
				// what to do when background task is completed

				try {

					JSONObject obj = new JSONObject(s);
					JSONArray arr = obj.getJSONArray("list");
					for (int i = 0; i < arr.length(); i++) {

						JSONObject obj1 = arr.getJSONObject(i);
						JSONObject obj2 = obj1.getJSONObject("Car");
						
						 carname=obj2.getString("carname");
				     	 	model=obj2.getString("model");
						String url = obj2.getString("url");
						pos = page + 1;
						
						
						String path = Environment.getExternalStorageDirectory()
								 .getAbsolutePath()
								 + "/Car safety Card/"
								 + carname
								 + "_" + model + ".pdf";
								
								 carAdap.icarname = carname;
								 carAdap.imodelname = model;
								 carAdap.isdcardpath = path;
								 carAdap.CreateRecent();
								 File SDCardRoot = new File(Environment
											.getExternalStorageDirectory().getAbsolutePath()
											+ "/Car safety Card");
									if (!SDCardRoot.exists()) {
										if (!SDCardRoot.mkdirs()) {
										}
									}
								 File file = new File(SDCardRoot, carname + "_" + model + ".pdf");

									if (file.exists()) {
										//Log.i("already", "already downloaded" + file);
										if (pos < 1551) {
											//Log.e("CAlling", pos + " calling");
											getdetail(pos);
											Notification();
										} else {

											//Log.e("llll", pos + " is completed.");
										}

									}
									else{
										Notification();
										new DownloadFileFromURL().execute(url);
									}
						
					}

				} catch (Exception e) {

					e.printStackTrace();
				}

				// dialog.cancel();

			}

		};

		updateTask.execute((Void[]) null);

	}

	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// showDialog(DIALOG_DOWNLOAD_PROGRESS);
		}

		/**
		 * Downloading file in background thread
		 *
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			try {
				// URL url1 = new URL(f_url[0]);
				final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
				String urlEncoded = Uri.encode(f_url[0], ALLOWED_URI_CHARS);
				URL url = new URL(urlEncoded);
				URLConnection conection = url.openConnection();
				conection.connect();
				// this will be useful so that you can show a tipical 0-100%
				// progress bar
				int lenghtOfFile = conection.getContentLength();

				File SDCardRoot = new File(Environment
						.getExternalStorageDirectory().getAbsolutePath()
						+ "/Car safety Card");
				if (!SDCardRoot.exists()) {
					if (!SDCardRoot.mkdirs()) {
					}
				}
				File file = new File(SDCardRoot, carname + "_" + model + ".pdf");

//				if (file.exists()) {
//					Log.i("already", "already downloaded" + file);
//
//				}

				// download the file
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				// Output stream
				OutputStream output = new FileOutputStream(file);

				byte data[] = new byte[1024];

				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					// publishing the progress....
					// After this onProgressUpdate will be called
					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
//
					// writing data to file
					output.write(data, 0, count);
				}

				// flushing output
				output.flush();
				// closing streams
				output.close();
				input.close();

			} catch (Exception e) {
				Log.e("Error: ", e.getMessage());
				e.printStackTrace();
			}

			return null;
		}

		
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			// dismissDialog(DIALOG_DOWNLOAD_PROGRESS);

			if (pos < 1551) {
			//	Log.e("CAlling", pos + " calling");
				getdetail(pos);
			} else {

				//Log.e("llll", pos + " is completed.");
			}

		}

	}

	public void Notification() 
	{
		
		Intent intent = new Intent(DownloadService.this, Search.class);	
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this)	
				.setProgress(1551, pos, false)
				.setSmallIcon(R.drawable.ic_launcher)			
				.setTicker("Herunterladen von Dateien...")			
				.setContentTitle(getString(R.string.app_name))			
				.setContentText("Herunterladen von Dateien..." + (pos+1)+"/1551")		
				.setContentIntent(pIntent)			
				.setAutoCancel(true);

		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager
		notificationmanager.notify(0, builder.build());

	}

	@Override
	public void onDestroy() 
	{
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	
}
