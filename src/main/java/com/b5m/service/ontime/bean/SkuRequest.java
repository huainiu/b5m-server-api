package com.b5m.service.ontime.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class SkuRequest implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1065600987357580615L;

	private int timeout = 5000;
	
	private String url;
	
	private String docid;
	
	private JSONArray keys = new JSONArray();
	
	public SkuRequest(String docid, String url){
		this.url = url;
		this.keys.add("Price");
		this.keys.add("Title");
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void addKeys(String ... keys){
		for(String key : keys){
			this.keys.add(key);
		}
	}

	public String toJsonString(){
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("timeout", timeout);
		jsonObject.put("url", url);
		jsonObject.put("keys", keys);
		return jsonObject.toJSONString();
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}
}
