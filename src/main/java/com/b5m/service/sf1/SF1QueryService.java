package com.b5m.service.sf1;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.b5m.service.sf1.bean.AutoFillInfo;
import com.b5m.service.sf1.bean.SuiSearchDto;
import com.b5m.sf1api.dto.res.SearchDTO;

public interface SF1QueryService {
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * 搜索
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年7月28日 上午11:34:15
	 *
	 * @param dto
	 * @return
	 */
	SearchDTO search(SuiSearchDto dto);
	
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * 查询纠错词
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年7月28日 上午11:32:47
	 *
	 * @param keywords
	 * @return
	 */
	String queryCorrect(String keywords);
	
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * get获取数据
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年7月28日 上午11:33:53
	 *
	 * @param collection
	 * @param docId
	 * @return
	 */
	JSONObject doGet(String collection, String docId);
	
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年7月28日 下午1:11:02
	 *
	 * @param keywords
	 * @return
	 */
	SearchDTO[] multiSearch(String[] keywords);
	
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * autofill
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年7月28日 下午1:39:55
	 *
	 * @param prefix
	 * @param limit
	 * @param city
	 * @return
	 * @throws Exception
	 */
	Map<String, List<AutoFillInfo>> autoFillSearch(String prefix, int limit, String city) throws Exception;
	
}
