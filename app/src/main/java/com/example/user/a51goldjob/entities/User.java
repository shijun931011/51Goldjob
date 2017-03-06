package com.example.user.a51goldjob.entities;

import org.apache.commons.lang3.StringUtils;

import com.chyjr.goldjob.fr.entities.BaseEntity;
import com.chyjr.goldjob.fr.utils.Encrypter;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.Table;

/**
 * 用户实体
 * 
 * @author yeq
 * 
 */
@Table(name = "users")
public class User extends BaseEntity {
	
	public static final int DEFAULT_ID = 1;

	@Column(column = "username")
	private String username;
	
	@Column(column = "password")
	private String password;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean validate() {
		String username = getUsername();
		String password = getPassword();
		return StringUtils.isNotEmpty(username) && StringUtils.isNotEmpty(password);
	}
	
	public String getDecryptUsername() {
		return Encrypter.decrypt(getUsername());
	}
	
	public String getDecryptPassword() {
		return Encrypter.decrypt(getPassword());
	}
	
	public void setEecryptUsername(String username) {
		setUsername(Encrypter.encrypt(username));
	}
	
	public void setEecryptPassword(String password) {
		setPassword(Encrypter.encrypt(password));
	}
}
