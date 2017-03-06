package com.example.user.a51goldjob.activity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.mgr.IBiz;
import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.ListAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Dict;
import com.example.user.a51goldjob.bean.DictConstants;
import com.example.user.a51goldjob.bean.Job;
import com.example.user.a51goldjob.mgr.DictMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 猎头职位
 * 
 * @author yeq
 * 
 */
@ContentView(R.layout.activity_hunter_job)
public class HunterJobActivity extends BaseActivity {
    
	@ViewInject(R.id.hunter_job_industry_listview)
	ListView hunter_job_industry_listview;

	DictMgr dictMgr = Mgr.get(DictMgr.class);

	String[] from = { "ioc", "label" };
	int[] to = { R.id.hunter_job_list_item_left_imgview, R.id.hunter_job_list_item_center_txt };
	
	Handler handler = new Handler();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.btn_hunterjob);	
		init();	
	}

	/**
	 * 初始化页面数据
	 * */
	protected void init() {		
		String cityName = AppContext.get(this).getCity(getString(R.string.national_city));
		if (getString(R.string.national_city).equalsIgnoreCase(cityName)) {
			cityName = "";
		}
		getIntent().putExtra(JobListActivity.ParamsIn.AREA_ID, cityName);
		getIntent().putExtra(JobListActivity.Constants.PARAM_JOB_TYPE, Job.Constants.JOB_TYPE_1); // 默认搜索为猎头职位
		dictMgr.execute(this, new IBiz() {
			public void execute() {
				List<Dict> dicts = dictMgr.getCurrentDicts(getThis(),
						DictConstants.DICT_COMP_INDUSTRY);
				if (CollectionUtils.isEmpty(dicts)) {
					// TODO 数据字典不存在，弹出错误信息
					finish();
					return;
				}
				initListView(dicts);
			}
		});
	}

	/**
	 * 加载内容到适配器
	 * */
	//hunter_job_list_item_left_imgview
	protected void initListView(List<Dict> dicts) {
		BeanAdapter adapter = ListAdapters.WebListAdapter.create(this, dicts, R.layout.activity_hunter_job_list_item, from, to);
		hunter_job_industry_listview.setAdapter(adapter);	
	}

	// 单击一项进入列表查询界面，同时带入参数：行业名称
	@OnItemClick(R.id.hunter_job_industry_listview)
	public void actionPerformed_industryItemClick(AdapterView<?> parent, View view, int pos, long id) {
		Dict dict = (Dict) hunter_job_industry_listview.getAdapter().getItem(pos);
		if (dict == null) {
			return;
		}
		final String industryName = dict.getValue();
		if (StringUtils.isEmpty(industryName)) {
			return;
		}
		ActivityUtils.startActivity(handler, this, JobListActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(JobListActivity.ParamsIn.AREA_ID, getIntent().getStringExtra(JobListActivity.ParamsIn.AREA_ID));
				intent.putExtra(JobListActivity.ParamsIn.KEYWORD_TYPE, ""); // 类型：搜索职位
				intent.putExtra(JobListActivity.ParamsIn.INDUSTRY_ID, industryName); // 行业名称
				intent.putExtra(JobListActivity.Constants.PARAM_JOB_TYPE, Job.Constants.JOB_TYPE_1); 
				intent.putExtra("topTitle", String.format("%s%s", industryName,  getString(R.string.industry))); // 标题 
			}
		});
	}
}
