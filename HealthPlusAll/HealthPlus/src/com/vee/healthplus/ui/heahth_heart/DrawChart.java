package com.vee.healthplus.ui.heahth_heart;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.PorterDuff.Mode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class DrawChart extends View {
	private int CHARTH = 100;
	private int CHARTW = 480;
	private int OFFSET_LEFT = 12;
	private int OFFSET_TOP = 40;
	private int X_INTERVAL = 20;
	private Context context;
	private List<Point> plist;
	HeartRateActivity activity;
	private WindowManager wmManager;
	private Boolean flag = true;
	float density;

	public DrawChart(Context context) {
		super(context);
		this.context = context;
		plist = new ArrayList<Point>();
		activity = (HeartRateActivity) context;
		Log.i("DrawChart", "DrawChart1");
		wmManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		// initPlist();

		DisplayMetrics metric = new DisplayMetrics();
		wmManager.getDefaultDisplay().getMetrics(metric);
		density = metric.density;
		X_INTERVAL = (int) (X_INTERVAL * density / 1.5);
		OFFSET_LEFT = (int) (OFFSET_LEFT * density / 1.5);
		CHARTW = wmManager.getDefaultDisplay().getWidth()
				- (int) (30 * density / 1.5);
		System.out.println("宽度"+wmManager.getDefaultDisplay().getWidth());
	}

	Boolean ClearView() {
		return false;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// context.getApplicationCo
		if (flag == false) {
			clear(canvas);
		} else {

			super.onDraw(canvas);

			Log.i("DrawChart", "onDraw2");
			// drawTable(canvas);
			prepareLine();
			drawCurve(canvas);
		}
	}

	/***
	 * 画出波浪线的表格以及虚线
	 * 
	 * @param canvas
	 */
	private void drawTable(Canvas canvas) {
		Log.i("DrawChart", "drawTable3");
		Paint paint = new Paint();
		paint.setAlpha(30);
		paint.setStrokeWidth(1);
		paint.setColor(Color.WHITE);
		// canvas.drawColor(R.color.)
		// 画外框
		paint.setStyle(Paint.Style.STROKE);
		Rect chartRec = new Rect(OFFSET_LEFT - 1, OFFSET_TOP - 5, CHARTW
				+ OFFSET_LEFT, CHARTH + OFFSET_TOP);
		canvas.drawRect(chartRec, paint);
		// 画左边的文字
		Path textPath = new Path();
		paint.setStyle(Paint.Style.FILL);
		textPath.moveTo(30, 420);
		textPath.lineTo(30, 300);
		paint.setTextSize(15);
		paint.setAntiAlias(true);
		canvas.drawTextOnPath("信号强度 [dBm]", textPath, 0, 0, paint);
		// 画表格中的虚线
		Path path = new Path();
		PathEffect effects = new DashPathEffect(new float[] { 2, 2, 2, 2 }, 1);
		paint.setStyle(Paint.Style.STROKE);
		paint.setAntiAlias(false);
		paint.setPathEffect(effects);
		for (int i = 1; i < 9; i++) {
			path.moveTo(OFFSET_LEFT, OFFSET_TOP + CHARTH / 10 * i);
			path.lineTo(OFFSET_LEFT + CHARTW, OFFSET_TOP + CHARTH / 10 * i);
			canvas.drawPath(path, paint);
		}

		for (int i = 23; i > 0; i--) {
			path.moveTo(OFFSET_LEFT + CHARTH / 10 * i, OFFSET_TOP);
			path.lineTo(OFFSET_LEFT + CHARTH / 10 * i, OFFSET_TOP + CHARTH);
			canvas.drawPath(path, paint);
		}

	}

	private void drawCurve(Canvas canvas) {
		Log.i("DrawChart", "drawCurve4");

		Paint paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAlpha(150);
		paint.setStrokeWidth(2);
		paint.setAntiAlias(true);
		if (plist.size() >= 2) {
			for (int i = 0; i < plist.size() - 1; i++) {
				// System.out.println(plist.get(i).x+"======="+plist.get(i).y);
				// System.out.println(plist.get(i+1).x+"======="+plist.get(i+1).y);
				canvas.drawLine(plist.get(i).x, plist.get(i).y,
						plist.get(i + 1).x, plist.get(i + 1).y, paint);
			}
		}
	}

	private void prepareLine() {
		Log.i("DrawChart", "prepareLine5");
		int py = (int) (102 * density / 1.5);
		double a = Math.random();
		if (a < 0.5)

			if (activity.getHeartbeat() != 0 && activity.getHeartbeat() > 30
					&& activity.getHeartbeat() < 150) {
				py = OFFSET_TOP
						+ (int) (a * (activity.getHeartbeat() - OFFSET_TOP));
			}
		// System.out.println("py"+py);
		Point p = new Point(OFFSET_LEFT + CHARTW, py);

		if (plist.size() > 24*density/1.5) {
			Log.i("prepareLine", "plist==" + plist.get(0).x);
			plist.remove(0);
			for (int i = 0; i < 23*density/1.5; i++) {
				if (i == 0)
					plist.get(i).x -= (X_INTERVAL - 2);
				else
					plist.get(i).x -= X_INTERVAL;
			}
			plist.add(p);
		} else {
			for (int i = 0; i < plist.size() - 1; i++) {
				plist.get(i).x -= X_INTERVAL;
			}

			plist.add(p);
		}

	}

	public void clear(Canvas canvas) {
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC));

		invalidate();
	}
}
