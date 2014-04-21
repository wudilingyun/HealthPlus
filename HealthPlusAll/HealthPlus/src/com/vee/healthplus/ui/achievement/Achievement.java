package com.vee.healthplus.ui.achievement;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.vee.healthplus.R;
import com.vee.healthplus.common.FragmentMsg;
import com.vee.healthplus.common.IFragmentMsg;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.baidumap.MyLocationListenner;
import com.vee.healthplus.util.sporttrack.ISportTrack;
import com.vee.healthplus.util.sporttrack.TrackEntity;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class Achievement extends BaseFragmentActivity implements IFragmentMsg {

    private static final String TAG = "xuxuxu";

    // 定位相关
    LocationClient mLocClient;

    LocationData locData = null;

    BDLocation curLocation = null;

    public MyLocationListenner myListener = new MyLocationListenner();

    public double distance = 0.123456789;

    public FragmentMsg fragmentArgs = null;

    // 定位图层
    MyLocationOverlay myLocationOverlay = null;

    // 地图相关，使用继承MapView的MyLocationMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MyLocationMapView mMapView = null; // 地图View

    private MapController mMapController = null;

    MyApplication app = null;

    // UI相关
    OnCheckedChangeListener radioButtonListener = null;

    Button requestLocButton = null;

    private TextView datePicker;

    boolean isRequest = false;// 是否手动触发请求定位

    boolean isFirstLoc = true;// 是否首次定位

    private Fragment curFragment;

    private void updateFragmentToStack(FragmentMsg fMsg) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (fMsg.getAnimIn() != 0 && fMsg.getAnimOut() != 0) {
            ft.setCustomAnimations(fMsg.getAnimIn(), fMsg.getAnimOut());
        }
        if(curFragment != null)
            ft.remove(curFragment);
        curFragment = fMsg.getObjFragment();
        ft.add(R.id.calendar_layout, fMsg.getObjFragment());
        ft.commit();
    }

    private void updateFragmentToStack(Fragment fragment, int animin,int animout) {
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        if (animin != 0 && animout != 0) {
            ft.setCustomAnimations(animin, animout);
        }
        if(curFragment != null)
            ft.remove(curFragment);
        curFragment = fragment;
        ft.replace(R.id.calendar_layout, fragment);
        ft.commit();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.achievement);
        updateHeaderTitle(this.getResources().getString(R.string.achievement));
        setContainer(View.inflate(this, R.layout.achievement, null));
        // addFragmentToStack(new AchievementInfo());
        updateFragmentToStack(new AchievementCalendar(), R.anim.slide_right_in, R.anim.slide_right_out);
        app = (MyApplication) this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(MyApplication.strKey, new MyApplication.MyGeneralListener());
        }
        setRightBtnVisible(View.INVISIBLE);
/*        if (app.m_bKeyRight) {
            Toast.makeText(this, "key right", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "key wrong", Toast.LENGTH_SHORT).show();
        }*/
//		CharSequence titleLable = "定位功能";
//		setTitle(titleLable);
        datePicker = (TextView) findViewById(R.id.date_picker);
        datePicker.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                updateFragmentToStack(new AchievementCalendarPicker(), 0, 0);
            }
        });
        Calendar cale = Calendar.getInstance();
        datePicker.setText(cale.get(Calendar.YEAR) + "-" + (cale.get(Calendar.MONTH) + 1));

        // 地图初始化
        mMapView = (MyLocationMapView) findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        mMapView.getController().setZoom(14);
        mMapView.getController().enableClick(true);
        mMapView.setBuiltInZoomControls(true);

        myListener.setSportTrackListener(trackListener);

    }

    public void addCustomElementsDemo(BDLocation loc) {
        if (mMapView == null)
            return;
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
        mMapView.getOverlays().add(graphicsOverlay);
        // 添加点
        // graphicsOverlay.setData(drawPoint());
        // 添加折线
        graphicsOverlay.setData(drawLine(loc));
        // 添加多边形
        // graphicsOverlay.setData(drawPolygon());
        // 添加圆
        // graphicsOverlay.setData(drawCircle());
        // 绘制文字
        // TextOverlay textOverlay = new TextOverlay(mMapView);
        // mMapView.getOverlays().add(textOverlay);
        // textOverlay.addText(drawText());
        // 执行地图刷新使生效
        mMapView.refresh();
    }

    public void drawTrack(List<TrackEntity> tracks) {
        if (mMapView == null)
            return;
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
        mMapView.getOverlays().add(graphicsOverlay);
        graphicsOverlay.removeAll();
        mMapView.refresh();
        // 构建线
        Geometry lineGeometry = new Geometry();
        // 设定折线点坐标
        List<GeoPoint> lineps = new ArrayList<GeoPoint>();
        if (tracks == null) {
            return;
        }
        for (int i = 0; i < tracks.size(); i++) {
            if (tracks.get(i).getLatitude().equals("4.9E-324") || tracks.get(i).getLongitude().equals("4.9E-324"))
                continue;
            double lat = Double.parseDouble(tracks.get(i).getLatitude()) * 1e6;
            double lon = Double.parseDouble(tracks.get(i).getLongitude()) * 1e6;
            // Log.i(TAG, tracks.get(i).getLatitude() + ":" +
            // tracks.get(i).getLongitude());
            lineps.add(new GeoPoint((int) lat, (int) lon));
        }
        GeoPoint[] linePoints = new GeoPoint[lineps.size()];
        for (int i = 0; i < lineps.size(); i++) {
            linePoints[i] = lineps.get(i);
        }
        mMapController.animateTo(linePoints[0]);
        lineGeometry.setPolyLine(linePoints);
        // 设定样式
        Symbol lineSymbol = new Symbol();
        Symbol.Color lineColor = lineSymbol.new Color();
        lineColor.red = 255;
        lineColor.green = 0;
        lineColor.blue = 0;
        lineColor.alpha = 255;
        lineSymbol.setLineSymbol(lineColor, 10);
        // 生成Graphic对象
        Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
        graphicsOverlay.setData(lineGraphic);
        mMapView.refresh();
    }

    public Graphic drawLine(BDLocation location) {
        if (location == null || curLocation == null) {
            curLocation = location;
            return null;
        }
        if ((location.getLatitude() == curLocation.getLatitude()) && (location.getLongitude() == curLocation.getLongitude())) {
            return null;
        }
        double mLat = curLocation.getLatitude();
        double mLon = curLocation.getLongitude();

        int lat = (int) (mLat * 1E6);
        int lon = (int) (mLon * 1E6);
        GeoPoint pt1 = new GeoPoint(lat, lon);

        mLat = location.getLatitude();
        mLon = location.getLongitude();
        lat = (int) (mLat * 1E6);
        lon = (int) (mLon * 1E6);
        GeoPoint pt2 = new GeoPoint(lat, lon);

        distance += DistanceUtil.getDistance(pt1, pt2);
        curLocation = location;
        // 构建线
        Geometry lineGeometry = new Geometry();
        // 设定折线点坐标
        GeoPoint[] linePoints = new GeoPoint[2];
        linePoints[0] = pt1;
        linePoints[1] = pt2;
        lineGeometry.setPolyLine(linePoints);
        // 设定样式
        Symbol lineSymbol = new Symbol();
        Symbol.Color lineColor = lineSymbol.new Color();
        lineColor.red = 255;
        lineColor.green = 0;
        lineColor.blue = 0;
        lineColor.alpha = 255;
        lineSymbol.setLineSymbol(lineColor, 10);
        // 生成Graphic对象
        Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
        return lineGraphic;
    }

    /**
     * 手动触发一次定位请求
     */
    public void requestLocClick() {
        isRequest = true;
        mLocClient.requestLocation();
        Toast.makeText(Achievement.this, "正在定位……", Toast.LENGTH_SHORT).show();
    }

    /**
     * 修改位置图标
     *
     * @param marker
     */
    public void modifyLocationOverlayIcon(Drawable marker) {
        // 当传入marker为null时，使用默认图标绘制
        myLocationOverlay.setMarker(marker);
        // 修改图层，需要刷新MapView生效
        mMapView.refresh();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        if (mLocClient != null)
            mLocClient.stop();
        mMapView.destroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mMapView.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public void replaceFragment(FragmentMsg fMsg) {
        // TODO Auto-generated method stub
        if (fMsg.getFlag() == FragmentMsg.CALENDAR_PICKER_TO_CALENDAR) {
            Calendar calen = fMsg.getPickCalen();
            datePicker.setText(calen.get(Calendar.YEAR) + "-" + (calen.get(Calendar.MONTH) + 1));
        }
        updateFragmentToStack(fMsg);
        if (fMsg != null)
            fragmentArgs = fMsg;
    }

    ISportTrack trackListener = new ISportTrack() {

        @Override
        public void onStep(int step) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onLocationChange(BDLocation location) {
            // TODO Auto-generated method stub
            if (location == null)
                return;
            addCustomElementsDemo(location);
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            // 如果不显示定位精度圈，将accuracy赋值为0即可
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            // 更新定位数据
            myLocationOverlay.setData(locData);
            // 更新图层数据执行刷新后生效
            mMapView.refresh();
            // 是手动触发请求或首次定位时，移动到定位点
            // 移动地图到定位点
            Log.d("LocationOverlay", "receive location, animate to it");
            mMapController.animateTo(new GeoPoint((int) (locData.latitude * 1e6), (int) (locData.longitude * 1e6)));
        }
    };

}

/**
 * 继承MapView重写onTouchEvent实现泡泡处理操作
 */
class MyLocationMapView extends MapView {
    static PopupOverlay pop = null;// 弹出泡泡图层，点击图标使用

    public MyLocationMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLocationMapView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyLocationMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!super.onTouchEvent(event)) {
            // 消隐泡泡
            if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
                pop.hidePop();
        }
        return true;
    }

}
