package com.vee.healthplus.ui.heahth_heart;

import android.content.Context;

import com.vee.healthplus.util.sporttrack.HealthDB;

public class PutDataforSQL {
	HealthDB healthDB;

	public void putHeartData(Context context, String imgavg, long date) {
		if (Integer.parseInt(imgavg) > 0 && Integer.parseInt(imgavg) < 150) {
			healthDB = HealthDB.getInstance(context);
			DataForSQL data = new DataForSQL();
			data.setCurrdate(date);
			data.setHeartdata(imgavg);
			if (healthDB.queryDate(date)) {
				healthDB.updateHeart(data);
			} else {
				
				healthDB.addHeart(data);
			}

		}

	}

	// 根据时间查询心率值。
	public String getHeartData(Context context, long date) {
		healthDB = HealthDB.getInstance(context);
		String heartData = healthDB.getHeart(date);
		return heartData;

	}
}
