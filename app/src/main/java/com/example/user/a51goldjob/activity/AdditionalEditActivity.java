package com.example.user.a51goldjob.activity;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;



import org.json.JSONObject;


import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.activity_additional_edit)
public class AdditionalEditActivity extends BaseFormActivity implements OnClickListener,TextWatcher{


	@ViewInject(R.id.additional_subject_id)
	TextView additional_subject_id;

	@ViewInject(R.id.additional_content_id)
	TextView additional_content_id;
	
	private final String[] subjects = {"特长","兴趣爱好","职业目标","特殊技能","社会活动","荣誉","宗教信仰","推荐信","其他"};
	private String tag;
	private String value;
	
	Handler handler = new Handler();
	
	List<String> defaultValue=null;
	private static TextView txtView;
	private static final TextView txtViews=txtView;
	private RelativeLayout relativeLayouts;
	private RelativeLayout relativeLayout;
	private int fistload=1;
	private int itemlength=1;
	private List<String> itemList=new ArrayList<String>();
	private boolean canShow=false;
	private String orignText="";
	private String tempst="";
	private String tempst2="";
	private String selectedStr="";
	
	private TextView subjectTxtView=null;
	private TextView contentTxtView=null;

	OnClickListener onSelectClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_selectContentOnClick(v);
		}
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_textContentOnClick(v);
		}
	};

	protected void actionPerformed_textContentOnClick(View v) {
		// TODO Auto-generated method stub
		Class<?> to = ContentInfoActivity.class;
		final String tag = (String) v.getTag();
		final TextView tv = (TextView) v;
		ActivityUtils.startActivityForResult(handler, this, to, new IIntentHandler() {
			@Override
			public void putExtra(Intent intent) {
				// TODO Auto-generated method stub
				intent.putExtra(ContentInfoActivity.TagContent, tag);
				intent.putExtra(ContentInfoActivity.CommonTextContent, tv
								.getText().toString());
			}
		});
	}
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.additional);
		// TODO Auto-generated method stub
	    subjectTxtView=(TextView)this.findViewById(R.id.additional_subject_id);
		subjectTxtView.addTextChangedListener(this);
		contentTxtView=(TextView)this.findViewById(R.id.additional_content_id);
		contentTxtView.addTextChangedListener(this);
		init();
	}

    private static int requestCodeForContent=100;
    private String selectString="";
	protected void actionPerformed_selectContentOnClick(View v) {
		// TODO Auto-generated method stub
		Class<?> to = AdditionalSubjectActivity.class;
		tag = (String) v.getTag();
		if(v instanceof TextView){
			selectString=((TextView) v).getText().toString();
		}
		ActivityUtils.startActivityForResult(handler, AdditionalEditActivity.this, to, new IIntentHandler() {
			@Override
			public void putExtra(Intent intent) {
				// TODO Auto-generated method stub
				intent.putExtra(AdditionalSubjectActivity.DataListKey, subjects);
				intent.putExtra(AdditionalSubjectActivity.KeyValue, "subject");
				intent.putExtra("selectStr", selectString);
				intent.putExtra(AdditionalSubjectActivity.TextContent, additional_subject_id.getText().toString());
			}
		});
	}

	private TextView ContentTxtView;
	private void init() {
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);
		
		TextView txtView=(TextView)this.findViewById(R.id.additional_subject_id);
		txtView.addTextChangedListener(this);
		
		ContentTxtView=(TextView)this.findViewById(R.id.additional_content_id);
		ContentTxtView.addTextChangedListener(this);
		// 查询
		String activeflag = getIntent().getExtras().getString("activeflag");
		String id = getIntent().getExtras().getString("id");
		if (id != null && !"".equals(id)) {
			Token token = AppContext.get(this).getToken();
			JSONObject jsonObject = new JSONObject();
			JsonUtils.put(jsonObject, "token", token.getToken());
			JsonUtils.put(jsonObject, "userid", token.getUserId());

			String resumeid = getIntent().getExtras().getString("resumeid");
			JsonUtils.put(jsonObject, "resumeid", resumeid);
			JsonUtils.put(jsonObject, "id", id);
			final String data = RequestData.getData(jsonObject,
					Transcodes.TC_ADDITIONAL, false);
			Https.post(this, Transcodes.TC_ADDITIONAL, data,
					new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
						protected void _onSuccess(ResponseInfo<String> response)
								throws Exception {
							loadForm(response);
						}
					});
		}

		additional_subject_id.setOnClickListener(onSelectClickListener);
		additional_content_id.setOnClickListener(onClickListener);

	}


	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data,
			new TypeToken<Map<String, Object>>() {
		    }.getType());
		additional_subject_id.setText(map.get("subject").toString());
		additional_content_id.setText(map.get("content").toString());
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			String tag = data.getExtras().getString(CommonTextAreaActivity.TagContent);
			if(getString(R.string.additional_content).equals(tag)){
				String value = (String) data.getExtras().getString(CommonTextAreaActivity.CommonTextContent);
				additional_content_id.setText(value);
			}else{
				String value = (String) data.getExtras().getString(AdditionalSubjectActivity.Value);
				additional_subject_id.setText(value);
			}
		}
		//backStr for content
		if(resultCode==requestCodeForContent){
			if(data!=null){
				String backStr=data.getStringExtra("RemarkBackStr");
				ContentTxtView.setText(backStr);
			}
		}
	}

	protected int getSubmitTextStringId() {
		return R.string.save;
	}

	protected void actionPerformed_submitOnClick(View v) {
		// if(!checkForm()){
		// DialogUtils.commonInfo(this,
		// this.getResources().getString(R.string.check_form_no_whole));
		// return;
		// }
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId());

		String resumeid = getIntent().getExtras().getString("resumeid");
		String otherid = getIntent().getExtras().getString("id");
		String activeflag = getIntent().getExtras().getString("activeflag");
		JsonUtils.put(jsonObject, "resumeid", resumeid);
		JsonUtils.put(jsonObject, "otherid", otherid);
		JsonUtils.put(jsonObject, "activeflag", activeflag);
		JsonUtils.put(jsonObject, "subject", additional_subject_id.getText()
				.toString()); 
		JsonUtils.put(jsonObject, "content", additional_content_id.getText()
				.toString()); 

		final String data = RequestData.getData(jsonObject,
				Transcodes.TC_ADDITIONAL_SET, false);
		Https.post(this, Transcodes.TC_ADDITIONAL_SET, data,
				new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
					protected void _onSuccess(ResponseInfo<String> response)
							throws Exception {
						doSuccess(response);
					}
				});
	}


	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		ActivityUtils.showMessage(this, responseData.isSuccess(),
				responseData.getReturnMsg());
	}


	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.title_left||v.getId()==R.id.topbar_return_btn){
			if(relativeLayout.isClickable()){ //可点击则为保存
				confirmMessage(AdditionalEditActivity.this, true, "是否保存编辑内容?");
			}else{
				this.finish();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}


	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(itemList.size()<2){
		   itemList.add(s.toString());
		}		
	}

	@Override
	public void afterTextChanged(Editable s) {	
		for(int i=0;i<itemList.size();i++){
			if(itemList.get(i).equals(s.toString())){ //dont show the save btn
				 txtView=(TextView)this.findViewById(R.id.textViewRight);
				 txtView.setText("保存");
				 relativeLayout.setClickable(false);
				 txtView.setVisibility(View.INVISIBLE);
				 txtView.setClickable(false);
			}else{
				 txtView.setClickable(true);
				 txtView.setVisibility(View.VISIBLE);
				 txtView=(TextView)this.findViewById(R.id.textViewRight);
				 txtView.setText("保存");
				 relativeLayout.setClickable(true);
			}
		}
		if(fistload==2){
		 txtView=(TextView)this.findViewById(R.id.textViewRight);
	     txtView.setText("保存"); 
		 relativeLayout.setClickable(true);	 
	     txtView.setVisibility(View.INVISIBLE);
		 txtView.setClickable(true);
	    }
		fistload=fistload+1;
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
								// TODO Auto-generated method stub
								actionPerformed_submitOnClick(txtViews);
								customDialog.dismiss();
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
