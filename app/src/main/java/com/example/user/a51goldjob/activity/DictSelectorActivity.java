package com.example.user.a51goldjob.activity;

import java.util.List;

import org.apache.commons.lang3.StringUtils;


import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chyjr.goldjob.fr.mgr.IBiz;
import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.ActivityUtils.IIntentHandler;
import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.chyjr.goldjob.fr.utils.IntentUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.BeanAdapter;
import com.example.user.a51goldjob.bean.Dict;
import com.example.user.a51goldjob.mgr.DictMgr;
import com.example.user.a51goldjob.utils.ActivityUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * （通用）数据字典选择器
 * @author yeq
 *
 */
@ContentView(R.layout.activity_dict_selector)
public class DictSelectorActivity extends BaseActivity {
	private ImageView iconSelectedImg=null;
	public static interface Constants {
		public static final String PARAM_IO_DICT_KEY = "dictKey"; // 数据字典名称
		
		public static final String PARAM_IN_TOP_TITLE = "topTitle"; // 顶部标题
		public static final String PARAM_IN_HEADER_TITLE = "headerTitle"; // 头部标题
		
		public static final String PARAM_OUT_SELECT_ITEM = "selectItem"; // 选中的一项
		public static final String PARAM_IN_ITEM = "inItem"; // 外部选项
		public static final String POSITION_SELECTED="position"; //被选中的项目位置
	}
	
	String[] from = { "label" }; 
	int[] to = { R.id.dict_selector_list_item_txt };
	
	Handler handler = new Handler();
	
	DictMgr dictMgr = Mgr.get(DictMgr.class);
	
	@ViewInject(R.id.dict_selector_listview)
	ListView dict_selector_listview;
	
	@ViewInject(R.id.dict_selector_header_txt)
	TextView dict_selector_header_txt;
    private int showPosition=1000000000;
    private int gouPosition=1000000000;
    private String selectString="";
    private int tempPosition=10000000;
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    selectString=getIntent().getExtras().getString("selectStr");
	    tempPosition=getIntent().getExtras().getInt("toDict");
	    init();
	    
	}

	private List<Dict> dicts=null;
	protected void init() {
		String topTitle = IntentUtils.getStringExtra(this, Constants.PARAM_IN_TOP_TITLE);
		setBarTitle(topTitle);
		String headerTitle = IntentUtils.getStringExtra(this, Constants.PARAM_IN_HEADER_TITLE);
		dict_selector_header_txt.setText(headerTitle);
		final String dictKey = IntentUtils.getStringExtra(this, Constants.PARAM_IO_DICT_KEY);
		
		dictMgr.execute(this, new IBiz() {
			public void execute() {
			dicts = dictMgr.getCurrentDicts(getThis(), dictKey);
				if (CollectionUtils.isEmpty(dicts)) {
					// TODO 数据字典不存在，弹出错误信息
					finish();
					return;
				}
				initListView(dicts);
			}
		});
	}

	private int lastShowIndex=100000000;
	protected void initListView(List<Dict> dicts) {
		BeanAdapter adapter = new BeanAdapter(this, dicts, R.layout.activity_dict_selector_list_item, from, to);
//		for(int i=0;i<dicts.size();i++){
//			if(dicts.get(i)){
//				
//			}
//		}
		dict_selector_listview.setAdapter(adapter);
	//	dict_selector_listview.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}

	private int lastselectedIndex=1000000000;
	private int cur_position=10000000;
	private View tempVie=null;
	// 单击一项选中内容并返回筛选界面
	@OnItemClick(R.id.dict_selector_listview)
	public void actionPerformed_dictListItemClick(AdapterView<?> parent, View view, int pos, long id) {
		cur_position=pos;
		if(tempVie!=null){
			tempVie.findViewById(R.id.itemSelectedImg).setVisibility(View.INVISIBLE);	
		}
		tempVie=view;
		TextView txt=(TextView)view.findViewById(R.id.dict_selector_list_item_txt);
		BeanAdapter adapter = new BeanAdapter(this, dicts, R.layout.activity_dict_selector_list_item, from, to);
		dict_selector_listview.setAdapter(adapter);
		view.findViewById(R.id.itemSelectedImg).setVisibility(View.VISIBLE);
		final String dictKey = IntentUtils.getStringExtra(this, Constants.PARAM_IO_DICT_KEY);
		Dict dict = (Dict) dict_selector_listview.getAdapter().getItem(Long.valueOf(id).intValue());
		lastselectedIndex=Long.valueOf(id).intValue();
		
		if (dict == null) {
			return;
		}
		dict.setSelected(true);
		final String selectItem = dict.getValue();
		if (StringUtils.isEmpty(selectItem)) {
			return;
		}

		//back to lastPage
		ActivityUtils.returnActivity(handler, this, new IIntentHandler() {
			public void putExtra(Intent intent) {
				int item = getIntent().getExtras().getInt(Constants.PARAM_IN_ITEM);
				intent.putExtra(Constants.PARAM_IO_DICT_KEY, dictKey);
				intent.putExtra(Constants.PARAM_OUT_SELECT_ITEM, selectItem); // 行业名称
				intent.putExtra(Constants.PARAM_IN_ITEM, item); // 行业名称
				intent.putExtra("position",tempPosition);

			}
		});
	}
	protected BeanAdapter getAdapter() {
		return (BeanAdapter) dict_selector_listview.getAdapter();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
	}	
		
	
	
}
