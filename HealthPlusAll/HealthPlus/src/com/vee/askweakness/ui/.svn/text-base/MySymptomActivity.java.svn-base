package com.vee.askweakness.ui;

import java.util.ArrayList;

import javax.security.auth.PrivateCredentialPermission;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;
import com.vee.healthplus.util.sporttrack.HealthDBConst;
import com.vee.myhealth.util.DBManager;

public class MySymptomActivity extends BaseFragmentActivity {
	private String mySymptom;
	private SQLiteDatabase database;
	private ArrayList<PartEntity> list;
	private ListView listView;
	private BodyPartDetailAdapter adapter;

	@SuppressLint("ResourceAsColor")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		View view = View
				.inflate(this, R.layout.askweakness_part_activity, null);
		setContainer(view);
		getHeaderView().setHeaderTitle("主证");
		getHeaderView().setBackGroundColor(R.color.blue);
		setRightBtnVisible(View.GONE);
		setLeftBtnVisible(View.VISIBLE);
		setLeftBtnType(1);
		
		list = new ArrayList<PartEntity>();
		getSelectData();

		getSymptomFromDB(mySymptom);
		init(view);

	}

	void init(View view) {
		listView = (ListView) view.findViewById(R.id.bodydetail_lv);
		adapter = new BodyPartDetailAdapter(this);
		listView.setAdapter(adapter);
		adapter.addAdapter(list);
		adapter.notifyDataSetChanged();
	}

	void getSelectData() {
		Intent intent = getIntent();
		mySymptom = intent.getStringExtra("MySymptom");
	}

	// 从数据库查询症状
	void getSymptomFromDB(String mySymptom) {
		database = SQLiteDatabase.openOrCreateDatabase(DBManager.DB_PATH + "/"
				+ DBManager.DB_NAME, null);
		list = getList();
		database.close();

	}

	private ArrayList<PartEntity> getList() {

		try {
			String sql = "select * from " + DBManager.SYMPTOM_BDA_TABLE
					+ " where PartID='" + mySymptom + "'";
			System.out.println("sql" + sql);
			Cursor cursor = database.rawQuery(sql, null);

			if (cursor.getCount() < 1) {
				cursor.close();
				return null;
			} else {
				while (cursor.moveToNext()) {
					String name = cursor.getString(3);
					String key_no = cursor.getString(2);
					Log.v("查询到数据", name);
					PartEntity entity = new PartEntity();
					entity.setName(name);
					entity.setId(key_no);
					list.add(entity);
				}
				cursor.close();
				return list;
			}

		} catch (Exception e) {

		}
		return null;

	}

}
