package com.example.user.a51goldjob.activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.chyjr.goldjob.fr.utils.BindUtils;

import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.bean.AppConstants;
import com.example.user.a51goldjob.bean.Job;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * 资讯内容详情父类
 * @author yeq
 *
 */
public abstract class BaseNewsDetailsActivity extends BaseActivity {

	protected ImageView shareImageView;
	private String linkurl;
	protected Handler handler = new Handler();
	protected OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_btnOnClick(v);
		}
	};

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    //更多
	    linkurl = getIntent().getExtras().getString("linkurl");
	    if(getString(R.string.choose_url).equals(linkurl)){
        setBarTitle(R.string.choose_title);
	    	// 更多按钮
	        shareImageView = new ImageView(this);
	        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
	        shareImageView.setLayoutParams(params);
	        shareImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_policy));
	        getTopBar().setCustomViews(shareImageView,1000);
	        BindUtils.bindCtrlOnClickListener(onClickListener, shareImageView, getTopBar().getCustomLayout());
	    }
//	    else{
//	    // 分享
//		    shareImageView = new ImageView(this);
//	        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//	        shareImageView.setLayoutParams(params);
//	        shareImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_share));
//	        getTopBar().setCustomView(shareImageView);
//	    }

        String id = getIntent().getExtras().getString("infoid");
        String linkurl = getIntent().getExtras().getString("linkurl");
        if (StringUtils.isNotEmpty(id)) {
            loadNewsDetails(id);
            return;
        }
        if (StringUtils.isNotEmpty(linkurl)) {
        	loadContent(linkurl);
        	return;
        }
	}
	
	protected void loadContent(String linkurl) {
	//	if(getString(R.string.bottombar_offer_url).equals(linkurl)){
			//招聘HTML5超链接监听器
			getWebView().setWebViewClient(new WebViewClient(){
				public boolean shouldOverrideUrlLoading (WebView view, String url) {
					// 打开职位详情界面
					final String jobtype = Job.Constants.JOB_TYPE_0;
					String[] strs = url.split("\\*");
					if(strs.length>2){
							final String corpid = strs[1];
							final String jobid = strs[2];
						ActivityUtils.startActivity(handler, BaseNewsDetailsActivity.this, JobApplyDetailsActivity.class, new IIntentHandler() {
							public void putExtra(Intent intent) {
								intent.putExtra(JobApplyDetailsActivity.ParamsIn.JOB_ID, jobid);
								intent.putExtra(JobApplyDetailsActivity.ParamsIn.CORP_ID, corpid);
								intent.putExtra(JobApplyDetailsActivity.ParamsIn.JOB_TYPE, jobtype);
							}
						});
					}else if(strs.length==1){
						final String linkurl = strs[0];
						ActivityUtils.startActivity(handler, BaseNewsDetailsActivity.this, NewsDetailsActivity.class, new IIntentHandler(){
							public void putExtra(Intent intent) {
								intent.putExtra("linkurl", linkurl);
							}
						});
					}
					return true ; //表 示已经处理了这次URL的请求
				}
			});
		//}
		
			getWebView().getSettings().setDefaultTextEncodingName(AppConstants.ENCODING) ;
			getWebView().loadDataWithBaseURL(linkurl, null, AppConstants.MINE_TYPE_HTML, AppConstants.ENCODING, null);
			getWebView().loadUrl(linkurl);
	}
	
	protected void loadNewsDetails(String id) {
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "infoid", id);
		
		final String data = RequestData.getData(jsonObject, Transcodes.TC_NEWS_LIST);

		Https.post(this, Transcodes.TC_NEWS_LIST, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
			public void _onSuccess(ResponseInfo<String> response) {
				ResponseData responseData = ResponseData.create(response.result);
				// text
				String listStr = responseData.getBodyPropertyValue("list");
				try {
					JSONArray jsonArray = new JSONArray(listStr);
					if (jsonArray.length() <= 0) {
						finish();
					}
					String text = jsonArray.getJSONObject(0).getString("text");
					getWebView().getSettings().setDefaultTextEncodingName(AppConstants.ENCODING) ;
					getWebView().loadDataWithBaseURL(null, text, AppConstants.MINE_TYPE_HTML, AppConstants.ENCODING, null); // WebView 控件显示内容
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	protected abstract WebView getWebView();

	protected void actionPerformed_btnOnClick(View v) {
		ActivityUtils.startActivity(handler, this, MoreActivity.class);
	}
}
