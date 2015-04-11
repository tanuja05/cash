package com.srm.srmodel;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.trivialdrivesample.util.IabHelper;
import com.example.android.trivialdrivesample.util.IabResult;
import com.example.android.trivialdrivesample.util.Inventory;
import com.example.android.trivialdrivesample.util.Purchase;
import com.fw.Database.PurchaseAdapter;

public class GalleryActivity extends Activity {
	static final String TAG = "SHANNON ROBINSONS";
	// The helper object
	IabHelper mHelper;
	int mTank;
	boolean mIsPremium = false;
	static final String SKU_PREMIUM = "premium";
	static String SKU_ONE = "com.srm.srmodel.shannon_001";
	// static final String SKU_TWO = " com.srm.srmodel.shannon_002";
	// static final String SKU_THREE = " com.srm.srmodel.shannon_003";
	// static final String SKU_FOUR = " com.srm.srmodel.shannon_004";
	// static final String SKU_FIVE = " com.srm.srmodel.shannon_005";
	// static final String SKU_SIX = " com.srm.srmodel.shannon_007";

	boolean mSubscribedToInfiniteGas = false;
	static final int TANK_MAX = 16;
	static final int RC_REQUEST = 10001;

	ArrayList<Integer> images = new ArrayList<Integer>();
	private int mPhotoSize, mPhotoSpacing;
	GridView grdgallery;
	ImageAdapter imageAdapter;
	ArrayList<HashMap<String, String>> purchase_statusList = new ArrayList<HashMap<String, String>>();
	ArrayList<HashMap<String, String>> final_list = new ArrayList<HashMap<String, String>>();
	Typeface ttf;
	PurchaseAdapter pAdap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gallery);
		pAdap = new PurchaseAdapter(GalleryActivity.this);
		pAdap.open();
		purchase_statusList = pAdap.fetchStudents();
	     System.gc();
		
		images.add(R.drawable.screen1);
		images.add(R.drawable.screen2);
		images.add(R.drawable.screen3);
		images.add(R.drawable.screen4);
		images.add(R.drawable.screen5);
		images.add(R.drawable.screen6);
		images.add(R.drawable.screen7);
		images.add(R.drawable.screen8);
		images.add(R.drawable.screen9);
		images.add(R.drawable.screen10);
		images.add(R.drawable.screen11);
		//unlockedd
		images.add(R.drawable.screen12);
		images.add(R.drawable.screen13);
		images.add(R.drawable.screen14);
		images.add(R.drawable.screen15);
		images.add(R.drawable.screen16);
		images.add(R.drawable.screen17);
		images.add(R.drawable.screen18);

		pAdap.open();

		grdgallery = (GridView) findViewById(R.id.albumGrid);
	
		mPhotoSize = getResources().getDimensionPixelSize(R.dimen.photo_size);
		mPhotoSpacing = getResources().getDimensionPixelSize(
				R.dimen.photo_spacing);
		imageAdapter = new ImageAdapter();
		ttf = Typeface.createFromAsset(this.getAssets(),
				"LinotypeZapfino One.ttf");
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2WYWicaiKTzdvx09s6B6j1jV17oGH6La2nQ5Vnxqhu8+0h8iOG4J7tnFnteiG7eu0qy/b+bx/8MHPFsCQ+rK3Fmyvl5Cz+7UihmVEH1LwB1NQTXcocNYWKDBPYTJG4YkoAzpsakYK9I4Xv0hp3PuDNR36CxaDu4uLn/FzElUkfb+5GCe568HASeW7fhr0N4DAS292r7cfiWzzAlLYPf6tqaPVYJCsiT7Sbi+etciNWv6n/bo4DoWMX2otEpFc0T6wT7I3NCH9EAMQjen3WPat7iKPfmNn/abRlqS4kW0cppVfbMLRlu1eW2AwyxkpFFnE56ztyIzzFAyvb+STsOlVQIDAQAB"; // Some
		
		
//		MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqP2SoFU/1nepGNGN2f+ZzvGiVPmReC2QFuvgljTPIaMddRi6ODmSUXJUZo6ukeU+SfUR99/Nd4SUdeXdUwxFyivKBr3g7IDz0cXIwL7qfbbauAS1/s1Z60RO7FYvJQ34BXLpxBQPGH6Mqhn1zE64p9JU1/ie8MnnjJegtISsdFvQ4tvgpVsv34cZiuoPEejK9// the
		if (getPackageName().startsWith("com.example")) {
			throw new RuntimeException(
					"Please change the sample's package name! See README.");
		}
		if (getPackageName().startsWith("com.example")) {
			throw new RuntimeException(
					"Please change the sample's package name! See README.");
		}

		mHelper = new IabHelper(this, base64EncodedPublicKey);
		mHelper.enableDebugLogging(true);
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {

					complain("Problem setting up in-app billing: " + result);
					return;
				}

				if (mHelper == null)
					return;

				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});

		grdgallery.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (imageAdapter.getNumColumns() == 0) {
							final int numColumns = (int) Math.floor(grdgallery
									.getWidth() / (mPhotoSize + mPhotoSpacing));
							if (numColumns > 0) {
								final int columnWidth = (grdgallery.getWidth() / numColumns)
										- mPhotoSpacing;
								imageAdapter.setNumColumns(numColumns);
								imageAdapter.setItemHeight(columnWidth);
							}
						}
					}
				});
		grdgallery.setAdapter(imageAdapter);
	}

	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			// Have we been disposed of in the meantime? If so, quit.
			if (mHelper == null)
				return;
			if (result.isFailure()) {
				complain("Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");
			// Check for gas delivery -- if we own gas, we should fill up the
			// tank immediately
			Purchase gasPurchase = inventory.getPurchase(SKU_ONE);
			if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
				Log.d(TAG, "We have gas. Consuming it.");
				mHelper.consumeAsync(inventory.getPurchase(SKU_ONE),
						mConsumeFinishedListener);
				return;
			}

			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();
		return true;
	}

	public class ImageAdapter extends BaseAdapter {
		private LayoutInflater mInflater;
		private int mItemHeight = 0;
		private int mNumColumns = 0;
		private RelativeLayout.LayoutParams mImageViewLayoutParams;

		public ImageAdapter() {
			mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mImageViewLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		}

		public int getCount() {
			return images.size();
		}

		// set numcols
		public void setNumColumns(int numColumns) {
			mNumColumns = numColumns;
		}

		public int getNumColumns() {
			return mNumColumns;
		}

		// set photo item height
		public void setItemHeight(int height) {
			if (height == mItemHeight) {
				return;
			}
			mItemHeight = height;
			mImageViewLayoutParams = new RelativeLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, mItemHeight);
			notifyDataSetChanged();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View view, ViewGroup parent) {
			ViewHolder viewholder = null;

			if (view == null) {
				view = mInflater.inflate(R.layout.galleryview, null);
				viewholder = new ViewHolder();
				viewholder.cover = (ImageView) view
						.findViewById(R.id.imageView1);
				viewholder.textView1 = (TextView) view
						.findViewById(R.id.textView1);
				viewholder.layout = (RelativeLayout) view
						.findViewById(R.id.relatv12);
				view.setTag(viewholder);

			} else {
				viewholder = (ViewHolder) view.getTag();
			}
			if (position == 12) 
			{
				purchase_statusList.clear();
				purchase_statusList = pAdap
						.searchPurchase("com.srm.srmodel.shannon_001");
				if (purchase_statusList.size() != 0) {
					if (purchase_statusList.get(0).get("purchase_status")
							.equals("unlock")) {
						viewholder.cover.setBackgroundResource(images
								.get(position));
					} else {
						viewholder.cover
								.setBackgroundResource(R.drawable.locked);
					}
				}

			}

			else if (position == 13) {
				purchase_statusList.clear();
				purchase_statusList = pAdap
						.searchPurchase("com.srm.srmodel.shannon_002");
				if (purchase_statusList.size() != 0) {

					if (purchase_statusList.get(0).get("purchase_status")
							.equals("unlock")) {
						viewholder.cover.setBackgroundResource(images
								.get(position));
					} else {
						viewholder.cover
								.setBackgroundResource(R.drawable.locked);
					}
				}

			}

			else if (position == 14) {
				purchase_statusList.clear();
				purchase_statusList = pAdap
						.searchPurchase("com.srm.srmodel.shannon_003");

				if (purchase_statusList.size() != 0) {
					if (purchase_statusList.get(0).get("purchase_status")
							.equals("unlock")) {
						viewholder.cover.setBackgroundResource(images
								.get(position));
					} else {
						viewholder.cover
								.setBackgroundResource(R.drawable.locked);
					}
				}

			}

			else if (position == 15) {
				purchase_statusList.clear();
				purchase_statusList = pAdap
						.searchPurchase("com.srm.srmodel.shannon_004");

				if (purchase_statusList.size() != 0) {
					if (purchase_statusList.get(0).get("purchase_status")
							.equals("unlock")) {
						viewholder.cover.setBackgroundResource(images
								.get(position));
					} else {
						viewholder.cover
								.setBackgroundResource(R.drawable.locked);
					}
				}

			}

			else if (position == 16) {
				purchase_statusList.clear();
				purchase_statusList = pAdap
						.searchPurchase("com.srm.srmodel.shannon_005");
				if (purchase_statusList.size() != 0) {

					if (purchase_statusList.get(0).get("purchase_status")
							.equals("unlock")) {
						viewholder.cover.setBackgroundResource(images
								.get(position));
					} else {
						viewholder.cover
								.setBackgroundResource(R.drawable.locked);
					}
				}

			}

			else if (position == 17) {
				purchase_statusList.clear();
				purchase_statusList = pAdap
						.searchPurchase("com.srm.srmodel.shannon_007");
				if (purchase_statusList.size() != 0) {
					if (purchase_statusList.get(0).get("purchase_status")
							.equals("unlock")) {
						viewholder.cover.setBackgroundResource(images
								.get(position));
					} else {

						viewholder.cover
								.setBackgroundResource(R.drawable.locked);
					}
				}
			} else {

				viewholder.cover.setBackgroundResource(images.get(position));
			}
			viewholder.cover.setLayoutParams(mImageViewLayoutParams);
			viewholder.layout.setLayoutParams(mImageViewLayoutParams);
			// Check the height matches our calculated column width
			if (viewholder.cover.getLayoutParams().height != mItemHeight) {
				viewholder.cover.setLayoutParams(mImageViewLayoutParams);

			}

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Intent i = new Intent(GalleryActivity.this,
							FullActivity.class);
					pAdap.open();
					if (position <= 11) {
						i.putExtra("status", "unlock");
						//i.putExtra("pos", position);
					
						
						i.putExtra("imname", "screen"+(position+1));
						startActivity(i);
					}
					else {
						if (position == 12) {
							SKU_ONE = "com.srm.srmodel.shannon_001";
							purchase_statusList.clear();
							purchase_statusList = pAdap.searchPurchase(""
									+ SKU_ONE);
							if (purchase_statusList.size() > 0) {
								if (purchase_statusList.get(0)
										.get("purchase_status")
										.equals("unlock")) {
									i.putExtra("status", "unlock");
								//	i.putExtra("pos", position);
									i.putExtra("imname", "screen13");
									startActivity(i);
								}

								else {

									purchaseImage(SKU_ONE);
								}
							}
						} else if (position == 13) {
							SKU_ONE = "com.srm.srmodel.shannon_002";
							purchase_statusList.clear();

							purchase_statusList = pAdap.searchPurchase(""
									+ SKU_ONE);
							if (purchase_statusList.size() > 0) {
								if (purchase_statusList.get(0)
										.get("purchase_status")
										.equals("unlock")) {
									i.putExtra("status", "unlock");
								//	i.putExtra("pos", position);
									i.putExtra("imname", "screen14");
									startActivity(i);
								} else {

									purchaseImage(SKU_ONE);
								}
							}
						} else if (position == 14) {
							SKU_ONE = "com.srm.srmodel.shannon_003";
							purchase_statusList.clear();
							purchase_statusList = pAdap.searchPurchase(""
									+ SKU_ONE);
							if (purchase_statusList.size() > 0) {
								if (purchase_statusList.get(0)
										.get("purchase_status")
										.equals("unlock")) {
									i.putExtra("status", "unlock");
									i.putExtra("imname", "screen15");
								//	i.putExtra("pos", position);
									startActivity(i);
								} else {

									purchaseImage(SKU_ONE);
								}
							}
						} else if (position == 15) {
							SKU_ONE = "com.srm.srmodel.shannon_004";
							purchase_statusList.clear();
							purchase_statusList = pAdap.searchPurchase(""
									+ SKU_ONE);
							if (purchase_statusList.size() > 0) {

								if (purchase_statusList.get(0)
										.get("purchase_status")
										.equals("unlock")) {
									i.putExtra("status", "unlock");
								//	i.putExtra("pos", position);
									i.putExtra("imname", "screen16");
									startActivity(i);
								} else {

									purchaseImage(SKU_ONE);

								}
							}
						} else if (position == 16)  {
							purchase_statusList.clear();
							SKU_ONE = "com.srm.srmodel.shannon_005";
							purchase_statusList = pAdap.searchPurchase(""
									+ SKU_ONE);
							if (purchase_statusList.size() > 0) {
								if (purchase_statusList.get(0)
										.get("purchase_status")
										.equals("unlock")) {
									i.putExtra("status", "unlock");
//									i.putExtra("pos", position);
									i.putExtra("imname", "screen17");
									startActivity(i);
								} else {

									purchaseImage(SKU_ONE);
								}
							}
						} else if (position == 17) {
							SKU_ONE = "com.srm.srmodel.shannon_007";
							purchase_statusList.clear();
							purchase_statusList = pAdap.searchPurchase(""
									+ SKU_ONE);
							if (purchase_statusList.size() > 0) {
								if (purchase_statusList.get(0)
										.get("purchase_status")
										.equals("unlock")) {
									i.putExtra("status", "unlock");
									
									i.putExtra("imname", "screen18");
									//i.putExtra("pos", position);
									startActivity(i);
								} else {

									purchaseImage(SKU_ONE);
								}
							}
						}
					}
				}
			});

			return view;
		}
	}

	class ViewHolder {
		ImageView cover;
		TextView textView1;
		RelativeLayout layout;
	}

	void complain(String message) {
		Log.e(TAG, "**** TrivialDrive Error: " + message);
		alert("Error: " + message);
	}

	void alert(String message) {
		AlertDialog.Builder bld = new AlertDialog.Builder(this);
		bld.setMessage(message);
		bld.setNeutralButton("OK", null);
		Log.d(TAG, "Showing alert dialog: " + message);
		bld.create().show();
	}

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase
					+ ", result: " + result);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			// We know this is the "gas" sku because it's the only one we
			// consume,
			// so we don't check which sku was consumed. If you have more than
			// one
			// sku, you probably should check...
			if (result.isSuccess()) {
				// successfully consumed, so we apply the effects of the item in
				// our
				// game world's logic, which in our case means filling the gas
				// tank a bit
				Log.d(TAG, "Consumption successful. Provisioning.");
				mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;

				// alert("You filled 1/4 tank. Your tank is now "
				// + String.valueOf(mTank) + "/4 full!");
			} else {
				complain("Error while consuming: " + result);
			}

			Log.d(TAG, "End consumption flow.");
		}
	};

	// We're being destroyed. It's important to dispose of the helper here!
	@Override
	public void onDestroy() {
		super.onDestroy();

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
				+ data);
		if (mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			// not handled, so handle it ourselves (here's where you'd
			// perform any handling of activity results not related to in-app
			// billing...
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				complain("Error purchasing: " + result);

				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				complain("Error purchasing. Authenticity verification failed.");

				return;
			}

			Log.e(TAG+"Shannon", "Purchase successful.");

			if (purchase.getSku().equals(SKU_ONE)) 
			{

				
				Log.e(TAG+"Shannon", "Purchase is image one. Starting gas consumption::"+SKU_ONE.toString());
				if (SKU_ONE.equals("com.srm.srmodel.shannon_001")) {
					pAdap.ipurchase_status = "unlock";
					pAdap.iposition = "12";
					pAdap.iimagename = "";
					pAdap.iproduct_id = "com.srm.srmodel.shannon_001";
					pAdap.open();
					pAdap.updateExpCategory(SKU_ONE);
//					imageAdapter = new ImageAdapter();
					imageAdapter.notifyDataSetChanged();
					grdgallery.setAdapter(imageAdapter);
				} else if (SKU_ONE.equals("com.srm.srmodel.shannon_002")) {
					pAdap.ipurchase_status = "unlock";
					pAdap.iposition = "13";
					pAdap.iimagename = "screen13";
					pAdap.iproduct_id = "com.srm.srmodel.shannon_002";
					pAdap.open();
					pAdap.updateExpCategory(SKU_ONE);
					imageAdapter.notifyDataSetChanged();
					grdgallery.setAdapter(imageAdapter);
				} else if (SKU_ONE.equals("com.srm.srmodel.shannon_003")) {
					pAdap.ipurchase_status = "unlock";
					pAdap.iposition = "14";
					pAdap.iimagename = "screen14";
					pAdap.iproduct_id = "com.srm.srmodel.shannon_003";
					pAdap.open();
					pAdap.updateExpCategory(SKU_ONE);
					imageAdapter.notifyDataSetChanged();
					grdgallery.setAdapter(imageAdapter);
				} else if (SKU_ONE.equals("com.srm.srmodel.shannon_004")) {
					pAdap.ipurchase_status = "unlock";
					pAdap.iposition = "15";
					pAdap.iimagename = "screen15";
					pAdap.iproduct_id = "com.srm.srmodel.shannon_004";
					pAdap.open();
					pAdap.updateExpCategory(SKU_ONE);
					imageAdapter.notifyDataSetChanged();
					grdgallery.setAdapter(imageAdapter);
				} else if (SKU_ONE.equals("com.srm.srmodel.shannon_005")) {
					pAdap.ipurchase_status = "unlock";
					pAdap.iposition = "16";
					pAdap.iimagename = "screen16";
					pAdap.iproduct_id = "com.srm.srmodel.shannon_005";
					pAdap.open();
					pAdap.updateExpCategory(SKU_ONE);
					imageAdapter.notifyDataSetChanged();
					grdgallery.setAdapter(imageAdapter);
				} else if (SKU_ONE.equals("com.srm.srmodel.shannon_007")) {
					pAdap.ipurchase_status = "unlock";
					pAdap.iposition = "17";
					pAdap.iimagename = "screen17";
					pAdap.iproduct_id = "com.srm.srmodel.shannon_007";
					pAdap.open();
					pAdap.updateExpCategory(SKU_ONE);
					imageAdapter.notifyDataSetChanged();
					grdgallery.setAdapter(imageAdapter);
				}

				mHelper.consumeAsync(purchase, mConsumeFinishedListener);

			} 
			else if (purchase.getSku().equals(SKU_PREMIUM)) {
				// bought the premium upgrade!
				Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
				alert("Thank you for upgrading to premium!");
				mIsPremium = true;

			}

		}
	};

	public void purchaseImage(String productId) {
		Log.d(TAG, "Buy gas button clicked.");

//		if (mSubscribedToInfiniteGas) {
//			complain("No need! You're subscribed to infinite gas. Isn't that awesome?");
//			return;
//		}

//		if (mTank >= TANK_MAX) {
//		//	complain("Your tank is full. Drive around a bit!");
//			return;
//		}

		// launch the gas purchase UI flow.

//		"https://play.google.com/store/apps/details?id=com.srm.srmodel"

		Log.d(TAG, "Launching purchase flow for gas.");

		/*
		 * TODO: for security, generate your payload here for verification. See
		 * the comments on verifyDeveloperPayload() for more info. Since this is
		 * a SAMPLE, we just use an empty string, but on a production app you
		 * should carefully generate this.
		 */
		String payload = "";

		mHelper.launchPurchaseFlow(GalleryActivity.this, productId, RC_REQUEST,
				mPurchaseFinishedListener, payload);

	}

	public void updateUi() {
		// update the car color to reflect premium status or lack thereof

	}
}
