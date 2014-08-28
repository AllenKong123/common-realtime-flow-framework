package com.sogou.flow.dao.impl;

import java.util.List;

import com.sogou.flow.dao.DBQuerierDao;
import com.sogou.flow.utils.abstracts.DBHolder;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	The DBQuerierDao's implementation, the HBaseQuerierImpl will provide the result of any type,
 *	eg: Map List<Object[]> sth.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-28 上午9:48:47
 * @DBHBaseQuerierImpl
 */
public class DBHBaseQuerierImpl implements DBQuerierDao{

	@SuppressWarnings({ "hiding", "unchecked" })
	@Override
	public <Object> Object queryByParams(DBHolder dbHolder, List<String> params 
																	, long timeBegin , long timeEnd) {
		// TODO Auto-generated method stub
		String[] a = {"1","2"};
		return (Object) a;
	}
	
//	public static void main(String[] args){
//		DBHBaseQuerier baseQuerier = new DBHBaseQuerier();
//		HBaseHolder baseHolder = new HBaseHolder(null,null,true,null,null);
//		Object o = baseQuerier.queryByParams(baseHolder, null);
//		System.out.println(o.getClass());
//		System.out.println(o.getClass().getInterfaces());
//		System.out.println("d");
//	}
}
