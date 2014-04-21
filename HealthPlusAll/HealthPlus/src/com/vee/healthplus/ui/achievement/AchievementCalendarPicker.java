package com.vee.healthplus.ui.achievement;

import java.util.Calendar;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.common.FragmentMsg;
import com.vee.healthplus.common.IFragmentMsg;

public class AchievementCalendarPicker extends Fragment {

	protected static final String TAG = "xuxuxu";

	private IFragmentMsg fragmentListener;

	private WheelView yearWheel;

	private WheelYearAdapter yearAdapter;

	private WheelView monthWheel;

	private WheelMonthAdapter monthAdapter;

	private TextView btPick;

    private Fragment me;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		fragmentListener = (IFragmentMsg) activity;
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        me = this;
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.achievement_calendar_selector, container, false);
		yearWheel = (WheelView) view.findViewById(R.id.date_year);
		yearAdapter = new WheelYearAdapter(getActivity());
		yearWheel.setViewAdapter(yearAdapter);

		monthWheel = (WheelView) view.findViewById(R.id.date_month);
		monthAdapter = new WheelMonthAdapter(getActivity());
		monthWheel.setViewAdapter(monthAdapter);
		btPick = (TextView) view.findViewById(R.id.btPick);
		btPick.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				FragmentMsg fMsg = new FragmentMsg();
				Calendar calen = Calendar.getInstance();
				calen.set(Calendar.YEAR, Integer.parseInt(yearAdapter.getItemText(yearWheel.getCurrentItem()).toString()));
				calen.set(Calendar.MONTH, Integer.parseInt(monthAdapter.getItemText(monthWheel.getCurrentItem()).toString())-1);
				calen.set(Calendar.DATE, 1);
				fMsg.setFlag(FragmentMsg.CALENDAR_PICKER_TO_CALENDAR);
                fMsg.setSrcFragment(me);
				fMsg.setObjFragment(new AchievementCalendar(calen.getTime()));
				fMsg.setPickCalen(calen);
				fragmentListener.replaceFragment(fMsg);
			}
		});

		return view;
	}

	private class WheelYearAdapter extends AbstractWheelTextAdapter {

		private final int BEGIN_YEAR = 2013;

		private String years[];

		protected WheelYearAdapter(Context context) {
			super(context);
			Calendar calen = Calendar.getInstance();
			int curYear = calen.get(Calendar.YEAR);
			years = new String[curYear - BEGIN_YEAR + 1];
			for (int i = 0; i < years.length; i++) {
				years[i] = String.valueOf(BEGIN_YEAR + i);
			}
		}

		@Override
		protected CharSequence getItemText(int index) {
			return years[index];
		}

		@Override
		public int getItemsCount() {
			return years.length;
		}
	}

	private class WheelMonthAdapter extends AbstractWheelTextAdapter {

		private String months[] = new String[12];

		protected WheelMonthAdapter(Context context) {
			super(context);
			for (int i = 0; i < months.length; i++) {
				months[i] = String.valueOf(i + 1);
			}
		}

		@Override
		protected CharSequence getItemText(int index) {
			return months[index];
		}

		@Override
		public int getItemsCount() {
			return months.length;
		}
	}

}
