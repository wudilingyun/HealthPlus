package com.vee.healthplus.util.sporttrack;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class CalorieDBExternal {
    private static CalorieDBExternal mInstance = null;

    private SQLiteDatabase database;
    private Context context;
    private String dbPath;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1)
                database = SQLiteDatabase.openOrCreateDatabase(dbPath, null);
        }
    };

    public CalorieDBExternal(Context context) {
        this.context = context;
        dbPath = context.getFilesDir().getAbsolutePath()
                + File.separator + HealthDBConst.CALORY_DB;
        this.database = this.openDatabase(dbPath);
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        if (!(new File(dbPath).exists()))
            copyDB();
        else return SQLiteDatabase.openOrCreateDatabase(dbfile, null);
        return null;
    }

    private void copyDB() {
        new Thread() {
            public void run() {
                try {
                    HealthDBConst.copyDatabase(context, dbPath, handler);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    public void closeDatabase() {
        this.database.close();
    }

    public static CalorieDBExternal getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new CalorieDBExternal(context);
        }
        return mInstance;
    }

    private SQLiteDatabase checkOpen(boolean needWrite) throws SQLiteException {
        return database;
    }

    private void execSql(String sql, String[] params, boolean needWrite) {
        try {
            checkOpen(needWrite).execSQL(sql, params);
        } catch (Exception e) {
            Log.v("sql------", e.toString());
        }
    }


    public String getCalory(int sportid) {
        String strDanger = "";
        try {
            String sql = "select * from " + HealthDBConst.CALORY_TABLE
                    + " where sportid=" + sportid;
            Cursor cursor = checkOpen(false).rawQuery(sql, null);
            if (cursor.getCount() < 1) {
                cursor.close();
            } else {
                cursor.moveToFirst();
                strDanger = cursor.getString(cursor
                        .getColumnIndex("value"));
                cursor.close();
            }
            cursor.close();
        } catch (Exception e) {
            e.toString();
        }
        return strDanger;
    }


    public void deleteAll(String tablename) {
        try {
            String sql = "delete from " + tablename;
            checkOpen(true).execSQL(sql);
        } catch (Exception e) {
        }
    }

//    @Override
//    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
//        createTables(sqLiteDatabase);
//    }
}
