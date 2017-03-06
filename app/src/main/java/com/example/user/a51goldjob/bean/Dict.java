package com.example.user.a51goldjob.bean;

import java.io.Serializable;

/**
 * 数据字典
 * @author yeq
 *
 */
public class Dict extends BaseBean implements Serializable {

	private static final long serialVersionUID = -9060036869495517527L;

	private int sortindex; // 显示排序
	private String ioc; // 图片地址
	private String color; // 色调
	private String value; // 值
	private String label; // 显示名字
	private boolean isSelected=false;
	
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getSortindex() {
		return sortindex;
	}
	public void setSortindex(int sortindex) {
		this.sortindex = sortindex;
	}
	public String getIoc() {
		return ioc;
	}
	public void setIoc(String ioc) {
		this.ioc = ioc;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String toString() {
		return String.format("%s,%s,%s,%s,%s", sortindex, ioc, color, value, label);
	}
	
	public String getId() {
		return String.valueOf(sortindex);
	}
}
