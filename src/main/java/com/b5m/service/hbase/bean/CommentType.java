package com.b5m.service.hbase.bean;

import java.io.Serializable;

public enum CommentType implements Serializable{

	BAD("bad"), GOOD("good"), NORMAL("normal"), ALL("all");

	private String s;

	private CommentType(String s) {
		this.s = s;
	}

	@Override
	public String toString() {
		return s;
	}
	
}
