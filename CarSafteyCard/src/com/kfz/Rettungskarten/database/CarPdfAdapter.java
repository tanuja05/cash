package com.kfz.Rettungskarten.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CarPdfAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_CAR = "car_name";
	public static final String KEY_MODEL = "model_name";
	public static final String KEY_PATH = "sdcard_path";

	public ArrayList<HashMap<String, String>> datalist;
	public HashMap<String, String> mHashMap;

	private static final String DATABASE_TABLE = "pdf_table";
	public static String icarname, imodelname, isdcardpath;
	private Context context;
	private SQLiteDatabase database, db;
	private DBHelper dbHelper;

	public CarPdfAdapter(Context context) {
		this.context = context;
	}

	public CarPdfAdapter open() throws SQLException 
	{
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
				KEY_ROWID, KEY_CAR, KEY_MODEL, KEY_PATH }, null, null, null,
				null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		datalist = new ArrayList<HashMap<String, String>>();
		mCursor.moveToFirst();

		while (mCursor.isAfterLast() == false) 
		{
			mHashMap = new HashMap<String, String>();
			int id = mCursor.getInt(0);
			String car_name = mCursor.getString(1);
			String model_name = mCursor.getString(2);
			String sdcard_path = mCursor.getString(3);
			mHashMap.put("car_name", car_name);
			mHashMap.put("model_name", model_name);
			mHashMap.put("sdcard_path", sdcard_path);
			mHashMap.put("_id", "" + id);
			datalist.add(mHashMap);

			mCursor.moveToNext();
		}
		mCursor.close();

		return datalist;
	}

	public boolean deleteCarSafety(String path) {
		return database.delete(DATABASE_TABLE, KEY_PATH + "=" + path, null) > 0;
	}

	public ArrayList<HashMap<String, String>> searchCards(String car,
			String model) throws SQLException {
	
		Cursor mCursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE
				+ " where " + KEY_CAR + " ='" + car + "'" + " and " + KEY_MODEL
				+ " = '" + model + "'", null);
		
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		datalist = new ArrayList<HashMap<String, String>>();
		mCursor.moveToFirst();

		while (mCursor.isAfterLast() == false) {
			mHashMap = new HashMap<String, String>();
			int id = mCursor.getInt(0);
			String car_name = mCursor.getString(1);
			String model_name = mCursor.getString(2);
			String sdcard_path = mCursor.getString(3);
			mHashMap.put("car_name", car_name);
			mHashMap.put("model_name", model_name);
			mHashMap.put("sdcard_path", sdcard_path);
			mHashMap.put("_id", "" + id);
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

	public boolean updateExpCategory(long rowId) {
		ContentValues updateValues = createContentValues();
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	private ContentValues createContentValues() {
		ContentValues values = new ContentValues();

		values.put(KEY_CAR, this.icarname);
		values.put(KEY_MODEL, this.imodelname);
		values.put(KEY_PATH, this.isdcardpath);
	
		return values;
	}

}
