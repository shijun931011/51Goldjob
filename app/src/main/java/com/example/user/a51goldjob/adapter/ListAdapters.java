package com.example.user.a51goldjob.adapter;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.view.WebImageView;
import com.example.user.a51goldjob.bean.AppConstants;
import com.lidroid.xutils.BitmapUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * 列表适配器帮助类
 * @author yeq
 *
 */
public class ListAdapters {

	/**
	 * 用于web元素的ListAdapter，使用了 WebImageView 组件
	 * @author yeq
	 *
	 */
	private static BitmapUtils bitmapUtils;
	private static Context mycontext;
	public static class WebListAdapter extends BeanAdapter {
		
		private int time=0;
		public static WebListAdapter create(Context context, List<?> data, int resource, String[] from, int[] to) {
			mycontext=context;
			return new WebListAdapter(context, data, resource, from, to);
		}

		public WebListAdapter(Context context, List<?> data, int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

	    public void setViewImage(ImageView imageView, String value) {
	    		if (StringUtils.isNotEmpty(value) && value.startsWith(AppConstants.HTTP) ) {
		    		//((WebImageView) v).setImageUrl(value);
//		    		String tempValue=value;
//		    		ImageLoader.getInstance().displayImage(value,imageView);
	    			bitmapUtils = new BitmapUtils(mycontext, Environment.getExternalStorageDirectory().getAbsolutePath()+"HAHA");
		    		bitmapUtils.display(imageView,value);
	    			return;
		    	}
	    	
	    	
	    	super.setViewImage(imageView, value);
	    }
	}
}
