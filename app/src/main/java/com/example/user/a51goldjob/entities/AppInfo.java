package com.example.user.a51goldjob.entities;

import com.chyjr.goldjob.fr.entities.BaseEntity;
import com.chyjr.goldjob.fr.utils.Encrypter;
import com.lidroid.xutils.db.annotation.Column;
import com.lidroid.xutils.db.annotation.NotNull;
import com.lidroid.xutils.db.annotation.Table;
import com.lidroid.xutils.db.annotation.Unique;

/**
 * 应用本地信息
 * 
 * @author yeq
 */
@Table(name = "app_infos")
public class AppInfo extends BaseEntity {

	@Column(column="name")
	@NotNull
	@Unique
	private String name;

	@Column(column="value")
	@NotNull
	private String value;
	
	public String getName() {
		return Encrypter.decrypt(name);
	}

	public void setName(String name) {
		this.name = Encrypter.encrypt(name);
	}

	public String getValue() {
		return Encrypter.decrypt(value);
	}

	public void setValue(String value) {
		this.value = Encrypter.encrypt(value);
	}
}
