package com.example.user.a51goldjob.utils;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.view.CustomDialog;


/**
 * Dialog 工具类
 * 
 * @author yeq
 * 
 */
public class DialogUtils {
	
	public static final String YES = "yes";
	public static final String CANCEL = "cancel";
	
	/**
	 * 创建Dialog
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static CustomDialog create(Activity activity, int style) {
		return new CustomDialog(activity, style, R.style.dialog);
	}

	/**
	 * 显示dialog
	 * 
	 * @param
	 * @param style
	 * @param message
	 * @return
	 */
	public static CustomDialog show(Activity activity, int style, String message) {
		CustomDialog dialog = new CustomDialog(activity, style, R.style.dialog);
		dialog.setMessage(message);
		dialog.show();
		return dialog;
	}

	/**
	 * 显示Info窗口
	 * 
	 * @param
	 * @param
	 * @return
	 */
	public static CustomDialog info(Activity activity, String message) {
		return show(activity, R.layout.dialog_custom, message);
	}
	
	/**
	 * 显示confirm窗口
	 * 
	 * @param activity
	 * @param message
	 * @return
	 */
	public static CustomDialog confirm(Activity activity, String message) {
		return show(activity, R.layout.dialog_custom_confirm, message);
	}

	/**
	 * 显示commonInfo窗口，默认点击ConfirmBtn关闭
	 * 
	 * @param
	 * @param message
	 * @return
	 */
	public static CustomDialog commonInfo(Activity activity, String message) {
		final CustomDialog dialog = show(activity, R.layout.dialog_custom,
				message);
		dialog.setBtnsOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		return dialog;
	}
	
	public static class DialogOnClickDismissListenerAdapter implements OnClickListener {
		protected Dialog dialog;

		public DialogOnClickDismissListenerAdapter(Dialog dialog) {
			this.dialog = dialog;
		}

		public void onClick(View v) {
			if (dialog != null) {
				dialog.dismiss();
			}
		}
	}

	public static class DialogOnClickFinishListenerAdapter extends DialogOnClickDismissListenerAdapter {
		protected Activity activity;

		public DialogOnClickFinishListenerAdapter(Dialog dialog, Activity activity) {
			super(dialog);
			this.activity = activity;
		}

		public void onClick(View v) {
			super.onClick(v);
			if (activity != null) {
				activity.finish();
			}
		}
	}
	
}
