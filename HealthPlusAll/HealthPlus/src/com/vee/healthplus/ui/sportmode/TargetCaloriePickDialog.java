package com.vee.healthplus.ui.sportmode;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.sportmode.ModeControl;
import com.vee.healthplus.widget.CustomDialog;
import com.vee.healthplus.widget.NumberPicker;

/**
 * Created by zhou on 13-10-28.
 */
public class TargetCaloriePickDialog extends DialogFragment implements
		View.OnClickListener {
	private WheelView calorie_wv;
	private String[] calorieArr = { "100", "200", "300", "400", "500", "600",
			"700", "800" };
	private NumberPicker calorie_np;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
		builder.setTitle(R.string.hp_target_modelcaloriedes);
		View layout = View.inflate(getActivity(),
				R.layout.hp_target_dialog_calorie, null);
		initView(layout);
		builder.setContentView(layout);
		return builder.create();
	}

	private void initView(View layout) {
		calorie_wv = (WheelView) layout.findViewById(R.id.calorie_wv);
		calorie_wv.setViewAdapter(new Calorie_Adapter(getActivity()));
		calorie_np = (NumberPicker) layout.findViewById(R.id.calorie_np);
		calorie_np.setMinValue(HPConst.MIN_CALORIE_VALUE);
		calorie_np.setUnitText(getResources().getString(R.string.calorie));
		Button setcalorieTarget_btn = (Button) layout
				.findViewById(R.id.setCalorieTarget_btn);
		setcalorieTarget_btn.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.setCalorieTarget_btn:
			HP_TargetConfig.getInstance().setTargetCalorie(0, getActivity());
			HP_TargetConfig.getInstance().setTargetTime(0, getActivity());
			HP_TargetConfig.getInstance().setTargetDistance(0, getActivity());
			HP_TargetConfig.getInstance().setTargetSportMode(
					HP_TargetConfig.TargetSportMode.CALORIE, getActivity());
			try {
				HP_TargetConfig.getInstance().setTargetCalorie(
						calorie_np.getCurValue(), getActivity());
			} catch (Exception ex) {
				Toast.makeText(getActivity(), "输入值不合法！", Toast.LENGTH_SHORT)
						.show();
				break;
			}
			HP_TargetConfig.getInstance().setTargetUpdateTime(
					System.currentTimeMillis(), getActivity());
			ModeControl.getInstance(getActivity()).onChange(
					HPConst.STRING_TYPE_MODE);
			HP_TargetConfig.modelChange();
			HP_TargetConfig.getInstance().setFinishState(false);
			this.dismiss();
			break;
		}
	}

	private class Calorie_Adapter extends AbstractWheelTextAdapter {

		protected Calorie_Adapter(Context context) {
			super(context);
		}

		@Override
		protected CharSequence getItemText(int index) {
			return calorieArr[index];
		}

		@Override
		public int getItemsCount() {
			return calorieArr.length;
		}
	}
}
