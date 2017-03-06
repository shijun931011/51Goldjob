package com.example.user.a51goldjob.bean;

import java.util.Date;

import com.chyjr.goldjob.fr.utils.DateUtils;

public class Job extends BaseBean {
	
	private String applyid;
	private String jobid;
	private String uid;
	private String jobname;
	private String corpname;
	private String jobcity;
	private String jobtype;
	private String status;
	private String companyid;
	private String issuedate;
	private String createby;
	private String createdate;
	private String activeflag;
	
	/** 常量 */
	public static interface Constants {
		/** 职位类型 0=公司职位 */
		public static final String JOB_TYPE_0 = "0"; 
		/** 职位类型 1=猎头职位 */
		public static final String JOB_TYPE_1 = "1"; 
	}
	
	public String getIssuedatedt() {
		try {
			long timestamp = Long.valueOf(getIssuedate()).longValue();
			return DateUtils.format(new Date(timestamp), "yyyy-MM-dd");
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setIssuedatedt(String issuedate) {
		try {
			Date date = DateUtils.parse(issuedate, "yyyy-MM-dd");
			setIssuedate(String.valueOf(date.getTime()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getId() {
		return getApplyid();
	}

	public String getApplyid() {
		return applyid;
	}

	public void setApplyid(String applyid) {
		this.applyid = applyid;
	}

	public String getJobid() {
		return jobid;
	}

	public void setJobid(String jobid) {
		this.jobid = jobid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getCorpname() {
		return corpname;
	}

	public void setCorpname(String corpname) {
		this.corpname = corpname;
	}

	public String getJobcity() {
		return jobcity;
	}

	public void setJobcity(String jobcity) {
		this.jobcity = jobcity;
	}

	public String getJobtype() {
		return jobtype;
	}

	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getIssuedate() {
		return issuedate;
	}

	public void setIssuedate(String issuedate) {
		this.issuedate = issuedate;
	}

	public String getCreatedate() {
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getCreateby() {
		return createby;
	}

	public void setCreateby(String createby) {
		this.createby = createby;
	}

	public String getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(String activeflag) {
		this.activeflag = activeflag;
	}
}
