package com.vee.healthplus.ui.sportmode;

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

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Created by zhou on 13-10-28.
 */
public class TargetTimePickDialog extends DialogFragment implements View.OnClickListener {
    private WheelView time_wv;
    private String[] timeArr = {"10", "20", "30", "40", "50", "60", "70", "80"};
    private NumberPicker htime_np, mtime_np;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle(R.string.hp_target_modeltimedes);
        View layout = View.inflate(getActivity(), R.layout.hp_target_dialog_time, null);
        initView(layout);
        builder.setContentView(layout);
        return builder.create();
    }

    private void initView(View layout) {
        time_wv = (WheelView) layout.findViewById(R.id.time_wv);
        time_wv.setViewAdapter(new Time_Adapter(getActivity()));
        htime_np = (NumberPicker) layout.findViewById(R.id.htime_np);
        htime_np.setMinValue(0);
        htime_np.setUnitText(getResources().getString(R.string.hour));
        mtime_np = (NumberPicker) layout.findViewById(R.id.mtime_np);
        mtime_np.setMinValue(0);
        mtime_np.setUnitText(getResources().getString(R.string.minute));
        Button setDistanceTarget_btn = (Button) layout.findViewById(R.id.setTimeTarget_btn);
        setDistanceTarget_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setTimeTarget_btn:
                HP_TargetConfig.getInstance().setTargetCalorie(0, getActivity());
                HP_TargetConfig.getInstance().setTargetTime(0, getActivity());
                HP_TargetConfig.getInstance().setTargetDistance(0, getActivity());
                HP_TargetConfig.getInstance().setTargetSportMode(HP_TargetConfig.TargetSportMode.TIME, getActivity());
                try {
                    HP_TargetConfig.getInstance().setTargetTime(htime_np.getCurValue() * 60 + mtime_np.getCurValue(), getActivity());
                } catch (Exception ex) {
                    Toast.makeText(getActivity(), "输入值不合法！", Toast.LENGTH_SHORT).show();
                    break;
                }
                HP_TargetConfig.getInstance().setTargetUpdateTime(System.currentTimeMillis(), getActivity());
                ModeControl.getInstance(getActivity()).onChange(HPConst.STRING_TYPE_MODE);
                HP_TargetConfig.modelChange();
                HP_TargetConfig.getInstance().setFinishState(false);
                this.dismiss();
                break;
        }
    }

    private class Time_Adapter extends AbstractWheelTextAdapter {

        protected Time_Adapter(Context context) {
            super(context);
        }

        @Override
        protected CharSequence getItemText(int index) {
            return timeArr[index];
        }

        @Override
        public int getItemsCount() {
            return timeArr.length;
        }
    }
}
