package com.example.user.a51goldjob.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.widget.TextView;


import com.example.user.a51goldjob.R;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_more_intro)
public class MoreIntroActivity extends BaseActivity {

	@ViewInject(R.id.choose_intro_first_id)
	TextView	choose_intro_first_id;
	
	@ViewInject(R.id.choose_intro_first_content_id)
	TextView	choose_intro_first_content_id;
	
	@ViewInject(R.id.choose_intro_second_id)
	TextView	choose_intro_second_id;
	
	@ViewInject(R.id.choose_intro_second_content_id)
	TextView	choose_intro_second_content_id;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.choose_intro);
	    // TODO Auto-generated method stub
	    int menuId = getIntent().getExtras().getInt(MoreActivity.MenuId);
	    if(R.id.more_standard_id == menuId){
	    	choose_intro_first_id.setText(getString(R.string.choose_standard_first));
	    	choose_intro_first_content_id.setText(getString(R.string.choose_standard_first_content));
	    	choose_intro_second_id.setText(getString(R.string.choose_standard_second));
	    	choose_intro_second_content_id.setText(getString(R.string.choose_standard_second_content));
	    }
	}
	

	
}
