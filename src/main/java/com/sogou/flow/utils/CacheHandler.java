package com.sogou.flow.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;

import com.sogou.flow.loader.impl.ProductLoader;
import com.sogou.flow.utils.dto.FileStatus;
import com.sogou.flow.utils.model.MenuItemWrapper;

public class CacheHandler {
	//The product holder(loader)
	public static final ProductLoader productLoader = new ProductLoader();
	
	//each product will have it's own menuItemWrapper, the menuKey will be the
	//product name
	public static final Map<String,MenuItemWrapper> menuMapper 
								= new HashMap<String,MenuItemWrapper>();
	
	//the product name will be the key and the dom4j doc is value
	//,the doc is used to create json object
	public static final Map<String,Document> dataSchemaMapper
								= new HashMap<String, Document>();
	
	//one product with a dimension details map, the key of the map is a dimension record
	//name and value is the direct children of it
	public static final Map<String,Map<String,List<String>>> dimensionsMapper 
								= new HashMap<String,Map<String,List<String>>>();
	
	//the cached data of the json result, the product/time and together with the current channel can be the key
	public static final Map<String,Object> cachedData
								= new HashMap<String, Object>();
	
	//file status tracker (just track the menu file)
	public static final Map<String,FileStatus> fileTracker
								= new HashMap<String, FileStatus>();

}
