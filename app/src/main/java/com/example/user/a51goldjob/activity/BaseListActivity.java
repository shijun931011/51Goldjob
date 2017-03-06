package com.example.user.a51goldjob.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.chyjr.goldjob.fr.bean.IBean;
import com.example.user.a51goldjob.R;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.utils.Https;
import com.example.user.a51goldjob.view.PullToRefreshListView;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * 列表界面
 * 
 * @author yeq
 *
 */
public abstract class BaseListActivity extends BaseActivity {
	
	public final static int LISTVIEW_ACTION_INIT = 0x01;
	public final static int LISTVIEW_ACTION_REFRESH = 0x02;
	public final static int LISTVIEW_ACTION_SCROLL = 0x03;
	public final static int LISTVIEW_ACTION_CHANGE_CATALOG = 0x04;

	public final static int LISTVIEW_DATA_MORE = 0x01;
	public final static int LISTVIEW_DATA_LOADING = 0x02;
	public final static int LISTVIEW_DATA_FULL = 0x03;
	public final static int LISTVIEW_DATA_EMPTY = 0x04;

	public final static int DEFAULT_PAGESIZE = 10;
	
	protected List<IBean> dataList = createDataList(); // 数据集
	protected int pageSize = getPageSize();

	protected View lvFavorite_footer;
	protected TextView lvFavorite_foot_more;
	protected ProgressBar lvFavorite_foot_progress;
	protected BaseAdapter adapter;
	protected int curLvDataState;
	protected int page = 1;
	
	protected OnItemClickListener onItemClickListener = new OnItemClickListener() {
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			actionPerformed_onItemClick(parent, view, position, id);
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    initListView();
	    getListView().setOnItemClickListener(onItemClickListener);
	}

	protected void initListView() {
		getListView().init(this, getHeadView(), getTopViews());
		this.adapter = createListViewAdapter();

        // ListView
    	lvFavorite_footer = getLayoutInflater().inflate(R.layout.listview_footer, null);
    	lvFavorite_foot_more = (TextView)lvFavorite_footer.findViewById(R.id.listview_foot_more);
    	lvFavorite_foot_progress = (ProgressBar)lvFavorite_footer.findViewById(R.id.listview_foot_progress);
    	getListView().addFooterView(lvFavorite_footer);//添加底部视图  必须在setAdapter前
    	getListView().setAdapter(this.adapter);
        
    	getListView().setOnScrollListener(new AbsListView.OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				getListView().onScrollStateChanged(view, scrollState);
				
				//数据为空--不用继续下面代码了
				if(dataList.size() == 0) return;
				
				//判断是否滚动到底部
				boolean scrollEnd = false;
				try {
					if(view.getPositionForView(lvFavorite_footer) == view.getLastVisiblePosition())
						scrollEnd = true;
				} catch (Exception e) {
					scrollEnd = false;
				}
				
				if(scrollEnd && curLvDataState==LISTVIEW_DATA_MORE)
				{
					getListView().setTag(LISTVIEW_DATA_LOADING);
					lvFavorite_foot_more.setText(R.string.load_ing);
					lvFavorite_foot_progress.setVisibility(View.VISIBLE);
					//当前pageIndex
					page = page + 1;
					loadList(page, LISTVIEW_ACTION_SCROLL);
				}
			}

			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				getListView().onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
			}
        });

    	getListView().setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
			public void onRefresh() {
				page = 1;
				loadList(page, LISTVIEW_ACTION_REFRESH);
            }
        });
    	
    	refreshList();
	}

	protected void refreshList() {
		loadList(1, LISTVIEW_ACTION_INIT);
	}
	
	private void loadList(int page, final int action) {
		
		if (page <= 0) {
			page = 1;
		}
		
		final String data = getJsonParams(page);

		Https.post(this, getUrlKey(), data, new RequestCallBackAdapters.InfoRequestCallBackAdapter<String>(this, true) {
			@SuppressWarnings({ "deprecation", "unchecked" })
			protected void _onSuccess(ResponseInfo<String> response) throws Exception {
				ResponseData responseData = ResponseData.create(response.result);
				
				List<IBean> list = (List<IBean>) parseDataList(responseData);
				int what = list.size();
				if(what >= 0){						
					//处理listview数据
					switch (action) {
					case LISTVIEW_ACTION_INIT:
					case LISTVIEW_ACTION_REFRESH:
					case LISTVIEW_ACTION_CHANGE_CATALOG:
						dataList.clear();//先清除原有数据
						dataList.addAll(list);
						break;
					case LISTVIEW_ACTION_SCROLL:
						if(dataList.size() > 0){
							for(IBean newItem : list){
								boolean b = false;
								for(IBean dataItem : dataList){
									if(newItem.getId().equalsIgnoreCase(dataItem.getId())){
										b = true;
										break;
									}
								}
								if(!b) dataList.add(newItem);
							}
						}else{
							dataList.addAll(list);
						}
						break;
					}	
					
					if(what < pageSize){
						curLvDataState = LISTVIEW_DATA_FULL;
						getListViewAdapter().notifyDataSetChanged();
						lvFavorite_foot_more.setText(R.string.load_full);
					}else if(what == pageSize){					
						curLvDataState = LISTVIEW_DATA_MORE;
						getListViewAdapter().notifyDataSetChanged();
						lvFavorite_foot_more.setText(R.string.load_more);
					}
				}
				else if(what == -1){
					//有异常--显示加载出错 & 弹出错误消息
					curLvDataState = LISTVIEW_DATA_MORE;
					lvFavorite_foot_more.setText(R.string.load_error);
				}
				if(dataList.size()==0){
					curLvDataState = LISTVIEW_DATA_EMPTY;
					lvFavorite_foot_more.setText(R.string.load_empty);
				} else {
					reviewDataList(dataList);
				}
				
				
				lvFavorite_foot_progress.setVisibility(View.GONE);
				if(action == LISTVIEW_ACTION_REFRESH){
					getListView().onRefreshComplete(getString(R.string.pull_to_refresh_update) + new Date().toLocaleString());
					getListView().setSelection(0);
				}else if(action == LISTVIEW_ACTION_CHANGE_CATALOG){
					getListView().onRefreshComplete();
					getListView().setSelection(0);
				}

				getListView().setVisibility(View.VISIBLE);
				afterLoaded();
			}
		});
	}

	protected boolean isInvalidItemClick(View view, int position) {
		return view == lvFavorite_footer;
	}

	protected List<IBean> createDataList() {
		return new ArrayList<IBean>(); // 数据集
	}
	
	protected int getPageSize() {
		return DEFAULT_PAGESIZE;
	}
	
	protected BaseAdapter getListViewAdapter() {
		return adapter;
	}
	
	protected List<?> getDataList() {
		return dataList;
	}
	
	@SuppressWarnings("unchecked")
	protected Object getBeanId(Object bean) {
		if (bean == null) {
			return null;
		}
		if (bean instanceof Map) {
			return ((Map<String, Object>) bean).get(getIdName());
		}
		if (bean instanceof IBean) {
			return ((IBean) bean).getId();
		}
		return null;
	}
	
	public BaseAdapter getAdapter() {
		return adapter;
	}
	
	/**
	 * 顶部附加的View
	 * @return
	 */
	protected LinearLayout getHeadView() {
		return null;
	}

	/**
	 * 顶部View，可用于在顶部新增如广告控件等其他View
	 * @return
	 */
	protected View[] getTopViews() {
		return null;
	}
	
	/**
	 * 获取IdName，如果结果集是Map雷系，需要指定
	 * @return
	 */
	protected String getIdName() {
		return null;
	}

	/**
	 * 列表项点击实现
	 * @param parent
	 * @param view
	 * @param position
	 * @param id
	 */
	protected void actionPerformed_onItemClick(AdapterView<?> parent, View view, int position, long id) {
	}

	/**
	 * 对当前数据做处理
	 * @param dataList
	 */
	protected void reviewDataList(List<?> dataList) {
	}

	/**
	 * 数据完全加载完毕后处理
	 */
	protected void afterLoaded() {
	}
	
	/**
	 * 报文解析为对象
	 * @param responseData
	 * @return
	 * @throws Exception
	 */
	protected abstract List<?> parseDataList(ResponseData responseData) throws Exception;

	/**
	 * Json格式参数
	 * @param page
	 * @return
	 */
	protected abstract String getJsonParams(int page);
	
	/**
	 * 创建列表Adapter
	 * @return
	 */
	protected abstract BaseAdapter createListViewAdapter();
	
	/**
	 * 重写得到列表View
	 * @return
	 */
	protected abstract PullToRefreshListView getListView();
	
	/**
	 * 重写得到UrlKey
	 * @return
	 */
	protected abstract String getUrlKey();

}
