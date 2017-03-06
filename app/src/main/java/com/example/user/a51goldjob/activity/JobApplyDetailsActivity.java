package com.example.user.a51goldjob.activity;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;


import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.DateUtils;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.IntentUtils;
import com.chyjr.goldjob.fr.utils.ViewerUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Job;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.PublicViews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 职位申请记录明细
 * 
 * @author yeq
 * 
 */
@ContentView(R.layout.activity_job_apply_details)
public class JobApplyDetailsActivity extends BaseActivity {

	
	// 输入参数
	public static class ParamsIn {
		public static final String CORP_ID = "corpid";
		public static final String JOB_ID = "jobid";
		public static final String JOB_NAME = "jobname";
		public static final String JOB_TYPE = "jobtype";
		public static final String HH_ID = "hhid";
	}

	//页面布局
	String status = "";
	String[] keys = { "jobdescription", "number", "jobcity", "jobname",
			"salary" };
	int[] viewIds = { R.id.job_apply_details_jobdescription_txt,
			R.id.job_apply_details_number_txt,
			R.id.job_apply_details_jobcity_txt,
			R.id.job_apply_details_jobname_txt,
			R.id.job_apply_details_salary_txt, };
	String[] keys2 = { "scope", "nature", "corpname", };
	int[] viewIds2 = { R.id.job_apply_details_scope_txt,
			R.id.job_apply_details_nature_txt,
			R.id.job_apply_details_corpname_txt, };

	@ViewInject(R.id.job_apply_details_btn)
	Button job_apply_details_btn;

	Handler handler = new Handler();
    public boolean isSaved=false;
	//Button按钮监听
	View.OnClickListener onClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			actionPerformed_onClick(v);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.job_details);
		//title_right
//		RelativeLayout layout=(RelativeLayout)this.findViewById(R.id.title_right);
//		layout.setBackgroundDrawable(JobApplyDetailsActivity.this.getResources().getDrawable(R.drawable.icon_favorite_out));
		init();
	}

	/**
	 * 页面初始化数据
	 * */
	protected void init() {
		String corpid = IntentUtils.getStringExtra(this, ParamsIn.CORP_ID);
		String jobid = IntentUtils.getStringExtra(this, ParamsIn.JOB_ID);
		String jobtype = IntentUtils.getStringExtra(this, ParamsIn.JOB_TYPE);

		if (StringUtils.isEmpty(corpid) || StringUtils.isEmpty(jobid)
				|| StringUtils.isEmpty(jobtype)) {
			ActivityUtils.showMessage(this, true,
					getString(R.string.network_error));
			return;
		}

		load(corpid, jobid, jobtype);
		PublicViews.createMenuFavoriteImageView(this, onClickListener); // 初始化收藏按钮
		job_apply_details_btn.setOnClickListener(onClickListener);
	}

	//页面跳转
	IIntentHandler postInitHandler = new IIntentHandler() {
		public void putExtra(Intent intent) {
			IntentUtils.putExtras(intent, getIntent(), ChooseResumeActivity.PARAMS_IN);
		}
	};

	//信息请求结果反馈。如,收藏操作结果反馈。
	RequestCallBackAdapters.InfoRequestCallBackAdapter<String> infoRequestCallBackAdapter = new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>( this, true) {
		protected void _onSuccess(ResponseInfo<String> response) throws Exception {
			// {"returnCode":"1000001","returnMsg":"职位已经收藏过了"}
			ResponseData responseData = ResponseData.create(response.result);
			String returnMsg = responseData.getBodyPropertyValue("returnMsg");
			ImageView icon_favorite_selector = null;
			if(returnMsg.equals("操作成功")){
				Toast.makeText(JobApplyDetailsActivity.this,"收藏成功!",Toast.LENGTH_SHORT).show();
			}else if(returnMsg.equals("职位已经收藏过了")){
				ActivityUtils.showMessage(JobApplyDetailsActivity.this, true, "该职位已经收藏过了");
			}
			
			icon_favorite_selector=(ImageView)JobApplyDetailsActivity.this.getTopBar().getCustomView();
			if(returnMsg.equals("职位已经收藏过了")){
				icon_favorite_selector=(ImageView)JobApplyDetailsActivity.this.getTopBar().getCustomView();
				icon_favorite_selector.setImageDrawable(JobApplyDetailsActivity.this.getResources().getDrawable(R.drawable.icon_favorite_in));
				icon_favorite_selector.refreshDrawableState();
			}else{
				icon_favorite_selector.setImageDrawable(JobApplyDetailsActivity.this.getResources().getDrawable(R.drawable.icon_favorite_in));
				icon_favorite_selector.refreshDrawableState();
			}
			//ActivityUtils.showMessage(getThis(), false, returnMsg);
		}
	};

	/**
	 * 按钮点击业务
	 * */
	protected void actionPerformed_onClick(View v) {

		if (noLogin()) {
			return;
		}
		if(!getString(R.string.no_overdue).equals(status)){
			//DialogUtils.commonInfo(this, this.getResources().getString(R.string.job_overdue));
			Toast.makeText(this,R.string.job_overdue,Toast.LENGTH_SHORT).show();
			return;
		}
		if (v.equals(job_apply_details_btn)) { // 按钮点击，转到选择简历界面
			ActivityUtils.startActivity(handler, this,
					ChooseResumeActivity.class, postInitHandler);
			return;
		}

		// 收藏功能：
		String tag = (String) v.getTag();
		if (tag == null) {
			return;
		}
		if (tag.equalsIgnoreCase("icon_favorite_selector")) { // 收藏

			String corpid = IntentUtils.getStringExtra(this,
					ParamsIn.CORP_ID);
			String jobid = IntentUtils.getStringExtra(this, ParamsIn.JOB_ID);
			String jobtype = IntentUtils.getStringExtra(this,
					ParamsIn.JOB_TYPE);

			Token token = AppContext.get(this).getToken();

			JSONObject jsonObject = new JSONObject();
			JsonUtils.put(jsonObject, "token", token.getToken());
			JsonUtils.put(jsonObject, "jobid", jobid); // 职位编号
			JsonUtils.put(jsonObject, "userid", token.getUserId());
			JsonUtils.put(jsonObject, "corpid", corpid); // 公司编号

			String transCode = Transcodes.TC_JOB_FAVORITE_KEEP;
			if (Job.Constants.JOB_TYPE_1.equalsIgnoreCase(jobtype)) {
				transCode = Transcodes.TC_HUNTER_JOB_FAVORITE_KEEP;
			}

			String data = RequestData.getData(jsonObject, transCode, false);
			Https.post(this, transCode, data, infoRequestCallBackAdapter);
			
		}
	}
	
	//判断是否登录
	private boolean noLogin() {
		boolean nologin = !AppContext.get(this).isLogin();
		if (nologin) {
			//DialogUtils.commonInfo(this, this.getResources().getString(R.string.nologin_error));
			Intent intent=new Intent(this,LoginActivity.class);
			intent.putExtra("viewid", R.id.job_apply_details_btn);
			startActivity(intent);
		}
		return nologin;
	}

	// 加载
	private void load(String corpid, String jobid, final String jobtype) {
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "jobid", jobid);
		JsonUtils.put(jsonObject, "corpid", corpid);

		String transCode = Transcodes.TC_JOB_DETAILS;
		if (Job.Constants.JOB_TYPE_1.equalsIgnoreCase(jobtype)) {
			transCode = Transcodes.TC_HUNTER_JOB_DETAILS;
		}

		String data = RequestData.getData(jsonObject, transCode, false);

		RequestCallBackAdapters.InfoRequestCallBackAdapter<String> infoRequestCallBackAdapter = new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
			public void _onSuccess(ResponseInfo<String> response) {
				ResponseData responseData = ResponseData.create(response.result);
				JSONObject returnData = responseData.getBodyPropertyJsonObject("returnData");
				String jobinfo = JsonUtils.getString(returnData, "jobinfo");
				String corpinfo = JsonUtils.getString(returnData, "corpinfo");
				
				Gson gson = Gsons.create();
				Map<String, Object> jobinfoDatas = gson.fromJson(jobinfo, new TypeToken<Map<String, Object>>() {}.getType());
				Map<String, Object> corpinfoDatas = gson.fromJson(corpinfo, new TypeToken<Map<String, Object>>() {}.getType());

				ViewerUtils.setTextView(getThis(), jobinfoDatas, keys, viewIds);
				ViewerUtils.setTextView(getThis(), corpinfoDatas, keys2, viewIds2);

				// 地址 jobcity、address R.id.job_apply_details_address
				TextView address_txt = (TextView) getThis().findViewById(R.id.job_apply_details_address_txt);
				if (Job.Constants.JOB_TYPE_1.equalsIgnoreCase(jobtype)) {
					address_txt.setText((String) jobinfoDatas.get("jobcity"));
				} else {
					address_txt.setText((String) corpinfoDatas.get("address"));
				}

				status = (String) jobinfoDatas.get("status");
				
				// 发布日期
				TextView refreshdate_txt = (TextView) getThis().findViewById(R.id.job_apply_details_refreshdate_txt);
				long refreshdateLong = ((Double) jobinfoDatas.get("refreshdate")).longValue();
				refreshdate_txt.setText(DateUtils.format(new Date(refreshdateLong), "yyyy-MM-dd"));
			}
		};

		Https.post(this, transCode, data, infoRequestCallBackAdapter);
	}
}
