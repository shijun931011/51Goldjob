package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chyjr.goldjob.fr.mgr.IBiz;
import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.BindUtils;
import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.NumberUtils;
import com.chyjr.goldjob.fr.utils.ViewerUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.bean.Dict;
import com.example.user.a51goldjob.bean.DictConstants;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Resume;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.mgr.DictMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.ActionSheet;
import com.example.user.a51goldjob.view.CustomDialog;
import com.example.user.a51goldjob.view.SlideShowView;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 我的简历
 * 
 * @author yeq
 *
 */
@ContentView(R.layout.activity_my_resume)
public class MyResumeActivity extends BaseFormActivity implements OnClickListener{
	
	@ViewInject(R.id.my_resume_sv)
	SlideShowView my_resume_sv;
	
	Handler handler = new Handler();

	DictMgr dictMgr = Mgr.get(DictMgr.class);

	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.btn_myresume);
	    TextView createResumetxt=(TextView)this.findViewById(R.id.textViewRight);
	    createResumetxt.setOnClickListener(this);
	    createResumetxt.setText("创建");
	    init();
	}

	protected int getSubmitTextStringId() {
		return R.string.create;
	}

	protected void init() {
		Token token = getToken();
		if (token == null) {
			finish();
			return;
		}
		
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 

		final String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_LIST, false);
		Https.post(this, Transcodes.TC_RESUME_LIST, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				initUserResume(response); // 初始化用户简历
			}
		});
	}

	protected void initUserResume(ResponseInfo<String> response) {
		ResponseData responseData = ResponseData.create(response.result);
		int datanum = Integer.valueOf(responseData.getBodyPropertyValue("datanum"));
		if (datanum <= 0) {
			resumeConfirmIsCreate();
			return;
		}
		if (!(responseData.isSuccess())) {
			String returnMsg = responseData.getReturnMsg();
			ActivityUtils.showMessage(this, true, returnMsg); // 弹出错误信息
			return;
		}
		String data = responseData.getBodyPropertyValue("data");
		List<Resume> resumes = Gsons.fromJson(data, new TypeToken<List<Resume>>() {}.getType());
		if (CollectionUtils.isEmpty(resumes)) {
			resumeConfirmIsCreate();
			return;
		}
		
		initResumeUI(resumes);
	}

//	[{resumeid=100005, whocomp=2, whohh=4, userid=60001, percent=100.0, resumename=测让历10, companyid=仅对猎头公开, 
//	updateby=60001, createby=60001, createdate=1.407982134E12, updatedate=1.411984036E12, activeflag=1.0, defaults=0.0}]
	protected View createView(Resume item) {
		View viewItem = getLayoutInflater().inflate(R.layout.activity_my_resume_item, null);
		
		// 简历名称
		TextView my_resume_item_resume_name_txt = (TextView) viewItem.findViewById(R.id.my_resume_item_resume_name_txt);
		my_resume_item_resume_name_txt.setText(item.getResumename());
		
		// 更新日期
		TextView my_resume_item_updatedatedt_txt = (TextView) viewItem.findViewById(R.id.my_resume_item_updatedatedt_txt);
		my_resume_item_updatedatedt_txt.setText(item.getUpdatedatedt());
		
		// 创建日期
		TextView my_resume_item_createdatedt_txt = (TextView) viewItem.findViewById(R.id.my_resume_item_createdatedt_txt);
		my_resume_item_createdatedt_txt.setText(item.getCreatedatedt());
		
		// 简历完整度
		ProgressBar my_resume_item_percent_progress = (ProgressBar) viewItem.findViewById(R.id.my_resume_item_percent_progress);
		my_resume_item_percent_progress.setProgress(NumberUtils.toIntFromDouble(item.getPercent()));
		
		// 仅对企业公开
		Button my_resume_item_public_btn = (Button) viewItem.findViewById(R.id.my_resume_item_public_btn);
		
		setCompanyid(item.getCompanyid(), my_resume_item_public_btn);
		viewItem.setTag(item);
		// 批量设置Tag
		ViewerUtils.setTag(item, viewItem, R.id.my_resume_item_public_btn, R.id.my_resume_edit_layout , 
				R.id.my_resume_refresh_layout , R.id.my_resume_preview_layout ,R.id.my_resume_delete_layout);
		
		// 绑定点击事件
		BindUtils.bindCtrlOnClickListener(viewItem, submitListener, R.id.my_resume_edit_layout ,
				R.id.my_resume_refresh_layout , R.id.my_resume_preview_layout ,R.id.my_resume_delete_layout );
		my_resume_item_public_btn.setOnClickListener(submitListener);
		
		return viewItem;
	}
	
	protected void initResumeUI(List<Resume> resumes) {
		List<View> views = new ArrayList<View>();
		View viewItem = null;
		for (Resume resume : resumes) {
			viewItem = createView(resume);
			if (viewItem == null) {
				continue;
			}
			views.add(viewItem);
		}
		my_resume_sv.initViewList(views, R.layout.ad_bottom_item_center);
		my_resume_sv.setDotPadding(10, 40, 10, 50);
	}
	
	protected void actionPerformed_submitOnClick(View view) {
		if (view.equals(submitTxt)) {
			resumeCreate(); // 创建简历
			return;
		}
		
		Resume resume = (Resume) (view.getTag());
		if (resume == null) {
			return;
		}
		
		int viewId = view.getId();
		if (viewId == R.id.my_resume_delete_layout) {
			resumeDelete(resume); // 删除
		} else if (viewId == R.id.my_resume_refresh_layout) {
			resumeRefresh(resume); // 刷新
		} else if (viewId == R.id.my_resume_preview_layout) {
			resumePreview(resume); // 预览
		} else if (viewId == R.id.my_resume_edit_layout) {
			resumeEdit(resume); // 编辑
		} else if (viewId == R.id.my_resume_item_public_btn) {
			resumeSetIsOpenAttr((Button)view, resume); // 设置公开属性
		}
	}

	// 设置公开属性
	private void resumeSetIsOpenAttr(final Button button, Resume resume) {
		dictMgr.execute(this, new IBiz() {
			public void execute() {
				List<Dict> dicts = dictMgr.getCurrentDicts(getThis(), DictConstants.DICT_RESUME_ISOPEN); // 获取数据字典
				if (CollectionUtils.isEmpty(dicts)) {
					return;
				}
				initSetIsOpenAttr(button, dicts); // 初始化公开属性
			}
		});
	}

	// 初始化公开属性
	protected void initSetIsOpenAttr(final Button button, List<Dict> dicts) {
		String[] dictArr = new String[dicts.size()];
		for (int i=0; i<dicts.size(); i++) {
			dictArr[i] = dicts.get(i).getValue();
		}
		ActionSheet.Builder builder = ActionSheet.createBuilder(this.getApplicationContext(), getFragmentManager());
		builder.setCancelButtonTitle(getString(R.string.cancel))
	        .setOtherButtonTitles(dictArr)
	        .setCancelableOnTouchOutside(true)
	        .setListener(new ActionSheet.ActionSheetListenerAdapter() {
	    		public void onOtherButtonClick(ActionSheet actionSheet, int index) {
	    			actionPerformed_onOtherButtonClick(actionSheet, index, button);
	    		}
	    	})
	        .show();
	}

	protected void actionPerformed_onOtherButtonClick(ActionSheet actionSheet, int index, final Button button) {
		List<Dict> dicts = dictMgr.getCurrentDicts(getThis(), DictConstants.DICT_RESUME_ISOPEN); // 获取数据字典
		if (CollectionUtils.isEmpty(dicts) || dicts.size() <= index) {
			return;
		}
		final Dict dict = dicts.get(index);
		if (dict == null) {
			return;
		}
		Token token = getToken();
		if (token == null) {
			return;
		}
		Resume resume = (Resume) (my_resume_sv.getCurrentView().getTag());
		if (resume == null) {
			return;
		}
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		JsonUtils.put(jsonObject, "resumeid", resume.getResumeid()); 
		JsonUtils.put(jsonObject, "companyid", dict.getValue()); // 设置简历公开参数

		String data = RequestData.getData(jsonObject, Transcodes.TC_RESUME_SET_ISOPEN, false);
		
		Https.post(this, Transcodes.TC_RESUME_SET_ISOPEN, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				ResponseData responseData = ResponseData.create(response.result);
				ActivityUtils.showMessage(getThis(), false, responseData.getReturnMsg()); // 弹出信息
				if (responseData.isSuccess()) {
					setCompanyid(dict.getValue(), button);
				}
			}
		});
	}
	
	// 创建简历
	private void resumeCreate() {
		ActivityUtils.startActivityForResult(handler, this, ResumeCreateActivity.class,new IIntentHandler() {
			@Override
			public void putExtra(Intent intent) {
				// TODO Auto-generated method stub
				intent.putExtra("IsCreate", true);
			}
		});
	}

//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		init();
//	}
	
	// 简历编辑
	private void resumeEdit(final Resume resume) {
		ActivityUtils.startActivity(handler, this, ResumeEditActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(ResumeEditActivity.ParamsIn.RESUME_ID, resume.getResumeid()); // 简历ID
				intent.putExtra(ResumeEditActivity.ParamsIn.RESUME_PERCENT, resume.getPercent()); // 简历完整度
				intent.putExtra(ResumeEditActivity.ParamsIn.RESUME_NAME, resume.getResumename()); // 简历名称
			}
		});
	}

	// 简历预览
	private void resumePreview(final Resume resume) {
		ActivityUtils.startActivity(handler, this, ResumePreviewActivity.class, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra(ResumePreviewActivity.DETAIL_ID, resume.getResumeid());
			}
		});
	}

	// 简历刷新
	private void resumeRefresh(Resume resume) {
		String data = getPostDataForDeleteOrRefresh(resume, Transcodes.TC_RESUME_REFRESH);
		if (StringUtils.isEmpty(data)) {
			return;
		}
		Https.post(this, Transcodes.TC_RESUME_REFRESH, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				ResponseData responseData = ResponseData.create(response.result);
				String returnMsg = responseData.getReturnMsg();
			//	ActivityUtils.showMessage(getThis(), false, returnMsg); // 弹出信息
				Toast.makeText(MyResumeActivity.this,returnMsg,Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void resumeIsCreate() {
		if (my_resume_sv.getViewSize() == 0) {
			resumeConfirmIsCreate();
		}
	}

	private void resumeConfirmIsCreate() {
		// 弹出新增对话框，是否要新增简历，不新增则退出
		final CustomDialog customDialog = DialogUtils.confirm(getThis(), getString(R.string.please_create_resume_first));
		customDialog.setBtnsOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String tag = (String) v.getTag();
				customDialog.dismiss();
				if (DialogUtils.CANCEL.equalsIgnoreCase(tag)) {
					finish();
					return;
				}
				resumeCreate();
			}
		});
	}

	// 简历删除
	private void resumeDelete(final Resume resume) {
		final CustomDialog customDialog = DialogUtils.confirm(this, getString(R.string.confirm_delete));
		customDialog.setBtnsOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String tag = (String) v.getTag();
				customDialog.dismiss();
				if (DialogUtils.CANCEL.equalsIgnoreCase(tag)) {
					return;
				}
				// YES
//				my_resume_sv.removeCurrent();
//				resumeIsCreate();
				
				// 调用删除接口
				String data = getPostDataForDeleteOrRefresh(resume, Transcodes.TC_RESUME_DELETE);
				if (StringUtils.isEmpty(data)) {
					return;
				}
				Https.post(getThis(), Transcodes.TC_RESUME_DELETE, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(getThis(), true){
					protected void _onSuccess(ResponseInfo<String> response) throws Exception {
						ResponseData responseData = ResponseData.create(response.result);
						String returnMsg = responseData.getReturnMsg();
						ActivityUtils.showMessage(getThis(), false, returnMsg); // 弹出信息
						my_resume_sv.removeCurrent();
						resumeIsCreate();
					}
				});
			}
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		refreshList();
	}
	
	//刷新简历
	private void refreshList() {
		// TODO Auto-generated method stub
		my_resume_sv.getViewList().clear();
		init();
	}

	private String getPostDataForDeleteOrRefresh(Resume resume, String transcode) {
		Token token = getToken();
		if (token == null) {
			return null;
		}
		
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId()); 
		JsonUtils.put(jsonObject, "resumeid", resume.getResumeid()); 

		String data = RequestData.getData(jsonObject, transcode, false);
		return data;
	}
	
	// 设置公开
	private void setCompanyid(String companyid, Button my_resume_item_public_btn) {
		companyid = StringUtils.isEmpty(companyid) || "(null)".equalsIgnoreCase(companyid.trim()) ? getString(R.string.all_is_open) : companyid;
		if (companyid.length() <= 4) {
			my_resume_item_public_btn.setBackgroundResource(R.drawable.bg_public_word_one);
		} else {
			my_resume_item_public_btn.setBackgroundResource(R.drawable.bg_public_word_two);
		}
		my_resume_item_public_btn.setText(companyid);
		my_resume_item_public_btn.refreshDrawableState();
	}

	@Override
	public void onClick(View v) {
		resumeCreate(); // 创建简历
		
	}
}
