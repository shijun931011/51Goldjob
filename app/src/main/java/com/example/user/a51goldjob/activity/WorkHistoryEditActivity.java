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

import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.BindUtils;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.IntentUtils;
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
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.utils.StringUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_work_history_edit)
public class WorkHistoryEditActivity extends BaseFormActivity implements TextWatcher,OnClickListener{

	@ViewInject(R.id.workhistory_starttime_id)
	TextView workhistory_starttime_id;

	@ViewInject(R.id.workhistory_endtime_id)
	TextView workhistory_endtime_id;

	@ViewInject(R.id.workhistory_corpname_id)
	EditText workhistory_corpname_id;

	@ViewInject(R.id.workhistory_industry_id)
	TextView workhistory_industry_id;

	@ViewInject(R.id.workhistory_corpscale_id)
	TextView workhistory_corpscale_id;

	@ViewInject(R.id.workhistory_corptype_id)
	TextView workhistory_corptype_id;

	@ViewInject(R.id.workhistory_department_id)
	EditText workhistory_department_id;

	@ViewInject(R.id.workhistory_postcode_id)
	EditText workhistory_postcode_id;

	@ViewInject(R.id.workhistory_workremark_id)
	TextView workhistory_workremark_id;

	WheelMain wheelMain;

	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

	final int[] dictViewIds = { R.id.workhistory_industry_id,
			R.id.workhistory_corpscale_id, R.id.workhistory_corptype_id };

	final String firstStr = "选择";
	final Map dictTitles = new HashMap();

	Handler handler = new Handler();

	
	
	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_dictSelectorOnClick(v);
		}
	};

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
	private String btnStr="";
	private String lastStr="";
	private boolean isClickBtn=false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.work_history);
		// TODO Auto-generated method stub
	//	btnStr=(String)getIntent().getExtras().get("btnclick");
		init();
	}

	private void init() {
		
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);
		
		TextView txtView=(TextView)this.findViewById(R.id.workhistory_starttime_id);
		txtView.addTextChangedListener(this);
		
		 txtView=(TextView)this.findViewById(R.id.workhistory_endtime_id);
			txtView.addTextChangedListener(this);
		
			 txtView=(TextView)this.findViewById(R.id.workhistory_industry_id);
				txtView.addTextChangedListener(this);
		
				txtView=(TextView)this.findViewById(R.id.workhistory_corpscale_id);
				txtView.addTextChangedListener(this);
				txtView=(TextView)this.findViewById(R.id.workhistory_corptype_id);
				txtView.addTextChangedListener(this);
				txtView=(TextView)this.findViewById(R.id.workhistory_workremark_id);
				txtView.addTextChangedListener(this);
		
		EditText edittext=(EditText)this.findViewById(R.id.workhistory_corpname_id);
		edittext.addTextChangedListener(this);
		edittext=(EditText)this.findViewById(R.id.workhistory_department_id);
		edittext.addTextChangedListener(this);
		edittext=(EditText)this.findViewById(R.id.workhistory_postcode_id);
		edittext.addTextChangedListener(this);
		
		
		// 基本资料查询
		String activeflag = IntentUtils.getStringExtra(this, "activeflag");
		String id = IntentUtils.getStringExtra(this, "id");
		if(id.equals("")){
			isClickBtn=true;
		}
		if (id != null && !"".equals(id)) {
			Token token = AppContext.get(this).getToken();
			JSONObject jsonObject = new JSONObject();
			JsonUtils.put(jsonObject, "token", token.getToken());
			JsonUtils.put(jsonObject, "userid", token.getUserId());

			String resumeid = getIntent().getExtras().getString("resumeid");
			JsonUtils.put(jsonObject, "resumeid", resumeid);
			JsonUtils.put(jsonObject, "id", id);
			final String data = RequestData.getData(jsonObject,
					Transcodes.TC_WORK_HISTORY, false);
			Https.post(this, Transcodes.TC_WORK_HISTORY, data,
					new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
						protected void _onSuccess(ResponseInfo<String> response)
								throws Exception {
							loadForm(response);
						}
					});
		}

		// 开始时间控件绑定
		workhistory_starttime_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(WorkHistoryEditActivity.this);
				final View timepickerview = inflater.inflate(
						R.layout.timepicker, null);
				ScreenInfo screenInfo = new ScreenInfo(
						WorkHistoryEditActivity.this);
				wheelMain = new WheelMain(timepickerview);
				wheelMain.screenheight = screenInfo.getHeight();
				String time = workhistory_starttime_id.getText().toString();
				Calendar calendar = Calendar.getInstance();
				if (JudgeDate.isDate(time, "yyyy-MM")) {
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
				wheelMain.initDateTimePicker(year, month, day);
				new AlertDialog.Builder(WorkHistoryEditActivity.this)
						.setTitle("选择时间")
						.setView(timepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if(wheelMain.isAfterCurrent()){
											DialogUtils.commonInfo(WorkHistoryEditActivity.this, WorkHistoryEditActivity.this.getResources().getString(R.string.check_date));
											return;
										}
										workhistory_starttime_id
												.setText(wheelMain.getMonth());
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}

		});

		// 结束时间控件绑定
		workhistory_endtime_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(WorkHistoryEditActivity.this);
				final View timepickerview = inflater.inflate(
						R.layout.timepicker, null);
				ScreenInfo screenInfo = new ScreenInfo(
						WorkHistoryEditActivity.this);
				wheelMain = new WheelMain(timepickerview);
				wheelMain.screenheight = screenInfo.getHeight();
				String time = workhistory_endtime_id.getText().toString();
				Calendar calendar = Calendar.getInstance();
				if (JudgeDate.isDate(time, "yyyy-MM")) {
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
				wheelMain.initDateTimePicker(year, month, day);
				new AlertDialog.Builder(WorkHistoryEditActivity.this)
						.setTitle("选择时间")
						.setView(timepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if(wheelMain.isAfterCurrent()){
											DialogUtils.commonInfo(WorkHistoryEditActivity.this, WorkHistoryEditActivity.this.getResources().getString(R.string.check_date));
											return;
										}
										workhistory_endtime_id
												.setText(wheelMain.getMonth());
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}

		});

		workhistory_workremark_id.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Class<?> to = CommonTextAreaActivity.class;
//				final String tag = (String) v.getTag();
//				final TextView tv = (TextView) v;
//				ActivityUtils.startActivityForResult(handler, WorkHistoryEditActivity.this, to, new IIntentHandler() {
//					@Override
//					public void putExtra(Intent intent) {
//						// TODO Auto-generated method stub
//						intent.putExtra(CommonTextAreaActivity.TagContent, tag);
//						intent.putExtra(
//								CommonTextAreaActivity.CommonTextContent, tv
//										.getText().toString());
//					}
//				});
				TextView selfTxtView=(TextView)WorkHistoryEditActivity.this.findViewById(R.id.workhistory_workremark_id);
				lastStr=selfTxtView.getText().toString();
				Intent intent=new Intent(WorkHistoryEditActivity.this,WorkDescribeActivity.class);
				intent.putExtra("WorkDesc",lastStr);
				startActivityForResult(intent, 105);
				return;
				
			}
			
		});
		
		
		// 数据字典选项绑定
		bindDictTitle();
		BindUtils.bindCtrlOnClickListener(this, onClickListener, dictViewIds);
	}

	/**
	 * 绑定id和显示TITLE
	 */
	private void bindDictTitle() {
		dictTitles.put(R.id.workhistory_industry_id,
				this.getString(R.string.industry));
		dictTitles.put(R.id.workhistory_corpscale_id,
				this.getString(R.string.corpscale));
		dictTitles.put(R.id.workhistory_corptype_id,
				this.getString(R.string.corptype));
	}

	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		workhistory_corpname_id.setText((String) map.get("corpname"));
		workhistory_starttime_id.setText((String) map.get("starttime"));
		workhistory_endtime_id.setText((String) map.get("endtime"));
		workhistory_industry_id.setText((String) map.get("industry"));
		workhistory_corpscale_id.setText((String) map.get("corpscale"));
		workhistory_corptype_id.setText((String) map.get("corptype"));
		workhistory_department_id.setText((String) map.get("department"));
		workhistory_workremark_id.setText((String) map.get("workremark"));
		workhistory_postcode_id.setText((String) map.get("postcode"));
//if(workhistory_corpname_id.getText().toString().equals("")
//		||workhistory_starttime_id.getText().toString().equals("")
//		||workhistory_endtime_id.getText().toString().equals("")
//		||workhistory_industry_id.getText().toString().equals("")
//		||workhistory_corpscale_id.getText().toString().equals("")
//		||workhistory_corptype_id.getText().toString().equals("")
//		||workhistory_department_id.getText().toString().equals("")
//		||workhistory_postcode_id.getText().toString().equals("")
//		){
//	isClickBtn=true;
//	
//}
	}
    private String selectedStr="";
	protected void actionPerformed_dictSelectorOnClick(View v) {
		final String dictKey = (String) v.getTag();
		final int dictId = v.getId();
		if(dictId==R.id.workhistory_industry_id
				||dictId==R.id.workhistory_corpscale_id
				||dictId==R.id.workhistory_corptype_id){
			TextView selectedTxtView=(TextView)v;
			selectedStr=selectedTxtView.getText().toString();
		}
		final String topTitle = firstStr + dictTitles.get(dictId);
		ActivityUtils.startActivityForResult(handler, this,
				DictSelectorActivity.class, new IIntentHandler() {
					public void putExtra(Intent intent) {
						intent.putExtra(
								DictSelectorActivity.Constants.PARAM_IO_DICT_KEY,
								dictKey);
						intent.putExtra(
								DictSelectorActivity.Constants.PARAM_IN_TOP_TITLE,
								topTitle);
						intent.putExtra(
								DictSelectorActivity.Constants.PARAM_IN_HEADER_TITLE,
								topTitle);
						intent.putExtra("selectStr", selectedStr);
					};
				});
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 带回数据初始化界面
		if (resultCode == RESULT_OK) {
			String dictKey = data
					.getStringExtra(DictSelectorActivity.Constants.PARAM_IO_DICT_KEY);

			TextView view = null;
			for (int i = 0; i < dictViewIds.length; i++) {
				view = (TextView) findViewById(dictViewIds[i]);
				if (((String) view.getTag()).equals(dictKey)) {
					view.setText(data
							.getStringExtra(DictSelectorActivity.Constants.PARAM_OUT_SELECT_ITEM));
				}
			}

			if(data!=null){
				String workremark = (String) data.getStringExtra(CommonTextAreaActivity.CommonTextContent);
				workhistory_workremark_id.setText(workremark);
			}
			
			 
		}else if(resultCode==105){
			String remarkStr=data.getStringExtra("RemarkBackStr");
			TextView selfTxtView=(TextView)this.findViewById(R.id.workhistory_workremark_id);
			selfTxtView.setText(remarkStr);
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
		String workid = getIntent().getExtras().getString("id");
		String activeflag = getIntent().getExtras().getString("activeflag");
		JsonUtils.put(jsonObject, "resumeid", resumeid);
		JsonUtils.put(jsonObject, "workid", workid);
		JsonUtils.put(jsonObject, "activeflag", activeflag);
		JsonUtils.put(jsonObject, "starttime", workhistory_starttime_id
				.getText().toString()); // 开始时间
		String endtime = workhistory_endtime_id.getText().toString();
		if(endtime==null||"".equals(endtime)){
			endtime = "至今";
		}
		JsonUtils.put(jsonObject, "endtime", endtime); 
		JsonUtils.put(jsonObject, "corpname", workhistory_corpname_id.getText()
				.toString()); // 公司
		JsonUtils.put(jsonObject, "corptype", workhistory_corptype_id.getText()
				.toString()); // 公司性质
		JsonUtils.put(jsonObject, "corpscale", workhistory_corpscale_id
				.getText().toString()); // 公司规模
		JsonUtils.put(jsonObject, "industry", workhistory_industry_id.getText()
				.toString()); // 行业
		JsonUtils.put(jsonObject, "department", workhistory_department_id
				.getText().toString()); // 部门
		JsonUtils.put(jsonObject, "postcode", workhistory_postcode_id.getText()
				.toString()); // 职能
		JsonUtils.put(jsonObject, "workremark", workhistory_workremark_id
				.getText().toString()); // 工作职责

		final String data = RequestData.getData(jsonObject,
				Transcodes.TC_WORK_HISTORY_SET, false);
		Https.post(this, Transcodes.TC_WORK_HISTORY_SET, data,
				new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
					protected void _onSuccess(ResponseInfo<String> response)
							throws Exception {
						doSuccess(response);
					}
				});
	}

	// 检查表单是否完整
	private String checkForm() {
		String reason = this.getResources().getString(R.string.check_form_no_whole);
		if (workhistory_starttime_id.getText() == null
				|| "".equals(workhistory_starttime_id.getText().toString()
						.trim())) {
			return reason;
		} else if (workhistory_corpname_id.getText() == null
				|| "".equals(workhistory_corpname_id.getText().toString()
						.trim())) {
			return reason;
		} else if (workhistory_corptype_id.getText() == null
				|| "".equals(workhistory_corptype_id.getText().toString()
						.trim())) {
			return reason;
		} else if (workhistory_corpscale_id.getText() == null
				|| "".equals(workhistory_corpscale_id.getText().toString()
						.trim())) {
			return reason;
		} else if (workhistory_industry_id.getText() == null
				|| "".equals(workhistory_industry_id.getText().toString()
						.trim())) {
			return reason;
		} else if (workhistory_department_id.getText() == null
				|| "".equals(workhistory_department_id.getText().toString()
						.trim())) {
			return reason;
		} else if (workhistory_postcode_id.getText() == null
				|| "".equals(workhistory_postcode_id.getText().toString()
						.trim())) {
			return reason;
		}
		if(workhistory_endtime_id.getText() != null
				&& !"".equals(workhistory_endtime_id.getText().toString()
						.trim())){
			String startDate = workhistory_starttime_id.getText().toString();
			String endDate = workhistory_endtime_id.getText().toString();
			DateFormat df = new SimpleDateFormat("yyyy-MM");
			try {
				boolean flag = StringUtils.compareDate(df.parse(startDate), df.parse(endDate));
				if(flag==true){
					reason = this.getResources().getString(R.string.check_begin_end_date);
					return reason;
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		reason = "";
		return reason;
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
				confirmMessage(WorkHistoryEditActivity.this, true, "返回当前页面会丢失编辑内容是否要保存?");
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
		if(itemList.size()<9){
			itemList.add(s.toString());
		//	Toast.makeText(WorkHistoryEditActivity.this, "集合大小="+itemList.size()+"添加的元素"+s.toString(), Toast.LENGTH_SHORT).show();
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) {
		//Toast.makeText(AdvancedActivity.this, s.toString(), Toast.LENGTH_SHORT).show();
		if(beforeText.equals(s.toString())){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isClickBtn==true){
				 txtView.setText("添加");
			 }else{
				 txtView.setText("保存");
			 }
			 relativeLayout.setClickable(true);
			    txtView.setVisibility(View.VISIBLE);

		}else {
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isClickBtn==true){
				 txtView.setText("添加");
			 }else{
				 txtView.setText("保存");
			 }
			 relativeLayout.setClickable(false);
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		
		if(fistload==9){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isClickBtn==true){
				 txtView.setText("添加");
			 }else{
				 txtView.setText("保存");
			 }
			 relativeLayout.setClickable(false);
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		fistload=fistload+1;
		if(itemList.size()==9){
			for(int i=0;i<9;i++){
				if(itemList.get(i).equals(s.toString())){
				//	Toast.makeText(BaseDocumentActivity.this,itemList.get(i)+"#"+s.toString(), Toast.LENGTH_SHORT).show();
					 txtView=(TextView)this.findViewById(R.id.textViewRight);
					 if(isClickBtn==true){
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
//						if(btnStr.equals("btnclick")){
//							 txtView.setText("添加");
//						 }else{
//							 txtView.setText("保存");
//						 }
						
						confirmBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								activity.finish();
							}
						});
						cancelBtn.setText("保存");
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
