package com.vee.healthplus.ui.heahth_exam;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.ui.heahth_heart.HeartRateActivity;
import com.vee.healthplus.ui.heahth_news.Health_ValuableBookActivity;

/**
 * Created by xujizhe on 13-12-25.
 */
public class HealthMainAdapter extends BaseAdapter {

    protected LayoutInflater _mInflater;
    private static Context ctx;

    private int icon[] = {R.drawable.hm_xinlv, R.drawable.hm_xueyang, R.drawable.hm_zhifang, R.drawable.hm_tiwen, R.drawable.hm_zixun, R.drawable.hm_ceshi};
    private static int[] name = {R.string.xinlv, R.string.xueyang, R.string.zhifang, R.string.tiwen, R.string.zixun, R.string.ceshi};
    private Class target[] = {HeartRateActivity.class, null, null, null, Health_ValuableBookActivity.class, ExamTypeActivity.class};
    private int selectItem = 0;

    public HealthMainAdapter(Context ctx) {
        this.ctx = ctx;
        _mInflater = LayoutInflater.from(this.ctx);
    }

    @Override
    public int getCount() {
        return name.length;
    }

    @Override
    public Object getItem(int i) {
        return ctx.getResources().getString(name[i]);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            view = _mInflater.inflate(R.layout.health_main_item, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) view.findViewById(R.id.item_icon);
            holder.name = (TextView) view.findViewById(R.id.item_name);
            holder.lltestItem = (LinearLayout) view.findViewById(R.id.ll_test_item);
         //   holder.rl = (RelativeLayout) view.findViewById(R.id.rl);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.icon.setImageResource(icon[i]);
        holder.name.setText(ctx.getResources().getString(name[i]));
        if (i == selectItem) {
            holder.lltestItem.setBackgroundResource(R.drawable.test_item_selector_selected);
        } else {
            holder.lltestItem.setBackgroundResource(R.drawable.test_item_selector);
        }
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(dm);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(dm.widthPixels/3,dm.widthPixels/3);
        holder.rl.setLayoutParams(lp);
        return view;
    }

    public Class getTargetClass(int pos) {
        return target[pos];
    }

    public void setSelectItem(int pos) {
        this.selectItem = pos;
        notifyDataSetChanged();
    }

    public static String getTargetTitle(int pos){
        return ctx.getResources().getString(name[pos]);
    }

    class ViewHolder {
        public ImageView icon;
        public TextView name;
        public LinearLayout lltestItem;
        public RelativeLayout rl;
    }
}
