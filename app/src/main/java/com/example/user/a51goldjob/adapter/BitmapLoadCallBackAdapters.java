package com.example.user.a51goldjob.adapter;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.chyjr.goldjob.fr.utils.ImageUtils;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

/**
 * 图片处理回调适配器
 * @author yeq
 *
 */
public class BitmapLoadCallBackAdapters {

	/**
	 * 圆角
	 * 
	 * @author yeq
	 *
	 */
	public static class CornnerBitmapLoadCallBack extends DefaultBitmapLoadCallBack<ImageView> {
		public void setBitmap(ImageView container, Bitmap bitmap) {
			Bitmap rst = ImageUtils.toRoundCorner(bitmap, 10);
			super.setBitmap(container, rst);
		}
	}
}
