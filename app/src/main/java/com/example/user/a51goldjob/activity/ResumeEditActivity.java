package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.BeanUtils;
import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.IntentUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 简历编辑：新增、编辑
 * @author yeq
 *
 */
@ContentView(R.layout.activity_resume_edit)
public class ResumeEditActivity extends BaseActivity {
	private TextView onListItemRIghtTv;
	// 输入参数
	public static class ParamsIn {
		/** 简历ID */
		public static final String RESUME_ID = "resume_resumeid";
		/** 完整度 */
		public static final String RESUME_PERCENT = "resume_percent";
		/** 简历名称 */
		public static final String RESUME_NAME = "resume_name";
	}
	
	@ViewInject(R.id.resume_edit_name_txt)
	TextView resume_edit_name_txt;
	
	@ViewInject(R.id.resume_edit_percent_txt)
	TextView resume_edit_percent_txt;
	//基本信息
	@ViewInject(R.id.resume_edit_baseinfo_listview)
	ListView resume_edit_baseinfo_listview;
	//其他信息
	@ViewInject(R.id.resume_edit_otherinfo_listview)
	ListView resume_edit_otherinfo_listview;
	
	String[] from = { "itemLabel", "itemValue" };
	int[] to = { R.id.resume_edit_list_item_label_txt, R.id.resume_edit_list_item_value_txt };
	
	Handler handler = new Handler();

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.resume_edit);
	
	    init();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		
//		if(onListItemRIghtTv.getText().equals("完整")){
//			onListItemRIghtTv.setTextColor(Color.BLUE);
//		}else{
//			onListItemRIghtTv.setTextColor(R.drawable.orange);
//		}
		super.onResume();
		
	}

	private void init() {
//		onListItemRIghtTv=(TextView)this.findViewById(R.id.resume_edit_list_item_value_txt);
		String resumeid = IntentUtils.getStringExtra(this, ParamsIn.RESUME_ID);
		String percent = IntentUtils.getStringExtra(this, ParamsIn.RESUME_PERCENT);
		String name = IntentUtils.getStringExtra(this, ParamsIn.RESUME_NAME);
		name = StringUtils.isEmpty(name) ? "" : name;
		resume_edit_name_txt.setText(name);
		String percentLang = getString(R.string.resume_finish_percent);
		resume_edit_percent_txt.setText(String.format("%s%s", percentLang, percent));
		
		// 加载完整度
		Token token = getToken();
		if (token == null || StringUtils.isEmpty(resumeid)) {
			finish();
			return;
		}
		
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		JsonUtils.put(jsonObject, "resumeid", resumeid);
		// {"returnCode":"AAAAAAA","returnMsg":"操作成功","data":"[{\"resumeid\":\"1200005\",\"base\":10,\"job\":0,\"work\":0,\"edu\":0,\"lan\":0,\"proj\":0,\"train\":0,\"cert\":0,\"other\":0,\"high\":0}]"}
		String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_FINISH_PERCENT, false);
		Https.post(this, Transcodes.TC_RESUME_FINISH_PERCENT, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				ResponseData responseData = ResponseData.create(response.result);
				initResumeEdit(responseData); // 初始化简历编辑界面
			}
		});
	}
	
	@OnItemClick(R.id.resume_edit_baseinfo_listview)
	public void actionPerformed_EditBaseinfoListviewItemOnClick(AdapterView<?> parent, View view, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, String> field = (Map<String, String>) resume_edit_baseinfo_listview.getAdapter().getItem(Long.valueOf(id).intValue());
		
		String activityCls = field.get("activityCls");
		
		Class<?> cls = null;
		try {
			cls = ClassUtils.getClass(activityCls);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (cls != null) {
			final String resumeid = IntentUtils.getStringExtra(this, ParamsIn.RESUME_ID);
			ActivityUtils.startActivity(handler, getThis(), cls, new IIntentHandler() {
				public void putExtra(Intent intent) {
					intent.putExtra("resumeid", resumeid);
				}
			});
			return;
		}
		
		System.out.println(id + " = " + view);
	}
	
	@OnItemClick(R.id.resume_edit_otherinfo_listview)
	public void actionPerformed_EditOtherinfoListviewItemOnClick(AdapterView<?> parent, View view, int position, long id) {
		@SuppressWarnings("unchecked")
		Map<String, String> field = (Map<String, String>) resume_edit_otherinfo_listview.getAdapter().getItem(Long.valueOf(id).intValue());
		
		String activityCls = field.get("activityCls");
		
		Class<?> cls = null;
		try {
			cls = ClassUtils.getClass(activityCls);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if (cls != null) {
			final String resumeid = IntentUtils.getStringExtra(this, ParamsIn.RESUME_ID);
			ActivityUtils.startActivity(handler, getThis(), cls, new IIntentHandler() {
				public void putExtra(Intent intent) {
					intent.putExtra("resumeid", resumeid);
				}
			});
			return;
		}
	}
	
	protected void initResumeEdit(ResponseData responseData) {
		if (!responseData.isSuccess()) {
			// TODO 失败弹出错误信息
			return;
		}
		
		String data = responseData.getBodyPropertyValue("data");
		List<Map<String, Object>> list = Gsons.fromJson(data, new TypeToken<List<Map<String, Object>>>(){}.getType());
		if (CollectionUtils.isEmpty(list)) {
			// TODO 失败弹出错误信息
			return;
		}
		
		Map<String, Object> finishPercentMap = list.get(0);
		initBaseInfo(finishPercentMap);
		initOtherInfo(finishPercentMap);
	}

	//基本资料
	private void initBaseInfo(Map<String, Object> finishPercentMap) {
		List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
		Map<String, String> field = null;

		String[] baseInfoColums = getResources().getStringArray(R.array.base_info_column_name_array);
		String[] baseInfoLangs = getResources().getStringArray(R.array.base_info_array);
		String[] activityCls = getResources().getStringArray(R.array.base_info_activity_class_array);
		for (int i =0 ; i < baseInfoLangs.length; i++) {
			field = new HashMap<String, String>();
			field.put("itemLabel", baseInfoLangs[i]);
			field.put("itemValue", getParamValue(finishPercentMap.get(baseInfoColums[i])));
			field.put("activityCls", activityCls[i]);
			fields.add(field);
		}
		
		//resume_edit_list_item_value_txt 不完整蓝色
		BeanAdapter adapter = new BeanAdapter(this, fields, R.layout.activity_resume_edit_list_item, from, to);
		resume_edit_baseinfo_listview.setAdapter(adapter);
//		TextView txtView=(TextView)resume_edit_baseinfo_listview.getChildAt(0).findViewById(R.id.resume_edit_baseinfo_title_txt);
//		if(txtView.getText().toString().equals("完整")){
//			txtView.setTextColor(R.drawable.orange);
//		}
		
	}

	//选填资料初始化
	private void initOtherInfo(Map<String, Object> finishPercentMap) {
		List<Map<String, String>> fields = new ArrayList<Map<String, String>>();
		Map<String, String> field = null;
		
		String[] otherInfoColumns = getResources().getStringArray(R.array.other_info_column_name_array);
		String[] otherInfoLangs = getResources().getStringArray(R.array.other_info_array);
		String[] activityCls = getResources().getStringArray(R.array.other_info_activity_class_array);
		for (int i =0 ; i < otherInfoLangs.length; i++) {
			field = new HashMap<String, String>();
			field.put("itemLabel", otherInfoLangs[i]);
			field.put("itemValue", getParamValue(finishPercentMap.get(otherInfoColumns[i])));
			field.put("activityCls", activityCls[i]);
			fields.add(field);
		}
		BeanAdapter adapter = new BeanAdapter(this, fields, R.layout.activity_resume_edit_list_item, from, to);
		resume_edit_otherinfo_listview.setAdapter(adapter);
	}

	private String getParamValue(Object value) {
		if (value == null) {
			return getString(R.string.imperfect);
		}
		double dValue = Double.parseDouble(value.toString());
		int strId = dValue >= 10 ? R.string.perfect : R.string.imperfect;
		return getString(strId);
	}
	
	@OnClick(R.id.resume_edit_name_rb)
	public void actionPerformed_resumeEditNameRb(View view) {
		// TODO 修改名称
		ActivityUtils.startActivityForResult(handler, this, ResumeNameEditActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				String resumeid = IntentUtils.getStringExtra(getThis(), ParamsIn.RESUME_ID);
				if (StringUtils.isEmpty(resumeid)) {
					return;
				}
				intent.putExtra("resumeid", resumeid);
				intent.putExtra("resumename", resume_edit_name_txt.getText().toString());
			}
		});
	}
	
	// 预览
	@OnClick(R.id.resume_edit_perview_rb)
	public void actionPerformed_resumeEditPerviewRb(View view) {
		ActivityUtils.startActivity(handler, this, ResumePreviewActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				String resumeid = IntentUtils.getStringExtra(getThis(), ParamsIn.RESUME_ID);
				if (StringUtils.isEmpty(resumeid)) {
					return;
				}
				intent.putExtra(ResumePreviewActivity.DETAIL_ID, resumeid);
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data!=null){
			String name = (String) data.getExtras().get("resumename");
			resume_edit_name_txt.setText(name);
		}
	}
}
