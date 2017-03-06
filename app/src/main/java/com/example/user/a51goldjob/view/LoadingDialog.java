package com.example.user.a51goldjob.view;



import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.user.a51goldjob.R;

/**
 * 界面加载框
 * @author yeq
 *
 */
public class LoadingDialog extends Dialog {
	
	private TextView tipTextView;
	
	public LoadingDialog(Context context) {
		super(context);
	}

	public LoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	protected LoadingDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		LayoutInflater inflater = LayoutInflater.from(getContext());
		View v = inflater.inflate(R.layout.loading_dialog, null); // 得到加载view
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.loading_dialog_view_layout); // 加载布局
		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.loading_dialog_img_imageview);
		tipTextView = (TextView) v.findViewById(R.id.loading_dialog_tip_textview); // 提示文字
		
		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.loading_animation); // 加载动画，使用ImageView显示动画
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);

		setCancelable(false);// 不可以用“返回键”取消
		setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
	}

	public TextView getTipTextView() {
		return tipTextView;
	}

	public void setTipTextView(TextView tipTextView) {
		this.tipTextView = tipTextView;
	}
}
