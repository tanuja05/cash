package com.fw.Database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PurchaseAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_PRODUCT_ID = "product_id";
	public static final String KEY_IMAGENAME = "imagename";
	public static final String KEY_PURCHASE_STATUS = "purchase_status";
	public static final String KEY_POSTION="position";
	
	public ArrayList<HashMap<String, String>> datalist;
	public HashMap<String, String> mHashMap;

	private static final String DATABASE_TABLE = "purchase_table";
	public static String iproduct_id, iimagename, ipurchase_status,iposition;
	private Context context;
	private SQLiteDatabase database, db;
	private DBHelper dbHelper;

	public PurchaseAdapter(Context context) {
		this.context = context;
	}

	public PurchaseAdapter open() throws SQLException {
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		db = dbHelper.getReadableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	/**
	 * Create a new todo If the todo is successfully created return the new
	 * rowId for that note, otherwise return a -1 to indicate failure.
	 */
	public ArrayList<HashMap<String, String>> fetchStudents()
			throws SQLException {
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_PRODUCT_ID, KEY_IMAGENAME, KEY_PURCHASE_STATUS,KEY_POSTION }, null, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		datalist = new ArrayList<HashMap<String, String>>();
		mCursor.moveToFirst();
		
	


		while (mCursor.isAfterLast() == false) {
			mHashMap = new HashMap<String, String>();
			int id = mCursor.getInt(0);
			String product_idd = mCursor.getString(1);
			String imagenamee = mCursor.getString(2);
			String purchase_statuss = mCursor.getString(3);
			String positionn=mCursor.getString(4);
			mHashMap.put("product_id", product_idd);
			mHashMap.put("imagename", imagenamee);
			mHashMap.put("purchase_status", purchase_statuss);
			mHashMap.put("position", "" +positionn);
			datalist.add(mHashMap);

			mCursor.moveToNext();
		}
		mCursor.close();

		return datalist;
	}

	
	public ArrayList<HashMap<String, String>> searchPurchase(String position) throws SQLException {
	
		Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE
				+ " where " + KEY_PRODUCT_ID + " = " +"'"+ position+"'", null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		datalist = new ArrayList<HashMap<String, String>>();
		mCursor.moveToFirst();

		while (mCursor.isAfterLast() == false) {
			mHashMap = new HashMap<String, String>();
			int id = mCursor.getInt(0);
			String product_idd = mCursor.getString(1);
			String imagenamee = mCursor.getString(2);
			String purchase_statuss = mCursor.getString(3);
			String positionn=mCursor.getString(4);
			mHashMap.put("product_id", product_idd);
			mHashMap.put("imagename", imagenamee);
			mHashMap.put("purchase_status", purchase_statuss);
			mHashMap.put("position", "" +positionn);
			datalist.add(mHashMap);

			mCursor.moveToNext();
		}

		mCursor.close();

		return datalist;
	}

	public long CreateRecent() {
		// database.execSQL("delete from accounts");

		ContentValues initialValues = createContentValues();
		Log.d("CREATEEE", "" + "created");
		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	public boolean updateExpCategory(String rowId) {
		ContentValues updateValues = createContentValues();
		Log.e("CREATEEE", "" + "created");
		return database.update(DATABASE_TABLE, updateValues, KEY_PRODUCT_ID + "="
				+" '" + rowId + "'", null) > 0;
	}

	private ContentValues createContentValues() {
		ContentValues values = new ContentValues();

		values.put(KEY_PRODUCT_ID, this.iproduct_id);
		values.put(KEY_IMAGENAME, this.iimagename);
		values.put(KEY_PURCHASE_STATUS, this.ipurchase_status);
		values.put(KEY_POSTION, this.iposition);
	
		return values;
	}

}
