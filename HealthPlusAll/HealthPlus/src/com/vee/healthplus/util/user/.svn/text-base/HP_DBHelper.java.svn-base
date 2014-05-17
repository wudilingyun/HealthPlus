package com.vee.healthplus.util.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.SystemClock;

public class HP_DBHelper extends SQLiteOpenHelper {

	private final String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS "
			+ HP_DBCommons.USERINFO_TABLENAME
			+ " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT," + HP_DBCommons.USERID
			+ " INTEGER," + HP_DBCommons.USERNAME + " CHAR,"
			+ HP_DBCommons.USERNICK + " CHAR," + HP_DBCommons.EMAIL + " CHAR,"
			+ HP_DBCommons.PHONE + " CHAR," + HP_DBCommons.REMARK + " CHAR,"
			+ HP_DBCommons.USERAGE + " INTEGER," + HP_DBCommons.USERHEIGHT
			+ " FLOAT," + HP_DBCommons.USERWEIGHT + " FLOAT,"
			+ HP_DBCommons.USERSEX + " INTEGER," + HP_DBCommons.UPDATETIME
			+ " LONG," + HP_DBCommons.PHOTO + " CHAR)";
	private final String CREATE_TABLE_USERWEIGHT = "CREATE TABLE IF NOT EXISTS "
			+ HP_DBCommons.USERWEIGHT_TABLENAME
			+ " (_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ HP_DBCommons.USERID
			+ " INTEGER,"
			+ HP_DBCommons.USERWEIGHT
			+ " FLOAT,"
			+ HP_DBCommons.UPDATETIME + " LONG)";
	private final String CREATE_TABLE_USERCOLLECT = "CREATE TABLE IF NOT EXISTS "
			+ HP_DBCommons.USERCOLLECT_TABLENAME
			+ " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ HP_DBCommons.USERID
			+ " INTEGER,"
			+ HP_DBCommons.TITLE
			+ " VARCHAR, "
			+ HP_DBCommons.IMGURL
			+ " VARCHAR, " + HP_DBCommons.WEBURL + " VARCHAR)";
	// 测试列表--可重复
		private final String CREATE_TABLE_USERTEST = "CREATE TABLE IF NOT EXISTS "
				+ HP_DBCommons.USERTEST_TABLENAME
				+ " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT," + HP_DBCommons.USERID
				+ " INTEGER," + HP_DBCommons.TESTNAME + " VARCHAR, "
				+ HP_DBCommons.TESTRESULT + " VARCHAR, " + HP_DBCommons.TESTTIME
				+ " LONG)";

		//测试列表---覆盖的
		private final String CREATE_TABLE_USERTEST_COVER = "CREATE TABLE IF NOT EXISTS "
				+ HP_DBCommons.USERTEST_TABLENAME_COVER
				+ " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT," + HP_DBCommons.USERID
				+ " INTEGER," + HP_DBCommons.TESTNAME + " VARCHAR, "
				+ HP_DBCommons.TESTRESULT + " VARCHAR, " + HP_DBCommons.TESTTIME
				+ " LONG)";
		
		private final String CREATE_TABLE_JPUSH = "CREATE TABLE IF NOT EXISTS "
				+ HP_DBCommons.JPUSH_TABLENAME
				+ " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT," + HP_DBCommons.USERID
				+ " INTEGER," + HP_DBCommons.JPUSHTITLE + " VARCHAR, "
				+ HP_DBCommons.JPUSHCONTENT + " VARCHAR, " + HP_DBCommons.JPUSHIMG
				+ " VARCHAR, "+HP_DBCommons.JPUSHTIME+ " LONG, "+ HP_DBCommons.JPUSHREADFLAG
				+ " INTEGER)";
		
		private final String INSERT_ONE_TO_TABLE_JPUSH = "INSERT INTO " + HP_DBCommons.JPUSH_TABLENAME
				+ " (" + HP_DBCommons.USERID + ","
				+ HP_DBCommons.JPUSHTITLE + "," + HP_DBCommons.JPUSHCONTENT
				+ "," + HP_DBCommons.JPUSHIMG + ","
				+ HP_DBCommons.JPUSHTIME + "," + HP_DBCommons.JPUSHREADFLAG
				+ ") VALUES (" + 0 + ",'" + "test Title" + "','" + "test Content abcdefghijklmnopqrstuvwxyz!"
				+ "','" + "test http://www.head.jpg" + "','" + System.currentTimeMillis()  + "'," + 1 + ")";
		
	public HP_DBHelper(Context context, String name,
			SQLiteDatabase.CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(android.database.sqlite.SQLiteDatabase sqLiteDatabase) {
		sqLiteDatabase.execSQL(CREATE_TABLE_USER);
		sqLiteDatabase.execSQL(CREATE_TABLE_USERWEIGHT);
		sqLiteDatabase.execSQL(CREATE_TABLE_USERCOLLECT);
		sqLiteDatabase.execSQL(CREATE_TABLE_USERTEST);
		sqLiteDatabase.execSQL(CREATE_TABLE_USERTEST_COVER);
		sqLiteDatabase.execSQL(CREATE_TABLE_JPUSH);
		sqLiteDatabase.execSQL(INSERT_ONE_TO_TABLE_JPUSH);
		
	}

	@Override
	public void onUpgrade(
			android.database.sqlite.SQLiteDatabase sqLiteDatabase, int i, int i2) {

	}
}
