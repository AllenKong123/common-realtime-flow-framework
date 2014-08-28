package com.sogou.flow.service;

import java.util.Map;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-28 下午1:46:19
 * @DBQueryService
 */
public interface DBQueryService {

	public String queryForResults(String productName, String dataSchemaName,
									String currentDimension, Map<String,?> params);
}
