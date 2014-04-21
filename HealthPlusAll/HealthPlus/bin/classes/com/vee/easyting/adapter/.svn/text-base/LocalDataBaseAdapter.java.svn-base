/**************************************************************

file name : 

date : 2011.11

**************************************************************/

package com.vee.easyting.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalDataBaseAdapter {
	private static final String tag = "DataBaseAdapter";
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_URL = "url";
	public static final String KEY_CHECKED = "checked";
	
	private static final String DB_TABLE = "LocalSongsList";

	private Context mContext = null;


	private static SQLiteDatabase mSQLiteDatabase = null;
	private static DatabaseHelper mDatabaseHelper = null;
	
	
	public LocalDataBaseAdapter(Context context)
	{
		mContext = context;
//		Log.i(tag, "MyDataBaseAdapter");
	}
	
	public LocalDataBaseAdapter()
	{
//		Log.i(tag, "MyDataBaseAdapter no input");
	}
	
	public void open() throws SQLException
	{
//		Log.i(tag, "MyDataBaseAdapter open()");
		if(mDatabaseHelper == null)
			mDatabaseHelper = new DatabaseHelper(mContext);
		if(mSQLiteDatabase == null)
			mSQLiteDatabase = mDatabaseHelper.getWritableDatabase();
	}
		
	public void close()
	{
//		Log.i(tag, "MyDataBaseAdapter close()");
		if(mDatabaseHelper != null)
		{
			mDatabaseHelper.close();
			mDatabaseHelper = null;
		}
		if(mSQLiteDatabase != null)
		{
			mSQLiteDatabase.close();
			mSQLiteDatabase = null;
		}
	}
		
	public long insertData(String name, String url, int isChecked)
	{
//		Log.i(tag, "MyDataBaseAdapter insertData()"+name);

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_URL, url);
		initialValues.put(KEY_CHECKED, isChecked);
		return mSQLiteDatabase.insert(DB_TABLE, KEY_ID, initialValues);
	}
	
	public boolean deleteData(long rowId)
	{
//		Log.i(tag, "MyDataBaseAdapter deleteData()");
		return mSQLiteDatabase.delete(DB_TABLE,KEY_ID + "=" +rowId, null) > 0;
	}
	
	public boolean deleteDatabyURL(String url)
	{
//		Log.i(tag, "MyDataBaseAdapter deleteData()"+url);

		return mSQLiteDatabase.delete(DB_TABLE,KEY_URL + "=\"" +url+"\"", null) > 0;
	}
	
	public Cursor fetchAllData()
	{
//		Log.i(tag, "MyDataBaseAdapter fetchAllData()");

		return mSQLiteDatabase.query(DB_TABLE, new String[]{ 
				KEY_ID, KEY_NAME, KEY_URL, KEY_CHECKED},
				null,null,null,null,null);
	}
	
	public Cursor fecthData(long rowId) throws SQLException
	{
//		Log.i(tag, "MyDataBaseAdapter fetchData()");
		Cursor mCursor = 
				mSQLiteDatabase.query(true, DB_TABLE, new String[]{
						KEY_ID, KEY_NAME, KEY_URL, KEY_CHECKED
				}, KEY_ID+"="+rowId, null, null, null,null,null);
		if(mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fecthDatabyURL(String url) throws SQLException
	{
//		Log.i(tag, "MyDataBaseAdapter fetchData()");
		Cursor mCursor = 
				mSQLiteDatabase.query(true, DB_TABLE, new String[]{
						KEY_ID, KEY_NAME, KEY_URL, KEY_CHECKED 
				}, KEY_URL+"=\""+url+"\"", null, null, null,null,null);
		if(mCursor != null)
		{
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public boolean updateData(String name, String url, int isChecked)
	{
//		Log.i(tag, "MyDataBaseAdapter updateData()");
		ContentValues args = new ContentValues();
		args.put(KEY_NAME, name);
		args.put(KEY_URL, url);
		args.put(KEY_CHECKED, isChecked);
		return mSQLiteDatabase.update(DB_TABLE, args, KEY_URL+"=\""+url+"\"", null) > 0;
	}
}
