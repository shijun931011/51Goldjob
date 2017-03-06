package com.example.user.a51goldjob.activity;

import android.os.Bundle;

import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.view.DialogViews;
import com.example.user.a51goldjob.view.PublicTopBar;


/**
 * Activity 基类扩展
 * @author yeq
 *
 */
public abstract class BaseActivity extends com.chyjr.goldjob.fr.activity.BaseActivity {

	/** 顶部工具栏 */
	protected PublicTopBar topBar;
	/** 界面加载帮助类 */
	protected DialogViews.LoadingDialogsHelper loadingDialogsHelper;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        loadingDialogsHelper = DialogViews.LoadingDialogsHelper.create(this);
        topBar = PublicTopBar.create(this);
	}
	
	public DialogViews.LoadingDialogsHelper getLoadingDialogsHelper() {
		return loadingDialogsHelper;
	}
	
	public PublicTopBar getTopBar() {
		return topBar;
	}
	
	/**
	 * 设置工具栏标题
	 * @param id
	 */
	protected void setBarTitle(int id) {
		getTopBar().setTitle(id);
	}
	
	/**
	 * 设置工具栏标题
	 * @param
	 */
	protected void setBarTitle(String title) {
		getTopBar().setTitle(title);
	}
	
	protected void onDestroy() {
		super.onDestroy();
		getLoadingDialogsHelper().dismissAll();
	}
	
	protected void onRestart() {
		super.onRestart();
		getLoadingDialogsHelper().dismissAll();
	}

	/**
	 * 获取用户Token
	 * @param
	 */
	public Token getToken() {
		AppContext ctx = AppContext.get(this);
		if (!ctx.isLogin()) {
			ActivityUtils.showMessage(this, true, R.string.nologin_error);
			return null;
		}
		return ctx.getToken();
	}
}
