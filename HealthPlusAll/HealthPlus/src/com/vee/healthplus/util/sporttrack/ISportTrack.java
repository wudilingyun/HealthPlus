package com.vee.healthplus.util.sporttrack;

import com.baidu.location.BDLocation;


public interface ISportTrack {
    public void onStep(int step);

    public void onLocationChange(BDLocation location);
}
