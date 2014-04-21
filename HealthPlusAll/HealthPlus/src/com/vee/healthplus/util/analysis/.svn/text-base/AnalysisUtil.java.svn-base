/**
 * 
 */
package com.vee.healthplus.util.analysis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wangfeng
 * 
 */
public class AnalysisUtil {

	public static long getWeekBeginTime(int weekcount) {
		Calendar calendar = Calendar.getInstance();
		// calendar.add(Calendar.DATE, 0 - 6);
		calendar.add(Calendar.DATE, 0 - ((weekcount * 7) + 6));
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(year);
		stringBuffer.append("/");
		if (month < 10) {
			stringBuffer.append("0");
		}
		stringBuffer.append(month);
		stringBuffer.append("/");
		if (day < 10) {
			stringBuffer.append("0");
		}
		stringBuffer.append(day);
		stringBuffer.append(" 00:00:00");
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = null;
		try {
			date = df.parse(stringBuffer.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long beginTime = date.getTime();

		return beginTime;
	}

	public static long getDayBeginTime() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(year);
		stringBuffer.append("/");
		if (month < 10) {
			stringBuffer.append("0");
		}
		stringBuffer.append(month);
		stringBuffer.append("/");
		if (day < 10) {
			stringBuffer.append("0");
		}
		stringBuffer.append(day);
		stringBuffer.append(" 00:00:00");
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = null;
		try {
			date = df.parse(stringBuffer.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long dayBeginTime = date.getTime();
		return dayBeginTime;
	}

	public static String getTimeText(long time, int timeType) {
		Date date = new Date(time);
		SimpleDateFormat formatter;
		switch (timeType) {
		case AnalysisConst.TIME_TYPE_DAY:
			formatter = new SimpleDateFormat("yyyy/MM/dd");
			break;
		case AnalysisConst.TIME_TYPE_SECOND:
			formatter = new SimpleDateFormat("yyyy/MM/dd\nHH:mm:ss");
			break;
		default:
			formatter = new SimpleDateFormat("yyyy/MM/dd\nHH:mm:ss");
			break;
		}
		return formatter.format(date);
	}

	public static String formatTimeStr(int l) {
		int hour = 0;
		int minute = 0;
		int second = 0;

		second = l;
		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}

		StringBuffer timeBuffer = new StringBuffer();
		if (hour > 0) {
			timeBuffer.append(hour);
		} else {
			timeBuffer.append("0");
		}
		timeBuffer.append("°");
		if (minute > 0) {
			timeBuffer.append(minute);
		} else {
			timeBuffer.append("0");
		}
		timeBuffer.append("'");
		if (second > 0) {
			timeBuffer.append(second);
		} else {
			timeBuffer.append("0");
		}
		timeBuffer.append("\"");
		return (timeBuffer.toString());
	}

}
