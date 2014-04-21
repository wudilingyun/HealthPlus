package com.vee.healthplus.ui.setting;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.vee.healthplus.R;
import com.vee.healthplus.util.alarm.AlarmOberver;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.widget.CustomDialog;
import com.vee.healthplus.widget.NumberPicker;

/**
 * Created by zhou on 13-10-28.
 */
public class TargetAlarmTimePickDialog extends DialogFragment implements View.OnClickListener {
    private NumberPicker htime_np, mtime_np;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle(R.string.hp_target_alarm_time_dialog_title);
        View layout = View.inflate(getActivity(), R.layout.hp_target_alarm_time_dialog, null);
        initView(layout);
        builder.setContentView(layout);
        return builder.create();
    }

    private void initView(View layout) {
        htime_np = (NumberPicker) layout.findViewById(R.id.target_alarm_htime_np);
        htime_np.setMinValue(0);
        htime_np.setUnitText(getResources().getString(R.string.hour));
        mtime_np = (NumberPicker) layout.findViewById(R.id.target_alarm_mtime_np);
        mtime_np.setMinValue(0);
        mtime_np.setUnitText(getResources().getString(R.string.minute));
        long time = HP_TargetConfig.getInstance().getTargetAlarmTime(this.getActivity());
        if (time != 0) {
            int hour = (int) time / 1000 / 60 / 60;
            int minute = (int) time / 1000 / 60 % 60;
            htime_np.setCurValue(hour);
            mtime_np.setCurValue(minute);
        }
        Button setDistanceTarget_btn = (Button) layout.findViewById(R.id.btnSetAlarmTime);
        setDistanceTarget_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSetAlarmTime:
                if (htime_np.getCurValue() > 24 || htime_np.getCurValue() < 0 || mtime_np.getCurValue() > 60 || mtime_np.getCurValue() < 0) {
                    Toast.makeText(getActivity(), "请输入正确时间！", Toast.LENGTH_SHORT).show();
                    return;
                }
                long time = (htime_np.getCurValue() * 60 * 60 + mtime_np.getCurValue() * 60) * 1000;
                HP_TargetConfig.getInstance().setTargetAlarmTime(time, getActivity());
                HP_TargetConfig.getInstance().setTargetAlarmOnOff(true, getActivity());
//                ((Setting) getActivity()).setAlarmTimeDesc(htime_np.getCurValue() + ":" + mtime_np.getCurValue());
                AlarmOberver.getInstance(getActivity()).onChange();
                this.dismiss();
                break;
        }
    }
}
