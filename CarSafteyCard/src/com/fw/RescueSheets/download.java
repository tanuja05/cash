package com.fw.RescueSheets;
//package com.kfz.Rettungskarten;
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.URL;
//import java.net.URLConnection;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.client.ClientProtocolException;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntity;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.util.EntityUtils;
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Environment;
//import android.util.Log;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
//public class download extends Activity {
//
//	public static final int DIALOG_DOWNLOAD_PROGRESS = 0;
//	private Button startBtn;
//	private ProgressDialog mProgressDialog;
//	static int pos = 0;
//
//	/** Called when the activity is first created. */
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.dwnl);
//		startBtn = (Button) findViewById(R.id.startBtn);
//		startBtn.setOnClickListener(new OnClickListener() {
//			public void onClick(View v) {
//				// startDownload();
//
//				getdetail(0);
//				//startDownload();
//			}
//		});
//	}
//	private void startDownload() {
//		String url = "http://congosolution.org/carsafety_card/files/Rettungskarten/Opel/Opel - Astra - F Classic 5-Doors model year from 1991.pdf";
//		new DownloadFileFromURL().execute(url);
//	}
//
//	// method for sending user detail on server
//	protected void getdetail(final int page) {
//		// TODO Auto-generated method stub
//
//		AsyncTask<Void, Void, Void> updateTask = new AsyncTask<Void, Void, Void>() {
//			ProgressDialog dialog = new ProgressDialog(download.this);
//
//			private HttpResponse response;
//			private String s;
//
//			@Override
//			protected void onPreExecute() {
//				// what to do before background task
//				dialog.setMessage("Validating... ");
//				dialog.setIndeterminate(true);
//				dialog.show();
//
//			}
//
//			@Override
//			protected Void doInBackground(Void... params) {
//
//				// do your background operation here
//				try {
//					long milli = System.currentTimeMillis();
//					String url = "http://congosolution.org/carsafety_card/Cars/single/"
//							+ page;
//
//					MultipartEntity entity = new MultipartEntity(
//							HttpMultipartMode.BROWSER_COMPATIBLE);
//
//					HttpClient httpclient = new DefaultHttpClient();
//					HttpPost httppost = new HttpPost(url);
//
//					httppost.setEntity(entity);
//
//					response = httpclient.execute(httppost);
//
//					s = EntityUtils.toString(response.getEntity());
//					Log.e("fhgfhj", s);
//
//				} catch (ClientProtocolException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//
//				return null;
//			}
//
//			@Override
//			protected void onPostExecute(Void result) {
//				// what to do when background task is completed
//
//				try {
//
//					JSONObject obj = new JSONObject(s);
//					JSONArray arr = obj.getJSONArray("list");
//					for (int i = 0; i < arr.length(); i++) {
//
//						JSONObject obj1 = arr.getJSONObject(i);
//						JSONObject obj2 = obj1.getJSONObject("Car");
//						String url = obj2.getString("url");
//						pos = page + 1;
//						new DownloadFileFromURL().execute(url);
//					}
//
//				} catch (Exception e) {
//
//					e.printStackTrace();
//				}
//
//				dialog.cancel();
//
//			}
//
//		};
//
//		updateTask.execute((Void[]) null);
//
//	}
//
//	@Override
//	protected Dialog onCreateDialog(int id) {
//		switch (id) {
//		case DIALOG_DOWNLOAD_PROGRESS:
//			mProgressDialog = new ProgressDialog(this);
//			mProgressDialog.setMessage("Downloading file..");
//			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//			mProgressDialog.setCancelable(false);
//			mProgressDialog.show();
//			return mProgressDialog;
//		default:
//			return null;
//		}
//	}
//
//	/**
//	 * Background Async Task to download file
//	 * */
//	class DownloadFileFromURL extends AsyncTask<String, String, String> {
//
//		/**
//		 * Before starting background thread Show Progress Bar Dialog
//		 * */
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			showDialog(DIALOG_DOWNLOAD_PROGRESS);
//		}
//
//		/**
//		 * Downloading file in background thread
//		 * */
//		@Override
//		protected String doInBackground(String... f_url) {
//			int count;
//			try {
//				//URL url1 = new URL(f_url[0]);
//				final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
//				String urlEncoded = Uri.encode(f_url[0], ALLOWED_URI_CHARS);
//				URL url = new URL(urlEncoded);
//				
//				URLConnection conection = url.openConnection();
//				conection.connect();
//				// this will be useful so that you can show a tipical 0-100%
//				// progress bar
//				int lenghtOfFile = conection.getContentLength();
//
//				File SDCardRoot = new File(Environment
//						.getExternalStorageDirectory().getAbsolutePath()
//						+ "/Car safety Card");
//				if (!SDCardRoot.exists()) {
//					if (!SDCardRoot.mkdirs()) {
//					}
//				}
//				File file = new File(SDCardRoot, "carname" + "_" + pos
//						+ ".pdf");
//
//				if (file.exists()) {
//					Log.i("already", "already downloaded" + file);
//
//				}
//
//				// download the file
//				InputStream input = new BufferedInputStream(url.openStream(),
//						8192);
//
//				// Output stream
//				OutputStream output = new FileOutputStream(file);
//
//				byte data[] = new byte[1024];
//
//				long total = 0;
//
//				while ((count = input.read(data)) != -1) {
//					total += count;
//					// publishing the progress....
//					// After this onProgressUpdate will be called
//					publishProgress("" + (int) ((total * 100) / lenghtOfFile));
//
//					// writing data to file
//					output.write(data, 0, count);
//				}
//
//				// flushing output
//				output.flush();
//
//				// closing streams
//				output.close();
//				input.close();
//
//			} catch (Exception e) {
//				Log.e("Error: ", e.getMessage());
//				e.printStackTrace();
//			}
//
//			return null;
//		}
//
//		/**
//		 * Updating progress bar
//		 * */
//		protected void onProgressUpdate(String... progress) {
//			// setting progress percentage
//			mProgressDialog.setProgress(Integer.parseInt(progress[0]));
//		}
//
//		/**
//		 * After completing background task Dismiss the progress dialog
//		 * **/
//		@Override
//		protected void onPostExecute(String file_url) {
//			// dismiss the dialog after the file was downloaded
//			dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
//
//			 if(pos<1551){
//			
//			 getdetail(pos);
//			 }else{
//			
//			 Log.e("llll", pos+" is completed.");
//			 }
//
//		}
//
//	}
//
// }