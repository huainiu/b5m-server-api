package com.b5m.service.sf1.helper;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.b5m.base.common.utils.StringTools;
import com.b5m.bean.entity.filterattr.Attibute;
import com.b5m.service.sf1.bean.SuiSearchDto;
import com.b5m.sf1api.dto.req.AttrFilterBean;
import com.b5m.sf1api.dto.req.GroupBean;
import com.b5m.sf1api.dto.req.SF1SearchBean;
import com.b5m.sf1api.utils.GetCndBuilder;
import com.b5m.sf1api.utils.SearchCndBuilder;

public class SearchHelper {
	public static final String IS_FREE_DELIVERY = "1";
	public static final String IS_COD = "1";
	public static final String IS_GENUINE = "1";
	
	public static SF1SearchBean converTo4Get(String collection, String docId){
		SF1SearchBean sf1SearchBean = new SF1SearchBean();
		GetCndBuilder builder = sf1SearchBean.builderGet();
		builder.collection(collection);
		builder.addCondition("DOCID", "=", docId);
		return sf1SearchBean;
	}

	public static SF1SearchBean convertTo4Search(SuiSearchDto searchDto) {
		SF1SearchBean sf1SearchBean = new SF1SearchBean();
		SearchCndBuilder builder = sf1SearchBean.builder();
		builder.collection(searchDto.getCollectionName());
		builder.page(searchDto.getPageSize(), (searchDto.getCurrPageNo() - 1) * searchDto.getPageSize());
		builder.getAttr(true, 20).sort(searchDto.getSortField(), searchDto.getSortType());
		builder.filterByPrice(searchDto.getPriceFrom(), searchDto.getPriceTo());
		builder.category(searchDto.getCategoryValue());
		builder.sources(searchDto.getSourceValue()).queryAbbreviation(true);
		builder.keywords(searchDto.getKeyword()).isRequireRelated(true);
		//设置过滤属性
		setAttrFilter(searchDto, builder);
		//设置group
		addGroup(searchDto, builder);
		if (searchDto.isCompare()) {
			builder.addCondition("itemcount", ">", "1");
		}
		if ("1".equals(searchDto.getIsLowPrice())) {
			builder.addCondition("isLowPrice", ">", "0f");
		}
		if (IS_FREE_DELIVERY.equals(searchDto.getIsFreeDelivery())) {
			builder.addCondition("isFreeDelivery", "=", IS_FREE_DELIVERY);
		}
		if (IS_GENUINE.equals(searchDto.getIsGenuine())) {
			builder.addCondition("isGenuine", "=", IS_GENUINE);
		}
		if(!StringUtils.isEmpty(searchDto.getCountry())){
			builder.addCondition("Country", "=", searchDto.getCountry());
		}
		return sf1SearchBean;
	}
	
	protected static void addGroup(SuiSearchDto searchDto, SearchCndBuilder builder) {
		if(StringUtils.isEmpty(searchDto.getPriceFrom()) && StringUtils.isEmpty(searchDto.getPriceTo())){
			builder.addGroupBean(new GroupBean(true, "Price"));
		}
		builder.addGroupBean(new GroupBean(false, "Source"));
		builder.addGroupBean(new GroupBean(false, "Category"));
	}

	protected static void setAttrFilter(SuiSearchDto searchDto, SearchCndBuilder builder) {
		if (StringTools.isEmpty(searchDto.getAttrs())) {
			return;
		}
		String[] attrValueArray = StringTools.split(searchDto.getAttrs(), ",");
		if (attrValueArray.length == 0)
			return;
		Map<String, Attibute> attrMap = searchDto.getFilterMap();
		for (String attrValue : attrValueArray) {
			if (StringUtils.isEmpty(attrValue)) continue;
			String[] attr = StringTools.split(attrValue, ":");
			if (attr.length < 2)
				continue;
			// 将品牌成改利用attr_label进行过滤
			String value = attr[1];
			Attibute attibute = null;
			if(attrMap != null){
				attibute = attrMap.get("ALL");
			}
			boolean haveMerge = false;
			if(attibute != null) {
				String[] attrs = attibute.getRelByDisplayName(value);
				if(attrs.length > 0){
					haveMerge = true;
					for(String a : attrs){
						builder.addAttrFilter(new AttrFilterBean(attr[0], a));
					}
				}
			}
			if(!haveMerge){
				builder.addAttrFilter(new AttrFilterBean(attr[0], value));
			}
		}
	}
}
