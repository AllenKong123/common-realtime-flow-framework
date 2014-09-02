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
	 * The results from the DB and the json formation are two procedures, though DB results
	 * can be load inner json formation duration.
	 * @param productName the productName
	 * @param currentDimension the current dimension will be used eg:province/abest, any but one
	 * @param params the request params needed in the methods
	 * @return the queried final json array
	 */
	public JSONArray queryForResults(String productName,String currentDimension,Map<String,?> params);
}
