package com.vee.askweakness.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.vee.healthplus.R;

import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

@SuppressLint("NewApi")
public class AskWeaknessFragment extends Fragment {
	private View view;
	private RadioGroup radioGroup;
	private ListView bodypartListView;
	private RadioButton femaleRadioButton, maleRadioButton;
	private BodyPartAdapter adapter;
	private String[] malepartStr = { "头", "颈部", "肩部", "胸部", "上肢", "背部", "腹部",
			"臀部", "下肢", "其它" };
	private String[] femalepartStr = { "头", "颈部", "肩部", "胸部", "乳房", "上肢", "背部",
			"腹部", "臀部", "下肢", "其它" };
	private ArrayList<PartEntity> bodypartlist;

	public static AskWeaknessFragment NewInstance() {
		return new AskWeaknessFragment();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = (View) inflater.inflate(R.layout.askweakness_fragment,
				container, false);
		init(view);
		data(view);
		return view;
	}

	void init(View view) {
		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_sex);
		bodypartListView = (ListView) view.findViewById(R.id.bodypart_lv);
		maleRadioButton = (RadioButton) view.findViewById(R.id.male_rb);
		femaleRadioButton = (RadioButton) view.findViewById(R.id.women_rb);
		adapter = new BodyPartAdapter(getActivity());

		bodypartlist = new ArrayList<PartEntity>();
		bodypartListView.setAdapter(adapter);
		adapter.addAdapter(getMaleData(parseXml()));
	}

	// 获得男性数据
	ArrayList<PartEntity> getMaleData(ArrayList<PartEntity> bplist) {

		for (int i = 0; i < bplist.size(); i++) {
			String mcNameString = bplist.get(i).getMcName();
			if (mcNameString.equals("w_belly") || mcNameString.equals("breast")) {
				bodypartlist.remove(i);
			}
		}
		return bplist;
	}

	// 获得女性数据
	ArrayList<PartEntity> getFeMaleData(ArrayList<PartEntity> bplist) {

		for (int i = 0; i < bplist.size(); i++) {
			String mcNameString = bplist.get(i).getMcName();
			if (mcNameString.equals("belly")) {
				bodypartlist.remove(i);
			}
		}
		return bplist;
	}

	void data(View view) {
		// 默认去除女同胞数据

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == femaleRadioButton.getId()) {
					bodypartlist.clear();
					adapter.addAdapter(getFeMaleData(parseXml()));
					adapter.notifyDataSetChanged();

				} else if (checkedId == maleRadioButton.getId()) {
					bodypartlist.clear();
					adapter.addAdapter(getMaleData(parseXml()));
					adapter.notifyDataSetChanged();
				}
			}
		});

		bodypartListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				//
				TextView text = (TextView) view
						.findViewById(R.id.simpleadapter_tv);
				List<PartEntity> list = (List<PartEntity>) text.getTag();
				if ((list.get(arg2).getPid()) != "") {
					// 直接取访问网络
					Toast.makeText(getActivity(), "访问网络取数据",
							Toast.LENGTH_SHORT).show();
				} else {

					Intent intent = new Intent(getActivity(),
							MyXmlActivity.class);
					intent.putExtra("body", list.get(arg2).getMcName()
							.toString());
					Toast.makeText(getActivity(), list.get(arg2).getMcName(),
							Toast.LENGTH_SHORT).show();
					startActivity(intent);
				}
			}
		});
	}

	private ArrayList<PartEntity> parseXml() {
		int i;

		bodypartlist = new ArrayList<PartEntity>();
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
				String mcName = subEle.getAttribute("mcName");
				String partName = subEle.getAttribute("name");
				String pid = subEle.getAttribute("pid");
				PartEntity entity = new PartEntity();
				entity.setMcName(mcName);
				entity.setPartName(partName);
				entity.setPid(pid);
				bodypartlist.add(entity);
			}
			return bodypartlist;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
