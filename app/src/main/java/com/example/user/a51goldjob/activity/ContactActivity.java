package com.example.user.a51goldjob.activity;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

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
 * 意见反馈
 * @author yeq
 *
 */
@ContentView(R.layout.activity_contact)
public class ContactActivity extends BaseActivity {
	
	@ViewInject(R.id.contact_email_txt)
	EditText contact_email_txt;
	
	@ViewInject(R.id.contact_description_txt)
	EditText contact_description_txt;

	RequestCallBackAdapters.InfoRequestCallBackAdapter<String> infoRequestCallBackAdapter;
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.other_contact);
	    
	    init();
	}

	private void init() {
		infoRequestCallBackAdapter = new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		};
	}

	@OnClick(R.id.contact_send_btn)
	public void actionPreformed_contactSendBtnOnClick(View v) {
		Https.post(this, getTransCode(), createParams(), infoRequestCallBackAdapter);
	}

	protected String createParams() {
		JSONObject jsonObject = new JSONObject();
		String contact = ViewerUtils.getTextViewString(contact_email_txt);
		String content = ViewerUtils.getTextViewString(contact_description_txt);
		JsonUtils.put(jsonObject, "contact", contact);
		JsonUtils.put(jsonObject, "content", content);

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
		return Transcodes.TC_CONTACT_POST;
	}
}
