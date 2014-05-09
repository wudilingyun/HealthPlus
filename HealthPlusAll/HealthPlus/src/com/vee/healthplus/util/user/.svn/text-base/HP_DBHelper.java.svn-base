package com.vee.healthplus.util.user;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	// 测试列表
		private final String CREATE_TABLE_USERTEST = "CREATE TABLE IF NOT EXISTS "
				+ HP_DBCommons.USERTEST_TABLENAME
				+ " ( _ID INTEGER PRIMARY KEY AUTOINCREMENT," + HP_DBCommons.USERID
				+ " INTEGER," + HP_DBCommons.TESTNAME + " VARCHAR, "
				+ HP_DBCommons.TESTRESULT + " VARCHAR, " + HP_DBCommons.TESTTIME
				+ " LONG)";

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
	}

	@Override
	public void onUpgrade(
			android.database.sqlite.SQLiteDatabase sqLiteDatabase, int i, int i2) {

	}
}
