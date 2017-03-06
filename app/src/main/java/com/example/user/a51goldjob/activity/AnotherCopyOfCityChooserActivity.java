package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chyjr.goldjob.fr.mgr.IBiz;
import com.chyjr.goldjob.fr.mgr.Mgr;
import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.BeanAdapter;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Dict;
import com.example.user.a51goldjob.bean.DictConstants;
import com.example.user.a51goldjob.mgr.AppMgr;
import com.example.user.a51goldjob.mgr.DictMgr;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

/**
 * 城市选择界面
 * @author yeq
 *
 */
@ContentView(R.layout.activity_city_chooser)
public class AnotherCopyOfCityChooserActivity extends BaseActivity {
	
	@ViewInject(R.id.city_chooser_current_city_txt)
	TextView city_chooser_current_city_txt;
	
	@ViewInject(R.id.city_chooser_cities_listview)
	ListView city_chooser_cities_listview;
	
	String[] from = { "label" };
	int[] to = { R.id.dict_selector_list_item_txt };
	
	AppMgr appMgr = Mgr.get(AppMgr.class);
	DictMgr dictMgr = Mgr.get(DictMgr.class);

	private String selectedStr="";
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setBarTitle(R.string.city_chooser);
	    if(getIntent().getExtras()!=null){
		    selectedStr=getIntent().getExtras().getString("selectStr");
	    }else{
	    	Toast.makeText(this, "显示了", Toast.LENGTH_SHORT).show();
	    }

	    init();
	}

	private List<Dict> dicts=null;
	protected void init() {
		dictMgr.execute(this, new IBiz() {
			public void execute() {
				 dicts = dictMgr.getCurrentDicts(getThis(), DictConstants.DICT_COMP_CITY);
				if (CollectionUtils.isEmpty(dicts)) {
					// TODO 数据字典不存在，弹出错误信息
					finish();
					return;
				}
				List<Dict> alldicts = new ArrayList<Dict>();
				if(dicts!=null&&dicts.size()>0){
					Dict first = dicts.get(0);
					if(!getString(R.string.national_city).equals(first.getLabel())){
						Dict dict_all = new Dict();//全部选项
						dict_all.setLabel(getString(R.string.national_city));
						dict_all.setValue(getString(R.string.national_city));
						dict_all.setSortindex(01);
						alldicts.add(dict_all);
						//alldicts.addAll(dicts);
					}else{
						alldicts.addAll(dicts);
					}
				}
				initListView(alldicts);
			}
		});
	}

	protected void initListView(List<Dict> dicts) {
		BeanAdapter adapter = new BeanAdapter(this, dicts, R.layout.activity_dict_selector_list_item, from, to);
		city_chooser_cities_listview.setAdapter(adapter);
	}

	protected void onResume() {
		super.onResume();	
		AppContext appContext = AppContext.get(this);
	    String city = appContext.getCity(getString(R.string.national_city));
	    city_chooser_current_city_txt.setText(city);
	}
	private View tempView=null;
	@OnItemClick(R.id.city_chooser_cities_listview)
	public void actionPerformed_cityChooserCitiesItemOnClick(AdapterView<?> parent, View view, int pos, long id) {
			
			TextView txt=(TextView)view.findViewById(R.id.dict_selector_list_item_txt);
			init();
			if(tempView!=null){
				tempView.findViewById(R.id.itemSelectedImg).setVisibility(View.INVISIBLE);	
			}
			tempView=view;
			
//	
//			BeanAdapter adapter = new BeanAdapter(this, dicts, R.layout.activity_dict_selector_list_item, from, to,txt.getText().toString());
//			city_chooser_cities_listview.setAdapter(adapter);

			view.findViewById(R.id.itemSelectedImg).setVisibility(View.VISIBLE);
			tempView=view;
			Dict dict = (Dict) city_chooser_cities_listview.getItemAtPosition(Long.valueOf(id).intValue());
			if (dict == null) {
				return;
			}
			String value = dict.getValue();
			if (StringUtils.isEmpty(value)) {
				return;
			}
			city_chooser_current_city_txt.setText(value);
			AppContext.get(this).setCityDict(dict);
			finish();
		
		
	}
}
