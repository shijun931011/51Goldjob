package com.example.user.a51goldjob.activity;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chyjr.goldjob.fr.adapter.BeanAdapter;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.Gsons;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Message;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Token;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.example.user.a51goldjob.utils.JsonUtils;
import com.example.user.a51goldjob.view.PublicViews;
import com.example.user.a51goldjob.view.PullToRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
@ContentView(R.layout.activity_message_return)
public class MessageReturnActivity extends BaseListActivity {

	/** 编辑动作：回复 */
	public static final String OPT_EDIT = "opt_edit";
	
	private static String name = "";
	
	
	@ViewInject(R.id.message_return_refresh_head)
	LinearLayout message_return_refresh_head;

	@ViewInject(R.id.message_return_listview)
	PullToRefreshListView message_return_listview;

	@ViewInject(R.id.message_return_progress_bar)
	View message_return_progress_bar;

	TextView headEditTxt;
	Handler handler = new Handler();

	View.OnClickListener onClickListener = new View.OnClickListener() {
		public void onClick(View v) {
			actionPerformed_onClick(v);
		}
	};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setBarTitle(R.string.message_return);
		
		headEditTxt = PublicViews.createMenuTextView(this, getString(R.string.opt_return), getResources().getColor(R.color.text_white), onClickListener); // 创建菜单按钮
        headEditTxt.setTag(OPT_EDIT);
        getTopBar().getCustomLayout().setTag(OPT_EDIT);
	}


	protected void actionPerformed_onClick(View v) {
		// TODO Auto-generated method stub
		Class<?> to = MessageReturnEditActivity.class;
		final String id = getIntent().getExtras().getString("id");
		ActivityUtils.startActivityForResult(handler, this, to, new IIntentHandler(){
			@Override
			public void putExtra(Intent intent) {
				// TODO Auto-generated method stub
				intent.putExtra("id", id);
			}
		});
	}


	protected String getJsonParams(int page) {
		Token token = AppContext.get(this).getToken();

		JSONObject jsonObject = new JSONObject();
		JsonUtils.put(jsonObject, "page", String.valueOf(page));
		JsonUtils.put(jsonObject, "userid", token.getUserId());
		JsonUtils.put(jsonObject, "token", token.getToken());
		
		String id = getIntent().getExtras().getString("id");
		JsonUtils.put(jsonObject, "id", id);

		String data = RequestData.getData(jsonObject, getUrlKey());
		return data;
	}

	protected List<?> parseDataList(ResponseData responseData) throws Exception {
		String data = responseData.getBodyPropertyValue("data");
		String datanumStr = responseData.getBodyPropertyValue("datanum");
		String msg = responseData.getBodyPropertyValue("msg");
		Integer datanum = Integer.valueOf(datanumStr);
//		if (datanum <= 0) {
//			ActivityUtils.showMessage(this, true,
//					getString(R.string.message_nocontent));
//		}
		
		Gson gson = Gsons.create();
		List<Message> datas = gson.fromJson(data, new TypeToken<List<Message>>() {}.getType());
		Map<String, Object> msgMap = Gsons.fromJson(msg, new TypeToken<Map<String, Object>>(){}.getType());
		if(msgMap!=null){
			if("0".equals(msgMap.get("smstype"))){
				name = (String) msgMap.get("corpname");
			}else{
				name = (String) msgMap.get("username");
			}
		}
		return datas;
	}

	protected BaseAdapter createListViewAdapter() {
		List<?> dataList = (List<?>) getDataList();
		BeanAdapter adapter = new BeanAdapter(this, dataList,
				R.layout.activity_message_return_list_item, new String[] {
						"corpname","content", "createdate","id","sender" },
				new int[] { R.id.activity_message_list_item_corpname,
						R.id.activity_message_list_item_content,
						R.id.activity_message_list_item_createdate,
						R.id.activity_message_list_item_id,
						R.id.activity_message_list_item_sender}){
			//1.根据smstype判断显示【公司】还是【猎头】
			protected void bindView(int position, View view) {
				super.bindView(position, view);
				TextView senderView = (TextView) view.findViewById(R.id.activity_message_list_item_sender);
				TextView corpnameView = (TextView) view.findViewById(R.id.activity_message_list_item_corpname);
				TextView langView = (TextView) view.findViewById(R.id.activity_message_list_item_lang);
				if(senderView.getText()!=null&&!"".equals(senderView.getText().toString())){
					corpnameView.setText(name);
					corpnameView.setVisibility(View.VISIBLE);
					langView.setVisibility(View.GONE);
				}
			}
		};
		return adapter;
	}

	protected void afterLoaded() {
		message_return_progress_bar.setVisibility(View.GONE);
	}

	protected PullToRefreshListView getListView() {
		return message_return_listview;
	}

	protected String getUrlKey() {
		return Transcodes.TC_MESSAGE_RETURN_LIST;
	}
	
	protected LinearLayout getHeadView() {
		return message_return_refresh_head;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		refreshList();
	}

}
