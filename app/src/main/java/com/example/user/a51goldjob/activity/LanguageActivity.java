package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.HashMap;
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
import com.chyjr.goldjob.fr.utils.BindUtils;
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
import com.example.user.a51goldjob.utils.StringUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.activity_language)
public class LanguageActivity extends BaseFormActivity implements OnClickListener,TextWatcher{


	@ViewInject(R.id.language_type_id)
	TextView language_type_id;
	
	@ViewInject(R.id.language_mastery_id)
	TextView language_mastery_id;
	
	@ViewInject(R.id.language_randw_id)
	TextView language_randw_id;
	
	@ViewInject(R.id.language_lands_id)
	TextView language_lands_id;
	
	final int[] dictViewIds = {
			R.id.language_type_id, 
			R.id.language_mastery_id,
			R.id.language_randw_id,
			R.id.language_lands_id
		};
	
	final String firstStr = "选择";
	final Map dictTitles=new HashMap();
	
	Handler handler = new Handler();
	private static TextView txtView;
	private static final TextView txtViews=txtView;
	private RelativeLayout relativeLayouts;
	private RelativeLayout relativeLayout;
	private int fistload=1;
	private int itemlength=1;
	private List<String> itemList=new ArrayList<String>();
	private boolean canShow=false;
	private String beforeText="";
	private String orignText="";
	private String tempst="";
	private String tempst2="";
	
	private boolean isAddFromBtn=false;
	
	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_dictSelectorOnClick(v);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.language);
	    // TODO Auto-generated method stub
	    if(itemList.size()<3){
			itemList.add("");
		}
	    init();
	}

	private void init() {
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);
		
	    TextView txtView=(TextView)this.findViewById(R.id.language_type_id);
	    txtView.addTextChangedListener(this);
	    
	    txtView=(TextView)this.findViewById(R.id.language_mastery_id);
	    txtView.addTextChangedListener(this);
	    
	    txtView=(TextView)this.findViewById(R.id.language_randw_id);
	    txtView.addTextChangedListener(this);
	    txtView=(TextView)this.findViewById(R.id.language_lands_id);
	    txtView.addTextChangedListener(this);
	    		
		//查询
		String languageid = getIntent().getExtras().getString("id");
		if(languageid==null){
			languageid="";
		}
		if(languageid.equals("")){
			isAddFromBtn=true;
		}
		if(languageid!=null&&!"".equals(languageid)){
			Token token = AppContext.get(this).getToken();
			JSONObject jsonObject = new JSONObject();
			JsonUtils.put(jsonObject, "token", token.getToken());
			JsonUtils.put(jsonObject, "userid", token.getUserId()); 
			
			String resumeid = getIntent().getExtras().getString("resumeid");
			JsonUtils.put(jsonObject, "resumeid", resumeid); 
			JsonUtils.put(jsonObject, "id", languageid); 
			final String data = RequestData.getData(jsonObject, Transcodes.TC_LANGUAGE, false);
			Https.post(this, Transcodes.TC_LANGUAGE, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
				protected void _onSuccess(ResponseInfo<String> response) throws Exception {
					loadForm(response);
				}
			});
		}
		
		//数据字典选项绑定
		bindDictTitle();
		BindUtils.bindCtrlOnClickListener(this, onClickListener, dictViewIds);
	}
	
	/**
	 * 绑定id和显示TITLE
	 */
	private void bindDictTitle(){
		dictTitles.put(R.id.language_lands_id, this.getString(R.string.language_lands));
		dictTitles.put(R.id.language_mastery_id, this.getString(R.string.language_mastery));
		dictTitles.put(R.id.language_randw_id, this.getString(R.string.language_randw));
		dictTitles.put(R.id.language_type_id, this.getString(R.string.language_type));
	}
	
	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
		language_lands_id.setText((String) map.get("lands"));
		language_mastery_id.setText((String)map.get("mastery"));
		language_randw_id.setText((String)map.get("randw"));
		language_type_id.setText((String)map.get("type"));
	}

	private String selectStr="";
	protected void actionPerformed_dictSelectorOnClick(View v) {
		final String dictKey = (String) v.getTag();
		TextView selectTxtView=(TextView)v;
		selectStr=selectTxtView.getText().toString();
		final int dictId = v.getId();
		final String topTitle = firstStr + dictTitles.get(dictId);
		ActivityUtils.startActivityForResult(handler, this, DictSelectorActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY, dictKey); 
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_TOP_TITLE, topTitle);
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_HEADER_TITLE, topTitle);
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_ITEM, dictId);
				intent.putExtra("selectStr", selectStr);
				
			};
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 带回数据初始化界面
		if (resultCode == RESULT_OK) {
			int dictId = data.getExtras().getInt(DictSelectorActivity.Constants.PARAM_IN_ITEM);

			TextView view = null;
			for (int i=0; i<dictViewIds.length; i++) {
				view = (TextView) findViewById(dictViewIds[i]);
				if (view.getId()==dictId) {
					view.setText(data.getStringExtra(DictSelectorActivity.Constants.PARAM_OUT_SELECT_ITEM));
				}
			}
			
		}
	}

	protected int getSubmitTextStringId() {
		return R.string.save;
	}

	protected void actionPerformed_submitOnClick(View v) {
		String reason = checkForm();
		if(!StringUtils.isEmpty(reason)){
			DialogUtils.commonInfo(this, reason);
			return;
		}
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		
		String resumeid = getIntent().getExtras().getString("resumeid");
		String languageid = getIntent().getExtras().getString("id");
		JsonUtils.put(jsonObject, "resumeid", resumeid); 
		JsonUtils.put(jsonObject, "languageid", languageid); 
		JsonUtils.put(jsonObject, "lands", language_lands_id.getText().toString()); 
		JsonUtils.put(jsonObject, "mastery", language_mastery_id.getText().toString()); 
		JsonUtils.put(jsonObject, "randw", language_randw_id.getText().toString()); 
		JsonUtils.put(jsonObject, "type", language_type_id.getText().toString()); 
		
		final String data = RequestData.getData(jsonObject, Transcodes.TC_LANGUAGE_SET, false);
		Https.post(this, Transcodes.TC_LANGUAGE_SET, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		});
	}
	
	//检查表单是否完整
		private String checkForm(){
			String reason = this.getResources().getString(R.string.check_form_no_whole);
			if(language_type_id.getText()==null||"".equals(language_type_id.getText().toString().trim())){
				return reason;
			}
			if(language_randw_id.getText()==null||"".equals(language_randw_id.getText().toString().trim())){
				return reason;
			}
			if(language_lands_id.getText()==null||"".equals(language_lands_id.getText().toString().trim())){
				return reason;
			}
			if(language_mastery_id.getText()==null||"".equals(language_mastery_id.getText().toString().trim())){
				return reason;
			}
			reason = isExist(language_type_id.getText().toString().trim());
			if(!StringUtils.isEmpty(reason)){
				return reason;
			}
			reason = "";
			return reason;
		}
		//检查语言是否已添加
		private String isExist(String type){
			String reason = "";
			if(getIntent().getExtras().getString("id")==null){
				List<String> typeList = (List<String>) getIntent().getExtras().get("typeList");
				if(typeList!=null){
					if(typeList.contains(type)){
						reason = this.getResources().getString(R.string.check_language_is_exist);
					}
				}
			}
			return reason;
		}
	
	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		ActivityUtils.showMessage(this, responseData.isSuccess(), responseData.getReturnMsg());
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.title_left||v.getId()==R.id.topbar_return_btn){
			if(relativeLayout.isClickable()){ //可点击则为保存
				confirmMessage(LanguageActivity.this, true, "返回当前页面会丢失编辑内容是否要保存?");
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
		beforeText=s.toString();
		if(itemList.size()<4){
			itemList.add(s.toString());
			//Toast.makeText(AdvancedActivity.this, "集合大小="+itemList.size()+"添加的元素"+s.toString(), Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

		//Toast.makeText(AdvancedActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
		if(beforeText.equals(s.toString())){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isAddFromBtn){
				 txtView.setText("添加"); 
			 }else{
				 txtView.setText("保存");
			 }
			
			 relativeLayout.setClickable(true);
			    txtView.setVisibility(View.VISIBLE);

		}else {
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isAddFromBtn){
				 txtView.setText("添加"); 
			 }else{
				 txtView.setText("保存");
			 }
			 relativeLayout.setClickable(false);
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		
		if(fistload==4){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isAddFromBtn){
				 txtView.setText("添加"); 
			 }else{
				 txtView.setText("保存");
			 }
			 relativeLayout.setClickable(false);
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		fistload=fistload+1;
		if(itemList.size()==4){
			for(int i=0;i<4;i++){
				if(itemList.get(i).equals(s.toString())){
				//	Toast.makeText(BaseDocumentActivity.this,itemList.get(i)+"#"+s.toString(), Toast.LENGTH_SHORT).show();
					 txtView=(TextView)this.findViewById(R.id.textViewRight);
					 if(isAddFromBtn){
						 txtView.setText("添加"); 
					 }else{
						 txtView.setText("保存");
					 }
					 relativeLayout.setClickable(false);
					    txtView.setVisibility(View.INVISIBLE);
					    txtView.setClickable(false);
				}
			}
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
