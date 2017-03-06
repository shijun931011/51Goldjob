package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.example.user.a51goldjob.R;

import com.example.user.a51goldjob.adapter.BeanAdapter;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.activity_subject)
public class AdditionalSubjectActivity extends BaseActivity implements TextWatcher{


	protected List dataList = createDataList(); // 数据集
	private BaseAdapter adapter;//适配器
	@ViewInject(R.id.subject_listview)
	ListView subject_listview;

	public static final String Value = "Value"; // value
	public static final String DataListKey = "DataListKey"; // List值key
	public static final String KeyValue = "KeyValue";
	public static final String TextContent = "TextContent";
	Handler handler = new Handler();
	TextView headEditTxt;
	private ImageView itemSelectedImg=null;

	protected List createDataList() {
	return new ArrayList(); // 数据集
	}

	private String selectStr="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.additional_subject);
		String content = getIntent().getExtras().getString(TextContent);
		selectStr=getIntent().getExtras().getString("selectStr");
		init();
	}

	
	OnItemClickListener listItemOnClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
		    ImageView selectedImg=(ImageView)view.findViewById(R.id.itemSelectedImg);
		    selectedImg.setVisibility(View.VISIBLE);
			actionPerformed_listItemOnClick(parent, view, position,id);
		}
	};
	protected void actionPerformed_listItemOnClick(AdapterView<?> parent,
			View view, int position, long id) {
		// TODO Auto-generated method stub
		int temp=0;
		final Map item = (Map) subject_listview.getItemAtPosition(position);
		ActivityUtils.returnActivity(handler, this, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(Value, item.get(KeyValue).toString());
			}
		});
	}



	private void init() {
		// TODO Auto-generated method stub
		createDataList();
		loadList();
	}



	/**
	 * 刷新页面
	 */
	private void refreshList() {
		dataList.clear();
		loadList();
	}



	protected List<?> getDataList() {
		// TODO Auto-generated method stub
		return dataList;
	}

	/**
	 * 查询
	 */
	private void loadList() {
		String[] values = getIntent().getExtras().getStringArray(DataListKey);
		for (String value : values) {
			Map map = new HashMap();
			map.put(KeyValue, value);
			dataList.add(map);
		}
		// 实现列表的显示
		subject_listview.setAdapter(createListViewAdapter());
		subject_listview.setOnItemClickListener(listItemOnClickListener);
	}

	/**
	 * ListView适配器
	 * 
	 * @return
	 */
	protected BaseAdapter createListViewAdapter() {
		// TODO Auto-generated method stub
		BeanAdapter adapter = new BeanAdapter(this, dataList,
				R.layout.activity_subject_list_item, new String[] {
				KeyValue }, new int[] {
						R.id.activity_subject_list_item_subject});
		setAdapter(adapter);
		return adapter;
	}

	public BaseAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}



	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		
	}

}
