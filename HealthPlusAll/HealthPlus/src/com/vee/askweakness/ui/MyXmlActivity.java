package com.vee.askweakness.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;

import com.vee.healthplus.R;
import com.vee.healthplus.activity.BaseFragmentActivity;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("ResourceAsColor")
public class MyXmlActivity extends BaseFragmentActivity {
	private String TAG = "xml";
	private ListView detailPartListview;
	private BodyPartDetailAdapter adapter;
	private String selectData;
	private ArrayList<PartEntity> list;

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
		getCheckData();
		init(view);
		parseXml(selectData);

	}

	void init(View view) {
		detailPartListview = (ListView) view.findViewById(R.id.bodydetail_lv);
		adapter = new BodyPartDetailAdapter(this);

		detailPartListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				TextView text = (TextView) view
						.findViewById(R.id.simpleadapter_tv);
				List<PartEntity> list = (List<PartEntity>) text.getTag();
				
				Intent intent = new Intent(MyXmlActivity.this,
						MySymptomActivity.class);
				intent.putExtra("MySymptom", list.get(arg2).getId());
				startActivity(intent);

			}
		});
	}

	void getCheckData() {
		Intent intent = getIntent();
		selectData = intent.getStringExtra("body");
	}

	private void parseXml(String part) {
		int i, j;

		list = new ArrayList<PartEntity>();
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder;
		Document doc = null;
		InputStream inStream = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
			inStream = this.getResources().getAssets().open("Bodymap.xml");
			doc = docBuilder.parse(inStream);
			Element rootEle = doc.getDocumentElement();
			NodeList questionNode = rootEle.getElementsByTagName("group");
			int subCount = questionNode.getLength();
			for (i = 0; i < subCount; i++) {
				Element subEle = (Element) questionNode.item(i);
				String subTitle = subEle.getAttribute("mcName");
				String subTitlech = subEle.getAttribute("name");
				Log.e(TAG, "title = " + subTitle);
				if (subTitle.equals(part)) {
					i = subCount;
					NodeList itemNode = subEle.getElementsByTagName("part");
					int itemCount = itemNode.getLength();
					for (j = 0; j < itemCount; j++) {
						Element optionEle = (Element) itemNode.item(j);
						String desc = optionEle.getAttribute("name");
						String pos = optionEle.getAttribute("id");
						Log.e(TAG, "desc = " + desc + ", pos = " + pos);
						PartEntity entity = new PartEntity();
						entity.setName(desc);
						entity.setId(pos);
						list.add(entity);
					}
					adapter.addAdapter(list);
					detailPartListview.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				}

			}
		} catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
};
