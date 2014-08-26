package com.b5m.spring;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.DispatcherServlet;

import com.b5m.service.log.LogUtils;

public class AccessLogDispatcherServlet extends DispatcherServlet{
	private String serverName;
	private String[] includeLogPath;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1519181176645882840L;

	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long startTime = System.currentTimeMillis();
		super.doService(request, response);
		if(StringUtils.isEmpty(serverName)) {
			serverName = request.getServerName();
		}
		if(isInclude(includeLogPath, request)){
			LogUtils.accessLog(serverName, startTime, request);
		}
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		initIncludeLogPath(config);
		serverName = config.getInitParameter("serverName");
	}
	
	protected void initIncludeLogPath(ServletConfig config){
		String paths = config.getInitParameter("includeLogPath");
		if(!StringUtils.isEmpty(paths)){
			includeLogPath = StringUtils.split(paths, ",");
		}
	}
	
	protected boolean isInclude(String[] includeLogPath, HttpServletRequest request){
		if(includeLogPath == null) return false;
		String api = request.getRequestURI();
		if(api.endsWith(".css") || api.endsWith(".js") || api.endsWith(".jsp") || api.endsWith(".jpg") || api.endsWith(".png") || api.endsWith(".gif") || api.endsWith(".ico")) return false;
		for(String path : includeLogPath){
			if(path.startsWith("*") && path.endsWith("*")){
				if(api.indexOf(path) >= 0) return true;
			}
			if(path.startsWith("*")){
				if(api.endsWith(api)) return true;
			}
			if(path.endsWith("*")){
				if(api.startsWith(api)) return true;
			}
			if(api.equals(path)) return true;
		}
		return false;
	}

}
