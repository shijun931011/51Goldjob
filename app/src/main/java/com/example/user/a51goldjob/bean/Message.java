package com.example.user.a51goldjob.bean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.chyjr.goldjob.fr.utils.DateUtils;

public class Message extends BaseBean {

	private String datanum;
	private String smstype;
	private String username;
	private String corpname;
	private String content;
	private String sender;
	private String createdate;
	private String id;
	private String page;
	private String msg;
	private String readflag;
	
	public String getReadflag() {
		return readflag;
	}

	public void setReadflag(String readflag) {
		this.readflag = readflag;
	}

	public String getDate(String dateStr) {
		try {
			if (StringUtils.isEmpty(dateStr)) {
				return "";
			}
			if (!(NumberUtils.isNumber(dateStr))) {
				return "";
			}
			long timestamp = Long.valueOf(dateStr).longValue();
			return DateUtils.format(new Date(timestamp), "yyyy-MM-dd hh:mm:ss");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getDatanum() {
		return datanum;
	}
	public void setDatanum(String datanum) {
		this.datanum = datanum;
	}
	public String getSmstype() {
		return smstype;
	}
	public void setSmstype(String smstype) {
		this.smstype = smstype;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCorpname() {
		return corpname;
	}
	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getCreatedate() {
		//转换时间戳
		return getDate(createdate);
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
