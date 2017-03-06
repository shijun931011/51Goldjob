package com.example.user.a51goldjob.mgr;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import android.app.Activity;


import com.chyjr.goldjob.fr.mgr.IBiz;
import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.example.user.a51goldjob.adapter.RequestCallBackAdapters;
import com.example.user.a51goldjob.AppContext;
import com.example.user.a51goldjob.bean.Dict;
import com.example.user.a51goldjob.bean.RequestData;
import com.example.user.a51goldjob.bean.ResponseData;
import com.example.user.a51goldjob.bean.Transcodes;
import com.example.user.a51goldjob.utils.Https;
import com.google.gson.Gson;

import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.http.ResponseInfo;

/**
 * 数据字典管理
 * @author yeq
 *
 */
public class DictMgr extends BaseMgr {
	
	/**
	 * 获取数据字典
	 * 
	 * @param activity
	 * @param key
	 * @return
	 */
	public List<Dict> getCurrentDicts(Activity activity, String key) {
		AppContext ctx = getAppContext(activity);
		return ctx.getDicts(key);
	}
	
	/**
	 * 获取所有数据字典
	 * 
	 * @param activity
	 * @return
	 */
	public Map<String, List<Dict>> getAll(Activity activity) {
		AppContext ctx = getAppContext(activity);
		return ctx.getDictMaps();
	}
	
	/**
	 * 执行一个业务逻辑
	 * 
	 * @param activity
	 * @param biz
	 */
	public void execute(Activity activity, IBiz biz) {
		if (CollectionUtils.isEmpty(getAll(activity))) {
			initDict(activity, biz);
			return;
		}
		biz.execute();
	}
	
	/**
	 * 初始化数据字典
	 * 
	 * @param activity
	 * @param biz
	 */
	public void initDict(final Activity activity, final IBiz biz) {
		JSONObject jsonObject = new JSONObject();
		final String data = RequestData.getData(jsonObject, Transcodes.TC_DICT_LIST, false);
		
		Https.post(activity,  Transcodes.TC_DICT_LIST, data, new RequestCallBackAdapters.RequestCallBackAdapter<String>() {
			public void _onSuccess(ResponseInfo<String> response) {
				if (StringUtils.isEmpty(response.result)) {
					return;
				}
				
				ResponseData responseData = ResponseData.create(response.result);
				String returnData = responseData.getBodyPropertyValue("returnData");
				Gson gson = new Gson();
				Map<String, List<Dict>> dictMaps = 
						gson.fromJson(returnData, new TypeToken<Map<String, List<Dict>>>(){}.getType());
				
				AppContext.get(activity).setDictMaps(dictMaps);
				
				if (biz != null) {
					biz.execute();
				}
			}
		});
	}
}
