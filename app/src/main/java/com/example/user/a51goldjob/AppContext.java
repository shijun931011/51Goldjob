package com.example.user.a51goldjob;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.example.user.a51goldjob.bean.AppExceptionHandler;
import com.example.user.a51goldjob.bean.Dict;
import com.example.user.a51goldjob.bean.Token;
import com.lidroid.xutils.DbUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;


/**
 * 应用程序入口
 * @author yeq
 *
 */
public class AppContext extends Application {
	private static DisplayImageOptions mDisplayImageOptions;
	private static ImageLoaderConfiguration mImageLoaderConfiguration;
	private DbUtils db;
	private String KEY_TOKEN = "token";
	private String KEY_DICTMAPS = "dictMaps";
	private String KEY_CURRENT_CITY = "currentCity";
	private String KEY_APP_INFO = "appInfo";
	private Map<String, Object> cache = new HashMap<String, Object>();



	public void onCreate() {
		super.onCreate();
		Log.d("shijun","onCreate()");
		AppExceptionHandler appExceptionHandler = AppExceptionHandler.getInstance();
		appExceptionHandler.init(this);
		//初始化ImageLoader的配置
		//initImageLoaderConfiguration();
		Log.d("shijun","initImageLoaderConfiguration(this)");
		initImageLoaderConfiguration(this);
	}

	public synchronized void setAppInfoMap(Map<String, String> appInfos) {
		cache.put(KEY_APP_INFO, appInfos);
	}
	
	/**
	 * 获取应用信息
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String getAppInfo(String key) {
		Map<String, String> appInfos = (Map<String, String>) cache.get(KEY_APP_INFO);
		if (CollectionUtils.isEmpty(appInfos)) {
			return null;
		}
		return appInfos.get(key);
	}
	
	/**
	 * 获取当前城市
	 * @return
	 */
	public String getCity() {
		return getCity(null);
	}

	/**
	 * 获取当前城市，如果没有返回默认城市
	 * @param defaultCity
	 * @return
	 */
	public String getCity(String defaultCity) {
		Dict dictCity = (Dict) cache.get(KEY_CURRENT_CITY);
		if (dictCity == null) {
			return defaultCity;
		}
		return dictCity.getValue();
	}

	/**
	 * 设置当前城市缓存
	 * @param dictCity
	 */
	public synchronized void setCityDict(Dict dictCity) {
		cache.put(KEY_CURRENT_CITY, dictCity);
	}
	
	/**
	 * 获取Token
	 * @return
	 */
	public Token getToken() {
		return (Token) cache.get(KEY_TOKEN);
	}

	/**
	 * 设置当前Token缓存
	 * @param token
	 */
	public synchronized void setToken(Token token) {
		Log.d("shijun","KEY_TOKEN");
		cache.put(KEY_TOKEN, token);
	}
	
	/**
	 * 清除Token
	 */
	public synchronized void clearToken() {
		Log.d("shijun","KEY_TOKEN");
		cache.remove(KEY_TOKEN);
	}

	/**
	 * 设置数据字典缓存
	 * @param dictMaps
	 */
	public synchronized void setDictMaps(Map<String, List<Dict>> dictMaps) {
		cache.put(KEY_DICTMAPS, dictMaps);
	}
	
	/**
	 * 获取数据字典
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Map<String, List<Dict>> getDictMaps() {
		return (Map<String, List<Dict>>) cache.get(KEY_DICTMAPS);
	}
	
	/**
	 * 获取缓存中数据字典项
	 * @param key
	 * @return
	 */
	public List<Dict> getDicts(String key) {
		Map<String, List<Dict>> dictMaps = getDictMaps();
		if (CollectionUtils.isEmpty(dictMaps)) {
			Log.d("shijun","dictMaps");
			return null;
		}
		Log.d("shijun","dictMaps");
		return dictMaps.get(key);
	}
	
	/**
	 * 获取数据字典项名称
	 * @param key
	 * @param value
	 * @return
	 */
	public String getDictLabel(String key, String value) {
		if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
			return null;
		}
		List<Dict> dicts = getDicts(key);
		for (Dict dict : dicts) {
			if( value.equalsIgnoreCase(dict.getValue()) ) {

				return dict.getLabel();
			}
		}
		return null;
	}
	
	/**
	 * 是否已经登录
	 * @return
	 */
	public boolean isLogin() {
		Token token = getToken();
		return token != null && token.getToken() != null;
	}

	/**
	 * 注销
	 */
	public void logout() {
		clearToken();
	}
	
	/**
	 * 获取DAO对象
	 * @param activity
	 * @return
	 */
	public DbUtils getDb(Activity activity) {
		if (db == null) {
			synchronized (this) {
				if (db == null) {
					db = DbUtils.create(activity);
				}
			}
		}
		return db;
	}

	/**
	 * 获取AppContext
	 * @param activity
	 * @return
	 */
	public static AppContext get(Activity activity) {
		if (activity == null) {
			Log.d("shijun", "activity" + activity);
			return null;
		}
//		Application application = activity.getApplication();
//		Log.d("shijun", "application" +application);
//		if (application == null || !(application instanceof  AppContext)) {
//			Log.d("shijun", "application2222" + application);
//			return null;
//		}
//		Log.d("shijun", "get: " + (AppContext)application);
		return new AppContext();
	}

	private void initImageLoaderConfiguration(){
		ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(this);
		ImageLoader.getInstance().init(configuration);
	}
	
	public void initImageLoaderConfiguration(Context context) {
		mImageLoaderConfiguration = new ImageLoaderConfiguration.Builder(
				context)
				.discCache(new UnlimitedDiscCache(getImgPath()))
				// 自定义缓存路径
				//.memoryCacheExtraOptions(getWith(context), getHeight(context))
				// default = device screen dimensions
				.threadPoolSize(5)
				// default 最大线程池5
				.threadPriority(Thread.NORM_PRIORITY - 2)
				// default
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.memoryCache(new LruMemoryCache(1 * 1024 * 1024))
				// 最大内存2M
				.discCacheSize(200 * 1024 * 1024).discCacheFileCount(1000)
				// 缓存文件数目 500
				.defaultDisplayImageOptions(mDisplayImageOptions)
				.imageDecoder(new BaseImageDecoder()) // default
				.build();
		
		
		ImageLoader.getInstance().init(mImageLoaderConfiguration);
	}
	
	/**
	 * 初始化显示图片的配置参数选项
	 */
	public void initDisplayImageLoadConfig() {
		mDisplayImageOptions = new DisplayImageOptions.Builder()
				.bitmapConfig(Bitmap.Config.RGB_565).cacheOnDisc()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
	}
	public File getImgPath() {
		File files = new File(getImgStrPath());
		return files;
	}
	public String getImgStrPath(){
		return Environment.getExternalStorageDirectory()+
				"/HaiYin/ImgCache";
	}
	
	
}
