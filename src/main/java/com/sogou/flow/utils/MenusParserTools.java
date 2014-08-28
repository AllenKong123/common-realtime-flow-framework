package com.sogou.flow.utils;

import java.io.File;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;

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
	public static void processMenuCompleter(MenuItemWrapper menuItemWrapper , Document document , String productName){
		
		//Fill the first menu.xml to menuItemWrapper, reflection attribute to instance
		Dom4jTools.setValueToInstanceByMethod(menuItemWrapper, document.getRootElement());
		
		//Get the import elements to include all of the dimensions
		@SuppressWarnings("unchecked")
		List<Element> importElements = document.getRootElement().elements();
		//The menu name should be set to menuItemWrapper
		menuItemWrapper.setMenuName(document.getRootElement().getName());
		//Load the product dataSchema
		CacheHandler.dataSchemaMapper.put(productName,
						createDataSchemaByFile(menuItemWrapper,productName+SystemConstants.SPLITTER));
		
		//Each element -> one import sentence -> one dimension
		for (Element element : importElements) {
			//Just load the import element and get the content, head menu
			if(!element.getName().equals(XmlVocabulary.IMPORT))
				continue;
			else {
				//Get the xml first and parse to Document
				File file = FileLoaderTools.getXmlByName(
						element.getText(), 
						productName
						+SystemConstants.SPLITTER
						+SystemConstants.properties.getProperty(SystemConstants.DATA_DIMENSION_PATH));
				Document doc = Dom4jTools.getDocByPathAndName(file);
				//The method to load MenuItem in the specified dimension xml configuration 
				processMenuItems(menuItemWrapper.getMenuItems(),doc.getRootElement(),0);
				menuItemWrapper.getDimensions().add(doc.getRootElement().attributeValue(XmlVocabulary.DIMENSION));
			}
		}
	}

	/**
	 * Load the menu one by one , then map the related dataSchema
	 * @param menuItems
	 * @param element
	 * @param order
	 * @param map 
	 */
	private static void processMenuItems(List<MenuItem> menuItems, Element element , int order) {
		
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
				
		@SuppressWarnings("unchecked")
		List<Element> elements = element.elements();
		for (Element elementTmp : elements) {
			processMenuItems(menuItem.getChildrenItems(), elementTmp , order++);
		}
	}

	private static Document createDataSchemaByFile(MenuItemWrapper menuItemWrapper , String currentProduct) {
		String realFilePath = 
				currentProduct+SystemConstants.properties.getProperty(SystemConstants.DATA_STRUCTURE_PATH);
		File tmp = FileLoaderTools.getXmlByName(menuItemWrapper.getDataSchemaFile(), realFilePath);
		Document document = Dom4jTools.getDocByPathAndName(tmp);
		return document;
	}
}
