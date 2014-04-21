package com.vee.healthplus.service;

import java.util.ArrayList;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.baidumap.MyLocationListenner;
import com.vee.healthplus.util.pedometer.StepDetector;
import com.vee.healthplus.util.pedometer.StepListener;
import com.vee.healthplus.util.sporttrack.GpsSensorUtil;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.HealthDBConst;
import com.vee.healthplus.util.sporttrack.ISportTrack;
import com.vee.healthplus.util.sporttrack.PedoSensorUtil;
import com.vee.healthplus.util.sporttrack.TrackEntity;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;

public class SportEngine extends Service implements ISportTrack {

    private static final String TAG = "xuxuxu";

    private static final int SCANSPAN = 3000;

    private static MyApplication app = null;
    
    private ISportTrack sportTrackListener = null;

    private LocationClient mLocClient;

    private SensorManager sm;

    private StepDetector sd = new StepDetector();

    private static Context mContext;

    private MyLocationListenner locListener = new MyLocationListenner();
    
    private int sportId;
    private HealthDB dbHelper;
    boolean isFirstGps;

    @Override
    public void onCreate() {
        super.onCreate();
        app = MyApplication.getInstance();
        mContext = app.getApplicationContext();
        sm = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
        locListener.setScanSpan(SCANSPAN);
        
        beginSensor();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return sportEngineBinder;
    }

    public class SportEngineBinder extends Binder {

        public SportEngine getService() {
            return SportEngine.this;
        }
    }

    private SportEngineBinder sportEngineBinder = new SportEngineBinder();

    
  
    public void beginSensor(){
    	dbHelper=HealthDB.getInstance(this);
        sportId=dbHelper.getSportIndexLastestSportId(HP_User.getOnLineUserId(this));
        isFirstGps=false;
        
    	 startPedometer(0);
         startTrack();
         setSportTrackListener(this);
     }
    public boolean startTrack() {
        if (locListener == null)
            return false;
        initMap();
        locListener.reset();
        mLocClient = new LocationClient(mContext);
        mLocClient.registerLocationListener(locListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(SCANSPAN);//1000
        mLocClient.setLocOption(option);
        mLocClient.start();
        return true;
    }

    @Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stopPedometer();
		stopTrack();
        Log.i("xuxuxu", "onDestroy");
        super.onDestroy();
	}

	public void stopTrack() {
        locListener.reset();
        if (mLocClient != null) {
            Log.i("xuxuxu", "stopTrack");
            mLocClient.stop();
        }
    }

    public void initMap() {
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(mContext);
            app.mBMapManager.init(MyApplication.strKey, new MyApplication.MyGeneralListener());
        }
    }

    public double getDistance() {
        return 0.01;
    }

    public Long getSpendTime() {
        return 0L;
    }

    public void startPedometer(int sensitivity) {
        sd.addStepListener(new StepListener() {

            @Override
            public void passValue() {
                // TODO Auto-generated method stub

            }

            @Override
            public void onStep(int type) {
                // TODO Auto-generated method stub
                sportTrackListener.onStep(type);
            }
        });
        sd.setSensitivity(sensitivity);

        sm.registerListener(sd, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_GAME);
    }

    public void stopPedometer() {
        sm.unregisterListener(sd);
    }

    public boolean isGpsOK() {
        return false;
    }

    public void setSportTrackListener(ISportTrack sportTrackListener) {
        this.sportTrackListener = sportTrackListener;
        locListener.setSportTrackListener(sportTrackListener);
    }

	@Override
	public void onStep(int step) {
	       if (sportId != HPConst.SPORT_TYPE_WALK) return;
	        if (step == 0) return; //has step.zero--no step
	        sendPedoBroadcast();
//	        //no save pedo data
//	        TrackEntity te = PedoSensorUtil.getInstance(this,HealthDBConst.SPORTRECORD__PEDO_TABLE).dataProcess(step*100);
//	        dbHelper.addSportRecord(te,HealthDBConst.SPORTRECORD__PEDO_TABLE);
//	        updateSportTime();	
//	        if(!TrackUtil.getInstance(this).isGpsSensor()){
//	        	sendGPSBroadcast(te);}
		
	}

	@Override
	public void onLocationChange(BDLocation location) {
		  TrackEntity te = GpsSensorUtil.getInstance(this, HealthDBConst.SPORTRECORD_TABLE).dataProcess(location);
	        dbHelper.addSportRecord(te, HealthDBConst.SPORTRECORD_TABLE);
	        if (TrackUtil.getInstance(this).isGpsSensor()) {
	        	sendGPSBroadcast(te); 	
	        }
	        updateSportTime();
	}
	
    private void updateSportTime() {
        if (isFirstGps) {
            dbHelper.updateSportIndexLastestTime(HP_User.getOnLineUserId(this), (new Date()).getTime());
            isFirstGps = false;
        }
    }

    private void sendGPSBroadcast(TrackEntity te)  
    {  
        Intent intent=new Intent(HPConst.SPORT_GPS_RECEIVER_ACTION);  
        Bundle bundle=new Bundle();   
        bundle.putParcelable("te", te);
        intent.putExtras(bundle);  
        this.sendBroadcast(intent);       
    }  
    
     private void sendPedoBroadcast()  
    {  
        Intent intent=new Intent(HPConst.SPORT_PEDO_RECEIVER_ACTION);  
        this.sendBroadcast(intent);       
    } 
}
