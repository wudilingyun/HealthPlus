package com.vee.healthplus.util.sportmode;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;

public class ModeControl{		
    private static ModeControl mInstance = null;

    private List<onModeListener> mListeners;

    private ModeControl(Context context) {
    }

    public static synchronized ModeControl getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ModeControl(context);
        }
        return mInstance;
    }

    public void setListener(onModeListener listener) {
    	if(mListeners==null) //lazy loading
    		mListeners=new ArrayList<ModeControl.onModeListener>();
        mListeners.add(listener);
    }

    public void removeListener(onModeListener listener) {
        mListeners=null;
    }

    public void onChange(int type) {
        if(mListeners!=null){
        	for(int i=0,size=mListeners.size();i<size;i++){
        		mListeners.get(i).onModeChange(type);
        	}  
        }
    }

    public interface onModeListener {
        public void onModeChange(int type);
    }
}
