package com.example.user.a51goldjob.view;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.user.a51goldjob.R;


/**
 * 滚动轮播组件
 * @author yeq
 *
 */
public class SlideShowView extends FrameLayout {

	// 放轮播图片的ImageView 的list
	private List<? extends View> viewList;
	// 放圆点的View的list
	private List<View> dotViewsList;

	private ViewPager viewPager;
	// 当前轮播页
	private int currentItem = 0;
	// 定时任务
	private ScheduledExecutorService scheduledExecutorService;
	private MyPagerAdapter myPagerAdapter;
	private LayoutInflater layoutInflater;
	
	private int bottomItemId;
	// Handler
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			viewPager.setCurrentItem(currentItem);
		}
	};

	public SlideShowView(Context context) {
		this(context, null);
	}

	public SlideShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlideShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initUI(context);
	}
	
	public List<? extends View> getViewList() {
		return viewList;
	}
	
	public int getViewSize() {
		return getViewList().size();
	}
	
	public View getCurrentView() {
		int currentItem = viewPager.getCurrentItem();
		return viewPager.getChildAt(currentItem);
	}
	
	@SuppressWarnings("rawtypes")
	private List getViews() {
		return viewList;
	}
	
	public void setDotGravity(int gravity) {
		LinearLayout ovalLayout = (LinearLayout) findViewById(R.id.viewPager_dotLayout);
		ovalLayout.setGravity(gravity);
	}
	
	public void setDotPadding(int left, int top, int right, int bottom) {
		LinearLayout ovalLayout = (LinearLayout) findViewById(R.id.viewPager_dotLayout);
		ovalLayout.setPadding(left, top, right, bottom);
	}
	
	@SuppressWarnings({ "unchecked" })
	public void initViewList(List<? extends View> views, int bottomItemId) {
		this.bottomItemId = bottomItemId;
		getViews().addAll(views);
		initDotViews(getViews(), bottomItemId);
		getMyPagerAdapter().notifyDataSetChanged();
	}
	
	public void initDotViews(List<? extends View> views, int bottomItemId) {
		LinearLayout ovalLayout = (LinearLayout) findViewById(R.id.viewPager_dotLayout);
		ovalLayout.removeAllViews();
		View dotView = null;
		for (int i=0; i< views.size(); i++) {
			dotView = layoutInflater.inflate(bottomItemId, null);
			if (i == 0) {
				((View) (dotView.findViewById(R.id.ad_item_v))).setBackgroundResource(R.drawable.dot_focused);
			}
			dotViewsList.add(dotView);
			ovalLayout.addView(dotView);
		}
		ovalLayout.refreshDrawableState();
	}
	
	public void removeCurrent() {
		removeCustomView(getCurrentView());
	}
	
	public void removeCustomView(View view) {
		if (view == null) {
			return;
		}
		getViews().remove(view);
		initDotViews(getViews(), bottomItemId);
		getMyPagerAdapter().notifyDataSetChanged();
	}
	
	public MyPagerAdapter getMyPagerAdapter() {
		return myPagerAdapter;
	}
	
	/**
	 * 初始化Views等UI
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initUI(Context context) {
		viewList = new ArrayList();
		dotViewsList = new ArrayList<View>();
		myPagerAdapter = new MyPagerAdapter(context, viewList);
		layoutInflater = LayoutInflater.from(context);
		layoutInflater.inflate(R.layout.layout_slideshow, this, true);

		viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setFocusable(true);
		viewPager.setAdapter(myPagerAdapter);
		viewPager.setOnPageChangeListener(new MyPageChangeListener());
	}

	/**
	 * 开始轮播图切换
	 */
	public void startPlay() {
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		scheduledExecutorService.scheduleAtFixedRate(new SlideShowTask(), 1, 4,
				TimeUnit.SECONDS);
	}

	/**
	 * 停止轮播图切换
	 */
	public void stopPlay() {
		scheduledExecutorService.shutdown();
	}

	/**
	 * 填充ViewPager的页面适配器
	 * 
	 * @author caizhiming
	 */
	public static class MyPagerAdapter extends PagerAdapter {
		
		private List<?> viewList;
		
		public MyPagerAdapter(Context context, List<?> viewList ) {
			this.viewList = viewList;
		}
		
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView((View)object);
		}

		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView((View)viewList.get(position));
			return viewList.get(position);
		}

		public int getCount() {
			return viewList.size();
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
		public Parcelable saveState() {
			return null;
		}
		
		public void restoreState(Parcelable arg0, ClassLoader arg1) { }
		public void startUpdate(View arg0) { }
		public void finishUpdate(View arg0) { }
	}

	/**
	 * ViewPager的监听器 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author caizhiming
	 */
	private class MyPageChangeListener implements OnPageChangeListener {

		boolean isAutoPlay = false;

		@Override
		public void onPageScrollStateChanged(int arg0) {
			switch (arg0) {
			case 1:// 手势滑动，空闲中
				isAutoPlay = false;
				break;
			case 2:// 界面切换中
				isAutoPlay = true;
				break;
			case 0:// 滑动结束，即切换完毕或者加载完毕
					// 当前为最后一张，此时从右向左滑，则切换到第一张
				if (viewPager.getCurrentItem() == viewPager.getAdapter()
						.getCount() - 1 && !isAutoPlay) {
					viewPager.setCurrentItem(0);
				}
				// 当前为第一张，此时从左向右滑，则切换到最后一张
				else if (viewPager.getCurrentItem() == 0 && !isAutoPlay) {
					viewPager
							.setCurrentItem(viewPager.getAdapter().getCount() - 1);
				}
				break;
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int pos) {
			currentItem = pos;
			for (int i = 0; i < dotViewsList.size(); i++) {
				if (i == pos) {
					((View) (dotViewsList.get(pos).findViewById(R.id.ad_item_v))).setBackgroundResource(R.drawable.dot_focused);
				} else {
					((View) (dotViewsList.get(i).findViewById(R.id.ad_item_v))).setBackgroundResource(R.drawable.dot_normal);
				}
			}
		}

	}

	/**
	 * 执行轮播图切换任务
	 * 
	 * @author caizhiming
	 */
	private class SlideShowTask implements Runnable {

		@Override
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % viewList.size();
				handler.obtainMessage().sendToTarget();
			}
		}

	}

	/**
	 * 销毁ImageView资源，回收内存
	 * 
	 * @author caizhiming
	 
	private void destoryBitmaps() {

		for (int i = 0; i < imageViewsList.size(); i++) {
			ImageView imageView = imageViewsList.get(i);
			Drawable drawable = imageView.getDrawable();
			if (drawable != null) {
				// 解除drawable对view的引用
				drawable.setCallback(null);
			}
		}
	}*/

}
