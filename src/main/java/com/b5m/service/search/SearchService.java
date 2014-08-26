package com.b5m.service.search;

import com.b5m.bean.dto.SuiSearchDto;
import com.b5m.sf1api.dto.res.SearchDTO;

public interface SearchService {
	
	/**
	 *<font style="font-weight:bold">Description: </font> <br/>
	 * 查询并处理过滤属性
	 * @author echo
	 * @email wuming@b5m.cn
	 * @since 2014年8月20日 下午2:33:16
	 *
	 * @param dto
	 * @param col
	 * @return
	 */
	SearchDTO searchList(SuiSearchDto dto, String col);
	
}
