package com.vee.healthplus.widget.chart;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;


/**
 * LineChart是在GridChart上绘制一条或多条线条的图
 */
public class LineChart extends GridChart {
	private List<LineEntity> lineData; // 绘制线条用的数据
	private int maxPointNum;// 线条的最大表示点数,决定点数
	private int minValue;// Y的最小表示值
	private int maxValue; // Y的最大表示值
	private Paint linePaint;
	private boolean isOneColor;

	public LineChart(Context context) {
		super(context);
	}

	public LineChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public LineChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// draw lines
		if (null != this.lineData) {
			drawLines(canvas);
		}
	}

	protected void drawLines(Canvas canvas) {
		// distance between two points
		float lineLength = ((super.getWidth() - super.getAxisMarginLeft() - this
				.getMaxPointNum()) / this.getMaxPointNum()) - 1;
		// start point‘s X
		float startX;

		// draw lines
		for (int i = 0; i < lineData.size(); i++) {
			LineEntity line = (LineEntity) lineData.get(i);
			if (line.isDisplay()) {
				Paint mPaint = new Paint();

				if (isOneColor)
					mPaint = getLinePaint();
				else {
					mPaint.setColor(line.getLineColor());
					mPaint.setAntiAlias(true);
					mPaint.setStrokeWidth(line.getLineStroke());
				}
				List<Float> lineData = line.getLineData();
				// set start point’s X
				startX = super.getAxisMarginLeft()  ; //+lineLength / 2f lynn modify,not center horizontal
				// start point
				PointF ptFirst = null;
				if (lineData != null) {
					for (int j = 0; j < lineData.size(); j++) {
						float value = lineData.get(j).floatValue();
						// calculate Y
						float valueY = (float) ((1f - (value - this
								.getMinValue())
								/ (this.getMaxValue() - this.getMinValue())) * (super
								.getHeight() - super.getAxisMarginBottom()));

						// if is not last point connect to previous point
						if (j > 0) {
							canvas.drawLine(ptFirst.x, ptFirst.y, startX,
									valueY, mPaint);
						}
						// reset
						ptFirst = new PointF(startX, valueY);
						startX = startX + 1 + lineLength; 
					}
				}
			}
		}
	}

	public List<LineEntity> getLineData() {
		return lineData;
	}

	public void setLineData(List<LineEntity> lineData) {
		this.lineData = lineData;
	}

	public int getMaxPointNum() {
		return maxPointNum;
	}

	public void setMaxPointNum(int maxPointNum) {
		this.maxPointNum = maxPointNum;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

	/**************** Lynn add ***************/
	public void setLinePaint(Paint paint) {
		this.linePaint = paint;
	}

	public Paint getLinePaint() {
		if (linePaint == null) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			return paint;

		}
		return linePaint;
	}

	public boolean isOneColor() {
		return isOneColor;
	}

	public void setOneColor(boolean isOneColor) {
		this.isOneColor = isOneColor;
	}

}
