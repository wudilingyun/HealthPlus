package com.vee.healthplus.ui.user.chart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.util.user.SaveDayRecordTask;
import com.vee.healthplus.widget.chart.GridChart;
import com.vee.healthplus.widget.chart.LineChart;
import com.vee.healthplus.widget.chart.LineEntity;


/**
 * Created by wangjiafeng on 13-11-20.
 */
public class WeightChartPopUp extends BaseChartPopUp implements
SaveDayRecordTask.SaveDayRecordCallBack, HP_DBModel.UserWeightCallBack {
    private EditText todayweight_et;
    private LineChart lineChart;
    private float maxWeight = 0F;
    private float minWeight = 1000F;
    private List<Float> list = new ArrayList<Float>();
//    private PopupWindow popupWindow;

    public WeightChartPopUp(Activity activity) {
    	super(activity);

    }

//    public PopupWindow getPopUp() {
//        view = View.inflate(mContext, R.layout.usercharts, null);
//        initView();
//        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//        popupWindow.setFocusable(true);
//        popupWindow.setOutsideTouchable(true);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
//        popupWindow.update();
//        return popupWindow;
//    }

    public View getView() {
        view = View.inflate(mContext, R.layout.usercharts, null);
        initView();
        initDate();
        return view;
    }

    @Override
    protected void initView() {
        Button save_btn = (Button) view.findViewById(R.id.save_btn);
        todayweight_et = (EditText) view.findViewById(R.id.todayweight_et);
        lineChart = (LineChart) view.findViewById(R.id.weight_lc);
        save_btn.setOnClickListener(this);
    }

    @Override
    protected void initDate() {
        list.clear();
        HP_DBModel.getInstance(mContext).queryUserWeightInfoByUserId(HP_User.getOnLineUserId(mContext), true, this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_btn:
                String weight = todayweight_et.getText().toString();
                if (HP_User.getOnLineUserId(mContext) != 0) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                    String date = sdf.format(new Date(System.currentTimeMillis()));
                    new SaveDayRecordTask(mContext, date, "weight", weight, this).execute();
                }
                HP_DBModel.getInstance(mContext).insertUserWeightInfo(HP_User.getOnLineUserId(mContext), Float.valueOf(weight), System.currentTimeMillis(), true);
                initDate();
//                popupWindow.dismiss();
                break;
        }
    }

    private void initweight_lc(List<Float> list) {
    	 List<LineEntity> lines = new ArrayList<LineEntity>();
         LineEntity testLine = new LineEntity();
         testLine.setTitle("Weight");
         testLine.setLineColor(mContext.getResources().getColor(R.color.hp_w_userweightchart_line));
         testLine.setLineData(list);
         testLine.setLineStroke(5.0f);
         lines.add(testLine);
         
         List<String> ytitle = new ArrayList<String>();
         ytitle.add("0");
         ytitle.add("65");
         ytitle.add("kg");

         
         List<String> xtitle = new ArrayList<String>();
         xtitle.add("10-11");
         xtitle.add("10-12");
         xtitle.add("10-13");
         xtitle.add("10-14");
         xtitle.add("10-15");
         xtitle.add("10-16");
         xtitle.add("10-17");
      
         super.initLineChart(lineChart, ytitle, xtitle, lines,2);
    }
   
    @Override
    public void onFinishSaveDayRecord(int reflag) {

    }

    @Override
    public void onErrorSaveDayRecord(Exception e) {

    }

    @Override
    public void queryUserWeightCallBack(float userWeight, long createTime) {
        list.add(userWeight);
        initweight_lc(list);
    }
}
