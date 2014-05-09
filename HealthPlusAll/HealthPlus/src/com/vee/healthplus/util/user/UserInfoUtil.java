package com.vee.healthplus.util.user;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

import com.vee.healthplus.R;
import com.vee.healthplus.heahth_news_http.ImageGetFromHttp;
import com.vee.healthplus.heahth_news_http.ImageLoader;
import com.vee.healthplus.heahth_news_utils.ImageFileCache;
import com.vee.healthplus.heahth_news_utils.ImageMemoryCache;

/**
 * Created by lingyun
 */
public class UserInfoUtil {

	private static HP_User currentUser;
	private Context mContext;
	private static ImageFileCache fileCache;
	private static ImageMemoryCache memoryCache;
	private static Bitmap result,defaultheadimg;
	private static String url;

	public UserInfoUtil(Context c) {
		mContext = c;
		currentUser = HP_DBModel.getInstance(mContext).queryUserInfoByUserId(
				HP_User.getOnLineUserId(mContext), true);
	}
	
	
	
	
	public static void getUserHeadImg(Context context,final ImageView iv) {
		currentUser = HP_DBModel.getInstance(context).queryUserInfoByUserId(
				HP_User.getOnLineUserId(context), true);
		
		defaultheadimg=BitmapFactory.decodeResource(context.getResources(),
				R.drawable.healthplus_personal_info_default_photo);
		if (currentUser == null) {
			result= defaultheadimg;
		}
		memoryCache = new ImageMemoryCache(context);
		fileCache = new ImageFileCache();
		url = currentUser.photourl;
		if(url.equals("")||url==null){
			result=defaultheadimg;
		}
		result = memoryCache.getBitmapFromCache(url);
		new Thread(){
			public void run() {
				
				if (result == null) {
					result = fileCache.getImage(url);
					if (result == null) {
						result = ImageGetFromHttp.downloadBitmap(url);
						if (result != null) {
							fileCache.saveBitmap(result, url);
							memoryCache.addBitmapToCache(url, result);
						} else {
							result = defaultheadimg;
							memoryCache.addBitmapToCache(url, result);
						}
					}
				}
				
				new Handler(){
					public void handleMessage(android.os.Message msg) {
						iv.setImageBitmap(result);
					};
				}.sendEmptyMessage(0);
				
				
			};
		}.start();
	
	}
	
	
	
	public static String getUserNickName(Context context) {
		currentUser = HP_DBModel.getInstance(context).queryUserInfoByUserId(
				HP_User.getOnLineUserId(context), true);
		if (currentUser == null) {
			return "未设置";
		}
		String nickname = currentUser.userNick;
		if (nickname.equals("") || nickname == null) {
			nickname = "未设置";
		}
		return nickname;
	}

}
