package com.example.user.a51goldjob.activity;

import android.os.Bundle;
import android.widget.TextView;


import com.chyjr.goldjob.fr.utils.ViewerUtils;
import com.example.user.a51goldjob.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 免责声明
 * @author yeq
 *
 */
@ContentView(R.layout.activity_disclaimer)
public class DisclaimerActivity extends BaseActivity {
	
	@ViewInject(R.id.disclaimer_description_txt)
	TextView disclaimer_description_txt;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.news_disclaimer);
	    
        ViewerUtils.setMovementMethod(disclaimer_description_txt);
	}

}
