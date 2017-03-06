package com.example.user.a51goldjob.adapter;

import android.app.Activity;


import com.chyjr.goldjob.fr.utils.DB.IDbExecutor;
import com.example.user.a51goldjob.utils.ActivityUtils;

/**
 * 本地数据库操作适配器
 * @author yeq
 *
 * @param <T>
 */
public  abstract class InfoDBExecutorAdapter<T> implements IDbExecutor<T> {
	public void doException(Activity activity, Exception e) {
		ActivityUtils.showMessage(activity, false, e.getMessage());
	}
}
