package com.example.user.a51goldjob.utils;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

	/**
	 * 空字符串验证
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str){
		boolean flag = false;
		if(str==null||"".equals(str)){
			flag = true;
		}
		return flag;
	}
	
	
	/**
	 * 手机号验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) { 
		Pattern p = null;
		Matcher m = null;
		boolean b = false;
		p = Pattern.compile("^[1][3,4,5,8][0-9]{9}$"); // 验证手机号
		m = p.matcher(str);
		b = m.matches(); 
		return b;
	}
	/**
	 * 电话号码验证
	 * 
	 * @param  str
	 * @return 验证通过返回true
	 */
	public static boolean isPhone(String str) { 
		Pattern p1 = null,p2 = null;
		Matcher m = null;
		boolean b = false;  
		p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$");  // 验证带区号的
		p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
		if(str.length() >9)
		{	m = p1.matcher(str);
 		    b = m.matches();  
		}else{
			m = p2.matcher(str);
 			b = m.matches(); 
		}  
		return b;
	}
	
	/**
	 * 比较日期
	 * @param beforeDate
	 * @param afterDate
	 * @return
	 */
	public static boolean compareDate(Date beforeDate, Date afterDate){
		boolean flag = false;
		if(beforeDate.after(afterDate)){
			flag = true;
		}
		return flag;
	}
}
