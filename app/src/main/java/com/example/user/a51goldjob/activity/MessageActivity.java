package com.example.user.a51goldjob.activity;

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
import com.example.user.a51goldjob.bean.Message;
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
@ContentView(R.layout.activity_message)
public class MessageActivity extends BaseListActivity {

	/** 编辑动作：编辑 */
	public static final String OPT_EDIT = "opt_edit";
	
	/** 编辑动作：完成 */
	public static final String OPT_FINISH = "opt_finish";

	
	@ViewInject(R.id.message_refresh_head)
	LinearLayout message_refresh_head;

	@ViewInject(R.id.message_listview)
	PullToRefreshListView message_listview;

	@ViewInject(R.id.message_progress_bar)
	View message_progress_bar;
	
	TextView headEditTxt;

	Handler handler = new Handler();
	
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

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.message_collect);

		headEditTxt = PublicViews.createMenuTextView(this, getString(R.string.opt_edit), getResources().getColor(R.color.text_white), onClickListener); // 创建菜单按钮
        headEditTxt.setTag(OPT_EDIT);
        getTopBar().getCustomLayout().setTag(OPT_EDIT);
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
		int childCount = message_listview.getChildCount();
		View view = null;
		ImageView imageView = null;
		for (int i=0; i<childCount; i++) {
			view = message_listview.getChildAt(i);
			imageView = (ImageView) view.findViewById(R.id.message_list_item_delete_imageview);
			if (imageView == null) {
				continue;
			}
			imageView.setTag(i);
			imageView.setOnClickListener(itemOnClickListener);
			imageView.setVisibility(visiable);
		}
	}
	
	protected void actionPerformed_itemOnClick(View v) {
		int viewId = v.getId();
		if (viewId == R.id.message_list_item_delete_imageview) {
			Object tag = v.getTag();
			final int position = Integer.parseInt(tag.toString());
			final Message item = (Message) getListView().getItemAtPosition(position);
			
			Token token = AppContext.get(this).getToken();
			
			JSONObject jsonObject = new JSONObject();
			JsonUtils.put(jsonObject, "token", token.getToken());
			JsonUtils.put(jsonObject, "id", item.getId()); 
			JsonUtils.put(jsonObject, "userid", token.getUserId()); 

			final String data = RequestData.getData(jsonObject, Transcodes.TC_MESSAGE_DEL, false);
			Https.post(this, Transcodes.TC_MESSAGE_DEL, data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true){
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
	
	protected void actionPerformed_onItemClick(AdapterView<?> parent,
			View view, int position, long id) {
		if (isInvalidItemClick(view, position)) {
			return;
		}
		final Message item = (Message) parent.getItemAtPosition(position);
		Class<?> to = MessageReturnActivity.class;
		ActivityUtils.startActivityForResult(handler, this, to, new IIntentHandler() {
			public void putExtra(Intent intent) {
				intent.putExtra("id", String.valueOf(item.getId()));
			}
		});
	}

	protected String getJsonParams(int page) {
		Token token = AppContext.get(this).getToken();

		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "page", String.valueOf(page));
		JsonUtils.put(jsonObject, "userid", token.getUserId());
		JsonUtils.put(jsonObject, "token", token.getToken());

		String data = RequestData.getData(jsonObject, getUrlKey());
		return data;
	}

	protected List<?> parseDataList(ResponseData responseData) throws Exception {
		String data = responseData.getBodyPropertyValue("data");
		String datanumStr = responseData.getBodyPropertyValue("datanum");
		Integer datanum = Integer.valueOf(datanumStr);
		if (datanum <= 0) {
			ActivityUtils.showMessage(this, true,
					getString(R.string.message_nocontent));
		}
		
		Gson gson = Gsons.create();
		List<Message> datas = gson.fromJson(data, new TypeToken<List<Message>>() {}.getType());
		return datas;
	}

	protected BaseAdapter createListViewAdapter() {
		List<?> dataList = (List<?>) getDataList();
		BeanAdapter adapter = new BeanAdapter(this, dataList,
				R.layout.activity_message_list_item, new String[] {
						"username", "corpname", "content", "createdate","smstype","id","readflag" },
				new int[] { R.id.activity_message_list_item_username,
						R.id.activity_message_list_item_corpname,
						R.id.activity_message_list_item_content,
						R.id.activity_message_list_item_createdate,
						R.id.activity_message_list_item_smstype,
						R.id.activity_message_list_item_id,
						R.id.activity_message_list_item_readflag}){
			//1.根据smstype判断显示【公司】还是【猎头】
			protected void bindView(int position, View view) {
				super.bindView(position, view);
				TextView smstypeView = (TextView) view.findViewById(R.id.activity_message_list_item_smstype);
				TextView corpnameView = (TextView) view.findViewById(R.id.activity_message_list_item_corpname);
				TextView usernameView = (TextView) view.findViewById(R.id.activity_message_list_item_username);
				TextView readflagView = (TextView) view.findViewById(R.id.activity_message_list_item_readflag);
				String smstype = smstypeView.getText().toString();
				if("0".equals(smstype)){
					corpnameView.setVisibility(View.VISIBLE);
					usernameView.setVisibility(View.GONE);
				}else{
					usernameView.setVisibility(View.VISIBLE);
					corpnameView.setVisibility(View.GONE);
				}
				//未读标识
				if(readflagView.getText()!=null&&"1".equals(readflagView.getText().toString())){
					readflagView.setVisibility(View.GONE);
				}else{
					readflagView.setText(getString(R.string.no_read));
				}
			}
		};
		return adapter;
	}

	protected void afterLoaded() {
		message_progress_bar.setVisibility(View.GONE);
	}

	protected PullToRefreshListView getListView() {
		return message_listview;
	}

	protected String getUrlKey() {
		return Transcodes.TC_MESSAGE_LIST;
	}
	
	protected LinearLayout getHeadView() {
		return message_refresh_head;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		refreshList();
	}
}
