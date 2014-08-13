package com.b5m.dubbo.filter;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.Filter;
import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.fastjson.JSON;
import com.b5m.base.common.Lang;
import com.b5m.base.common.spring.aop.Cache;
import com.b5m.base.common.spring.aop.LocalCache;
import com.b5m.base.common.utils.cache.MemCachedUtils;

@Activate(group = {Constants.CONSUMER, Constants.PROVIDER}, value = "B5mCache")
public class B5mCacheFilter implements Filter{
	private static LocalCache localCache = new LocalCache();

	@Override
	public Result invoke(Invoker<?> invoker, Invocation inv) throws RpcException {
		try {
			Method method = invoker.getInterface().getMethod(inv.getMethodName(), inv.getParameterTypes());
			Cache cache = method.getAnnotation(Cache.class);
			if(cache == null){
				return invoker.invoke(inv);
			}
			String key = cache.key();
			if(StringUtils.isEmpty(key)){
				key = DigestUtils.md5Hex(createKey(invoker.getInterface().getSimpleName(), inv.getMethodName(), inv.getArguments()));
			}
			Object result = null;
			if(cache.localCache()){
				result = localCache.get(key);
			}
			if(result == null){
				result = MemCachedUtils.getCache(key);
			}
			if(result != null){
				return new RpcResult(result);
			}
			if(result == null){
				synchronized (key) {
					result = invoker.invoke(inv).getValue();
					if(!cache.cacheEmpty()){
						if(result == null){
							return new RpcResult(result);
						}
						if(Lang.isList(result) && ((List<?>) result).isEmpty()){
							return new RpcResult(result);
						}
						if(Lang.isMap(result) && ((Map) result).isEmpty()){
							return new RpcResult(result);
						}
					}
					MemCachedUtils.setCache(key, result, (int)cache.timeout());
					if(cache.localCache()){
						localCache.put(key, result, cache.timeout() * 1000000);
					}
				}
			}
	        return new RpcResult(result);
		} catch (SecurityException e1) {
			e1.printStackTrace();
		} catch (NoSuchMethodException e1) {
			e1.printStackTrace();
		}
		return invoker.invoke(inv);
	}
	
	private String createKey(String className, String methodName, Object[] args){
		StringBuilder key = new StringBuilder();
		key.append(className);
		key.append("_");
		key.append(methodName);
		key.append("_");
		for(Object arg : args){
			if(arg instanceof HttpServletRequest){
				continue;
			}
			if(arg instanceof HttpServletResponse){
				continue;
			}
			key.append(JSON.toJSONString(arg));
			key.append("_");
		}
		return key.toString();
	}

}
