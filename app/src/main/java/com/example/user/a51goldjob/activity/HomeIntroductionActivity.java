package com.example.user.a51goldjob.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.View;


import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.anim.ViewFlipperAnim;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 引导页
 * 
 * @author yeq
 */
@ContentView(R.layout.activity_home_introduction)
public class HomeIntroductionActivity extends Activity {

	Handler handler = new Handler();
	
	GestureDetector gestureDetector;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityUtils.fullScreen(this);
        ViewUtils.inject(this);
		
        ViewFlipperAnim viewFlipperAnim = ViewFlipperAnim.create(this, R.id.home_viewflipper);
        viewFlipperAnim.setLoop(false);
		
//	    Display mDisplay = getWindowManager().getDefaultDisplay();
//	    int W = mDisplay.getWidth();
//	    int H = mDisplay.getHeight();
//	    Log.i("Main", "Width = " + W);
//	    Log.i("Main", "Height = " + H);
	}
	
	@OnClick(value={R.id.jump_imgbtn, R.id.more_imgbtn})
	public void actionPerformed_jumpClick(View v) {
		handler.post(new Runnable() {
			public void run() {
				Intent intent = new Intent(HomeIntroductionActivity.this, HomeActivity.class); // 跳转自引导页
				startActivity(intent);
				overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out); 
				finish();
			}
		});
	}
	
}
