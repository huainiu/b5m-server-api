package com.b5m.service.ontime.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class SkuProp implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7403896245999852990L;

	private String name;
	
	private Set<String> props;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addProp(String prop){
		if(props == null){
			props = new HashSet<String>();
		}
		props.add(prop);
	}

	public Set<String> getProps() {
		return props;
	}

	public void setProps(Set<String> props) {
		this.props = props;
	}
	
}
