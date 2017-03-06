package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.mgr.IBiz;
import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.IntentUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.DictConstants;
import com.example.user.a51goldjob.mgr.DictMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

@ContentView(R.layout.activity_job_list_filter)
public class JobListFilterActivity extends BaseActivity implements View.OnClickListener{
	private TextView cleanTxtView=null;
	private int position=1000000000;
	public static final String[] PARAMS_IO = {
		DictConstants.DICT_COMP_CITY , // 所在地区
		DictConstants.DICT_COMP_INDUSTRY, // 所在行业
		DictConstants.DICT_RESUME_ESC, // 年薪范围
		DictConstants.DICT_JOB_PUB_DATE // 发布时间
	};
	
	int[] filterDictLangs = {
		R.string.comp_city,
		R.string.comp_industry,
		R.string.job_salary,
		R.string.job_pub_date
	};
	
	String[] from = { "itemLabel", "itemValue" };
	int[] to = { R.id.job_list_filter_list_item_label_txt, R.id.job_list_filter_list_item_value_txt };
	
	@ViewInject(R.id.job_list_filter_listview)
	ListView job_list_filter_listview;

	DictMgr dictMgr = Mgr.get(DictMgr.class);
	
	Handler handler = new Handler();
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.list_filter);
	    this.findViewById(R.id.clean_up).setOnClickListener(this);
	    init();
	    Map<String, String> map = (Map<String, String>) job_list_filter_listview.getItemAtPosition(1);
	    if(map!=null){
	    	String str=map.get("itemValue");
	    	if(str.equals("")){
	    		map.put("itemValue", "不限");
				getAdapter().notifyDataSetChanged();
	    	}
		
		}	
		
	    
	    
	    Map<String, String> mapss = (Map<String, String>) job_list_filter_listview.getItemAtPosition(2);
		if(mapss!=null){
			mapss.put("itemValue", "不限");
			getAdapter().notifyDataSetChanged();
		}	
		
		 Map<String, String> mapsss = (Map<String, String>) job_list_filter_listview.getItemAtPosition(3);
			if(mapsss!=null){
				mapsss.put("itemValue", "不限");
				getAdapter().notifyDataSetChanged();
			}	
	}

	protected void init() {
		dictMgr.execute(this, new IBiz() {
			public void execute() {
				initListView();
			}
		});
	}
	
	protected BeanAdapter getAdapter() {
		return (BeanAdapter) job_list_filter_listview.getAdapter();
	}

	protected void initListView() {
		BeanAdapter adapter = new BeanAdapter(this, getFields(), R.layout.activity_job_list_filter_list_item, from, to);
		job_list_filter_listview.setAdapter(adapter);
	}

	protected List<?> getFields() {
		List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
		Map<String, String> field = null;
		int filterDictLang;
		for (int i =0 ; i < filterDictLangs.length; i++) {
			filterDictLang = filterDictLangs[i];
			field = new HashMap<String, String>();
			field.put("itemLabel", getString(filterDictLang));
			field.put("itemValue", getParamValue(PARAMS_IO[i]));
			fields.add(field);
		}
		return fields;
	}

	private String getParamValue(String param) {
		if (param.equalsIgnoreCase(DictConstants.DICT_COMP_CITY)) {
			String city = AppContext.get(this).getCity(getString(R.string.national_city));
			return city;
		}
		return IntentUtils.getStringExtra(this, param);
	}
	
	

	private  int tempPosition=1000000000;
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);	
		// 带回数据初始化界面
		if (resultCode == RESULT_OK) {
			String dictKey = data.getStringExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY);
			tempPosition=data.getExtras().getInt("position");
			int index = ArrayUtils.indexOf(PARAMS_IO, dictKey);
			Map<String, String> map = (Map<String, String>) job_list_filter_listview.getItemAtPosition(index);
			String selectedValue=data.getStringExtra(DictSelectorActivity.Constants.PARAM_OUT_SELECT_ITEM);
			map.put("itemValue", selectedValue);
			getAdapter().notifyDataSetChanged();
		}
	}
	
		
		
		
	
    private TextView diquTextView=null;
    private TextView industry=null;
	@OnItemClick(R.id.job_list_filter_listview)
	public void actionPerformed_jobListFilterItemOnClick(AdapterView<?> parent, View view, int pos, long id) {
		int index = Long.valueOf(id).intValue();
		final String dictKey = PARAMS_IO[index];
		final String topTitle = getString(filterDictLangs[index]);
		TextView selextTxtView=(TextView)view.findViewById(R.id.job_list_filter_list_item_value_txt);
		final String selectStr=selextTxtView.getText().toString();
		ActivityUtils.startActivityForResult(handler, this, DictSelectorActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY, dictKey); 
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_TOP_TITLE, topTitle);
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_HEADER_TITLE, topTitle);
				intent.putExtra("selectStr",selectStr);
				intent.putExtra("toDict", tempPosition);
			};
		});
	}

	@SuppressWarnings("unchecked")
	@OnClick(R.id.job_list_filter_search_btn)
	public void actionPerformed_jobListFilterSearchBtnOnClick(View v) {
		ActivityUtils.returnActivity(handler, this, new IIntentHandler() {
			public void putExtra(Intent intent) {
				for (int i=0; i<PARAMS_IO.length; i++) {
					Map<String, String> map = (Map<String, String>) job_list_filter_listview.getItemAtPosition(i);
					intent.putExtra(PARAMS_IO[i], (String)map.get("itemValue"));
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clean_up: //清除筛选按钮
			init();
			 Map<String, String> map = (Map<String, String>) job_list_filter_listview.getItemAtPosition(0);
				if(map!=null){
					map.put("itemValue", "全部");
					getAdapter().notifyDataSetChanged();
				}
				 Map<String, String> maps = (Map<String, String>) job_list_filter_listview.getItemAtPosition(1);
					if(maps!=null){
						maps.put("itemValue", "不限");
						getAdapter().notifyDataSetChanged();
					}	
					
					 Map<String, String> mapss = (Map<String, String>) job_list_filter_listview.getItemAtPosition(2);
						if(mapss!=null){
							mapss.put("itemValue", "不限");
							getAdapter().notifyDataSetChanged();
						}	
						
						 Map<String, String> mapsss = (Map<String, String>) job_list_filter_listview.getItemAtPosition(3);
							if(mapsss!=null){
								mapsss.put("itemValue", "不限");
								getAdapter().notifyDataSetChanged();
							}	
							
							
			 Toast.makeText(this,"完成!" ,Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
		
	}
}
