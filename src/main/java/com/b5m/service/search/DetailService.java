package com.b5m.service.search;

import com.alibaba.fastjson.JSONObject;

public interface DetailService {
	
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * 获取详情
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年8月21日 下午4:54:29
	 *
	 * @param docId
	 * @return
	 */
	JSONObject getItem(String docId);
	
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * 获取详情
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年8月21日 下午4:54:40
	 *
	 * @param collection
	 * @param docId
	 * @return
	 */
	JSONObject getItem(String collection, String docId);
	
}
