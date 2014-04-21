package com.vee.healthplus.ui.heahth_heart;

import android.R;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.view.SurfaceHolder;
import android.view.View;

public class PathView extends View {
	private Path path;
	private Paint paint;
	private int a;

	public PathView(Context context) {
		super(context);
		/*paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		// 创建、并初始化Path
		path = new Path();
		path.moveTo(0, 0);
		for (int i = 1; i <= 20; i++) {
			// 生成15个点，随机生成它们的Y座标。并将它们连成一条Path
			path.lineTo(i * 20, (float) Math.random() * 60);
		}*/
	}

	public void getData(int a) {
		this.a = a;
		
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
	//	canvas.drawColor(Color.WHITE);
		System.out.println("被调用");
		
		paint = new Paint();
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(2);
		paint.setColor(Color.BLUE);
		// 创建、并初始化Path
		path = new Path();
		path.moveTo(8, 8);
		for (int i = 1; i <= 100; i++) {
			// 生成15个点，随机生成它们的Y座标。并将它们连成一条Path
			path.lineTo(i * 10, (float) Math.random() * 60);
		}
		path.addRect(0, 0, 8, 8, Path.Direction.CCW);
		canvas.drawPath(path, paint);
		canvas.translate(0, 60);
		canvas.translate(8, 8);
		
		super.onDraw(canvas);
	}

	public void clear(Canvas canvas) {
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(paint);
		canvas.drawPath(path, paint);
		canvas.translate(0, 60);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
	}

}
