package com.sogou.flow.utils.abstracts;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *		To provide the basic information of the DB in order to be used to get the table or connection
 *		, then add some abstract methods to be used by children.
 *  </p>
 * @author Qun He
 * @Creat Time : 2014-8-28 上午9:40:09
 * @DBHolder
 */
public abstract class DBHolder {

	protected String dbName;
	protected String address;

	protected boolean bigTable;
	
	protected String username;
	protected String password;

	public DBHolder(String dbName, String address, boolean bigTable,
			String username, String password) {
		super();
		this.dbName = dbName;
		this.address = address;
		this.bigTable = bigTable;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Get the agent, eg: if it is HBase this can be HTable indeed.
	 * @return the connection or table depends on the implements class
	 */
	public abstract <T> T getConnectedAgent();
	
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isBigTable() {
		return bigTable;
	}
	public void setBigTable(boolean bigTable) {
		this.bigTable = bigTable;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
