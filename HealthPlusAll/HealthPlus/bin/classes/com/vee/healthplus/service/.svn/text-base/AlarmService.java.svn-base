package com.vee.healthplus.service;

import java.util.Calendar;

import android.R.integer;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.vee.healthplus.util.SharedPreferenceUtil;
import com.vee.healthplus.util.alarm.AlarmOberver;
import com.vee.healthplus.util.alarm.AlarmOberver.onAlarmModeListener;
import com.vee.healthplus.util.alarm.AlarmReceiver;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;

public class AlarmService extends Service {

	@Override
	public void onCreate() {
		super.onCreate();
		//beginSensor();
		beginAlarm();
		AlarmOberver.getInstance(this).setListener(new onAlarmModeListener() {
			@Override
			public void onAlarmChange() {
				beginAlarm();
			}
		});
	}

	private void beginSensor(){
		this.startService(new Intent(this,
                SportEngine.class));
		
	}
	@Override
	public void onDestroy() {
		super.onDestroy();

	}

	@TargetApi(5)
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// synchronized (this) {
		//
		// }

		return super.onStartCommand(intent, flags, startId);
	}

	private void beginAlarm() {
		if (HP_TargetConfig.getInstance().getTargetAlarmOn(this)) {
			startAlarm(getAlarmTime());
		} else
			cancelAlarm(this);
	}

	private void addAlarmListener() {
		AlarmOberver.getInstance(this).setListener(new onAlarmModeListener() {
			@Override
			public void onAlarmChange() {
				beginAlarm();
			}
		});
	}

	private void startAlarm(long time) {
		int hour = (int) time / (1000) / 60 / 60;
		int minute = (int) time / (1000) / 60 % 60;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int year=c.get(Calendar.YEAR); //c.getTime().getYear()
		c.set(Calendar.YEAR, year);
		int month= c.getTime().getMonth();
		c.set(Calendar.MONTH,month);
		int day=c.get(Calendar.DAY_OF_MONTH) ;//c.getTime().getDay()
		c.set(Calendar.DAY_OF_MONTH,day);
		c.set(Calendar.HOUR_OF_DAY, hour);
		c.set(Calendar.MINUTE, minute);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		
		Intent intent = new Intent(this, AlarmReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0); //requestcode
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		
		long value=c.getTimeInMillis();
		long nowValue=System.currentTimeMillis();
	    if(value<nowValue){ //delay
	    	value=value+1000*60*60*24;
	    }
		am.setRepeating(AlarmManager.RTC_WAKEUP, value, (24 * 60 * 60 * 1000), pi);
		
		//am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
	}

	private void cancelAlarm(Context context) {
		Intent intent = new Intent(context, AlarmReceiver.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarm = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarm.cancel(sender);
	}

	private long getAlarmTime() {
		return HP_TargetConfig.getInstance().getTargetAlarmTime(this);
	}

	public static class BootCompletedBroadcast extends BroadcastReceiver {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			context.startService(new Intent(context, AlarmService.class));

		}
	}

	// private final InjectBinder mBinder = new InjectBinder();
	// public class InjectBinder extends Binder {
	// public AlarmService getService() {
	// return AlarmService.this;
	// }
	// }

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
