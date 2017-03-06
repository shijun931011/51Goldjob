package com.example.user.a51goldjob.activity;

import android.os.Bundle;
import android.webkit.WebView;


import com.example.user.a51goldjob.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 政策法规明细
 * @author yeq
 *
 */
@ContentView(R.layout.activity_policy_list_details)
public class PolicyListDetailsActivity extends BaseNewsDetailsActivity {
	
	@ViewInject(R.id.policy_list_details_webview)
	WebView policy_list_details_webview;

	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setBarTitle(R.string.news_policy);
	}
	
	protected WebView getWebView() {
		return policy_list_details_webview;
	}
}
