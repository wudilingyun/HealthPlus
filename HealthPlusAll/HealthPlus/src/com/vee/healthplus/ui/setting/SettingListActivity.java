package com.vee.healthplus.ui.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.ui.user.UserInfoEdit;
import com.vee.healthplus.ui.user.UserLogin_Activity;
import com.vee.healthplus.util.VersionUtils;
import com.vee.healthplus.util.alarm.AlarmOberver;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.ICallBack;

public class SettingListActivity extends android.support.v4.app.ListFragment
        implements OnClickListener, ICallBack {

    class SimpleAdapter extends ArrayAdapter<Item> {

        public SimpleAdapter(Context context, int resource) {
            super(context, resource);
            int sectionPosition = 0, listPosition = 0;
            Item item = new Item(Item.SECTION, getContext().getResources()
                    .getString(R.string.user_txt), sectionPosition,
                    listPosition++);
            add(item);
            int userid = HP_User.getOnLineUserId(getActivity());
            if (userid != 0) {
                HP_User user = HP_DBModel.getInstance(getActivity())
                        .queryUserInfoByUserId(userid, true);
                item = new Item(Item.TAIL, user.userName,
                        R.drawable.setting_unregist, 0, sectionPosition,
                        listPosition++);
            } else {
                item = new Item(Item.TAIL, getContext().getResources()
                        .getString(R.string.setting_unregist),
                        R.drawable.setting_unregist, 0, sectionPosition,
                        listPosition++);
            }
            add(item);

            ++sectionPosition;
            listPosition = 0;
            item = new Item(Item.SECTION, "运动", sectionPosition, listPosition++);
            add(item);
            // item = new Item(Item.ITEM, "成就", R.drawable.setting_achievement,
            // 0, sectionPosition, listPosition++);
            // add(item);
            item = new Item(Item.TAIL, "目标提醒时间", R.drawable.setting_alarm,
                    R.drawable.target_alarm_on, sectionPosition, listPosition++);
            add(item);

            ++sectionPosition;
            listPosition = 0;
            item = new Item(Item.SECTION, "产品相关", sectionPosition,
                    listPosition++);
            add(item);
            item = new Item(Item.ITEM, "邀请好友", R.drawable.setting_invite, 0,
                    sectionPosition, listPosition++);
            add(item);
            item = new Item(Item.ITEM, "创建快捷方式", R.drawable.setting_shortcut,
                    0, sectionPosition, listPosition++);
            add(item);
            item = new Item(Item.TAIL, "检查更新", R.drawable.setting_update, 0,
                    sectionPosition, listPosition++);
            add(item);
            item = new Item(Item.ITEM, "用户反馈", R.drawable.setting_feedback, 0,
                    sectionPosition, listPosition++);
            add(item);
            item = new Item(Item.TAIL, "关于", R.drawable.setting_about, 0,
                    sectionPosition, listPosition++);
            add(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            Item item = getItem(position);
            if (convertView == null) {
                if (item.type == Item.SECTION) {
                    convertView = getActivity().getLayoutInflater().inflate(
                            R.layout.setting_list_item_section, null);
                    holder = new ViewHolder();
                    holder.name = (TextView) convertView
                            .findViewById(R.id.section_title);
                    holder.line = (TextView) convertView
                            .findViewById(R.id.line);
                    convertView.setTag(holder);
                } else {
                    convertView = getActivity().getLayoutInflater().inflate(
                            R.layout.setting_list_item, null);
                    holder = new ViewHolder();
                    holder.icon = (ImageView) convertView
                            .findViewById(R.id.setting_image);
                    holder.name = (TextView) convertView
                            .findViewById(R.id.tvItemName);
                    holder.alarmOnOff = (ImageView) convertView
                            .findViewById(R.id.alarmOnOff);
                    holder.line = (TextView) convertView
                            .findViewById(R.id.line);
                    convertView.setTag(holder);
                }

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (item.type == Item.SECTION) {
                holder.name.setText(item.text);
                return convertView;
            }

            if (item.type == Item.TAIL) {
                holder.line.setVisibility(View.GONE);
            }

            holder.icon.setImageResource(item.leftIconRes);
            holder.name.setText(item.text);
            if (item.rightIconRes != 0) {
                if (HP_TargetConfig.getInstance().getTargetAlarmOn(
                        getActivity())) {
                    holder.alarmOnOff
                            .setImageResource(R.drawable.target_alarm_on);
                } else {
                    holder.alarmOnOff
                            .setImageResource(R.drawable.target_alarm_off);
                }
                holder.alarmOnOff.setVisibility(View.VISIBLE);
                holder.alarmOnOff
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (HP_TargetConfig.getInstance()
                                        .getTargetAlarmOn(getActivity())) {
                                    HP_TargetConfig.getInstance()
                                            .setTargetAlarmOnOff(false,
                                                    getActivity());
                                    holder.alarmOnOff
                                            .setImageResource(R.drawable.target_alarm_off);
                                } else {
                                    HP_TargetConfig.getInstance()
                                            .setTargetAlarmOnOff(true,
                                                    getActivity());
                                    holder.alarmOnOff
                                            .setImageResource(R.drawable.target_alarm_on);
                                }
                                AlarmOberver.getInstance(getActivity())
                                        .onChange();
                            }
                        });
            }

            return convertView;
        }

        @Override
        public int getViewTypeCount() {
            return 3;
        }

        @Override
        public boolean isEnabled(int position) {
            if (getItem(position).type == Item.SECTION)
                return false;
            else
                return super.isEnabled(position);
        }

        @Override
        public int getItemViewType(int position) {
            return getItem(position).type;
        }
    }

    static class Item {

        public static final int ITEM = 0;

        public static final int SECTION = 1;

        public static final int TAIL = 2;

        public final int type;

        public String text;

        public int leftIconRes;

        public int rightIconRes;

        public int sectionPosition;

        public int listPosition;

        public Item(int type, String text, int sectionPosition, int listPosition) {
            this.type = type;
            this.text = text;
            this.sectionPosition = sectionPosition;
            this.listPosition = listPosition;
        }

        public Item(int type, String text, int leftIconRes, int rightIconRes,
                    int sectionPos, int listPos) {
            this.type = type;
            this.text = text;
            this.leftIconRes = leftIconRes;
            this.rightIconRes = rightIconRes;
            this.sectionPosition = sectionPos;
            this.listPosition = listPos;
        }

        @Override
        public String toString() {
            return text;
        }

    }

    public class ViewHolder {
        ImageView icon;

        TextView name;

        ImageView alarmOnOff;

        TextView line;
    }

    private boolean hasHeaderAndFooter;

    private boolean isFastScroll;

    private boolean addPadding;

    private boolean isShadowVisible = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.setting_list, container, false);
        if (savedInstanceState != null) {
            isFastScroll = savedInstanceState.getBoolean("isFastScroll");
            addPadding = savedInstanceState.getBoolean("addPadding");
            isShadowVisible = savedInstanceState.getBoolean("isShadowVisible");
            hasHeaderAndFooter = savedInstanceState
                    .getBoolean("hasHeaderAndFooter");
        }
        return view;
    }

    @Override
    public void onResume() {
        initializeAdapter();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isFastScroll", isFastScroll);
        outState.putBoolean("addPadding", addPadding);
        outState.putBoolean("isShadowVisible", isShadowVisible);
        outState.putBoolean("hasHeaderAndFooter", hasHeaderAndFooter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        switch (position) {
            case 1:
                int userid = HP_User.getOnLineUserId(getActivity());
                if (userid != 0)
                    startActivity(new Intent(getActivity(), UserInfoEdit.class));
                else
                    new UserLogin_Activity(SettingListActivity.this).show(
                            getFragmentManager(), "");
                break;
            case 3:
                TargetAlarmTimePickDialog timePickDialog = new TargetAlarmTimePickDialog();
                timePickDialog.show(getFragmentManager(), "");
                break;
            case 5:
                String sendMsg = getResources().getString(R.string.hp_share_invite);
                MyApplication.shareBySystem(getActivity(), sendMsg, "", "", "", "");
                break;
            case 6:
                MyApplication.createMyGameShortCut(getActivity());
                Toast.makeText(this.getActivity(),"快捷方式创建成功",Toast.LENGTH_SHORT).show();
                break;
            case 7:
                VersionUtils.getInstance().checkVersion(
                        getActivity(), false);
                break;
            case 8:
                Intent intent = new Intent(getActivity(), SettingFeedBack.class);
                startActivity(intent);
                break;
            case 9:
                Intent intent1 = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @SuppressLint("NewApi")
    private void initializeAdapter() {
        setListAdapter(new SimpleAdapter(getActivity(),
                R.layout.setting_list_item_bak));
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(getActivity(), "Item: " + v.getTag(), Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onChange() {
        initLoginStat();
    }

    @Override
    public void onCancel() {

    }

    private void initLoginStat() {
        int userid = HP_User.getOnLineUserId(getActivity());
        if (userid == 0) {
            setUnlogin();
        } else {
            setLogin(userid);
        }
    }

    public void setLogin(int userId) {
    	
        HP_User user = HP_DBModel.getInstance(getActivity())
                .queryUserInfoByUserId(userId, true);
        ((Item) getListAdapter().getItem(1)).text = user.userName;
       // this.notify();
    }

    public void setUnlogin() {
        ((Item) getListAdapter().getItem(1)).text = this.getResources()
                .getString(R.string.setting_unregist);
        this.notify();
    }

}