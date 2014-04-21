package com.vee.healthplus.util.sportmode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.sporttrack.SportTrackMain;
import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.SystemMethod;
import com.vee.healthplus.util.sportmode.ModeControl.onModeListener;
import com.vee.healthplus.util.sporttrack.TrackUtil;

/**
 * Created by zhou on 13-10-28.
 * 
 * @author zhou 目标配置文件信息
 */
public class HP_TargetConfig {
	public static HP_TargetConfig instance;
	private int userId = 0;
	private long curValue = 0;
	private static final String ConfigName = "HPTarget";
	private static final String Model = "Model";
	private static final String FiledTargetMode = "TargetMode";
	private static final String TargetModeSport = "TargetModeSport";
	private static final String FiledTargetTime = "TargetTime";
	private static final String FiledTargetCalorie = "TargetCalorie";
	private static final String FiledTargetDistance = "TargetDistance";
	private static final String FiledTargetAlarmTime = "TargetAlarmTime";
	private static final String FiledTargetAlarmOnOff = "FiledTargetAlarmOnOff";
	private static final String TargetUpdateTime = "TargetUpdateTime";
	private boolean isTargetFinish = false;

	public static enum TargetMode {
		DAY, WEEK
	}

	public static enum TargetSportMode {
		DISTANCE, TIME, CALORIE
	}

	public static HP_TargetConfig getInstance() {
		if (instance == null) {
			instance = new HP_TargetConfig();
		}
		return instance;
	}

	/**
	 * 获取模式
	 * 
	 * @param context
	 * @return
	 */
	public int getMode(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(Model, 0);
	}

	/**
	 * 设置模式
	 * 
	 * @param model
	 * @param context
	 */
	public void setMode(int model, Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putInt(Model, model);
		editor.commit();
	}

	/**
	 * 获取运动计划模式
	 * 
	 * @param context
	 * @return
	 */
	public int getTargetMode(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(FiledTargetMode,
				TargetMode.DAY.ordinal());
	}

	/**
	 * 设置运动计划模式
	 * 
	 * @param targetMod
	 * @param context
	 */
	public void setTargetMode(TargetMode targetMod, Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putInt(FiledTargetMode, targetMod.ordinal());
		editor.commit();
	}

	/**
	 * 获取运动计划
	 * 
	 * @param context
	 * @return
	 */
	public int getTargetSportMode(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(TargetModeSport,
				TargetMode.DAY.ordinal());
	}

	/**
	 * 设置运动计划
	 * 
	 * @param targetSportMod
	 * @param context
	 */
	public void setTargetSportMode(TargetSportMode targetSportMod,
			Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putInt(TargetModeSport, targetSportMod.ordinal());
		editor.commit();
	}

	/**
	 * 获取运动计划时间
	 * 
	 * @param context
	 * @return
	 */
	public int getTargetTime(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(FiledTargetTime, 0);
	}

	/**
	 * 设置运动计划时间
	 * 
	 * @param minute
	 * @param context
	 */
	public void setTargetTime(int minute, Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putInt(FiledTargetTime, minute);
		editor.commit();
	}

	/**
	 * 获取运动计划卡路里
	 * 
	 * @param context
	 * @return
	 */
	public int getTargetCalorie(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(FiledTargetCalorie, 0);
	}

	/**
	 * 设置运动计划卡路里
	 * 
	 * @param calorie
	 * @param context
	 */
	public void setTargetCalorie(int calorie, Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putInt(FiledTargetCalorie, calorie);
		editor.commit();
	}

	/**
	 * 获取运动计划距离
	 * 
	 * @param context
	 * @return
	 */
	public int getTargetDistance(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(FiledTargetDistance, 0);
	}

	/**
	 * 设置运动计划距离
	 * 
	 * @param distance
	 * @param context
	 */
	public void setTargetDistance(int distance, Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putInt(FiledTargetDistance, distance);
		editor.commit();
	}

	public long getTargetUpdateTime(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getLong(TargetUpdateTime, 0);
	}

	public void setTargetUpdateTime(long time, Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putLong(TargetUpdateTime, time);
		editor.commit();
	}

	/**
	 * 设置登录用户ID
	 * 
	 * @param userId
	 *            用户ID
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * 获取登录用户ID
	 */
	public int getUserId() {
		return userId;
	}

	public void setFinishState(boolean isTargetFinish) {
		this.isTargetFinish = isTargetFinish;
	}

	public boolean getFinishState() {
		return isTargetFinish;
	}

	public void setTargetAlarmTime(long time, Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putLong(FiledTargetAlarmTime, time);
		editor.commit();
	}

	public long getTargetAlarmTime(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getLong(FiledTargetAlarmTime,
				19 * 60 * 60 * 1000);
	}

	public void setTargetAlarmOnOff(boolean on, Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();// 获取编辑器
		editor.putBoolean(FiledTargetAlarmOnOff, on);
		editor.commit();
	}

	public boolean getTargetAlarmOn(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				ConfigName, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(FiledTargetAlarmOnOff, false);
	}

	/**
	 * *************************************************************
	 */
	public void addModeListener(final Context context, final TextView view) {
		ModeControl.getInstance(context).setListener(new onModeListener() {

			@Override
			public void onModeChange(int type) {
				int mode = HP_TargetConfig.getInstance().getTargetMode(context);
				int strId = R.string.hp_target_main_dialog_plan_day;
				switch (mode) {
				/*
				 * case 0:// HP_TargetConfig.TargetMode.DEFAULT strId =
				 * R.string.hp_targetmodel; break;
				 */
				case 0:// HP_TargetConfig.TargetMode.DAY
					strId = R.string.hp_target_main_dialog_plan_day;
					break;
				case 1:// HP_TargetConfig.TargetMode.WEEK
					strId = R.string.hp_target_main_dialog_plan_week;
					break;
				}
				String modelValue = "";
				if (HP_TargetConfig.getInstance().getMode(context) == 1) {
					modelValue = context.getResources().getString(strId);
				}
				if (type == HPConst.STRING_TYPE_MODE) {
					if (modelValue.equals(""))
						modelValue = modelValue
								+ HP_TargetConfig.getInstance()
										.getCurTargetStr(context);
				}
				view.setText(modelValue);
			}
		});
	}

	public static List<ModelChangeListener> listeners = new ArrayList<ModelChangeListener>();

	public static void addModelChangeListener(ModelChangeListener listener) {
		listeners.add(listener);
	}

	public static void modelChange() {
		for (ModelChangeListener listener : listeners) {
			listener.onChange();
		}
	}

	public interface ModelChangeListener {
		public void onChange();
	}

	public String getCurTargetStr(Context context) {
		StringBuffer content = new StringBuffer();

		int modevalue = getMode(context);
		if (modevalue == 0) {
			content.append(SystemMethod.getString(context,
					R.string.hp_normalmodel));
		} else {
			// content.append(context.getResources().getString(R.string.hp_setting));
			switch (HP_TargetConfig.getInstance().getTargetSportMode(context)) {
			case 0:
				content.append(HP_TargetConfig.getInstance().getTargetDistance(
						context));
				content.append(context.getResources().getString(
						R.string.hp_main_distance_unit));
				break;
			case 1:
				content.append(SportTrackMain.parserMinute(String
						.valueOf(HP_TargetConfig.getInstance().getTargetTime(
								context) * 1000 * 60)));
				break;
			case 2:
				content.append(HP_TargetConfig.getInstance().getTargetCalorie(
						context));
				content.append(context.getResources().getString(
						R.string.calorie));
				break;
			}
		}
		return content.toString();
	}

	public String getCurTargetNotiStr(Context context) {
		StringBuffer content = new StringBuffer();
		if (getMode(context) == 1) {
			if (!isTargetFinish) {
				HashMap<String, String> doneTarget = TrackUtil.getInstance(
						context).getTaskState();
				content.append(context.getResources().getString(
						R.string.hp_done));
				switch (getTargetSportMode(context)) {
				case 0:
					double distance = getTargetDistance(context)
							* 1000
							- Double.parseDouble(GpsUitl.checkNull(doneTarget
									.get("distance")));
					content.append(GpsUitl.distanceFormat(
							String.valueOf(distance), true, context));
					break;
				case 1:
					long time = getTargetTime(context)
							* 1000
							* 60
							- Long.parseLong(GpsUitl.checkNull(doneTarget
									.get("duration")));
					content.append(GpsUitl.durationTrackFormat(String
							.valueOf(time)));
					break;
				case 2:
					double calorie = getTargetCalorie(context)
							- Double.parseDouble(GpsUitl.checkNull(doneTarget
									.get("calory")));
					content.append(GpsUitl.caloryFormat(
							String.valueOf(calorie), true));
					break;
				}
			}

		}
		return content.toString();
	}

	public String getFinishTargetStr(Context context) {
		StringBuffer content = new StringBuffer();
		content.append("恭喜你\n完成");
		switch (HP_TargetConfig.getInstance().getTargetSportMode(context)) {
		case 0:
			content.append(HP_TargetConfig.getInstance().getTargetDistance(
					context));
			content.append(context.getResources().getString(
					R.string.hp_main_distance_unit));
			break;
		case 1:
			content.append(SportTrackMain.parserMinute(String
					.valueOf(HP_TargetConfig.getInstance().getTargetTime(
							context) * 1000 * 60)));
			break;
		case 2:
			content.append(HP_TargetConfig.getInstance().getTargetCalorie(
					context));
			content.append(context.getResources().getString(R.string.calorie));
			break;
		}
		content.append("\n再接再厉");
		return content.toString();
	}
}
