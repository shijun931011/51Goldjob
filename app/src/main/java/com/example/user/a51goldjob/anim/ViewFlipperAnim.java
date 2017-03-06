package com.example.user.a51goldjob.anim;

import android.app.Activity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;


import com.chyjr.goldjob.fr.adapter.OnGestureListenerAdapter;
import com.example.user.a51goldjob.R;

/**
 * 手势滑动动画
 * @author yeq
 *
 */
public class ViewFlipperAnim {

	private int minDistance = 100;
	private int minVelocity = 200;
	private boolean loop = true;
	
	private Activity activity;
	private ViewFlipper viewFlipper;
	private GestureDetector gestureDetector;
	
	public static ViewFlipperAnim create(Activity activity, int id) {
		ViewFlipperAnim anim = new ViewFlipperAnim(activity, id);
		anim.init();
		return anim;
	}
	
	public static ViewFlipperAnim create(Activity activity, ViewFlipper viewFlipper) {
		ViewFlipperAnim anim = new ViewFlipperAnim(activity, viewFlipper);
		anim.init();
		return anim;
	}

	public ViewFlipperAnim(Activity activity, int id) {
		this(activity, (ViewFlipper) activity.findViewById(id));
	}

	public ViewFlipperAnim(Activity activity, ViewFlipper viewFlipper) {
		this.activity = activity;
		this.viewFlipper = viewFlipper;
	}

	public void init() {
		if (gestureDetector != null) {
			return;
		}
		
		gestureDetector = new GestureDetector(activity.getApplicationContext(),
				new OnGestureListenerAdapter() {
					public boolean onFling(MotionEvent e1, MotionEvent e2,
							float velocityX, float velocityY) {
						return actionPerformed_onFling(e1, e2, velocityX,
								velocityY);
					}
				});

		viewFlipper.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				return gestureDetector.onTouchEvent(event);
			}
		});

		viewFlipper.setLongClickable(true);
	}

	protected boolean actionPerformed_onFling(MotionEvent e1, MotionEvent e2,
			float velocityX, float velocityY) {
		int minDistance = getMinDistance();
		int minVelcity = getMinVelocity();
		if (e1.getX() - e2.getX() > minDistance && Math.abs(velocityX) > minVelcity) {
			return showNext();
		} else if (e2.getX() - e1.getX() > minDistance && Math.abs(velocityX) > minVelcity) {
			return showPrevious();
		}
		return true;
	}
	
	public boolean showPrevious() {
		if (!isLoop() && getFirst().equals(getCurrent())) {
			return false;
		}
		viewFlipper.setInAnimation((AnimationUtils.loadAnimation(activity, R.anim.push_right_in)));
		viewFlipper.setOutAnimation((AnimationUtils.loadAnimation(activity, R.anim.push_right_out)));
		viewFlipper.showPrevious();
		return true;
	}
	
	public boolean showNext() {
		if (!isLoop() && getLast().equals(getCurrent())) {
			return false;
		}
		viewFlipper.setInAnimation((AnimationUtils.loadAnimation(activity, R.anim.push_left_in)));
		viewFlipper.setOutAnimation((AnimationUtils.loadAnimation(activity, R.anim.push_left_out)));
		viewFlipper.showNext();
		return true;
	}

	public View getCurrent() {
		return viewFlipper.getCurrentView();
	}
	
	public View getFirst() {
		return viewFlipper.getChildAt(0);
	}
	
	public View getLast() {
		return viewFlipper.getChildAt(viewFlipper.getChildCount() - 1);
	}

	public int getMinDistance() {
		return minDistance;
	}

	public void setMinDistance(int minDistance) {
		this.minDistance = minDistance;
	}

	public int getMinVelocity() {
		return minVelocity;
	}

	public void setMinVelocity(int minVelocity) {
		this.minVelocity = minVelocity;
	}

	public Activity getActivity() {
		return activity;
	}

	public ViewFlipper getViewFlipper() {
		return viewFlipper;
	}

	public void setLoop(boolean loop) {
		this.loop = loop;
	}

	public boolean isLoop() {
		return loop;
	}
}
