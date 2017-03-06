package com.example.user.a51goldjob.activity;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;


import com.example.user.a51goldjob.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.activity_more_challenge)
public class MoreChallengeActivity extends BaseActivity {

	@ViewInject(R.id.choose_challenge_second_id)
	TextView choose_challenge_second_id;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.choose_challenge);
	    // TODO Auto-generated method stub
	    init();
	}

	private void init() {
		// TODO Auto-generated method stub
		choose_challenge_second_id.setText(Html.fromHtml(getHtml()));
		choose_challenge_second_id.setMovementMethod(LinkMovementMethod.getInstance());
	}

	private String getHtml() {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer("参与此次评选的理财师请通过以下网站提交材料\n请登录<a href='http://10.12.0.110:8090/vita/my/fxapply'>http://10.12.0.110:8090/vita/my/fxapply</a>报名\n登陆填写报名资料，评审委员会根据选手真实报名资料及线上答题考试。\n（主办方将对复赛入围选手报名材料真实性做诚信调查,若与事实不符，将取消参赛资格）\n");
		return sb.toString();
	}

}
