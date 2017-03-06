package com.example.user.a51goldjob.utils;

import java.util.Map;
import android.content.Context;
import com.chyjr.goldjob.fr.utils.StringUtils;
import com.chyjr.goldjob.fr.utils.UrlConfigParsers;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.bean.AppConstants;

/**
 * API Url 工具类
 * @author yeq
 *
 */
public class UrlUtils {

	private static Map<String, String> map = null;

	public static String getCustomUrl(Context context, String key) {
		String url = getUrlFromMap(context, key);
		if (url.startsWith(AppConstants.HTTP)) {
			return url;
		}
		String host = getUrlFromMap(context, "base");
		return StringUtils.isEmpty(host) ? url : String.format("%s%s", host, url);
	}

	public static String getUrlFromMap(Context context, String key) {
		if (null == map) {
			synchronized (UrlUtils.class) {
				if (null == map) {
					map = UrlConfigParsers.generateMap(getXmlFilename(context), context);	
				}
			}
		}
		return map.get(key);
	}

	private static String getXmlFilename(Context context) {
		if ("prod".equalsIgnoreCase(AppConstants.ENVIRONMENT)) {
			return "strings_url_prod.xml";
		} else if ("test".equalsIgnoreCase(AppConstants.ENVIRONMENT)) {
			return "strings_url_test.xml";
		}
		throw new IllegalArgumentException(context.getString(R.string.application_config_error));
	}
}
