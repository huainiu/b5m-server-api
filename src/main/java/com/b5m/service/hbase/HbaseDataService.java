package com.b5m.service.hbase;

import java.util.Date;
import java.util.List;

import com.b5m.base.common.spring.aop.Cache;
import com.b5m.dao.domain.page.PageView;
import com.b5m.service.hbase.bean.CommentType;
import com.b5m.service.hbase.bean.HComment;
import com.b5m.service.hbase.bean.PriceTrend;

/**
 * @author echo
 * 获取hbase数据接口
 */
public interface HbaseDataService {
	@Cache
	String getProductDetail(String docId) throws Exception;

	@Cache
	PageView<HComment> queryCommentPage(String docId, CommentType type, Integer pageSize, Integer pageCode) throws Exception;
	
	@Cache
	long getCommentSize(String docId, CommentType type) throws Exception;

	@Cache
	List<String> getTag(String docId) throws Exception;
	
	@Cache
	PriceTrend getPriceTrend(Date start, Date end, String docId);
	
	@Cache
	List<PriceTrend> getPriceTrends(Date start, Date end, String... docIds);
	
	@Cache
	List<PriceTrend> getPriceTrends(Date start, Date end, List<String> docIds);
}