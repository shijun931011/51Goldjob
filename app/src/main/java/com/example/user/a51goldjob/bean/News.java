package com.example.user.a51goldjob.bean;

/**
 * 金融资讯
 * @author yeq
 *
 */
public class News extends BaseBean {

	private String groupid; // 资讯分类ID
	private String groupname; // 资讯分类名称
	private String photourl; // 图片地址
	private String createdate; // 创建日期
	private String author; // 编辑
	private String source; // 来源
	private String infoid; // 资讯ID
	private String title; // 资讯标题
	private String previewText; // 内容
	private String pubdate; // 发布日期

	public String getId() {
		return getInfoid();
	}
	public String getInfoid() {
		return infoid;
	}
	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPreviewText() {
		return previewText;
	}
	public void setPreviewText(String previewText) {
		this.previewText = previewText;
	}
	public String getPubdate() {
		return pubdate;
	}
	public void setPubdate(String pubdate) {
		this.pubdate = pubdate;
	}
	public String getGroupid() {
		return groupid;
	}
	public void setGroupid(String groupid) {
		this.groupid = groupid;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getPhotourl() {
		return photourl;
	}
	public void setPhotourl(String photourl) {
		this.photourl = photourl;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
}
