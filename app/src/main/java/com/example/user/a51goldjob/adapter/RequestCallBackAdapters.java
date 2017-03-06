package com.example.user.a51goldjob.adapter;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.util.Log;

import com.chyjr.goldjob.fr.utils.Throws;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.activity.BaseActivity;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * API访问回调帮助类
 * @author yeq
 *
 */
public class RequestCallBackAdapters {

	public static class RequestCallBackAdapter<T> extends RequestCallBack<T> {
		public void onFailure(HttpException exception, String result) {
		}

		public void onSuccess(ResponseInfo<T> response) {
			try {
				_onSuccess(response);
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("RCA", Throws.getRootMessage(e));
			}
		}
		
		protected void _onSuccess(ResponseInfo<T> response) throws Exception {
			
		}
	}

	public static class InfoRequestCallBackAdapter<T> extends RequestCallBackAdapter<T> {
		private Activity activity;
		private String failMsg;
		private boolean finish;

		public InfoRequestCallBackAdapter(Activity activity) {
			this(activity, false);
		}

		public InfoRequestCallBackAdapter(Activity activity, boolean finish) {
			this(activity, null, finish);
		}

		public InfoRequestCallBackAdapter(Activity activity, String failMsg) {
			this(activity, failMsg, false);
		}

		public InfoRequestCallBackAdapter(Activity activity, int failMsg) {
			this(activity, ActivityUtils.getString(activity, failMsg));
		}

		public InfoRequestCallBackAdapter(Activity activity, int failMsg, boolean finish) {
			this(activity, ActivityUtils.getString(activity, failMsg), finish);
		}

		public InfoRequestCallBackAdapter(Activity activity, String failMsg, boolean finish) {
			this.activity = activity;
			this.failMsg = failMsg;
			this.finish = finish;
		}
		
		public void onStart() {
			showLoading();
		}
		
		public void onFailure(HttpException exception, String result) {
			if (activity == null) {
				return;
			}
			dismissLoading();
			doMessage(getFailMsg());
			reset();
		}
		
		protected void showLoading() {
			if (activity instanceof BaseActivity) {
				((BaseActivity)activity).getLoadingDialogsHelper().show(activity.getString(R.string.load_ing));
			}
		}

		protected void dismissLoading() {
			if (activity instanceof BaseActivity) {
				((BaseActivity)activity).getLoadingDialogsHelper().dismiss();
			}
		}
		
		protected void reset() {
			if (activity instanceof BaseActivity) {
				((BaseActivity)activity).reset();
			}
		}
		
		protected void doMessage(final String message) {
			ActivityUtils.showMessage(activity, finish, message);
		}
		
		protected String getFailMsg() {
			if (StringUtils.isEmpty(failMsg)) {
				failMsg = ActivityUtils.getString(activity, R.string.network_error);
			}
			return failMsg;
		}
		
		public void onSuccess(ResponseInfo<T> response) {
			try {
				_onSuccess(response);
				dismissLoading();
			} catch (Exception e) {
				dismissLoading();
				e.printStackTrace();
				String message = Throws.getRootMessage(e);
				if (StringUtils.isNotEmpty(message)) {
					Log.i("RCA", message);
					doMessage(message);
				}
				reset();
			}
		}
		
		protected void _onSuccess(ResponseInfo<T> response) throws Exception {
			
		}
	}
}
