package com.vee.healthplus.ui.sportmode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vee.healthplus.R;
import com.vee.healthplus.common.IFragmentMsg;
import com.vee.healthplus.common.MyApplication;
import com.vee.healthplus.ui.achievement.Achievement;
import com.vee.healthplus.ui.sporttrack.SportTrackMain;
import com.vee.healthplus.util.GpsUitl;
import com.vee.healthplus.util.HPConst;
import com.vee.healthplus.util.SystemMethod;
import com.vee.healthplus.util.sportmode.HP_TargetConfig;
import com.vee.healthplus.util.sportmode.ModeControl;
import com.vee.healthplus.util.sportmode.ModeControl.onModeListener;
import com.vee.healthplus.util.sporttrack.CalorieDBExternal;
import com.vee.healthplus.util.sporttrack.HealthDB;
import com.vee.healthplus.util.sporttrack.SportEntity;
import com.vee.healthplus.util.sporttrack.SportTypeEntity;
import com.vee.healthplus.util.sporttrack.TrackEntity;
import com.vee.healthplus.util.sporttrack.TrackUtil;
import com.vee.healthplus.util.user.HP_User;
import com.vee.healthplus.widget.PopupMenu;
import com.vee.healthplus.widget.PopupMenu.OnPopupMenuClickListener;
import com.vee.healthplus.widget.ShadowTextView;

public class SportModeFragment extends Fragment implements
		OnPopupMenuClickListener {
	private static final String TAG = "xuxuxu";
	private View view = null;

	ImageView lastSportType;

	TextView modeTV;

	TextView sportTV;

	// TextView moodTV; //useless

	ShadowTextView distanceTV;

	TextView caloryTV;

	TextView durationTV;

	TextView countTV;

	String calory;

	String duration;

	String distance;

	String count;

	String mode;

	String sporttype;

	// String mood; //useless

	PopupMenu popupMenu;

	int sportType = 0; // default run

	HealthDB dbHelper;

	List<SportTypeEntity> sportTypes;

	HashMap<String, String> sportSumHash;

	private Activity activity;

	private IFragmentMsg fragmentMsg;
	private TextView value_tv;

	private TextView lastSportTime;
	private TextView lastSportDistance;
	private RelativeLayout rlAchivement;

	public SportModeFragment() {

	}

	public static SportModeFragment newInstance() {
		return new SportModeFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.sportmode_main, container, false);
		// test
		SportTypeEntity entity = new SportTypeEntity();
		entity.setId(HPConst.SPORT_TYPE_RUN);
		entity.setName("跑步");
		HealthDB.getInstance(this.getActivity()).addSport(entity);
		entity = new SportTypeEntity();
		entity.setId(HPConst.SPORT_TYPE_BIKE);
		entity.setName("骑行");
		HealthDB.getInstance(this.getActivity()).addSport(entity);
		entity = new SportTypeEntity();
		entity.setId(HPConst.SPORT_TYPE_WALK);
		entity.setName("走步");
		HealthDB.getInstance(this.getActivity()).addSport(entity);

		Test();
		initView();
		// initData();
		SystemMethod.setFonts((ViewGroup) this.getActivity().getWindow()
				.getDecorView(), getActivity());

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		fragmentMsg = (IFragmentMsg) activity;
		super.onAttach(activity);
	}

	private void Test() {
		// TrackEntity entity=new TrackEntity();
		// entity.setUserId(0);
		// entity.setMode(1);
		// entity.setId(0);
		// entity.setTime(new Date())

	}

	private void initView() {
		lastSportType = (ImageView) view.findViewById(R.id.last_sport_type);
		distanceTV = (ShadowTextView) view.findViewById(R.id.distance);
		distanceTV.setTypeface(MyApplication.getTypeFace());
		value_tv = (TextView) view.findViewById(R.id.value_tv);

		modeTV = getTextView(R.id.mode, 0, 0);
		sportTV = getTextView(R.id.sporttype, 0, 0);
		// moodTV = getTextView(R.id.mood, null);

		caloryTV = getTextView(R.id.calory, R.string.sportmode_calory_sum,
				R.drawable.hp_main_burning);
		caloryTV.setTypeface(MyApplication.getTypeFace());
		durationTV = getTextView(R.id.duration,
				R.string.sportmode_duration_sum, R.drawable.hp_main_duration);
		durationTV.setTypeface(MyApplication.getTypeFace());
		countTV = getTextView(R.id.count, R.string.sportmode_count_sum,
				R.drawable.hp_main_count);
		countTV.setTypeface(MyApplication.getTypeFace());

		view.findViewById(R.id.btnGo).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						beginRecord();
					}
				});

		lastSportTime = (TextView) view.findViewById(R.id.lastSportTime);
		lastSportDistance = (TextView) view
				.findViewById(R.id.lastSportDistance);
		rlAchivement = (RelativeLayout) view
				.findViewById(R.id.main_achievement);
		rlAchivement.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), Achievement.class);
				startActivity(intent);
			}
		});

	}

	private void initData() {
		CalorieDBExternal.getInstance(getActivity());
		dbHelper = HealthDB.getInstance(this.getActivity());
		sportTypes = dbHelper.getSportTypes();
		sportSumHash = dbHelper.getSportSumbyIndex(dbHelper
				.getSportIndexAll(HP_User.getOnLineUserId(getActivity())),
				TrackUtil.getInstance(getActivity()).getTableName());
		modeTV.setText("运动模式");
		if (HP_TargetConfig.getInstance().getMode(getActivity()) == 0) {
			value_tv.setText(getResources().getString(
					R.string.hp_main_usually_mode));
		} else if (HP_TargetConfig.getInstance().getMode(getActivity()) == 1) {
			if (HP_TargetConfig.getInstance().getTargetMode(getActivity()) == HP_TargetConfig.TargetMode.DAY
					.ordinal()) {
				value_tv.setText(getResources().getString(
						R.string.hp_target_main_dialog_plan_day));
			} else if (HP_TargetConfig.getInstance().getTargetMode(
					getActivity()) == HP_TargetConfig.TargetMode.WEEK.ordinal()) {
				value_tv.setText(getResources().getString(
						R.string.hp_target_main_dialog_plan_week));
			}
		}
		HP_TargetConfig.getInstance().addModeListener(getActivity(), value_tv);
		((RelativeLayout) view.findViewById(R.id.mode))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						TargetModelDialog modelDialog = new TargetModelDialog();
						modelDialog.show(getFragmentManager(), "");
						// showAddMenu(SystemMethod.getStringArray(SportModeMain.this.getActivity(),
						// R.array.sportmode), R.string.sportmode_choosemode,
						// POPMENU_CHOSE_MODE);
					}
				});

		int size = sportTypes.size();
		final String[] typenames = new String[size];
		for (int i = 0; i < size; i++) {
			typenames[i] = sportTypes.get(i).getName();
		}

		sportTV.setText("运动类型");
		((RelativeLayout) view.findViewById(R.id.sporttype))
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						showAddMenu(typenames, R.string.sportmode_choosetype,
								HPConst.STRING_TYPE_SRPOT);

					}
				});

		// moodTV.setText("心情");

		caloryTV.setText(GpsUitl.caloryFormat(sportSumHash.get("calory"), false));
		durationTV.setText(GpsUitl.durationTrackFormat(sportSumHash
				.get("duration")));
		distanceTV.setText(GpsUitl.distanceFormat(sportSumHash.get("distance"),
				false, getActivity()));
		
		countTV.setText(GpsUitl.countFormat(sportSumHash.get("count"), false));
		//
		ModeControl.getInstance(getActivity()).setListener(
				new onModeListener() {
					@Override
					public void onModeChange(int type) {
						if (type == HPConst.STRING_TYPE_SRPOT)
							sportTV.setText(dbHelper.getSportName(sportType));
						// sportTV.setCompoundDrawablesWithIntrinsicBounds(null,
						// null,
						// activity.getResources().getDrawable(HPConst.SPORTTYPE_ICONS[sportType]),
						// null);
						RelativeLayout layout = (RelativeLayout) view
								.findViewById(R.id.sporttype);
						TextView sportTypeIcon = (TextView) layout
								.findViewById(R.id.value_tv);
						sportTypeIcon.setCompoundDrawablesWithIntrinsicBounds(
								null,
								null,
								activity.getResources().getDrawable(
										HPConst.SPORTTYPE_ICONS[sportType]),
								null);
					}
				});

		initSportArgs();

		int id = HealthDB.getInstance(getActivity())
				.getSportIndexLastestSportId(
						HP_User.getOnLineUserId(getActivity()));
	/*	TrackEntity entity = HealthDB.getInstance(getActivity())
				.getSportRecordByIdLastest(id,
						HP_User.getOnLineUserId(getActivity()),
						TrackUtil.getInstance(getActivity()).getTableName());*/
		
		
		TrackEntity entity = HealthDB.getInstance(getActivity()).
				getSportRecordByIdLastest(dbHelper
						.getSportIndexLastestId(HP_User.getOnLineUserId(getActivity())), HP_User
						.getOnLineUserId(getActivity()), TrackUtil.getInstance(getActivity())
						.getTableName());
				
		SportEntity sportEntity = HealthDB.getInstance(getActivity())
				.getSportIndexLastest(HP_User.getOnLineUserId(getActivity()));
		if (sportEntity == null) {
			lastSportTime.setText("您还没有运动过^_^");
			lastSportDistance.setVisibility(View.GONE);
			lastSportType.setVisibility(View.INVISIBLE);
		} else {
			lastSportDistance.setVisibility(View.VISIBLE);
			lastSportType.setVisibility(View.VISIBLE);
			Date date = new Date(Long.parseLong(sportEntity.getTime()));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			lastSportTime.setText("上次运动时间:" + sdf.format(date));
			if (entity == null || entity.getDistance() == null) {
				lastSportDistance.setText("上次没有运动距离^_^");
			} else {
				lastSportDistance.setText("上次运动距离:" + entity.getDistance());
			}
			lastSportType
					.setImageResource(HPConst.SPORTTYPE_ICONS_MAIN[sportEntity
							.getSportid()]);
		}
	}

	private void initSportArgs() {
		RelativeLayout layout = (RelativeLayout) view
				.findViewById(R.id.sporttype);
		TextView sportTypeIcon = (TextView) layout.findViewById(R.id.value_tv);
		sportTypeIcon.setCompoundDrawablesWithIntrinsicBounds(
				null,
				null,
				activity.getResources().getDrawable(
						HPConst.SPORTTYPE_ICONS[sportType]), null);
	}

	private void beginRecord() {
		SportEntity entity = new SportEntity();
		entity.setId(dbHelper.getSportIndexLastestId(HP_User
				.getOnLineUserId(this.getActivity())) + 1);
		entity.setSportid(sportTypes.get(sportType).getId());
		entity.setTime(String.valueOf((new Date()).getTime()));
		entity.setMode(0); // 需要获取mode值
		entity.setUserId(HP_User.getOnLineUserId(this.getActivity()));
		entity.setSync(HPConst.SPORT_INDEX_SYNC_NOT);
		dbHelper.addSportRecordIndex(entity);

		startActivityForResult(new Intent(SportModeFragment.this.getActivity(),
				SportTrackMain.class), 3);
	}

	private TextView getTextView(int resID, int titleRes, int iconRes) {
		RelativeLayout layout = (RelativeLayout) view.findViewById(resID);
		if (titleRes != 0) {
			TextView titleView = (TextView) layout.findViewById(R.id.title);
			// titleView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(iconRes),
			// null, null, null);
			titleView.setText(getResources().getString(titleRes));
			ImageView img = (ImageView) layout.findViewById(R.id.middle_icon);
			img.setImageResource(iconRes);
		}
		return (TextView) layout.findViewById(R.id.value);
	}

	public void showAddMenu(String[] menuList, int title, int addtype) {
		if (popupMenu == null) {
			popupMenu = new PopupMenu(this.getActivity());
			popupMenu.setItemLayout(R.layout.sportmode_sporttype_item);
			popupMenu.setHeaderTitle(title);
			for (String menuItem : menuList) {
				popupMenu.addItem(addtype, menuItem);
			}
			popupMenu.setItemIcon(HPConst.SPORTTYPE_ICONS_Blue);
			popupMenu.setOnPopupMenuClickListener(this);
			// popupMenu.setShowPosition();
		}
		// popupMenu.removeAllItem(addtype);
		popupMenu.show();
	}

	@Override
	public void OnPopupMenuClick(View v, int groupId, int itemId) {
		if (groupId == HPConst.STRING_TYPE_MODE) {
			new TargetDialogFragment().show(getFragmentManager(), "");
		} else if (groupId == HPConst.STRING_TYPE_SRPOT) {
			if (sportType != itemId) {
				sportType = itemId;
			}
			ModeControl.getInstance(getActivity()).onChange(
					HPConst.STRING_TYPE_SRPOT);
			popupMenu.dismiss();
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 3 && resultCode == -1) {
			// FragmentMsg fMsg = new FragmentMsg();
			// fMsg.setObjFragment(SportResultFragment.newInstance());
			// fMsg.setSrcFragment(this);
			// fragmentMsg.replaceFragment(fMsg);
			Intent intent = new Intent();
			intent.setClass(getActivity(), SportResultFragment.class);
			startActivity(intent);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
