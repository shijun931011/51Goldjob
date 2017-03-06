package com.example.user.a51goldjob.activity;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.IntentUtils;
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
import com.example.user.a51goldjob.view.RadioButtonViews;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 选择简历
 * @author yeq
 *
 */
@ContentView(R.layout.activity_choose_resume)
public class ChooseResumeActivity extends BaseActivity {
	
	// 参数列表
	public static final String[] PARAMS_IN = {
		"jobname" ,
		"corpid" ,
		"jobid" ,
		"jobtype" ,
		"hhid"
	};
	
	@ViewInject(R.id.choose_resume_rg)
	RadioGroup choose_resume_rg;
	
	@ViewInject(R.id.choose_resume_jobname_txt)
	TextView choose_resume_jobname_txt;
	
	RadioButtonViews.RadioButtonViewHelper radioButtonViewHelper;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
		setBarTitle(R.string.choose_resume);
		
		init();
	}

	protected void init() {
		String jobname = IntentUtils.getStringExtra(this, "jobname");
		choose_resume_jobname_txt.setText(jobname);
		radioButtonViewHelper = RadioButtonViews.RadioButtonViewHelper.create(this, choose_resume_rg);
		Token token = AppContext.get(this).getToken();
	
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 

		final String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_LIST, false);
		Https.post(this, Transcodes.TC_RESUME_LIST, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				initUserResume(response);
			}
		});
		loadingDialogsHelper.dismiss();
	}
	
	/**
	 * 初始化用户简历
	 * @param response
	 */
	protected void initUserResume(ResponseInfo<String> response) {
		// {"datanum":2,"returnCode":"AAAAAAA","returnMsg":"操作成功"
		// ,"data":"[
		// {\"resumeid\":\"100005\",\"whocomp\":\"2\",\"whohh\":\"3\",\"userid\":\"60001\",\"percent\":100,\"resumename\":\"测历10\",\"companyid\":\"仅对企业公开\",\"updateby\":\"60001\",\"createby\":\"60001\",\"createdate\":1407982134000,\"updatedate\":1411370530000,\"activeflag\":1,\"defaults\":0}
		// ,{\"resumeid\":\"30001\",\"whocomp\":\"1\",\"whohh\":\"0\",\"userid\":\"60001\",\"percent\":100,\"resumename\":\"测试简历1111\",\"companyid\":\"仅对企业公开\",\"createby\":\"60001\",\"createdate\":1407554246000,\"updatedate\":1409731395000,\"activeflag\":1,\"defaults\":1}]"}
		ResponseData responseData = ResponseData.create(response.result);
		int datanum = Integer.valueOf(responseData.getBodyPropertyValue("datanum"));
		if (datanum <= 0) {
			ActivityUtils.showMessage(this, true, getString(R.string.please_create_resume_first)); // 请先完善简历
			return;
		}
		if (!(responseData.isSuccess())) {
			ActivityUtils.showMessage(this, true, responseData.getReturnMsg()); // 弹出错误信息
			return;
		}
		String data = responseData.getBodyPropertyValue("data");
		Gson gson = Gsons.create();
		List<Map<String, Object>> resumes = gson.fromJson(data, new TypeToken<List<Map<String, Object>>>() {}.getType());
		if (CollectionUtils.isEmpty(resumes)) {
			ActivityUtils.showMessage(this, true, getString(R.string.please_create_resume_first)); // 请先完善简历
			return;
		}
		radioButtonViewHelper.createRadioButtons(resumes, "resumename", "resumeid", null); // 获取数据列表，组装RadioButton，放入RadioGroup
	}
	
	/**
	 * 发送简历
	 * @param view
	 */
	@OnClick(R.id.choose_resume_submit_btn)
	public void actionPerformed_chooseResumeSubmit(View view) {
		String jobname = IntentUtils.getStringExtra(this, "jobname");
		String jobid = IntentUtils.getStringExtra(this, "jobid");
		String corpid = IntentUtils.getStringExtra(this, "corpid");
		String jobtype = IntentUtils.getStringExtra(this, "jobtype");
		String resumeid = (String) radioButtonViewHelper.getSelectedItemTag(); // resumeid 简历编号
		
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		JsonUtils.put(jsonObject, "resumeid", resumeid); // userid 用户编号
		JsonUtils.put(jsonObject, "jobid", jobid); // jobid 职位编号
		JsonUtils.put(jsonObject, "corpid", corpid); // corpid 公司编号
		
		String transCode;
		if (Job.Constants.JOB_TYPE_1.equalsIgnoreCase(jobtype)) {
			transCode = Transcodes.TC_HUNTER_RESUME_SEND;
			JsonUtils.put(jsonObject, "hhid", jobname); // hhid 猎头ID
		} else {
			transCode = Transcodes.TC_RESUME_SEND;
			JsonUtils.put(jsonObject, "jobname", jobname); // jobname 职位名称
		}

		final String data = RequestData.getData(jsonObject, transCode, false);
		Https.post(this, transCode, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		});
	}

	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		// 00000j0000047{"returnCode":"1000002","returnMsg":"职位已经投递过了"}
		ActivityUtils.showMessage(this, responseData.isSuccess(), responseData.getReturnMsg());
	}
}
