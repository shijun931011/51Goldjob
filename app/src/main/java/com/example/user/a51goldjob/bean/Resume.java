package com.example.user.a51goldjob.bean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.chyjr.goldjob.fr.utils.DateUtils;

public class Resume extends BaseBean {
	
	private String resumeid;
	private String whocomp;
	private String whohh;
	private String userid;
	private String percent;
	private String resumename;
	private String companyid;
	private String updateby;
	private String createby;
	private String createdate;
	private String updatedate;
	private String activeflag;
	private String defaults;

	public String getCreatedatedt() {
		try {
			String dateStr = getCreatedate();
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
	
	public String getUpdatedatedt() {
		try {
			String dateStr = getUpdatedate();
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

	public String getId() {
		return resumeid;
	}

	public String getResumeid() {
		return resumeid;
	}

	public void setResumeid(String resumeid) {
		this.resumeid = resumeid;
	}

	public String getWhocomp() {
		return whocomp;
	}

	public void setWhocomp(String whocomp) {
		this.whocomp = whocomp;
	}

	public String getWhohh() {
		return whohh;
	}

	public void setWhohh(String whohh) {
		this.whohh = whohh;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getResumename() {
		return resumename;
	}

	public void setResumename(String resumename) {
		this.resumename = resumename;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	public String getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(String activeflag) {
		this.activeflag = activeflag;
	}

	public String getDefaults() {
		return defaults;
	}

	public void setDefaults(String defaults) {
		this.defaults = defaults;
	}

}
