package com.example.user.a51goldjob.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.activity_common_textarea)
public class CommonTextAreaActivity extends BaseFormActivity implements TextWatcher,OnClickListener{

	public final static String CommonTextContent = "textValue";
	public final static String TagContent = "tag";
	public final static String Title = "Title";
	
	@ViewInject(R.id.common_textarea)
	EditText common_textarea;
	
	private String tag ;
	
	private EditText common_view;
	private LinearLayout reLayout;
	private TextView txtView;
	private ImageView backArrow;
	private RelativeLayout relativeLayout;

	Handler handler = new Handler();
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // TODO Auto-generated method stub
	    tag = getIntent().getExtras().getString(TagContent);
	    orignStr=getIntent().getExtras().getString("SelfRemark");
	    if(orignStr==null){
	    	orignStr="";
	    }
	   
	   
	    setBarTitle("自我评价");
	    String content = getIntent().getExtras().getString(CommonTextContent);
	    common_textarea.setText(content);
	    common_view=(EditText)this.findViewById(R.id.common_textarea);
	    common_view.addTextChangedListener(this);
	    relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
	    relativeLayout.setOnClickListener(this);
	    relativeLayout.setClickable(false);
	   txtView=(TextView)this.findViewById(R.id.textViewRight);
	   reLayout=(LinearLayout)this.findViewById(R.id.topbar_active_icon_layout);
	   reLayout.setOnClickListener(this);
	   backArrow=(ImageView)this.findViewById(R.id.topbar_return_btn);
	   backArrow.setOnClickListener(this);
//	   this.findViewById(id);
	   reLayout.setClickable(false);
	   txtView.setText("保存");
	   txtView.setOnClickListener(this);
	   txtView.setVisibility(View.INVISIBLE);
	   if(!orignStr.equals("")){
	    	common_view.setText(orignStr);
	    }
	}

	protected int getSubmitTextStringId() {
		return R.string.submit;
	}
	
	protected void actionPerformed_submitOnClick(View v) {
		ActivityUtils.returnActivity(handler, this, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(CommonTextContent, common_textarea.getText().toString());
				intent.putExtra(TagContent, tag);
			}
		});
	}

	@Override
	public void onClick(View v) {
   if(v.getId()==R.id.title_left||v.getId()==R.id.topbar_return_btn){
	   if(relativeLayout.isClickable()){ //可点击则为保存
			confirmMessage(CommonTextAreaActivity.this, true, "返回当前页面会丢失编辑内容是否要保存?");
		}else{
			this.finish();
		}
	   
   }	
		switch (v.getId()) {
        case R.id.textViewRight:// save data to lastPage
			String selfRemarkStr=common_view.getText().toString();
			Intent intent=new Intent();
			intent.putExtra("RemarkBackStr", selfRemarkStr);
			setResult(100, intent);
			this.finish();
			break;

		default:
			break;
		}
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
    public static String orignStr="";
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		if(!s.toString().equals(orignStr)){
			reLayout.setClickable(true);
			txtView.setVisibility(View.VISIBLE);
			relativeLayout.setClickable(true);
		}else{
			reLayout.setClickable(false);
			txtView.setVisibility(View.INVISIBLE);
			relativeLayout.setClickable(false);
		}
  	}
	
	public void confirmMessage(final Activity activity, final boolean finish, final String message) {
		if (activity == null) {
			return;
		}
		activity.runOnUiThread(new Runnable() {
			public void run() {
				if(!activity.isFinishing()) {
					if (finish) {
						final CustomDialog customDialog = DialogUtils.confirm(activity, message);
						Button confirmBtn = (Button) customDialog.findViewById(R.id.dialog_custom_confirm_btn);
						confirmBtn.setText("返回");
						Button cancelBtn = (Button) customDialog.findViewById(R.id.dialog_custom_cancel_btn);
						cancelBtn.setText("保存");
						confirmBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								
								activity.finish();
							}
						});
						cancelBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								String selfRemarkStr=common_view.getText().toString();
								Intent intent=new Intent();
								intent.putExtra("RemarkBackStr", selfRemarkStr);
								setResult(100, intent);
								activity.finish();
							}
						});
					} else {
						DialogUtils.commonInfo(activity, message);
					}
				}
			}
		});
	}

}
