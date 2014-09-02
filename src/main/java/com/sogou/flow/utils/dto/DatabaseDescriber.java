package com.sogou.flow.utils.dto;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	The database information describer, the infos holder.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-9-1 下午5:00:46
 * @DatabaseDescriber
 */
public class DatabaseDescriber {

	private String name;
	private boolean bigtable;
	private String address;
	private String username;
	private String password;
	private String columns;
	
	public String getColumns() {
		return columns;
	}
	public void setColumns(String columns) {
		this.columns = columns;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getBigtable() {
		return bigtable;
	}
	public void setBigtable(boolean bigtable) {
		this.bigtable = bigtable;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
