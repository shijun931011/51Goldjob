package com.example.user.a51goldjob.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.chyjr.goldjob.fr.utils.MD5;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 更改密码
 * @author yeq
 *
 */
@ContentView(R.layout.activity_changed_password)
public class ChangedPasswordActivity extends BaseFormActivity implements View.OnClickListener{
	
	@ViewInject(R.id.password_old_et)
	EditText password_old_et;
	
	@ViewInject(R.id.password_new_et)
	EditText password_new_et;
	
	@ViewInject(R.id.password_confirm_et)
	EditText password_confirm_et;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.user_center_changed_password);
	    password_old_et.requestFocus();
	    this.findViewById(R.id.changepwd).setOnClickListener(this);;
	}

	// 提交
	protected void actionPerformed_submitOnClick(View v) {
		String passwordOld = password_old_et.getText().toString();
		String passwordNew = password_new_et.getText().toString();
		String passwordConfirm = password_confirm_et.getText().toString();
		int minLen = Integer.parseInt(getString(R.string.password_min_length));
		int maxLen = Integer.parseInt(getString(R.string.password_max_length));
		if (StringUtils.isEmpty(passwordOld) || StringUtils.isEmpty(passwordNew) || StringUtils.isEmpty(passwordConfirm)) {
			ActivityUtils.showMessage(this, false, getString(R.string.input_required)); 
			return;
		}
		
		if (passwordOld.equalsIgnoreCase(passwordNew)) {
			// 旧密码和新密码相同，提示
		}
		
		if (!(passwordNew.equalsIgnoreCase(passwordConfirm))) {
			ActivityUtils.showMessage(this, false, getString(R.string.twice_input_password_noequal)); // 二次输入密码不正确
			return;
		}
		if (StringUtils.length(passwordNew)<minLen||StringUtils.length(passwordNew)>maxLen) {
			ActivityUtils.showMessage(this, false, getString(R.string.password_length_required));
			return;
		}
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken()); // token
		JsonUtils.put(jsonObject, "userid", token.getUserId()); // userid
		JsonUtils.put(jsonObject, "oldpasswd", MD5.md5Hex(passwordOld)); // userid
		JsonUtils.put(jsonObject, "newpasswd", MD5.md5Hex(passwordNew)); // userid
		
		final String data = RequestData.getData(jsonObject, Transcodes.TC_CHANGED_PASSWORD, false);
		Https.post(this, Transcodes.TC_CHANGED_PASSWORD, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		});
	}
	
	/**
	 * 提交完成后信息提示
	 * */
	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		if (responseData.isSuccess()) {
			ActivityUtils.showMessage(this, true, responseData.getReturnMsg()); 
			return;
		}
		ActivityUtils.showMessage(this, false, responseData.getReturnMsg()); 
		resetForm();
	}

	/**
	 * 重置页面
	 * */
	protected void resetForm() {
		password_old_et.setText("");
		password_new_et.setText("");
		password_confirm_et.setText("");
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.changepwd){
			actionPerformed_submitOnClick(v);
		}
		
	}
}
