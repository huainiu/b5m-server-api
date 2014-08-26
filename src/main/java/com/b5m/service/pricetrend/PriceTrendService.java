package com.b5m.service.pricetrend;

import com.b5m.base.common.spring.aop.Cache;
import com.b5m.service.hbase.bean.PriceTrend;

public interface PriceTrendService {
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 *
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年7月31日 上午11:03:09
	 *
	 * @param docIds
	 * @param range 几天的价格趋势
	 * @return
	 */
	@Cache
	String priceTrendType(String[] docIds, Integer range);
	
	@Cache
	PriceTrend singlePriceTrend(Integer range, String docId, boolean fill, String price);
	
}
