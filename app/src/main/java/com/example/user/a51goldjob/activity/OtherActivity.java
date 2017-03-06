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
 * 其它（更多）
 * @author yeq
 *
 */
@ContentView(R.layout.activity_other)
public class OtherActivity extends BaseActivity {
	
	@ViewInject(R.id.other_action_listview)
	ListView other_action_listview;

	Handler handler = new Handler();
	int[] listItemLangs = {R.string.other_aboutus, R.string.other_versioncheck, R.string.other_contact};
	int[] listItemIcons = {R.drawable.icon_aboutus, R.drawable.icon_versioncheck, R.drawable.icon_contact};
	Class<?>[] activities = {AboutUsActivity.class, VersionCheckActivity.class, ContactActivity.class};
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.topbar_other);
        
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, getData(), R.layout.list_item, 
				new String[]{"list_item_left_imgview", "list_item_center_txt", "list_item_right_imgview"}, 
				new int[]{R.id.list_item_left_imgview, R.id.list_item_center_txt, R.id.list_item_right_imgview});
        other_action_listview.setAdapter(simpleAdapter);
	}

	@OnItemClick(R.id.other_action_listview)
	public void actionPerformed_otherActionListViewItemClick(AdapterView<?> parent, View view, int pos, long id) {
//		Map<String, Object> item = (Map<String, Object>)parent.getItemAtPosition(pos);
//		System.out.println(parent.getItemAtPosition(pos));
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
    		map.put("list_item_center_txt", getResources().getString(listItemLangs[i]));
        	map.put("list_item_left_imgview", listItemIcons[i]);
        	map.put("list_item_right_imgview", R.drawable.icon_arrow);        	
        	list.add(map);
    	}
    	
    	return list;
    }
}
