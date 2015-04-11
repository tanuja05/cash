package com.fw.emars;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class NumberAdapter
{
	public static final String KEY_ROWID = "_id";
	public static final String KEY_FIRST = "number_first";
	public static final String KEY_SEC = "number_sec";	
	public static final String KEY_THIRD= "number_third";
	public static final String KEY_K_FACTOR= "K_Factor";
	public static  String ifirst,isec,ithird,ikFactor;
	public static int grp_id;
	private static final String DATABASE_TABLE = "number_TABLE";	
	private Context context;
	@SuppressWarnings("unused")
	private SQLiteDatabase database,db;
	private DBHelper dbHelper;
	public static int iID;

	 public NumberAdapter (Context context) 
	 {
		this.context = context;
	}

	public NumberAdapter  open() throws SQLException 
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

	public long createGroup() 
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
	 * 
	 * Deletes todo
	 */
	public boolean deleteExpenseCategory(long rowId) {
		return database.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	private ContentValues createContentValues()
	{
		ContentValues values = new ContentValues();		
		values.put(KEY_FIRST, NumberAdapter.ifirst);
		values.put(KEY_THIRD, NumberAdapter.ithird);			
		values.put(KEY_SEC, NumberAdapter.isec);
		values.put(KEY_K_FACTOR,NumberAdapter.ikFactor);			
		return values;
	}
		
	public ArrayList<HashMap<String, String>> fetchNumbers() throws SQLException 
	{
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] 
		{
			KEY_ROWID,KEY_FIRST,KEY_SEC,KEY_THIRD,KEY_K_FACTOR},
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
			   ifirst=mCursor.getString(1);
			   isec=mCursor.getString(2);
			   ithird=mCursor.getString(3);
			   ikFactor=mCursor.getString(4);
			   map.put("id", ""+iID);
			   map.put("first", ifirst);
			   map.put("sec", isec);
			   map.put("third", ithird);
			   map.put("K_factor",ikFactor);
			   expTypes.add(map);
			   mCursor.moveToNext();
		   }
		   mCursor.close();
		return expTypes;
	}
	
}
	
	
	
