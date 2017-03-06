package com.example.user.a51goldjob.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.ImageUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.mgr.UserActivityMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 首页
 * 
 * @author yeq
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {
	
	@ViewInject(R.id.logo_imgview)
	ImageView logo_imgview;
	SharedPreferences sp=null;
	@ViewInject(R.id.copyright_imgview)
	ImageView copyright_imgview;
	private boolean isGuidePageShowing=false;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityUtils.fullScreen(this);
        sp=getSharedPreferences("mySharedPreferences",Context.MODE_PRIVATE);
        ViewUtils.inject(this);
        
        Bitmap logo2 = BitmapFactory.decodeResource(getResources(), R.drawable.logo_two);
        Bitmap logo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        
        Bitmap bitMap = ImageUtils.overlay(getResources(), logo2, logo);
        logo_imgview.setImageBitmap(bitMap);
        
        logo_imgview.setPadding( logo_imgview.getPaddingLeft(),  logo_imgview.getPaddingTop(),  logo_imgview.getPaddingRight(),  logo_imgview.getPaddingBottom()+200);
        copyright_imgview.setPadding(copyright_imgview.getPaddingLeft(),  copyright_imgview.getPaddingTop(),  copyright_imgview.getPaddingRight(),  copyright_imgview.getPaddingBottom()+50);

        Mgr.get(UserActivityMgr.class).initApp(this); // 自动登录
        
        // 2秒后跳转至四个引导页
        Handler handler = new Handler();
        if(!isGuidePageShowing){
        	handler.postDelayed(new Runnable() {
    			@Override
    			public void run() {
    				String temp=sp.getString("isShown", "");
    				if (!isFinishing()&&!temp.equals("yes")) {	
    					Editor editor=sp.edit();
    					editor.putString("isShown","yes");
    					editor.commit();
    					Intent intent = new Intent(MainActivity.this, HomeIntroductionActivity.class); // 跳转自引导页
    					startActivity(intent);
    					overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out); 
    					finish();  		
    				}else{
    					Intent intent=new Intent(MainActivity.this,HomeActivity.class);
    		        	startActivity(intent);
    		        	overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out); 
    		        	finish();
    				}
    			}
    		}, 2000);

        }
		        
//      if (savedInstanceState == null) {
//          getSupportFragmentManager().beginTransaction()
//                  .add(R.id.container, new PlaceholderFragment())
//                  .commit();
//      }
    }


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
    
    private long exitTime=0;
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		if(keyCode==KeyEvent.KEYCODE_BACK&&event.getAction() == KeyEvent.ACTION_DOWN){
			long currentTime=System.currentTimeMillis();
			if((System.currentTimeMillis()-exitTime) > 2000){  
		            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();                                
		            exitTime = System.currentTimeMillis();   
		        } else {
		            finish();
		            System.exit(0);
		        }
			 return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
