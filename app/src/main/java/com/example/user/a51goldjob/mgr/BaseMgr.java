package com.example.user.a51goldjob.mgr;

import android.app.Activity;


import com.chyjr.goldjob.fr.mgr.Mgr.IMgr;
import com.example.user.a51goldjob.AppContext;
import com.lidroid.xutils.DbUtils;

/**
 * Mgr基类
 * @author yeq
 *
 */
public class BaseMgr implements IMgr {

	/**
	 * 获取本地数据库操作工具类
	 * @param activity
	 * @return
	 */
	protected DbUtils getDb(Activity activity) {
		return getAppContext(activity).getDb(activity);
	}
	
	/**
	 * 获取AppContext
	 * @param activity
	 * @return
	 */
	protected AppContext getAppContext(Activity activity) {
		return AppContext.get(activity);
	}
}
