package com.b5m.bean.entity;

import java.io.Serializable;
import java.util.Date;

import com.b5m.dao.annotation.Column;
import com.b5m.dao.annotation.Id;
import com.b5m.dao.annotation.Table;
import com.b5m.dao.domain.ColType;

//接口访问日志
@Table("t_access_log_{date}")
public class AccessLog implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2450428931746634309L;
	
	@Id	
	private long id;
    //访问的来源服务
    @Column	
	private String server;
    //访问路径
    @Column
	private String path;
    //ip
    @Column	
	private String ip;
    //来源
    @Column(type = ColType.TEXT)
	private String ref;
    //访问时间
    @Column(name = "access_date")	
    private Date accessDate;
    //参数
    @Column(name = "params", type = ColType.TEXT)
    private String params;
    
    @Column(name = "respone_time")
    private Long responeTime;
    
    @Column(name = "description", type = ColType.TEXT)
    private String description;
    
    @Column(name = "exception")
    private String exception;
    
    public AccessLog(){}
    
	public AccessLog(String server, String path, String ip, String ref, Date accessDate, String params,
			Long responeTime, String description, String exception) {
		this.server = server;
		this.path = path;
		this.ip = ip;
		this.ref = ref;
		this.accessDate = accessDate;
		this.params = params;
		this.responeTime = responeTime;
		this.description = description;
		this.exception = exception;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public Date getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public Long getResponeTime() {
		return responeTime;
	}

	public void setResponeTime(Long responeTime) {
		this.responeTime = responeTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}
	
}
