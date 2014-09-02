package com.sogou.flow.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.sogou.common.utils.TimeTools;
import com.sogou.flow.constants.SystemConstants;
import com.sogou.flow.constants.XmlVocabulary;
import com.sogou.flow.dao.DBQuerierDao;
import com.sogou.flow.service.DBQueryService;
import com.sogou.flow.utils.CacheHandler;
import com.sogou.flow.utils.DataParserTools;
import com.sogou.flow.utils.abstracts.DBHolder;
import com.sogou.flow.utils.dto.Criterion;
import com.sogou.flow.utils.model.MenuItemWrapper;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	The implement of the {@link DBQueryService}.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-29 下午6:59:21
 * @DBQueryServiceImpl
 */
public class DBQueryServiceImpl implements DBQueryService {

	private DBQuerierDao dbQuerierDao;
	private DBHolder dbHolder;
	
	private Logger logger = Logger.getLogger(DBQueryServiceImpl.class);
	
	public DBQueryServiceImpl(DBQuerierDao dbQuerierDao, DBHolder dbHolder) {
		super();
		this.dbQuerierDao = dbQuerierDao;
		this.dbHolder = dbHolder;
	}

	@Override
	public JSONArray queryForResults(String productName,String currentDimension, Map<String, ?> params) {
		//Get the itemWrapper first, one product to one wrapper
		MenuItemWrapper itemWrapper = CacheHandler.menuMapper.get(productName);
		//According to the current dimension and the product flatten the queries
		Map<String,List<Criterion>> queries = flatten(productName,currentDimension,itemWrapper.getDimensions(),params);
		//Get the timerange 
		String timeBegin = (String) params.get(SystemConstants.TIME_BEGIN);
		String timeEnd = (String) params.get(SystemConstants.TIME_END);
		int depth = Integer.valueOf((String) params.get(SystemConstants.DEPTH));
		//Define the results map, key is the value of the current dimension (extract from the request params)
		//the value is the result from the db
		Map<String,Object> results = new HashMap<String,Object>(); 
		//Recursive load the results to the results map
		addQueryResults(results,productName,currentDimension,dbHolder,queries.entrySet()
								,TimeTools.getLongValueOfTimeInSeconds(new StringBuffer(timeBegin))
								,TimeTools.getLongValueOfTimeInSeconds(new StringBuffer(timeEnd))
								,depth);
		
		JSONArray jsonArray = new JSONArray();
		//Formating the results to json type
		for (Entry<String, List<Criterion>> item : queries.entrySet()) {
			//Add the jsonObject to results
			jsonArray.add(DataParserTools.parseByDocument(productName, item.getKey(), 
				CacheHandler.dataSchemaMapper.get(productName), new JSONObject(), params, results, depth));
		}
		
		// TODO Auto-generated method stub
		return jsonArray;
	}
	
	/**
	 * <p>Add the queried out results to results map recursive by the inheritance of the current
	 * 		dimension and it's value.</p>
	 * @param results the results to be added data in
	 * @param productName the product name
	 * @param currentDimension the current dimension
	 * @param dbHolder the db holder (connection and some details)
	 * @param queryParams the query params in {@link Criterion} type
	 * @param timeBegin begin time of the query
	 * @param timeEnd end time of the query
	 * @param depth the recursive depth of data extacting
	 */
	private void addQueryResults(Map<String, Object> results,String productName, String currentDimension, 
										DBHolder dbHolder, Set<Entry<String,List<Criterion>>> queryParams, 
												long timeBegin, long timeEnd, int depth) {
		if(depth == 0) return;
		//Query the data out from the DB by the queryParams
		for (Entry<String, List<Criterion>> item : queryParams) {
			//Each item.getKey() represents one dimension value <-> one DB results
			results.put(item.getKey(),
					dbQuerierDao.queryByParams(dbHolder, item.getValue(), timeBegin, timeEnd));
			List<String> sons = CacheHandler.dimensionsMapper.get(productName).get(item.getKey());

			if(sons == null){
				logger.error("There must be an error in the params passed by " +
						"productName is "+productName+" and currentDimension is "+currentDimension);
				continue;
			}
			
			--depth;
			//Load the sons
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

	/**
	 * <p>Flatten the queries. For example:
	 * 	  <p>current dimension:province</p>
	 * 	  <p>province:hebei,beijing,tianjin</p>
	 * 	  <p>others:abtest=0,channel=\N,browser=qq</p>
	 *    <ul>The results will be:
	 *    	<li>1.abtest=0,channel=\N,browser=qq,province=hebei</li>
	 *    	<li>2.abtest=0,channel=\N,browser=qq,province=beijing</li>
	 *    	<li>3.abtest=0,channel=\N,browser=qq,province=tianjin</li>
	 *    </ul>
	 * </p>
	 * @param productName the product name
	 * @param currentDimension the current dimension
	 * @param dimensions the dimensions of the product (get from the menuItemWrapper)
	 * @param params the parameters map from the request
	 * @return the flattened results key is the dimension and value is the flattened criterion
	 */
	private Map<String,List<Criterion>> flatten(String productName,String currentDimension, List<String> dimensions,Map<String, ?> params) {
		//Define the map, key is the dimension value of the currentDimension
		//Get them from the request params
		Map<String,List<Criterion>> criteriaMapper = new HashMap<String,List<Criterion>>();
		//Get the keyName from the struct element and the key attribute
		String keyName = (CacheHandler.dataSchemaMapper.get(productName)
					.getRootElement().element(XmlVocabulary.JSON).element(XmlVocabulary.LIST)
					.element(XmlVocabulary.STRUCT).attribute(XmlVocabulary.KEY).getValue()).substring(1);
		//Split the values from the request of this currentDimensions
		//There can be one or many values
		String[] splits = (
							(String)params.get(
								((String)params.get(keyName))
							)
						  ).split(XmlVocabulary.COMMA);
		//Flatten the conditions especially by this dimension
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
