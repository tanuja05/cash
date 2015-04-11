package com.chat_vendre_acheter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chat_vendre_acheter.pojo.Product;
import com.chat_vendre_acheter.pojo.Product.SubCategory;
import com.chat_vendre_acheter.pojo.Product.SubCategory.ItemList;

public class Category_Inflate_Activity extends FragmentActivity {
	ScrollView scrollCategory;
	LinearLayout mLinearListView;
	View mLinearView;
	ImageView category_done;
	boolean isFirstViewClick = false;
	boolean isSecondViewClick = false;
	private ArrayList<Product> pProductArrayList;
	private ArrayList<SubCategory> pAutoSubItemArrayList;
	private ArrayList<SubCategory> pRentalSubItemArrayList2;
	private ArrayList<SubCategory> pElectronicsSubItemArrayList3;
	private ArrayList<SubCategory> pDesignerSubItemArrayList4;
	private ArrayList<SubCategory> pHomeSubItemArrayList5;
	private ArrayList<SubCategory> pShareSubItemArrayList6;
	private ArrayList<SubCategory> pMobileSubItemArrayList7;
	private ArrayList<SubCategory> pBabiesSubItemArrayList8;
	private ArrayList<SubCategory> pBoutiquesSubItemArrayList9;
	int prv_click_chk, i_id, j_id;
	CheckBox last;
	String itemName1, cat, cat1, cat2;
	String[] listContent = { "January", "February", "March", "April", "May",
			"June", "July", "August", "September", "October", "November",
			"December" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.category__inflate);
		scrollCategory = (ScrollView) findViewById(R.id.scrollCategory);
		category_done = (ImageView) findViewById(R.id.imageView1);
		mLinearListView = new LinearLayout(this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		params.setMargins(0, 0, 0, 0);
		params.gravity = Gravity.CENTER;
		mLinearListView.setOrientation(LinearLayout.VERTICAL);
		category_done.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(Category_Inflate_Activity.this,
						AddPostingActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				if (cat1 != null) {
					i.putExtra("category", cat + "-" + cat1 + "-" + itemName1);
				} else {
					i.putExtra("category", cat + "-" + itemName1);
				}
				setResult(RESULT_OK, i);
				finish();
			}
		});

		ArrayList<ItemList> mItemListArray = new ArrayList<ItemList>();

		ArrayList<ItemList> mRentalApartListArray = new ArrayList<ItemList>();
		mRentalApartListArray.add(new ItemList("Rooms", ""));
		mRentalApartListArray.add(new ItemList("Studios", ""));
		mRentalApartListArray.add(new ItemList("1 bedrooms", ""));
		mRentalApartListArray.add(new ItemList("2 bedrooms", ""));
		mRentalApartListArray.add(new ItemList("3 bedrooms", ""));
		mRentalApartListArray.add(new ItemList("4+ bedrooms", ""));

		ArrayList<ItemList> mRentalRoomsListArray = new ArrayList<ItemList>();
		mRentalRoomsListArray.add(new ItemList("In apartments / Villas", ""));
		mRentalRoomsListArray.add(new ItemList("In houses / Villas", ""));

		ArrayList<ItemList> mRentalShort_VacationListArray = new ArrayList<ItemList>();
		mRentalShort_VacationListArray.add(new ItemList("Villas", ""));
		mRentalShort_VacationListArray.add(new ItemList("Apartments / Flats",
				""));
		mRentalShort_VacationListArray.add(new ItemList("Rooms", ""));

		ArrayList<ItemList> mElectronicComputerListArray = new ArrayList<ItemList>();
		mElectronicComputerListArray.add(new ItemList("Show All", ""));
		mElectronicComputerListArray.add(new ItemList("Desktop PC", ""));
		mElectronicComputerListArray.add(new ItemList("Laptop PC", ""));
		mElectronicComputerListArray.add(new ItemList("Mac", ""));
		mElectronicComputerListArray.add(new ItemList("Other", ""));

		ArrayList<ItemList> mMobileSmartListArray = new ArrayList<ItemList>();
		mMobileSmartListArray.add(new ItemList("iPhone", ""));
		mMobileSmartListArray.add(new ItemList("Samsung", ""));
		mMobileSmartListArray.add(new ItemList("HTC", ""));
		mMobileSmartListArray.add(new ItemList("LG", ""));
		mMobileSmartListArray.add(new ItemList("Blackberry", ""));
		mMobileSmartListArray.add(new ItemList("Nokia", ""));
		mMobileSmartListArray.add(new ItemList("All Others", ""));

		ArrayList<ItemList> mMobileTabListArray = new ArrayList<ItemList>();
		mMobileTabListArray.add(new ItemList("iPad", ""));
		mMobileTabListArray.add(new ItemList("Google", ""));
		mMobileTabListArray.add(new ItemList("Samsung", ""));
		mMobileTabListArray.add(new ItemList("All Others", ""));

		ArrayList<ItemList> mBoutiquesHandListArray = new ArrayList<ItemList>();
		mBoutiquesHandListArray.add(new ItemList("Home Decor", ""));
		mBoutiquesHandListArray.add(new ItemList("Fashion & Beauty", ""));
		mBoutiquesHandListArray.add(new ItemList("PaperCrafts", ""));
		mBoutiquesHandListArray.add(new ItemList("Toys", ""));
		mBoutiquesHandListArray.add(new ItemList("Simply Beautiful", ""));

		ArrayList<ItemList> mBoutiquesImportedListArray = new ArrayList<ItemList>();
		mBoutiquesImportedListArray.add(new ItemList("African", ""));
		mBoutiquesImportedListArray.add(new ItemList("American", ""));
		mBoutiquesImportedListArray.add(new ItemList("Asian", ""));
		mBoutiquesImportedListArray.add(new ItemList("European", ""));
		mBoutiquesImportedListArray.add(new ItemList("MENA", ""));
		mBoutiquesImportedListArray.add(new ItemList("International", ""));

		pAutoSubItemArrayList = new ArrayList<SubCategory>();
		pRentalSubItemArrayList2 = new ArrayList<SubCategory>();
		pElectronicsSubItemArrayList3 = new ArrayList<SubCategory>();
		pDesignerSubItemArrayList4 = new ArrayList<SubCategory>();
		pHomeSubItemArrayList5 = new ArrayList<SubCategory>();
		pShareSubItemArrayList6 = new ArrayList<SubCategory>();
		pMobileSubItemArrayList7 = new ArrayList<SubCategory>();
		pBabiesSubItemArrayList8 = new ArrayList<SubCategory>();
		pBoutiquesSubItemArrayList9 = new ArrayList<SubCategory>();
		pAutoSubItemArrayList.add(new SubCategory("Auto parts & accessories",
				mItemListArray));
		pAutoSubItemArrayList.add(new SubCategory("Cars, SUVs, Trucks",
				mItemListArray));
		pAutoSubItemArrayList
				.add(new SubCategory("Motorcyles", mItemListArray));
		pAutoSubItemArrayList.add(new SubCategory("RVs, dune buggies, boats",
				mItemListArray));
		pRentalSubItemArrayList2.add(new SubCategory("Villas / Houses",
				mItemListArray));
		pRentalSubItemArrayList2.add(new SubCategory("Apartments / Flats",
				mRentalApartListArray));
		pRentalSubItemArrayList2.add(new SubCategory("Rooms",
				mRentalRoomsListArray));
		pRentalSubItemArrayList2.add(new SubCategory("Short-term / Vacation",
				mRentalShort_VacationListArray));
		pElectronicsSubItemArrayList3.add(new SubCategory(
				"Cameras and Imaging", mItemListArray));
		pElectronicsSubItemArrayList3.add(new SubCategory("Computers",
				mElectronicComputerListArray));
		pElectronicsSubItemArrayList3.add(new SubCategory("Gaming",
				mItemListArray));
		pElectronicsSubItemArrayList3.add(new SubCategory("Accessories",
				mItemListArray));
		pDesignerSubItemArrayList4.add(new SubCategory("For Her",
				mItemListArray));
		pDesignerSubItemArrayList4.add(new SubCategory("For Him",
				mItemListArray));
		pHomeSubItemArrayList5
				.add(new SubCategory("Furniture", mItemListArray));
		pHomeSubItemArrayList5
				.add(new SubCategory("Appliances", mItemListArray));
		pHomeSubItemArrayList5.add(new SubCategory("Books & DVDs",
				mItemListArray));
		pHomeSubItemArrayList5.add(new SubCategory("Miscellaneous",
				mItemListArray));
		pShareSubItemArrayList6
				.add(new SubCategory("I NEED...", mItemListArray));
		pShareSubItemArrayList6.add(new SubCategory("I CAN OFFER...",
				mItemListArray));
		pMobileSubItemArrayList7.add(new SubCategory("Smartphones",
				mMobileSmartListArray));
		pMobileSubItemArrayList7.add(new SubCategory("Tablets",
				mMobileTabListArray));
		pMobileSubItemArrayList7.add(new SubCategory("Accessories",
				mItemListArray));
		pBabiesSubItemArrayList8.add(new SubCategory("0-6 months",
				mItemListArray));
		pBabiesSubItemArrayList8.add(new SubCategory("6-12 months",
				mItemListArray));
		pBabiesSubItemArrayList8.add(new SubCategory("1-2 years",
				mItemListArray));
		pBabiesSubItemArrayList8.add(new SubCategory("2-5 years",
				mItemListArray));
		pBabiesSubItemArrayList8
				.add(new SubCategory("All Ages", mItemListArray));
		pBoutiquesSubItemArrayList9.add(new SubCategory("Handmade",
				mBoutiquesHandListArray));
		pBoutiquesSubItemArrayList9.add(new SubCategory("Imported",
				mBoutiquesImportedListArray));
		pBoutiquesSubItemArrayList9
				.add(new SubCategory("Other", mItemListArray));

		pProductArrayList = new ArrayList<Product>();
		pProductArrayList.add(new Product("AUTOS", R.drawable.auto_icon,
				pAutoSubItemArrayList));
		pProductArrayList.add(new Product("RENTALS", R.drawable.rental_icon,
				pRentalSubItemArrayList2));
		pProductArrayList.add(new Product("ELECTRONICS",
				R.drawable.electronics, pElectronicsSubItemArrayList3));
		pProductArrayList.add(new Product("DESIGNER FASHION",
				R.drawable.clothes, pDesignerSubItemArrayList4));
		pProductArrayList.add(new Product("HOME", R.drawable.home,
				pHomeSubItemArrayList5));
		pProductArrayList.add(new Product("SHARE & EXCHANGE", R.drawable.share,
				pShareSubItemArrayList6));
		pProductArrayList.add(new Product("MOBILE DEVICES", R.drawable.mobile,
				pMobileSubItemArrayList7));
		pProductArrayList.add(new Product("BABIES & KIDS", R.drawable.babies,
				pBabiesSubItemArrayList8));
		pProductArrayList.add(new Product("M-BOUTIQUES", R.drawable.boutiques,
				pBoutiquesSubItemArrayList9));

		for (int i = 0; i < pProductArrayList.size(); i++) {
			final int pos_i = i;

			LayoutInflater inflater = null;
			inflater = (LayoutInflater) getApplicationContext()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mLinearView = inflater.inflate(R.layout.row_first_category, null);

			final ImageView mProductImge = (ImageView) mLinearView
					.findViewById(R.id.icon);
			final TextView mProductName = (TextView) mLinearView
					.findViewById(R.id.textViewName);
			final RelativeLayout mLinearFirstArrow = (RelativeLayout) mLinearView
					.findViewById(R.id.linearFirst);
			final ImageView mImageArrowFirst = (ImageView) mLinearView
					.findViewById(R.id.imageFirstArrow);
			final LinearLayout mLinearScrollSecond = (LinearLayout) mLinearView
					.findViewById(R.id.linear_scroll);

			if (isFirstViewClick == false) {
				mLinearScrollSecond.setVisibility(View.GONE);
				mImageArrowFirst.setBackgroundResource(R.drawable.category);
			} else {
				mLinearScrollSecond.setVisibility(View.VISIBLE);
				mImageArrowFirst.setBackgroundResource(R.drawable.category);
			}
			mLinearFirstArrow.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (isFirstViewClick == false) {
						isFirstViewClick = true;
						mImageArrowFirst
								.setBackgroundResource(R.drawable.category);
						cat = mProductName.getTag().toString();
						mLinearScrollSecond.setVisibility(View.VISIBLE);

					} else {
						isFirstViewClick = false;
						mImageArrowFirst
								.setBackgroundResource(R.drawable.category);
						mLinearScrollSecond.setVisibility(View.GONE);
					}

					return false;
				}
			});

			final String name = pProductArrayList.get(i).getpName();
			mProductName.setTag(name);
			mProductName.setText(name);

			final int icon = pProductArrayList.get(i).getimg_icon();
			mProductImge.setImageResource(icon);
			/***
			 * second row
			 * 
			 ***/

			for (int j = 0; j < pProductArrayList.get(i).getmSubCategoryList()
					.size(); j++) {

				final int pos_j = j;

				LayoutInflater inflater2 = null;
				inflater2 = (LayoutInflater) getApplicationContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				final View mLinearView2 = inflater2.inflate(
						R.layout.row_second_category, null);
				ImageView imgArrDown = (ImageView) mLinearView2
						.findViewById(R.id.imageSecondArrow);
				final CheckBox checkBox1 = (CheckBox) mLinearView2
						.findViewById(R.id.checkBox1);
				String id = i + "-" + j + "-0";
				checkBox1.setTag(id);
				if (pProductArrayList.get(i).getmSubCategoryList().get(j)
						.getmItemListArray().size() == 0) {
					imgArrDown.setVisibility(View.INVISIBLE);
					checkBox1.setVisibility(View.VISIBLE);
				} else {

					checkBox1.setVisibility(View.INVISIBLE);
					imgArrDown.setVisibility(View.VISIBLE);
				}

				mLinearView2.setTag(id);

				TextView mSubItemName = (TextView) mLinearView2
						.findViewById(R.id.textViewTitle);
				final RelativeLayout mLinearSecondArrow = (RelativeLayout) mLinearView2
						.findViewById(R.id.linearSecond);
				checkBox1
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// TODO Auto-generated method stub
								if (last != null) {
									last.setChecked(false);
								}
								String abc[] = checkBox1.getTag().toString()
										.split("-");
								int ii, jj, kk;
								ii = Integer.valueOf(abc[0]);

								jj = Integer.valueOf(abc[1]);

								kk = Integer.valueOf(abc[2]);

								itemName1 = pProductArrayList.get(ii)
										.getmSubCategoryList().get(jj)
										.getpSubCatName();
								last = checkBox1;

							}
						});

				final LinearLayout mLinearScrollThird = (LinearLayout) mLinearView2
						.findViewById(R.id.linear_scroll_third1);

				if (isSecondViewClick == false) {
					mLinearScrollThird.setVisibility(View.GONE);
				} else {
					mLinearScrollThird.setVisibility(View.VISIBLE);
				}

				mLinearSecondArrow.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (isSecondViewClick == false) {
							isSecondViewClick = true;

							mLinearScrollThird.setVisibility(View.VISIBLE);

						} else {
							isSecondViewClick = false;
							mLinearScrollThird.setVisibility(View.GONE);
						}

						return false;
					}
				});

				final String catName = pProductArrayList.get(i)
						.getmSubCategoryList().get(j).getpSubCatName();
				mSubItemName.setText(catName);

				for (int k = 0; k < pProductArrayList.get(i)
						.getmSubCategoryList().get(j).getmItemListArray()
						.size(); k++) {
					final int pos_k = k;
					LayoutInflater inflater3 = null;
					inflater3 = (LayoutInflater) getApplicationContext()
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					View mLinearView3 = inflater3.inflate(
							R.layout.row_third_category, null);
					final CheckBox chk_ch = (CheckBox) mLinearView3
							.findViewById(R.id.checkBox1);
					String iid = i + "-" + j + "-" + k;
					chk_ch.setTag(iid);

					TextView mItemName = (TextView) mLinearView3
							.findViewById(R.id.textViewItemName);
					final String itemName = pProductArrayList.get(i)
							.getmSubCategoryList().get(j).getmItemListArray()
							.get(k).getItemName();
					final String itemPrice = pProductArrayList.get(i)
							.getmSubCategoryList().get(j).getmItemListArray()
							.get(k).getItemPrice();
					mItemName.setText(itemName);

					mLinearScrollThird.addView(mLinearView3);
					chk_ch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// TODO Auto-generated method stub
							if (last != null) {
								last.setChecked(false);

							}
							String abc[] = chk_ch.getTag().toString()
									.split("-");
							int ii, jj, kk;

							ii = Integer.valueOf(abc[0]);

							jj = Integer.valueOf(abc[1]);

							kk = Integer.valueOf(abc[2]);

							last = chk_ch;
							itemName1 = pProductArrayList.get(ii)
									.getmSubCategoryList().get(jj)
									.getmItemListArray().get(kk).getItemName();

						}
					});
					mLinearView2.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String gh = mLinearView2.getTag().toString();
							String chString[] = gh.split("-");
							int c1 = Integer.parseInt(chString[0]);
							int ca = Integer.parseInt(chString[1]);

							cat1 = pProductArrayList.get(c1)
									.getmSubCategoryList().get(ca)
									.getpSubCatName();
						}
					});
				}

				mLinearScrollSecond.addView(mLinearView2);

			}

			mLinearListView.addView(mLinearView, params);

		}
		scrollCategory.addView(mLinearListView);

	}

}
