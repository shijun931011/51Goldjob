package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import android.view.View;

import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.mgr.IBiz;
import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.IntentUtils;
import com.chyjr.goldjob.fr.utils.ViewerUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.DictConstants;
import com.example.user.a51goldjob.bean.Job;
import com.example.user.a51goldjob.entities.User;
import com.example.user.a51goldjob.listeners.UserLogoutListener;
import com.example.user.a51goldjob.mgr.DictMgr;
import com.example.user.a51goldjob.mgr.UserActivityMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.view.PublicViews;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 企业职位
 * @author yeq
 *
 */
@ContentView(R.layout.activity_corp_job)
public class CorpJobActivity extends BaseActivity implements View.OnClickListener{
    private TextView mPageTitleTextView;
    private TextView allAreasTv=null;
    private TextView allindustryTv=null;
    private TextView removeCondition=null;
	public static final String[] PARAMS_IO = {
		DictConstants.DICT_COMP_CITY , // 所在地区
		DictConstants.DICT_COMP_INDUSTRY, // 所在行业
	};
	
	
	int[] filterDictLangs = {
		R.string.comp_city,
		R.string.comp_industry,
	};
	
	int[] defaultItemValues = {
		R.string.national_city,
		R.string.all_industry
	};
	
	String[] from = { "itemLabel", "itemValue" };
	int[] to = { R.id.corp_job_list_item_label_txt, R.id.corp_job_list_item_value_txt };
	
	@ViewInject(R.id.corp_job_keywords_et)
	EditText corp_job_keywords_et;
	
	@ViewInject(R.id.corp_job_filter_listview)
	ListView corp_job_filter_listview;

	DictMgr dictMgr = Mgr.get(DictMgr.class);

    UserActivityMgr userActivityMgr = Mgr.get(UserActivityMgr.class);
	
	Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    init();
	}

	protected void init() {
        this.findViewById(R.id.removeConditionView).setOnClickListener(this);
		PublicViews.createMenuTextView(this, getString(R.string.logout), getResources().getColor(R.color.text_white), new UserLogoutListener(this)); // 创建菜单按钮
		getIntent().putExtra(JobListActivity.Constants.PARAM_JOB_TYPE, Job.Constants.JOB_TYPE_0); // 默认搜索为企业职位
		dictMgr.execute(this, new IBiz() {
			public void execute() {
				initListView();
			}
		});
		corp_job_filter_listview.setFocusable(true);
		corp_job_filter_listview.setFocusableInTouchMode(true);
		corp_job_filter_listview.requestFocus();
		 mPageTitleTextView=(TextView)this.findViewById(R.id.title_middle_txt);
		 mPageTitleTextView.setTextSize(21);
		 mPageTitleTextView.setText("企业职位");
	}

	protected void onResume() {
		super.onResume();

		User user = userActivityMgr.getLoginUser(this);
		if (user == null) {
//			CustomDialog customDialog = DialogUtils.info(this, getResources().getString(R.string.nologin_error));
//			customDialog.setBtnsOnClickListener(new DialogOnClickFinishListenerAdapter(customDialog, this));
		}
	}

	protected BeanAdapter getAdapter() {
		return (BeanAdapter) corp_job_filter_listview.getAdapter();
	}

	protected void initListView() {
		BeanAdapter adapter = new BeanAdapter(this, getFields(), R.layout.activity_corp_job_list_item, from, to);
		corp_job_filter_listview.setAdapter(adapter);
	}

	protected List<?> getFields() {
		List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
		Map<String, String> field = null;
		int filterDictLang;
		for (int i =0 ; i < filterDictLangs.length; i++) {
			filterDictLang = filterDictLangs[i];
			field = new HashMap<String, String>();
			field.put("itemLabel", getString(filterDictLang));
			field.put("itemValue", getParamValue(i));
			fields.add(field);
		}
		return fields;
	}

	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 带回数据初始化界面
			if (resultCode == RESULT_OK) {
			String dictKey = data.getStringExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY);
			int index = ArrayUtils.indexOf(PARAMS_IO, dictKey);
			Map<String, String> map = (Map<String, String>) corp_job_filter_listview.getItemAtPosition(index);
			map.put("itemValue", data.getStringExtra(DictSelectorActivity.Constants.PARAM_OUT_SELECT_ITEM));
		}
		//带入当前选择的城市
		String city = AppContext.get(this).getCity(getString(R.string.national_city));
		int index = ArrayUtils.indexOf(PARAMS_IO, DictConstants.DICT_COMP_CITY);
		Map<String, String> map = (Map<String, String>) corp_job_filter_listview.getItemAtPosition(index);
		if(map!=null){
			map.put("itemValue", city);
			getAdapter().notifyDataSetChanged();
		}
		
	}
	private TextView tempView=null;

	//城市选择页面跳转
	@OnItemClick(R.id.corp_job_filter_listview)
	public void actionPerformed_jobListFilterItemOnClick(AdapterView<?> parent, View view, int pos, long id) {
	
		int index = Long.valueOf(id).intValue();
		if(0 != index){
			final String dictKey = PARAMS_IO[index];
			final String topTitle = getString(filterDictLangs[index]);
			TextView txtView=(TextView)view.findViewById(R.id.corp_job_list_item_value_txt);
		    final String selectedStr=txtView.getText().toString();
			ActivityUtils.startActivityForResult(handler, this, DictSelectorActivity.class, new IIntentHandler() {
				public void putExtra(Intent intent) {
					intent.putExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY, dictKey); 
					intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_TOP_TITLE, topTitle);
					intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_HEADER_TITLE, topTitle);
					intent.putExtra("selectStr",selectedStr);
					
				};
			});
		}else{
			tempView=(TextView)view.findViewById(R.id.corp_job_list_item_value_txt);
		    final String selectedStr=tempView.getText().toString();
			ActivityUtils.startActivityForResult(handler, this, CityChooserActivity.class,new IIntentHandler() {
				@Override
				public void putExtra(Intent intent) {
					// TODO Auto-generated method stub
					intent.putExtra("selectStr",selectedStr);
				}
			});
		}
	}

	//搜索按钮事件处理
	@OnClick(R.id.corp_job_search_btn)
	public void actionPerformed_corpJobSearchBtnOnClick(View v) {
		// 查询
		ActivityUtils.startActivity(handler, this, JobListActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				fillIntent(intent);
				
				String cityName = intent.getStringExtra(DictConstants.DICT_COMP_CITY);
				if (getString(R.string.national_city).equalsIgnoreCase(cityName)) {
					cityName = "";
				}
				
				String industryName = intent.getStringExtra(DictConstants.DICT_COMP_INDUSTRY);
				if (getString(R.string.all_industry).equalsIgnoreCase(industryName)) {
					industryName = "";
				}
				
				String keywrods = ViewerUtils.getTextViewString(corp_job_keywords_et);
				intent.putExtra(JobListActivity.Constants.PARAM_JOB_TYPE, Job.Constants.JOB_TYPE_0); 
				intent.putExtra(JobListActivity.ParamsIn.KEYWORDS, keywrods); 
				intent.putExtra(JobListActivity.ParamsIn.KEYWORD_TYPE, ""); 
				intent.putExtra(JobListActivity.ParamsIn.AREA_ID, cityName); 
				intent.putExtra(JobListActivity.ParamsIn.INDUSTRY_ID, industryName); // 行业名称
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void fillIntent(Intent intent) {
		for (int i=0; i<PARAMS_IO.length; i++) {
			Map<String, String> map = (Map<String, String>) corp_job_filter_listview.getItemAtPosition(i);
			intent.putExtra(PARAMS_IO[i], (String)map.get("itemValue"));
		}
	}

	private String getParamValue(int i) {
		if (PARAMS_IO[i].equalsIgnoreCase(DictConstants.DICT_COMP_CITY)) {
			String city = AppContext.get(this).getCity(getString(R.string.national_city));
			return city;
		}
		
		String value =  IntentUtils.getStringExtra(this, PARAMS_IO[i]);
		return StringUtils.isEmpty(value) ? getString(defaultItemValues[i]) : value;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.removeConditionView){
//			String city = AppContext.get(this).getCity(getString(R.string.national_city));
//			int index = ArrayUtils.indexOf(PARAMS_IO, DictConstants.DICT_COMP_CITY);
			
			init();
			Map<String, String> map = (Map<String, String>) corp_job_filter_listview.getItemAtPosition(0);
			if(map!=null){
				map.put("itemValue", "全部");
				getAdapter().notifyDataSetChanged();
			}
			Toast.makeText(CorpJobActivity.this,"完成!",Toast.LENGTH_SHORT).show();
		}
		
	}
	
	

	
}
