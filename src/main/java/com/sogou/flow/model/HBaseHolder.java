package com.sogou.flow.model;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;

import com.sogou.flow.constants.SystemConstants;
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
	
	private String dbName;
	private HTable hTable;

	public HBaseHolder(String dbName, String address, boolean bigTable,
			String username, String password ,String[] targets) {
		super(dbName, address, bigTable, username, password, targets);
		this.dbName = dbName;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HTable getConnectedAgent() {
		if(this.hTable == null){
			Configuration conf = HBaseConfiguration.create();
			conf.set(SystemConstants.ZOOKEEPER, 
					SystemConstants.properties.getProperty(SystemConstants.HBASE_ZOOKEEPER_QUORUM));
			try {
				this.hTable = new HTable(conf, dbName);
			} catch (IOException e) {
				e.printStackTrace();
			}			
		}
		return this.hTable;
	}
	
	public HTable gethTable() {
		return hTable;
	}
	
	public void sethTable(HTable hTable) {
		this.hTable = hTable;
	}

}
