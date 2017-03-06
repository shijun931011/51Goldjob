package com.example.user.a51goldjob.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.view.CustomDialog;


/**
 * ActivityUtils 工具类
 * @author yeq
 *
 */
public class ActivityUtils extends com.chyjr.goldjob.fr.utils.ActivityUtils {
	
	/**
	 * 启动一个Activity，默认打开样式为fade
	 * 
	 * @param handler
	 * @param activity
	 * @param targetClass
	 */
	public static void startActivity(Handler handler, final Activity activity, final Class<?> targetClass) {
		startActivity(handler, activity, targetClass, null);
	}
	
	public static  String tempStr="";
	public static void startActivitys(Handler handler, final Activity activity, final Class<?> targetClass,String str) {
		tempStr=str;
		startActivitys(handler, activity, targetClass,new IIntentHandler() {

			@Override
			public void putExtra(Intent intent) {
				intent.putExtra("selectStr",tempStr);
				
			}
			
			
		},str);
	}
	
	/**
	 * 启动一个Activity，默认打开样式为fade
	 * 
	 * @param handler
	 * @param activity
	 * @param targetClass
	 * @param intentHandler
	 */
	public static void startActivity(Handler handler, final Activity activity, final Class<?> targetClass, final IIntentHandler intentHandler) {
		startActivity(handler, activity, targetClass, R.anim.abc_fade_in, R.anim.abc_fade_out, intentHandler);
	}
	
	public static void startActivitys(Handler handler, final Activity activity, final Class<?> targetClass, final IIntentHandler intentHandler,String str) {
		startActivity(handler, activity, targetClass, R.anim.abc_fade_in, R.anim.abc_fade_out, intentHandler);
	}

	/**
	 * 启动一个Activity
	 * 
	 * @param handler
	 * @param activity
	 * @param targetClass
	 * @param in
	 * @param out
	 * @param intentHandler
	 */
	public static void startActivity(Handler handler, final Activity activity, final Class<?> targetClass
			, int in, int out, IIntentHandler intentHandler) {
		startActivity(handler, activity, targetClass, false, in, out, intentHandler);
	}
	
	/**
	 * 启动一个带返回的Activity，默认打开样式为fade
	 * 
	 * @param handler
	 * @param activity
	 * @param targetClass
	 */
	public static void startActivityForResult(Handler handler, final Activity activity, final Class<?> targetClass) {
		startActivityForResult(handler, activity, targetClass, null);
	}
	
	/**
	 * 启动一个带返回的Activity，默认打开样式为fade
	 * 
	 * @param handler
	 * @param activity
	 * @param targetClass
	 * @param intentHandler
	 */
	public static void startActivityForResult(Handler handler, final Activity activity, final Class<?> targetClass, final IIntentHandler intentHandler) {
		startActivity(handler, activity, targetClass, true, R.anim.abc_fade_in, R.anim.abc_fade_out, intentHandler);
	}
	
	/**
	 * 返回上一个Activity
	 * 
	 * @param handler
	 * @param activity
	 * @param intentHandler
	 */
	public static void returnActivity(Handler handler, Activity activity, IIntentHandler intentHandler) {
		returnActivity(handler, activity, R.anim.abc_fade_in, R.anim.abc_fade_out, intentHandler);
	}

	/**
	 * 显示消息对话框
	 * @param activity
	 * @param finish
	 * @param messageId
	 */
	public static void showMessage(Activity activity, boolean finish, int messageId) {
		showMessage(activity, finish, activity.getString(messageId));
	}

	/**
	 * 显示消息对话框
	 * 
	 * @param activity
	 * @param finish
	 * @param message
	 */
	public static void showMessage(final Activity activity, final boolean finish, final String message) {
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if(!activity.isFinishing()) {
					if (finish) {
						final CustomDialog customDialog = DialogUtils.info(activity, message);
						customDialog.setBtnsOnClickListener(new DialogUtils.DialogOnClickFinishListenerAdapter(customDialog, activity));
					} else {
						DialogUtils.commonInfo(activity, message);
					}
				}
			}
		});
	}
	
	/**
	 * 注销确认对话框
	 * 
	 * @param activity
	 * @param finish
	 * @param message
	 */
	public static void confirmMessage(final Activity activity, final boolean finish, final String message) {
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if(!activity.isFinishing()) {
					if (finish) {
						final CustomDialog customDialog = DialogUtils.confirm(activity, message);
						Button confirmBtn = (Button) customDialog.findViewById(R.id.dialog_custom_confirm_btn);
						confirmBtn.setText("确定");
						Button cancelBtn = (Button) customDialog.findViewById(R.id.dialog_custom_cancel_btn);
						confirmBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								AppContext.get(activity).logout(); // 注销
								customDialog.dismiss();
								//DialogUtils.commonInfo(activity, activity.getString(R.string.logout_success));
								Toast.makeText(activity,"已退出登录!",Toast.LENGTH_SHORT).show();
								activity.findViewById(R.id.home_title_logout_img).setVisibility(View.GONE);
								activity.findViewById(R.id.home_title_login_img).setVisibility(View.VISIBLE);
							}
						});
						cancelBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								customDialog.dismiss();
							}
						});
					} else {
						DialogUtils.commonInfo(activity, message);
					}
				}
			}
		});
	}
}
