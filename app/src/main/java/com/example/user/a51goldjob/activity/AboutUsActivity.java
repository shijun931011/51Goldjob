package com.example.user.a51goldjob.activity;

import android.os.Bundle;
import android.widget.TextView;


import com.chyjr.goldjob.fr.utils.ViewerUtils;

import com.example.user.a51goldjob.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 关于我们
 * @author yeq
 *
 */
@ContentView(R.layout.activity_abouts)
public class AboutUsActivity extends BaseActivity {
	
	@ViewInject(R.id.aboutus_description_txt)
	TextView aboutus_description_txt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.other_aboutus);
        
        ViewerUtils.setMovementMethod(aboutus_description_txt); // 设置文本带滚动条
	}

}
