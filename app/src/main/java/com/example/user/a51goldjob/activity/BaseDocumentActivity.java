package com.example.user.a51goldjob.activity;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.user.a51goldjob.utils.DatePicker.JudgeDate;
import com.example.user.a51goldjob.utils.DatePicker.ScreenInfo;
import com.example.user.a51goldjob.utils.DatePicker.WheelMain;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.IDCardUtils;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.utils.StringUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;


@ContentView(R.layout.activity_basedocument)
public class BaseDocumentActivity extends BaseFormActivity implements TextWatcher,OnClickListener{
	private Button saveBtn;
	
	@ViewInject(R.id.basedocument_name_id)
	EditText basedocument_name_id; //姓名
	
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
	
	WheelMain wheelMain;
	
	private RelativeLayout relativeLayout;
	
	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	private int firstSetup=1;
	
	final int[] dictViewIds = {
			R.id.basedocument_sex_id, 
			R.id.basedocument_jobstatus_id,
			R.id.basedocument_workyears_id,
			R.id.basedocument_salary_id,
			R.id.basedocument_idtype_id
		};
	
	final String firstStr = "选择";
	final Map dictTitles=new HashMap();
	private final int msgs=1;
	private final int orginState=2;
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			//个人信息内容变化
			case msgs:
				 txtView.setVisibility(View.VISIBLE);
				 txtView.setText("保存");
				break;
			//个人信息经过编辑后没有变化	
			case orginState:
				txtView.setVisibility(View.INVISIBLE);
				break;
			default:
				break;
			}
		}
		
	};
	
	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {		
			actionPerformed_dictSelectorOnClick(v);
		}
	};

	List<String> defaultValue=null;
	private static TextView txtView;
	private static final TextView txtViews=txtView;
	private RelativeLayout relativeLayouts;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.base_document);
	    // TODO Auto-generated method stub
	    init();
	 //  defaultValue=getDefaultValue();
	  // Toast.makeText(BaseDocumentActivity.this,defaultValue.size(), Toast.LENGTH_SHORT).show();
	//    addTextWatchListener();
	   
	}

	private List<String> getDefaultValue(){
		List<String> tempValueList=new ArrayList<String>();
		EditText edittxt=(EditText)this.findViewById(R.id.basedocument_name_id);
		tempValueList.add(edittxt.getText().toString());
		TextView txtView=(TextView)this.findViewById(R.id.basedocument_sex_id);
		tempValueList.add(txtView.getText().toString());
		txtView=(TextView)this.findViewById(R.id.basedocument_borndate_id);
		tempValueList.add(txtView.toString());
		edittxt=(EditText)this.findViewById(R.id.basedocument_address_id);
		tempValueList.add(edittxt.getText().toString());
		edittxt=(EditText)this.findViewById(R.id.basedocument_telephone_id);
		tempValueList.add(edittxt.getText().toString());
		txtView=(TextView)this.findViewById(R.id.basedocument_jobstatus_id);
		tempValueList.add(txtView.getText().toString());
		txtView=(TextView)this.findViewById(R.id.basedocument_workyears_id);
		tempValueList.add(txtView.getText().toString());
		txtView=(TextView)this.findViewById(R.id.basedocument_salary_id);
		tempValueList.add(txtView.getText().toString());
		txtView=(TextView)this.findViewById(R.id.basedocument_idtype_id);
		tempValueList.add(txtView.getText().toString());
		edittxt=(EditText)this.findViewById(R.id.basedocument_id_id);
		tempValueList.add(edittxt.getText().toString());
		edittxt=(EditText)this.findViewById(R.id.basedocument_accountcityid_id);
		tempValueList.add(edittxt.getText().toString());
		return tempValueList;
	}
	private void addTextWatchListener(){
		basedocument_name_id.addTextChangedListener(this); 
		basedocument_address_id.addTextChangedListener(this);
		basedocument_telephone_id.addTextChangedListener(this);
		basedocument_id_id.addTextChangedListener(this);
		basedocument_accountcityid_id.addTextChangedListener(this);
	}
	private void init() {
		//给控件添加监听器，如果控件的值变了，那么执行
//		txtView=(TextView)this.findViewById(R.id.textViewRight);
//	    txtView.setText("保存");
//	    txtView.setVisibility(View.VISIBLE);
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);
		EditText editText=(EditText)this.findViewById(R.id.basedocument_name_id);
		editText.addTextChangedListener(this);
		TextView txtView=(TextView)this.findViewById(R.id.basedocument_sex_id);
		txtView.addTextChangedListener(this);
		txtView=(TextView)this.findViewById(R.id.basedocument_borndate_id);
		txtView.addTextChangedListener(this);
		editText=(EditText)this.findViewById(R.id.basedocument_address_id);
		editText.addTextChangedListener(this);
		editText=(EditText)this.findViewById(R.id.basedocument_telephone_id);
		editText.addTextChangedListener(this);
		txtView=(TextView)this.findViewById(R.id.basedocument_jobstatus_id);
		txtView.addTextChangedListener(this);
		txtView=(TextView)this.findViewById(R.id.basedocument_workyears_id);
		txtView.addTextChangedListener(this);
		txtView=(TextView)this.findViewById(R.id.basedocument_salary_id);
		txtView.addTextChangedListener(this);
		
		txtView=(TextView)this.findViewById(R.id.basedocument_idtype_id);
		txtView.addTextChangedListener(this);
		
		editText=(EditText)this.findViewById(R.id.basedocument_id_id);
		editText.addTextChangedListener(this);
		
		editText=(EditText)this.findViewById(R.id.basedocument_accountcityid_id);
		editText.addTextChangedListener(this);
		
		//基本资料查询
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		
		String resumeid = getIntent().getExtras().getString("resumeid");
		JsonUtils.put(jsonObject, "resumeid", resumeid); 
		final String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_VIEW, false);
		Https.post(this, Transcodes.TC_RESUME_VIEW, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				loadForm(response);
			}
		});
		
		//出生日期控件绑定
		basedocument_borndate_id.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					LayoutInflater inflater=LayoutInflater.from(BaseDocumentActivity.this);
					final View timepickerview=inflater.inflate(R.layout.timepicker, null);
					ScreenInfo screenInfo = new ScreenInfo(BaseDocumentActivity.this);
					wheelMain = new WheelMain(timepickerview);
					wheelMain.screenheight = screenInfo.getHeight();
				final 	String time = basedocument_borndate_id.getText().toString();
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
					new AlertDialog.Builder(BaseDocumentActivity.this)
					.setTitle("选择时间")
					.setView(timepickerview)
					.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if(wheelMain.isAfterCurrent()){
								DialogUtils.commonInfo(BaseDocumentActivity.this, BaseDocumentActivity.this.getResources().getString(R.string.check_date));
								return;
							}
							String selectTime=wheelMain.getTime();
							basedocument_borndate_id.setText(selectTime);
							if(!basedocument_borndate_id.equals(time)){
								Toast.makeText(BaseDocumentActivity.this,"前后日期选择不同", Toast.LENGTH_SHORT).show();
								
							}
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
	}
	
	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
		basedocument_name_id.setText((String) map.get("username"));
		basedocument_sex_id.setText((String)map.get("sex"));
		basedocument_borndate_id.setText((String)map.get("borndate"));
		basedocument_address_id.setText((String)map.get("address"));
		basedocument_telephone_id.setText((String)map.get("telephone"));
		basedocument_jobstatus_id.setText((String)map.get("jobstatus"));
		basedocument_workyears_id.setText((String)map.get("workyears"));
		basedocument_salary_id.setText((String)map.get("salary"));
		basedocument_idtype_id.setText((String)map.get("idtype"));
		basedocument_id_id.setText((String)map.get("id"));
		basedocument_accountcityid_id.setText((String)map.get("accountcityid"));
	}

	private String lastStr="";
	protected void actionPerformed_dictSelectorOnClick(View v) {

		final String dictKey = (String) v.getTag();
		final int dictId = v.getId();
		switch (dictId) {
		case R.id.basedocument_sex_id:
			TextView txtView=(TextView)this.findViewById(R.id.basedocument_sex_id);
			lastStr=txtView.getText().toString();
			break;
		case R.id.basedocument_jobstatus_id:
			TextView jobstatus_txt=(TextView)this.findViewById(R.id.basedocument_jobstatus_id);
			lastStr=jobstatus_txt.getText().toString();
			break;
		case R.id.basedocument_workyears_id:
			TextView workyear_txt=(TextView)this.findViewById(R.id.basedocument_workyears_id);
			lastStr=workyear_txt.getText().toString();
			break;
		case R.id.basedocument_salary_id:
			TextView salary_txt=(TextView)this.findViewById(R.id.basedocument_salary_id);
			lastStr=salary_txt.getText().toString();
			break;
		case R.id.basedocument_idtype_id:
			TextView idtype=(TextView)this.findViewById(R.id.basedocument_idtype_id);
			lastStr=idtype.getText().toString();
			break;
		default:
			break;
		}
		final String topTitle = firstStr + dictTitles.get(dictId);
		ActivityUtils.startActivityForResult(handler, this, DictSelectorActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY, dictKey); 
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_TOP_TITLE, topTitle);
				intent.putExtra(DictSelectorActivity.Constants.PARAM_IN_HEADER_TITLE, topTitle);
				intent.putExtra("selectStr",lastStr);
				
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
					String tempStr=data.getStringExtra(DictSelectorActivity.Constants.PARAM_OUT_SELECT_ITEM);
					if(!tempStr.equals(lastStr)){
						txtView.setVisibility(View.INVISIBLE);
					}
					view.setText(data.getStringExtra(DictSelectorActivity.Constants.PARAM_OUT_SELECT_ITEM));
					
				}
			}
			
		}
	}

	protected int getSubmitTextStringId() {
		return R.string.save;
	}

	//修改提交
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
		JsonUtils.put(jsonObject, "resumeid", resumeid); 
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
		
		final String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_SET_ISOPEN, false);
		Https.post(this, Transcodes.TC_RESUME_SET_ISOPEN, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		});
	}
	
	private boolean isFirstLoad=false;
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		canShow=true;
	
		
	}

	//检查表单是否完整
	private String checkForm(){
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
		//验证手机号
		if(!StringUtils.isMobile(basedocument_telephone_id.getText().toString().trim())){
			reason = this.getResources().getString(R.string.check_telephone);
			return reason;
		}
		//验证身份证
		reason = IDCardUtils.IDCardValidate(basedocument_id_id.getText().toString().trim(),basedocument_borndate_id.getText().toString().trim());
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

	private boolean canShow=false;
	private String beforeText="";
	private String orignText="";
	private String tempst="";
	private String tempst2="";
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		tempst+=s;
		tempst2=s.toString();

			}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

		beforeText=s.toString();
		if(itemList.size()<11){
			itemList.add(s.toString());
//			Toast.makeText(BaseDocumentActivity.this, "集合大小="+itemList.size()+"添加的元素"+s.toString(), Toast.LENGTH_SHORT).show();
		}

		
		
	}

	private int fistload=1;
	private int itemlength=1;
	private List<String> itemList=new ArrayList<String>();
	@Override
	public void afterTextChanged(Editable s) {
		
		if(beforeText.equals(s.toString())){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 relativeLayout.setClickable(true);
			    txtView.setText("保存");
			    txtView.setVisibility(View.VISIBLE);

		}else {
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 relativeLayout.setClickable(false);
			    txtView.setText("保存");
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		
		if(fistload==11){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);;
			 relativeLayout.setClickable(false);
			    txtView.setText("保存");
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		fistload=fistload+1;
		if(itemList.size()==11){
			for(int i=0;i<11;i++){
				if(itemList.get(i).equals(s.toString())){
				//	Toast.makeText(BaseDocumentActivity.this,itemList.get(i)+"#"+s.toString(), Toast.LENGTH_SHORT).show();
					 txtView=(TextView)this.findViewById(R.id.textViewRight);
					 relativeLayout.setClickable(false);
					    txtView.setText("保存");
					    txtView.setVisibility(View.INVISIBLE);
					    txtView.setClickable(false);
				}
			}
		}
		
		
	}

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.title_left||v.getId()==R.id.topbar_return_btn){
			if(relativeLayout.isClickable()){ //可点击则为保存
				confirmMessage(BaseDocumentActivity.this, true, "返回当前页面会丢失编辑内容是否要保存?");
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
	
}
