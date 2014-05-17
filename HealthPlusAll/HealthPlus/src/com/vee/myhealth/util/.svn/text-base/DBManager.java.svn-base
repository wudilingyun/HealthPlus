package com.vee.myhealth.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DBManager {
	private final int BUFFER_SIZE = 400000;
	public static final String DB_NAME = "habitus.db"; // 保存的数据库文件名
	public static final String PACKAGE_NAME = "com.vee.healthplus";
	public static final String DB_PATH = "/data"
			
			+ Environment.getDataDirectory().getAbsolutePath() + "/"
			+ PACKAGE_NAME; // 在手机里存放数据库的位置
	public static final String  SYMPTOM_BDA_TABLE = "BDA";
	public static final String  SYMPTOM_BDC_TABLE = "BDC";
	public static final String  SYMPTOM_ILL_TABLE = "ILL";
	public static final String  SYMPTOM_SYM_TABLE = "SYM";
	//数据库表名字
	public static final String  HABITUS_TEST_TABLE = "test";
	public static final String  HABITUS_RESULT_TABLE = "result";
	public static final String  HEALTH_QUESTIONS = "health_questions";
	public static final String  HEALTH_RESULT = "health_result";
	public static final String  HEALTH_SUGGEST = "health_suggest";
	
	private SQLiteDatabase database;
	private Context context;

	public DBManager(Context context) {
		this.context = context;
	}

	public void openDatabase() {
		this.database = this.openDatabase(DB_PATH + "/" + DB_NAME);
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			if (!(new File(dbfile).exists())) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
				//Toast.makeText(context, "准备导入", Toast.LENGTH_SHORT).show();
				InputStream is = context.getResources().getAssets()
						.open(DB_NAME); // 欲导入的数据库
				FileOutputStream fos = new FileOutputStream(dbfile);
				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				fos.close();
				is.close();
			}
			SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
					null);
			return db;
		} catch (FileNotFoundException e) {
			Log.e("Database", "File not found");
			e.printStackTrace();
		} catch (IOException e) {
			Log.e("Database", "IO exception");
			e.printStackTrace();
		}
		return null;
	}
	
	   public void closeDatabase() {
	        this.database.close();
	    }
}
