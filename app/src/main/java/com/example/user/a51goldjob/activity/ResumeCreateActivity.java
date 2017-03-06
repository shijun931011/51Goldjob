package com.example.user.a51goldjob.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.chyjr.goldjob.fr.utils.BindUtils;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.DatePicker.JudgeDate;
import com.example.user.a51goldjob.utils.DatePicker.ScreenInfo;
import com.example.user.a51goldjob.utils.DatePicker.WheelMain;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.IDCardUtils;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.utils.StringUtils;
import com.example.user.a51goldjob.view.PublicViews;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * 创建简历
 * @author yeq
 *
 */
@ContentView(R.layout.activity_basedocument)
public class ResumeCreateActivity extends BaseFormActivity {

	@ViewInject(R.id.row_resume_name_id)
	TableRow row_resume_name_id;
	
	@ViewInject(R.id.row_resume_isopen_id)
	TableRow row_resume_isopen_id;
	
	@ViewInject(R.id.resume_name_id)
	EditText resume_name_id;
	
	@ViewInject(R.id.basedocument_name_id)
	EditText basedocument_name_id;
	
	@ViewInject(R.id.basedocument_sex_id)
	TextView basedocument_sex_id;
	
	@ViewInject(R.id.basedocument_borndate_id)
	TextView basedocument_borndate_id;
	
	@ViewInject(R.id.basedocument_address_id)
	EditText basedocument_address_id;
	
	@ViewInject(R.id.basedocument_telephone_id)
	EditText basedocument_telephone_id;
	
	@ViewInject(R.id.basedocument_jobstatus_id)
	TextView basedocument_jobstatus_id;
	
	@ViewInject(R.id.basedocument_workyears_id)
	TextView basedocument_workyears_id;
	
	@ViewInject(R.id.basedocument_salary_id)
	TextView basedocument_salary_id;
	
	@ViewInject(R.id.basedocument_idtype_id)
	TextView basedocument_idtype_id;
	
	@ViewInject(R.id.basedocument_id_id)
	EditText basedocument_id_id;
	
	@ViewInject(R.id.basedocument_accountcityid_id)
	EditText basedocument_accountcityid_id;
	
	@ViewInject(R.id.resume_isopen_id)
	TextView resume_isopen_id;
	
	WheelMain wheelMain;
	
	
	
	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	final int[] dictViewIds = {
			R.id.basedocument_sex_id, 
			R.id.basedocument_jobstatus_id,
			R.id.basedocument_workyears_id,
			R.id.basedocument_salary_id,
			R.id.basedocument_idtype_id,
			R.id.resume_isopen_id
		};
	
	final String firstStr = "选择";
	final Map dictTitles=new HashMap();
	
	Handler handler = new Handler();
	
	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_dictSelectorOnClick(v);
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.resume_create);
	    RelativeLayout relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
	    TextView txtView= PublicViews.createMenuTextView(this, "保存",Color.WHITE, null);
	    init();
	    if(txtView!=null){
	    	relativeLayout.setVisibility(View.VISIBLE);
	    	txtView.setText("保存");
	    	txtView.setVisibility(View.VISIBLE);
	    }
	    
	   
	}

	private void init() {
	    boolean isCreate = (Boolean) getIntent().getExtras().get("IsCreate");
		if(isCreate){
			row_resume_name_id.setVisibility(View.VISIBLE);
			row_resume_isopen_id.setVisibility(View.VISIBLE);
		}
		//出生日期控件绑定
		basedocument_borndate_id.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					LayoutInflater inflater=LayoutInflater.from(ResumeCreateActivity.this);
					final View timepickerview=inflater.inflate(R.layout.timepicker, null);
					ScreenInfo screenInfo = new ScreenInfo(ResumeCreateActivity.this);
					wheelMain = new WheelMain(timepickerview);
					wheelMain.screenheight = screenInfo.getHeight();
					String time = basedocument_borndate_id.getText().toString();
					Calendar calendar = Calendar.getInstance();
					if(JudgeDate.isDate(time, "yyyy-MM-dd")){
						try {
							calendar.setTime(dateFormat.parse(time));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH);
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					wheelMain.initDateTimePicker(year,month,day);
					new AlertDialog.Builder(ResumeCreateActivity.this)
					.setTitle("选择时间")
					.setView(timepickerview)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(wheelMain.isAfterCurrent()){
								DialogUtils.commonInfo(ResumeCreateActivity.this, ResumeCreateActivity.this.getResources().getString(R.string.check_date));
								return;
							}
							basedocument_borndate_id.setText(wheelMain.getTime());
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					})
					.show();
				}
			
		});
		
		//数据字典选项绑定
		bindDictTitle();
		BindUtils.bindCtrlOnClickListener(this, onClickListener, dictViewIds);
	}
	
	/**
	 * 绑定id和显示TITLE
	 */
	private void bindDictTitle(){
		dictTitles.put(R.id.basedocument_sex_id, this.getString(R.string.base_sex));
		dictTitles.put(R.id.basedocument_jobstatus_id, this.getString(R.string.base_jobstatus));
		dictTitles.put(R.id.basedocument_workyears_id, this.getString(R.string.base_workyears));
		dictTitles.put(R.id.basedocument_salary_id, this.getString(R.string.base_salary));
		dictTitles.put(R.id.basedocument_idtype_id, this.getString(R.string.base_idtype));
		dictTitles.put(R.id.resume_isopen_id, this.getString(R.string.resume_isopen));
	}
	
	protected void actionPerformed_dictSelectorOnClick(View v) {
		final String dictKey = (String) v.getTag();
		final int dictId = v.getId();
		final String topTitle = firstStr + dictTitles.get(dictId);
		ActivityUtils.startActivityForResult(handler, this, DictSelectorActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY, dictKey); 
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_TOP_TITLE, topTitle);
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_HEADER_TITLE, topTitle);
			};
		});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 带回数据初始化界面
		if (resultCode == RESULT_OK) {
			String dictKey = data.getStringExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY);

			TextView view = null;
			for (int i=0; i<dictViewIds.length; i++) {
				view = (TextView) findViewById(dictViewIds[i]);
				if (((String)view.getTag()).equals(dictKey)) {
					view.setText(data.getStringExtra(DictSelectorActivity.Constants.PARAM_OUT_SELECT_ITEM));
				}
			}
			
		}
	}

	protected void actionPerformed_submitOnClick(View v) {
		boolean isCreate = (Boolean) getIntent().getExtras().get("IsCreate");
		String reason = checkForm(isCreate);
		if(!StringUtils.isEmpty(reason)){
			DialogUtils.commonInfo(this, reason);
			return;
		}
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		
//		String resumeid = IntentUtils.getStringExtra(this, ParamsIn.RESUME_ID);
//		JsonUtils.put(jsonObject, "resumeid", resumeid); 
		JsonUtils.put(jsonObject, "username", basedocument_name_id.getText().toString()); // 姓名
		JsonUtils.put(jsonObject, "sex", basedocument_sex_id.getText().toString()); // 性别
		JsonUtils.put(jsonObject, "workyears", basedocument_workyears_id.getText().toString()); // 工作年限
		JsonUtils.put(jsonObject, "borndate", basedocument_borndate_id.getText().toString()); // 出生日期
		JsonUtils.put(jsonObject, "telephone", basedocument_telephone_id.getText().toString()); // 手机
		JsonUtils.put(jsonObject, "idtype", basedocument_idtype_id.getText().toString()); // 证件类型
		JsonUtils.put(jsonObject, "jobstatus", basedocument_jobstatus_id.getText().toString()); // 求职状态
		JsonUtils.put(jsonObject, "id", basedocument_id_id.getText().toString()); // 身份证号码
		JsonUtils.put(jsonObject, "accountcityid", basedocument_accountcityid_id.getText().toString()); // 户籍地址
		JsonUtils.put(jsonObject, "address", basedocument_address_id.getText().toString()); // 居住地址
		JsonUtils.put(jsonObject, "salary", basedocument_salary_id.getText().toString()); // 目前年薪
		
		if(isCreate){
			JsonUtils.put(jsonObject, "resumename", resume_name_id.getText().toString()); 
			JsonUtils.put(jsonObject, "companyid", resume_isopen_id.getText().toString()); 
			final String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_CREATE, false);
			Https.post(this, Transcodes.TC_RESUME_CREATE, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
				protected void _onSuccess(ResponseInfo<String> response) throws Exception {
					doSuccess(response);
				}
			});
		}else{
			final String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_SET_ISOPEN, false);
			Https.post(this, Transcodes.TC_RESUME_SET_ISOPEN, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
				protected void _onSuccess(ResponseInfo<String> response) throws Exception {
					doSuccess(response);
				}
			});
		}
	}
	
	//检查表单是否完整
		private String checkForm(boolean isCreate){
			String reason = this.getResources().getString(R.string.check_form_no_whole);
			if(basedocument_name_id.getText()==null||"".equals(basedocument_name_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_sex_id.getText()==null||"".equals(basedocument_sex_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_workyears_id.getText()==null||"".equals(basedocument_workyears_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_borndate_id.getText()==null||"".equals(basedocument_borndate_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_telephone_id.getText()==null||"".equals(basedocument_telephone_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_idtype_id.getText()==null||"".equals(basedocument_idtype_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_jobstatus_id.getText()==null||"".equals(basedocument_jobstatus_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_id_id.getText()==null||"".equals(basedocument_id_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_accountcityid_id.getText()==null||"".equals(basedocument_accountcityid_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_address_id.getText()==null||"".equals(basedocument_address_id.getText().toString().trim())){
				return reason;
			}else if(basedocument_salary_id.getText()==null||"".equals(basedocument_salary_id.getText().toString().trim())){
				return reason;
			}
			if(isCreate){
				if(resume_name_id.getText()==null||"".equals(resume_name_id.getText().toString().trim())){
					return reason;
				}else if(resume_isopen_id.getText()==null||"".equals(resume_isopen_id.getText().toString().trim())){
					return reason;
				}
			}
			//验证手机号
			if(!StringUtils.isMobile(basedocument_telephone_id.getText().toString().trim())){
				reason = this.getResources().getString(R.string.check_telephone);
				return reason;
			}
			//验证身份证
			reason = IDCardUtils.IDCardValidate(basedocument_id_id.getText().toString().trim(),basedocument_borndate_id.getText().toString());
			if(!"".equals(reason)){
				return reason;
			}
			reason = "";
			return reason;
		}
	
	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		ActivityUtils.showMessage(this, responseData.isSuccess(), responseData.getReturnMsg());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
