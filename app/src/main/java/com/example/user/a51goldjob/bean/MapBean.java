package com.example.user.a51goldjob.bean;

import java.util.HashMap;
import java.util.Map;

import com.chyjr.goldjob.fr.bean.IBean;

public class MapBean<K, V> extends HashMap<K, V> implements Map<K, V> , IBean {
	
	private static final long serialVersionUID = 1716268794112221838L;
	private K idName;
	
	public MapBean() {
	}
	
	public MapBean(K idName) {
		this.idName = idName;
	}
	
	public K getIdName() {
		return idName;
	}

	public String getId() {
		V idObject = get(getIdName());
		if (idObject == null) {
			return null;
		}
		return idObject.toString();
	}

}
