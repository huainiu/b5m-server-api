package com.b5m.dubbo.cachefactory;

import com.alibaba.dubbo.cache.Cache;
import com.b5m.base.common.utils.cache.MemCachedUtils;

public class MCache implements Cache{

	@Override
	public void put(Object key, Object value) {
		if(key == null) return;
		MemCachedUtils.setCache(key.toString(), value);
	}

	@Override
	public Object get(Object key) {
		if(key == null) return null;
		return MemCachedUtils.getCache(key.toString());
	}

}
