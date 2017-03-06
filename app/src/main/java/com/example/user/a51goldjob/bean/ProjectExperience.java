package com.example.user.a51goldjob.bean;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.chyjr.goldjob.fr.utils.DateUtils;

public class ProjectExperience {
	private String resumeid;
	public String getResumeid() {
		return resumeid;
	}

	public void setResumeid(String resumeid) {
		this.resumeid = resumeid;
	}

	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
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

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getProjectremark() {
		return projectremark;
	}

	public void setProjectremark(String projectremark) {
		this.projectremark = projectremark;
	}

	public String getTrustremark() {
		return trustremark;
	}

	public void setTrustremark(String trustremark) {
		this.trustremark = trustremark;
	}

	public String getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(String activeflag) {
		this.activeflag = activeflag;
	}

	private String projectid;
	private String starttime;
	private String endtime;
	private String projectname;
	private String projectremark;
	private String trustremark;
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
	
}
