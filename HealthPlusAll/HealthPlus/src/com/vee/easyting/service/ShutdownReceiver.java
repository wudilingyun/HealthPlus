package com.vee.easyting.service;

import com.vee.easyting.service.MusicService;
import com.vee.easyting.utils.MyApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ShutdownReceiver extends BroadcastReceiver {

	String TAG  = "ShutdownReceiver";
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Log.i(TAG, "ShutdownReceiver");
		// TODO Auto-generated method stub
		MusicService.musicServiceStop(arg0);
		//deinitReceiver();
		Intent intent = new Intent(arg0,MusicService.class);
		arg0.stopService(intent);
		
		MyApplication.getInstance().exit();
	}

}
