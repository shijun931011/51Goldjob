package com.example.user.a51goldjob.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ImageView;

import com.chyjr.goldjob.fr.utils.BindUtils;
import com.example.user.a51goldjob.R;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 内容详情
 * @author yeq
 *
 */
@ContentView(R.layout.activity_news_details)
public class NewsDetailsActivity extends BaseNewsDetailsActivity {
	
	@ViewInject(R.id.news_details_webview)
	WebView news_details_webview;
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    	setBarTitle(R.string.news_details);
        
	}
	
	
	protected WebView getWebView() {
		return news_details_webview;
	}

}
