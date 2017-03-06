package com.example.user.a51goldjob.bean;

/**
 * 应用配置
 * @author yeq
 *
 */
public class AppConstants {

	/** 1、生产配置prod, 测试配置 test TODO 暂时只有生产配置 */
	public static final String ENVIRONMENT = "prod";
	
	/** 编码 */
	public static final String ENCODING = "UTF-8";
	
	/** HTML MINE TYPE */
	public static final String MINE_TYPE_HTML = "text/html";

	/** URL 前缀 */
	public static final String HTTP = "http://";
	
	/** 错误日志存放路径 */
	public static final String ERROR_LOG_DIR = "/sdcard/crash/";

	/** 动作：编辑 */
	public static final String ACT_EDIT = "act_edit";
	
	/** 动作：创建 */
	public static final String ACT_CREATE = "act_create";
}
