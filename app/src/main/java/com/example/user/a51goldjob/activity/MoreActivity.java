package com.example.user.a51goldjob.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;


import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

@ContentView(R.layout.activity_more)
public class MoreActivity extends BaseActivity {

	@ViewInject(R.id.more_intro_id)
	TextView more_intro_id;

	@ViewInject(R.id.more_challenge_id)
	TextView more_challenge_id;

	@ViewInject(R.id.more_standard_id)
	TextView more_standard_id;

	@ViewInject(R.id.more_structure_id)
	TextView more_structure_id;

	@ViewInject(R.id.more_cooperation_id)
	TextView more_cooperation_id;

	public static final String MenuId = "MenuId"; 
	Handler handler = new Handler();
	// 菜单+按钮
	int[] menuIds = { R.id.more_intro_id, R.id.more_challenge_id,
			R.id.more_standard_id, R.id.more_structure_id,
			R.id.more_cooperation_id };

	// 菜单、按钮关联的界面
	Class<?>[] activities = { MoreIntroActivity.class, MoreChallengeActivity.class,
			MoreIntroActivity.class, MoreImageActivity.class, MoreCooperationActivity.class };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.load_more);
		// TODO Auto-generated method stub
	}

	@OnClick(value = { R.id.more_intro_id, R.id.more_challenge_id,
			R.id.more_standard_id, R.id.more_structure_id,
			R.id.more_cooperation_id })
	public void actionPerformed_itemOnClick(View v) {
		int viewId = v.getId();

		// 打开菜单
		for (int i = 0; i < menuIds.length; i++) {
			if (menuIds[i] == viewId) {
				final int menuId = viewId;
				ActivityUtils.startActivity(handler, this, activities[i], new IIntentHandler() {
					@Override
					public void putExtra(Intent intent) {
						// TODO Auto-generated method stub
						intent.putExtra(MenuId, menuId);
					}
				});
				return;
			}
		}

	}

}
