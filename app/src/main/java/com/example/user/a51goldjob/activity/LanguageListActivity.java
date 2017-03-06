package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.bean.IBean;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.chyjr.goldjob.fr.utils.StringUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Language;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.activity_language_list)
public class LanguageListActivity extends BaseActivity {

	/** 编辑动作：编辑 */
	public static final String OPT_EDIT = "opt_edit";

	/** 编辑动作：完成 */
	public static final String OPT_FINISH = "opt_finish";

	protected List<IBean> dataList = createDataList(); // 数据集
	private BaseAdapter adapter;//适配器
	@ViewInject(R.id.language_listview)
	ListView language_listview;

	Handler handler = new Handler();
	TextView headEditTxt;

	protected List<IBean> createDataList() {
		return new ArrayList<IBean>(); // 数据集
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.language);

		init();
	}

	OnClickListener onClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_onClick(v);
		}
	};

	OnClickListener itemOnClickListener = new OnClickListener() {
		public void onClick(View v) {
			actionPerformed_itemOnClick(v);
		}
	};
	
	OnItemClickListener listItemOnClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			actionPerformed_listItemOnClick(parent, view, position,id);
		}
	};

	protected void actionPerformed_itemOnClick(View v) {
		int viewId = v.getId();
		String activeflag = "0";//删除命令
		if (viewId == R.id.language_list_item_delete_imageview) {
			Object tag = v.getTag();
			final int position = Integer.parseInt(tag.toString());
			final Language item = (Language) language_listview.getItemAtPosition(position);
			
			Token token = AppContext.get(this).getToken();
			String resumeid = getIntent().getExtras().getString("resumeid");
			
			JSONObject jsonObject = new JSONObject();
			JsonUtils.put(jsonObject, "token", token.getToken());
			JsonUtils.put(jsonObject, "userid", token.getUserId());
			JsonUtils.put(jsonObject, "resumeid", resumeid); 
			JsonUtils.put(jsonObject, "languageid", item.getLanguageid());
			JsonUtils.put(jsonObject, "activeflag", activeflag); 

			final String data = RequestData.getData(jsonObject, Transcodes.TC_LANGUAGE_SET, false);
			Https.post(this, Transcodes.TC_LANGUAGE_SET, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
				protected void _onSuccess(ResponseInfo<String> response) throws Exception {
					// {"datanum":0,"returnCode":"AAAAAAA","returnMsg":"操作成功"}
					ResponseData responseData = ResponseData.create(response.result);
					String returnMsg = responseData.getBodyPropertyValue("returnMsg");
					final CustomDialog dialog = DialogUtils.info(getThis(), returnMsg);
					dialog.setBtnsOnClickListener(new OnClickListener() {
						public void onClick(View v) {
							dataList.remove(position);
							adapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
				}
			});
			
		}
	}

	protected void actionPerformed_listItemOnClick(AdapterView<?> parent,
			View view, int position, long id) {
		// TODO Auto-generated method stub
		final Language item = (Language) language_listview.getItemAtPosition(position);
		Class<?> to = LanguageActivity.class;
		ActivityUtils.startActivityForResult(handler,
				this, to, new IIntentHandler() {
					public void putExtra(Intent intent) {
						String resumeid = getIntent().getExtras()
								.getString("resumeid");
						intent.putExtra("resumeid", resumeid);
						intent.putExtra("id", item.getLanguageid());
					}
				});
	}

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
		int childCount = language_listview.getChildCount();
		View view = null;
		ImageView imageView = null;
		for (int i = 0; i < childCount; i++) {
			view = language_listview.getChildAt(i);
			imageView = (ImageView) view
					.findViewById(R.id.language_list_item_delete_imageview);
			if (imageView == null) {
				continue;
			}
			imageView.setTag(i);
			imageView.setOnClickListener(itemOnClickListener);
			imageView.setVisibility(visiable);
		}
	}

	private void init() {
		// TODO Auto-generated method stub
		// 添加按钮
		Button addBtn = (Button) findViewById(R.id.add_language);// 获取按钮资源
		addBtn.setOnClickListener(listener);// 设置监听
		createDataList();
		loadList();

		// 条目点击事件
		// work_history_listview.setOnItemClickListener((OnItemClickListener)
		// itemOnClickListener);

		headEditTxt = PublicViews.createMenuTextView(this,
				getString(R.string.opt_edit),
				getResources().getColor(R.color.text_white), onClickListener); // 创建菜单按钮
		headEditTxt.setTag(OPT_EDIT);
		getTopBar().getCustomLayout().setTag(OPT_EDIT);
	}

	OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Class<?> to = LanguageActivity.class;
			ActivityUtils.startActivityForResult(handler,
					LanguageListActivity.this, to, new IIntentHandler() {
						public void putExtra(Intent intent) {
							String activeflag = "";
							String resumeid = getIntent().getExtras()
									.getString("resumeid");
							intent.putExtra("resumeid", resumeid);
							intent.putExtra("activeflag", activeflag);
							intent.putStringArrayListExtra("typeList", getLanguageList());
						}
					});
		}// 创建监听对象

	};
	
	/**
	 * 获取已添加的语言
	 * @return
	 */
	private ArrayList<String> getLanguageList(){
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < dataList.size(); i++) {
			Language bean = (Language) dataList.get(i);
			list.add(bean.getType());
		}
		return list;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		refreshList();
	}

	/**
	 * 刷新页面
	 */
	private void refreshList() {
		dataList.clear();
		loadList();
	}

	protected void actionPerformed_onItemClick(AdapterView<?> parent,
			View view, int position, long id) {
		final Language item = (Language) parent
				.getItemAtPosition(position);
		Class<?> to = LanguageActivity.class;
		ActivityUtils.startActivity(handler, this, to, new IIntentHandler() {
			public void putExtra(Intent intent) {
				String resumeid = getIntent().getExtras().getString("resumeid");
				intent.putExtra("resumeid", resumeid);
				intent.putExtra("id", item.getLanguageid());
			}
		});
	}

	protected List<?> parseDataList(ResponseData responseData) throws Exception {
		// TODO Auto-generated method stub
		String data = responseData.getBodyPropertyValue("data");
		Gson gson = Gsons.create();
		List<Language> datas = gson.fromJson(data,
				new TypeToken<List<Language>>() {
				}.getType());
		return datas;
	}

	protected List<?> getDataList() {
		// TODO Auto-generated method stub
		return dataList;
	}

	/**
	 */
	private void loadList() {
		Token token = AppContext.get(this).getToken();
		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "token", token.getToken());
		JsonUtils.put(jsonObject, "userid", token.getUserId());

		String resumeid = getIntent().getExtras().getString("resumeid");
		JsonUtils.put(jsonObject, "resumeid", resumeid);

		String data = RequestData.getData(jsonObject,
				Transcodes.TC_LANGUAGE, false);
		Https.post(this, Transcodes.TC_LANGUAGE, data,
				new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
					protected void _onSuccess(ResponseInfo<String> response)
							throws Exception {
						ResponseData responseData = ResponseData
								.create(response.result);
						List<IBean> list = (List<IBean>) parseDataList(responseData);
						dataList.addAll(list);
						// 实现列表的显示
						language_listview
								.setAdapter(createListViewAdapter());
						language_listview.setOnItemClickListener(listItemOnClickListener);
					}
				});
	}

	/**
	 * ListView适配器
	 * 
	 * @return
	 */
	protected BaseAdapter createListViewAdapter() {
		// TODO Auto-generated method stub
		BeanAdapter adapter = new BeanAdapter(this, dataList,
				R.layout.activity_language_list_item, new String[] {
						"type", "languageid" }, new int[] {
						R.id.activity_language_list_item_type,
						R.id.activity_language_list_item_languageid});
		setAdapter(adapter);
		return adapter;
	}

	public BaseAdapter getAdapter() {
		return adapter;
	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;
	}

}
