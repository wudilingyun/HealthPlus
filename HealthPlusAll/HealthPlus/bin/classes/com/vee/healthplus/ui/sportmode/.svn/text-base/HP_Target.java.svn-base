package com.vee.healthplus.ui.sportmode;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.vee.healthplus.R;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;

/**
 * Created by zhou on 13-10-28.
 * 
 * @author zhou 任务目标模式
 */
public class HP_Target extends FragmentActivity implements
		RadioGroup.OnCheckedChangeListener, View.OnClickListener {

	private RadioButton rb_default, rb_custom;
	private RadioGroup mRadioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hp_target_main);
		initView();
	}

	private void initView() {
		mRadioGroup = (RadioGroup) findViewById(R.id.targetRadioGroup);
		rb_default = (RadioButton) findViewById(R.id.radioButton_default);
		rb_custom = (RadioButton) findViewById(R.id.radioButton_custom);
		// rb_default.setOnClickListener(this);
		rb_custom.setOnClickListener(this);

		mRadioGroup.setOnCheckedChangeListener(this);
		int mode = HP_TargetConfig.getInstance().getTargetMode(this);
		switch (mode) {
		case 0:// HP_TargetConfig.TargetMode.DEFAULT
			rb_default.setChecked(true);
			break;
		case 1:// HP_TargetConfig.TargetMode.DAY
		case 2:// HP_TargetConfig.TargetMode.WEEK
			rb_custom.setChecked(true);
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup radioGroup, int id) {
		switch (radioGroup.getId()) {
		case R.id.targetRadioGroup:
			switch (id) {
			case R.id.radioButton_default:
				// HP_TargetConfig.getInstance().setTargetMode(HP_TargetConfig.TargetMode.DEFAULT,
				// this);
				break;
			case R.id.radioButton_custom:

				break;
			}
			break;
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.radioButton_default:

			break;
		case R.id.radioButton_custom:
			TargetDialogFragment dialogFragment = new TargetDialogFragment();
			dialogFragment.show(getSupportFragmentManager(), "");
			break;
		}
	}
}
