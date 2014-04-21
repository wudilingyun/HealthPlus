package com.vee.healthplus.ui.sportmode;

import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.load.UploadData;
import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.baidumap.MyLocationListenner;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.HealthDBConst;
import com.vee.healthplus.util.sporttrack.TrackEntity;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.CustomDialog;
import com.vee.healthplus.widget.HeaderView;
import com.vee.healthplus.widget.HeaderView.OnHeaderClickListener;

public class SportResultFragment extends BaseFragmentActivity {
	private View view;
	private TextView distanceTv, durationTv, calorieTv, countTv, rateTv,
			speedTv;
	private HealthDB dbHelper;
	private TrackEntity trackEntity;
	private String[] contents;
	private MapView mMapView;
	private MapController mMapController = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	private MyLocationOverlay mLocationOverlay;
	private LocationListener mLocationListener;
	private BMapManager mapManager = null;
	private LocationClient locationClient = null;
	private List<GeoPoint> geoPoints = new ArrayList<GeoPoint>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		view = View.inflate(this, R.layout.sport_result_details, null);
		setContainer(view);

		init();
		initData();
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		mMapView.onResume();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		mMapView.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mMapView.onSaveInstanceState(outState);

	}

	void init() {

		setHeaderClickListener(new OnHeaderClickListener() {
			@Override
			public void OnHeaderClick(View v, int option) {
				if (option == HeaderView.HEADER_BACK) {
					showFinish();
				}
			}
		});
		distanceTv = (TextView) view.findViewById(R.id.distance);
		durationTv = (TextView) view.findViewById(R.id.duration);
		distanceTv.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/minmin.ttf"));
		durationTv.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/minmin.ttf"));
		calorieTv = (TextView) view.findViewById(R.id.tv_calorie);
		countTv = (TextView) view.findViewById(R.id.tv_count);
		rateTv = (TextView) view.findViewById(R.id.tv_rate);
		speedTv = (TextView) view.findViewById(R.id.tv_speed);
		calorieTv.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/minmin.ttf"));
		countTv.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/minmin.ttf"));
		rateTv.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/minmin.ttf"));
		speedTv.setTypeface(Typeface.createFromAsset(getAssets(),
				"fonts/minmin.ttf"));
		
		
		mMapView = (MapView) findViewById(R.id.path_bmapView);
		mMapController = mMapView.getController();
		// 获取用于在地图上标注一个地理坐标点的图标
		// Drawable drawable =
		// this.getResources().getDrawable(R.drawable.maket);

		// 创建覆盖物（MyOverlayItem）对象并添加到覆盖物列表中
		// mMapView.getOverlays().add(new MyOverlayItem(drawable, mMapView));
		clearClick();

		// 定位图层及定位覆盖物
		 mLocationOverlay = new MyLocationOverlay(mMapView);
		 mMapView.getOverlays().add(mLocationOverlay);

	}

	private void initData() {
		dbHelper = HealthDB.getInstance(this);

		trackEntity = dbHelper.getSportRecordByIdLastest(dbHelper
				.getSportIndexLastestId(HP_User.getOnLineUserId(this)), HP_User
				.getOnLineUserId(this), TrackUtil.getInstance(this)
				.getTableName());
		if (trackEntity == null)
			trackEntity = new TrackEntity();
		distanceTv.setText(GpsUitl.distanceFormat(trackEntity.getDistance(),
				false, this));
		durationTv.setText(GpsUitl.durationTrackFormat(trackEntity
				.getDuration()));
		String velocity = TrackUtil.getVelocity(trackEntity.getDistance(),
				trackEntity.getDuration());
		int count = dbHelper.getSportIndexCount(HP_User.getOnLineUserId(this));
		contents = new String[] {
				GpsUitl.caloryFormat(trackEntity.getCalory(), true),
				GpsUitl.countFormat(String.valueOf(count), false), "0",
				GpsUitl.velocityFormat(velocity, true) };
		String []counts=contents[0].split("卡");
		calorieTv.setText(counts[0]);
		countTv.setText(contents[1]);
		String []speed=contents[3].split("公");
		speedTv.setText(speed[0]);

		// 通过表名。用户名获得运动轨迹对象
		int userid = HP_User.getOnLineUserId(getApplicationContext());
		int recordid = dbHelper.getSportIndexLastestId(userid);
		List<TrackEntity> tracList = dbHelper.getRecordById(recordid, userid,
				HealthDBConst.SPORTRECORD_TABLE);
		if (tracList != null) {
			for (int i = 0; i < tracList.size(); i++) {
				int lat = (int) ((Double.valueOf(tracList.get(i).getLatitude()) * 1E6));
				int lon = (int) ((Double
						.valueOf(tracList.get(i).getLongitude()) * 1E6));
				GeoPoint pt = new GeoPoint(lat, lon);
				geoPoints.add(pt);
			}
			mMapController.setCenter(geoPoints.get(0));
			mMapView.getController().enableClick(true);
			mMapView.getController().setZoom(18);
			mMapView.setBuiltInZoomControls(true);

			addCustomElementsDemo(geoPoints);

		} else {
			mMapView.getController().enableClick(true);
			mMapView.getController().setZoom(18);
			mMapView.setBuiltInZoomControls(true);
			Toast.makeText(this, "还没有运动", Toast.LENGTH_SHORT).show();
		}

	}

	private void showFinish() {
		View layout = View
				.inflate(this, R.layout.sportmode_result_dialog, null);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(this);
		customBuilder
				.setTitle("提示")
				.setContentView(layout)
				.setPositiveButton("上传", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						UploadData.getInstance(SportResultFragment.this)
								.uploadData(false);
						dialog.dismiss();
						finish();
					}
				})
				.setNegativeButton("放弃", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						finish();
					}
				});
		Dialog dialog = customBuilder.create();
		dialog.show();

	}

	class MyOverlayItem extends ItemizedOverlay<OverlayItem> {

		public MyOverlayItem(Drawable drawable, MapView mapView) {
			super(drawable, mapView);
			// TODO Auto-generated constructor stub
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (KeyEvent.KEYCODE_BACK == keyCode) {
			showFinish();
		}

		return false;

	}

	/**
	 * 绘制点线
	 * 
	 */

	public void addCustomElementsDemo(List<GeoPoint> data) {
		System.out.println("dfddafdafasfasdf");
		int count = data.size();
		GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.getOverlays().add(graphicsOverlay);
		// 添加折线
		graphicsOverlay.setData(drawLine(data));// 轨迹
		// 添加点
		graphicsOverlay.setData(drawPoint(data.get(0)));// 起点
		graphicsOverlay.setData(drawPoint(data.get(count - 1)));// 终点
		// 执行地图刷新使生效
		mMapView.refresh();
	}

	/**
	 * 绘制单点，该点状态不随地图状态变化而变化
	 * 
	 * @return 点对象
	 */
	public Graphic drawPoint(GeoPoint data) {
		// 构建点
		Geometry pointGeometry = new Geometry();
		// 设置坐标
		pointGeometry.setPoint(data, 10);
		// 设定样式
		Symbol pointSymbol = new Symbol();
		Symbol.Color pointColor = pointSymbol.new Color();
		pointColor.red = 255;
		pointColor.green = 0;
		pointColor.blue = 0;
		pointColor.alpha = 255;
		pointSymbol.setPointSymbol(pointColor);
		// 生成Graphic对象
		Graphic pointGraphic = new Graphic(pointGeometry, pointSymbol);
		return pointGraphic;
	}

	/**
	 * 绘制文字，该文字随地图变化有透视效果
	 * 
	 * @return 文字对象
	 */
	public TextItem drawText() {
		double mLat = 39.86923;
		double mLon = 116.397428;
		int lat = (int) (mLat * 1E6);
		int lon = (int) (mLon * 1E6);
		// 构建文字
		TextItem item = new TextItem();
		// 设置文字位置
		item.pt = new GeoPoint(lat, lon);
		// 设置文件内容
		item.text = "起点";

		// 设文字大小
		item.fontSize = 10;
		Symbol symbol = new Symbol();
		Symbol.Color bgColor = symbol.new Color();

		// 设置文字背景色
		bgColor.red = 0;
		bgColor.blue = 0;
		bgColor.green = 255;
		bgColor.alpha = 50;

		Symbol.Color fontColor = symbol.new Color();
		// 设置文字着色
		fontColor.alpha = 255;
		fontColor.red = 0;
		fontColor.green = 0;
		fontColor.blue = 255;
		// 设置对齐方式
		item.align = TextItem.ALIGN_CENTER;
		// 设置文字颜色和背景颜色
		item.fontColor = fontColor;
		item.bgColor = bgColor;
		return item;
	}

	/**
	 * 清除所有图层
	 */
	public void clearClick() {
		mMapView.getOverlays().clear();
	}

	/**
	 * 绘制折线，该折线状态随地图状态变化
	 * 
	 * @return 折线对象
	 */
	public Graphic drawLine(List<GeoPoint> data) {
		int count = data.size();
		// 构建线
		Geometry lineGeometry = new Geometry();
		// 设定折线点坐标
		GeoPoint[] linePoints = new GeoPoint[count];
		for (int i = 0; i < count; i++) {
			linePoints[i] = data.get(i);
		}

		lineGeometry.setPolyLine(linePoints);
		// 设定样式
		Symbol lineSymbol = new Symbol();
		Symbol.Color lineColor = lineSymbol.new Color();
		lineColor.red = 255;
		lineColor.green = 0;
		lineColor.blue = 0;
		lineColor.alpha = 255;
		lineSymbol.setLineSymbol(lineColor, 3);
		// 生成Graphic对象
		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
		return lineGraphic;
	}

	/**
	 * 标准的GPS经纬度坐标直接在地图上绘制会有偏移，这是测绘局和地图商设置的加密，要转换成百度地图坐标
	 * 
	 * @return 百度地图坐标
	 */
	public GeoPoint gpsToBaidu(String data) {// data格式 nmea标准数据
												// ddmm.mmmmm,ddmm.mmmm
												// 如3030.90909,11449.1234
		String[] p = data.split(",");
		/*
		 * int lat = (int) (((int) (Float.valueOf(p[0]) / 100) + (100 * (Float//
		 * 将ddmm.mmmm格式转成dd.ddddd .valueOf(p[0]) / 100.0 - (int)
		 * (Float.valueOf(p[0]) / 100)) / 60.0)) * 1E6); int lon = (int) (((int)
		 * (Float.valueOf(p[1]) / 100) + (100 * (Float .valueOf(p[1]) / 100.0 -
		 * (int) (Float.valueOf(p[1]) / 100)) / 60.0)) * 1E6);
		 */
		int lat = (int) ((Double.valueOf(p[0])) * 1E6);
		int lon = (int) ((Double.valueOf(p[1])) * 1E6);
		GeoPoint pt = new GeoPoint(lat, lon);
		// return CoordinateConvert.fromWgs84ToBaidu(pt);// 转成百度坐标
		return pt;

	}

}
