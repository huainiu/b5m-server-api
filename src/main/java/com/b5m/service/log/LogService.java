package com.b5m.service.log;

import com.b5m.bean.entity.AccessLog;

public interface LogService {
	
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * 添加访问日志
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年8月7日 上午10:39:17
	 *
	 * @param accessLog
	 */
	void accessLog(AccessLog accessLog);
	
}
