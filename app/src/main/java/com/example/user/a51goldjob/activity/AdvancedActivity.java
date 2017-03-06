package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;

import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
@ContentView(R.layout.activity_advanced)
public class AdvancedActivity extends BaseFormActivity implements OnClickListener,TextWatcher{
	
	@ViewInject(R.id.advanced_stock_id)
	EditText advanced_stock_id;
	
	@ViewInject(R.id.advanced_basesalary_id)
	EditText advanced_basesalary_id;
	
	@ViewInject(R.id.advanced_subsidy_id)
	EditText advanced_subsidy_id;
	
	@ViewInject(R.id.advanced_yearbonus_id)
	EditText advanced_yearbonus_id;
	
	List<String> defaultValue=null;
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
	
	
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.advanced);
	    // TODO Auto-generated method stub
	    init();
	}

	public static EditText editTxt;
	private void init() {
		
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);

	    editTxt=(EditText)this.findViewById(R.id.advanced_basesalary_id);
		String baseSalary=editTxt.getText().toString();
		editTxt.addTextChangedListener(this);
		editTxt=(EditText)this.findViewById(R.id.advanced_yearbonus_id);
		String yearbonus=editTxt.getText().toString();
		editTxt.addTextChangedListener(this);
		editTxt=(EditText)this.findViewById(R.id.advanced_stock_id);
		String stockid=editTxt.getText().toString();
		editTxt.addTextChangedListener(this);
		editTxt=(EditText)this.findViewById(R.id.advanced_subsidy_id);
		String subsidy=editTxt.getText().toString();
		editTxt.addTextChangedListener(this);	
		
		//基本资料查询
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		
		String resumeid = getIntent().getExtras().getString("resumeid");
		JsonUtils.put(jsonObject, "resumeid", resumeid); 
		final String data = RequestData.getData(jsonObject, Transcodes.TC_ADVANCED, false);
		Https.post(this, Transcodes.TC_ADVANCED, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				loadForm(response);
			}
		});
		
	}
	
	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
		advanced_basesalary_id.setText((String) map.get("basesalary"));
		advanced_stock_id.setText((String)map.get("stock"));
		advanced_subsidy_id.setText((String)map.get("subsidy"));
		advanced_yearbonus_id.setText((String)map.get("yearbonus"));
	}



	protected int getSubmitTextStringId() {
		return R.string.save;
	}

	protected void actionPerformed_submitOnClick(View v) {
//		if(!checkForm()){
//			DialogUtils.commonInfo(this, this.getResources().getString(R.string.check_form_no_whole));
//			return;
//		}
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		
		String resumeid = getIntent().getExtras().getString("resumeid");
		JsonUtils.put(jsonObject, "resumeid", resumeid); 
		JsonUtils.put(jsonObject, "basesalary", advanced_basesalary_id.getText().toString()); 
		JsonUtils.put(jsonObject, "stock", advanced_stock_id.getText().toString()); 
		JsonUtils.put(jsonObject, "subsidy", advanced_subsidy_id.getText().toString()); 
		JsonUtils.put(jsonObject, "yearbonus", advanced_yearbonus_id.getText().toString()); 
		
		final String data = RequestData.getData(jsonObject, Transcodes.TC_ADVANCED_SET, false);
		Https.post(this, Transcodes.TC_ADVANCED_SET, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		});
	}
	
	
	//检查表单是否完整
//	private boolean checkForm(){
//		boolean result = false;
//		if(basedocument_name_id.getText()==null||"".equals(basedocument_name_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_sex_id.getText()==null||"".equals(basedocument_sex_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_workyears_id.getText()==null||"".equals(basedocument_workyears_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_borndate_id.getText()==null||"".equals(basedocument_borndate_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_telephone_id.getText()==null||"".equals(basedocument_telephone_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_idtype_id.getText()==null||"".equals(basedocument_idtype_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_jobstatus_id.getText()==null||"".equals(basedocument_jobstatus_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_id_id.getText()==null||"".equals(basedocument_id_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_accountcityid_id.getText()==null||"".equals(basedocument_accountcityid_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_address_id.getText()==null||"".equals(basedocument_address_id.getText().toString().trim())){
//			return result;
//		}else if(basedocument_salary_id.getText()==null||"".equals(basedocument_salary_id.getText().toString().trim())){
//			return result;
//		}
//		result = true;
//		return result;
//	}
	
	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		ActivityUtils.showMessage(this, responseData.isSuccess(), responseData.getReturnMsg());
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.title_left||v.getId()==R.id.topbar_return_btn){
			if(relativeLayout.isClickable()){ //可点击则为保存
				confirmMessage(AdvancedActivity.this, true, "是否保存当前编辑内容?");
			}else{
				this.finish();
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

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		//Toast.makeText(this, "onChange"+s.toString(), Toast.LENGTH_SHORT).show();
		beforeText=s.toString();
		if(itemList.size()<4){
			itemList.add(s.toString());
			//Toast.makeText(AdvancedActivity.this, "集合大小="+itemList.size()+"添加的元素"+s.toString(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		//Toast.makeText(AdvancedActivity.this, "afterChange"+s.toString(), Toast.LENGTH_SHORT).show();
		if(beforeText.equals(s.toString())){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 txtView.setText("保存");
			 relativeLayout.setClickable(true);
			 txtView.setVisibility(View.VISIBLE);

		}else {
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 txtView.setText("保存");
			 relativeLayout.setClickable(false);
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		
		if(fistload==4){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 txtView.setText("保存");
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
					 txtView.setText("保存");
					 relativeLayout.setClickable(false);
					    txtView.setVisibility(View.INVISIBLE);
					    txtView.setClickable(false);
				}
			}
		}
		
	
	}
}
