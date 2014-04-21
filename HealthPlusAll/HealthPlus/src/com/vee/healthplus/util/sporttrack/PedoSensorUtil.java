package com.vee.healthplus.util.sporttrack;

import java.util.Date;

import android.content.Context;

import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;

public class PedoSensorUtil implements ISensorData {
	private static PedoSensorUtil mInstance = null;

	private Context context;

	HealthDB dbHelper;
	
	private String tablename;

	public PedoSensorUtil(Context context,String tablename) {
		this.context = context;
		dbHelper = HealthDB.getInstance(this.context);
		this.tablename=tablename;
	}

	public static PedoSensorUtil getInstance(Context context,String tablename) {
		if (mInstance == null) {
			mInstance = new PedoSensorUtil(context,tablename);
		}
		return mInstance;
	}

	
	public TrackEntity dataProcess(int stepCounts) { //all count
		if (stepCounts == 0)
			return null;

		TrackEntity newTe = new TrackEntity();
		TrackEntity oldTe =  dbHelper.getSportRecordByIdLastest(dbHelper.getSportIndexLastestId(HP_User.getOnLineUserId(context)),HP_User.getOnLineUserId(context),tablename);
		int index = dbHelper.getSportIndexLastestId(HP_User
				.getOnLineUserId(context));

		// whether new or old
		newTe.setId(index);
		newTe.setUserId(HP_User.getOnLineUserId(context));
		if (oldTe == null || oldTe.getId() != index) { // it's totolly new or a
														// new sport and first
														// time
		} else { // oldTe.getId()==index
			newTe.setDistance(getDistance(newTe,stepCounts));
			long duration = getDuration(newTe);
			newTe.setDuration(String.valueOf(duration)); //时间和速度的计算不变，与gps相同
			newTe.setCalory(getCalory(newTe,stepCounts));
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

	private String getDistance(TrackEntity newTe, int stepCounts) { 
		double distance =stepCounts*getSteplength();
		return String.valueOf(distance);
	}

	private String getCalory(TrackEntity newTe,int stepCounts) {
	    double distance=stepCounts*getSteplength();
		double value=(double) ( Math.round(TrackUtil.getInstance(context).getStandardCalory() *distance*100.0)/100.0);
		return String.valueOf(value);
	}
	
	  private double getSteplength(){ //厘米--米
			HP_User userEntity=HP_DBModel.getInstance(context).queryUserInfoByUserId(HP_User.getOnLineUserId(context), true);
			if(userEntity==null) return 0.6;
			float height=userEntity.userHeight;
			if(height==0) return 0.6;
			else return    (double)(Math.round(height*3/100.0*100/7)/100.0);
		}
		

	private String getVelocity(TrackEntity newTe, TrackEntity oldTe) { //米/小时
		double increaseDistance = Double.parseDouble(GpsUitl.checkNull(newTe
				.getDistance()))
				- Double.parseDouble(GpsUitl.checkNull(oldTe.getDistance()));
		long increaseTime = (Long.parseLong(GpsUitl.checkNull(newTe
				.getDuration())) - Long.parseLong(GpsUitl.checkNull(oldTe
				.getDuration())));
		double velcotiy =(double) (Math.round( (increaseDistance*3600000)*100.0/ increaseTime)/100.0);
		return String.valueOf(velcotiy);
	}
}
