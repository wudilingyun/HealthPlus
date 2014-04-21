package com.vee.healthplus.ui.heahth_exam;

import java.util.ArrayList;
import java.util.List;

import com.vee.healthplus.R;
import com.yunfox.s4aservicetest.response.Exam;
import com.yunfox.s4aservicetest.response.ExamType;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ExamListAdapter extends BaseExpandableListAdapter {

	Context context;

	List<ExamType> examTypes;// 考试类型列表[主]
	LayoutInflater inflater;
	List<Exam> exams;

	public ExamListAdapter(Context context) {
		super();

		this.context = context;
		inflater = LayoutInflater.from(context);

		this.examTypes = new ArrayList<ExamType>();
		exams = new ArrayList<Exam>();
	}

	public void ExamTypelistaddAdapter(List<ExamType> examTypes) {
		this.examTypes = examTypes;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {

		return examTypes.get(groupPosition).getExams();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		View view = inflater.inflate(R.layout.health_item_examlist, parent,
				false);
		TextView examtxt = (TextView) view.findViewById(R.id.txt_exam);
		exams = (List<Exam>) examTypes.get(groupPosition).getExams();
		if (exams != null) {
			String name = exams.get(childPosition).getName();
			System.out.println("取回来的名字是" + name);
			int id = exams.get(childPosition).getExam_id();
			examtxt.setText(name);
			examtxt.setTag(id);
		}

		return view;

	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return examTypes.get(groupPosition).getExams().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return examTypes.get(groupPosition).getName();
	}

	@Override
	public int getGroupCount() {
		return examTypes.size();

	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view;

		view = inflater.inflate(R.layout.health_item_examtype, parent, false);
		TextView examtxt = (TextView) view.findViewById(R.id.txt_examtype);
		// ImageView iv = (ImageView) view.findViewById(R.id.img_expand);
		
		Drawable open= context.getResources().getDrawable(R.drawable.open);
		Drawable close= context.getResources().getDrawable(R.drawable.close);
		/// 这一步必须要做,否则不会显示.
		open.setBounds(0, 0, open.getMinimumWidth(), open.getMinimumHeight());
		close.setBounds(0, 0, close.getMinimumWidth(), close.getMinimumHeight());
		
		
		
		examtxt.setText(getGroup(groupPosition).toString());

		int examTypeId = examTypes.get(groupPosition).getExamtype_id();
		examtxt.setTag(examTypeId);
		
		if (examTypes.get(groupPosition).getExams().size() == 0) {
			examtxt.setCompoundDrawables(open,null,null,null);
		}
		if (isExpanded) {
			examtxt.setCompoundDrawables(close,null,null,null);
		} else {
			examtxt.setCompoundDrawables(open,null,null,null);
		}

		return view;
	}

	@Override
	public boolean hasStableIds() {

		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {

		return true;
	}

}
