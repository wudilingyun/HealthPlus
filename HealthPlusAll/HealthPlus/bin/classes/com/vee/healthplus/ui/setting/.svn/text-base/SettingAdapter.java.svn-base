package com.vee.healthplus.ui.setting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.achievement.Achievement;
import com.vee.healthplus.util.alarm.AlarmOberver;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;

public class SettingAdapter extends BaseAdapter {
//	private static final String TAG = "xuxuxu";

    private Context ctx;

    private String itemName[] = new String[]{"成就", "提醒时间设置", "邀请好友", "创建快捷方式", /*"检查更新",*/ "用户反馈", "关于"};

    private String itemDesc[] = new String[]{"运动的成绩及运动记录", "暂未设置目标提醒", "邀请好友一起运动", "在桌面创建快捷方式", /*"检查是否有新版本",*/ "您有任何意见都可以在这里反馈", "当前版本号"};

    private int itemIcon[] = new int[]{R.drawable.setting_achievement, R.drawable.setting_alarm, R.drawable.setting_invite, R.drawable.setting_shortcut, /*R.drawable.setting_update,*/ R.drawable.setting_feedback, R.drawable.setting_about};

    @SuppressWarnings("rawtypes")
    public static Class clazz[] = new Class[]{Achievement.class, Achievement.class, /*Achievement.class,*/ Achievement.class, Achievement.class, SettingFeedBack.class, AboutActivity.class};

    protected LayoutInflater _mInflater;

    public SettingAdapter(Context ctx) {
        this.ctx = ctx;
        _mInflater = LayoutInflater.from(this.ctx);
        long time = HP_TargetConfig.getInstance().getTargetAlarmTime(ctx);
        int hour = (int) time / 1000 / 60 / 60;
        int minute = (int) time / 1000 / 60 % 60;
        setAlarmTimeDesc(hour + ":" + minute);
    }

    public void notifyDataChange(){
        this.notifyDataSetChanged();
    }

    public void setAlarmTimeDesc(String desc) {
        itemDesc[1] = "目标提醒时间为" + desc;
        notifyDataChange();
    }

    public int getCount() {
        return itemName.length;
    }

    public Object getItem(int position) {
        return itemName[position];
    }

    public long getItemId(int position) {
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = _mInflater.inflate(R.layout.setting_list_item_bak, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.setting_image);
            holder.name = (TextView) convertView.findViewById(R.id.tvItemName);
            holder.desc = (TextView) convertView.findViewById(R.id.tvItemDesc);
            holder.alarmOnOff = (ImageView) convertView.findViewById(R.id.alarmOnOff);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setImageResource(itemIcon[position]);
        holder.name.setText(itemName[position]);
        holder.desc.setText(itemDesc[position]);
        if (position == 1) {
            if (HP_TargetConfig.getInstance().getTargetAlarmOn(ctx)) {
                holder.alarmOnOff.setImageResource(R.drawable.target_alarm_on);
            } else {
                holder.alarmOnOff.setImageResource(R.drawable.target_alarm_off);
            }
            holder.alarmOnOff.setVisibility(View.VISIBLE);
            holder.alarmOnOff.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (HP_TargetConfig.getInstance().getTargetAlarmOn(ctx)) {
                        HP_TargetConfig.getInstance().setTargetAlarmOnOff(false, ctx);
                        holder.alarmOnOff.setImageResource(R.drawable.target_alarm_off);
                    } else {
                        HP_TargetConfig.getInstance().setTargetAlarmOnOff(true, ctx);
                        holder.alarmOnOff.setImageResource(R.drawable.target_alarm_on);
                    }
                  AlarmOberver.getInstance(ctx).onChange();
                }
            });
        }

        return convertView;
    }

    public class ViewHolder {
        ImageView icon;

        TextView name;

        TextView desc;

        ImageView alarmOnOff;
    }

}
