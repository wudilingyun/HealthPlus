package com.vee.healthplus.ui.sporttrack;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import com.vee.healthplus.R;
import com.vee.healthplus.common.MyApplication;

public class WatchCanvas extends View {

	private int width, height;

	private int radiusX=200;
	private int radiusY=200;

	private int circleRadius = 200;
	private int innerCircleRadius = 200;
	private int borderWidth = 6; //
	private int gapWidth = 28;
	private int borderColor=Color.WHITE;
	private int shadowColor=Color.rgb(0, 81, 102);

	private Context context;
	
	private DataInfo dataInfo;
	

	public WatchCanvas(Context context) {
		super(context);
		init(context);
	}

	public WatchCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		initData();

	}

	private void initData() {
		dataInfo=new DataInfo();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec,heightMeasureSpec);
		width = getWidth();
		height = getHeight();

//		if (width != 0 && height != 0) {
//			// initPoints(points);
//			radiusX=width/2;
//			radiusY=height/2-dpToPx(100)/2; //height=whole height
//			//heightMeasureSpec=heightMeasureSpec-(height-(radiusY+circleRadius+10))+radiusY/2;  
//		 setMeasuredDimension(width,circleRadius*2+20);    
//		}
		
		if (width != 0 && height != 0) {
			// initPoints(points);
			radiusX=width/2;
			radiusY=height/2; 
			circleRadius=dpToPx(130);
		}
	}

	@Deprecated
	private int measureHeight(int measureSpec) {
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);
		int result = 500;
		if (specMode == MeasureSpec.AT_MOST)
		{
			result = specSize;
		}
		else if (specMode == MeasureSpec.EXACTLY)
		{
			result = specSize;
		}
		return result;
	}	  
		   
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, 0, 0, getWidth(), height-100);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawCanvasBottomShdow(canvas);
		drawOutBorder(canvas);
		drawOutRound(canvas);
		drawOutShdow(canvas);
		drawGapRound(canvas);
		drawInnerBorder(canvas);
		drawInnerRound(canvas);
		drawInnerData(canvas);
		
		super.onDraw(canvas);
	}
	

	@Override
	protected void dispatchDraw(Canvas canvas) {
		
		//drawCanvasShdow(canvas);
		super.dispatchDraw(canvas);
	}
	
	private void drawCanvasBottomShdow(Canvas canvas){
		Paint paint=new Paint();
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.sporttrack_shadow);  
        canvas.drawBitmap(bmp, radiusX-bmp.getWidth()/2, radiusY+circleRadius-bmp.getHeight()*3/4, paint);  
	}
	
	private void drawOutBorder(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(borderColor);
		paint.setStrokeWidth(borderWidth);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		//paint.setShadowLayer(1.0f, 0.0f, 3.0f, Color.BLACK);
		canvas.drawCircle(radiusX, radiusY, circleRadius, paint);
	}
	
	private void drawOutRound(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(0, 156, 189));
		paint.setStrokeWidth(0);
		paint.setStyle(Style.FILL);
		paint.setAntiAlias(true);
		//paint.setShadowLayer(4.0f,0.0f, 2.0f, shadowColor);
		canvas.drawCircle(radiusX, radiusY, circleRadius-2, paint);
		
//		canvas.save();
//		canvas.scale(-1, 1, x + bmpLuffy[0].getWidth() / 2, y + bmpLuffy[0].getHeight() / 2);
//		canvas.drawBitmap(bmpLuffy[0], x, y, paint);
//		canvas.restore();
	}

	
	private void drawOutShdow(Canvas canvas){
		Paint paint=new Paint();
		paint.setColor(Color.rgb(0, 113, 137));
		paint.setStrokeWidth(2);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		canvas.drawCircle(radiusX, radiusY, circleRadius-2, paint);
	}
	
	
	private void drawGapRound(Canvas canvas){
		float percent=(float)dataInfo.percent;
		float perimeter = (float) (2 * (circleRadius-2-gapWidth/2) * Math.PI);
		
		Paint  paint=new Paint();
		paint.setColor(Color.rgb(255, 241, 30));
		paint.setAntiAlias(true);
		paint.setStrokeWidth(gapWidth/2);
		paint.setStyle(Style.STROKE);
	
		DashPathEffect pathEffect = new DashPathEffect(new float[] {perimeter*percent,perimeter- perimeter*percent }, perimeter/4);
		paint.setPathEffect(pathEffect);
		canvas.drawCircle(radiusX, radiusY, circleRadius-2-gapWidth/2-borderWidth/2/2, paint);
	}
	
	private void drawInnerBorder(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(borderColor);
		paint.setStrokeWidth(borderWidth);
		paint.setStyle(Style.STROKE);
		paint.setAntiAlias(true);
		paint.setShadowLayer(1.0f, 0.0f, 4.0f, shadowColor);
		canvas.drawCircle(radiusX, radiusY, circleRadius-2-gapWidth, paint);
	}
	
	private void drawInnerRound(Canvas canvas) {
		Paint paint = new Paint();
		paint.setColor(Color.rgb(233, 250, 254));
		paint.setStrokeWidth(borderWidth);
		paint.setStyle(Style.FILL_AND_STROKE);
		paint.setAntiAlias(true);
		canvas.drawCircle(radiusX, radiusY, circleRadius-2-gapWidth-borderWidth, paint);
	}

	private void drawInnerData(Canvas canvas){
		String string=dataInfo.step;
		innerCircleRadius=circleRadius-2-gapWidth-borderWidth;
		
		Paint paint=new Paint();
		if(string!=null){
		paint.setColor(Color.BLACK);
		paint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP,16));
		paint.setAntiAlias(true);
		canvas.drawText(string,radiusX-(getFontHalfWidth(paint)*string.length())/2, radiusY-innerCircleRadius/2, paint);
		}
		
		//
		string=dataInfo.distance;
		Paint largepaint=new Paint();
		largepaint.setColor(getResources().getColor(R.color.hp_w_target_title));
		largepaint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP,80));
		largepaint.setTypeface(MyApplication.getTypeFace());
		largepaint.setAntiAlias(true);
		Paint smallPaint=new Paint();
		smallPaint.setColor(getResources().getColor(R.color.hp_w_target_title));
		smallPaint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP,12));
		smallPaint.setTypeface(MyApplication.getTypeFace());
		smallPaint.setAntiAlias(true);
		canvas.drawText(string,radiusX-(largepaint.measureText(string)+getFontHalfWidth(smallPaint)*2)/2, radiusY+getFontHalfHeight(largepaint)/2, largepaint);
		canvas.drawText("公里",radiusX- (getFontHalfWidth(smallPaint)*2)/2+largepaint.measureText(string)/2, radiusY+getFontHalfHeight(largepaint)/2, smallPaint);

		//
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.calorie_grayiconsmall);  
        canvas.drawBitmap(bmp, radiusX-innerCircleRadius/2-bmp.getWidth(), radiusY+innerCircleRadius/2-bmp.getHeight(), paint);  
 
        string=dataInfo.calory;
        paint=new Paint();
		paint.setColor(getResources().getColor(R.color.lightgray));
		paint.setAntiAlias(true);
		paint.setTextSize(getRawSize(TypedValue.COMPLEX_UNIT_SP,16));
		canvas.drawText(string,radiusX-innerCircleRadius/2, radiusY+innerCircleRadius/2, paint);
		
		string=dataInfo.duration;
		bmp=BitmapFactory.decodeResource(getResources(), R.drawable.sporttrack_wathch_time);  
        canvas.drawBitmap(bmp, radiusX+innerCircleRadius/2-bmp.getWidth(), radiusY+innerCircleRadius/2-bmp.getHeight(), paint);  
		canvas.drawText(string,radiusX+innerCircleRadius/2, radiusY+innerCircleRadius/2, paint);	
	}


	private void cleanDataAndCallback() {
		// if(isMove){
		// for (PointInfo temp : points) {
		// temp.setSelected(false);
		// temp.setCorrect(true);
		// temp.setNextId(temp.getId());
		// }
		// NinePointLineCtrl.getInstance(context).onCallback(flag, lockString);
		// lockString.delete(0, lockString.length());
		// isUp = false;
		// isMove=false;
		// }
	}

	private int getFontHalfHeight(Paint paint) {
		FontMetrics fm = paint.getFontMetrics();
		int length = (int) Math.ceil(fm.descent - fm.top) + 2;
		return length / 2;
	}

	private float getFontHalfWidth(Paint paint) {
		return paint.measureText("2");
	}

	
	public void setDataInfo( String distance, String calory,
			String duration ) {
		dataInfo.distance = distance;
		dataInfo.duration = duration;
		dataInfo.calory = calory;
	}

	public void setDataStep(String step){
		dataInfo.step = step;
	}
	
	public void setDataPercent(double percent){
		dataInfo.percent = percent; 
	}
	private class DataInfo {
		public String step="0步";
		public String distance="0";
		public String calory="0";
		public String duration="0'0''";
		public double percent=0.0f; //has finish
	}
	
	public static int pxToDp(int px){
		DisplayMetrics dMetrics=new DisplayMetrics();
		dMetrics.setToDefaults();
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, dMetrics);
	}
	
	public static int dpToPx(int dp){
		DisplayMetrics dMetrics=new DisplayMetrics();
		dMetrics.setToDefaults();
		return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, dMetrics);
	}

	public float getRawSize(int unit, float size) {
	       Context c = getContext();
	       Resources r;
	       if (c == null)
	           r = Resources.getSystem();
	       else
	           r = c.getResources();
	       return TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
	}
}
