package com.chat_vendre_acheter.extra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.os.Handler;
import android.widget.ImageView;

import com.chat_vendre_acheter.R;

public class ImageLoader
{
	int type = 0;
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	int ischat = 0;
	int hj = 0;
	
	public static Bitmap bitmap1;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;
	Handler handler = new Handler();// handler to display images in UI thread
	Context context;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		this.context = context;
	}

	int stub_id = R.drawable.ic_launcher;

	public static Bitmap GetCurveImage(Bitmap bitmap) {

		int w = 90, h = 90;
		if (bitmap.getHeight() < bitmap.getWidth()) {
			w = bitmap.getHeight();
			h = bitmap.getHeight();
		} 
		else
		{
			w = bitmap.getWidth();
			h = bitmap.getWidth();
		}

		Bitmap rounder = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(rounder);
		Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		xferPaint.setColor(Color.RED);
		canvas.drawRoundRect(new RectF(0, 0, w, h), w / 2f, h / 2f, xferPaint);
		// Now we apply the 'magic sauce' to the paint
		xferPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		Bitmap result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas resultCanvas = new Canvas(result);
		resultCanvas.drawBitmap(bitmap, 0, 0, null);
		resultCanvas.drawBitmap(rounder, 0, 0, xferPaint);
		return result;
	}

	public static Bitmap GetCurveImagechat(Bitmap bitmap) {
		
		int w = bitmap.getWidth(), h = bitmap.getHeight();

		Bitmap rounder = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(rounder);

		Paint xferPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		xferPaint.setColor(Color.RED);		
		canvas.drawRoundRect(new RectF(0, 0, w, h), 10f, 10f, xferPaint);

		// Now we apply the 'magic sauce' to the paint
		xferPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

		Bitmap result = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas resultCanvas = new Canvas(result);
		resultCanvas.drawBitmap(bitmap, 0, 0, null);
		resultCanvas.drawBitmap(rounder, 0, 0, xferPaint);

		return result;
	}

	public Bitmap createReflectedImages(Bitmap originalImage)
			throws IOException {

		System.gc();
		// do your background operation here
		try {
			final int reflectionGap = 4;
			

			int width = originalImage.getWidth();
			int height = originalImage.getHeight();

			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);

			Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
					height / 2, width, height / 2, matrix, false);

			Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
					(height + height / 2), Config.ARGB_8888);

			Canvas canvas = new Canvas(bitmapWithReflection);

			canvas.drawBitmap(originalImage, 0, 0, null);

			Paint deafaultPaint = new Paint();
			canvas.drawRect(0, height, width, height + reflectionGap,
					deafaultPaint);

			canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

			Paint paint = new Paint();
			LinearGradient shader = new LinearGradient(0,
					originalImage.getHeight(), 0,
					bitmapWithReflection.getHeight() + reflectionGap,
					0x70ffffff, 0x00ffffff, TileMode.CLAMP);

			paint.setShader(shader);

			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

			canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
					+ reflectionGap, paint);
			return bitmapWithReflection;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return BitmapFactory.decodeResource(context.getResources(),
				R.drawable.ic_launcher);

	}

	public void DisplayImage(String url, ImageView imageView, int a) {
		hj = a;
		type = 0;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);

		if (bitmap != null)
			imageView.setImageBitmap(bitmap);

		else {
			queuePhoto(url, imageView);

		}
	}

	public void DisplayImage(String url, ImageView imageView) {
		type = 0;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);


		if (bitmap != null){
			imageView.setImageBitmap(bitmap);
		bitmap1=bitmap;
		}
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(R.drawable.ic_launcher);
		}
	}

	public void DisplayImageC(String url, ImageView imageView, int a) {

		ischat = 1;
		type = 1;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		stub_id = R.drawable.ic_launcher;

		if (bitmap != null)
			imageView.setImageBitmap(GetCurveImagechat(bitmap));

		else {
			queuePhoto(url, imageView);
			imageView.setImageBitmap(GetCurveImagechat(BitmapFactory
					.decodeResource(context.getResources(), stub_id)));
		}
	}

	public void DisplayImageC(String url, ImageView imageView) {

		ischat = 0;
		type = 1;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		stub_id = R.drawable.ic_launcher;

		if (bitmap != null)
			imageView.setImageBitmap(GetCurveImage(bitmap));

		else {
			queuePhoto(url, imageView);
			imageView.setImageBitmap(GetCurveImage(BitmapFactory
					.decodeResource(context.getResources(), stub_id)));
		}
	}

	public void DisplayImageWC(String url, ImageView imageView) {

		type = 2;
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);

		if (bitmap != null)
			try {
				imageView.setImageBitmap(createReflectedImages(bitmap));
			} catch (IOException e) {

				e.printStackTrace();
			}
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);

		}
	}

	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = null;
		if (type == 0) {
			b = decodeFileLarge(f);
		} else {
			b = decodeFile(f);
		}
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			conn.disconnect();
			if (type == 0) {
				bitmap = decodeFileLarge(f);
			} else {
				bitmap = decodeFile(f);
			}
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				memoryCache.clear();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Bitmap decodeFileLarge(File f) {
		try {
						// decode image size
			Bitmap bitmap = null;
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);

			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			int REQUIRED_SIZE = 160;

			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			if (hj == 0) {
				
				while (true) {
					if (width_tmp / 2 < REQUIRED_SIZE
							|| height_tmp / 2 < REQUIRED_SIZE)
						break;
					width_tmp /= 2;
					height_tmp /= 2;
					scale *= 2;
				}
			}

			// decode with inSampleSize

			FileInputStream stream2 = new FileInputStream(f);
			if (hj == 0) {
				BitmapFactory.Options o2 = new BitmapFactory.Options();

				o2.inSampleSize = scale;
				bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			} else {
				bitmap = BitmapFactory.decodeStream(stream2);
			}
			stream2.close();

			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		public void run() {
			try {
				if (imageViewReused(photoToLoad))
					return;
				Bitmap bmp = getBitmap(photoToLoad.url);
				memoryCache.put(photoToLoad.url, bmp);
				if (imageViewReused(photoToLoad))
					return;
				if (type == 0) {
					BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
					handler.post(bd);
				} else if (type == 1) {
					BitmapDisplayerC bd = new BitmapDisplayerC(bmp, photoToLoad);
					handler.post(bd);
				} else {
					BitmapDisplayerWC bd = new BitmapDisplayerWC(bmp,
							photoToLoad);
					handler.post(bd);
				}

			} catch (Throwable th)
			{
				th.printStackTrace();
			}
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	class BitmapDisplayerC implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayerC(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				if (ischat == 0) {
					photoToLoad.imageView.setImageBitmap(GetCurveImage(bitmap));
				} else {
					photoToLoad.imageView
							.setImageBitmap(GetCurveImagechat(bitmap));
				}
			} else {
				if (ischat == 0) {

					photoToLoad.imageView
							.setImageBitmap(GetCurveImage(BitmapFactory
									.decodeResource(context.getResources(),
											stub_id)));
				} else {
					photoToLoad.imageView
							.setImageBitmap(GetCurveImagechat(BitmapFactory
									.decodeResource(context.getResources(),
											stub_id)));

				}
			}
		}
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else {
				if (hj == 0)
					photoToLoad.imageView.setImageResource(stub_id);
			}

		}
	}

	class BitmapDisplayerWC implements Runnable 
	{
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayerWC(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				try 
				{
					photoToLoad.imageView
							.setImageBitmap(createReflectedImages(bitmap));
				} catch (IOException e) {

					e.printStackTrace();
				}
			else
				photoToLoad.imageView.setImageResource(stub_id);
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}
