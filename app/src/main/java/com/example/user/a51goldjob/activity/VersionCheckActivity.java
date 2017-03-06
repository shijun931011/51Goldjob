package com.example.user.a51goldjob.activity;

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;


import com.chyjr.goldjob.fr.utils.PackageUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.lidroid.xutils.view.annotation.ContentView;

/**
 * 版本升级
 * @author yeq
 *
 */
@ContentView(R.layout.activity_versioncheck)
public class VersionCheckActivity extends BaseActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.other_versioncheck);
        
	    init();
	}

	protected void init() {
	    // TODO 获取版本
        PackageInfo packageInfo = PackageUtils.getPackageInfo(this);
        // TODO 检查版本
    	final CustomDialog dialog = DialogUtils.info(this, getString(R.string.other_versioncheck_currentisnew));
        dialog.setBtnsOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO 下载
				dialog.dismiss();
				finish();
			}
		});
	}
}
