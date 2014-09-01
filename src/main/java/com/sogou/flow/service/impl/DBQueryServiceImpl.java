package com.sogou.flow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sogou.flow.dao.DBQuerierDao;
import com.sogou.flow.service.DBQueryService;
import com.sogou.flow.utils.CacheHandler;
import com.sogou.flow.utils.DataParserTools;
import com.sogou.flow.utils.XmlVocabulary;
import com.sogou.flow.utils.abstracts.DBHolder;
import com.sogou.flow.utils.dto.Criterion;
import com.sogou.flow.utils.model.MenuItemWrapper;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-29 下午6:59:21
 * @DBQueryServiceImpl
 */
public class DBQueryServiceImpl implements DBQueryService {

	private DBQuerierDao dbQuerierDao;
	private DBHolder dbHolder;
	
	public DBQueryServiceImpl(DBQuerierDao dbQuerierDao, DBHolder dbHolder) {
		super();
		this.dbQuerierDao = dbQuerierDao;
		this.dbHolder = dbHolder;
	}

	@Override
	public JSONArray queryForResults(String productName,String currentDimension, Map<String, ?> params) {
		
		MenuItemWrapper itemWrapper = CacheHandler.menuMapper.get(productName);
		
		Map<String,List<Criterion>> queries = faltten(productName,currentDimension,itemWrapper.getDimensions(),params);
				
		Map<String,Object> results = new HashMap<String,Object>(); 
		
		addQueryResults(results,productName,currentDimension,dbHolder,queries.entrySet()
											,1406822700, 1406823000,5);
		
		JSONArray jsonArray = new JSONArray();
		
		for (Entry<String, List<Criterion>> item : queries.entrySet()) {
			
			jsonArray.add(DataParserTools.parseByDocument(productName, item.getKey(), 
					CacheHandler.dataSchemaMapper.get(productName), new JSONObject(), params, results, 5));
		}
		
		// TODO Auto-generated method stub
		return jsonArray;
	}
	
	private void addQueryResults(Map<String, Object> results,String productName, String currentDimension, 
										DBHolder dbHolder, Set<Entry<String,List<Criterion>>> queryParams, 
												int timeBegin, int timeEnd, int depth) {
		if(depth == 0) return;
		for (Entry<String, List<Criterion>> item : queryParams) {
			results.put(item.getKey(),
					dbQuerierDao.queryByParams(dbHolder, item.getValue(), timeBegin, timeEnd));
			List<String> sons = CacheHandler.dimensionsMapper.get(productName).get(item.getKey());

			--depth;
			for (String son : sons) {
				List<Criterion> tmpC = item.getValue();
				for (Criterion criterion : tmpC) {
					if(criterion.getVariableName().equals(currentDimension)){
						criterion.setVariableValue(son);
						break;
					}
				}
				Map<String,List<Criterion>> parseBy = new HashMap<String, List<Criterion>>();
				parseBy.put(son, tmpC);
				addQueryResults(results, productName, currentDimension, dbHolder, parseBy.entrySet(), 
						timeBegin, timeEnd, depth);
			}
		}
	}

	private Map<String,List<Criterion>> faltten(String productName,String currentDimension, List<String> dimensions,Map<String, ?> params) {
		
		Map<String,List<Criterion>> criteriaMapper = new HashMap<String,List<Criterion>>();
		
		String keyName = (CacheHandler.dataSchemaMapper.get(productName)
					.getRootElement().element(XmlVocabulary.JSON).element(XmlVocabulary.LIST)
					.element(XmlVocabulary.STRUCT).attribute(XmlVocabulary.KEY).getValue()).substring(1);
		
		String[] splits = (
							(String)params.get(
								((String)params.get(keyName))
							)
						  ).split(XmlVocabulary.COMMA);
		
		for (int i = 0; i < splits.length; i++) {
			Criterion criterion = new Criterion();
			criterion.setVariableName(currentDimension);
			criterion.setVariableValue(splits[i]);
			List<Criterion> innerCriteria = new ArrayList<Criterion>();
			for (Iterator<String> iterator = dimensions.iterator(); iterator.hasNext();) {
				String dimension = (String) iterator.next();
				if (!dimension.equals(currentDimension)) {
					Criterion criterionTmp = new Criterion();
					criterionTmp.setVariableName(dimension);
					String value = (String) params.get(dimension);
					criterionTmp.setVariableValue(value);
					innerCriteria.add(criterionTmp);
				}else {
					innerCriteria.add(criterion);
				}
			}
			criteriaMapper.put(splits[i],innerCriteria);
		}
		
		return criteriaMapper;
	}
	
}
