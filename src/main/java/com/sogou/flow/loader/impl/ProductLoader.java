package com.sogou.flow.loader.impl;

import java.io.File;

import org.dom4j.Document;

import com.sogou.flow.loader.Loader;
import com.sogou.flow.utils.CacheHandler;
import com.sogou.flow.utils.Dom4jTools;
import com.sogou.flow.utils.FileLoaderTools;
import com.sogou.flow.utils.MenusParserTools;
import com.sogou.flow.utils.SystemConstants;
import com.sogou.flow.utils.model.MenuItemWrapper;

/**
 * 
 *  Copyright 2014 SOGOU
 *  All right reserved.
 *	<p>Load the products' menus, one to one, </p>
 * @author Qun He
 * @Creat Time : 2014-8-26 上午10:21:31
 * @ProductLoader
 */
public class ProductLoader implements Loader
{

	@Override
	public boolean load() {
		boolean result = true;
    	try {
			String path = SystemConstants.properties.getProperty(SystemConstants.PRODUCTS_LOCATION);
			String menuXml = SystemConstants.properties.getProperty(SystemConstants.MENU_XML);
			File[] files = FileLoaderTools.getFoldersByDirectory(path);
			for (File file : files) {
				if(file.isDirectory()){
					String folderName = file.getName();
					MenuItemWrapper menuWrapper = new MenuItemWrapper();
					File tmp = FileLoaderTools.getXmlByName(menuXml, path+folderName+SystemConstants.SPLITTER);
					Document document = Dom4jTools.getDocByFile(tmp);
					
					MenusParserTools.processMenuCompleter(menuWrapper, document, folderName);
					menuWrapper.setProductName(folderName);
					
					CacheHandler.menuMapper.put(folderName, menuWrapper);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
    	return result;
	}

	@Override
	public boolean unload(String key) {
		boolean result = true;
		try {
			CacheHandler.dataSchemaMapper.remove(key);
			CacheHandler.menuMapper.remove(key);
			CacheHandler.dimensionsMapper.remove(key);
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	@Override
	public boolean reload(String key) {
		boolean result = true;
    	try {
			String path = SystemConstants.properties.getProperty(SystemConstants.PRODUCTS_LOCATION);
			String menuXml = SystemConstants.properties.getProperty(SystemConstants.MENU_XML);
			File[] files = FileLoaderTools.getFoldersByDirectory(path);
			for (File file : files) {
				if(file.isDirectory()&&file.getName().equals(key)){
					String folderName = file.getName();
					MenuItemWrapper menuWrapper = new MenuItemWrapper();
					File tmp = FileLoaderTools.getXmlByName(menuXml, path+folderName+SystemConstants.SPLITTER);
					Document document = Dom4jTools.getDocByFile(tmp);
					
					MenusParserTools.processMenuCompleter(menuWrapper, document, folderName);
					
					menuWrapper.setProductName(folderName);
					CacheHandler.menuMapper.put(folderName, menuWrapper);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
    	return result;
	}
	
	public static void main(String[] args){
		ProductLoader loader = new ProductLoader();
		loader.load();
		System.out.println("d");
	}
}
