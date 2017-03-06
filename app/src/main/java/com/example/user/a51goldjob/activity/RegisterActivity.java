package com.example.user.a51goldjob.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.chyjr.goldjob.fr.utils.MD5;
import com.chyjr.goldjob.fr.utils.StringUtils;
import com.chyjr.goldjob.fr.utils.ViewerUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 注册界面
 * @author yeq
 *
 */
@ContentView(R.layout.activity_register)
public class RegisterActivity extends BaseFormActivity implements View.OnClickListener{
	
	@ViewInject(R.id.register_email_et)
	EditText register_email_et;
	
	@ViewInject(R.id.register_password_et)
	EditText register_password_et;
	
	@ViewInject(R.id.register_repassword_et)
	EditText register_repassword_et;
	
	RequestCallBackAdapters.InfoRequestCallBackAdapter<String> infoRequestCallBackAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setBarTitle(R.string.register);
        
        init();
	}

	protected void init() {
		infoRequestCallBackAdapter = new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		};
		register_email_et.setFocusable(true);
		register_email_et.requestFocus();
		this.findViewById(R.id.submit_bottom_btn).setOnClickListener(this);;
	}

	// 提交“注册”
	protected void actionPerformed_submitOnClick(View v) {
		String params = createParams();
		if (StringUtils.isEmpty(params)) {
			return;
		}
		Https.post(this, getTransCode(), params, infoRequestCallBackAdapter);
	}

	protected String createParams() {
		String username = ViewerUtils.getTextViewString(register_email_et);
		String passwd = ViewerUtils.getTextViewString(register_password_et);
		String repassword = ViewerUtils.getTextViewString(register_repassword_et);
		int minLen = Integer.parseInt(getString(R.string.password_min_length));
		int maxLen = Integer.parseInt(getString(R.string.password_max_length));
		if (StringUtils.isAllEmpty(username, passwd, repassword)) {
			ActivityUtils.showMessage(this, false, getString(R.string.input_required));
			return null;
		}
		if (StringUtils.length(passwd)<minLen||StringUtils.length(passwd)>maxLen) {
			ActivityUtils.showMessage(this, false, getString(R.string.password_length_required));
			return null;
		}
		if (!passwd.equalsIgnoreCase(repassword)) {
			ActivityUtils.showMessage(this, false, getString(R.string.twice_input_password_noequal));
			return null;
		}
		
		JSONObject jsonObject = new JSONObject();
		
		JsonUtils.put(jsonObject, "username", username);
		JsonUtils.put(jsonObject, "passwd", MD5.md5Hex(passwd));

		String data = RequestData.getData(jsonObject, getTransCode());
		return data;
	}

	/**
	 * 处理反馈信息
	 * @param response
	 */
	protected void doSuccess(ResponseInfo<String> response) {
		// G0002j0000043{"returnCode":"AAAAAAA","returnMsg":"操作成功"}
		ResponseData responseData = ResponseData.create(response.result);
		ActivityUtils.showMessage(this, true, responseData.getReturnMsg());
	}
	
	protected String getTransCode() {
		return Transcodes.TC_REG_POST;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.submit_bottom_btn){
			actionPerformed_submitOnClick(v);
		}
	}
}
