package com.vee.healthplus.widget.chart;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;



/**
 * StickChart是在GridChart上绘制柱状数据的图表、如果需要支持显示均线，请参照 MAStickChart。
 */
public class StickChart extends GridChart {
	private int stickBorderColor =Color.RED;// 表示柱条的边框颜色
	private int stickFillColor = Color.RED; // 表示柱条的填充颜色
	private List<StickEntity> StickData;// 绘制柱条用的数据
	private List<StickEntity> StickLessData;
	private int maxSticksNum; // 柱条的最大表示数
	protected float maxValue;// Y的最大表示值
	protected float minValue; // Y的最小表示值
	
	//Lynn add
	private   int xGapValue; //
	private   int yGapValue; 
	private int stickLessColor = Color.RED; // 表示柱条的填充颜色

	public StickChart(Context context) {
		super(context);
	}

	public StickChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public StickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initAxisY();
		initAxisX();
		super.onDraw(canvas);

		drawSticks(canvas,StickData,stickFillColor);
		drawSticks(canvas,StickLessData,stickLessColor);
		
	}

	@Override // 计算X轴上显示的坐标值
	public String getAxisXGraduate(Object value) {
		float graduate = Float.valueOf(super.getAxisXGraduate(value));
		int index = (int) Math.floor(graduate * maxSticksNum);

		if (index >= maxSticksNum) {
			index = maxSticksNum - 1;
		} else if (index < 0) {
			index = 0;
		}

		return String.valueOf(StickData.get(index).getDate());
	}

	@Override
	public String getAxisYGraduate(Object value) {
		float graduate = Float.valueOf(super.getAxisYGraduate(value));
		return String.valueOf((int) Math.floor(graduate * (maxValue - minValue)
				+ minValue));
	}

	@Override
	public void notifyEvent(GridChart chart) {
//		CandleStickChart candlechart = (CandleStickChart) chart;
//
//		this.maxSticksNum = candlechart.getMaxSticksNum();

		super.setDisplayCrossYOnTouch(false);
		// notifyEvent
		super.notifyEvent(chart);
		// notifyEventAll
		super.notifyEventAll(this);
	}

	protected void initAxisX() { // 初始化Y轴的坐标值
		List<String> TitleX = new ArrayList<String>();
		if (null != StickData) {
			float average = maxSticksNum / this.getLongitudeNum();
			for (int i = 0; i < this.getLongitudeNum(); i++) {
				int index = (int) Math.floor(i * average);
				if (index > maxSticksNum - 1) {
					index = maxSticksNum - 1;
				}
				TitleX.add(String.valueOf(StickData.get(index).getDate())
						);//.substring(4) //lynn modify
			}
			TitleX.add(String
					.valueOf(StickData.get(maxSticksNum - 1).getDate())
					);//.substring(4)
		}
		super.setAxisXTitles(TitleX);
	}

	@Deprecated
	public int getSelectedIndex() { // 获取当前选中的柱条的index
		if (null == super.getTouchPoint()) {
			return 0;
		}
		float graduate = Float.valueOf(super.getAxisXGraduate(super
				.getTouchPoint().x));
		int index = (int) Math.floor(graduate * maxSticksNum);

		if (index >= maxSticksNum) {
			index = maxSticksNum - 1;
		} else if (index < 0) {
			index = 0;
		}

		return index;
	}

	
	protected void initAxisY() { // 初始化Y轴的坐标值
		List<String> TitleY = new ArrayList<String>();
		float average = (int) ((maxValue - minValue) * 100/ this.getLatitudeNum()) / 100 ; 
		// calculate degrees on Y axis
		for (int i = 0; i < this.getLatitudeNum(); i++) {
			String value = String.valueOf((int) Math.floor(minValue + i
					* average));
			if (value.length() < super.getAxisYMaxTitleLength()) {
				while (value.length() < super.getAxisYMaxTitleLength()) {
					value = new String(" ") + value;
				}
			}
			TitleY.add(value);
		}
		// calculate last degrees by use max value
		String value = String.valueOf((int) Math
				.floor(((int) maxValue)* 100 / 100 )); //lynn modify
		if (value.length() < super.getAxisYMaxTitleLength()) {
			while (value.length() < super.getAxisYMaxTitleLength()) {
				value = new String(" ") + value;
			}
		}
		TitleY.add(value);

		super.setAxisYTitles(TitleY);
	}

	protected void drawSticks(Canvas canvas,List<StickEntity> StickData,int fillcolor) { // 绘制柱条
		xGapValue=xGapValue*getWidth()/800;
		float stickWidth = ((super.getWidth() - super.getAxisMarginLeft() - super
				.getAxisMarginRight()) / maxSticksNum) - 1-xGapValue;
		float stickX = super.getAxisMarginLeft() + 1;

		Paint mPaintStick = new Paint();
		mPaintStick.setColor(fillcolor);

		if (null != StickData) {

			for (int i = 0; i < StickData.size(); i++) {
				StickEntity ohlc = StickData.get(i);

				float highY = (float) ((1f - (ohlc.getHigh() - minValue)
						/ (maxValue - minValue))
						* (super.getHeight() - super.getAxisMarginBottom()) - super
						.getAxisMarginTop());
				float lowY = (float) ((1f - (ohlc.getLow() - minValue)
						/ (maxValue - minValue))
						* (super.getHeight() - super.getAxisMarginBottom()) - super
						.getAxisMarginTop());

				// stick or line?
				if (stickWidth >= 2f) {
					canvas.drawRect(stickX, highY, stickX + stickWidth, lowY,
							mPaintStick);
				} else{
					canvas.drawLine(stickX, highY, stickX, lowY, mPaintStick);
				}

				// next x
				stickX = stickX + 1 + stickWidth+xGapValue;
			}
		}
	}

	// 追加一条新数据并刷新当前图表
	public void pushData(StickEntity entity,List<StickEntity> StickData) {
		if (null != entity) {
			addData(entity);
			super.postInvalidate();
		}
	}

	// 追加一条新数据--important
	public void addData(StickEntity entity) {
		if (null != entity) {
			// data is null or empty
			if (null == StickData || 0 == StickData.size()) {
				StickData = new ArrayList<StickEntity>();
				this.maxValue = ((int) entity.getHigh()) / 100 * 100;
			}

			// add
			StickData.add(entity);

			if (this.maxValue < entity.getHigh()) {
				this.maxValue =yGapValue + ((int) entity.getHigh()) * 100/ 100 ;
			}

			if (StickData.size() > maxSticksNum) { 
			}
		}
	}

	public void addLessData(StickEntity entity) {
		if (null != entity) {
			// data is null or empty
			if (null == StickLessData || 0 == StickLessData.size()) {
				StickLessData = new ArrayList<StickEntity>();
				this.maxValue = ((int) entity.getHigh()) / 100 * 100;
			}

			// add
			StickLessData.add(entity);

			if (this.maxValue < entity.getHigh()) {
				this.maxValue =yGapValue + ((int) entity.getHigh()) * 100/ 100 ;
			}

			if (StickLessData.size() > maxSticksNum) { 
			}
		}
	}
	
	public int getStickBorderColor() {
		return stickBorderColor;
	}

	public void setStickBorderColor(int stickBorderColor) {
		this.stickBorderColor = stickBorderColor;
	}

	public int getStickFillColor() {
		return stickFillColor;
	}

	public void setStickFillColor(int stickFillColor) {
		this.stickFillColor = stickFillColor;
	}
	
	public int getStickLessColor() {
		return stickLessColor;
	}

	public void setStickLessColor(int stickLessColor) {
		this.stickLessColor = stickLessColor;
	}

	public List<StickEntity> getStickData() {
		return StickData;
	}
	
	public List<StickEntity> getStickLessData() {
		return StickLessData;
	}

	public void setStickData(List<StickEntity> stickData) {
		if (null != StickData) {
			StickData.clear();
		}
		for (StickEntity e : stickData) {
			addData(e);
		}
	}

	public void setStickLessData(List<StickEntity> stickData) {
		if (null != StickLessData) {
			StickLessData.clear();
		}
		for (StickEntity e : stickData) {
			addLessData(e);
		}
	}
	
	public int getMaxSticksNum() {
		return maxSticksNum;
	}

	public void setMaxSticksNum(int maxSticksNum) {
		this.maxSticksNum = maxSticksNum;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}
	
	public int getXGapValue() {
		return xGapValue;
	}

	public void setXGapValue(int xGapValue) {
		this.xGapValue = xGapValue;
	}
	
	public int getYGapValue() {
		return yGapValue;
	}

	public void setYGapValue(int yGapValue) {
		this.yGapValue = yGapValue;
	}
}
