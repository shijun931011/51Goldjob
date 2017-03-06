package com.example.user.a51goldjob.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.chyjr.goldjob.fr.utils.Gsons;
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
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.utils.StringUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

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

@ContentView(R.layout.activity_training_edit)
public class TrainingEditActivity extends BaseFormActivity implements OnClickListener,TextWatcher {

	@ViewInject(R.id.training_starttime_id)
	TextView training_starttime_id;

	@ViewInject(R.id.training_endtime_id)
	TextView training_endtime_id;

	@ViewInject(R.id.training_cert_id)
	EditText training_cert_id;

	@ViewInject(R.id.training_org_id)
	EditText training_org_id;
	
	@ViewInject(R.id.training_subject_id)
	EditText training_subject_id;
	
	@ViewInject(R.id.training_address_id)
	EditText training_address_id;

	@ViewInject(R.id.training_remark_id)
	TextView training_remark_id;

	WheelMain wheelMain;
	public String tag;
	
	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

	Handler handler = new Handler();

	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_textContentOnClick(v);
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
	
	private boolean isAddFromBtn=false;
	
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.training);
		// TODO Auto-generated method stub
		for(int i=0;i<7;i++){
			if(itemList.size()<=7){
				itemList.add("");
			}
		}
		
		init();
	}

	protected void actionPerformed_textContentOnClick(View v) {
		// TODO Auto-generated method stub
		Class<?> to = SubjectRemarkActivity.class;
		final String tag = (String) v.getTag();
		final TextView tv = (TextView) v;
		ActivityUtils.startActivityForResult(handler, TrainingEditActivity.this, to, new IIntentHandler() {
			@Override
			public void putExtra(Intent intent) {
				// TODO Auto-generated method stub
				intent.putExtra(SubjectRemarkActivity.TagContent, tag);
				intent.putExtra(
						SubjectRemarkActivity.CommonTextContent, tv
								.getText().toString());
			}
		});
	}

	private void init() {
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);
		
		TextView txtView=(TextView)this.findViewById(R.id.training_starttime_id);
		txtView.addTextChangedListener(this);
		txtView=(TextView)this.findViewById(R.id.training_endtime_id);
		txtView.addTextChangedListener(this);
		
		EditText editText=(EditText)this.findViewById(R.id.training_org_id);
		editText.addTextChangedListener(this);
		
		editText=(EditText)this.findViewById(R.id.training_address_id);
		editText.addTextChangedListener(this);
		
		editText=(EditText)this.findViewById(R.id.training_subject_id);
		editText.addTextChangedListener(this);
		
		editText=(EditText)this.findViewById(R.id.training_cert_id);
		editText.addTextChangedListener(this);
		
		txtView=(TextView)this.findViewById(R.id.training_remark_id);
		txtView.addTextChangedListener(this);
		
		
		// 查询
		String activeflag = getIntent().getExtras().getString("activeflag");
		String id = getIntent().getExtras().getString("id");
		if(id==null){
			id="";
		}
		if(id.equals("")){
		isAddFromBtn=true;	
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
					Transcodes.TC_TRAINING, false);
			Https.post(this, Transcodes.TC_TRAINING, data,
					new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
						protected void _onSuccess(ResponseInfo<String> response)
								throws Exception {
							loadForm(response);
						}
					});
		}

		// 开始时间控件绑定
		training_starttime_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(TrainingEditActivity.this);
				final View timepickerview = inflater.inflate(
						R.layout.timepicker, null);
				ScreenInfo screenInfo = new ScreenInfo(
						TrainingEditActivity.this);
				wheelMain = new WheelMain(timepickerview);
				wheelMain.screenheight = screenInfo.getHeight();
				String time = training_starttime_id.getText().toString();
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
				new AlertDialog.Builder(TrainingEditActivity.this)
						.setTitle("选择时间")
						.setView(timepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if(wheelMain.isAfterCurrent()){
											DialogUtils.commonInfo(TrainingEditActivity.this, TrainingEditActivity.this.getResources().getString(R.string.check_date));
											return;
										}
										training_starttime_id
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
		training_endtime_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(TrainingEditActivity.this);
				final View timepickerview = inflater.inflate(
						R.layout.timepicker, null);
				ScreenInfo screenInfo = new ScreenInfo(
						TrainingEditActivity.this);
				wheelMain = new WheelMain(timepickerview);
				wheelMain.screenheight = screenInfo.getHeight();
				String time = training_endtime_id.getText().toString();
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
				new AlertDialog.Builder(TrainingEditActivity.this)
						.setTitle("选择时间")
						.setView(timepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if(wheelMain.isAfterCurrent()){
											DialogUtils.commonInfo(TrainingEditActivity.this, TrainingEditActivity.this.getResources().getString(R.string.check_date));
											return;
										}
										training_endtime_id
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

		training_remark_id.setOnClickListener(onClickListener);
		
	}


	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		training_address_id.setText((String) map.get("address"));
		training_cert_id.setText((String) map.get("cert"));
		training_endtime_id.setText((String) map.get("endtime"));
		training_org_id.setText((String) map.get("org"));
		training_starttime_id.setText((String) map.get("starttime"));
		training_remark_id.setText((String) map.get("remark"));
		training_subject_id.setText((String) map.get("subject"));
	}


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			String tag = data.getStringExtra("RemarkBackStr");
		//	String textContent = (String) data.getStringExtra(CommonTextAreaActivity.CommonTextContent);
			training_remark_id.setText(tag);
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
		String trainid = getIntent().getExtras().getString("id");
		String activeflag = getIntent().getExtras().getString("activeflag");
		JsonUtils.put(jsonObject, "resumeid", resumeid);
		JsonUtils.put(jsonObject, "trainid", trainid);
		JsonUtils.put(jsonObject, "activeflag", activeflag);
		JsonUtils.put(jsonObject, "starttime", training_starttime_id
				.getText().toString()); // 开始时间
		String endtime = training_endtime_id.getText().toString();
		if(endtime==null||"".equals(endtime)){
			endtime = "至今";
		}
		JsonUtils.put(jsonObject, "endtime", endtime); 
		JsonUtils.put(jsonObject, "address", training_address_id.getText()
				.toString()); 
		JsonUtils.put(jsonObject, "org", training_org_id.getText()
				.toString()); 
		JsonUtils.put(jsonObject, "cert", training_cert_id.getText().toString()); 
		JsonUtils.put(jsonObject, "subject", training_subject_id.getText().toString()); 
		JsonUtils.put(jsonObject, "remark", training_remark_id.getText().toString()); 

		final String data = RequestData.getData(jsonObject,
				Transcodes.TC_TRAINING_SET, false);
		Https.post(this, Transcodes.TC_TRAINING_SET, data,
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

	//检查表单是否完整
	private String checkForm(){
		String reason = this.getResources().getString(R.string.check_form_no_whole);
		if(training_starttime_id.getText()==null||"".equals(training_starttime_id.getText().toString().trim())){
			return reason;
		}
		if(training_endtime_id.getText()!=null&&!"".equals(training_endtime_id.getText().toString().trim())){
			String startDate = training_starttime_id.getText().toString();
			String endDate = training_endtime_id.getText().toString();
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

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.title_left||v.getId()==R.id.topbar_return_btn){
			if(relativeLayout.isClickable()){ //可点击则为保存
				confirmMessage(TrainingEditActivity.this, true, "返回当前页面会丢失编辑内容是否要保存?");
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
		if(itemList.size()<7){
			itemList.add(s.toString());
		}
		
	}

	@Override
	public void afterTextChanged(Editable s) {
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
			 };
			 relativeLayout.setClickable(false);
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		
		if(fistload==7){
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
		if(itemList.size()==7){
			for(int i=0;i<7;i++){
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
