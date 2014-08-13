package com.b5m.service.ontime;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.b5m.base.common.spring.aop.Cache;
import com.b5m.service.ontime.bean.OntimePriceBean;
import com.b5m.service.ontime.bean.SkuBean;
import com.b5m.service.ontime.bean.SkuRequest;

public interface OntimeService {
	
	@Cache
	JSONArray queryPrices(OntimePriceBean ontimePriceBean);
	
	@Cache
	SkuBean querySkuProp(SkuRequest skuRequest, String docId);
	
	String getPriceFromSku(String docId, String goosSpec);
	
	@Cache
	JSONObject queryDetail(String url);
	
}
