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
public class TargetDistancePickDialog extends DialogFragment implements View.OnClickListener {

    private WheelView distance_wv;
    private String[] distanceArr = {"1", "2", "3", "4", "5", "6", "7", "8"};
    private NumberPicker distance_np;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());
        builder.setTitle(R.string.hp_target_modeldistancedes);
        View layout = View.inflate(getActivity(), R.layout.hp_target_dialog_distance, null);
        initView(layout);
        builder.setContentView(layout);
        return builder.create();
    }

    private void initView(View layout) {
        distance_wv = (WheelView) layout.findViewById(R.id.distance_wv);
        distance_wv.setViewAdapter(new Distance_Adapter(getActivity()));
        distance_np=(NumberPicker)layout.findViewById(R.id.distance_np);
        distance_np.setUnitText(getResources().getString(R.string.hp_main_distance_unit));
        Button setDistanceTarget_btn = (Button) layout.findViewById(R.id.setDistanceTarget_btn);
        setDistanceTarget_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.setDistanceTarget_btn:
                HP_TargetConfig.getInstance().setTargetCalorie(0, getActivity());
                HP_TargetConfig.getInstance().setTargetTime(0, getActivity());
                HP_TargetConfig.getInstance().setTargetDistance(0, getActivity());
                HP_TargetConfig.getInstance().setTargetSportMode(HP_TargetConfig.TargetSportMode.DISTANCE,getActivity());
                try{
                HP_TargetConfig.getInstance().setTargetDistance(distance_np.getCurValue(), getActivity());
                }catch (Exception ex){
                    Toast.makeText(getActivity(),"输入值不合法！",Toast.LENGTH_SHORT).show();
                    break;
                }
                HP_TargetConfig.getInstance().setTargetUpdateTime(System.currentTimeMillis(),getActivity());
                ModeControl.getInstance(getActivity()).onChange(HPConst.STRING_TYPE_MODE);
                HP_TargetConfig.modelChange();
                HP_TargetConfig.getInstance().setFinishState(false);
                this.dismiss();
                break;
        }
    }

    private class Distance_Adapter extends AbstractWheelTextAdapter {


        protected Distance_Adapter(Context context) {
            super(context);
        }

        @Override
        protected CharSequence getItemText(int index) {
            return distanceArr[index];
        }

        @Override
        public int getItemsCount() {
            return distanceArr.length;
        }
    }
}
