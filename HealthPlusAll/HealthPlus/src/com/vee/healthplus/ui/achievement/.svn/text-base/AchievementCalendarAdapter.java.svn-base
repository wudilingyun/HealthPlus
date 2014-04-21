package com.vee.healthplus.ui.achievement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;

public class AchievementCalendarAdapter extends BaseAdapter {

    private static final String TAG = "xuxuxu";

    private Calendar mCalen;

    private Context ctx;

    private List<DailyTrack> days = new ArrayList<DailyTrack>();

    protected LayoutInflater _mInflater;

    final private String[] WEEK = {"日", "一", "二", "三", "四", "五", "六"};

    public AchievementCalendarAdapter(Context ctx, Date curDate) {
        this.ctx = ctx;
        mCalen = Calendar.getInstance();
        _mInflater = LayoutInflater.from(this.ctx);
        initDays(curDate);
    }

    private void initDays(Date curDate) {
        // TODO Auto-generated method stub
        mCalen.setTime(curDate);
        mCalen.set(Calendar.DATE, 1);
        mCalen.set(Calendar.HOUR_OF_DAY, 0);
        mCalen.set(Calendar.MINUTE, 0);
        mCalen.set(Calendar.SECOND, 0);

        for (int i = 0; i < WEEK.length; i++) {
            days.add(new DailyTrack(WEEK[i], false));
        }
        int dayOfMonth = mCalen.getActualMaximum(Calendar.DAY_OF_MONTH);
        int dayOfWeek = mCalen.get(Calendar.DAY_OF_WEEK) - 1;
//        Log.i(TAG, "dayOfMonth:" + dayOfMonth);
//        Log.i(TAG, "weekday:" + dayOfWeek);
        for (int i = 0; i < dayOfWeek; i++) {
            days.add(new DailyTrack("", false));
        }
        for (int i = 1; i <= dayOfMonth; i++) {
            // days.add(String.valueOf(i));
            mCalen.set(Calendar.DATE, i);
            days.add(new DailyTrack(String.valueOf(i), mCalen));
        }
        this.notifyDataSetChanged();
    }

    public int getCount() {
        return days.size();
    }

    public Object getItem(int position) {
        return days.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = _mInflater.inflate(R.layout.achievement_grid_item, null);
            holder = new ViewHolder();
            holder.tvDay = (TextView) convertView.findViewById(R.id.tvDay);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setBackgroundResource(R.color.hp_w_header_view_black);
        holder.tvDay.setText(days.get(position).getDate());
        holder.dateInfo = days.get(position);
        if (days.get(position).isHaveTrack())
            holder.tvDay.setBackgroundColor(ctx.getResources().getColor(R.color.yellow));
        if (position % 7 == 0 || position % 7 == 6)
            holder.tvDay.setTextColor(ctx.getResources().getColor(R.color.blue));

        return convertView;
    }

    public class ViewHolder {
        public TextView tvDay;

        public DailyTrack dateInfo;
    }

    public class DailyTrack {

        private String date;

        private boolean haveTrack;

        private long beginTime;

        private long endTime;

        public DailyTrack(String date, boolean b) {
            // TODO Auto-generated constructor stub
            this.date = date;
            this.haveTrack = b;
        }

        public DailyTrack(String date, Calendar calen) {
            // TODO Auto-generated constructor stub
            this.date = date;
            this.beginTime = calen.getTimeInMillis();
            Calendar c = Calendar.getInstance();
            c = calen;
            c.set(Calendar.DATE, calen.get(Calendar.DATE) + 1);
            this.endTime = c.getTimeInMillis();
            this.haveTrack = TrackUtil.getInstance(ctx).isTrack(beginTime, endTime, HP_User.getOnLineUserId(ctx));
//            Log.i(TAG, "DailyTrack:" + calen.getTime() + "," + beginTime + "-" + endTime + "," +
//                    this.haveTrack);
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isHaveTrack() {
            return haveTrack;
        }

        public void setHaveTrack(boolean haveTrack) {
            this.haveTrack = haveTrack;
        }

        public long getBeginTime() {
            return beginTime;
        }

        public long getEndTime() {
            return endTime;
        }
    }

}
