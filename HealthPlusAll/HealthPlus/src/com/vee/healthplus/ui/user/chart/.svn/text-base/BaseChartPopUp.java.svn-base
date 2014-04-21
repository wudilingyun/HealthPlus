package com.vee.healthplus.ui.user.chart;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.view.View;

import com.vee.healthplus.R;
import com.vee.healthplus.util.user.HP_DBModel;
import com.vee.healthplus.util.user.SaveDayRecordTask;
import com.vee.healthplus.widget.chart.GridChart;
import com.vee.healthplus.widget.chart.LineChart;
import com.vee.healthplus.widget.chart.LineEntity;
import com.vee.healthplus.widget.chart.StickChart;
import com.vee.healthplus.widget.chart.StickEntity;

public class BaseChartPopUp implements View.OnClickListener {
	protected  final static long ONE_DAY_TIME=24 * 60 * 60 * 1000;

	protected Activity mContext;
	protected View view;


	public BaseChartPopUp(Activity activity) {
		this.mContext = activity;
	}

	public View getView() {
		view = View.inflate(mContext, R.layout.usercharts, null);
		initDate();
		initView();
		return view;
	}

	protected void initView() {
	}

	protected void initDate() {

	}

	@Override
	public void onClick(View view) {

	}

	protected void initLineChart(LineChart lineChart,
			List<String> ytitle, List<String> xtitle, List<LineEntity> lines,int scaleXAxis) {
		lineChart.setAxisYTitles(ytitle);
		lineChart.setAxisXTitles(xtitle);

		setBaseChartUI(lineChart);
		lineChart.setDisplayLongitude(true);
		lineChart.setDisplayLatitude(true);
		lineChart.setXAxisScale(scaleXAxis);

		// line chart
		lineChart.setMaxPointNum(6); // 7
		lineChart.setMaxValue(130);
		lineChart.setMinValue(0);

		lineChart.setOneColor(true);
		lineChart.setLineData(lines);

		Paint paint = new Paint();
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setStrokeCap(Cap.ROUND);
		paint.setAntiAlias(true);
		paint.setShadowLayer(1.0f, 10.0f, 5.0f, R.color.gray);
		paint.setColor(mContext.getResources().getColor(
				R.color.hp_w_userweightchart_line));
		paint.setStrokeWidth(15);

		lineChart.setLinePaint(paint);

		lineChart.setLineData(lines);
		// 刷新数据
		lineChart.postInvalidate();
	}

	protected void initStickChart(StickChart stickChart, List<StickEntity> vol,
			List<StickEntity> lessVol) {

		setBaseChartUI(stickChart);
		stickChart.setDisplayLongitude(false);
		stickChart.setDisplayLatitude(true);
		stickChart.setXAxisScale(1);
		// stickchart.setLatitudeNum(5);
		stickChart.setLongitudeNum(7);

		stickChart.setStickFillColor(mContext.getResources().getColor(
				R.color.hp_w_userstickchart_fill));
		stickChart.setStickLessColor(mContext.getResources().getColor(
				R.color.hp_w_userstickchart_fill_less));
		stickChart.setMaxSticksNum(7);

		stickChart.setMaxValue(50);
		stickChart.setMinValue(0);

		stickChart.setXGapValue(40);
		stickChart.setYGapValue(10);

		stickChart.setAxisXTitleGap(40 / 2);

		stickChart.setStickData(vol);
		stickChart.setStickLessData(lessVol);

		// 刷新数据
		stickChart.postInvalidate();
	}

	protected void setBaseChartUI(GridChart chart) {
		chart.setBackgroundColor(mContext.getResources().getColor(
				R.color.hp_w_userweightchart_bg));

		Paint paint = new Paint();
		paint.setColor(mContext.getResources().getColor(
				R.color.hp_w_userweightchart_xline));
		paint.setStrokeWidth(3);
		chart.setAxisXPaint(paint);
		chart.setAxisYPaint(paint);

		paint = new Paint();
		paint.setColor(mContext.getResources().getColor(
				R.color.hp_w_userweightchart_font));
		paint.setStrokeWidth(3);
		paint.setTextSize(12);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		chart.setTextXPaint(paint);
		chart.setTextYPaint(paint);

		paint = new Paint();
		paint.setColor(mContext.getResources().getColor(
				R.color.hp_w_userweightchart_lonline));
		chart.setLatitudePaint(paint);
		chart.setLongitudePaint(paint);
		chart.setLongitudeFontSize(10);
		chart.setLatitudeFontSize(20);

		chart.setAxisMarginTop(3);
		chart.setAxisMarginLeft(25);
		chart.setAxisMarginRight(3);
		chart.setAxisMarginBottom(13); //margin内部距离，padding布局无效
		
		chart.setDisplayAxisXTitle(true);
		chart.setDisplayAxisYTitle(true);
		chart.setDisplayCrossXOnTouch(false);// 关闭点击十字线
		chart.setDisplayCrossYOnTouch(false);
		chart.setDisplayCrossXOnTouch(false);
		chart.setDisplayCrossYOnTouch(false);
		chart.setDisplayBorder(false);
		chart.setDisplayAxisXTitle(true);
		chart.setDisplayAxisYTitle(true);
	}
}
