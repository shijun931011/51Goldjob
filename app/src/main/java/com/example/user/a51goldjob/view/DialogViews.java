package com.example.user.a51goldjob.view;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.chyjr.goldjob.fr.utils.Nets;
import com.example.user.a51goldjob.R;

/**
 * Dialog帮助类
 * @author yeq
 *
 */
public class DialogViews {
	
	public static class LoadingDialogsHelper {
		
		private LoadingDialogHelper dialog;
		private int size = 0;
		private Activity activity;
		
		public static LoadingDialogsHelper create(Activity activity) {
			return new LoadingDialogsHelper(activity);
		}
		
		public LoadingDialogsHelper(Activity activity) {
			this.activity = activity;
		}
		
		public synchronized void show(String message) {
			if (dialog == null) {
				dialog = LoadingDialogHelper.create(activity, message);
			} else {
				if (size == 0) {
					dialog.dismiss();
					dialog = LoadingDialogHelper.create(activity, message);
				}
			}
			size = size + 1;
			if (dialog.isShowing()) {
				return;
			}
			dialog.show();
		}
		
		public synchronized void dismissAll() {
			size = 0;
			dismiss();
		}
		
		public synchronized void dismiss() {
			if (dialog == null) {
				return;
			}
			size = size - 1;
			if (size > 0) {
				return;
			}
			if (size <= 0) {
				size = 0;
			}
			if (dialog.isShowing()) {
				dialog.dismiss();
			}
		}
	}

	public static class LoadingDialogHelper {

		private String message;
		private Activity activity;
		private Dialog mLoadingDialog;
		private Handler handler = new Handler(Looper.getMainLooper());
		
		public LoadingDialogHelper(Activity activity) {
			this(activity, null);
		}
		
		public LoadingDialogHelper(Activity activity, String message) {
			this.activity = activity;
			this.message = message;
			if (Nets.isNetworkAvailable(activity)) {
				mLoadingDialog = DialogViews.createLoadingDialog(activity, message);
			}
		}

		public static LoadingDialogHelper create(Activity activity) {
			return new LoadingDialogHelper(activity);
		}
		
		public static LoadingDialogHelper create(Activity activity, String message) {
			return new LoadingDialogHelper(activity, message);
		}

		public void show() {
			show(message);
		}
		
		public void show(String message) {
			if (Nets.isNetworkAvailable(activity)) {
//				mLoadingDialog = DialogViews.createLoadingDialog(activity, message);
				mLoadingDialog.show();
			}
		}
		
		public boolean isShowing() {
			return mLoadingDialog.isShowing();
		}
		
		public void dismiss(long time) {
			if (!(com.chyjr.goldjob.fr.utils.ActivityUtils.isActivityAvailable(activity))) {
				return;
			}
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					dismiss();
				}
			}, time);
		}
		
		public void dismiss() {
			if (!(com.chyjr.goldjob.fr.utils.ActivityUtils.isActivityAvailable(activity))) {
				return;
			}
			handler.post(new Runnable() {
				@Override
				public void run() {
					if (mLoadingDialog != null) {
						mLoadingDialog.dismiss();
					}
				}
			});
		}
	}
	
	/**
	 * 创建自定义LoadingDialog
	 * 
	 * @param context
	 * @param msg
	 * @return
	 */
	public static Dialog createLoadingDialog(Context context, String msg) {

		LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.loading_dialog, null); // 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_dialog_view_layout); // 加载布局
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_dialog_img_imageview);
		TextView tipTextView = (TextView) v.findViewById(R.id.loading_dialog_tip_textview); // 提示文字
		
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context, R.anim.loading_animation); // 加载动画，使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);

		Dialog loadingDialog = new Dialog(context, R.style.loading_dialog);

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}
}
