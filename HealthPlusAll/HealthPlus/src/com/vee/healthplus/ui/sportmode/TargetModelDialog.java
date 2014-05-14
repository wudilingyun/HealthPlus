package com.vee.healthplus.ui.sportmode;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.sportmode.ModeControl;
import com.vee.healthplus.widget.CustomDialog;

/**
 * Created by zhou on 13-10-28.
 */
public class TargetModelDialog extends DialogFragment implements
		AdapterView.OnItemClickListener, HP_TargetConfig.ModelChangeListener {

	private TargetModelAdapter adapter;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle(R.string.hp_sportmodel);
	//	builder.setTitleBG(getResources().getColor(android.R.color.transparent));
		/*builder.setViewBG(getResources().getDrawable(
				R.drawable.hp_main_popmenu_bg));*/
		/*builder.setTitleColor(getResources()
				.getColor(R.color.hp_w_target_title));*/
		View layout = View.inflate(getActivity(),
				R.layout.hp_targetmodel_dialog, null);
		initView(layout);
		builder.setContentView(layout);
		HP_TargetConfig.addModelChangeListener(this);
		return builder.create();
	}

	private void initView(View layout) {
		ListView contentView = (ListView) layout
				.findViewById(R.id.popupmenu_content);
		adapter = new TargetModelAdapter(getActivity());
		contentView.setAdapter(adapter);
		contentView.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> arg0, View view, int location,
			long arg3) {
		HP_TargetConfig.getInstance().setMode(location, getActivity());
		switch (location) {
		case 0:
			ModeControl.getInstance(getActivity()).onChange(
					HPConst.STRING_TYPE_MODE);
			break;
		case 1:
			TargetDialogFragment dialogFragment = new TargetDialogFragment();
			dialogFragment.show(getFragmentManager(), "");
			break;
		}
		adapter.notifyDataSetChanged();
		dismiss();
	}

	@Override
	public void onChange() {
		adapter.notifyDataSetChanged();
	}

	private class TargetModelAdapter extends BaseAdapter {

		private Context mContext;
		private String[] titles = null;

		public TargetModelAdapter(Context mContext) {
			this.mContext = mContext;
			this.titles = new String[] {
					mContext.getResources().getString(R.string.hp_normalmodel),
					mContext.getResources().getString(R.string.hp_targetmodel) };
		}

		@Override
		public int getCount() {
			return titles.length;
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
				view = View.inflate(mContext,
						R.layout.hp_targetmodeldialog_item, null);
				holder.titleName_tv = (TextView) view
						.findViewById(R.id.titleName_tv);
				holder.titleDes_tv = (TextView) view
						.findViewById(R.id.titleDes_tv);
				holder.isCheck_iv = (ImageView) view
						.findViewById(R.id.isCheck_iv);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.titleName_tv.setText(titles[i]);
			int mode = HP_TargetConfig.getInstance().getTargetMode(mContext);
			int strId = R.string.hp_target_main_dialog_plan_day;
			switch (mode) {
			/*
			 * case 0://HP_TargetConfig.TargetMode.DEFAULT break;
			 */
			case 0:// HP_TargetConfig.TargetMode.DAY
				strId = R.string.hp_target_main_dialog_plan_day;
				break;
			case 1:// HP_TargetConfig.TargetMode.WEEK
				strId = R.string.hp_target_main_dialog_plan_week;
				break;
			}
			String modelValue = "";
			if (HP_TargetConfig.getInstance().getMode(mContext) == 1) {
				modelValue = HP_TargetConfig.getInstance().getCurTargetStr(
						mContext);
			}
			holder.titleDes_tv.setText(mContext.getResources().getString(strId)
					+ modelValue);
			if (HP_TargetConfig.getInstance().getMode(mContext) == i) {
				holder.isCheck_iv.setVisibility(View.VISIBLE);
			} else {
				holder.isCheck_iv.setVisibility(View.GONE);
			}
			if (i == 0) {
				holder.titleDes_tv.setVisibility(View.GONE);
			} else {
				holder.titleDes_tv.setVisibility(View.VISIBLE);
			}
			return view;
		}
	}

	private final class ViewHolder {
		private TextView titleName_tv;
		private TextView titleDes_tv;
		private ImageView isCheck_iv;
	}
}
