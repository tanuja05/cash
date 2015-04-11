package com.fw.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper  extends SQLiteOpenHelper{
	
	private static final String DATABASE_NAME =  "Shannon12";
	private static final int DATABASE_VERSION =2;
	private static final String CREATE_PURCHASETABLE= "create table purchase_table(_id integer primary key autoincrement, product_id text, imagename text, purchase_status text,position text);";

	public DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) 
	{
		// TODO Auto-generated method stub
		database.execSQL("DROP TABLE IF EXISTS purchase_table" );			
		database.execSQL(CREATE_PURCHASETABLE);
		
		database.execSQL("insert into purchase_table(product_id,imagename,purchase_status,position) values('com.srm.srmodel.shannon_001','screen13','locked','12')");
		database.execSQL("insert into purchase_table(product_id,imagename,purchase_status,position) values('com.srm.srmodel.shannon_002','screen14','locked','13')");
		database.execSQL("insert into purchase_table(product_id,imagename,purchase_status,position) values('com.srm.srmodel.shannon_003','screen15','locked','14')");
		database.execSQL("insert into purchase_table(product_id,imagename,purchase_status,position) values('com.srm.srmodel.shannon_004','screen16','locked','15')");
		database.execSQL("insert into purchase_table(product_id,imagename,purchase_status,position) values('com.srm.srmodel.shannon_005','screen17','locked','16')");
		database.execSQL("insert into purchase_table(product_id,imagename,purchase_status,position) values('com.srm.srmodel.shannon_007','screen18','locked','17')");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		onCreate(db);
		
		
		
		
		
	}

}
