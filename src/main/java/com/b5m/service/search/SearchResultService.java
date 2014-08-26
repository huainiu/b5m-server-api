package com.b5m.service.search;

import java.util.Map;

import com.b5m.base.common.spring.aop.Cache;
import com.b5m.bean.entity.filterattr.Attibute;
import com.b5m.sf1api.dto.res.SearchDTO;

public interface SearchResultService {
	/**
	 * 查询需要过滤的属性
	 * @param keyword
	 * @return
	 */
	@Cache(cacheEmpty = true, timeout = 72000)
	Map<String, Attibute> queryAttrFilterList(String keyword);

	/**
	 * 增加关键词人工排序
	 * @param name
	 * @return
	 */
	void sort(SearchDTO searchDTO, String name);
	
}