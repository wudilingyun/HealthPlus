package com.vee.healthplus.util.alarm;

import java.util.ArrayList;
import java.util.List;

import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.sportmode.ModeControl;
import com.vee.healthplus.util.sportmode.ModeControl.onModeListener;

import android.R.integer;
import android.content.Context;
import android.widget.TextView;

public class AlarmOberver {
	private static AlarmOberver mInstance = null;

	private onAlarmModeListener mListeners;

	private AlarmOberver(Context context) {
	}

	public static synchronized AlarmOberver getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new AlarmOberver(context);
		}
		return mInstance;
	}

	public void setListener(onAlarmModeListener listener) {
		this.mListeners = listener;
	}

	public void removeListener(onAlarmModeListener listener) {
		mListeners = null;
	}

	public void onChange() {
		if (mListeners != null) {
			mListeners.onAlarmChange();
		}
	}

	public interface onAlarmModeListener {
		public void onAlarmChange();
	}
}
