package com.vee.healthplus.util.sporttrack;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vee.healthplus.load.SportLEntity;
import com.vee.healthplus.load.TrackLEntity;
import com.vee.healthplus.ui.heahth_heart.DataForSQL;
import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.analysis.AnalysisConst;
import com.vee.healthplus.util.analysis.AnalysisUtil;
import com.vee.healthplus.util.analysis.HistoryModel;
import com.vee.healthplus.util.user.HP_User;

public class HealthDB extends SQLiteOpenHelper {
	private static HealthDB mInstance = null;

	protected static final String SQL_Create_Table_SPORTTYPE = "CREATE TABLE "
			+ HealthDBConst.SPORTTYPE_TABLE + HealthDBConst.SPORTTYPE_ITEM;
	protected static final String SQL_Create_Table_SPORTRECORD_INDEX = "CREATE TABLE "
			+ HealthDBConst.SPORTRECORD_INDEX_TABLE
			+ HealthDBConst.SPORTRECORD_INDEX_ITEM;
	protected static final String SQL_Create_Table_SPORTRECORD_GPS = "CREATE TABLE "
			+ HealthDBConst.SPORTRECORD_TABLE + HealthDBConst.SPORTRECORD_ITEM;
	protected static final String SQL_Create_Table_SPORTRECORD_PEDO = "CREATE TABLE "
			+ HealthDBConst.SPORTRECORD__PEDO_TABLE
			+ HealthDBConst.SPORTRECORD_ITEM;
	/*
	 * 添加心率结果表
	 */

	protected static final String SQL_Create_Table_SPORTRECORD_HeartRate = "CREATE TABLE "
			+ HealthDBConst.HealthHeart_TABLE
			+ HealthDBConst.HealthHeart_INDEX_ITEM;

	private Context context;

	public HealthDB(Context context) {
		super(context, HealthDBConst.HEALTHDB_NAME, null,
				HealthDBConst.HEALTHDB_VERSION);
		this.context = context;
	}

	public static HealthDB getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new HealthDB(context);
		}
		return mInstance;
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		createTables(sqLiteDatabase);
	}

	private void createTables(SQLiteDatabase db) {
		db.execSQL(SQL_Create_Table_SPORTTYPE);
		db.execSQL(SQL_Create_Table_SPORTRECORD_GPS);
		db.execSQL(SQL_Create_Table_SPORTRECORD_PEDO);
		db.execSQL(SQL_Create_Table_SPORTRECORD_INDEX);
		db.execSQL(SQL_Create_Table_SPORTRECORD_HeartRate);
	}

	private SQLiteDatabase checkOpen(boolean needWrite) throws SQLiteException {
		SQLiteDatabase db = null;
		if (needWrite)
			db = getWritableDatabase();
		
		else
			db = getReadableDatabase();
		if (db == null)
			throw new SQLiteException("database not opened!");
		return db;
	}

	private void execSql(String sql, Object[] params, boolean needWrite) {
		try {
			checkOpen(needWrite).execSQL(sql, params);
		} catch (Exception e) {
			Log.v("sql------", e.toString());
		}
	}

	/*
	 * 添加心率数据和查询心率数据
	 */
	public void addHeart(DataForSQL data) {
		int userid = HP_User.getOnLineUserId(context);
		if (data != null) {
			String sql = "insert into " + HealthDBConst.HealthHeart_TABLE
					+ "(heartrate,time,userid)" + "values(?,?,?)";
			Object[] params = new Object[3];
			params[0] = data.getHeartdata();
			params[1] = data.getCurrdate();
			params[2] = userid;
			execSql(sql, params, true);
		}

	}

	public Boolean queryDate(long date) {
		long starttime = AnalysisUtil.getDayBeginTime();
		long endtime = starttime + AnalysisConst.DAY_MS;
		String userid = String.valueOf(HP_User.getOnLineUserId(context));
		try {
			String sql = "select * from " + HealthDBConst.HealthHeart_TABLE
					+ " where time>" + starttime + " and time<" + endtime
					+ " and userid=" + userid;
			System.out.println("sql" + sql);
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return false;
			} else {
				cursor.close();
				return true;

			}
		} catch (Exception e) {
			return null;
		}

	}

	public void updateHeart(DataForSQL data) {
		long starttime = AnalysisUtil.getDayBeginTime();
		long endtime = starttime + AnalysisConst.DAY_MS;
		int userid = HP_User.getOnLineUserId(context);
		if (data != null) {
			// String sql = "update " + HealthDBConst.HealthHeart_TABLE +
			// " set "
			// + "heartrate=?" + " , time=? where userid=?";
			String sql = "update " + HealthDBConst.HealthHeart_TABLE + " set "
					+ "heartrate=?" + " , time=?" + " , userid=?"
					+ " where time>" + starttime + " and time<" + endtime;
			Object[] params = new Object[3];
			params[0] = data.getHeartdata();
			System.out.println("dfd" + data.getCurrdate());
			params[1] = data.getCurrdate();
			// params[2] = userid;
			params[2] = String.valueOf(userid);
			execSql(sql, params, true);
		}

	}

	public String getHeart(long date) {
		try {
			String sql = "select * from " + HealthDBConst.HealthHeart_TABLE
					+ " where time=" + date;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				String name = cursor.getString(0);
				cursor.close();
				return name;

			}
		} catch (Exception e) {
			return null;
		}
	}

	public HashMap<String, String> getHeartbyDay(long dayBegintTime, int userid) {
		long dayEndTime = dayBegintTime + AnalysisConst.DAY_MS;
		HashMap<String, String> day = new HashMap<String, String>();
		day.put("time", null);
		day.put("value", null);
		try {
			String sql = "select * from " + HealthDBConst.HealthHeart_TABLE
					+ " where userid = " + userid + " and time > "
					+ dayBegintTime + " and time < " + dayEndTime;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				String value = cursor.getString(0);
				String time = cursor.getString(1);
				if (value != null && value != "0") {
					day.put("value", value);
				} else {
					day.put("value", "0");
				}
				if (time != null && time != "0")
					day.put("time", time);

			}
		} catch (Exception e) {
			return null;
		}
		return day;
	}

	// the first valuable heartrate record time
	public String getFirstValuableHeartRecordTime(int userid) {
		try {
			String sql = "select * from " + HealthDBConst.HealthHeart_TABLE
					+ " where userid=" + userid + " order by time asc";
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					if (Integer.parseInt(cursor.getString(0)) != 0) {
						String time = cursor.getString(1);
						cursor.close();
						return time;
					}
				}
				cursor.close();
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public void addSport(SportTypeEntity entity) {

		if (getSportName(entity.getId()) == null) {
			String sql = "insert into " + HealthDBConst.SPORTTYPE_TABLE
					+ "(id,name) " + "values(?,?)";
			Object[] params = new Object[2];
			params[0] = entity.getId();
			params[1] = entity.getName();
			execSql(sql, params, true);
		}
	}

	public String getSportName(int sportid) {
		try {
			String sql = "select * from " + HealthDBConst.SPORTTYPE_TABLE
					+ " where id=" + sportid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				String name = cursor.getString(1);
				cursor.close();
				return name;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public List<SportTypeEntity> getSportTypes() {
		try {
			String sql = "select * from " + HealthDBConst.SPORTTYPE_TABLE;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				int length = cursor.getCount();
				List<SportTypeEntity> typeList = new ArrayList<SportTypeEntity>();
				for (int i = 0; i < length; i++) {
					if (cursor.isAfterLast())
						break;
					SportTypeEntity se = new SportTypeEntity();
					se.setId(cursor.getInt(0));
					se.setName(cursor.getString(1));
					typeList.add(se);
					cursor.moveToNext();
				}
				cursor.close();
				return typeList;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public void addSportRecordIndex(SportLEntity entity, int userid) {
		String sql = "insert into " + HealthDBConst.SPORTRECORD_INDEX_TABLE
				+ "(id,sportid,time,modeid,userid,sync) "
				+ "values(?,?,?,?,?,?)";
		Object[] params = new Object[6];
		params[0] = entity.getId();
		params[1] = entity.getSportid();
		params[2] = String.valueOf((new Date()).getTime());
		params[3] = entity.getMode();
		params[4] = userid;
		params[5] = HPConst.SPORT_INDEX_SYNC_YES;
		execSql(sql, params, true);
	}

	public void addSportRecordIndex(SportEntity entity) {
		String sql = "insert into " + HealthDBConst.SPORTRECORD_INDEX_TABLE
				+ "(id,sportid,time,modeid,userid,sync) "
				+ "values(?,?,?,?,?,?)";
		Object[] params = new Object[6];
		params[0] = entity.getId();
		params[1] = entity.getSportid();
		params[2] = String.valueOf((new Date()).getTime());
		params[3] = entity.getMode();
		params[4] = entity.getUserId();
		params[5] = entity.getSync();
		execSql(sql, params, true);
	}

	public void addSportRecord(TrackLEntity entity, String tablename, int id,
			int userid) {
		String sql = "insert into " + tablename
				+ "(id,duration,distance,calory,velocity,lon,lat,userid) "
				+ "values(?,?,?,?,?,?,?,?)";
		Object[] params = new Object[8];
		params[0] = id;
		params[1] = entity.getDuration();
		params[2] = entity.getDistance();
		params[3] = entity.getCalory();
		params[4] = entity.getVelocity();
		params[5] = entity.getLongitude();
		params[6] = entity.getLatitude();
		params[7] = userid;
		execSql(sql, params, true);
	}

	public void addSportRecord(TrackEntity entity, String tablename) {
		String sql = "insert into " + tablename
				+ "(id,duration,distance,calory,velocity,lon,lat,userid) "
				+ "values(?,?,?,?,?,?,?,?)";
		Object[] params = new Object[8];
		params[0] = entity.getId();
		params[1] = entity.getDuration();
		params[2] = entity.getDistance();
		params[3] = entity.getCalory();
		params[4] = entity.getVelocity();
		params[5] = entity.getLongitude();
		params[6] = entity.getLatitude();
		params[7] = entity.getUserId();
		execSql(sql, params, true);
	}

	public int getSportIndexLastestId(int userid) { // the lastest
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return 0;
			} else {
				cursor.moveToLast();
				int id = cursor.getInt(0);
				cursor.close();
				return id;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	public int getSportIndexCount(int userid) { // the lastest,same as
												// getSportIndexLastestId
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return 0;
			} else {
				int count = cursor.getCount();
				;
				cursor.close();
				return count;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	public String getSportIndexLastestTime(int userid) { // the lastest
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return "";
			} else {
				cursor.moveToLast();
				String time = cursor.getString(2);
				cursor.close();
				return time;
			}
		} catch (Exception e) {
			return "";
		}
	}

	public String getSportIndexFirstTime(int userid) { // the lastest
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return "";
			} else {
				cursor.moveToFirst();
				String time = cursor.getString(2);
				cursor.close();
				return time;
			}
		} catch (Exception e) {
			return "";
		}
	}

	public int getSportIndexLastestSportId(int userid) { // the lastest
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return 0;
			} else {
				cursor.moveToLast();
				int id = cursor.getInt(1);
				cursor.close();
				return id;
			}
		} catch (Exception e) {
			return 0;
		}
	}

	public SportEntity getSportIndexLastest(int userid) { // the lastest
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToLast();
				SportEntity se = getSportEntity(cursor);
				cursor.close();
				return se;

			}
		} catch (Exception e) {
			return null;
		}
	}

	public SportEntity getSportIndexById(int id, int userid) {
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where id=" + id
					+ "and userid=" + userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				SportEntity se = getSportEntity(cursor);
				cursor.close();
				return se;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public List<SportEntity> getSportIndexAll(int userid) {
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				int length = cursor.getCount();
				List<SportEntity> sportList = new ArrayList<SportEntity>();
				for (int i = 0; i < length; i++) {
					if (cursor.isAfterLast())
						break;
					SportEntity se = getSportEntity(cursor);
					sportList.add(se);
					cursor.moveToNext();
				}
				cursor.close();
				return sportList;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public List<SportEntity> getSportIndexByDate(long begintime, long endtime,
			int userid) {
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid + " and time > " + begintime + " and time < "
					+ endtime;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				int length = cursor.getCount();
				List<SportEntity> sportList = new ArrayList<SportEntity>();
				for (int i = 0; i < length; i++) {
					SportEntity se = getSportEntity(cursor);
					sportList.add(se);
					cursor.moveToNext();
				}
				cursor.close();
				return sportList;
			}
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * public List<SportEntity> getSportIndexByDate(long begintime, long
	 * endtime, int userid) { try { String sql = "select * from " +
	 * HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid=" + userid ;
	 * Cursor cursor = checkOpen(false).rawQuery(sql, null); if
	 * (cursor.getCount() < 1) { cursor.close(); return null; } else {
	 * cursor.moveToFirst(); int length = cursor.getCount(); List<SportEntity>
	 * sportList = new ArrayList<SportEntity>(); for (int i = 0; i < length;
	 * i++) { if (cursor.isAfterLast()) break; long time =
	 * Long.parseLong(cursor.getString(2)); if (time < begintime) continue; if
	 * (time > begintime && time < endtime) { SportEntity se =
	 * getSportEntity(cursor); sportList.add(se); } if (time > endtime) break;
	 * cursor.moveToNext(); } cursor.close(); return sportList; } } catch
	 * (Exception e) { return null; } }
	 */

	public List<SportEntity> getSportIndexByDateExceptLast(long begintime,
			int userid) {
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				int length = cursor.getCount();
				List<SportEntity> sportList = new ArrayList<SportEntity>();
				for (int i = 0; i < length - 1; i++) {
					if (cursor.isAfterLast())
						break;
					long time = Long.parseLong(cursor.getString(2));
					// if (time < begintime)
					// continue; // no movetonext()
					if (time > begintime) {
						SportEntity se = getSportEntity(cursor);
						sportList.add(se);
					}
					cursor.moveToNext();
				}
				cursor.close();
				return sportList;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public void updateSportIndexLastestTime(int userid, long time) {
		SportEntity entity = getSportIndexLastest(userid);
		if (entity != null) {
			String sql = "update " + HealthDBConst.SPORTRECORD_INDEX_TABLE
					+ " set time=? where id=? and userid=?";
			String[] params = new String[3];
			params[0] = String.valueOf(time);
			params[1] = String.valueOf(entity.getId());
			params[2] = String.valueOf(userid);
			execSql(sql, params, true);
		}
	}

	public void updateSportIndexSyncById(int userid, int id) {
		String sql = "update " + HealthDBConst.SPORTRECORD_INDEX_TABLE
				+ " set sync=" + HPConst.SPORT_INDEX_SYNC_YES + " where id="
				+ id + " and userid=" + userid;
		execSql(sql, null, true);
	}

	public void updateAllSportIndexSync(int userid) {
		String sql = "update " + HealthDBConst.SPORTRECORD_INDEX_TABLE
				+ " set sync=" + HPConst.SPORT_INDEX_SYNC_YES + " where sync="
				+ HPConst.SPORT_INDEX_SYNC_NOT + " and userid=" + userid;
		execSql(sql, null, true);
	}

	public void updateSportIndexId(int userid, int newid, int oldid) {
		String sql = "update " + HealthDBConst.SPORTRECORD_INDEX_TABLE
				+ " set id=? where id=? and userid=?";
		String[] params = new String[3];
		params[0] = String.valueOf(newid);
		params[1] = String.valueOf(oldid);
		params[2] = String.valueOf(userid);
		execSql(sql, params, true);
	}

	public void updateSportRecordId(int userid, int newid, int oldid) {
		String sql = "update " + HealthDBConst.SPORTRECORD_TABLE
				+ " set id=? where id=? and userid=?";
		String[] params = new String[3];
		params[0] = String.valueOf(newid);
		params[1] = String.valueOf(oldid);
		params[2] = String.valueOf(userid);
		execSql(sql, params, true);

	}

	public List<SportLEntity> getSportIndexNeedUpload(int userid, int lastestId) {
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid + " and sync=" + HPConst.SPORT_INDEX_SYNC_NOT
					+ " order by id";
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				int length = cursor.getCount();
				List<SportLEntity> sportList = new ArrayList<SportLEntity>();
				for (int i = 0; i < length; i++) {
					if (cursor.isAfterLast())
						break;

					if (cursor.getInt(0) != lastestId + 1 + i) {
						updateSportIndexId(userid, lastestId + 1 + i,
								cursor.getInt(0));
						updateSportRecordId(userid, lastestId + 1 + i,
								cursor.getInt(0));
					}

					//
					SportLEntity se = new SportLEntity();
					se.setId(lastestId + 1 + i); // cursor.getInt(0)
					se.setSportid(cursor.getInt(1));
					se.setTime(cursor.getString(2));
					se.setMode(cursor.getInt(3));
					se.setSensorId(cursor.getInt(4));
					sportList.add(se);

					cursor.moveToNext();
				}
				cursor.close();
				return sportList;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public List<TrackEntity> getRecordById(int id, int userid, String tablename) {
		try {
			String sql = "select * from " + tablename + " where id=" + id
					+ " and userid=" + userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				int length = cursor.getCount();
				List<TrackEntity> trackList = new ArrayList<TrackEntity>();
				for (int i = 0; i < length; i++) {
					if (cursor.isAfterLast())
						break;
					TrackEntity te = getTrackEntity(cursor);
					trackList.add(te);
					cursor.moveToNext();
				}
				cursor.close();
				return trackList;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public List<TrackLEntity> getRecordByIdforLoad(int id, int userid,
			String tablename) {
		try {
			String sql = "select * from " + tablename + " where id=" + id
					+ " and userid=" + userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				int length = cursor.getCount();
				List<TrackLEntity> trackList = new ArrayList<TrackLEntity>();
				for (int i = 0; i < length; i++) {
					if (cursor.isAfterLast())
						break;
					TrackLEntity te = new TrackLEntity();
					te.setDuration(cursor.getString(1));
					te.setDistance(cursor.getString(2));
					te.setCalory(cursor.getString(3));
					te.setVelocity(cursor.getString(4));
					te.setLongitude(cursor.getString(5));
					te.setLatitude(cursor.getString(6));
					trackList.add(te);
					cursor.moveToNext();
				}
				cursor.close();
				return trackList;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public TrackEntity getSportRecordByIdLastest(int id, int userid,
			String tablename) {
		try {
			String sql = "select * from " + tablename + " where id=" + id
					+ " and userid=" + userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToLast();
				TrackEntity te = getTrackEntity(cursor);
				cursor.close();
				return te;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public HashMap<String, String> getSportSumbyIndex(
			List<SportEntity> sportList, String tablename) {
		HashMap<String, String> sportSum = new HashMap<String, String>();
		sportSum.put("count", String.valueOf(0));
		sportSum.put("duration", String.valueOf(0));
		sportSum.put("calory", String.valueOf(0));
		sportSum.put("distance", String.valueOf(0));
		sportSum.put("velcotiy", String.valueOf(0));

		if (sportList == null)
			return sportSum;
		int count = sportList.size();
		long duration = 0;
		long calory = 0;
		double distance = 0;
		double velcotiy = 0;
		for (int i = 0; i < count; i++) {
			TrackEntity entity = getSportRecordByIdLastest(sportList.get(i)
					.getId(), HP_User.getOnLineUserId(context), tablename);
			if (entity == null)
				entity = new TrackEntity(); // add one fake data
			duration += Long.parseLong(GpsUitl.checkNull(entity.getDuration()));
			calory += Double.parseDouble(GpsUitl.checkNull(entity.getCalory()));
			distance += Double.parseDouble(GpsUitl.checkNull(entity
					.getDistance()));
			if (null != sportList.get(i))
				sportSum.put("time", sportList.get(i).getTime());
		}
		sportSum.put("count", String.valueOf(count));
		sportSum.put("duration", String.valueOf(duration));
		sportSum.put("calory", String.valueOf(calory));
		sportSum.put("distance", String.valueOf(distance));
		if (duration != 0) { // in case NaN
			velcotiy = distance / duration;
		}
		sportSum.put("velcotiy", String.valueOf(velcotiy));
		return sportSum;
	}

	public HashMap<String, String> getDaySportHistorybyIndex(
			List<SportEntity> sportList, String tablename) {
		HashMap<String, String> sportSum = new HashMap<String, String>();
		sportSum.put("count", String.valueOf(0));
		sportSum.put("duration", String.valueOf(0));
		sportSum.put("calory", String.valueOf(0));
		sportSum.put("distance", String.valueOf(0));

		if (sportList == null)
			return sportSum;
		int count = sportList.size();
		long duration = 0;
		long calory = 0;
		double distance = 0;
		for (int i = 0; i < count; i++) {
			List<TrackEntity> trackList = getRecordById(sportList.get(i)
					.getId(), HP_User.getOnLineUserId(context), tablename);
			if (null != trackList && trackList.size() > 0) {
				for (int j = 0; j < trackList.size(); j++) {
					TrackEntity entity = trackList.get(j);
					duration += Long.parseLong(GpsUitl.checkNull(entity
							.getDuration()));
					calory += Double.parseDouble(GpsUitl.checkNull(entity
							.getCalory()));
					distance += Double.parseDouble(GpsUitl.checkNull(entity
							.getDistance()));
				}
			}
			if (null != sportList.get(i))
				sportSum.put("time", sportList.get(i).getTime());
		}
		sportSum.put("count", String.valueOf(count));
		sportSum.put("duration", String.valueOf(duration));
		sportSum.put("calory", String.valueOf(calory));
		sportSum.put("distance", String.valueOf(distance));
		return sportSum;
	}

	public List<HistoryModel> getDaySportDetailbyIndex(
			List<SportEntity> sportList, String tablename, int type) {

		List<HistoryModel> dayList = new ArrayList<HistoryModel>();

		if (sportList == null)
			return null;
		int count = sportList.size();
		for (int i = 0; i < count; i++) {
			HistoryModel day = new HistoryModel();
			long duration = 0;
			long calory = 0;
			double distance = 0;
			List<TrackEntity> trackList = getRecordById(sportList.get(i)
					.getId(), HP_User.getOnLineUserId(context), tablename);
			if (null != trackList && trackList.size() > 0) {
				for (int j = 0; j < trackList.size(); j++) {
					TrackEntity entity = trackList.get(j);
					duration += Long.parseLong(GpsUitl.checkNull(entity
							.getDuration()));
					calory += Double.parseDouble(GpsUitl.checkNull(entity
							.getCalory()));
					distance += Double.parseDouble(GpsUitl.checkNull(entity
							.getDistance()));
				}
			}
			switch (type) {
			case HPConst.CHART_TYPE_DISTANCE:
				day.setValue(String.valueOf((int) Math.floor(distance)));
				break;
			case HPConst.CHART_TYPE_CALORY:
				day.setValue(String.valueOf((int) Math.floor(calory)));
				break;
			case HPConst.CHART_TYPE_DURATION:
				day.setValue(String.valueOf(((int) Math.floor(duration)) / 1000));
				break;
			default:
				break;

			}
			if (null != sportList.get(i))
				day.setTime(Long.parseLong(sportList.get(i).getTime()));
			if (Integer.parseInt(day.getValue()) != 0)
				dayList.add(day);
		}
		return dayList;
	}

	// the first valuable sport record id
	public String getFirstValuableSportRecordId(String tablename, int userid) {
		try {
			String sql = "select * from " + tablename + " where userid="
					+ userid;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
						.moveToNext()) {
					String id = cursor.getString(0);
					long duration = Long.parseLong(cursor.getString(1));
					Double distance = Double.parseDouble(cursor.getString(2));
					Double calory = Double.parseDouble(cursor.getString(3));
					if (duration > 0 && distance > 0 && calory > 0) {
						cursor.close();
						return id;
					}
				}
				cursor.close();
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public String getSportRecordTimeById(int id) {
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where id=" + id;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				cursor.moveToFirst();
				String time = cursor.getString(2);
				cursor.close();
				return time;
			}
		} catch (Exception e) {
			return null;
		}
	}

	public HashMap<String, String> getFirstValuableTime(String sporttablename,
			int userid) {
		HashMap<String, String> valueMap = new HashMap<String, String>();
		valueMap.put("firstsporttime", null);
		valueMap.put("firsthearttime", null);
		valueMap.put("firstbloodtime", null);

		String sportId = getFirstValuableSportRecordId(sporttablename, userid);
		if (null != sportId) {
			valueMap.put("firstsporttime",
					getSportRecordTimeById(Integer.parseInt(sportId)));
		}
		valueMap.put("firsthearttime", getFirstValuableHeartRecordTime(userid));
		return valueMap;
	}

	public boolean isTrack(long begintime, long endtime, int userid) {
		try {
			String sql = "select * from "
					+ HealthDBConst.SPORTRECORD_INDEX_TABLE + " where userid="
					+ userid + " and time > " + begintime + " and time < "
					+ endtime;
			Cursor cursor = checkOpen(false).rawQuery(sql, null);
			if (cursor.getCount() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/*
	 * public boolean isTrack(long begintime, long endtime, int userid) { try {
	 * String sql = "select * from " + HealthDBConst.SPORTRECORD_INDEX_TABLE +
	 * " where userid=" + userid; Cursor cursor = checkOpen(false).rawQuery(sql,
	 * null); if (cursor.getCount() < 1) { cursor.close(); return false; } else
	 * { cursor.moveToFirst(); int length = cursor.getCount(); for (int i = 0; i
	 * < length; i++) { if (cursor.isAfterLast()) break; long time =
	 * Long.parseLong(cursor.getString(2)); Log.i("xuxuxu", "time:" + time +
	 * ",begintime:" + begintime + ",endtime:" + endtime + ",cursor.getcount:" +
	 * cursor.getCount()); if (time < begintime || time > endtime){
	 * cursor.moveToNext(); continue; } if (time > begintime && time < endtime)
	 * { return true; } } cursor.close(); return false; } } catch (Exception e)
	 * { return false; } }
	 */

	private SportEntity getSportEntity(Cursor cursor) {
		SportEntity se = new SportEntity();
		se.setId(cursor.getInt(0));
		se.setSportid(cursor.getInt(1));
		se.setTime(cursor.getString(2));
		se.setMode(cursor.getInt(3));
		se.setSensorId(cursor.getInt(4));
		se.setSync(cursor.getInt(5));
		return se;
	}

	private TrackEntity getTrackEntity(Cursor cursor) {
		TrackEntity te = new TrackEntity();
		te.setId(cursor.getInt(0));
		te.setDuration(cursor.getString(1));
		te.setDistance(cursor.getString(2));
		te.setCalory(cursor.getString(3));
		te.setVelocity(cursor.getString(4));
		te.setLongitude(cursor.getString(5));
		te.setLatitude(cursor.getString(6));
		te.setUserId(Integer.parseInt(cursor.getString(7)));
		return te;
	}

	public void deleteAll(String tablename) {
		try {
			String sql = "delete from " + tablename;
			checkOpen(true).execSQL(sql);
		} catch (Exception e) {
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
		createTables(sqLiteDatabase);
	}

}
