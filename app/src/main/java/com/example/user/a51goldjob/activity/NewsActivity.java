package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.BindUtils;
import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.NewsListAdapter;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.bean.AdNews;
import com.example.user.a51goldjob.bean.News;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.PublicViews;
import com.example.user.a51goldjob.view.PullToRefreshListView;
import com.example.user.a51goldjob.view.SlideShowView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 金融快讯
 * @author yeq
 *
 */
@ContentView(R.layout.activity_news)
public class NewsActivity extends BaseListActivity implements OnClickListener{
	
	@ViewInject(R.id.news_refresh_head)
	LinearLayout news_refresh_head;
	
	@ViewInject(R.id.activity_news_list_slideshowview)
	View activity_news_list_slideshowview;
	
	@ViewInject(R.id.slideshowView)
	SlideShowView slideshowView;
	
	@ViewInject(R.id.news_listview)
	PullToRefreshListView news_listview;

	@ViewInject(R.id.activity_news_progress_bar)
	View activity_news_progress_bar;
	
	BitmapUtils bitmapUtils;

	ImageView policyImageView;
	Handler handler = new Handler();
	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_btnOnClick(v);
		}
	};
	
	OnClickListener adOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			if (!(v instanceof ImageView)) {
				return;
			}
			ImageView imageView = (ImageView) v;
			Object tag = imageView.getTag();
			if (tag == null || !(tag instanceof AdNews)) {
				return;
			}
			final AdNews adNews = (AdNews) tag;
			ActivityUtils.startActivity(handler, NewsActivity.this, NewsDetailsActivity.class, new IIntentHandler(){
				public void putExtra(Intent intent) {
					intent.putExtra("linkurl", adNews.getLinkurl());
				}
			});
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setBarTitle(R.string.btn_news);
        TextView createResumetxt= PublicViews.createMenuTextView(this, "",Color.WHITE, this);
        createResumetxt.setId(500);
	    createResumetxt.setOnClickListener(this);
	    createResumetxt.setBackgroundResource(R.drawable.icon_more);
	}
	
	protected void initListView() {
	 LinearLayout	customLayout = (LinearLayout) this.findViewById(R.id.topbar_active_icon_layout);
		// 政策法规按钮
        policyImageView = new ImageView(this);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        policyImageView.setLayoutParams(params);
        policyImageView.setImageDrawable(getResources().getDrawable(R.drawable.icon_policy));
        customLayout.addView(policyImageView);
        //getTopBar().setCustomView(policyImageView,1000);
        BindUtils.bindCtrlOnClickListener(onClickListener, policyImageView, getTopBar().getCustomLayout());
        this.initAd();
        super.initListView();
	}

	private void initAd() {
		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
			bitmapUtils = new BitmapUtils(this, this.getExternalCacheDir().getAbsolutePath());
		}

		// 获取广告
		JSONObject jsonObject = new JSONObject();
		final String data = RequestData.getData(jsonObject, Transcodes.TC_AD_LIST);
		
		Https.post(this, Transcodes.TC_AD_LIST, data, new RequestCallBackAdapters.RequestCallBackAdapter<String>() {
			protected void _onSuccess(ResponseInfo<String> response) {
				String text = response.result;
				if (StringUtils.isEmpty(text)) {
					return;
				}
				
				ResponseData responseData = ResponseData.create(text);
				String str = responseData.getBodyPropertyValue("list");
				if (StringUtils.isEmpty(str)) {
					return;
				}
				
				Gson gson = new Gson();
				List<AdNews> adNewsList = gson.fromJson(str, new TypeToken<List<AdNews>>(){}.getType());
				if (CollectionUtils.isEmpty(adNewsList)) {
					return;
				}
				
				List<ImageView> imgList = new ArrayList<ImageView>();
				
				ImageView imageView = null;
				for (AdNews adNews : adNewsList) {
					imageView = new ImageView(NewsActivity.this);
					imageView.setOnClickListener(adOnClickListener);
					imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.haiyin));
					imageView.setTag(adNews);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					bitmapUtils.display(imageView, adNews.getPhotourl());
					imgList.add(imageView);
				}
				
				slideshowView.initViewList(imgList, R.layout.ad_bottom_item);
				slideshowView.setDotGravity(Gravity.LEFT);
				slideshowView.startPlay();
			}
		});
	}

	protected void actionPerformed_onItemClick(AdapterView<?> parent, View view, int position, long id) {
  		//点击头部、底部栏无效
		if(view == lvFavorite_footer) return;
		
		final News news = (News) getListView().getItemAtPosition(position);
		ActivityUtils.startActivity(handler, NewsActivity.this, NewsDetailsActivity.class, new IIntentHandler(){
			public void putExtra(Intent intent) {
				intent.putExtra("infoid", news.getInfoid());
			}
		});
	}
	
	private List<News> getNews(ResponseData responseData) {
		List<News> newsList = new ArrayList<News>();
		String listStr = responseData.getBodyPropertyValue("list");
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(listStr);
			News news = null;
			JSONObject jsonObject = null;
			for (int i=0; i < jsonArray.length(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				news = new News();
				news.setInfoid(JsonUtils.getString(jsonObject, "infoid"));
				news.setPreviewText(JsonUtils.getString(jsonObject, "preview_text"));
				news.setTitle(JsonUtils.getString(jsonObject, "title"));
				news.setGroupid(JsonUtils.getString(jsonObject, "groupid"));
				news.setGroupname(JsonUtils.getString(jsonObject, "groupname"));
				news.setPhotourl(JsonUtils.getString(jsonObject, "photourl"));
				news.setPubdate(JsonUtils.getString(jsonObject, "pubdates"));
				news.setCreatedate(JsonUtils.getString(jsonObject, "createdates"));
				news.setAuthor(JsonUtils.getString(jsonObject, "author"));
				news.setSource(JsonUtils.getString(jsonObject, "source"));
				newsList.add(news);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return newsList;
	}

	protected void actionPerformed_btnOnClick(View v) {
		ActivityUtils.startActivity(handler, this, PolicyActivity.class);
	}

	@Override
	protected List<?> parseDataList(ResponseData responseData) throws Exception {
		List<News> list = getNews(responseData);
		return list;
	}

	@Override
	protected String getJsonParams(int page) {
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "page", String.valueOf(page)); // cc@test.com
		JsonUtils.put(jsonObject, "groupid", "1"); // 111
		
		String data = RequestData.getData(jsonObject, getUrlKey());
		return data;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected BaseAdapter createListViewAdapter() {
		List dataList = getDataList();
		return new NewsListAdapter(NewsActivity.this, dataList);
	}

	protected void afterLoaded() {
		activity_news_progress_bar.setVisibility(View.GONE);
		activity_news_list_slideshowview.setVisibility(View.VISIBLE);
	}

	@Override
	protected PullToRefreshListView getListView() {
		return news_listview;
	}

	@Override
	protected String getUrlKey() {
		return Transcodes.TC_NEWS_LIST;
	}

	protected LinearLayout getHeadView() {
		return news_refresh_head;
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==500){
			ActivityUtils.startActivity(handler, this, PolicyActivity.class);
		}
		
	}
}
