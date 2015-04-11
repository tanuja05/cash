package com.kfz.Rettungskarten.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME =  "CarSafetyPdff";
	private static final int DATABASE_VERSION =1;
	private static final String CREATE_PDFTABLE= "create table pdf_table(_id integer primary key autoincrement, car_name text, model_name text, sdcard_path text);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) 
	{
		// TODO Auto-generated method stub
		database.execSQL("DROP TABLE IF EXISTS pdf_table" );			
		database.execSQL(CREATE_PDFTABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		onCreate(db);
	}

}
