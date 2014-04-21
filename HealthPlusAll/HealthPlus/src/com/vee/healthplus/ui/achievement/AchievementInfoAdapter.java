package com.vee.healthplus.ui.achievement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.load.TrackLEntity;
import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.sporttrack.SportEntity;
import com.vee.healthplus.util.sporttrack.TrackEntity;
import com.vee.healthplus.util.sporttrack.TrackUtil;

import org.w3c.dom.Text;

public class AchievementInfoAdapter extends BaseAdapter {
    private static final String TAG = "xuxuxu";

    private Context ctx;

    private List<SportEntity> tracks = new ArrayList<SportEntity>();

    protected LayoutInflater _mInflater;

    public AchievementInfoAdapter(Context ctx, List<SportEntity> list) {
        this.ctx = ctx;
        this.tracks = list;
        _mInflater = LayoutInflater.from(this.ctx);
    }

    public int getCount() {
        return tracks.size();
    }

    public Object getItem(int position) {
        return tracks.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = _mInflater.inflate(R.layout.achievement_info_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.infoTime = (TextView) convertView.findViewById(R.id.info_time);
            holder.infoType = (TextView) convertView.findViewById(R.id.info_type);
            holder.infoItemTime = (TextView) convertView.findViewById(R.id.info_item_time);
            holder.infoItemDistance = (TextView) convertView.findViewById(R.id.info_item_distance);
            holder.infoItemCalor = (TextView) convertView.findViewById(R.id.info_item_calor);
            holder.infoItemSpeed = (TextView) convertView.findViewById(R.id.info_item_speed);
            holder.infoItemHeartRate = (TextView) convertView.findViewById(R.id.info_item_heart_rate);
            holder.line = (TextView) convertView.findViewById(R.id.line);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == getCount() - 1) {
            holder.line.setVisibility(View.INVISIBLE);
        } else {
            holder.line.setVisibility(View.VISIBLE);
        }
        SportEntity se = tracks.get(position);
        TrackLEntity te = se.getTrack().get(0);
        long milli = se.getTime() == null ? System.currentTimeMillis() : Long.parseLong(se.getTime());
        Date date = new Date(milli);
        Calendar calen = Calendar.getInstance();
        calen.setTime(date);
        String time = (calen.get(Calendar.MONTH)+1) + "月" + calen.get(Calendar.DATE) + "日 " + calen.get(Calendar.HOUR_OF_DAY) + ":" + calen.get(Calendar.MINUTE);
        holder.infoTime.setText(time);
        holder.icon.setImageResource(HPConst.SPORTTYPE_ICONS_Blue[se.getSportid()]);
//		holder.infoTime.setCompoundDrawablesWithIntrinsicBounds(, null, null, null);
        holder.infoType.setText("普通运动");
        holder.infoItemTime.setText(GpsUitl.durationTrackFormat(te.getDuration()));
        //(double) Math.round(distance * 100) / 100
        holder.infoItemDistance.setText(GpsUitl.distanceFormat(te.getDistance(), false, ctx));
        holder.infoItemCalor.setText(te.getCalory());
        //String.valueOf(Integer.parseInt(te.getDistance())*1000/Integer.parseInt(te.getDuration()))
        holder.infoItemSpeed.setText(GpsUitl.velocityFormat(TrackUtil.getInstance(ctx).getVelocity(te.getDistance(), te.getDuration()), false));
        holder.infoItemHeartRate.setText("0");
        return convertView;
    }

    public class ViewHolder {
        TextView infoTime;

        TextView infoType;

        TextView infoItemTime;

        TextView infoItemDistance;

        TextView infoItemCalor;

        TextView infoItemSpeed;

        TextView infoItemHeartRate;

        TextView line;

        ImageView icon;
    }

}
