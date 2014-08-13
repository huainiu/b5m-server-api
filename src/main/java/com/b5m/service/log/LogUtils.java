package com.b5m.service.log;

import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.fastjson.JSONObject;
import com.b5m.base.common.spring.utils.ApplicationContextUtils;
import com.b5m.base.common.utils.WebTools;
import com.b5m.bean.entity.AccessLog;

public class LogUtils {
	private static final Log LOG = LogFactory.getLog(LogUtils.class);
	private static LogService logService = null;
	private static BlockingQueue<AccessLog> blockingQueue = new LinkedBlockingQueue<AccessLog>(500);
	
	public static LogService getLogService(){
		if(logService == null){
			synchronized (LogUtils.class) {
				if(logService == null){
					logService = ApplicationContextUtils.getBean(LogService.class);
					Executors.newSingleThreadExecutor().submit(new Runnable() {
						
						@Override
						public void run() {
							while(true){
								try {
									AccessLog accessLog = blockingQueue.poll();
									if(accessLog != null){
										logService.accessLog(accessLog);
									}
								} catch (Exception e) {
									LOG.error(e);
								}
							}
						}
					});
				}
			}
		}
		return logService;
	}
	
	public static void accessLog(final AccessLog accessLog){
		try {
			blockingQueue.put(accessLog);
		} catch (InterruptedException e1) {
		}
		//异步执行添加
	}
	
	public static void accessLog(String server, long startTime, HttpServletRequest req){
		String ip = WebTools.getIpAddr(req);
		String ref = referer(req);
        String api = req.getRequestURI();
        long now = System.currentTimeMillis();
        AccessLog accessLog = new AccessLog(server, api, ip, ref, new Date(), getParams(getParams(req)), now - startTime, "", "");
        getLogService().accessLog(accessLog);
	}
	
	public static void accessLog(String server, long startTime, HttpServletRequest req, Exception e){
		String ip = WebTools.getIpAddr(req);
		String ref = referer(req);
        String api = req.getRequestURI();
        long now = System.currentTimeMillis();
        AccessLog accessLog = new AccessLog(server, api, ip, ref, new Date(), getParams(getParams(req)), now - startTime, "", ExceptionUtils.getFullStackTrace(e));
        accessLog(accessLog);
	}
	
	public static String getParams(Map<String, String> params){
		if(params == null) return "";
		return JSONObject.toJSONString(params);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, String> getParams(HttpServletRequest req){
		Map<String, String> params = new HashMap<String, String>();
		Enumeration<String> enumeration = req.getParameterNames();
		while(enumeration.hasMoreElements()){
			String key = enumeration.nextElement();
			String value = req.getParameter(key);
			params.put(key, value);
		}
		return params;
	}
	
	public static String referer(HttpServletRequest req){
		String referer = req.getHeader("referer");
		if(StringUtils.isEmpty(referer)){
			referer = req.getHeader("Referer");
		}
		return referer;
	}
}
