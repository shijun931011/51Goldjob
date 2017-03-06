package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.StringUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.JobFavorite;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.DialogUtils;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.CustomDialog;
import com.example.user.a51goldjob.view.PublicViews;
import com.example.user.a51goldjob.view.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 职位收藏夹
 * @author yeq
 *
 */
@ContentView(R.layout.activity_job_favorite)
public class JobFavoriteActivity extends BaseListActivity {

	/** 编辑动作：编辑 */
	public static final String OPT_EDIT = "opt_edit";
	
	/** 编辑动作：完成 */
	public static final String OPT_FINISH = "opt_finish";
	
	@ViewInject(R.id.job_favorite_progress_bar)
	View job_favorite_progress_bar;
	
	@ViewInject(R.id.job_favorite_head_txt)
	TextView job_favorite_head_txt;
	
	@ViewInject(R.id.job_favorite_refresh_head2)
	LinearLayout job_favorite_refresh_head2;
	
	@ViewInject(R.id.job_favorite_center_listview)
	PullToRefreshListView job_favorite_center_listview;

	TextView headEditTxt;
	
	Handler handler = new Handler();

	//按钮监听
    OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_onClick(v);
		}
	};
	
	//明细项监听
	OnClickListener itemOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_itemOnClick(v);
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.user_center_job_favorite);
	    
        headEditTxt = PublicViews.createMenuTextView(this, getString(R.string.opt_edit), getResources().getColor(R.color.text_white), onClickListener); // 创建菜单按钮
        headEditTxt.setTag(OPT_EDIT);
        getTopBar().getCustomLayout().setTag(OPT_EDIT);
	}

	/**
	 * 按钮事件监听过程处理
	 * */
	protected void actionPerformed_onClick(View v) {
		Object tagObject = v.getTag();
		if (tagObject == null) {
			return;
		}
		String tag = tagObject.toString();
		if (StringUtils.isEmpty(tag)) {
			return;
		}
		if (tag.equalsIgnoreCase(OPT_EDIT)) {
	        headEditTxt.setTag(OPT_FINISH);
			headEditTxt.setText(getResources().getString(R.string.opt_finish));
			showDelete(View.VISIBLE); // 显示删除
		} else {
	        headEditTxt.setTag(OPT_EDIT);
			headEditTxt.setText(getResources().getString(R.string.opt_edit));
			showDelete(View.GONE);
		}
	}
	
	private void showDelete(int visiable) {
		int childCount = job_favorite_center_listview.getChildCount();
		View view = null;
		ImageView imageView = null;
		for (int i=0; i<childCount; i++) {
			view = job_favorite_center_listview.getChildAt(i);
			imageView = (ImageView) view.findViewById(R.id.job_favorite_list_item_delete_imageview);
			if (imageView == null) {
				continue;
			}
			imageView.setOnClickListener(itemOnClickListener);
			imageView.setVisibility(visiable);
		}
	}
	
	/**
	 * 进入查看职位列表详细详细页面
	 * */
	@Override
	protected void actionPerformed_onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (isInvalidItemClick(view, position)) {
			return;
		}
		final JobFavorite item = (JobFavorite) parent.getItemAtPosition(position);
		Class<?> to = JobApplyDetailsActivity.class;
		ActivityUtils.startActivity(handler, this, to, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra("corpid", String.valueOf(item.getCompanyid()));
				intent.putExtra("jobid", String.valueOf(item.getJobid()));
				intent.putExtra("jobname", String.valueOf(item.getJobname()));
				intent.putExtra("jobtype", item.getJobtype());
			}
		});
	}

	/**
	 * 明细项监听事件过程处理
	 * */
	protected void actionPerformed_itemOnClick(View v) {
		int viewId = v.getId();
		if (viewId == R.id.job_favorite_list_item_delete_imageview) {
			Object tag = v.getTag();
			final int position = Integer.parseInt(tag.toString());
			final JobFavorite item = (JobFavorite) getListView().getItemAtPosition(position);
			
			Token token = AppContext.get(this).getToken();
			
			JSONObject jsonObject = new JSONObject();
			JsonUtils.put(jsonObject, "token", token.getToken());
			JsonUtils.put(jsonObject, "id", item.getJobcollectionid()); // 职位编号
			JsonUtils.put(jsonObject, "userid", token.getUserId()); 

			final String data = RequestData.getData(jsonObject, Transcodes.TC_JOB_FAVORITE_DELETE, false);
			Https.post(this, Transcodes.TC_JOB_FAVORITE_DELETE, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
				protected void _onSuccess(ResponseInfo<String> response) throws Exception {
					// {"datanum":0,"returnCode":"AAAAAAA","returnMsg":"操作成功"}
					ResponseData responseData = ResponseData.create(response.result);
					String returnMsg = responseData.getBodyPropertyValue("returnMsg");
					final CustomDialog dialog = DialogUtils.info(getThis(), returnMsg);
					dialog.setBtnsOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							getDataList().remove(position);
							getAdapter().notifyDataSetChanged();
							dialog.dismiss();
						}
					});
				}
			});
			
		}
	}

	/**
	 * 加载报文数据
	 * */
	protected List<?> parseDataList(ResponseData responseData) throws Exception {
		String data = responseData.getBodyPropertyValue("data");
		String datanumStr = responseData.getBodyPropertyValue("datanum");
		Integer datanum = Integer.valueOf(datanumStr);
		if (datanum <= 0) {
			ActivityUtils.showMessage(this, true,
					getString(R.string.user_center_apply_info_nocontent));
		}
		//去除h
		data = JobApplyAcativity.remove_h(data);
		Gson gson = Gsons.create();
		List<JobFavorite> datas = gson.fromJson(data, new TypeToken<List<JobFavorite>>() {}.getType());
		List<JobFavorite> copyDatas = new ArrayList<JobFavorite>();
		for (Iterator iterator = datas.iterator(); iterator.hasNext();) {
			JobFavorite jobFavorite = (JobFavorite) iterator.next();
			if("2".equals(jobFavorite.getStatus())){
				copyDatas.add(jobFavorite);
			}
		}
		return copyDatas;
	}

	/**
	 * 重写加载页面数据方法
	 * */
	protected String getJsonParams(int page) {
		Token token = AppContext.get(this).getToken();

		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "page", String.valueOf(page)); // cc@test.com
		JsonUtils.put(jsonObject, "userid", token.getUserId()); // 111
		JsonUtils.put(jsonObject, "token", token.getToken());

		String data = RequestData.getData(jsonObject, getUrlKey(), false);
		return data;
	}

	/**
	 * 适配器载入职位信息
	 * */
	protected BaseAdapter createListViewAdapter() {
		List<?> dataList = (List<?>) getDataList();
		BeanAdapter adapter = new BeanAdapter(this, dataList,
				R.layout.activity_job_favorite_list_item, new String[] {
						"jobname", "issuedatedt", "corpname", "jobcity" },
				new int[] { R.id.activity_job_apply_list_item_jobname,
						R.id.activity_job_apply_list_item_issuedate,
						R.id.activity_job_apply_list_item_corpname,
						R.id.activity_job_apply_list_item_jobcity }) {
			protected void bindView(int position, View view) {
				super.bindView(position, view);
				
				ImageView imageView = (ImageView) view.findViewById(R.id.job_favorite_list_item_delete_imageview);
				imageView.setTag(position);
				imageView.setOnClickListener(itemOnClickListener);
				
				
			}
		};
		return adapter;
	}

	protected String getUrlKey() {
		return Transcodes.TC_JOB_FAVORITE_LIST;
	}

	/**
	 * 数据完全加载完毕后处理
	 * 重写父类的afterLoaded()方法。
	 */
	protected void afterLoaded() {
		job_favorite_progress_bar.setVisibility(View.GONE);
		job_favorite_refresh_head2.setVisibility(View.VISIBLE);
		job_favorite_head_txt.setVisibility(View.VISIBLE);
	}

	protected PullToRefreshListView getListView() {
		return job_favorite_center_listview;
	}
	
	protected LinearLayout getHeadView() {
		return job_favorite_refresh_head2;
	}
}
