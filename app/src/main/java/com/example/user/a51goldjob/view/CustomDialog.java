package com.example.user.a51goldjob.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.a51goldjob.R;


/**
 * 自定义Dialog
 * @author yeq
 *
 */
public class CustomDialog extends Dialog {

	private int defaultWidth = 278; // 默认宽度
	private int defaultHeight = 170;// 默认高度
	private View messageTxt;
	private View titleTxt;
	private View closeIcon;
	private LinearLayout btnsLayout;

	private View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			CustomDialog.this.dismiss();
		}
	};
	
	public CustomDialog(Context context, int layout, int style) {
		super(context, style);
		setContentView(layout);
		btnsLayout = (LinearLayout) findViewById(R.id.dialog_custom_btn_layout);
		messageTxt = findViewById(R.id.dialog_custom_message_txt);
		closeIcon = findViewById(R.id.dialog_custom_close_imageview);
		try {
			titleTxt = findViewById(R.id.dialog_title_txt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		float density = getDensity(context);
		params.width = (int) (defaultWidth * density);
		params.height = (int) (defaultHeight * density);
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
		
		closeIcon.setOnClickListener(onClickListener);
	}
	
	public View getBtn(int viewId) {
		return findViewById(viewId);
	}
	
	private float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return dm.density;
	}

	/** 设置Dialog宽度 */
	public void setWidth(int width) {
		defaultWidth = width;
	};

	/**
	 * 设置按钮的点击事件
	 * 
	 * @param listener 点击时间监听器
	 */
	public void setBtnsOnClickListener(View.OnClickListener listener) {
		if (btnsLayout == null) {
			return;
		}
		
		int childCount = btnsLayout.getChildCount();
		
		View view = null;
		for (int i=0; i<childCount; i++) {
			view = btnsLayout.getChildAt(i);
			if (view == null) {
				continue;
			}
			if (view instanceof Button) {
				((Button)view).setOnClickListener(listener);
			}
		}
	}

	/**
	 * 设置提示信息
	 * 
	 * @param title 按钮标题
	 */
	public void setMessage(String message) {
		((TextView) messageTxt).setText(message);
	}

	public void setTitle(String title) {
		((TextView) titleTxt).setText(title);
	}

}
