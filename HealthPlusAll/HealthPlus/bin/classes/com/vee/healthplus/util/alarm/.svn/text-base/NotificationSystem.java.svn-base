package com.vee.healthplus.util.alarm;

import android.R.integer;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.main.MainPage;

public class NotificationSystem implements Handler.Callback {
	Handler handler = new Handler(this);
	private static NotificationSystem singleton;
	public static int id = 111111111;

	private Context context;
	NotificationManager manager;
	Notification notification;
	RemoteViews contentView;

	public NotificationSystem(Context mContext) {
		this.context = mContext;

	}

	public void showNotification(int type) {
		if (getNoti(context)) {
			createNotification();
			updateView(type);
		} else
			closeNoticaticon();
	}

	public void createNotification() {
		if (manager != null)
			return;
		manager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		//getOngoingNoti(notification);
		getAutocancelNoti();

		// notification.flags = Notification.FLAG_ONGOING_EVENT;
		// notification.flags=Notification.DEFAULT_SOUND;
		// notification.flags=Notification.DEFAULT_VIBRATE; //加入设置后不在auto cancel
	}

	private void getAutocancelNoti() {
		Intent intent = new Intent(context, MainPage.class);
		intent.putExtra("id", id);
		PendingIntent mPendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);

		notification = new Notification();
		notification.tickerText = "您有目标尚未完成";
		notification.defaults = Notification.DEFAULT_SOUND;
		notification.setLatestEventInfo(context, "您有目标尚未完成", "您有目标尚未完成",
				mPendingIntent);
	}

	private void getOngoingNoti() {
		contentView = new RemoteViews(context.getPackageName(),
				R.layout.hp_notification);
		notification = new Notification(R.drawable.ic_launcher, "您有目标尚未完成",
				System.currentTimeMillis());
		notification.contentView = contentView;
		addPendingIntent(MainPage.class);

	}

	private void addPendingIntent(Class<?> c) {
		Intent intent = new Intent(context, c);
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				intent, 0);
		notification.contentIntent = pendingIntent;
	}

	public void updateView(int type) {
		if (contentView != null && manager != null) {
			updateText();

			manager.notify(id, notification);
		} else
			manager.notify(id, notification);
	}

	private void updateText() {
		notification.contentView.setViewVisibility(R.id.content, View.VISIBLE);

		notification.contentView.setTextViewText(R.id.content, "任务的描述");
	}

	private boolean getNoti(Context context) {
		return true;
	}

	public void closeNoticaticon() {

		if (manager != null) {
			manager.cancel(id);
			manager = null;
		}
	}

	public static NotificationSystem getInstacnce(Context context) {
		if (singleton == null)
			singleton = new NotificationSystem(context);
		return singleton;
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
