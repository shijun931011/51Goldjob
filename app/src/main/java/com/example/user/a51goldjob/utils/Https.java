package com.example.user.a51goldjob.utils;

import java.io.UnsupportedEncodingException;



import android.content.Context;
import org.apache.http.entity.StringEntity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

/**
 * Http 工具类
 * @author yeq
 *
 */
public class Https {
	
	public static final String ENCODING = "UTF-8";

	/**
	 * 提交POST请求
	 * @param context
	 * @param urlKey urkKey从assets/strings_url_prod.xml 中获取
	 * @param params
	 * @param callBack 异步结果回调
	 * @return
	 */
	public static <T> HttpHandler<T> post(Context context, String urlKey, RequestParams params, RequestCallBack<T> callBack) {
		HttpUtils http = createHttpUtils();
		String url = UrlUtils.getCustomUrl(context, urlKey);
		params.getEntity();
		return http.send(HttpMethod.POST, url, params, callBack);
	}
	
	public static <T> HttpHandler<T> post(String url, RequestParams params, RequestCallBack<T> callBack) {
		HttpUtils http = createHttpUtils();
		return http.send(HttpMethod.POST, url, params, callBack);
	}
                                                                            //C0000
	public static <T> HttpHandler<T> post(Context context, String urlKey, String data, RequestCallBack<T> callBack) {
	//	data="vuXVOjXXEAEziGD3YsKV+55vhiXKuztV2NRnUPdEZ9U=C0000j0000000a";
		RequestParams params = createParams();
		setBodyEntity(params, data);
		return post(context, urlKey, params, callBack);
	}
	
	public static <T> HttpHandler<T> post(String url, String data, RequestCallBack<T> callBack) {
		RequestParams params = createParams();
		setBodyEntity(params, data);
		return post(url, params, callBack);
	}
	
	public static void setBodyEntity(RequestParams params, String data) {
		try {
			params.setBodyEntity(new StringEntity(data, ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static HttpUtils createHttpUtils() {
		return new HttpUtils();
	}
	
	public static RequestParams createParams() {
		return new RequestParams(ENCODING);
	}
}
