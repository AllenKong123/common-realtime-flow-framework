package com.sogou.flow.service;

import java.util.Map;

import net.sf.json.JSONArray;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	The service for querying data.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-28 下午1:46:19
 * @DBQueryService
 */
public interface DBQueryService {

	/**
	 * The methods for querying records, this should be implemented by different Querier.
	 * @param productName
	 * @param currentDimension
	 * @param params
	 * @return
	 */
	public JSONArray queryForResults(String productName,String currentDimension,Map<String,?> params);
}
