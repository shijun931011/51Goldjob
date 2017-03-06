package com.example.user.a51goldjob.bean;

import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;


import com.chyjr.goldjob.fr.utils.DateUtils;
import com.chyjr.goldjob.fr.utils.Encrypter;
import com.example.user.a51goldjob.utils.JsonUtils;

/**
 * 请求报文
 * @author yeq
 *
 */
public class RequestData {

	private String header;
	private String body;
	private String dateStr = DateUtils.current("MM-dd HH:mm:ss"); // 交易时间
	// 加密串：有#分隔
	// 交易码
	// 报文格式
	// 报文长度
	// 终端类型
	// 报文内容{数据内容,p:无#分隔加密串}
	
	public RequestData(JSONObject jsonObject, String transcode) {
		this(jsonObject, transcode, true);
	}
	
	public RequestData(JSONObject jsonObject, String transcode, boolean hasBody) {
		String bodyLength = getBodyLength(jsonObject); // 报文长度
		
		if (hasBody) {
			String p = String.format("%s%s%s%s", transcode, "j", bodyLength, dateStr); 
			JsonUtils.put(jsonObject, "p", Encrypter.encrypt(p)); // 报文内容p字段内容
		}

		String encrypt = getEncrypt(transcode, bodyLength); // 报文头加密串
		
		header = String.format("%s%s%s%s%s", encrypt, transcode, "j", bodyLength, "a"); // 整个报文头
		Log.i(this.getClass().getName(), header);
		
		body = getJsonObjectString(jsonObject); // 整个报文内容
		Log.i(this.getClass().getName(), body);
	}
	
	private String getJsonObjectString(JSONObject jsonObject) {
		JSONArray names = jsonObject.names();
		return names == null ? "" :jsonObject.toString();
	}
	
	private String getBodyLength(JSONObject jsonObject) {
		String bodyText = jsonObject.toString();
		JSONArray names = jsonObject.names();
		if (names == null) {
			bodyText = "";
		}
		String bodyTextLengthStr = String.valueOf(bodyText.length());
		StringBuilder builder = new StringBuilder();
		for (int i = 7; i > bodyTextLengthStr.length(); i--) {
			builder.append("0");
		}
		bodyTextLengthStr = builder.append(bodyTextLengthStr).toString();
		return bodyTextLengthStr;
	}
	
	private String getEncrypt(String transcode, String bodyTextLengthStr) {
		String encryptStr = String.format("%s#%s#%s#%s", transcode, "j", bodyTextLengthStr, dateStr);
		encryptStr = Encrypter.encrypt(encryptStr);
		StringBuilder builder = new StringBuilder(encryptStr);
		for (int i = 50; i > encryptStr.length(); i--) {
			builder.append(" ");
		}
		encryptStr = builder.toString();
		return encryptStr;
	}
	
	public String getHeader() {
		return header;
	}
	
	public String getBody() {
		return body;
	}
	
	public String getData() {
		return String.format("%s%s", header, body);
	}
	
	public static String getData(JSONObject jsonObject, String transcode) {
		return getData(jsonObject, transcode, true);
	}

	public static String getData(JSONObject jsonObject, String transcode, boolean hasBody) {
		RequestData data = new RequestData(jsonObject, transcode, hasBody);
		return data.getData();
	}
}
