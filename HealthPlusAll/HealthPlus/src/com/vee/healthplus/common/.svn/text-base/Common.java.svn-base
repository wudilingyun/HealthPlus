package com.vee.healthplus.common;

import android.content.Context;

import com.vee.healthplus.http.StatisticsUtils;

public class Common {



//	public static  String APPLICATION_ID = "700071";
	public static  String UPDATE_SERVER = "http://cdn.17vee.com/lmstation/shoujiweishi/";
			//+ APPLICATION_ID + "/";
	public static final String UPDATE_VERSION_JSON = "version.json";
	public static final String UPDATE_APK_NAME = "CloudDoctor.apk";
	public static final String DOWNLOADDIRNAME = "CloudDoctor";
	
	
	public static String  getAppId(Context context){
		return StatisticsUtils.getChannel(context);
	}
	
	public static String getUpdate_Server(Context context){
		
		return UPDATE_SERVER+getAppId(context)+"/";
	}
	
}
