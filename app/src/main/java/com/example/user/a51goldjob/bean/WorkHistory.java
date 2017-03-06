package com.example.user.a51goldjob.bean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.chyjr.goldjob.fr.utils.DateUtils;

public class WorkHistory {

	private String resumeid;
	private String workid;
	private String starttime;
	private String endtime;
	private String corpname;
	private String corptype;
	private String corpscale;
	private String industry;
	private String department;
	private String postcode;
	private String workremark;
	private String activeflag;

	public String getStarttimedt() {
		try {
			String dateStr = getStarttime();
			if (StringUtils.isEmpty(dateStr)) {
				return "";
			}
			if (!(NumberUtils.isNumber(dateStr))) {
				return "";
			}
			long timestamp = Long.valueOf(dateStr).longValue();
			return DateUtils.format(new Date(timestamp), "yyyy-MM-dd");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getEndtimedt() {
		try {
			String dateStr = getEndtime();
			if (StringUtils.isEmpty(dateStr)) {
				return "";
			}
			if (!(NumberUtils.isNumber(dateStr))) {
				return "";
			}
			long timestamp = Long.valueOf(dateStr).longValue();
			return DateUtils.format(new Date(timestamp), "yyyy-MM-dd");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getResumeid() {
		return resumeid;
	}

	public void setResumeid(String resumeid) {
		this.resumeid = resumeid;
	}

	public String getWorkid() {
		return workid;
	}

	public void setWorkid(String workid) {
		this.workid = workid;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getCorptype() {
		return corptype;
	}

	public void setCorptype(String corptype) {
		this.corptype = corptype;
	}

	public String getCorpscale() {
		return corpscale;
	}

	public void setCorpscale(String corpscale) {
		this.corpscale = corpscale;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getWorkremark() {
		return workremark;
	}

	public void setWorkremark(String workremark) {
		this.workremark = workremark;
	}

	public String getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(String activeflag) {
		this.activeflag = activeflag;
	}
}
