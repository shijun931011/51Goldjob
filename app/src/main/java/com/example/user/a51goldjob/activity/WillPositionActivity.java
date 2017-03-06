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
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.activity_will_position)
public class WillPositionActivity extends BaseFormActivity implements TextWatcher,OnClickListener {

	@ViewInject(R.id.willposition_expectsalarycode_id)
	TextView willposition_expectsalarycode_id;
	
	@ViewInject(R.id.willposition_jobnature_id)
	TextView willposition_jobnature_id;
	
	@ViewInject(R.id.willposition_postids_id)
	TextView willposition_postids_id;
	
	@ViewInject(R.id.willposition_poststime_id)
	TextView willposition_poststime_id;
	
	@ViewInject(R.id.willposition_selfremark_id)
	TextView willposition_selfremark_id;
	
	@ViewInject(R.id.willposition_sitecity_id)
	TextView willposition_sitecity_id;

	final int[] dictViewIds = {
			R.id.willposition_expectsalarycode_id, 
			R.id.willposition_jobnature_id,
			R.id.willposition_postids_id,
			R.id.willposition_poststime_id,
			R.id.willposition_sitecity_id
		};
	
	final String firstStr = "选择";
	final Map dictTitles=new HashMap();
	
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
	private int requestCodeForCommonTextAty=100;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.want_job);
	    // TODO Auto-generated method stub
	    init();
	}

	private void init() {
	
		relativeLayout=(RelativeLayout)this.findViewById(R.id.title_left);
		relativeLayout.setOnClickListener(this);
		ImageView img=(ImageView)this.findViewById(R.id.topbar_return_btn);
		img.setOnClickListener(this);
		
		TextView txtView=(TextView)this.findViewById(R.id.willposition_jobnature_id);
		txtView.addTextChangedListener(this);
		
		txtView=(TextView)this.findViewById(R.id.willposition_sitecity_id);
		txtView.addTextChangedListener(this);
		
		
		txtView=(TextView)this.findViewById(R.id.willposition_postids_id);
		txtView.addTextChangedListener(this);
		
		txtView=(TextView)this.findViewById(R.id.willposition_expectsalarycode_id);
		txtView.addTextChangedListener(this);
		
		txtView=(TextView)this.findViewById(R.id.willposition_poststime_id);
		txtView.addTextChangedListener(this);

		
		txtView=(TextView)this.findViewById(R.id.willposition_selfremark_id);
		txtView.addTextChangedListener(this);
		
		//查询
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		
		String resumeid = getIntent().getExtras().getString("resumeid");
		JsonUtils.put(jsonObject, "resumeid", resumeid); 
		final String data = RequestData.getData(jsonObject, Transcodes.TC_WILL_POSITION, false);
		Https.post(this, Transcodes.TC_WILL_POSITION, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				loadForm(response);
			}
		});
		
		willposition_selfremark_id.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Class<?> to = CommonTextAreaActivity.class;
				ActivityUtils.startActivityForResult(handler, WillPositionActivity.this, to, new IIntentHandler() {
					@Override
					public void putExtra(Intent intent) {
						// TODO Auto-generated method stub
						TextView selfTxtView=(TextView)WillPositionActivity.this.findViewById(R.id.willposition_selfremark_id);
						lastStr=selfTxtView.getText().toString();
						//Intent intent=new Intent(WillPositionActivity.this,CommonTextAreaActivity.class);
						intent.putExtra("SelfRemark",lastStr);
						//intent.putExtra("text", "text"); 
					}
				});
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
		dictTitles.put(R.id.willposition_expectsalarycode_id, this.getString(R.string.expectsalarycode));
		dictTitles.put(R.id.willposition_jobnature_id, this.getString(R.string.jobnature));
		dictTitles.put(R.id.willposition_postids_id, this.getString(R.string.postids));
		dictTitles.put(R.id.willposition_poststime_id, this.getString(R.string.poststime));
		dictTitles.put(R.id.willposition_sitecity_id, this.getString(R.string.sitecity));
	}
	
	protected void loadForm(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		String data = responseData.getBodyPropertyValue("data");
		Map<String, Object> map = Gsons.fromJson(data, new TypeToken<Map<String, Object>>(){}.getType());
		willposition_expectsalarycode_id.setText((String) map.get("expectsalarycode"));
		willposition_jobnature_id.setText((String)map.get("jobnature"));
		willposition_postids_id.setText((String)map.get("postids"));
		willposition_poststime_id.setText((String)map.get("poststime"));
		willposition_sitecity_id.setText((String)map.get("sitecity"));
		willposition_selfremark_id.setText((String)map.get("selfremark"));
	}
    private String lastStr="";
	protected void actionPerformed_dictSelectorOnClick(View v) {
		final String dictKey = (String) v.getTag();
		final int dictId = v.getId();
		switch (dictId) {
		case R.id.willposition_jobnature_id:
			TextView jobnature_view=(TextView)this.findViewById(R.id.willposition_jobnature_id);
			lastStr=jobnature_view.getText().toString();
			break;
		case R.id.willposition_sitecity_id:
			TextView sitecity_view=(TextView)this.findViewById(R.id.willposition_sitecity_id);
			lastStr=sitecity_view.getText().toString();
			break;
		case R.id.willposition_postids_id:
			TextView position_view=(TextView)this.findViewById(R.id.willposition_postids_id);
			lastStr=position_view.getText().toString();
			break;
		case R.id.willposition_expectsalarycode_id:
			TextView expectsalary_view=(TextView)this.findViewById(R.id.willposition_expectsalarycode_id);
			lastStr=expectsalary_view.getText().toString();
			break;
		case R.id.willposition_poststime_id:
			TextView posttime_view=(TextView)this.findViewById(R.id.willposition_poststime_id);
			lastStr=posttime_view.getText().toString();
			break;
		default:
			break;
		}
		
		if(v.getId()==R.id.willposition_selfremark_id){
			TextView selfTxtView=(TextView)this.findViewById(R.id.willposition_selfremark_id);
			lastStr=selfTxtView.getText().toString();
			Intent intent=new Intent(this,CommonTextAreaActivity.class);
			intent.putExtra("SelfRemark",lastStr);
			startActivityForResult(intent, requestCodeForCommonTextAty);
			return;
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
					view.setText(data.getStringExtra(DictSelectorActivity.Constants.PARAM_OUT_SELECT_ITEM));
				}
			}
			if(data!=null){
				String selfremark = (String) data.getStringExtra(CommonTextAreaActivity.CommonTextContent);
				willposition_selfremark_id.setText(selfremark);
			}
		}else if(resultCode==100){
			String remarkStr=data.getStringExtra("RemarkBackStr");
			TextView selfTxtView=(TextView)this.findViewById(R.id.willposition_selfremark_id);
			selfTxtView.setText(remarkStr);
		}
	}

	protected int getSubmitTextStringId() {
		return R.string.save;
	}

	protected void actionPerformed_submitOnClick(View v) {
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		
		String resumeid = getIntent().getExtras().getString("resumeid");
		JsonUtils.put(jsonObject, "resumeid", resumeid); 
		JsonUtils.put(jsonObject, "jobnature", willposition_jobnature_id.getText().toString()); 
		JsonUtils.put(jsonObject, "postids", willposition_postids_id.getText().toString()); 
		JsonUtils.put(jsonObject, "sitecity", willposition_sitecity_id.getText().toString()); 
		JsonUtils.put(jsonObject, "expectsalarycode", willposition_expectsalarycode_id.getText().toString()); 
		JsonUtils.put(jsonObject, "poststime", willposition_poststime_id.getText().toString()); 
		JsonUtils.put(jsonObject, "selfremark", willposition_selfremark_id.getText().toString()); 
		
		final String data = RequestData.getData(jsonObject, Transcodes.TC_WILL_POSITION_SET, false);
		Https.post(this, Transcodes.TC_WILL_POSITION_SET, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				doSuccess(response);
			}
		});
	}
	
	
	
	protected void doSuccess(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		Toast.makeText(this, responseData.getReturnMsg(),Toast.LENGTH_SHORT).show();
		this.finish();
		//ActivityUtils.showMessage(this, responseData.isSuccess(), responseData.getReturnMsg());
	}

	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.title_left||v.getId()==R.id.topbar_return_btn){
			if(relativeLayout.isClickable()){ //可点击则为保存
				confirmMessage(WillPositionActivity.this, true, "返回当前页面会丢失编辑内容是否要保存?");
			}else{
				this.finish();
			}
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		//Toast.makeText(WillPositionActivity.this,"beforChanged"+s.toString(), Toast.LENGTH_SHORT).show();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		beforeText=s.toString();
	//	Toast.makeText(WillPositionActivity.this,"onTextChanged"+s.toString(), Toast.LENGTH_SHORT).show();
		if(itemList.size()<5){
			itemList.add(s.toString());	
		}	
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
	//	Toast.makeText(WillPositionActivity.this,"afterTextChanged"+s.toString(), Toast.LENGTH_SHORT).show();
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
		
		if(fistload==6){
			 txtView=(TextView)this.findViewById(R.id.textViewRight);
			 txtView.setText("保存");
			 relativeLayout.setClickable(false);
			 txtView.setVisibility(View.INVISIBLE);
			 txtView.setClickable(false);
		}
		fistload=fistload+1;	
		if(itemList.size()==6){
			for(int i=0;i<6;i++){
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
