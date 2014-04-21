package com.vee.healthplus.widget.chart;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.vee.healthplus.R;

/**
 * GridChart是所有网格图表的基础类对象，它实现了基本的网格图表功能，这些功能将被它的继承类使用
 */
public class GridChart extends BaseChart implements ITouchEventNotify,
		ITouchEventResponse {

	private Paint axisXPaint = null; // 坐标轴X的paint
	private Paint axisYPaint = null; // 坐标轴y的paint
	private Paint textXPaint = null; // 坐标轴x的text paint
	private Paint textYPaint = null; // 坐标轴y的text paint
	private Paint longitudePaint = null; // 坐标轴y的text paint
	private Paint latitudePaint = null; // 坐标轴y的text paint
	private int xAxisScale=1; //x轴坐标轴的比列位置
	private int axisXTitleGap; //坐标x轴文本的位移

	public static final PathEffect DEFAULT_DASH_EFFECT = new DashPathEffect(
			new float[] { 3, 3, 3, 3 }, 1); // 默认虚线效果
	private int backgroundColor = Color.BLACK; // 背景色

	private int latitudeNum = 4;// 网格纬线的数量
	private int longitudeNum = 3;// 网格经线的数量

	private float axisMarginLeft = 42f; // 轴线左边距
	private float axisMarginBottom = 16f; // 轴线下边距
	private float axisMarginTop = 5f; // 轴线上边距
	private float axisMarginRight = 5f;// 轴线右边距
	private boolean displayAxisXTitle = Boolean.TRUE;// X轴上的标题是否显示
	private boolean displayAxisYTitle = Boolean.TRUE; // Y轴上的标题是否显示
	private boolean displayLongitude = Boolean.TRUE; // 纬线是否显示
	private boolean displayLatitude = Boolean.TRUE; //
	private PathEffect dashEffect = DEFAULT_DASH_EFFECT; // 虚线效果
	private boolean displayBorder = Boolean.TRUE; // 控件是否显示边框
	private int borderColor = Color.RED;// 图边框的颜色
	private int longitudeFontSize = 12; // 经线刻度字体大小
	private int latitudeFontSize = 12;// 纬线刻度字体大小
	private List<String> axisXTitles;// X轴标题数组
	private List<String> axisYTitles;// Y轴标题数组
	private int axisYMaxTitleLength = 5; // Y轴标题最大文字长度
	private boolean displayCrossXOnTouch = true; // 在控件被点击时，显示十字竖线线
	private boolean displayCrossYOnTouch = true;// 在控件被点击时，显示十字横线线
	private PointF touchPoint;// 单点触控的选中点
	private float clickPostX = 0f;// 单点触控的选中点的X
	private float clickPostY = 0f;// 单点触控的选中点的Y
	private List<ITouchEventResponse> notifyList; // 事件通知对象列表

	public GridChart(Context context) {
		super(context);
	}

	public GridChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public GridChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		super.setBackgroundColor(getResources().getColor(
				R.color.hp_w_userweightchart_bg));

		drawXAxis(canvas);
		drawYAxis(canvas);

		if (this.displayBorder) {
			drawBorder(canvas);
		}

		// 经纬线的文本（x/y坐标轴文字）
		if (displayLongitude || displayAxisXTitle) {
			drawAxisGridX(canvas);
		}
		if (displayLatitude || displayAxisYTitle) {
			drawAxisGridY(canvas);
		}

		// if (displayCrossXOnTouch || displayCrossYOnTouch) {
		// drawWithFingerClick(canvas);
		// }
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		if (event.getY() > 0
				&& event.getY() < super.getBottom() - getAxisMarginBottom()
				&& event.getX() > super.getLeft() + getAxisMarginLeft()
				&& event.getX() < super.getRight()) {

			// touched points, if touch point is only one
			if (event.getPointerCount() == 1) {
				// 获取点击坐�?
				clickPostX = event.getX();
				clickPostY = event.getY();

				PointF point = new PointF(clickPostX, clickPostY);
				touchPoint = point;

				// redraw
				super.invalidate();

				// do notify
				notifyEventAll(this);

			} else if (event.getPointerCount() == 2) {
			}
		}
		return super.onTouchEvent(event);
	}

	// 绘制一段文本，并增加外框
	private void drawAlphaTextBox(PointF ptStart, PointF ptEnd, String content,
			int fontSize, Canvas canvas) {

		Paint mPaintBox = new Paint();
		mPaintBox.setColor(Color.BLACK);
		mPaintBox.setAlpha(80);

		Paint mPaintBoxLine = new Paint();
		mPaintBoxLine.setColor(Color.CYAN);
		mPaintBoxLine.setAntiAlias(true);

		// draw a rectangle
		canvas.drawRoundRect(new RectF(ptStart.x, ptStart.y, ptEnd.x, ptEnd.y),
				20.0f, 20.0f, mPaintBox);

		// draw a rectangle' border
		canvas.drawLine(ptStart.x, ptStart.y, ptStart.x, ptEnd.y, mPaintBoxLine);
		canvas.drawLine(ptStart.x, ptEnd.y, ptEnd.x, ptEnd.y, mPaintBoxLine);
		canvas.drawLine(ptEnd.x, ptEnd.y, ptEnd.x, ptStart.y, mPaintBoxLine);
		canvas.drawLine(ptEnd.x, ptStart.y, ptStart.x, ptStart.y, mPaintBoxLine);

		// draw text
		canvas.drawText(content, ptStart.x, ptEnd.y, mPaintBoxLine);
	}

	// 计算X轴上显示的坐标值
	public String getAxisXGraduate(Object value) {

		float length = super.getWidth() - axisMarginLeft - 2 * axisMarginRight;
		float valueLength = ((Float) value).floatValue() - axisMarginLeft
				- axisMarginRight;

		return String.valueOf(valueLength / length);
	}

	// 计算Y轴上显示的坐标值
	public String getAxisYGraduate(Object value) {

		float length = super.getHeight() - axisMarginBottom - 2 * axisMarginTop;
		float valueLength = length
				- (((Float) value).floatValue() - axisMarginTop);

		return String.valueOf(valueLength / length);
	}

	// 绘制经线
	protected void drawAxisGridX(Canvas canvas) {

		if (null != axisXTitles) {
			int counts = axisXTitles.size();
			float length = super.getHeight() - axisMarginBottom;

			Paint mPaintLine = getLongitudePaint();
			Paint mPaintFont = getTextXPaint();
			if (counts > 1) {
				float postOffset = (super.getWidth() - axisMarginLeft - 2 * axisMarginRight)
						/ (counts - 1);
				float offset = axisMarginLeft + axisMarginRight;
				for (int i = 0; i <= counts; i++) {
					// draw line
					if (displayLongitude) {
						canvas.drawLine(offset + i * postOffset, 0f, offset + i
								* postOffset, length, mPaintLine);
					}
					// draw title
					if (displayAxisXTitle) {
						if (i < counts && i > 0) {
							canvas.drawText(axisXTitles.get(i), offset + i
									* postOffset+(int)(axisXTitleGap*1.5)- (axisXTitles.get(i).length())* longitudeFontSize / 3f, 
									super.getHeight()- axisMarginBottom + longitudeFontSize,
									mPaintFont);
						} else if (0 == i) {
							canvas.drawText(axisXTitles.get(i),
									this.axisMarginLeft - 2f+axisXTitleGap, super.getHeight()
											- axisMarginBottom
											+ longitudeFontSize, mPaintFont);
						}
					}
				}
			}
		}
	}

	// 绘制纬线
	protected void drawAxisGridY(Canvas canvas) {
		if (null != axisYTitles) {
			int counts = axisYTitles.size();
			float length = super.getWidth() - axisMarginLeft;

			Paint mPaintLine = getLatitudePaint();
			Paint mPaintFont = getTextYPaint();

			if (counts > 1) {
				float postOffset = (super.getHeight() - axisMarginBottom - 2 * axisMarginTop)
						/ (counts - 1);
				float offset = super.getHeight() - axisMarginBottom
						- axisMarginTop;
				for (int i = 0; i <= counts; i++) {
					// draw line
					if (displayLatitude) {
						canvas.drawLine(axisMarginLeft,
								offset - i * postOffset, axisMarginLeft
										+ length, offset - i * postOffset,
								mPaintLine);
					}
					// draw title
					if (displayAxisYTitle) {
						if (i < counts && i > 0) {
							canvas.drawText(axisYTitles.get(i), 0f, offset - i
									* postOffset + latitudeFontSize / 2f,
									mPaintFont);
						} else if (0 == i) {
							canvas.drawText(axisYTitles.get(i), 0f,
									super.getHeight() - this.axisMarginBottom
											- 2f, mPaintFont);
						}
					}
				}
			}
		}
	}

	// 绘制边框
	protected void drawBorder(Canvas canvas) {
		float width = super.getWidth() - 2;
		float height = super.getHeight() - 2;

		Paint mPaint = new Paint();
		mPaint.setColor(borderColor);

		// draw a rectangle
		canvas.drawLine(1f, 1f, 1f + width, 1f, mPaint);
		canvas.drawLine(1f + width, 1f, 1f + width, 1f + height, mPaint);
		canvas.drawLine(1f + width, 1f + height, 1f, 1f + height, mPaint);
		canvas.drawLine(1f, 1f + height, 1f, 1f, mPaint);
	}

	// 绘制X轴
	protected void drawXAxis(Canvas canvas) {
		float postX = axisMarginLeft + 1; //lynn modify
		float length = super.getWidth();
		float postY = super.getHeight() - axisMarginBottom - 1;
		Paint mPaint = getAxisXPaint();
		canvas.drawLine(postX, postY/xAxisScale, length, postY/xAxisScale, mPaint); //lynn modify

	}

	// 绘制Y轴
	protected void drawYAxis(Canvas canvas) {

		float length = super.getHeight() - axisMarginBottom;
		float postX = axisMarginLeft + 1;

		Paint mPaint = getAxisYPaint();

		canvas.drawLine(postX, 0f, postX, length, mPaint);
	}

	// 放大表示
	protected void zoomIn() {

	}

	// 縮小表示
	protected void zoomOut() {

	}

	public void notifyEvent(GridChart chart) {
		PointF point = chart.getTouchPoint();
		if (null != point) {
			clickPostX = point.x;
			clickPostY = point.y;
		}
		touchPoint = new PointF(clickPostX, clickPostY);
		super.invalidate();
	}

	public void addNotify(ITouchEventResponse notify) {
		if (null == notifyList) {
			notifyList = new ArrayList<ITouchEventResponse>();
		}
		notifyList.add(notify);
	}

	public void removeNotify(int i) {
		if (null != notifyList && notifyList.size() > i) {
			notifyList.remove(i);
		}
	}

	public void removeAllNotify() {
		if (null != notifyList) {
			notifyList.clear();
		}
	}

	public void notifyEventAll(GridChart chart) {
		if (null != notifyList) {
			for (int i = 0; i < notifyList.size(); i++) {
				ITouchEventResponse ichart = notifyList.get(i);
				ichart.notifyEvent(chart);
			}
		}
	}

	/*************** Lynn add ******************/
	public void setXAxisScale(int xAxisScale) {
		this.xAxisScale = xAxisScale;
	}

	public int getXAxisScale() {
		return xAxisScale;
	}

	public void setAxisXTitleGap(int axisXTitleGap) {
		this.axisXTitleGap = axisXTitleGap;
	}

	public int getAxisXTitleGap() {
		return axisXTitleGap;
	}
	

	public void setTextXPaint(Paint paint) {
		this.textXPaint = paint;
	}

	public Paint getTextXPaint() {
		if (textXPaint == null) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			return paint;

		}
		return textXPaint;
	}

	public void setTextYPaint(Paint paint) {
		this.textYPaint = paint;
	}

	public Paint getTextYPaint() {
		if (textYPaint == null) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			return paint;

		}
		return textYPaint;
	}

	public void setLongitudePaint(Paint paint) {
		this.longitudePaint = paint;
	}

	public Paint getLongitudePaint() {
		if (longitudePaint == null) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			return paint;

		}
		return longitudePaint;
	}

	public void setLatitudePaint(Paint paint) {
		this.latitudePaint = paint;
	}

	public Paint getLatitudePaint() {
		if (latitudePaint == null) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			return paint;

		}
		return latitudePaint;
	}

	public void setAxisXPaint(Paint paint) {
		this.axisXPaint = paint;
	}

	public void setAxisYPaint(Paint paint) {
		this.axisYPaint = paint;
	}

	public Paint getAxisXPaint() {
		if (axisXPaint == null) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			return paint;

		}
		return axisXPaint;
	}

	public Paint getAxisYPaint() {
		if (axisYPaint == null)
			return new Paint();
		return axisYPaint;
	}

	/********************************************/
	public boolean isDisplayLongitude() {
		return displayLongitude;
	}

	public void setDisplayLongitude(boolean displayLongitude) {
		this.displayLongitude = displayLongitude;
	}

	public boolean isDisplayLatitude() {
		return displayLatitude;
	}

	public void setDisplayLatitude(boolean displayLatitude) {
		this.displayLatitude = displayLatitude;
	}

	public int getLongitudeNum() {
		return longitudeNum;
	}

	public void setLongitudeNum(int longitudeNum) {
		this.longitudeNum = longitudeNum;
	}

	public int getLatitudeNum() {
		return latitudeNum;
	}

	public void setLatitudeNum(int latitudeNum) {
		this.latitudeNum = latitudeNum;
	}

	public float getAxisMarginLeft() {
		return axisMarginLeft;
	}

	public void setAxisMarginLeft(float axisMarginLeft) {
		this.axisMarginLeft = axisMarginLeft;
	}

	public float getAxisMarginBottom() {
		return axisMarginBottom;
	}

	public void setAxisMarginBottom(float axisMarginBottom) {
		this.axisMarginBottom = axisMarginBottom;
	}

	public float getAxisMarginTop() {
		return axisMarginTop;
	}

	public void setAxisMarginTop(float axisMarginTop) {
		this.axisMarginTop = axisMarginTop;
	}

	public float getAxisMarginRight() {
		return axisMarginRight;
	}

	public void setAxisMarginRight(float axisMarginRight) {
		this.axisMarginRight = axisMarginRight;
	}

	public boolean isDisplayAxisXTitle() {
		return displayAxisXTitle;
	}

	public void setDisplayAxisXTitle(boolean displayAxisXTitle) {
		this.displayAxisXTitle = displayAxisXTitle;
	}

	public boolean isDisplayAxisYTitle() {
		return displayAxisYTitle;
	}

	public void setDisplayAxisYTitle(boolean displayAxisYTitle) {
		this.displayAxisYTitle = displayAxisYTitle;
	}

	public PathEffect getDashEffect() {
		return dashEffect;
	}

	public void setDashEffect(PathEffect dashEffect) {
		this.dashEffect = dashEffect;
	}

	public boolean isDisplayBorder() {
		return displayBorder;
	}

	public void setDisplayBorder(boolean displayBorder) {
		this.displayBorder = displayBorder;
	}

	public int getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(int borderColor) {
		this.borderColor = borderColor;
	}

	public int getLongitudeFontSize() {
		return longitudeFontSize;
	}

	public void setLongitudeFontSize(int longitudeFontSize) {
		this.longitudeFontSize = longitudeFontSize;
	}

	public int getLatitudeFontSize() {
		return latitudeFontSize;
	}

	public void setLatitudeFontSize(int latitudeFontSize) {
		this.latitudeFontSize = latitudeFontSize;
	}

	public List<String> getAxisXTitles() {
		return axisXTitles;
	}

	public void setAxisXTitles(List<String> axisXTitles) {
		this.axisXTitles = axisXTitles;
	}

	public List<String> getAxisYTitles() {
		return axisYTitles;
	}

	public void setAxisYTitles(List<String> axisYTitles) {
		this.axisYTitles = axisYTitles;
	}

	public int getAxisYMaxTitleLength() {
		return axisYMaxTitleLength;
	}

	public void setAxisYMaxTitleLength(int axisYMaxTitleLength) {
		this.axisYMaxTitleLength = axisYMaxTitleLength;
	}

	public boolean isDisplayCrossXOnTouch() {
		return displayCrossXOnTouch;
	}

	public void setDisplayCrossXOnTouch(boolean displayCrossXOnTouch) {
		this.displayCrossXOnTouch = displayCrossXOnTouch;
	}

	public boolean isDisplayCrossYOnTouch() {
		return displayCrossYOnTouch;
	}

	public void setDisplayCrossYOnTouch(boolean displayCrossYOnTouch) {
		this.displayCrossYOnTouch = displayCrossYOnTouch;
	}

	public float getClickPostX() {
		return clickPostX;
	}

	public void setClickPostX(float clickPostX) {
		this.clickPostX = clickPostX;
	}

	public float getClickPostY() {
		return clickPostY;
	}

	public void setClickPostY(float clickPostY) {
		this.clickPostY = clickPostY;
	}

	public List<ITouchEventResponse> getNotifyList() {
		return notifyList;
	}

	public void setNotifyList(List<ITouchEventResponse> notifyList) {
		this.notifyList = notifyList;
	}

	public PointF getTouchPoint() {
		return touchPoint;
	}

	public void setTouchPoint(PointF touchPoint) {
		this.touchPoint = touchPoint;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}
