package com.chat_vendre_acheter;

import static com.chat_vendre_acheter.CommonUtilities.SENDER_ID;
import static com.chat_vendre_acheter.CommonUtilities.displayMessage;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "GCMIntentService";

	public static String msg, frnd_name, frnd_pic_url, frnd_device_token,
			frnd_id, user_name, user_pic_url, user_device_token, user_id,
			ad_name, ad_id, ad_price, ad_created, ad_image_url;

	public GCMIntentService() {
		super(SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		displayMessage(context, "Your device registred with GCM");

		ServerUtilities.register(context, "Business card", "user@gmail.com",
				registrationId);
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		displayMessage(context, getString(R.string.gcm_unregistered));
		ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		String message = intent.getExtras().getString("price");
		Log.i(TAG, "Received message   " + message);
		try {
			Log.e("notification rsp", message);
			JSONObject object = new JSONObject(message);
			JSONObject jsonObjectFrnd = object.getJSONObject("User");

			msg = jsonObjectFrnd.getString("message");
			frnd_id = jsonObjectFrnd.getString("id");
			frnd_name = jsonObjectFrnd.getString("username");
			frnd_pic_url = jsonObjectFrnd.getString("image");
			frnd_device_token = jsonObjectFrnd.getString("device_token");
			JSONObject jsonObject0 = object.getJSONObject("0");

			JSONObject jsonObjectsUser = jsonObject0.getJSONObject("User");
			user_name = jsonObjectsUser.getString("username");
			user_id = jsonObjectsUser.getString("id");
			user_pic_url = jsonObjectsUser.getString("image");

			JSONObject jsonObjectsAd = jsonObject0.getJSONObject("Ad");
			ad_id = jsonObjectsAd.getString("id");
			ad_price = jsonObjectsAd.getString("price");
			ad_created = jsonObjectsAd.getString("created");
			ad_name = jsonObjectsAd.getString("name");

			JSONObject jsonObjectsAdfile = jsonObject0.getJSONObject("Adfile");
			ad_image_url = jsonObjectsAdfile.getString("file");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		generateNotification(context, frnd_name + ":" + msg);
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	@SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {

		int icon = R.drawable.ic_launcher;
		long when = System.currentTimeMillis();
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// Create the final Notification object.
		Notification notification = new Notification(icon, message, when);

		int requestID = (int) System.currentTimeMillis();

		Intent contact = new Intent(context, ChatActivity.class);
		contact.putExtra("User Name", frnd_name);
		contact.putExtra("Product Price", ad_price);
		contact.putExtra("Product_name", ad_name);
		String cre_day = ad_created;
		if (cre_day.contains(",")) {
			String[] day1 = cre_day.split(",");
			cre_day = day1[0];
		}
		contact.putExtra("day", cre_day);
		contact.putExtra("ad_id", ad_id);
		contact.putExtra("PRO_USER_ID", user_id);
		contact.putExtra("PRO_USER_NAME", user_name);
		contact.putExtra("PRO_TOKEN", frnd_device_token);
		contact.putExtra("product_image_url", ad_image_url);
		contact.putExtra("user_image_url", frnd_pic_url);

		contact.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent intent = PendingIntent.getActivity(context, requestID,
				contact, PendingIntent.FLAG_UPDATE_CURRENT);

		notification.setLatestEventInfo(context, message, message, intent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		// Play default notification sound
		notification.defaults |= Notification.DEFAULT_SOUND;
		// Vibrate if vibrate is enabled
		notification.defaults |= Notification.DEFAULT_VIBRATE;

		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String currentDateandTime = sdf.format(new Date());
		int time = Integer.parseInt(currentDateandTime);

		notificationManager.notify(0, notification);

	}

}
