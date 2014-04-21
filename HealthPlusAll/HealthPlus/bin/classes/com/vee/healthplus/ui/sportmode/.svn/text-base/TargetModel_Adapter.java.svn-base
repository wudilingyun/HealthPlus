package com.vee.healthplus.ui.sportmode;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vee.healthplus.R;


/**
 * Created by wangjiafeng on 13-11-5.
 */
public class TargetModel_Adapter extends BaseAdapter {

    private String[] titles = null;
    private String[] smallTitles = null;
    private Context mContext;

    public TargetModel_Adapter(Context mContext) {
        this.mContext = mContext;
        initTitle(mContext);
    }

    private void initTitle(Context mContext) {
        titles = new String[]{mContext.getResources().getString(R.string.hp_target_modeldistance), mContext.getResources().getString(R.string.hp_target_modeltime), mContext.getResources().getString(R.string.hp_target_modelcalorie)};
        smallTitles = new String[]{mContext.getResources().getString(R.string.hp_target_modeldistancedes), mContext.getResources().getString(R.string.hp_target_modeltimedes), mContext.getResources().getString(R.string.hp_target_modelcaloriedes)};
    }

    @Override
    public int getCount() {
        if (titles != null) {
            return titles.length;
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = View.inflate(mContext, R.layout.hp_w_targetmodel_item, null);
            holder.title_tv = (TextView) view.findViewById(R.id.title_tv);
            holder.titleDes_tv = (TextView) view.findViewById(R.id.titleDes_tv);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.title_tv.setText(titles[i]);
        holder.titleDes_tv.setText(smallTitles[i]);
        return view;
    }

    private final class ViewHolder {
        public TextView title_tv;
        public TextView titleDes_tv;
    }
}
