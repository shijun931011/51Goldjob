package com.example.user.a51goldjob.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import org.json.JSONObject;

import android.content.Intent;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;


import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.ViewerUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.bean.AdHome;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Job;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.mgr.AppMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.SlideShowView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

/**
 * 首页
 * @author yeq
 *
 */
@ContentView(R.layout.activity_home)
public class HomeActivity extends com.chyjr.goldjob.fr.activity.BaseActivity {
	
	@ViewInject(R.id.home_title_left_text)
	TextView home_title_left_text;
	
	@ViewInject(R.id.corp_job_keywords_et)
	EditText corp_job_keywords_et;
	
	@ViewInject(R.id.home_title_logout_img)
	ImageView home_title_logout_img;
	
	@ViewInject(R.id.home_title_login_img)
	ImageView home_title_login_img;

	@ViewInject(R.id.home_title_more_img)
	ImageView home_title_more_img;
	
	@ViewInject(R.id.home_bottom_collect_img)
	ImageView home_bottom_collect_img;
	
	@ViewInject(R.id.home_bottom_unfold_img)
	ImageView home_bottom_unfold_img;
	
	@ViewInject(R.id.home_bottom_msg_img)
	ImageView home_bottom_msg_img;

	@ViewInject(R.id.activity_news_list_slideshowview_home)
	View activity_news_list_slideshowview_home;
	
	@ViewInject(R.id.slideshowView)
	SlideShowView slideshowView; // Ad 显示控件
	
	BitmapUtils bitmapUtils;
	Handler handler = new Handler();
	AppMgr appMgr = Mgr.get(AppMgr.class);

	// 受控的菜单：必须登录后才能访问
	int[] ctrlMenuIds = {
		R.id.home_usercenter_img,
		R.id.home_myresume_img,
		R.id.home_bottom_collect_img,
		R.id.home_bottom_msg_img
	};

	// 菜单+按钮
	int[] menuIds = {
		R.id.home_usercenter_img, R.id.home_corpjob_img,
		R.id.home_myresume_img, R.id.home_hunterjob_img,
		R.id.home_news_img, R.id.home_title_more_img,
		R.id.home_title_left_text, R.id.home_title_login_img,
		R.id.home_bottom_collect_img,R.id.home_bottom_unfold_img,
		R.id.home_bottom_msg_img,R.id.home_selection_img
	};
	
	// 菜单、按钮关联的界面
	Class<?>[] activities = {
		UserCenterActivity.class, CorpJobActivity.class,
		MyResumeActivity.class, HunterJobActivity.class,
		NewsActivity.class, OtherActivity.class,
		CityChooserActivity.class, LoginActivity.class,
		JobFavoriteActivity.class, null,//对应home_bottom_unfold_img按钮跳转html5内容
		MessageActivity.class,null
	};
	
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Log.d("shijun","onCreate(Bundle savedInstanceState)");
		initImageLoaderConfig();
		Log.d("shijun","initImageLoaderConfig()");
	    hasNewMsg();
	    initAd();
		Log.d("shijun","initAd()");
	    intKeywordsView();
		Log.d("shijun"," intKeywordsView()");
	    initWindowDisplay();
	}
	
	//ImageLoaderConfiguration的配置
	private void initImageLoaderConfig(){
		File cache=StorageUtils.getOwnCacheDirectory(this,Environment.getExternalStorageDirectory()+"imageloader/"+System.currentTimeMillis()+"Cache");
		ImageLoaderConfiguration configuration=new ImageLoaderConfiguration
	    .Builder(this)
		.memoryCacheExtraOptions(480, 800)
		.threadPoolSize(3)
		.threadPriority(Thread.NORM_PRIORITY-2)
		.denyCacheImageMultipleSizesInMemory()
		.memoryCache(new UsingFreqLimitedMemoryCache(2*1024*1024))
		.memoryCacheSize(2*1024*1024)
		.discCacheSize(50*1024*1024)
		.discCacheFileNameGenerator(new Md5FileNameGenerator())
		.tasksProcessingOrder(QueueProcessingType.LIFO)
		.discCacheFileCount(100)
		.discCache(new UnlimitedDiscCache(cache))
		.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
		.imageDownloader(new BaseImageDownloader(this, 5*1000, 30*100))
		.build();
		ImageLoader.getInstance().init(configuration);
	}
	
	
	//分辨率适配
	private void initWindowDisplay() {
		// TODO Auto-generated method stub
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		int W = mDisplayMetrics.widthPixels;
		int H = mDisplayMetrics.heightPixels;
		System.out.println("W="+W+",H="+H);
	}

	private void intKeywordsView(){
		corp_job_keywords_et.setOnTouchListener(onTouchListener);
	}
	
	OnTouchListener onTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			if (event.getAction() == MotionEvent.ACTION_UP) {
				if (((TextView) v).getCompoundDrawables()[2] != null) {

					boolean touchable = event.getX() > (v.getWidth() - ((TextView) v).getTotalPaddingRight())
							&& (event.getX() < ((v.getWidth() - v.getPaddingRight())));

					if (touchable) {
						actionPerformed_corpJobSearchBtnOnClick(v);
					}
				}
			}

			return v.onTouchEvent(event);
		}
	};
	
	/**
	 * 加载广告
	 * */
	private void initAd() {
		//判断sd卡是否存在
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			//bitmapUtils = new BitmapUtils(this, this.getExternalCacheDir().getAbsolutePath());
		}	
		// 获取广告
		String str="";
		JSONObject jsonObject = new JSONObject();            //C0000
		final String data = RequestData.getData(jsonObject, Transcodes.TC_HOME_AD_LIST);
		Https.post(this, Transcodes.TC_HOME_AD_LIST, data, new RequestCallBackAdapters.RequestCallBackAdapter<String>() {
			protected void _onSuccess(ResponseInfo<String> response) {
				String text = response.result;
				if (StringUtils.isEmpty(text)) {
					return;
				}
				
				ResponseData responseData = ResponseData.create(text);
				String str = responseData.getBodyPropertyValue("returnData");
				if (StringUtils.isEmpty(str)) {
					return;
				}
				
				Gson gson = new Gson();
				List<AdHome> adHomeList = gson.fromJson(str, new TypeToken<List<AdHome>>(){}.getType());
				if (CollectionUtils.isEmpty(adHomeList)) {
					return;
				}				
				List<ImageView> imgList = new ArrayList<ImageView>();				
				ImageView imageView = null;
				//首页广告
				for (AdHome adHome : adHomeList) {
					imageView = new ImageView(HomeActivity.this);
					imageView.setOnClickListener(adOnClickListener);
					imageView.setImageBitmap(null);
					imageView.setTag(adHome);
					String url=adHome.getImage_url();					
					imageView.setScaleType(ScaleType.CENTER_CROP);
					//bitmapUtils.display(imageView, adHome.getImage_url());				
					ImageLoader.getInstance().displayImage(url, imageView);
					imgList.add(imageView);
				}					
				slideshowView.initViewList(imgList, R.layout.ad_bottom_item);
				slideshowView.setDotGravity(Gravity.LEFT);
				slideshowView.startPlay();
			}
			@Override
			public void onFailure(HttpException exception, String result) {
				// TODO Auto-generated method stub
				super.onFailure(exception, result);
				Toast.makeText(HomeActivity.this,"网络异常请稍后重试!",Toast.LENGTH_LONG).show();
			}		
		});
	}
	
	/**
	 * 广告点击监听
	 * */
	OnClickListener adOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			ImageView imageView = (ImageView) v;
			Object tag = imageView.getTag();
			if (tag == null || !(tag instanceof AdHome)) {
				return;
			}	
			final AdHome adHome = (AdHome) tag;
			ActivityUtils.startActivity(handler, HomeActivity.this, NewsDetailsActivity.class, new IIntentHandler(){
				public void putExtra(Intent intent) {
					intent.putExtra("linkurl", adHome.getHyperlinks());
				}
			});
		}
	};	
	
	private String city="";
	protected void onResume() {
		super.onResume();
	    // 获取当前登录状态
		AppContext appContext = AppContext.get(this);
		Log.d("shijun","appContext");
	    if (appContext.isLogin()) {
	    	home_title_logout_img.setVisibility(View.VISIBLE);
	    	home_title_login_img.setVisibility(View.GONE);
	    } else {
	    	home_title_logout_img.setVisibility(View.GONE);
	    	home_title_login_img.setVisibility(View.VISIBLE);
	    }
	    city = appContext.getCity(getString(R.string.national_city));
	    this.home_title_left_text.setText(city); // 动态从接口中获取
	}
	
	/**
	 * 没有登录，消息提示。
	 * */
	private boolean noLogin(int viewid) {
		boolean nologin = !AppContext.get(this).isLogin();
		Log.d("shijun","nologin");
		if (nologin) {
		//	DialogUtils.commonInfo(this, this.getResources().getString(R.string.nologin_error));
			Toast.makeText(HomeActivity.this,"请先登录...", Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(this,LoginActivity.class);
			intent.putExtra("viewid",viewid);
			startActivity(intent);
		}
		return nologin;
	}
	
	//对各个区域进行动作监听
	@OnClick(value={R.id.home_usercenter_img, R.id.home_corpjob_img, 
			R.id.home_myresume_img, R.id.home_hunterjob_img, 
			R.id.home_news_img, R.id.home_title_more_img, 
	    	R.id.home_title_left_text, R.id.home_title_logout_img, 
	    	R.id.home_title_login_img, R.id.home_bottom_collect_img,
			R.id.home_bottom_unfold_img, R.id.home_bottom_msg_img,
			R.id.home_selection_img})
	public void actionPerformed_homeImgOnClick(View v) {
		int viewId = v.getId();
		Log.d("shijun","viewId");
		
		
		// 权限控制
		for (int i=0; i<ctrlMenuIds.length; i++) {
			if (ctrlMenuIds[i] == viewId) {
				if (noLogin(viewId)) {	//如果没有登录，则返回；				
					return;
				}
			}
		}
		
		// 打开菜单
		for (int i=0; i<menuIds.length; i++) {
			if (menuIds[i] == viewId) {
				if(R.id.home_bottom_unfold_img == viewId||R.id.home_selection_img == viewId){
					//跳转HTML5页面
					String tempUrl = "";
					if(R.id.home_bottom_unfold_img == viewId){
						tempUrl = getString(R.string.bottombar_offer_url);
					}else if(R.id.home_selection_img == viewId){
						tempUrl = getString(R.string.choose_url);
					}
					final String url = tempUrl;
					ActivityUtils.startActivity(handler, HomeActivity.this, NewsDetailsActivity.class, new IIntentHandler(){
						public void putExtra(Intent intent) {
							intent.putExtra("linkurl", url);
						}
					});
					return;
				}else if(R.id.home_bottom_msg_img == viewId || R.id.home_title_login_img == viewId || R.id.home_title_logout_img == viewId){
					ActivityUtils.startActivityForResult(handler, this, activities[i]);
					return;
				}else{
					if(i==6){
						ActivityUtils.startActivitys(handler, this, activities[i],city);
					}else{
						ActivityUtils.startActivity(handler, this, activities[i]);
					}
				
					return;
				}
			}
		}
		
		if (viewId == R.id.home_title_logout_img) {
			ActivityUtils.confirmMessage(this, true, getString(R.string.confirm_logout));
			
		}
	}
	
	/**
	 * 职位搜索
	 * */
	public void actionPerformed_corpJobSearchBtnOnClick(View v) {
		// 查询
		ActivityUtils.startActivity(handler, this, JobListActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				
				String cityName = home_title_left_text.getText().toString();
				Log.d("shijun","cityName");
				if (getString(R.string.national_city).equalsIgnoreCase(cityName)) {
					cityName = "";
				}
				String keywrods = ViewerUtils.getTextViewString(corp_job_keywords_et);
				Log.d("shijun","keywrods");
				intent.putExtra(JobListActivity.Constants.PARAM_JOB_TYPE, Job.Constants.JOB_TYPE_0);
				intent.putExtra(JobListActivity.ParamsIn.KEYWORDS, keywrods); 
				intent.putExtra(JobListActivity.ParamsIn.KEYWORD_TYPE, ""); 
				intent.putExtra(JobListActivity.ParamsIn.AREA_ID, cityName); 
			}
		});
	}

	/**
	 * 是否有新消息
	 */
	private void hasNewMsg(){
		Token token=AppContext.get(this).getToken();
		Log.d("shijun","hasNewMsg()" + token);
		if(token!=null){
			Log.d("shijun","shcujvh" + token);
			JSONObject jsonObject = new JSONObject();
			JsonUtils.put(jsonObject, "token", token.getToken());
			JsonUtils.put(jsonObject, "userid", token.getUserId());
			final String data = RequestData.getData(jsonObject, Transcodes.TC_MESSAGE_NEW, false);
			Https.post(this, Transcodes.TC_MESSAGE_NEW, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
				protected void _onSuccess(ResponseInfo<String> response) throws Exception {
					ResponseData responseData = ResponseData.create(response.result);
					String data = responseData.getBody();
					Map<String, Object> map = Gsons.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
					Double newmsg = (Double) map.get("newmsg");
					if(1==newmsg){
					    home_bottom_msg_img.setImageResource(R.drawable.icon_new_msg);
					}else{
						home_bottom_msg_img.setImageResource(R.drawable.icon_msg);
					}
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		hasNewMsg();
	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-exitTime) > 2000){  
	            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
	            exitTime = System.currentTimeMillis();   
	        } else {
	            finish();
	            System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	
	
}
