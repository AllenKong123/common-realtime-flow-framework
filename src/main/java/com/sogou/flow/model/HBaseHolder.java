package com.sogou.flow.model;

import org.apache.hadoop.hbase.client.HTable;

import com.sogou.flow.dao.impl.DBHBaseQuerierImpl;
import com.sogou.flow.utils.abstracts.DBHolder;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	The implements of the DBHolder, HBase will provide the agent as HTable,
 *	so the instance of HBaseHolder will load the information by constructor 
 *	and then to be used by the implementation of DBQuerier {@link DBHBaseQuerierImpl}.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-28 上午9:45:37
 * @HBaseHolder
 */
public class HBaseHolder extends DBHolder{
	
	public HBaseHolder(String dbName, String address, boolean bigTable,
			String username, String password) {
		super(dbName, address, bigTable, username, password);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	public HTable getConnectedAgent() {
		// TODO Auto-generated method stub
		return null;
	}

}
