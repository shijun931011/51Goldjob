package com.example.user.a51goldjob.bean;

import java.util.Date;

import com.chyjr.goldjob.fr.utils.DateUtils;
import com.chyjr.goldjob.fr.utils.StringUtils;

/**
 * 猎头职位
 * @author yeq
 *
 */
public class HunterJob extends BaseBean {

	private String jobname;
	private String corpid;
	private String createby;
	private String createdate;
	private String corpname;
	private String id;
	private String jobtitle;
	private String category;
	private String username;
	private String jobtype;
	private String nature;
	private String updateby;
	private String hhid;
	private String showname;
	private String activeflag;
	private String salary;
	private String updatedate;
	private String industry;
	private String jobcity;
	
	public String getJobid() {
		return id;
	}
	
	public String getPrettySalary() {
		String salary = getSalary();
		if (salary == null) {
			salary = " ";
		}
		return StringUtils.abbreviate(String.format("%s  ", salary), 12);
	}
	
	public String getCreatedatedt() {
		try {
			long timestamp = Long.valueOf(getCreatedate()).longValue();
			return DateUtils.format(new Date(timestamp), "yyyy-MM-dd");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setCreatedatedt(String issuedate) {
		try {
			Date date = DateUtils.parse(issuedate, "yyyy-MM-dd");
			setCreatedate(String.valueOf(date.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
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

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJobtype() {
		return jobtype;
	}

	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getUpdateby() {
		return updateby;
	}

	public void setUpdateby(String updateby) {
		this.updateby = updateby;
	}

	public String getHhid() {
		return hhid;
	}

	public void setHhid(String hhid) {
		this.hhid = hhid;
	}

	public String getShowname() {
		return showname;
	}

	public void setShowname(String showname) {
		this.showname = showname;
	}

	public String getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(String activeflag) {
		this.activeflag = activeflag;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public String getUpdatedate() {
		return updatedate;
	}

	public void setUpdatedate(String updatedate) {
		this.updatedate = updatedate;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getJobcity() {
		return jobcity;
	}

	public void setJobcity(String jobcity) {
		this.jobcity = jobcity;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
