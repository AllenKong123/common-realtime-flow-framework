package com.sogou.flow.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

import com.sogou.common.utils.Dom4jTools;
import com.sogou.common.utils.FileLoadTools;
import com.sogou.flow.constants.SystemConstants;
import com.sogou.flow.constants.XmlVocabulary;
import com.sogou.flow.utils.model.MenuItem;
import com.sogou.flow.utils.model.MenuItemWrapper;

/**
 * The parser to parse document to {@link MenuItem}
 * @author hequn
 *2014-8-20 12:34:26
 */
public class MenusParserTools {

	public static Logger logger = Logger.getLogger(MenusParserTools.class);
	
	/**
	 * The menu parser starts to parse the menu from the menu.xml, especially the import element.
	 * @param menuItemWrapper the instance menuItemWrapper which represents one product menu
	 * @param document the dom4j document, in order to get the element
	 * @param productName the product name specially for this product
	 */
	public static void processMenuCompleter(MenuItemWrapper menuItemWrapper , Document document , 
														String productName){
		
		String path = SystemConstants.properties.getProperty(SystemConstants.PRODUCTS_LOCATION);

		//Fill the first menu.xml to menuItemWrapper, reflection attribute to instance
		Dom4jTools.setValueToInstanceByMethod(menuItemWrapper, document.getRootElement());
		
		//Get the import elements to include all of the dimensions
		@SuppressWarnings("unchecked")
		List<Element> importElements = document.getRootElement().elements();
		//The menu name should be set to menuItemWrapper
		menuItemWrapper.setMenuName(document.getRootElement().getName());
		//Load the product dataSchema
		CacheHandler.dataSchemaMapper.put(productName,
						createDataSchemaByFile(menuItemWrapper,path+productName+SystemConstants.SPLITTER));
		
		Map<String, List<String>> currProductDimensions=new HashMap<String, List<String>>();
		//Load the dimensionsMapper
		CacheHandler.dimensionsMapper.put(productName,currProductDimensions);
		//Each element -> one import sentence -> one dimension
		for (Element element : importElements) {
			//Just load the import element and get the content, head menu
			if(!element.getName().equals(XmlVocabulary.IMPORT))
				continue;
			else {
				//Get the xml first and parse to Document
				File file = FileLoadTools.getXmlByName(
						element.getText(), 
						path+productName
						+SystemConstants.SPLITTER
						+SystemConstants.properties.getProperty(SystemConstants.DATA_DIMENSION_PATH));
				Document doc = Dom4jTools.getDocByFile(file);
				//The method to load MenuItem in the specified dimension xml configuration 
				processMenuItems(menuItemWrapper.getMenuItems(),doc.getRootElement(),currProductDimensions,0);
				menuItemWrapper.getDimensions().add(doc.getRootElement().attributeValue(XmlVocabulary.DIMENSION));
			}
		}
	}

	/**
	 * Load the menu one by one , then map the related dataSchema
	 * @param menuItems
	 * @param element
	 * @param currProductDimensions 
	 * @param order
	 * @param map 
	 */
	private static void processMenuItems(List<MenuItem> menuItems, Element element , 
											Map<String, List<String>> currProductDimensions, int order) {
		
		//Create the menuItem and set it to parent's children list
		MenuItem menuItem = new MenuItem();
		menuItems.add(menuItem);
		//Fill attribute if exist
		Dom4jTools.setValueToInstanceByMethod(menuItem, element);
		//Set value to menuItem
		String itemName = element.getName();
		menuItem.setItemName(itemName);
		menuItem.setOrder(order);
		//If the dimension is DEFAULT_DEMENSION, load the parents dimension
		//so the dimension of each menuItem will be the closest ancestor's
		if(menuItem.getDimension().equals(XmlVocabulary.DEFAULT_DEMENSION)){
			menuItem.setDimension(element.getParent().attributeValue(XmlVocabulary.DIMENSION));
			element.addAttribute(XmlVocabulary.DIMENSION, menuItem.getDimension());
		}
		
		List<String> sons = new ArrayList<String>();
		//For each dimension, it's doc will be loaded in
		currProductDimensions.put(itemName, sons);
		
		@SuppressWarnings("unchecked")
		List<Element> elements = element.elements();
		for (Element elementTmp : elements) {
			processMenuItems(menuItem.getChildrenItems(), elementTmp , currProductDimensions , order++);
			sons.add(elementTmp.getName());
		}
	}

	/**
	 * Load the dataSchema file of the current product.
	 * @param menuItemWrapper the menuItemWrapper to get the schema file name
	 * @param currentProduct the name of the product
	 * @return the schema document
	 */
	private static Document createDataSchemaByFile(MenuItemWrapper menuItemWrapper , String currentProduct) {
		String realFilePath =
				currentProduct+SystemConstants.properties.getProperty(SystemConstants.DATA_STRUCTURE_PATH);
		File tmp = FileLoadTools.getXmlByName(menuItemWrapper.getDataSchemaFile(), realFilePath);
		return Dom4jTools.getDocByFile(tmp);
	}
}
