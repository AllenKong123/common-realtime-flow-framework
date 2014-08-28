package com.sogou.flow.utils;

import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;

import com.sogou.flow.utils.model.MenuItemWrapper;

public class CacheHandler {
	//each product will have it's own menuItemWrapper, the menuKey will be the
	//product name
	public static final Map<String,MenuItemWrapper> menuMapper 
								= new HashMap<String,MenuItemWrapper>();
	
	//the product name will be the key and the dom4j doc is value
	//,the doc is used to create json object
	public static final Map<String,Document> dataSchemaMapper 
								= new HashMap<String, Document>();
	
	//the cached data of the json result, the product/time and together with the current channel can be the key
	public static final Map<String,Object> cachedData
								= new HashMap<String, Object>();
}
