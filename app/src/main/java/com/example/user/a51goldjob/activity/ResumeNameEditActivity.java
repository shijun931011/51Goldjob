package com.example.user.a51goldjob.activity;

import org.json.JSONObject;

import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
@ContentView(R.layout.activity_resume_name_edit)
public class ResumeNameEditActivity extends BaseFormActivity {

	@ViewInject(R.id.resume_name_id)
	EditText	resume_name_id;
	
	Handler handler = new Handler();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.resume_name);
	    // TODO Auto-generated method stub
	    String name = getIntent().getExtras().getString("resumename");
	    resume_name_id.setText(name);
	}

	protected void actionPerformed_submitOnClick(View v) {
		String reason = checkForm();
		if(reason!=null&&!"".equals(reason)){
			DialogUtils.commonInfo(this, reason);
			return;
		}
		
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId());

		String resumeid = getIntent().getExtras().getString("resumeid");
		JsonUtils.put(jsonObject, "resumeid", resumeid);
		JsonUtils.put(jsonObject, "resumename", resume_name_id.getText()
				.toString()); 

		final String data = RequestData.getData(jsonObject,
				Transcodes.TC_RESUME_SET_ISOPEN, false);
		Https.post(this, Transcodes.TC_RESUME_SET_ISOPEN, data,
				new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
					protected void _onSuccess(ResponseInfo<String> response)
							throws Exception {
						doSuccess(response);
					}
				});
	}
	
	
	
	private String checkForm() {
		// TODO Auto-generated method stub
		String reason = "";
		if(resume_name_id.getText()==null||"".equals(resume_name_id.getText().toString().trim())){
			reason = this.getResources().getString(R.string.check_form_no_whole);
		}
		if(resume_name_id.length()>20){
			reason = this.getResources().getString(R.string.check_resume_name_length);
		}
		return reason;
	}

	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		ActivityUtils.showMessage(this, responseData.isSuccess(),
				responseData.getReturnMsg());
		ActivityUtils.returnActivity(handler, this, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra("resumename", resume_name_id.getText().toString());
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	
}
