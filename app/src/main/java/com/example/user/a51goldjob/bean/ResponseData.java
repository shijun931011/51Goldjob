package com.example.user.a51goldjob.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.example.user.a51goldjob.utils.JsonUtils;


/**
 * 返回报文
 * @author yeq
 *
 */
public class ResponseData {
	
	public static final String RC_SUCCESSED = "AAAAAAA"; // 成功
	public static final String RC_ERR_DATA = "0000001"; // 报文不完全
	public static final String RC_ERR_PARSER = "0000002"; // 报文解析失败
	
	private String header;
	private String body;
	private String data;
	private JSONObject jsonObject;

	private String transcode;
	private String packetformat;
	private String length;

	private String returnCode;
	private String returnMsg;
	private String token;
	private String tokenKey;
	
	public ResponseData(String data) {
		this.data = data;

		transcode = data.substring(0, 5);
		packetformat = data.substring(5, 6);
		length = data.substring(7, 13);

		header = String.format("%s%s%s", transcode, packetformat, length);
		body = data.substring(13, data.length());

		initJsonObject();

		Log.i(this.getClass().getName(), this.data);
		
//		System.out.println( transcode + "," + packetformat + "," + packetformat + "," + length + "," + body + ",");
//		System.out.println( returnCode + "," + returnMsg + "," + token + "," + tokenKey );
	}
	
	private void initJsonObject() {
		try {
			jsonObject = new JSONObject(body.trim());
			returnCode = getBodyPropertyValue("returnCode");
			returnMsg = getBodyPropertyValue("returnMsg");
			token = getBodyPropertyValue("token");
			tokenKey = getBodyPropertyValue("tokenKey");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	public static ResponseData create(String data) {
		return new ResponseData(data);
	}
	
	/**
	 * 是否成功
	 * @return
	 */
	public boolean isSuccess() {
		return RC_SUCCESSED.equalsIgnoreCase(getReturnCode());
	}
	
	public String getBodyPropertyValue(String name) {
		return JsonUtils.getString(jsonObject, name);
	}
	
	public JSONObject getBodyPropertyJsonObject(String name) {
		try {
			if (jsonObject.has(name)) {
				return jsonObject.getJSONObject(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public JSONArray getBodyPropertyList(String name) {
		try {
			if (jsonObject.has(name)) {
				return jsonObject.getJSONArray(name);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public JSONObject getJsonObject() {
		return jsonObject;
	}

	public void setJsonObject(JSONObject jsonObject) {
		this.jsonObject = jsonObject;
	}

	public String getTranscode() {
		return transcode;
	}

	public void setTranscode(String transcode) {
		this.transcode = transcode;
	}

	public String getPacketformat() {
		return packetformat;
	}

	public void setPacketformat(String packetformat) {
		this.packetformat = packetformat;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getReturnMsg() {
		return returnMsg;
	}

	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTokenKey() {
		return tokenKey;
	}

	public void setTokenKey(String tokenKey) {
		this.tokenKey = tokenKey;
	}
}
