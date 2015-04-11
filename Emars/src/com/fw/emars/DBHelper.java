package com.fw.emars;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper 
{    private static final String DATABASE_NAME = "NumberList";

	private static final int DATABASE_VERSION =10;
	//	1
	private static final String CREATE_NUMBER_TABLE= "create table number_TABLE(_id integer primary key "
			+ "autoincrement, "
			+ "number_first text,"
			+ "number_sec text,"
			+ "K_Factor text,"
			+ "number_third text);";
	private static final String CREATE_MESSAGE_LOG= "create table message_log_table(_id integer primary key "
			+ "autoincrement, "
			+ "message text,"
			+ "date text,"
			+ "time text,"
			+ "number text);";
	private static final String CREATE_CURRENT_READING= "create table currentReading_table(_id integer primary key "
			+ "autoincrement, "
			+ "message_reading text,"
			+ "date_r text,"
			+ "time_r text,"
			+ "number_r text);";
	
		 
	public DBHelper(Context context) 
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override     
	public void onCreate(SQLiteDatabase database) {
			
			database.execSQL("DROP TABLE IF EXISTS number_TABLE" );			
			database.execSQL(CREATE_NUMBER_TABLE);
			database.execSQL("DROP TABLE IF EXISTS currentReading_table" );			
			database.execSQL(CREATE_CURRENT_READING);
			database.execSQL("DROP TABLE IF EXISTS message_log_table" );			
			database.execSQL(CREATE_MESSAGE_LOG);
		
			}

	// Method is called during an upgrade of the database, e.g. if you increase
	// the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion)
	{

		onCreate(database);
	}

}
