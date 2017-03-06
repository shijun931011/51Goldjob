package com.example.user.a51goldjob.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.Fields;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.IntentUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.bean.DictConstants;
import com.example.user.a51goldjob.bean.HunterJob;
import com.example.user.a51goldjob.bean.Job;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.PublicViews;
import com.example.user.a51goldjob.view.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 职位列表
 * 
 * @author yeq
 *
 */
@ContentView(R.layout.activity_job_list)
public class JobListActivity extends BaseListActivity implements View.OnClickListener{

	// 业务常量
	public static interface Constants {
		/** 搜职位名 */
		public static final String KEYWORD_TYPE_1 = "1"; // 搜职位名
		/** 搜公司名 */
		public static final String KEYWORD_TYPE_2 = "2"; // 搜公司名
		
		public static final String PARAM_JOB_TYPE = "jobtype"; // 职位列表参数
	}
	
	// 输入参数：
	public static class ParamsIn {
		/** 关键字 */
		public static final String KEYWORDS = "keyword"; // 关键字
		/** 关键字类型 ( 1=搜职位名 2=搜公司名 ) */
		public static final String KEYWORD_TYPE = "keywordtype"; // 关键字类型 ( 1=搜职位名 2=搜公司名 )
		/** 职位编号 */
		public static final String JOB_ID = "jobid"; // 职位编号
		/** 公司性质 */
		public static final String CORP_TYPE = "corptype"; // 公司性质
		/** 地区编号 */
		public static final String AREA_ID = "areaid"; // 地区编号
		/** 行业编号 */
		public static final String INDUSTRY_ID = "industryid"; // 行业编号
		/** 年薪范围 */
		public static final String SALARY_ID = "salaryid"; // 年薪范围
		/** 发布时间 */
		public static final String PUBLIC_DATE = "publicdate"; // 发布时间
	}
	
	Map<String, Object> params = new HashMap<String, Object>();
	
	@ViewInject(R.id.job_list_listview)
	PullToRefreshListView job_list_listview;
	
	Handler handler = new Handler();
	
	//配置筛选按钮监听
	View.OnClickListener onClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			actionPerformed_onClick(v);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);	   
	    TextView createResumetxt=(TextView)this.findViewById(R.id.textViewRight);
	    createResumetxt.setOnClickListener(this);
	    createResumetxt.setText("筛选");
	    init();
	}

	/**
	 * 初始化页面数据
	 */
	protected void init() {
		String topTitle = IntentUtils.getStringExtra(this, "topTitle");
		if (StringUtils.isEmpty(topTitle)) {
			topTitle = getString(R.string.search_result);
		}
		setBarTitle(topTitle);
		
		PublicViews.createMenuTextView(this, getString(R.string.list_filter)
				, getResources().getColor(R.color.text_white), onClickListener); // 筛选按钮
	}

	/**
	 * 处理报文数据
	 * */
	protected List<?> parseDataList(ResponseData responseData) throws Exception {
		JSONObject joReturnData = responseData.getBodyPropertyJsonObject("returnData");
		String numStr = JsonUtils.getString(joReturnData, "num");
		String listStr = JsonUtils.getString(joReturnData, "list");

		Integer num = Integer.valueOf(numStr);
		if (num <= 0) {
			ActivityUtils.showMessage(this, true, getString(R.string.not_found_any_job));
		}
		
		Gson gson = Gsons.create();
		List<HunterJob> datas = gson.fromJson(listStr, new TypeToken<List<HunterJob>>() {}.getType());
		return datas;
	}

	/**
	 * 数据载入适配器
	 * */
	protected BaseAdapter createListViewAdapter() {
		List<?> dataList = (List<?>) getDataList();
		BeanAdapter adapter = new BeanAdapter(this, dataList,
				R.layout.activity_job_list_item, new String[] {
						"jobname", "corpname", "jobcity", "prettySalary", "createdatedt" },
				new int[] { R.id.job_list_item_jobname,
						R.id.job_list_item_corpname,
						R.id.job_list_item_jobcity,
						R.id.job_list_item_salary,
						R.id.job_list_item_createdatedt}){
			@SuppressLint("ResourceAsColor") 
			protected void bindView(int position, View view) {
				super.bindView(position, view);
				
				TextView jobView = (TextView) view.findViewById(R.id.job_list_item_jobname);
				String keywords = getIntent().getExtras().getString(ParamsIn.KEYWORDS);
					if(jobView!=null&&!"".equals(jobView.getText().toString())){
						if(jobView.getText().toString().equals(keywords)){
							jobView.setTextColor(R.color.text_red);							
						}
					}
				}
			};
		
		return adapter;
	}

	/**
	 * 查看职位详情
	 * */
	protected void actionPerformed_onItemClick(AdapterView<?> parent, View view, int position, long id) {
		final String jobtype = IntentUtils.getStringExtra(this, Constants.PARAM_JOB_TYPE);
		if(id>=0){
			final HunterJob job = (HunterJob) getAdapter().getItem(Long.valueOf(id).intValue());
			// 打开职位详情界面
			ActivityUtils.startActivity(handler, this, JobApplyDetailsActivity.class, new IIntentHandler() {
				public void putExtra(Intent intent) {
					IntentUtils.putExtras(intent, job, Fields.getAll(JobApplyDetailsActivity.ParamsIn.class, String[].class));
					intent.putExtra(JobApplyDetailsActivity.ParamsIn.JOB_TYPE, jobtype);
				}
			});
		}
	}

	protected PullToRefreshListView getListView() {
		return job_list_listview;
	}

	protected String getUrlKey() {
		String jobtype = IntentUtils.getStringExtra(this, Constants.PARAM_JOB_TYPE);
		if (jobtype.equalsIgnoreCase(Job.Constants.JOB_TYPE_1)) {
			return Transcodes.TC_HUNTER_JOB_LIST;
		}
		return Transcodes.TC_CORP_JOB_LIST;
	}

	/**
	 * 筛选页面跳转
	 * */
	protected void actionPerformed_onClick(View v) {
		ActivityUtils.startActivityForResult(handler, this, JobListFilterActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				IntentUtils.putExtra(intent, DictConstants.DICT_COMP_CITY, getIntent(), ParamsIn.AREA_ID);
				IntentUtils.putExtra(intent, DictConstants.DICT_COMP_INDUSTRY, getIntent(), ParamsIn.INDUSTRY_ID);
				IntentUtils.putExtra(intent, DictConstants.DICT_RESUME_ESC, getIntent(), ParamsIn.SALARY_ID);
				IntentUtils.putExtra(intent, DictConstants.DICT_JOB_PUB_DATE, getIntent(), ParamsIn.PUBLIC_DATE);
			}
		}); // 跳转到筛选界面
	}

	/**
	 * 筛选结果返回
	 * */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		IntentUtils.putExtra(getIntent(), ParamsIn.AREA_ID, data, DictConstants.DICT_COMP_CITY);
		IntentUtils.putExtra(getIntent(), ParamsIn.INDUSTRY_ID, data, DictConstants.DICT_COMP_INDUSTRY);
		IntentUtils.putExtra(getIntent(), ParamsIn.SALARY_ID, data, DictConstants.DICT_RESUME_ESC);
		IntentUtils.putExtra(getIntent(), ParamsIn.PUBLIC_DATE, data, DictConstants.DICT_JOB_PUB_DATE);
		refreshList(); // 搜索回传
	}

	/**
	 * 重写加载页面数据方法
	 * */
	protected String getJsonParams(int page) {		
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "pageNo", String.valueOf(page)); // cc@test.com
		JsonUtils.putExtrasParams(this, jsonObject, Fields.getAll(ParamsIn.class, String[].class), new JsonUtils.ExtrasParam(){
			public String getValue(Activity activity, String param) {
				String value = IntentUtils.getStringExtra(activity, param);
				if (ParamsIn.AREA_ID.equalsIgnoreCase(param) && (getString(R.string.national_city).equalsIgnoreCase(value))) { 
					value = ""; // 如果areaid为全国，那么参数设置为空字符串
				}
				return value;
			}
		});
		JsonUtils.put(jsonObject, ParamsIn.KEYWORD_TYPE, Constants.KEYWORD_TYPE_1);
		
		String data = RequestData.getData(jsonObject, getUrlKey(), false);
		return data;
	}

	@Override
	public void onClick(View v) {
		actionPerformed_onClick(v);
		
	}
}
