package com.example.user.a51goldjob.mgr;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import android.app.Activity;


import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.chyjr.goldjob.fr.utils.DB;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.entities.AppInfo;


/**
 * App 应用管理
 * 
 * @author yeq
 *
 */
public class AppMgr extends BaseMgr {
	
	/**
	 * 初始化
	 * 
	 * @param activity
	 */
	public void initAppInfo(final Activity activity) {
		List<AppInfo> appInfos = DB.execute(activity, new DB.DBExecutorAdapter<List<AppInfo>>() {
			public List<AppInfo> execute() throws Exception {
				return getDb(activity).findAll(AppInfo.class);
			}
		});
		
		if (CollectionUtils.isNotEmpty(appInfos)) {
			Map<String, String> appInfoMap = new HashMap<String, String>();
			for (AppInfo appInfo : appInfos) {
				appInfoMap.put(appInfo.getName(), appInfo.getValue());
			}
			AppContext.get(activity).setAppInfoMap(appInfoMap);
		}
	}
}
