package com.vee.healthplus.ui.achievement;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.vee.healthplus.R;
import com.vee.healthplus.common.FragmentMsg;
import com.vee.healthplus.common.IFragmentMsg;
import com.vee.healthplus.ui.achievement.AchievementCalendarAdapter.DailyTrack;
import com.vee.healthplus.ui.achievement.AchievementCalendarAdapter.ViewHolder;

public class AchievementCalendar extends Fragment {

	IFragmentMsg fragmentHandle = null;

	private Date date;

    private Fragment me;

	public AchievementCalendar() {
		date = new Date(System.currentTimeMillis());
        me = this;
	}

	public AchievementCalendar(Date date) {
		if (date != null)
			this.date = date;
		else
			date = new Date(System.currentTimeMillis());
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.achievement_calendar, container, false);
		GridView grid = (GridView) view.findViewById(R.id.calendar);
		grid.setAdapter(new AchievementCalendarAdapter(getActivity(), date));
		grid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				DailyTrack dateInfo = ((ViewHolder) view.getTag()).dateInfo;
				if (fragmentHandle != null && dateInfo.isHaveTrack()) {
					FragmentMsg fMsg = new FragmentMsg();
					fMsg.setFlag(FragmentMsg.CALENDER_TO_INFO);
					fMsg.setObjFragment(new AchievementInfo());
                    fMsg.setSrcFragment(me);
					fMsg.setDailyTrack(dateInfo);
					fMsg.setAnimIn(R.anim.slide_left_in);
					fMsg.setAnimOut(R.anim.slide_left_out);
					fragmentHandle.replaceFragment(fMsg);
				}
			}
		});
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		try {
			fragmentHandle = (IFragmentMsg) activity;
		} catch (Exception e) {
			// TODO: handle exception
			// throw new ClassCastException(activity.toString() +
			// "must implement IFragmentMsg");
		}
		super.onAttach(activity);
	}

}
