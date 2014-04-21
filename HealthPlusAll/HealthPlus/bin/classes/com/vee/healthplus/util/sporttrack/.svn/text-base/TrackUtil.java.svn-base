package com.vee.healthplus.util.sporttrack;

import java.util.HashMap;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.sporttrack.SportTrackMain;
import com.vee.healthplus.util.SharedPreferenceUtil;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.CustomDialog;

public class TrackUtil implements ISensorData {
    private static TrackUtil mInstance = null;

    private Context context;

    HealthDB dbHelper;

    public TrackUtil(Context context) {
        this.context = context;
        dbHelper = HealthDB.getInstance(this.context);
    }

    public static TrackUtil getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TrackUtil(context);
        }
        return mInstance;
    }

    public void changeSensor() {
        if (isGpsSensor())
            SharedPreferenceUtil.setBooleanPref(context, "senid", false);
        else SharedPreferenceUtil.setBooleanPref(context, "senid", true);  
    }

    public boolean isGpsSensor() {
        return SharedPreferenceUtil.getBooleanPref(context, "senid", true); //true--gps,false--pedo
    }

    public String getTableName() {
        if (isGpsSensor()) {
            return HealthDBConst.SPORTRECORD_TABLE;
        } else return HealthDBConst.SPORTRECORD__PEDO_TABLE;
    }

    public boolean isTrack(long begintime, long endtime, int userid) {
        return HealthDB.getInstance(context).isTrack(begintime, endtime, userid);
    }

    public List<TrackEntity> getRecordById(int id, int userid, String tablename) {
        return HealthDB.getInstance(context).getRecordById(id, userid, tablename);
    }

    public HashMap<String, String> getTaskState() {
        String string = "";
        long worktime = HP_TargetConfig.getInstance().getTargetTime(context); //设置目标时间
        long modeCalory = HP_TargetConfig.getInstance().getTargetCalorie(context);
        long modeDistance = HP_TargetConfig.getInstance().getTargetDistance(context);
        long modeDuration = HP_TargetConfig.getInstance().getTargetTime(context);

        HealthDB helper = HealthDB.getInstance(context);
        List<SportEntity> sportList = helper.getSportIndexByDateExceptLast(worktime, HP_User.getOnLineUserId(context));
        HashMap<String, String> result = dbHelper.getSportSumbyIndex(sportList, getTableName());
        return result;
    }

    //为不同数据显示接口
    public List<SportEntity> getTrackListByDate(long begintime, long endtime, int userid, String tablename) {
        List<SportEntity> sportList = HealthDB.getInstance(context).getSportIndexByDate(begintime, endtime, userid);
        if (sportList == null) return null;
        for (int i = 0,size=sportList.size(); i < size; i++) {
            SportEntity index = sportList.get(i);
            TrackEntity te = HealthDB.getInstance(context).getSportRecordByIdLastest(index.getId(), index.getUserId(), tablename);
            if (te == null) {
              te=new TrackEntity();
            }
            index.addTrack(te);
        }
        return sportList;
    }
   
    public void showTaskState(String text) {
        TextView textView = new TextView(context);
        textView.setTextColor(context.getResources().getColor(
                R.color.hp_w_target_week));
        textView.setTextSize(16);
        textView.setPadding(30, 30,30, 40);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        //textView.setText(getTaskState());
        CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
        customBuilder.setTitle("温馨提示").setContentView(textView)
                .setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        Dialog dialog = customBuilder.createWrapContent();
        dialog.show();
    }
    
    public static String getVelocity(String distanceStr,String timeStr){ //米/小时,保留后两位
    	StringBuffer buffer = new StringBuffer();
    	if(distanceStr==null||distanceStr.equals("")||distanceStr.equals("0")||timeStr==null||timeStr.equals("")||timeStr.equals("0")) buffer.append("0.00");
    	else {
    		double distance=Double.parseDouble(distanceStr);
    		long time=Long.parseLong(timeStr);
			double doubleValue=(double) ( Math.round((distance*3600000)*100.0/ time )/100.0);
			buffer.append(String.valueOf(doubleValue));
			}
    	return buffer.toString();
    }
	

    
    public double getStandardCalory(){
    	int sportid = dbHelper.getSportIndexLastestSportId(HP_User.getOnLineUserId(context));
		HP_User userEntity=HP_DBModel.getInstance(context).queryUserInfoByUserId(HP_User.getOnLineUserId(context),true);
		double standardCalory;
		if(userEntity==null) 
			standardCalory=calculateCalory(20, 55,sportid);
		else
			standardCalory=calculateCalory(userEntity.userAge, userEntity.userWeight,sportid);
	 return standardCalory;
    }

    private static double calculateCalory(int age, float weight, int sportid) { //米，小时
		if(age==0) age=20;
		if(weight==0)weight=55;
		
		//
    	float metabolism = 0;
		float coefficient = 0;
		
		if (age >= 18 && age <= 30) {
			metabolism = 14.6f * weight + 450f;
		} else if (age > 30 && age <= 60) {
			metabolism = 8.6f * weight + 830f;
		} else if (age > 60) {
			metabolism = 13.4f * weight + 490f;
		} else {
			return -1;
		}
		
		float stepSpeed=0;
		switch (sportid) {
		case 0:
			stepSpeed = 130;
			break;
		case 1:
			stepSpeed = 200;
			break;
		case 2:
			stepSpeed = 100;
			break;
		default:
			break;
		}
		
		if (stepSpeed > 0 && stepSpeed <= 90) {
			coefficient = 0.18f;
		} else if (stepSpeed > 90 && stepSpeed <= 120) {
			coefficient = 0.38f;
		} else if (stepSpeed > 120 && stepSpeed <= 150) {
			coefficient = 0.45f;
		} else if (stepSpeed > 150) {
			coefficient = 0.49f;
		} else {
			return -1;
		}                 
		
		return (double) (Math.round((metabolism * coefficient)*100.0/5000.0)/100.0);
	}

    
    private void setSyncSportIndexId(){
    	SharedPreferenceUtil.setIntPref(context, "sportindexidlastest", 0);
    }
    
    private int getSyncSportIndexId(){
    	return SharedPreferenceUtil.getIntPref(context, "sportindexidlastest", 0);
    }
}
