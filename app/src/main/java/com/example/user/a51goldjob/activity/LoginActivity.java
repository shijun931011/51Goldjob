package com.example.user.a51goldjob.activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.ViewerUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.mgr.UserActivityMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.view.LineEditText;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 登录界面
 * @author yeq
 *
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseFormActivity implements View.OnClickListener{
	
	@ViewInject(R.id.login_email_txt)
	LineEditText login_email_txt;
	
	@ViewInject(R.id.login_password_txt)
	LineEditText login_password_txt;
	
	Handler handler = new Handler();
	
	UserActivityMgr userMgr = Mgr.get(UserActivityMgr.class);
	
	private int whichView=0;
	private int whichpage=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    if(getIntent().getExtras()!=null){
	          whichView=getIntent().getExtras().getInt("viewid");
	    }
        setBarTitle(R.string.login);
        TextView txt=(TextView)this.findViewById(R.id.textViewRight); 
        txt.setText("注册");
        txt.setOnClickListener(this);
	}

	@OnClick(value={R.id.login_bottom_btn, R.id.login_security_center_right_txt})
	public void actionPreformed_onClick(View v) {
		int viewId = v.getId();
		if (viewId == R.id.login_bottom_btn) {
			actionPerformed_submitOnClick(v);
		} else if (viewId == R.id.login_security_center_right_txt) {
			ActivityUtils.startActivity(handler, this, ResetPasswordActivity.class); // 忘记密码，打开重置密码界面
		}
	}
	
	// 提交“登录”
	protected void actionPerformed_submitOnClick(View v) {
		String email = ViewerUtils.getTextViewString(login_email_txt);
		String password = ViewerUtils.getTextViewString(login_password_txt);
		userMgr.login(this, email, password, whichView);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.textViewRight){
			ActivityUtils.startActivity(handler, this, RegisterActivity.class); // 打开注册界面
		}
		
	}
}
