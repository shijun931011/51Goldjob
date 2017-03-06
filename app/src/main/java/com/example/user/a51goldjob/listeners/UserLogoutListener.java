package com.example.user.a51goldjob.listeners;

import android.app.Activity;
import android.view.View;

import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.view.CustomDialog;


/**
 * 用户登出监听器
 * @author yeq
 *
 */
public class UserLogoutListener implements View.OnClickListener{
	
	private Activity activity;
	
	public UserLogoutListener(Activity activity) {
		this.activity = activity;
	}
	
	public void onClick(View v) {
		final CustomDialog customDialog = DialogUtils.info(activity, activity.getResources().getString(R.string.opt_success));
		customDialog.setBtnsOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				AppContext.get(activity).logout();
				activity.finish();
				customDialog.dismiss();
			}
		});
	}
}
