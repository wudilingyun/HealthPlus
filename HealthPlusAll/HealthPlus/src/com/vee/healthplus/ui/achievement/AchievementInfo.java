package com.vee.healthplus.ui.achievement;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.vee.healthplus.R;
import com.vee.healthplus.common.FragmentMsg;
import com.vee.healthplus.ui.achievement.AchievementCalendarAdapter.DailyTrack;
import com.vee.healthplus.util.sporttrack.HealthDBConst;
import com.vee.healthplus.util.sporttrack.SportEntity;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;

import static com.vee.healthplus.ui.achievement.AchievementInfoAdapter.*;

public class AchievementInfo extends Fragment {
    private static final String TAG = "xuxuxu";

    protected View view;

    private Achievement activity;

    private AchievementCalendarAdapter.DailyTrack dt;

    private GestureDetector flingDetector;

    private Fragment me;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        me = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.achievement_info, container, false);
        final ListView list = (ListView) view.findViewById(R.id.info_list);
        final List<SportEntity> tracks = TrackUtil.getInstance(getActivity()).getTrackListByDate(dt.getBeginTime(), dt.getEndTime(), HP_User.getOnLineUserId(getActivity()), HealthDBConst.SPORTRECORD_TABLE);
        if (tracks != null) {
            AchievementInfoAdapter ai = new AchievementInfoAdapter(this.getActivity(), tracks);
            View view = View.inflate(this.getActivity(), R.layout.achievement_info_foot, null);
            LinearLayout ll = (LinearLayout)view.findViewById(R.id.foot);
            ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    return;
                }
            });
            list.addFooterView(view);
            list.setFooterDividersEnabled(false);
            list.setAdapter(ai);
            list.setOnTouchListener(touchListener);
        }
        flingDetector = new GestureDetector(gestureListener);
        flingDetector.setIsLongpressEnabled(false);
        list.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                activity.mMapView.getOverlays().clear();
                activity.drawTrack(TrackUtil.getInstance(getActivity()).getRecordById(tracks.get(position).getId(), HP_User.getOnLineUserId(getActivity()), HealthDBConst.SPORTRECORD_TABLE));
            }
        });
//        Log.i(TAG, "Time:" + new Date(dt.getBeginTime()) + ":" + new Date(dt.getEndTime()));
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        this.activity = (Achievement) activity;
        dt = (DailyTrack) (this.activity).fragmentArgs.getDailyTrack();
        super.onAttach(activity);
    }

    OnTouchListener touchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            return flingDetector.onTouchEvent(event);
        }
    };

    OnGestureListener gestureListener = new OnGestureListener() {

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            // TODO Auto-generated method stub
            if(e1==null || e2==null){
                return false;
            }
            if (e2.getX() - e1.getX() < 10 && Math.abs(e2.getY()-e1.getY()) < 50) {
                FragmentMsg fMsg = new FragmentMsg();
                fMsg.setSrcFragment(me);
                fMsg.setObjFragment(new AchievementCalendar());
                fMsg.setAnimIn(R.anim.slide_right_in);
                fMsg.setAnimOut(R.anim.slide_right_out);
                activity.replaceFragment(fMsg);
                return true;
            } else
                return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return false;
        }
    };
}
