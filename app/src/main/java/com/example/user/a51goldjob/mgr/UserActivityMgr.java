package com.example.user.a51goldjob.mgr;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.app.Activity;

import android.content.Intent;

import android.widget.Toast;


import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.DB;

import com.chyjr.goldjob.fr.utils.MD5;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.activity.ChooseResumeActivity;
import com.example.user.a51goldjob.activity.JobFavoriteActivity;
import com.example.user.a51goldjob.activity.MessageActivity;
import com.example.user.a51goldjob.activity.MyResumeActivity;
import com.example.user.a51goldjob.activity.UserCenterActivity;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.entities.User;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * 用户会员业务逻辑管理
 * @author yeq
 *
 */
public class UserActivityMgr extends BaseMgr {

	/**
	 * 初始化
	 */
	public void initApp(Activity activity) {
		autoLogin(activity); // 自动登录
		Mgr.get(DictMgr.class).initDict(activity, null); // 初始化数据字典
		Mgr.get(AppMgr.class).initAppInfo(activity); // 初始化应用信息
	}

	/**
	 * 自动登录
	 */
	public void autoLogin(final Activity activity) {
    	// 查找数据库，查看是否有用户名、密码信息保存
    	final User user = getDefaultUser(activity);
    	
    	// 如果没有，跳过。
    	if (user == null || !(user.validate())) {
    		return;
    	}
    	
    	// 如果有，取出登录系统拿到token和userid之后，连同用户名密码一起放入本地数据库和静态变量

		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "username", user.getDecryptUsername()); // cc@test.com
		JsonUtils.put(jsonObject, "passwd", MD5.md5Hex(user.getDecryptPassword()).toLowerCase()); // 111

		String data = RequestData.getData(jsonObject, Transcodes.TC_LOGIN);
		
		Https.post(activity,  Transcodes.TC_LOGIN, data, new RequestCallBackAdapters.RequestCallBackAdapter<String>() {
			public void onSuccess(ResponseInfo<String> response) {
				ResponseData responseData = ResponseData.create(response.result);
				
				if (StringUtils.isEmpty(responseData.getToken())) {
					return;
				}
				// 获取userid、token，存入静态变量
				AppContext.get(activity).setToken(new Token(responseData.getToken(), responseData.getBodyPropertyValue("userid")));
			}
		});
	}

	private  int whichViews=0;
	/**
	 * 登录界面登录
	 * 
	 * @param email
	 * @param password
	 */
	public void login(final Activity activity, final String email, final String password, int whichView) {
		whichViews=whichView;
		if (StringUtils.isEmpty(email)) {
			//DialogUtils.commonInfo(activity, activity.getResources().getString(R.string.check_email_cannot_empty));
			Toast.makeText(activity,"邮箱不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if (StringUtils.isEmpty(password)) {
			//DialogUtils.commonInfo(activity, activity.getResources().getString(R.string.check_password_cannot_empty));
			Toast.makeText(activity,"密码不能为空!", Toast.LENGTH_SHORT).show();
			return;
		}

		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "username", email); // 登录用户
		JsonUtils.put(jsonObject, "passwd", MD5.md5Hex(password).toLowerCase()); // 登录密码
		
		String data = RequestData.getData(jsonObject,  Transcodes.TC_LOGIN);
		
		Https.post(activity,  Transcodes.TC_LOGIN, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(activity) {
			public void _onSuccess(ResponseInfo<String> response) {
				ResponseData responseData = ResponseData.create(response.result);

				if (StringUtils.isEmpty(responseData.getToken())) {
				//	DialogUtils.commonInfo(activity, responseData.getReturnMsg());
					Toast.makeText(activity,responseData.getReturnMsg(),Toast.LENGTH_SHORT).show();
					return;
				}

			//	final CustomDialog dialog = DialogUtils.info(activity, responseData.getReturnMsg());
//				dialog.setBtnsOnClickListener(new OnClickListener() {
//					public void onClick(View v) {
//						dialog.dismiss();
//						activity.finish();
//					}
//				});
				
				// 获取userid、token，存入数据库
				updateDefaultUser(activity, email, password);
				AppContext.get(activity).setToken(new Token(responseData.getToken(), responseData.getBodyPropertyValue("userid")));
                Toast.makeText(activity,responseData.getReturnMsg(),Toast.LENGTH_SHORT).show();    
                skipToCertinPage(activity);
			}
		});
	}
	
	/*
	 * 跳转到指定的页面
	 */
	public void skipToCertinPage(Activity context){
		 switch (whichViews) {
			 case R.id.home_usercenter_img:
				Intent intent=new Intent(context,UserCenterActivity.class);
				context.startActivity(intent);
				break;

             case R.id.home_myresume_img:
         	    Intent intent1=new Intent(context,MyResumeActivity.class);
         	    context.startActivity(intent1);
				break;
             case R.id.home_bottom_collect_img:
         	    Intent intent2=new Intent(context,JobFavoriteActivity.class);
				context.startActivity(intent2);
                break;
             case R.id.home_bottom_msg_img:
         	    Intent intent3=new Intent(context,MessageActivity.class);
         	    context.startActivity(intent3);
                break;
             case R.id.job_apply_details_btn:
        	    Intent intent4=new Intent(context,ChooseResumeActivity.class);
          	    context.startActivity(intent4);
        	    break;
		 }
		 context.finish();
		
	}
	/**
	 * 更新用户信息
	 * @param user
	 */
	public void updateDefaultUser(final Activity activity, final User user) {
		DB.execute(activity, new DB.DBExecutorAdapter<User>() {
			public User execute() throws Exception {
				getDb(activity).saveOrUpdate(user);
				return user;
			}
		});
	}
	
	/**
	 * 更新本地默认用户
	 * 
	 * @param username
	 * @param password
	 */
	public void updateDefaultUser(final Activity activity, final String username, final String password) {
		DB.execute(activity, new DB.DBExecutorAdapter<User>() {
			public User execute() throws Exception {
				User user = getDb(activity).findById(User.class, User.DEFAULT_ID);
				if (user == null || user.getId() <= 0) {
					user = new User();
				}
				user.setEecryptUsername(username);
				user.setEecryptPassword(password);
				getDb(activity).saveOrUpdate(user);
				return user;
			}
		});
	}
	
	public User getLoginUser(final Activity activity) {
		if (!AppContext.get(activity).isLogin()) {
			return null;
		}
    	User user = DB.execute(activity, new DB.DBExecutorAdapter<User>() {
			public User execute() throws Exception {
				return getDb(activity).findById(User.class, User.DEFAULT_ID);
			}
		});
		return user;
	}

	/**
	 * 获取本地存储的用户
	 * 
	 * @return
	 */
	public User getDefaultUser(final Activity activity) {
    	User user = DB.execute(activity, new DB.DBExecutorAdapter<User>() {
			public User execute() throws Exception {
				return getDb(activity).findById(User.class, User.DEFAULT_ID);
			}
		});
		return user;
	}
}
