package com.example.user.a51goldjob.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 重置密码
 * @author yeq
 *
 */
@ContentView(R.layout.activity_reset_password)
public class ResetPasswordActivity extends BaseActivity {
	
	@ViewInject(R.id.reset_password_email_et)
	EditText reset_password_email_et;
	
	@ViewInject(R.id.reset_password_checkcode_et)
	EditText reset_password_checkcode_et;
	
	@ViewInject(R.id.reset_password_password_et)
	EditText reset_password_password_et;
	
	@ViewInject(R.id.reset_password_repassword_et)
	EditText reset_password_repassword_et;

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.reset_password);
	}

	// 获取验证码，验证码发送到邮箱
	@OnClick(R.id.reset_password_get_checkcode_txt)
	public void actionPerformed_resetPasswordGetCheckcodeTxtOnClick(View v) {
		String params = createGetCheckcodeParams();
		if (StringUtils.isEmpty(params)) {
			return;
		}
		Https.post(this, getGetCheckcodeTransCode(), params, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				// G0002j0000043{"returnCode":"AAAAAAA","returnMsg":"操作成功"}
				ResponseData responseData = ResponseData.create(response.result);
				ActivityUtils.showMessage(getThis(), false, responseData.getReturnMsg());
			}
		});
	}
	
	protected String createGetCheckcodeParams() {
		String email = ViewerUtils.getTextViewString(reset_password_email_et);
		if (StringUtils.isAllEmpty(email)) {
			String message = String.format("%s%s", getString(R.string.email_zh), getString(R.string.input_required));
			ActivityUtils.showMessage(this, false, message);
			return null;
		}
		
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "email", email);
		String data = RequestData.getData(jsonObject, getGetCheckcodeTransCode());
		return data;
	}
	
	protected String getGetCheckcodeTransCode() {
		return Transcodes.TC_RESET_GET_CHECKCODE_POST;
	}

	// 重置密码“提交”
	@OnClick(R.id.reset_password_submit_btn)
	public void actionPerformed_resetPasswordSubmitBtnOnClick(View v) {
		String params = createSubmitParams();
		if (StringUtils.isEmpty(params)) {
			return;
		}
		Https.post(this, getTransCode(), params, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				// G0002j0000043{"returnCode":"AAAAAAA","returnMsg":"操作成功"}
				ResponseData responseData = ResponseData.create(response.result);
				ActivityUtils.showMessage(getThis(), responseData.isSuccess(), responseData.getReturnMsg());
			}
		});
	}

	protected String createSubmitParams() {
		String email = ViewerUtils.getTextViewString(reset_password_email_et);
		String password = ViewerUtils.getTextViewString(reset_password_password_et);
		String checkCode = ViewerUtils.getTextViewString(reset_password_checkcode_et);
		String repassword = ViewerUtils.getTextViewString(reset_password_repassword_et);
		
		if (StringUtils.isAllEmpty(email, password, checkCode, repassword)) {
			ActivityUtils.showMessage(this, false, getString(R.string.input_required));
			return null;
		}
		
		if (!password.equalsIgnoreCase(repassword)) {
			ActivityUtils.showMessage(this, false, getString(R.string.twice_input_password_noequal));
			return null;
		}
		
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "email", email);
		JsonUtils.put(jsonObject, "password", password);
		JsonUtils.put(jsonObject, "verifycode", checkCode);
		String data = RequestData.getData(jsonObject, getTransCode());
		return data;
	}
	
	protected String getTransCode() {
		return Transcodes.TC_RESET_PASSWORD_POST;
	}
}
