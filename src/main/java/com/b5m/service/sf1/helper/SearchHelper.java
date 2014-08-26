package com.b5m.service.sf1.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.b5m.base.common.utils.StringTools;
import com.b5m.bean.dto.SuiSearchDto;
import com.b5m.bean.entity.filterattr.Attibute;
import com.b5m.sf1api.dto.req.AttrFilterBean;
import com.b5m.sf1api.dto.req.GroupBean;
import com.b5m.sf1api.dto.req.SF1SearchBean;
import com.b5m.sf1api.dto.res.Group;
import com.b5m.sf1api.dto.res.GroupTree;
import com.b5m.sf1api.dto.res.SearchDTO;
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
	
	public static void filterAttr(Map<String, Attibute> filterMap, SearchDTO searchDto) {
		List<GroupTree> attributeTrees = searchDto.getAttributeTree();
		if (CollectionUtils.isEmpty(attributeTrees) || CollectionUtils.isEmpty(filterMap))
			return;
		String[] attrRanks = getAttrRanks(filterMap);
		if(attrRanks.length > 0){//如果排序值大于1 则进行排序
			attributeTrees = rankAttrValue(attributeTrees, attrRanks);
			searchDto.setAttributeTree(attributeTrees);
		}
		int length = attributeTrees.size();
		for (int i = 0; i < length; i++) {
			GroupTree groupTree = attributeTrees.get(i);
			String name = groupTree.getGroup().getGroupName();
			if (groupTree == null || StringUtils.isEmpty(name) || !filterMap.containsKey(name))
				continue;
			Attibute attr = filterMap.get(name);
			//设置属性显示名称
			if (attr.getStatus() == 1) {
				attributeTrees.remove(groupTree);
				i--;
				length--;
				continue;
			} else {
				groupTree.getGroup().setDisPlayName(attr.getDisplayName());
				Set<String> set = attr.getValues();
				List<GroupTree> subGroupTrees = groupTree.getGroupTree();
				if (!CollectionUtils.isEmpty(set) && !CollectionUtils.isEmpty(subGroupTrees)){
					int sublength = subGroupTrees.size();
					for (int j = 0; j < sublength; j++) {
						GroupTree subGroupTree = subGroupTrees.get(j);
						String subName = subGroupTree.getGroup().getGroupName();
						if (StringUtils.isEmpty(subName) || !set.contains(subName))
							continue;
						subGroupTrees.remove(subGroupTree);
						j--;
						sublength--;
					}
				}
				String[] attrValueRanks = attr.getRankArray();
				if(attrValueRanks != null && attrValueRanks.length > 0){//如果排序值大于1 则进行排序
					groupTree.setGroupTree(rankAttrValue(subGroupTrees, attrValueRanks));
				}
			}
		}
	}
	
	private static String[] getAttrRanks(Map<String, Attibute> filterMap){
		Attibute attibute = filterMap.get("ALL");
		if(attibute == null) return new String[0];
		return attibute.getKeyRes().getRankArray();
	}
	
	private static List<GroupTree> rankAttrValue(List<GroupTree> subGroupTrees, String[] attrValueRanks){
		List<GroupTree> rankTop = new ArrayList<GroupTree>();
		for(String attrValue : attrValueRanks){
			if(attrValue == "ALL") continue;
			GroupTree contain = contain(subGroupTrees, attrValue);
			if(contain != null){
				subGroupTrees.remove(contain);
			}else{
				contain = new GroupTree();
				contain.setGroup(new Group(attrValue, 0, true, attrValue));
			}
			rankTop.add(contain);
		}
		rankTop.addAll(subGroupTrees);
		return rankTop;
	}
	
	private static GroupTree contain(List<GroupTree> subGroupTrees, String attrValue){
		for(GroupTree groupTree : subGroupTrees){
			if(attrValue.equals(groupTree.getGroup().getGroupName())){
				return groupTree;
			}
		}
		return null;
	}
	
	private boolean contain(String[] attrValueRanks, String subName){
		if(attrValueRanks.length < 1) return false;
		for(String attrValue : attrValueRanks){
			if(attrValue.equals(subName)) return true;
		}
		return false;
	}
	
}
