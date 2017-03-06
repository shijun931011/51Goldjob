package com.example.user.a51goldjob.view;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.chyjr.goldjob.fr.utils.BindUtils;
import com.chyjr.goldjob.fr.utils.StringUtils;
import com.example.user.a51goldjob.R;

/**
 * 顶部工具栏封装
 * @author yeq
 *
 */
public class PublicTopBar {

	private ImageView returnBtn;
	private TextView titleTxt;
	private LinearLayout customLayout;
	private int in = R.anim.abc_fade_in;
	private int out = R.anim.abc_fade_out;
	
	private Activity activity;
	
	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			onReturn();
		}
	};
	
	public static PublicTopBar create(Activity activity) {
		return new PublicTopBar(activity);
	}

	public PublicTopBar(Activity activity) {
		this(activity, null);
	}

	public PublicTopBar(final Activity activity, int id) {
		this(activity, activity.getResources().getString(id));
	}
	
	public PublicTopBar(final Activity activity, String title) {
		this.activity = activity;
		customLayout = (LinearLayout) activity.findViewById(R.id.topbar_active_icon_layout);
		titleTxt = (TextView) activity.findViewById(R.id.title_middle_txt);
		returnBtn = (ImageView) activity.findViewById(R.id.topbar_return_btn);

		titleTxt.setText(activity.getTitle());
		RelativeLayout titleLeftLayout = (RelativeLayout) activity.findViewById(R.id.title_left);
		
		BindUtils.bindCtrlOnClickListener(onClickListener, titleLeftLayout, returnBtn);
		if (StringUtils.isNotEmpty(title)) {
			setTitle(title);
		}
	}
	
	public LinearLayout getCustomLayout() {
		return customLayout;
	}
	
	protected void onReturn() {
		activity.finish();
		activity.overridePendingTransition(in, out);
	}
	
	public void setIn(int in) {
		this.in = in;
	}
	
	public void setOut(int out) {
		this.out = out;
	}

	public TextView getTitleTxt() {
		return titleTxt;
	}
	
	public ImageView getReturnBtn() {
		return returnBtn;
	}
	
	public void setTitle(int id) {
		getTitleTxt().setText(getString(id));
	}
	
	public void setTitle(String title) {
		getTitleTxt().setText(title);
	}

	protected String getString(int id) {
		return activity.getResources().getString(id);
	}
	
	public void setCustomView(View view,int position) {
		if (view == null) {
			return;
		}
		int count = customLayout.getChildCount();
		
		if (count > 0) {
			return;
		}
		if(position==1000){
			customLayout.addView(view);
		}
	}
	
	public void setCustomViews(View view,int position) {
		if (view == null) {
			return;
		}
		int count = customLayout.getChildCount();
		if(position==1000){
			customLayout.addView(view);
		}
	}
	public void setCustomView(View view, OnClickListener onClickListener,int position) {
		setCustomView(view,position);
		if (view != null) {
			view.setOnClickListener(onClickListener);
		}
	}
	
	public View getCustomView() {
		return customLayout.getChildCount() <= 0 ? null : customLayout.getChildAt(0);
	}
}
