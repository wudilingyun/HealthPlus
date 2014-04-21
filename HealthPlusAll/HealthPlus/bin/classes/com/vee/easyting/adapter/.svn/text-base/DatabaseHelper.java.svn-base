package com.vee.easyting.adapter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final String tag = "DataBaseAdapter";
	private static final String DB_NAME = "selectedlist.db";
	
	private static final String DB_TABLE_LOCAL = "LocalSongsList";
	private static final String DB_TABLE = "TopList";
	
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	
	public static final String KEY_DETIAL = "detial";
	public static final String KEY_URL = "url";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_CHECKED = "checked";
	
	private static final int DB_VERSION = 1;
	private Context mContext = null;
	private static final String DB_CREATE = "CREATE TABLE "
			+ DB_TABLE + " ("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_NAME + " TEXT,"
			+ KEY_URL + " TEXT,"
			+ KEY_IMAGE + " TEXT,"
			+ KEY_DETIAL + " TEXT)";
	
	private static final String DB_CREATE_LOCAL = "CREATE TABLE "
			+ DB_TABLE_LOCAL + " ("
			+ KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_NAME + " TEXT,"
			+ KEY_URL + " TEXT,"
			+ KEY_CHECKED + " INTEGER)";
	
	DatabaseHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
//		Log.i(tag, "DatabaseHelper");
	}
	
	public void onCreate(SQLiteDatabase db)
	{
//		Log.i(tag, "Before DB_CREATE");
		db.execSQL(DB_CREATE);
		db.execSQL(DB_CREATE_LOCAL);
//		Log.i(tag, "DB_CREATE");
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS "+DB_TABLE);
//		Log.i(tag, "onUpgrade");
		onCreate(db);
	}
}
