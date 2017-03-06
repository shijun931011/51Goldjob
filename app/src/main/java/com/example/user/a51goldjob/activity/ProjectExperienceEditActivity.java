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

@ContentView(R.layout.activity_project_experience_edit)
public class ProjectExperienceEditActivity extends BaseFormActivity implements OnClickListener,TextWatcher {

	@ViewInject(R.id.projectexperience_starttime_id)
	TextView projectexperience_starttime_id;

	@ViewInject(R.id.projectexperience_endtime_id)
	TextView projectexperience_endtime_id;

	@ViewInject(R.id.projectexperience_projectname_id)
	EditText projectexperience_projectname_id;

	@ViewInject(R.id.projectexperience_companyname_id)
	EditText projectexperience_companyname_id;

	@ViewInject(R.id.projectexperience_projectremark_id)
	TextView projectexperience_projectremark_id; //项目描述

	@ViewInject(R.id.projectexperience_trustremark_id)
	TextView projectexperience_trustremark_id; //项目职责

	WheelMain wheelMain;
	public String tag;

	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

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
	private String beforeText="";
	private String orignText="";
	private String tempst="";
	private String tempst2="";
	
	private boolean isAddFromBtn=false;
	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_textContentOnClick(v);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.project_experience);
		// TODO Auto-generated method stub
		init();
	}

	protected void actionPerformed_textContentOnClick(View v) {
		if(v.getId()==R.id.projectexperience_trustremark_id){ //项目职责
			Class<?> to = ProjectTaskActivity.class;
			tag = (String) v.getTag();
			final TextView tv = (TextView) v;
			ActivityUtils.startActivityForResult(handler,
					ProjectExperienceEditActivity.this, to, new IIntentHandler() {
						@Override
						public void putExtra(Intent intent) {
							// TODO Auto-generated method stub
							intent.putExtra(ProjectTaskActivity.TagContent, tag);
							intent.putExtra(ProjectTaskActivity.CommonTextContent, tv.getText().toString());
						}
					});
			
			return;
		}
		// TODO Auto-generated method stub
		Class<?> to = ProjectRemarkActivity.class;
		tag = (String) v.getTag();
		final TextView tv = (TextView) v;
		ActivityUtils.startActivityForResult(handler,
				ProjectExperienceEditActivity.this, to, new IIntentHandler() {
					@Override
					public void putExtra(Intent intent) {
						// TODO Auto-generated method stub
						intent.putExtra(ProjectRemarkActivity.TagContent, tag);
						intent.putExtra(ProjectRemarkActivity.CommonTextContent, tv.getText().toString());
					}
				});
	}

	private void init() {
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);
		
		TextView txtView=(TextView)this.findViewById(R.id.projectexperience_starttime_id);
		txtView.addTextChangedListener(this);
		
		txtView=(TextView)this.findViewById(R.id.projectexperience_endtime_id);
		txtView.addTextChangedListener(this);
		
		txtView=(TextView)this.findViewById(R.id.projectexperience_trustremark_id);
		txtView.addTextChangedListener(this);
		EditText editext=(EditText)this.findViewById(R.id.projectexperience_projectname_id);
		editext.addTextChangedListener(this);
		editext=(EditText)this.findViewById(R.id.projectexperience_companyname_id);
		editext.addTextChangedListener(this);
		
		txtView=(TextView)this.findViewById(R.id.projectexperience_projectremark_id);
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
					Transcodes.TC_PROJECT_EXPERIENCE, false);
			Https.post(this, Transcodes.TC_PROJECT_EXPERIENCE, data,
					new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
						protected void _onSuccess(ResponseInfo<String> response)
								throws Exception {
							loadForm(response);
						}
					});
		}

		// 开始时间控件绑定
		projectexperience_starttime_id
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						LayoutInflater inflater = LayoutInflater
								.from(ProjectExperienceEditActivity.this);
						final View timepickerview = inflater.inflate(
								R.layout.timepicker, null);
						ScreenInfo screenInfo = new ScreenInfo(
								ProjectExperienceEditActivity.this);
						wheelMain = new WheelMain(timepickerview);
						wheelMain.screenheight = screenInfo.getHeight();
						String time = projectexperience_starttime_id.getText()
								.toString();
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
						new AlertDialog.Builder(
								ProjectExperienceEditActivity.this)
								.setTitle("选择时间")
								.setView(timepickerview)
								.setPositiveButton("确定",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												if(wheelMain.isAfterCurrent()){
													DialogUtils.commonInfo(ProjectExperienceEditActivity.this, ProjectExperienceEditActivity.this.getResources().getString(R.string.check_date));
													return;
												}
												projectexperience_starttime_id
														.setText(wheelMain
																.getMonth());
											}
										})
								.setNegativeButton("取消",
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
											}
										}).show();
					}

				});

		// 结束时间控件绑定
		projectexperience_endtime_id.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater = LayoutInflater
						.from(ProjectExperienceEditActivity.this);
				final View timepickerview = inflater.inflate(
						R.layout.timepicker, null);
				ScreenInfo screenInfo = new ScreenInfo(
						ProjectExperienceEditActivity.this);
				wheelMain = new WheelMain(timepickerview);
				wheelMain.screenheight = screenInfo.getHeight();
				String time = projectexperience_endtime_id.getText().toString();
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
				new AlertDialog.Builder(ProjectExperienceEditActivity.this)
						.setTitle("选择时间")
						.setView(timepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if(wheelMain.isAfterCurrent()){
											DialogUtils.commonInfo(ProjectExperienceEditActivity.this, ProjectExperienceEditActivity.this.getResources().getString(R.string.check_date));
											return;
										}
										projectexperience_endtime_id
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

		projectexperience_projectremark_id.setOnClickListener(onClickListener);

		projectexperience_trustremark_id.setOnClickListener(onClickListener);

	}

	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		projectexperience_projectname_id.setText((String) map
				.get("projectname"));
		projectexperience_companyname_id.setText((String) map
				.get("companyname"));
		projectexperience_starttime_id.setText((String) map.get("starttime"));
		projectexperience_endtime_id.setText((String) map.get("endtime"));
		projectexperience_projectremark_id.setText((String) map
				.get("projectremark"));
		projectexperience_trustremark_id.setText((String) map
				.get("trustremark"));
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data != null) {
			String tag = data.getStringExtra("RemarkBackStr");
			//String textContent = (String) data
				//	.getStringExtra(ProjectRemarkActivity.CommonTextContent);
			if (getString(R.string.projectremark).equals(tag)) {
				projectexperience_projectremark_id.setText(tag);
			} else if (getString(R.string.trustremark).equals(tag)) {
				projectexperience_trustremark_id.setText(tag);
			}
		}
		
		if(resultCode==100){
			String selectedStr=data.getStringExtra("RemarkBackStr");
			projectexperience_projectremark_id.setText(selectedStr);
		}
		
		if(resultCode==115){	
			String selectedStr=data.getStringExtra("RemarkBackStr");
			projectexperience_trustremark_id.setText(selectedStr);
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
		String projectid = getIntent().getExtras().getString("id");
		String activeflag = getIntent().getExtras().getString("activeflag");
		JsonUtils.put(jsonObject, "resumeid", resumeid);
		JsonUtils.put(jsonObject, "projectid", projectid);
		JsonUtils.put(jsonObject, "activeflag", activeflag);
		JsonUtils.put(jsonObject, "starttime", projectexperience_starttime_id
				.getText().toString()); // 开始时间
		String endtime = projectexperience_endtime_id.getText().toString();
		if (endtime == null || "".equals(endtime)) {
			endtime = "至今";
		}
		JsonUtils.put(jsonObject, "endtime", endtime);
		JsonUtils.put(jsonObject, "projectname",
				projectexperience_projectname_id.getText().toString());
		JsonUtils.put(jsonObject, "companyname",
				projectexperience_companyname_id.getText().toString());
		JsonUtils.put(jsonObject, "projectremark",
				projectexperience_projectremark_id.getText().toString());
		JsonUtils.put(jsonObject, "trustremark",
				projectexperience_trustremark_id.getText().toString());

		final String data = RequestData.getData(jsonObject,
				Transcodes.TC_PROJECT_EXPERIENCE_SET, false);
		Https.post(this, Transcodes.TC_PROJECT_EXPERIENCE_SET, data,
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
				if(projectexperience_starttime_id.getText()==null||"".equals(projectexperience_starttime_id.getText().toString().trim())){
					return reason;
				}
				if(projectexperience_endtime_id.getText()!=null&&!"".equals(projectexperience_endtime_id.getText().toString().trim())){
					String startDate = projectexperience_starttime_id.getText().toString();
					String endDate = projectexperience_endtime_id.getText().toString();
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
						confirmMessage(ProjectExperienceEditActivity.this, true, "返回当前页面会丢失编辑内容是否要保存?");
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
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				beforeText=s.toString();
				if(itemList.size()<=6){
					itemList.add(s.toString());
					//Toast.makeText(ProjectExperienceEditActivity.this, "集合大小="+itemList.size()+"添加的元素"+s.toString(), Toast.LENGTH_SHORT).show();
				}				
			}
			@Override
			public void afterTextChanged(Editable s) {
			    //初始化加载完默认不显示
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
				if(fistload==6){
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
                if(itemList.size()==6){
                	for(int i=0;i<6;i++){
                		if(!s.toString().equals(itemList.get(i))){
                			 if(isAddFromBtn){
        						 txtView.setText("添加");
        					 }else{
        						 txtView.setText("保存");
        					 }
                			 relativeLayout.setClickable(true);
        					 txtView.setVisibility(View.VISIBLE);
        					 txtView.setClickable(true);
                		}else{
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
