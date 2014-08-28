package com.sogou.flow.dao;

import java.util.List;

import com.sogou.flow.utils.abstracts.DBHolder;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>The interface for querying actions. All the methods are working 
 *	   for querying records from the datasource.</p>
 * @author Qun He
 * @Creat Time : 2014-8-27 下午4:02:21
 * @DBQuerierDao
 */
public interface DBQuerierDao {

	/**
	 * Get the results from the DBHolder according to the params which are conditions of the querying 
	 * and between the time range.
	 * @param dbHolder the specified instance used to get DB connection or table in bigtable implements
	 * @param params the params used as conditions for querying
	 * @param timeBegin the begin time of the records
	 * @param timeEnd the end time of the records
	 * @return the results of the querying <em>eg: Map List<Object[]> sth.</em>
	 */
	public <T> T queryByParams(DBHolder dbHolder , List<String> params , long timeBegin , long timeEnd);
}
