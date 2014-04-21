package com.vee.healthplus.ui.sportmode;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vee.healthplus.R;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.sportmode.ModeControl;
import com.vee.healthplus.widget.CustomDialog;

/**
 * Created by zhou on 13-10-28.
 * 
 * @author zhou 目标计划对话弹窗
 */
public class TargetDialogFragment extends DialogFragment implements
		RadioGroup.OnCheckedChangeListener, AdapterView.OnItemClickListener {

	private RadioGroup radioGroup;
	private RadioButton rb_day, rb_week;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle(R.string.hp_target_modelchoose);
		View layout = View.inflate(getActivity(), R.layout.hp_target_dialog,
				null);
		initView(layout);
		builder.setContentView(layout);
		int mode = HP_TargetConfig.getInstance().getTargetMode(getActivity());
		switch (mode) {
		/*
		 * case 0:// HP_TargetConfig.TargetMode.DEFAULT //
		 * rb_day.setChecked(false); // rb_week.setChecked(false); break;
		 */
		case 0:// HP_TargetConfig.TargetMode.DAY
			rb_day.setChecked(true);
			break;
		case 1:// HP_TargetConfig.TargetMode.WEEK
			rb_week.setChecked(true);
			break;
		}
		ModeControl.getInstance(getActivity()).onChange(
				HPConst.STRING_TYPE_MODE);
		return builder.create();
	}

	private void initView(View layout) {
		radioGroup = (RadioGroup) layout
				.findViewById(R.id.targetRadioGroupCustom);
		rb_day = (RadioButton) layout.findViewById(R.id.daymodel_rb);
		rb_week = (RadioButton) layout.findViewById(R.id.weekmodel_rb);
		radioGroup.setOnCheckedChangeListener(this);
		ListView targetmodel_lv = (ListView) layout
				.findViewById(R.id.targetmodel_lv);
		TargetModel_Adapter adapter = new TargetModel_Adapter(getActivity());
		targetmodel_lv.setAdapter(adapter);
		targetmodel_lv.setOnItemClickListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int i) {
		Log.d("sssssssssssssssssssssss", "onCheckedChanged");
		switch (i) {
		case R.id.daymodel_rb:
			HP_TargetConfig.getInstance().setTargetMode(
					HP_TargetConfig.TargetMode.DAY, getActivity());
			break;
		case R.id.weekmodel_rb:
			HP_TargetConfig.getInstance().setTargetMode(
					HP_TargetConfig.TargetMode.WEEK, getActivity());
			break;
		}
		ModeControl.getInstance(getActivity()).onChange(
				HPConst.STRING_TYPE_MODE);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
		switch (i) {
		case 0:
			TargetDistancePickDialog distancePickDialog = new TargetDistancePickDialog();
			distancePickDialog.show(getFragmentManager(), "");
			break;
		case 1:
			TargetTimePickDialog timePickDialog = new TargetTimePickDialog();
			timePickDialog.show(getFragmentManager(), "");
			break;
		case 2:
			TargetCaloriePickDialog caloriePickDialog = new TargetCaloriePickDialog();
			caloriePickDialog.show(getFragmentManager(), "");
			break;
		}
		dismiss();
	}
}
