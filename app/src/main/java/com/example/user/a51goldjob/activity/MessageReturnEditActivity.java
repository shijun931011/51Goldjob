package com.example.user.a51goldjob.activity;

import org.json.JSONObject;

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

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
@ContentView(R.layout.activity_message_return_edit)
public class MessageReturnEditActivity extends BaseFormActivity {

	@ViewInject(R.id.message_return_textarea)
	EditText message_return_textarea;
	
	/** Called when the activity is first created. */
	Handler handler = new Handler();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.message_send);
	    // TODO Auto-generated method stub
	   
	}

	protected int getSubmitTextStringId() {
		return R.string.opt_send;
	}
	
	protected void actionPerformed_submitOnClick(View v) {
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		
		String id = getIntent().getExtras().getString("id");
		JsonUtils.put(jsonObject, "id", id); 
		JsonUtils.put(jsonObject, "content", message_return_textarea.getText().toString()); 
		
		final String data = RequestData.getData(jsonObject, Transcodes.TC_MESSAGE_RETURN, false);
		Https.post(this, Transcodes.TC_MESSAGE_RETURN, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		});
	}
	
	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		ActivityUtils.showMessage(this, responseData.isSuccess(), responseData.getReturnMsg());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
