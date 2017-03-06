package com.example.user.a51goldjob.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.example.user.a51goldjob.view.PublicViews;


/**
 * 表单界面基类
 * @author yeq
 *
 */
public abstract class BaseFormActivity extends BaseActivity implements OnClickListener{
	private static TextView txtView;
	private static final TextView txtViews=txtView;
	
	/** 表单按钮 */
	protected TextView submitTxt; 
	
	/** 点击监听 */
	protected OnClickListener submitListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_submitOnClick(v);	
		}
	};

	private RelativeLayout relativeLayout;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        submitTxt = PublicViews.createMenuTextView(this, getSubmitTextLang(), getSubmitTxtColor(), submitListener); // 创建菜单按钮
        relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
	}
	
	/**
	 * 按钮文本颜色
	 * 
	 * @return
	 */
	protected int getSubmitTxtColor() {
		return getResources().getColor(getSubmitTxtColorId());
	}

	/**
	 * 按钮文本颜色
	 * 
	 * @return
	 */
	protected int getSubmitTxtColorId() {
		return R.color.text_white;
	}
	
	/**
	 * 按钮文本
	 * @return
	 */
	protected String getSubmitTextLang() {
		return getResources().getString(getSubmitTextStringId());
	}

	/**
	 * 按钮文本
	 * @return
	 */
	protected int getSubmitTextStringId() {
		return R.string.submit;
	}

	/**
	 * 点击动作实现方法
	 * @return
	 */
	protected void actionPerformed_submitOnClick(View v) {
	}
	
	public void confirmMessage(final Activity activity, final boolean finish, final String message) {
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if(!activity.isFinishing()) {
					if (finish) {
						final CustomDialog customDialog = DialogUtils.confirm(activity, message);
						Button confirmBtn = (Button) customDialog.findViewById(R.id.dialog_custom_confirm_btn);
						confirmBtn.setText("返回");
						Button cancelBtn = (Button) customDialog.findViewById(R.id.dialog_custom_cancel_btn);
						cancelBtn.setText("保存");
						confirmBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								activity.finish();
							}
						});
						cancelBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								actionPerformed_submitOnClick(txtViews);
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
