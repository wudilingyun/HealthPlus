package com.vee.healthplus.util.sporttrack;

import java.util.Date;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.user.HP_User;

public class GpsSensorUtil implements ISensorData {
	private static GpsSensorUtil mInstance = null;

	private Context context;

	private HealthDB dbHelper;
	
	private String tablename;

	public GpsSensorUtil(Context context,String tablename) {
		this.context = context;
		dbHelper = HealthDB.getInstance(this.context);
		this.tablename=tablename;
	}

	public static GpsSensorUtil getInstance(Context context,String tablename) {
		if (mInstance == null) {
			mInstance = new GpsSensorUtil(context,tablename);
		}
		return mInstance;
	}

	// 有效距离的概念
	public TrackEntity dataProcess(BDLocation location) {
		if (location == null)
			return null;

		TrackEntity newTe = new TrackEntity();
		TrackEntity oldTe = dbHelper.getSportRecordByIdLastest(dbHelper.getSportIndexLastestId(HP_User.getOnLineUserId(context)),HP_User.getOnLineUserId(context),tablename);
		int index = dbHelper.getSportIndexLastestId(HP_User
				.getOnLineUserId(context));

		// whether new or old
		newTe.setId(index);
		newTe.setUserId(HP_User.getOnLineUserId(context));
		newTe.setLatitude(String.valueOf(location.getLatitude()));
		newTe.setLongitude(String.valueOf(location.getLongitude()));

		if (oldTe == null || oldTe.getId() != index) { // it's totolly new or a
														// new sport and first
														// time
		} else { // oldTe.getId()==index
			newTe.setDistance(getDistance(newTe, oldTe));
			long duration = getDuration(newTe);
			newTe.setDuration(String.valueOf(duration));
			newTe.setCalory(getCalory(newTe));
			newTe.setVelocity(getVelocity(newTe, oldTe));
		}
		return newTe;
	}

	private long getDuration(TrackEntity newTe) {
		long beginTime = Long.parseLong(GpsUitl.checkNull(dbHelper
				.getSportIndexLastestTime(HP_User.getOnLineUserId(context))));
		long nowTime = (new Date()).getTime();
		return nowTime - beginTime;
	}

	public String getDistance(TrackEntity newTe, TrackEntity oldTe) { //1e6--double into int,米,精确到厘米
		double finishDistance = Double.parseDouble(GpsUitl.checkNull(oldTe
				.getDistance()));
		GeoPoint oldPoint = new GeoPoint((int) (Double.parseDouble(GpsUitl
				.checkNull(oldTe.getLatitude())) * 1E6),
				(int) (Double.parseDouble(GpsUitl.checkNull(oldTe
						.getLongitude())) * 1E6));
		GeoPoint newPoint = new GeoPoint((int) (Double.parseDouble(newTe
				.getLatitude()) * 1E6), (int) (Double.parseDouble(newTe
				.getLongitude()) * 1E6));
		double v=DistanceUtil.getDistance(newPoint, oldPoint);
		double increaseDistance = (double)(Math.round(v*100.0)/100.0);
		return String.valueOf(finishDistance + increaseDistance);
	}

	private String getCalory(TrackEntity newTe) { //米,小时
	    double distance=Double.parseDouble(newTe.getDistance());
		double value= (double)(Math.round(TrackUtil.getInstance(context).getStandardCalory() *distance*100.0)/100.0);
		return String.valueOf(value);
	}
	
	private String getVelocity(TrackEntity newTe, TrackEntity oldTe) {//米/小时
		double increaseDistance = Double.parseDouble(GpsUitl.checkNull(newTe
				.getDistance()))
				- Double.parseDouble(GpsUitl.checkNull(oldTe.getDistance()));
		long increaseTime = (Long.parseLong(GpsUitl.checkNull(newTe
				.getDuration())) - Long.parseLong(GpsUitl.checkNull(oldTe
				.getDuration())));
		double velcotiy = (double)(Math.round((increaseDistance*3600000)*100.0/ increaseTime )/100.0);
		return String.valueOf(velcotiy);
	}
}
