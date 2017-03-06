package com.example.user.a51goldjob.activity;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;

import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.CommonUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.bean.MapBean;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.PullToRefreshListView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 政策法规列表
 * 
 * @author yeq
 * 
 */
@ContentView(R.layout.activity_policy_list)
public class PolicyListActivity extends BaseListActivity {

	@ViewInject(R.id.policy_list_listview)
	PullToRefreshListView policy_list_listview;

	@ViewInject(R.id.policy_list_progress_bar)
	View policy_list_progress_bar;

	Handler handler = new Handler();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.news_policy);
	}

	@SuppressWarnings("unchecked")
	protected void actionPerformed_onItemClick(AdapterView<?> parent,
			View view, int position, long id) {
		if (isInvalidItemClick(view, position)) {
			return;
		}
		final MapBean<String, Object> item = (MapBean<String, Object>) parent
				.getItemAtPosition(position);
		Class<?> to = PolicyListDetailsActivity.class;
		ActivityUtils.startActivity(handler, this, to, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra("infoid", String.valueOf(item.get("infoid")));
			}
		});
	}

	@SuppressWarnings("unchecked")
	public void reviewDataList(List<?> dataList) {
		for (int i = 0; i < dataList.size(); i++) {
			((MapBean<String, Object>) dataList.get(i)).put("lineNo",
					CommonUtils.getLeadingNumber(i + 1, 10)); // 加入行号
		}
	}

	protected String getJsonParams(int page) {
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "page", String.valueOf(page)); // cc@test.com
		JsonUtils.put(jsonObject, "groupid", "2"); // 111

		String data = RequestData.getData(jsonObject, Transcodes.TC_NEWS_LIST);
		return data;
	}

	protected List<?> parseDataList(ResponseData responseData) throws Exception {
		String listStr = responseData.getBodyPropertyValue("list");
		JsonUtils.JsonListMapResult<MapBean<String, Object>> jsonListMapResult = JsonUtils
				.parseToMaps(listStr, "infoid");
		List<MapBean<String, Object>> datas = jsonListMapResult.getList();
		return datas;
	}

	@SuppressWarnings("unchecked")
	protected BaseAdapter createListViewAdapter() {
		List<MapBean<String, Object>> dataList = (List<MapBean<String, Object>>) getDataList();
		SimpleAdapter adapter = new SimpleAdapter(this, dataList,
				R.layout.activity_policy_list_list_item, new String[] {
						"lineNo", "title", "preview_text", "pubdates" },
				new int[] { R.id.policy_list_list_item_lineno_txt,
						R.id.policy_list_list_item_title_txt,
						R.id.policy_list_list_item_preview_txt,
						R.id.policy_list_list_item_time_txt });
		return adapter;
	}

	protected void afterLoaded() {
		policy_list_progress_bar.setVisibility(View.GONE);
	}

	protected PullToRefreshListView getListView() {
		return policy_list_listview;
	}

	protected String getUrlKey() {
		return Transcodes.TC_NEWS_LIST;
	}
}
