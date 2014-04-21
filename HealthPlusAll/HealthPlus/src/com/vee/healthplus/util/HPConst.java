package com.vee.healthplus.util;

import com.vee.healthplus.R;

public class HPConst {
	public static final int SPORT_TYPE_RUN = 0;
	public static final int SPORT_TYPE_BIKE = 1;
	public static final int SPORT_TYPE_WALK = 2;

	public static final int STRING_TYPE_MODE = 0;
	public static final int STRING_TYPE_SRPOT = STRING_TYPE_MODE + 1;

	public static final int PEDO_SENSITIVITY_INDEX = 1; // more small more
														// sensivity

	public static final Integer[] SPORTTYPE_ICONS = {
			R.drawable.hp_target_rungray, R.drawable.hp_target_bikegray,
			R.drawable.hp_target_walkgray };
	public static final Integer[] SPORTTYPE_ICONS_Blue = {
			R.drawable.hp_target_rungray_blue,
			R.drawable.hp_target_bikegray_blue,
			R.drawable.hp_target_walkgray_blue };

	public static final Integer[] SPORTTYPE_ICONS_MAIN = {
			R.drawable.main_run_imge, R.drawable.main_bike_imge,
			R.drawable.main_walk_imge };

	public static final int SPORT_INDEX_SYNC_NOT = 0;
	public static final int SPORT_INDEX_SYNC_YES = SPORT_INDEX_SYNC_NOT + 1;

	public static final String SPORT_GPS_RECEIVER_ACTION = "com.vee.healthplus.service.action.SPORT_GPS_RECEIVER_ACTION";
	public static final String SPORT_PEDO_RECEIVER_ACTION = "com.vee.healthplus.service.action.SPORT_PEDO_RECEIVER_ACTION";

	public static final int CHART_TYPE_DISTANCE = 0;
	public static final int CHART_TYPE_CALORY = 1;
	public static final int CHART_TYPE_DURATION = 2;
	public static final int CHART_TYPE_HEART = 3;
	public static final int CHART_TYPE_BLOOD = 4;

	public static final int MIN_CALORIE_VALUE = 1;
}
