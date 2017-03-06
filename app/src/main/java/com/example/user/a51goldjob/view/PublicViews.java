package com.example.user.a51goldjob.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


import com.chyjr.goldjob.fr.utils.BindUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.activity.BaseActivity;

/**
 * 公共View帮助类
 * @author yeq
 *
 */
public class PublicViews {
	
	/**
	 * 创建菜单文本按钮
	 * 
	 * @param activity
	 * @param onClickListener
	 * @return
	 */
	public static TextView createMenuTextView(BaseActivity activity, String text, int color, View.OnClickListener onClickListener) {
		TextView txt = new TextView(activity);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        txt.setLayoutParams(params);
        txt.setText(text);
        txt.setTextSize(18);
        txt.setTextColor(color);
        activity.getTopBar().setCustomView(txt,1000);
        BindUtils.bindCtrlOnClickListener(onClickListener, txt, activity.getTopBar().getCustomLayout());
        return txt;
	}
	
	/**
	 * 创建菜单图片按钮：收藏
	 * 
	 * @param activity
	 * @param onClickListener
	 * @return
	 */
	public static ImageView createMenuFavoriteImageView(BaseActivity activity, View.OnClickListener onClickListener) {
		ImageView icon_favorite_selector = new ImageView(activity);
		icon_favorite_selector.setTag("icon_favorite_selector");
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        icon_favorite_selector.setLayoutParams(params);
        icon_favorite_selector.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_favorite_selector));
        icon_favorite_selector.setImageDrawable(activity.getResources().getDrawable(R.drawable.icon_favorite_in));
        activity.getTopBar().setCustomView(icon_favorite_selector,1000);
        icon_favorite_selector.setOnClickListener(onClickListener);
     //   BindUtils.bindCtrlOnClickListener(onClickListener, icon_favorite_selector, activity.getTopBar().getCustomLayout());
        return icon_favorite_selector;
	}
}
