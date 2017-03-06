package com.example.user.a51goldjob.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import com.chyjr.goldjob.fr.utils.CollectionUtils;
import com.chyjr.goldjob.fr.utils.IntentUtils;
import com.example.user.a51goldjob.bean.MapBean;

/**
 * Json 工具类
 * @author yeq
 *
 */
public class JsonUtils {

	public static String getString(JSONObject jsonObject, String name) {
		try {
			if (jsonObject.has(name)) {
				return jsonObject.getString(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String put(JSONObject jsonObject, String key, String value) {
		try {
			jsonObject.put(key, value);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return value;
	}
	
	public static class JsonListMapResult<T extends Map<String, Object>> {
		
		private List<String> columns = new ArrayList<String>();
		private List<T> list = new ArrayList<T>();

		public List<T> getList() {
			return list;
		}

		public void setList(List<T> list) {
			this.list = list;
		}

		public List<String> getColumns() {
			return columns;
		}

		public void setColumns(List<String> columns) {
			this.columns = columns;
		}
	}
	
	public static JsonListMapResult<MapBean<String, Object>> parseToMaps(String jsonStr, String idName) {
		JsonListMapResult<MapBean<String, Object>> result = new JsonListMapResult<MapBean<String, Object>>();
		try {
			JSONArray array = new JSONArray(jsonStr);
			if (array == null || array.length() <= 0) {
				return result;
			}

			result.getColumns().addAll(getColumns(array.getJSONObject(0)));
			
			MapBean<String, Object> map = null;
			for (int i=0; i<array.length(); i++) {
				map = getMap(array.getJSONObject(i), idName);
				if (CollectionUtils.isEmpty(map)) {
					continue;
				}
				result.getList().add(map);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static List<String> getColumns(JSONObject jsonObject) {
		List<String> columns = new ArrayList<String>();
		try {
			JSONArray names = jsonObject.names();
			String name = null;
			for (int j=0; j<names.length(); j++) {
				name = names.getString(j);
				columns.add(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return columns;
	}
	
	public static MapBean<String, Object> getMap(JSONObject jsonObject, String idName) {
		MapBean<String, Object> map = new MapBean<String, Object>(idName);
		try {
			JSONArray names = jsonObject.names();
			String name = null;
			for (int j=0; j<names.length(); j++) {
				name = names.getString(j);
				map.put(name, jsonObject.getString(name));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String, Object> parseToMap(String jsonStr, String idName) {
		try {
			JSONObject jsonObject = new JSONObject(jsonStr);
			return getMap(jsonObject, idName);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void putExtrasParams(Activity activity, JSONObject jsonObject, String[] params) {
		putExtrasParams(activity, jsonObject, params, new ExtrasParam());
	}
	
	public static interface IExtrasParam {
		String getValue(Activity activity, String param);
	}
	
	public static class ExtrasParam implements IExtrasParam {
		public String getValue(Activity activity, String param) {
			return IntentUtils.getStringExtra(activity, param);
		}
	}
	
	public static void putExtrasParams(Activity activity, JSONObject jsonObject, String[] params, IExtrasParam extrasParam) {
		String value = null;
		for (String param : params) {
			value = extrasParam.getValue(activity, param);
			put(jsonObject, param, value);
		}
	}
}
