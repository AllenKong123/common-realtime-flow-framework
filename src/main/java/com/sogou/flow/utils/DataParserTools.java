package com.sogou.flow.utils;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * The data parser is used to parse json by dom4j document and the queried table data.
 * @author hequn
 *
 */
public class DataParserTools {
	
	public static Logger logger = Logger.getLogger(DataParserTools.class);
	
	/**
	 * The method will get useful params from args to json objects , so the map can be request mapping
	 * and then the results should be the data queried from the DB.
	 * @param document dom4j document, from the dataSchemaFile
	 * @param args the variables container
	 * @param results the result returned from the DB, key is the dimension and value is the queried results
	 * @return json string (final result)
	 */
	@SuppressWarnings("unchecked")
	public static String parseByDocument(Document document , Map<String,?> args , Map<String,?> results){
		
		//load exact element, make sure the file is in right formation
		Element element = null;
		try {
			element = document.getRootElement()
								.element(XmlVocabulary.JSON)
									.element(XmlVocabulary.LIST);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("The xml configuration get root element failed"+e.getMessage());
			return null;
		}
		String eleName = element.getName();
		if(!eleName.equals(XmlVocabulary.LIST)) return null;
		
		//build the jsonArray
		JSONArray jsonArray = new JSONArray();
		Element struct = element.element(XmlVocabulary.STRUCT);

		//the key which represents the current dimension(the number of the json objects depends on it)
		String keyName = trimFirstLetter(struct.attribute(XmlVocabulary.KEY).getValue());
		String[] dimensions = ((String)args.get(keyName)).split(XmlVocabulary.COMMA);
		
		//create the objects 
		for (int i = 0; i < dimensions.length; i++) {
			String dimension = dimensions[i];
			JSONObject jsonObject = new JSONObject();
			jsonObject.put(XmlVocabulary.KEY, dimension);
			fillValues(jsonObject,struct.elements(),args,results.get(dimension));
			jsonArray.add(jsonObject);
		}
		
		return jsonArray.toString();
	}

	/**
	 * Fill the objects to jsonArray, three kinds of variables.
	 * <p>PARAM_RESULT : the values from the args map</p>
	 * <p>CONSTANT_RESULT : the values from the xml dataSchema file</p>
	 * <p>TABLE_RESULT : the values from the DB</p>
	 * @param jsonObject
	 * @param elements
	 * @param args
	 * @param results
	 */
	private static void fillValues(JSONObject jsonObject, List<Element> elements, Map<String, ?> args, Object results) {
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
				if(checkAvailableValue(type, results)&&trimFirstLetter(value).equals(XmlVocabulary.TABLE))
					jsonObject.put(name, results);
			}
		}
	}

	/**
	 * Check the element type defined by user, if it matches the java type.
	 * @param type
	 * @param target
	 * @return true if matches
	 */
	private static boolean checkAvailableValue(String type, Object target) {
		
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
