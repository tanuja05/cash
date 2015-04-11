package com.fw.emars;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class GetReadingAdapter
{
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_MESSAGE = "message_reading";
	public static final String KEY_DATE = "date_r";	
	public static final String KEY_TIME= "time_r";
	public static final String KEY_NUMBER= "number_R";
	public static  String imessg_r,idate_r,itime_r,inumber_r;
	public static int grp_id;
	private static final String DATABASE_TABLE = "currentReading_table";	
	private Context context;
	@SuppressWarnings("unused")
	private SQLiteDatabase database,db;
	private DBHelper dbHelper;
	public static int iID;

	 public GetReadingAdapter (Context context) 
	 {
		this.context = context;
	}

	public GetReadingAdapter  open() throws SQLException 
	{
		dbHelper = new DBHelper(context);
		database = dbHelper.getWritableDatabase();
		db=dbHelper.getReadableDatabase();
		return this;
	}

	public void close() 
	{
		dbHelper.close();
	}
	public long createRecords() 
	{
        
		ContentValues initialValues = createContentValues();
		
		return database.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Update the todo
	 */
	public boolean updateExpCategory(long rowId) 
	{
		ContentValues updateValues = createContentValues();		
		return database.update(DATABASE_TABLE, updateValues, KEY_ROWID + "="
				+ rowId, null) > 0;
	}

	/**
	 * Deletes todo
	 */
	public boolean deleteExpenseCategory(long rowId) {
		
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	private ContentValues createContentValues()
	{
		ContentValues values = new ContentValues();		
		values.put(KEY_MESSAGE, GetReadingAdapter.imessg_r);
		values.put(KEY_TIME, GetReadingAdapter.itime_r);			
		values.put(KEY_DATE, GetReadingAdapter.idate_r);
		values.put(KEY_NUMBER,GetReadingAdapter.inumber_r);			
		return values;
	}
		
	public ArrayList<HashMap<String, String>> fetchMessage() throws SQLException 
	{
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] 
		{
			KEY_ROWID,KEY_MESSAGE,KEY_NUMBER,KEY_DATE,KEY_TIME},
			null, null, null, null, null, null);
		if (mCursor != null) 
		{
			mCursor.moveToFirst();
		}
		ArrayList<HashMap<String, String>> expTypes=new ArrayList<HashMap<String,String>>();
		mCursor.moveToFirst();
	
		while(mCursor.isAfterLast() == false)
		   {
			    HashMap< String, String> map=new HashMap<String, String>();
			   iID=mCursor.getInt(0);
			   imessg_r=mCursor.getString(1);
			   inumber_r=mCursor.getString(2);
			   itime_r=mCursor.getString(3);
			   idate_r=mCursor.getString(4);
			   map.put("id", ""+iID);
			   map.put("date", idate_r);
			   map.put("time", itime_r);
			   map.put("message", imessg_r);
			   map.put("number",inumber_r);
			   expTypes.add(map);
			   mCursor.moveToNext();
		   }
		   mCursor.close();
		return expTypes;
	}
	
}
	
	
	
