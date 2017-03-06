package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
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
@ContentView(R.layout.activity_cert_edit)
public class CertEditActivity extends BaseFormActivity implements OnClickListener,TextWatcher{

	@ViewInject(R.id.cert_certname_id)
	EditText cert_certname_id;

	@ViewInject(R.id.cert_certorg_id)
	EditText cert_certorg_id;

	@ViewInject(R.id.cert_score_id)
	EditText cert_score_id;

	
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
	private boolean isFromAddBtn=false;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.cert);
		// TODO Auto-generated method stub
		for(int i=0;i<3;i++){
			if(itemList.size()<=3){
				itemList.add("");
			}
		}
		
		init();
	}


	private void init() {
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);
		
	    EditText editText=(EditText)this.findViewById(R.id.cert_certname_id);
		editText.addTextChangedListener(this);
		 editText=(EditText)this.findViewById(R.id.cert_certorg_id);
			editText.addTextChangedListener(this);
			 editText=(EditText)this.findViewById(R.id.cert_score_id);
				editText.addTextChangedListener(this);
		
		// 查询
		String activeflag = getIntent().getExtras().getString("activeflag");
		String id = getIntent().getExtras().getString("id");
		if(id==null){
			id="";
		}
		if(id.equals("")){
			isFromAddBtn=true;
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
					Transcodes.TC_CERT, false);
			Https.post(this, Transcodes.TC_CERT, data,
					new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
						protected void _onSuccess(ResponseInfo<String> response)
								throws Exception {
							loadForm(response);
						}
					});
		}

		
		
	}


	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data,
				new TypeToken<Map<String, Object>>() {
				}.getType());
		cert_certname_id.setText((String) map.get("certname"));
		cert_certorg_id.setText((String) map.get("certorg"));
		cert_score_id.setText((String) map.get("score"));
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
		String certid = getIntent().getExtras().getString("id");
		String activeflag = getIntent().getExtras().getString("activeflag");
		JsonUtils.put(jsonObject, "resumeid", resumeid);
		JsonUtils.put(jsonObject, "certid", certid);
		JsonUtils.put(jsonObject, "activeflag", activeflag);
		JsonUtils.put(jsonObject, "certname", cert_certname_id.getText()
				.toString()); 
		JsonUtils.put(jsonObject, "certorg", cert_certorg_id.getText()
				.toString()); 
		JsonUtils.put(jsonObject, "score", cert_score_id.getText().toString()); 

		final String data = RequestData.getData(jsonObject,
				Transcodes.TC_CERT_SET, false);
		Https.post(this, Transcodes.TC_CERT_SET, data,
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
				confirmMessage(CertEditActivity.this, true, "返回当前页面会丢失编辑内容是否要保存?");
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
		if(itemList.size()<3){
			itemList.add("");
		}
		
	}


	@Override
	public void afterTextChanged(Editable s) {
		
		if(beforeText.equals(s.toString())){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isFromAddBtn){
				 txtView.setText("添加");
			 }else{
				 txtView.setText("保存");
			 }
			 relativeLayout.setClickable(true);
			    txtView.setVisibility(View.VISIBLE);

		}else {
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isFromAddBtn){
				 txtView.setText("添加");
			 }else{
				 txtView.setText("保存");
			 }
			 relativeLayout.setClickable(false);
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		
		if(fistload==3){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 if(isFromAddBtn){
				 txtView.setText("添加");
			 }else{
				 txtView.setText("保存");
			 }
			 relativeLayout.setClickable(false);
			    txtView.setVisibility(View.INVISIBLE);
			    txtView.setClickable(false);
		}
		fistload=fistload+1;
		if(itemList.size()==3){
			for(int i=0;i<3;i++){
				if(itemList.get(i).equals(s.toString())){
				//	Toast.makeText(BaseDocumentActivity.this,itemList.get(i)+"#"+s.toString(), Toast.LENGTH_SHORT).show();
					 txtView=(TextView)this.findViewById(R.id.textViewRight);
					 if(isFromAddBtn){
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
