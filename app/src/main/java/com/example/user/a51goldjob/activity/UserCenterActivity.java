package com.example.user.a51goldjob.activity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.BindUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.entities.User;
import com.example.user.a51goldjob.listeners.UserLogoutListener;
import com.example.user.a51goldjob.mgr.UserActivityMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.example.user.a51goldjob.view.PublicViews;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 个人中心
 * 
 * @author yeq
 */
@ContentView(R.layout.activity_user_center)
public class UserCenterActivity extends BaseActivity {
	
	@ViewInject(R.id.user_center_register_email_content)
	TextView user_center_register_email_content;
	
	@ViewInject(R.id.home_title_logout_img)
	TextView home_title_logout_img;
	
	@ViewInject(R.id.home_title_login_img)
	TextView home_title_login_img;
	
	Handler handler = new Handler();
	
	//组织页面布局的ID数组
	int[] menuViewIds = {
		R.id.user_center_apply_info_layout, 
		R.id.user_center_favorite_layout, 
		R.id.user_center_changed_password_layout
	};
	
	//组织页面布局相关的Activity链接
	Class<?>[] activities = {
		JobApplyAcativity.class, // 职位申请记录
		JobFavoriteActivity.class, // 职位收藏夹
		ChangedPasswordActivity.class // 更改密码
	};
    
	//菜单动作监听
	View.OnClickListener menuOnClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			actionPerformed_menuOnClick(v);
		}
	};
    
    UserActivityMgr userActivityMgr = Mgr.get(UserActivityMgr.class);
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        setBarTitle(R.string.activity_user_home_name);
        
        init();
	}
	
	/**
	 * 初始化页面数据
	 * */
    protected void init() {
    	
        PublicViews.createMenuTextView(this, getString(R.string.logout), getResources().getColor(R.color.text_white), new UserLogoutListener(this)); // 创建菜单按钮

        BindUtils.bindCtrlOnClickListener(this, menuOnClickListener, menuViewIds); // 绑定职位申请记录、职位收藏夹、更改密码点击事件
        BindUtils.bindCtrlOnClickListener(this, menuOnClickListener, R.id.user_center_resume_refresh_layout); // 绑定刷新事件
	}

    //菜单动作监听
	protected void actionPerformed_menuOnClick(View view) {
    	int viewId = view.getId();
    	
    	//刷新简历的动作
    	if (viewId == R.id.user_center_resume_refresh_layout) {
    		refreshResume(); // 刷新简历
    		return;
    	}
    	
    	//申请记录、收藏夹、修改密码的动作。
    	for (int i=0 ;i<menuViewIds.length; i++) {
    		if (menuViewIds[i] == viewId) {
    			//相应页面跳转，通过数组对应。
    			ActivityUtils.startActivity(handler, this, activities[i]);
    			break;
    		}
    	}
	}

	protected void onResume() {
		super.onResume();

		User user = userActivityMgr.getLoginUser(this);
		if (user == null) {
			CustomDialog customDialog = DialogUtils.info(this, getResources().getString(R.string.nologin_error));
			customDialog.setBtnsOnClickListener(new DialogUtils.DialogOnClickFinishListenerAdapter(customDialog, this));
		} else {
			user_center_register_email_content.setText(user.getDecryptUsername());
		}
	}
	
    // 刷新简历
	private void refreshResume() {
		
//		final Dialog dialog = DialogViews.createLoadingDialog(this, getString(R.string.resume_refreshing));
//		dialog.show();

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Looper.prepare();
				//DialogUtils.commonInfo(getThis(), getString(R.string.refresh_success));
				Toast.makeText(UserCenterActivity.this,R.string.refresh_success,Toast.LENGTH_SHORT).show();
				Looper.loop();
			}
		}, 1000);
	}
}
