package com.sogou.flow.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>
 *	The data parser is used to parse json by dom4j document and the queried table data.
 *	</p>
 * @author Qun He
 * @Creat Time : 2014-8-29 下午2:56:25
 * @DataParserTools
 */
public class DataParserTools {
	
	public static Logger logger = Logger.getLogger(DataParserTools.class);
	
	/**
	 * The method will get useful params from args to json objects , so the map can be request mapping
	 * and then the results should be the data queried from the DB.
	 * @param productName the current product
	 * @param dimension the specified dimension of the current object
	 * @param document dom4j document, from the dataSchemaFile
	 * @param jsonObject the object to be add datas eg:jsonObject from the results map
	 * @param args the variables container
	 * @param results the result returned from the DB, key is the dimension and value is the queried results
	 * @param depth the depth of the offsprings using by dimension further searching
	 * @return json string (final result)
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject parseByDocument(String productName, String dimension, Document document , 
							JSONObject jsonObject, Map<String,?> args, Map<String,?> results, int depth){
		if(depth == 0) return jsonObject;
		//load exact element, make sure the file is in right formation
		Element root = null;
		try {
			root = document.getRootElement()
								.element(XmlVocabulary.JSON)
									.element(XmlVocabulary.LIST);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("The xml configuration get root element failed"+e.getMessage());
			return null;
		}
		String eleName = root.getName();
		if(!eleName.equals(XmlVocabulary.LIST)) return null;
		
		Element struct = root.element(XmlVocabulary.STRUCT);
		
		jsonObject.put(XmlVocabulary.KEY, dimension);

		List<Element> elements = struct.elements();
		//get all the data description
		for (Element element : elements) {
			
			String type = element.attributeValue(XmlVocabulary.TYPE);
			String name = element.attributeValue(XmlVocabulary.NAME);
			String value = element.attributeValue(XmlVocabulary.VALUE);
			
			String sign = getFirstLetter(value);
			
			if(sign.equals(XmlVocabulary.PARAM_RESULT)){
				if(checkAvailableValue(type,args.get(trimFirstLetter(value))))
					jsonObject.put(name, args.get(trimFirstLetter(value)));
			}else if(sign.equals(XmlVocabulary.CONSTANT_RESULT)){
				if(checkAvailableValue(type, trimFirstLetter(value).split(XmlVocabulary.COMMA)))
					jsonObject.put(name, trimFirstLetter(value).split(XmlVocabulary.COMMA));
			}else if(sign.equals(XmlVocabulary.TABLE_RESULT)){
				if(checkAvailableValue(type, results.get(dimension))
											&&trimFirstLetter(value).equals(XmlVocabulary.TABLE))
					jsonObject.put(name, results.get(dimension));
			}else if(sign.equals(XmlVocabulary.INHERIT_RESULT)){
				if(trimFirstLetter(value).equals(XmlVocabulary.INHERIT)){
					List<String> dimensions = CacheHandler.dimensionsMapper
														.get(productName).get(dimension);
					JSONArray jsonArray = new JSONArray();
					
					if(dimensions==null || dimension == null) {
						jsonArray.add(null);
						logger.error("There must be errors in schema xml,productName is "
									+productName+" and dimension is "+dimension);
						continue;
					}
					//load the next object
					--depth;
					for (String dimTmp : dimensions) {
						jsonArray.add(parseByDocument(productName, dimTmp, 
											document,new JSONObject(), args, results,depth));
					}
					
					jsonObject.put(name, jsonArray);
				}
			}
		}
				
		return jsonObject;
	}

	/**
	 * Check the element type defined by user, if it matches the java type.
	 * @param type
	 * @param target
	 * @return true if matches
	 */
	private static boolean checkAvailableValue(String type, Object target) {
		if(target == null) return false;
		String instanceName = target.getClass().getSimpleName();
		return instanceName.toLowerCase().endsWith(type);
	}

	private static String trimFirstLetter(String value) {
		
		return value.substring(1);
	}

	private static String getFirstLetter(String value) {
		
		return value.substring(0,1);
	}

}
