package com.vee.healthplus.util.alarm;

import java.util.Random;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.main.MainPage;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;

public class AlarmReceiver extends BroadcastReceiver {
	 private Builder builder = null;
	 private  Notification n =null;
    @Override
    public void onReceive(Context context, Intent intent) {
    	 //NotificationSystem.getInstacnce(context).showNotification(0);
    	showNotification(context);
    	 
    }
    
	/*private void showNotification(Context context){ 
		Random random =new Random();
		int id=random.nextInt();
				
		NotificationManager	manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(context, MainPage.class);
		intent.putExtra("id",id);
		PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		Notification mNotification = new Notification(); 
		mNotification.icon = R.drawable.ic_pic;
		mNotification.tickerText = "温馨提示"; 
		mNotification.defaults = Notification.DEFAULT_SOUND; 
		mNotification.flags=Notification.FLAG_AUTO_CANCEL;
		String content=HP_TargetConfig.getInstance().getCurTargetNotiStr(context);
		String title=HP_TargetConfig.getInstance().getCurTargetStr(context);
		if(content!=null&&!content.equals("")){
			mNotification.setLatestEventInfo(context, title, content, mPendingIntent); 
			manager.notify(id, mNotification); 
		}
	}
	*/
	private  void showNotification(Context context){
		
		Random random =new Random();
		int id=random.nextInt();
		String content=HP_TargetConfig.getInstance().getCurTargetNotiStr(context);
		String title=HP_TargetConfig.getInstance().getCurTargetStr(context);
		NotificationManager	manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent = new Intent(context, MainPage.class);
		intent.putExtra("id",id);
		PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		if(content!=null&&!content.equals("")){
		 Resources res = context.getResources();
		 builder = new Notification.Builder(context);
		 builder.setContentIntent(mPendingIntent).
		// setSmallIcon(R.drawable.ic_pic).
		 setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_notifiaction))
		 .setTicker("温馨提示").
		 setWhen(System.currentTimeMillis())
		 .setAutoCancel(true)
		 .setContentTitle(title)
		 .setContentText(content);
		 n  = builder.getNotification();
		 n.defaults = Notification.DEFAULT_SOUND;
		 manager.notify(id, n);
		}
		
	}
}
