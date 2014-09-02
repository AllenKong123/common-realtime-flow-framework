package com.sogou.flow.dao.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

import com.sogou.flow.constants.XmlVocabulary;
import com.sogou.flow.dao.DBQuerierDao;
import com.sogou.flow.utils.abstracts.DBHolder;
import com.sogou.flow.utils.dto.Criterion;

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

	//Define some useful properties here
	private final String QUALIFIER = "master";
	
	@SuppressWarnings({ "hiding", "unchecked" })
	@Override
	public <Object> Object queryByParams(DBHolder dbHolder, List<Criterion> params 
																	, long timeBegin , long timeEnd) {
		//use the List<List<Object>> as the Object to the results
		List<List<Object>> results = new ArrayList<List<Object>>();
		try {
			HTable hTable = (HTable)dbHolder.getConnectedAgent();
			
			//combine the key
			String key = "";
			for (Criterion criterion : params) {
				key+=criterion.getVariableValue();
			}
			
			byte[] rowKey = Bytes.toBytes(key);
			byte[] qualifier = Bytes.toBytes(QUALIFIER);
			
			Get get =new Get(rowKey);
			get.setMaxVersions().setTimeRange(timeBegin, timeEnd);
			for (String target : dbHolder.getTargets()) {
				if(!target.equals(XmlVocabulary.TIMESTAMP)) get.addFamily(Bytes.toBytes(target));
			}
			
			Result r = hTable.get(get);

			NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> resultMap = r.getMap();

			if(resultMap == null){
				results.add(null);
			}
			else {
				//get the datas and map them to list
				boolean signal = true;
				List<Long> times = new ArrayList<Long>();
				for (String target : dbHolder.getTargets()) {
					if(target.equals(XmlVocabulary.TIMESTAMP)) continue;
					NavigableMap<Long, byte[]> contentMap = 
										resultMap.get(Bytes.toBytes(target)).get(qualifier);
					int count = 0;
					for(Map.Entry<Long, byte[]> ent : contentMap.entrySet()){
						List<Object> objs = new ArrayList<Object>();
						byte[] value = ent.getValue();
						
						if(results.size() == count && signal == true) {
							objs.add((Object)Integer.valueOf(Bytes.toString(value)));
							times.add(ent.getKey());
							results.add(objs);
						}
						else {
							results.get(count).add((Object)Integer.valueOf(Bytes.toString(value)));
						}
						count++;
					}
					signal = false;
				}
				//the timestamp is a unique property, deal it here
				for (int i = 0 ; i < times.size() ; i++) {
					results.get(i).add((Object)times.get(i));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (Object) results;
	}

}
