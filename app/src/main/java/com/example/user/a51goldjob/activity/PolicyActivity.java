package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 政策法规
 * @author yeq
 *
 */
@ContentView(R.layout.activity_policy)
public class PolicyActivity extends BaseActivity {
	
	@ViewInject(R.id.policy_action_listview)
	ListView policy_action_listview;

	Handler handler = new Handler();
	int[] listItemLangs = {R.string.news_policy, R.string.news_disclaimer};
	Class<?>[] activities = {PolicyListActivity.class, DisclaimerActivity.class};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState);
        setBarTitle(R.string.news_policy_addnew);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.activity_policy_list_item, 
				new String[]{"policy_list_item_txt"}, 
				new int[]{R.id.policy_list_item_txt});
        policy_action_listview.setAdapter(simpleAdapter);
	}

	@OnItemClick(R.id.policy_action_listview)
    public void actionPerformed_otherActionListViewItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if (pos >= activities.length) {
			return;
		}
		Class<?> activity = activities[pos];
		if (activity == null) {
			return;
		}
		ActivityUtils.startActivity(handler, this, activity);
	}
	
	private List<Map<String, Object>> getData() {
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();    	
    	
    	for(int i = 0; i < listItemLangs.length; i++) {
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put("policy_list_item_txt", getResources().getString(listItemLangs[i]));
        	list.add(map);
    	}
    	
    	return list;
    }
}
