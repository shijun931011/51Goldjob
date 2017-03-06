package com.example.user.a51goldjob.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.os.Bundle;
import android.webkit.WebView;

import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.bean.AppConstants;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 简历预览
 * @author yeq
 *
 */
@ContentView(R.layout.activity_resume_preview)
public class ResumePreviewActivity extends BaseActivity {
	
	public static final String DETAIL_ID = "detailId";
	
	@ViewInject(R.id.resume_preview_webview)
	WebView resume_preview_webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.resume_preview); // 简历预览
	    
	    init();
	}

	private void init() {
        String id = getIntent().getExtras().getString(DETAIL_ID);
        if (StringUtils.isNotEmpty(id)) {
            loadDetails(id);
            return;
        }
	}

	private void loadDetails(String id) {
		Token token = getToken();
		if (token == null) {
			finish();
			return;
		}
		
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		JsonUtils.put(jsonObject, "resumeid", id); 

		String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_PREVIEW, false);
		
		Https.post(this, Transcodes.TC_RESUME_PREVIEW, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
			public void _onSuccess(ResponseInfo<String> response) {
				ResponseData responseData = ResponseData.create(response.result);
				String data = responseData.getBodyPropertyValue("data");
				getWebView().getSettings().setDefaultTextEncodingName(AppConstants.ENCODING) ;
				getWebView().getSettings().setJavaScriptEnabled(true); // 支持Javascript
				getWebView().loadDataWithBaseURL(null, data, AppConstants.MINE_TYPE_HTML, AppConstants.ENCODING, null); // WebView 打开内容显示
			}
		});
	}

	protected WebView getWebView() {
		return resume_preview_webview;
	}

}
