package com.vee.healthplus.util.user;

import java.util.ArrayList;
import java.util.List;

import com.vee.healthplus.heahth_news_beans.NewsCollectinfor;
import com.vee.myhealth.bean.TestCollectinfor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by wangjiafeng on 13-10-23.
 */
public class HP_DBModel {

	private static HP_DBModel model;
	private static SQLiteDatabase database;

	public static HP_DBModel getInstance(Context mContext) {
		if (model == null) {
			model = new HP_DBModel();
		}
		database = new HP_DBHelper(mContext, HP_DBCommons.DBNAME, null, 1)
				.getWritableDatabase();
		return model;
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return database;
	}

	public void insertUserInfo(HP_User user, boolean used) {
		if (queryUserInfoByUserId(user.userId, false) != null) {
			updateUserInfo(user, false);
		} else {
			String insertUserInfo = "INSERT INTO "
					+ HP_DBCommons.USERINFO_TABLENAME + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.USERNAME + ","
					+ HP_DBCommons.USERNICK + "," + HP_DBCommons.EMAIL + ","
					+ HP_DBCommons.PHONE + "," + HP_DBCommons.REMARK + ","
					+ HP_DBCommons.USERAGE + "," + HP_DBCommons.USERHEIGHT
					+ "," + HP_DBCommons.USERWEIGHT + ","
					+ HP_DBCommons.USERSEX + "," + HP_DBCommons.UPDATETIME
					+ "," + HP_DBCommons.PHOTO + ") " + "VALUES ('"
					+ user.userId + "','" + user.userName + "','"
					+ user.userNick + "','" + user.email + "','" + user.phone
					+ "','" + user.remark + "'," + user.userAge + ","
					+ user.userHeight + "," + user.userWeight + ","
					+ user.userSex + "," + user.updateTime + ",'"
					+ user.photourl + "')";
			database.execSQL(insertUserInfo);
		}
		if (used)
			database.close();
	}

	public void updateUserInfo(HP_User user, boolean used) {
		ContentValues values = new ContentValues();
		if (user.userAge > 0)
			values.put(HP_DBCommons.USERAGE, user.userAge);
		if (user.userHeight > 0)
			values.put(HP_DBCommons.USERHEIGHT, user.userHeight);
		if (user.userWeight > 0)
			values.put(HP_DBCommons.USERWEIGHT, user.userWeight);
		if (user.userSex > -2)
			values.put(HP_DBCommons.USERSEX, user.userSex);
		values.put(HP_DBCommons.UPDATETIME, user.updateTime);
		values.put(HP_DBCommons.USERNICK, user.userNick);
		values.put(HP_DBCommons.EMAIL, user.email);
		values.put(HP_DBCommons.PHONE, user.phone);
		values.put(HP_DBCommons.PHOTO, user.photourl);
		database.update(HP_DBCommons.USERINFO_TABLENAME, values,
				HP_DBCommons.USERNAME + "=?", new String[] { user.userName });
		// insertUserWeightInfo(user, false);
		if (used)
			database.close();
	}

	public HP_User queryUserInfoByUserName(String name, boolean used) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ HP_DBCommons.USERINFO_TABLENAME + " WHERE "
				+ HP_DBCommons.USERNAME + " ='" + name + "'", null);
		HP_User user = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			int id = cursor.getInt(cursor.getColumnIndex(HP_DBCommons.USERID));
			int userAge = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERAGE));
			int userHeight = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERHEIGHT));
			float userWeight = cursor.getFloat(cursor
					.getColumnIndex(HP_DBCommons.USERWEIGHT));
			int userSex = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERSEX));
			user = new HP_User();
			user.userId = id;
			user.userName = name;
			user.userAge = userAge;
			user.userHeight = userHeight;
			user.userWeight = userWeight;
			user.userSex = userSex;
			cursor.close();
		}
		if (used)
			database.close();
		return user;
	}

	public HP_User queryUserInfoByUserId(int id, boolean used) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ HP_DBCommons.USERINFO_TABLENAME + " WHERE "
				+ HP_DBCommons.USERID + " = " + id, null);
		HP_User user = null;
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			String name = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.USERNAME));
			String nick = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.USERNICK));
			String email = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.EMAIL));
			String phone = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.PHONE));
			String remark = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.REMARK));
			int userAge = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERAGE));
			int userHeight = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERHEIGHT));
			float userWeight = cursor.getFloat(cursor
					.getColumnIndex(HP_DBCommons.USERWEIGHT));
			int userSex = cursor.getInt(cursor
					.getColumnIndex(HP_DBCommons.USERSEX));
			String photourl = cursor.getString(cursor
					.getColumnIndex(HP_DBCommons.PHOTO));
			user = new HP_User();
			user.userId = id;
			user.userName = name;
			user.userNick = nick;
			user.email = email;
			user.phone = phone;
			user.remark = remark;
			user.userAge = userAge;
			user.userHeight = userHeight;
			user.userWeight = userWeight;
			user.userSex = userSex;
			user.photourl = photourl;
			cursor.close();
		}
		if (used)
			database.close();
		return user;
	}

	public void insertUserWeightInfo(int userId, float weight, long date,
			boolean used) {
		try {
			String insertUserWeightInfo = "INSERT INTO "
					+ HP_DBCommons.USERWEIGHT_TABLENAME + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.USERWEIGHT + ","
					+ HP_DBCommons.UPDATETIME + ") VALUES (" + userId + ","
					+ weight + "," + date + ")";
			database.execSQL(insertUserWeightInfo);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (used)
				database.close();
		}
	}

	public interface UserWeightCallBack {
		public void queryUserWeightCallBack(float userWeight, long createTime);
	}

	public void queryUserWeightInfoByUserId(int id, boolean used,
			UserWeightCallBack callBack) {
		Cursor cursor = database.rawQuery("SELECT * FROM "
				+ HP_DBCommons.USERWEIGHT_TABLENAME + " WHERE "
				+ HP_DBCommons.USERID + " = " + id + "", null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				float userWeight = cursor.getFloat(cursor
						.getColumnIndex(HP_DBCommons.USERWEIGHT));
				long createTime = cursor.getLong(cursor
						.getColumnIndex(HP_DBCommons.UPDATETIME));
				callBack.queryUserWeightCallBack(userWeight, createTime);
			}
			cursor.close();
		}
		if (used)
			database.close();
	}

	/*
	 * 添加收藏
	 */
	public void insertUserCollect(int userId, String title, String imgurl,
			String weburl) {
		try {
			String insertUserCollect = "INSERT INTO "
					+ HP_DBCommons.USERCOLLECT_TABLENAME + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.TITLE + ","
					+ HP_DBCommons.IMGURL + "," + HP_DBCommons.WEBURL
					+ ") VALUES (" + userId + ",'" + title + "','" + imgurl
					+ "','" + weburl + "')";
			System.out.println("添加收藏=" + insertUserCollect);
			database.execSQL(insertUserCollect);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 取消收藏
	 */

	public void deletUserCollect(int userId, String title, String imgurl,
			String weburl) {
		try {
			String delUserinfor = "DELETE FROM "
					+ HP_DBCommons.USERCOLLECT_TABLENAME + " WHERE "
					+ HP_DBCommons.USERID + " =" + userId + " and "
					+ HP_DBCommons.TITLE + "='" + title + "' and "
					+ HP_DBCommons.IMGURL + "='" + imgurl + "'";
			System.out.println("删除语句" + delUserinfor);
			database.execSQL(delUserinfor);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 查看收藏状态
	 */

	public Boolean queryUserBooleanCollectInfor(int userId, String title,
			String imgurl) {
		String sql = "SELECT * FROM " + HP_DBCommons.USERCOLLECT_TABLENAME
				+ " WHERE " + HP_DBCommons.USERID + " =" + userId + " and "
				+ HP_DBCommons.TITLE + "='" + title + "'" + " and "
				+ HP_DBCommons.IMGURL + "='" + imgurl + "'";
		System.out.println("查询状态语句=" + sql);
		Cursor cursor = database.rawQuery(sql, null);

		if (cursor != null && cursor.getCount() > 0) {

			cursor.close();
			database.close();
			System.out.println("已经有了");
			return true;
		} else {
			cursor.close();
			database.close();
			return false;
		}

	}

	/*
	 * 获得收藏列表
	 */

	public List<NewsCollectinfor> queryUserCollectInfor(int userId) {
		List<NewsCollectinfor> newslist = new ArrayList<NewsCollectinfor>();
		String sql = "SELECT * FROM " + HP_DBCommons.USERCOLLECT_TABLENAME
				+ " WHERE " + HP_DBCommons.USERID + " =" + userId;
		System.out.println("获得收藏列表=" + sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				String title = cursor.getString(2);
				String imgurl = cursor.getString(3);
				String weburl = cursor.getString(4);
				NewsCollectinfor newsCollectinfor = new NewsCollectinfor();
				newsCollectinfor.setTitle(title);
				newsCollectinfor.setImgurl(imgurl);
				newsCollectinfor.setWeburl(weburl);
				newslist.add(newsCollectinfor);
			}
			cursor.close();
			database.close();
			return newslist;
		} else {
			cursor.close();
			database.close();
			return null;
		}

	}

	/*
	 * 获得测试列表
	 */
	public List<TestCollectinfor> queryUserTestInfor(int userId) {
		List<TestCollectinfor> testlist = new ArrayList<TestCollectinfor>();
		String sql = "SELECT * FROM " + HP_DBCommons.USERTEST_TABLENAME
				+ " WHERE " + HP_DBCommons.USERID + " =" + userId;
		System.out.println("获得收藏列表=" + sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				String name = cursor.getString(2);
				String result = cursor.getString(3);
				long time = cursor.getLong(4);
				TestCollectinfor testCollectinfor = new TestCollectinfor();
				testCollectinfor.setName(name);
				testCollectinfor.setResult(result);
				testCollectinfor.setCreattime(time);
				testlist.add(testCollectinfor);
			}
			cursor.close();
			database.close();
			return testlist;
		} else {
			cursor.close();
			database.close();
			return null;
		}

	}

	/*
	 * 添加测试列表
	 */

	public void insertUserTest(int userId, String name, String result, long time) {
		try {
			String insertUserCollect = "INSERT INTO "
					+ HP_DBCommons.USERTEST_TABLENAME + " ("
					+ HP_DBCommons.USERID + "," + HP_DBCommons.TESTNAME + ","
					+ HP_DBCommons.TESTRESULT + "," + HP_DBCommons.TESTTIME
					+ ") VALUES (" + userId + ",'" + name + "','" + result
					+ "','" + time + "')";
			System.out.println("添加测试=" + insertUserCollect);
			database.execSQL(insertUserCollect);
			System.out.println("成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			database.close();
		}
	}

	/*
	 * 查询是否已经测试过
	 */
}
