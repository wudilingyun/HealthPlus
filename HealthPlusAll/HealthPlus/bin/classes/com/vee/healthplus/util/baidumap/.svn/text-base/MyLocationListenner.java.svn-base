package com.vee.healthplus.util.baidumap;

import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.vee.healthplus.util.sporttrack.ISportTrack;

public class MyLocationListenner implements BDLocationListener {

    private static final String TAG = "xuxuxu";

    private final int IGNORE_POINT = 3;

    private ISportTrack sportTrackListener = null;

    private int scanSpan = 0;
    private long lastScanLocTime = 0;
    private long curScanLocTime = 0;
    public static int curPoint = 0;

    @Override
    public void onReceiveLocation(BDLocation location) {
        if (lastScanLocTime == 0 && curPoint < IGNORE_POINT) {
            curPoint++;
            return;
        }
        if (location == null || location.getLocType() != BDLocation.TypeGpsLocation) {
            Log.i(TAG, "location is null or is not gps location!");
            return;
        }
        if (location.getLatitude() == 4.9E-324 || location.getLongitude() == 4.9E-324) {
            Log.i(TAG, "location is 4.9E-324");
            return;
        }
        Log.i(TAG, "location is not null!" + location.getLocType());
        if (sportTrackListener != null) {
            curScanLocTime = System.currentTimeMillis();
            if (lastScanLocTime != 0 && (curScanLocTime - lastScanLocTime) > scanSpan) {
                reset();
                return;
            }
            sportTrackListener.onLocationChange(location);
            lastScanLocTime = curScanLocTime;
        }
    }

    public void onReceivePoi(BDLocation poiLocation) {
        Log.i(TAG, "on receive poi!");
        if (poiLocation == null) {
            return;
        }
    }

    public void setSportTrackListener(ISportTrack sportTrackListener) {
        this.sportTrackListener = sportTrackListener;
    }

    public void setScanSpan(int scanSpan) {
        this.scanSpan = scanSpan;
    }

    public void reset() {
        curPoint = 0;
        setLastScanLocTime(0);
        setCurScanLocTime(0);
    }

    public void setLastScanLocTime(long lastScanLocTime) {
        this.lastScanLocTime = lastScanLocTime;
    }

    public void setCurScanLocTime(long curScanLocTime) {
        this.curScanLocTime = curScanLocTime;
    }

}
