package com.b5m.dubbo.cachefactory;

import com.alibaba.dubbo.cache.Cache;
import com.alibaba.dubbo.cache.support.AbstractCacheFactory;
import com.alibaba.dubbo.common.URL;

public class MemcacheFactory extends AbstractCacheFactory{

	@Override
	protected Cache createCache(URL url) {
		return new MCache();
	}

}
