package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Job;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.PullToRefreshListView;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 职位申请记录
 * 
 * @author yeq
 * 
 */
@ContentView(R.layout.activity_job_apply)
public class JobApplyAcativity extends BaseListActivity {

	@ViewInject(R.id.job_apply_text_block)
	View job_apply_text_block;
	
	@ViewInject(R.id.job_apply_refresh_head)
	LinearLayout job_apply_refresh_head;

	@ViewInject(R.id.job_apply_listview)
	PullToRefreshListView job_apply_listview;

	@ViewInject(R.id.top_text_block_txt)
	TextView top_text_block_txt;

	@ViewInject(R.id.job_apply_progress_bar)
	View job_apply_progress_bar;

	Handler handler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.user_center_apply_info);

		top_text_block_txt
				.setText(getString(R.string.user_center_apply_info_header_text));
	}

	/**
	 * 对具体职位申请项的点击监听
	 * 重写父类的Item列表点击监听方法，通过父类的onCreate()方法调用
	 * */
	protected void actionPerformed_onItemClick(AdapterView<?> parent,
			View view, int position, long id) {
		if (isInvalidItemClick(view, position)) {
			return;
		}
		final Job item = (Job) parent.getItemAtPosition(position);
		Class<?> to = JobApplyDetailsActivity.class;
		ActivityUtils.startActivity(handler, this, to, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra("jobtype", String.valueOf(item.getJobtype()));
				intent.putExtra("corpid", String.valueOf(item.getCompanyid()));
				intent.putExtra("jobid", String.valueOf(item.getJobid()));
				intent.putExtra("jobname", String.valueOf(item.getJobname()));
			}
		});
	}

	/**
	 * 重写加载页面数据方法
	 * */
	protected String getJsonParams(int page) {
		Token token = AppContext.get(this).getToken();

		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "page", String.valueOf(page)); // cc@test.com
		JsonUtils.put(jsonObject, "userid", token.getUserId()); // 111
		JsonUtils.put(jsonObject, "token", token.getToken());

		String data = RequestData.getData(jsonObject, getUrlKey());
		return data;
	}

	/**
	 * 加载报文数据
	 * */
	protected List<?> parseDataList(ResponseData responseData) throws Exception {
		String data = responseData.getBodyPropertyValue("data");
		String datanumStr = responseData.getBodyPropertyValue("datanum");
		Integer datanum = Integer.valueOf(datanumStr);
		if (datanum <= 0) {
			ActivityUtils.showMessage(this, true,
					getString(R.string.user_center_apply_info_nocontent));
		}
		//去除h
		data = remove_h(data);
		Gson gson = Gsons.create();
		List<Job> datas = gson.fromJson(data, new TypeToken<List<Job>>() {}.getType());
		List<Job> copyDatas = new ArrayList<Job>();
		for (Iterator iterator = datas.iterator(); iterator.hasNext();) {
			Job job = (Job) iterator.next();
			if("2".equals(job.getStatus())){
				copyDatas.add(job);
			}
		}
		return copyDatas;
	}

	/**
	 * 属于特殊化处理，去除Key的h字母
	 * */
	public static String remove_h(String data) {
		// TODO Auto-generated method stub
		String[] strs = {"hjobname","hjobcity"};
		for (String str_h : strs) {
			String str_h_no = str_h.substring(1);			
			data = data.replace(str_h, str_h_no);
		}
		data = data.replace("husername", "corpname");
		return data;
	}

	/**
	 * 适配器载入职位信息
	 * */
	
	protected BaseAdapter createListViewAdapter() {
		List<?> dataList = (List<?>) getDataList();
		BeanAdapter adapter = new BeanAdapter(this, dataList,
				R.layout.activity_job_apply_list_item, new String[] {
						"jobname", "issuedatedt", "corpname", "jobcity" },
				new int[] { R.id.activity_job_apply_list_item_jobname,
						R.id.activity_job_apply_list_item_issuedate,
						R.id.activity_job_apply_list_item_corpname,
						R.id.activity_job_apply_list_item_jobcity });
		return adapter;
	}

	/**
	 * 数据完全加载完毕后处理
	 * 重写父类的afterLoaded()方法。
	 */
	protected void afterLoaded() {
		job_apply_progress_bar.setVisibility(View.GONE);
		job_apply_text_block.setVisibility(View.VISIBLE);
	}

	protected PullToRefreshListView getListView() {
		return job_apply_listview;
	}

	protected String getUrlKey() {
		return Transcodes.TC_JOB_APPLY_LIST;
	}
	
	protected LinearLayout getHeadView() {
		return job_apply_refresh_head;
	}
}
