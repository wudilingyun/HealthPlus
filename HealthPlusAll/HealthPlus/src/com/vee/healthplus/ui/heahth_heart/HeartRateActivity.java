package com.vee.healthplus.ui.heahth_heart;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.vee.healthplus.R;
import com.vee.healthplus.R.color;
import com.vee.healthplus.R.layout;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.ui.heahth_exam.ExamTypeActivity;
import com.vee.healthplus.ui.heahth_exam.HealthFragment;
import com.vee.healthplus.ui.heahth_exam.ImageProcessing;
import com.vee.healthplus.ui.main.MainPage;

@SuppressLint("ResourceAsColor")
public class HeartRateActivity extends BaseFragmentActivity implements
		OnClickListener, PreviewCallback, SurfaceHolder.Callback,
		android.widget.RadioGroup.OnCheckedChangeListener {
	private TextView select_1, select_2, select_3, heart_count, heart_icon,
			txt_data, txt_timer, current_value_1, current_value_2,
			current_value_3, result_txt_data, result_txt_timer,
			heart_count_unit;
	private AlertDialog dialog, dialog2;
	private ToggleButton togglebutton;
	private Timer timer = new Timer();// 定时器
	private TimerTask timerTask;
	private static Camera mCamera = null;
	private Handler handler;
	private View view;
	private Animation displayResultAnimation, displayResultAnimation_1;
	private AnimationDrawable heartAnimation;
	private static int heartbeat = 0;
	private LinearLayout linearLayout_normal, linearLayout_row,
			linearLayout_hight, ready_ui, heart_result_table;
	private RelativeLayout result_Ui;
	private int imgAvg;
	private SimpleDateFormat sdfy, sdft;
	private Handler mHandler;
	PathView pathView;
	private RadioGroup radioGroup;
	private UploadHeartData upData;
	private PutDataforSQL dataforSQL;
	private int sumImg, imgResult;
	int count = 0;
	LinearLayout layout;
	private DrawChart drawChart;
	private WakeLock wakeLock;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private int titleH, serviceH, chartH;

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		view = View.inflate(this, R.layout.health_heartrate_main, null);
		getHeaderView().setHeaderTitle("心率检测");
		setRightBtnVisible(View.VISIBLE);
		getHeaderView().setBackGroundColor(R.color.blue);
		getHeaderView().setRightOption(5);
		getHeaderView().setRightRes(R.drawable.btn_heart_share_selector);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		setContainer(view);

		init();

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				switch (msg.what) {
				case 1:
					pathView.invalidate();
					ready_ui.setVisibility(View.GONE);
					result_Ui.setVisibility(View.VISIBLE);
					break;
				case 2:
					mCamera.setOneShotPreviewCallback(HeartRateActivity.this);
					// drawChart.invalidate();
					break;
				default:
					break;
				}
			};
		};
	}

	@SuppressLint({ "SimpleDateFormat", "ResourceAsColor" })
	void init() {
		surfaceView = (SurfaceView) this.findViewById(R.id.surface);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		displayResultAnimation = AnimationUtils.loadAnimation(this,
				R.anim.push_up_in);
		displayResultAnimation_1 = AnimationUtils.loadAnimation(this,
				R.anim.push_down);
		ready_ui = (LinearLayout) findViewById(R.id.ready_ui);
		result_Ui = (RelativeLayout) findViewById(R.id.result_Ui);
		radioGroup = (RadioGroup) findViewById(R.id.radio_deveice);
		radioGroup.setOnCheckedChangeListener(this);

		togglebutton = (ToggleButton) findViewById(R.id.toggle);
		togglebutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressLint("ResourceAsColor")
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
				// TODO Auto-generated method stub
				togglebutton.setChecked(isChecked);

				if (isChecked) {
					if (getflashLight()) {

						heartAnimation = (AnimationDrawable) heart_icon
								.getBackground();
						heartAnimation.setOneShot(false);
						count = 0;
						imgResult = 0;
						sumImg = 0;
						heart_count.setTextColor(getResources().getColor(
								R.drawable.start_bac));
						heart_count_unit.setTextColor(getResources().getColor(
								R.drawable.start_bac));
						txt_data.setTextColor(getResources().getColor(
								R.drawable.start_bac));
						txt_timer.setTextColor(getResources().getColor(
								R.drawable.start_bac));

						mCamera.startPreview();
						// 相机参数
						Camera.Parameters parameters = mCamera.getParameters();

						// 打开闪光灯
						parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
						try {
							mCamera.setParameters(parameters);
							startTimer();
							ready_ui.setVisibility(View.VISIBLE);
							result_Ui.setVisibility(View.GONE);
							heartAnimation.start();
						} catch (Exception e) {
							Toast.makeText(getApplication(), "暂时不支持此设备",
									Toast.LENGTH_SHORT).show();
							togglebutton.setChecked(false);
						}

					} else {
						Toast.makeText(getApplication(), "您的手机没有闪光灯无法测试",
								Toast.LENGTH_SHORT).show();
						togglebutton.setChecked(false);
					}
				} else {

					heart_count.setText("00");
					heartAnimation.stop();
					heart_icon.clearAnimation();
					if (count > 0)
						imgResult = sumImg / count;
					heartAnimation.stop();
					heart_count.setTextColor(getResources().getColor(
							R.drawable.normao_bac));
					heart_count_unit.setTextColor(getResources().getColor(
							R.drawable.normao_bac));

					heart_icon.setBackgroundDrawable(getResources()
							.getDrawable(R.drawable.health_heart_icon));
					new Thread() {
						public void run() {
							Message message = new Message();
							message.what = 1;
							handler.sendMessage(message);
						};
					}.start();

					// 获取摄像头参数
					Camera.Parameters parameter = mCamera.getParameters();
					// 设置摄像头参数 关闭闪光灯
					parameter.setFlashMode(Parameters.FLASH_MODE_OFF);
					mCamera.setParameters(parameter);
					heart_icon.clearAnimation();
					pauseTimer();
					getShowResult();

				}
			}
		});
		heart_count = (TextView) findViewById(R.id.heart_count);
		heart_count_unit = (TextView) findViewById(R.id.heart_count_unit);
		heart_icon = (TextView) findViewById(R.id.heart_icon);

		txt_data = (TextView) findViewById(R.id.txt_data);
		txt_timer = (TextView) findViewById(R.id.txt_timer);
		txt_data.setTextColor(getResources()
				.getColor(R.color.hp_w_target_title));
		txt_timer.setTextColor(getResources().getColor(
				R.color.hp_w_target_title));
		heart_count.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/Radioland.ttf"));
		heart_count_unit.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/Radioland.ttf"));
		result_txt_data = (TextView) findViewById(R.id.result_txt_data);
		result_txt_timer = (TextView) findViewById(R.id.result_txt_timer);

		sdfy = new SimpleDateFormat("yyyy-MM-dd");
		sdft = new SimpleDateFormat("HH:mm:ss");
		txt_data.setText(sdfy.format(new Date()));
		txt_timer.setText(sdft.format(new Date()));
		// 初始化曲线图
		layout = (LinearLayout) findViewById(R.id.char_bac);
		heart_result_table = (LinearLayout) findViewById(R.id.heart_result_table);
		drawChart = new DrawChart(this);
		layout.addView(drawChart);
		pathView = new PathView(this);
		heart_result_table.addView(pathView);

	}

	private void getmCamera() {
		if (mCamera == null) {
			try {
				mCamera = Camera.open();
			} catch (RuntimeException e) {
				Log.i("v", "mCamera.open() failed: " + e.getMessage());
			}
		}
	}

	void getShowResult() {

		// 根据结果值显示小红旗的位置。
		linearLayout_normal = (LinearLayout) findViewById(R.id.your_position_2);
		linearLayout_row = (LinearLayout) findViewById(R.id.your_position_1);
		linearLayout_hight = (LinearLayout) findViewById(R.id.your_position_3);
		current_value_1 = (TextView) findViewById(R.id.current_value_1);
		current_value_2 = (TextView) findViewById(R.id.current_value_2);
		current_value_3 = (TextView) findViewById(R.id.current_value_3);

		current_value_2.setText(imgResult + "");
		current_value_1.setText(imgResult + "");
		current_value_3.setText(imgResult + "");
		if (imgResult >= 60 && imgResult <= 90) {

			linearLayout_normal.setVisibility(View.VISIBLE);
			linearLayout_hight.setVisibility(View.INVISIBLE);
			linearLayout_row.setVisibility(View.INVISIBLE);

		} else if (imgResult < 60) {
			linearLayout_row.setVisibility(View.VISIBLE);
			linearLayout_normal.setVisibility(View.INVISIBLE);
			linearLayout_hight.setVisibility(View.INVISIBLE);

		} else if (imgResult > 90) {

			linearLayout_hight.setVisibility(View.VISIBLE);
			linearLayout_normal.setVisibility(View.INVISIBLE);
			linearLayout_row.setVisibility(View.INVISIBLE);
		}
		saveHeartData();
		result_txt_data.setText(sdfy.format(new Date()));
		result_txt_timer.setText(sdft.format(new Date()));

	}

	public void saveHeartData() {
		upData = new UploadHeartData();
		upData.addData(this, imgResult + "");
		dataforSQL = new PutDataforSQL();
		// 把系统当前时间（毫秒表示）作为测试的时间
		long currDate = System.currentTimeMillis();

		dataforSQL.putHeartData(this, imgResult + "", currDate);

		String abc = dataforSQL.getHeartData(this, currDate);
		System.out.println("abc当前从数据库得到心率是" + abc);
	}

	Boolean getflashLight() {

		PackageManager pm = this.getPackageManager();
		System.out.println("打印获取状态" + PackageManager.FEATURE_CAMERA_FLASH);
		// if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
			if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
				return true;
			} else {
				// TODO:无闪光灯
				return false;
			}
		} else {
			if ((PackageManager.FEATURE_CAMERA_FLASH)
					.equals("android.hardware.camera.flash")) {
				return true;
			} else {
				// TODO:无闪光灯
				return false;
			}
		}

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onPreviewFrame(byte[] data, Camera arg1) {
		if (data == null)
			throw new NullPointerException();
		// 获取摄像头数据的尺寸
		Camera.Size size = arg1.getParameters().getPreviewSize();
		// 如果获取不到摄像头数据尺寸抛出异常
		if (size == null)
			throw new NullPointerException();
		// 获取摄像头数据长度与高度
		int width = size.width;
		int height = size.height;
		Log.i("width", width + "");
		Log.i("height", height + "");
		// 接口回调并获取到相机数据后调用YUV转换RGB方法将数据进行转换。
		imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height,
				width);
		if (imgAvg == 0) {
			Toast.makeText(this, "请将手指覆盖在摄像头", Toast.LENGTH_SHORT).show();
		}

		Log.i("imgAvg", "imgAvg========" + imgAvg);
		// 限定心率值范围，只有值在合理的也就是40到150之间的值才会显示出来
		if (imgAvg > 40 && imgAvg < 151 || imgAvg == 0) {
			// 将Imagvg写入图像方法中做出不规则波线
			setHeartbeat(imgAvg);
			count++;
			sumImg += imgAvg;
			Log.i("imgAvg", "imgAvg========" + imgAvg);
			heart_count.setText(String.valueOf(imgAvg));
			drawChart.invalidate();
		}
		imgAvg = 0;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(mCamera==null){
		mCamera = Camera.open();}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 暂停定时器
		pauseTimer();
		// 将相机的回调接口设为NULL即不再接受回调数据
		mCamera.setPreviewCallback(null);
		mCamera.stopPreview();
		// 断开相机并释放对象
		mCamera.release();
		mCamera = null;
	}

	public void startTimer() {
		if (timer == null) {
			timer = new Timer();
		}
		if (timerTask == null) {
			timerTask = new TimerTask() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					Message message = new Message();
					message.what = 2;
					handler.sendMessage(message);

				}
			};
		}
		if (timer != null && timerTask != null) {
			timer.schedule(timerTask, 500, 10);
		}
	}

	/***
	 * 结束测试方法
	 */
	public void pauseTimer() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
		if (timerTask != null) {
			timerTask.cancel();
			timerTask = null;

		}

	}

	public static int getHeartbeat() {
		return heartbeat;
	}

	public static void setHeartbeat(int heartbeat) {
		HeartRateActivity.heartbeat = heartbeat;
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
		Toast.makeText(this, radioButton.getText(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		try {
			if (mCamera == null) {
				getmCamera();
			}
			mCamera.setPreviewDisplay(surfaceHolder);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub

	}
	
	
}
