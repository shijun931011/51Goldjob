package com.example.user.a51goldjob.bean;

import java.util.Date;

import com.chyjr.goldjob.fr.utils.DateUtils;

/**
 * 职位收藏
 * @author yeq
 *
 */
public class JobFavorite extends BaseBean {

	private String jobcollectionid;
	private String jobid;
	private String uid;
	private String jobname;
	private String corpname;
	private String jobcity;
	private String companyid;
	private String jobtype;
	private String issuedate;
	private String status;
	private String createby;
	private String createdate;
	private String activeflag;

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
		return jobcollectionid;
	}

	public String getJobcollectionid() {
		return jobcollectionid;
	}

	public void setJobcollectionid(String jobcollectionid) {
		this.jobcollectionid = jobcollectionid;
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

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getJobtype() {
		return jobtype;
	}

	public void setJobtype(String jobtype) {
		this.jobtype = jobtype;
	}

	public String getIssuedate() {
		return issuedate;
	}

	public void setIssuedate(String issuedate) {
		this.issuedate = issuedate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getActiveflag() {
		return activeflag;
	}

	public void setActiveflag(String activeflag) {
		this.activeflag = activeflag;
	}
}
