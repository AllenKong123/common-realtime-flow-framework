package com.sogou.flow.loader.impl;

import java.io.File;

import org.dom4j.Document;

import com.sogou.common.utils.Dom4jTools;
import com.sogou.common.utils.FileLoadTools;
import com.sogou.flow.constants.SystemConstants;
import com.sogou.flow.loader.Loader;
import com.sogou.flow.utils.CacheHandler;
import com.sogou.flow.utils.MenusParserTools;
import com.sogou.flow.utils.dto.FileStatus;
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

	private String path = SystemConstants.properties.getProperty(SystemConstants.PRODUCTS_LOCATION);
	private String menuXml = SystemConstants.properties.getProperty(SystemConstants.MENU_XML);
	
	@Override
	public boolean load() {
		boolean result = true;
    	try {
			File[] files = FileLoadTools.getFoldersByDirectory(path);
			for (File file : files) {
				if(file.isDirectory())
					createOneProduct(file.getName());
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
			CacheHandler.fileTracker.remove(key);
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
			File[] files = FileLoadTools.getFoldersByDirectory(path);
			for (File file : files) {
				if(file.isDirectory()&&file.getName().equals(key)){
					createOneProduct(file.getName());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
    	return result;
	}
	
	/**
	 * Create the menu and data for this product.
	 * @param folderName
	 */
	private void createOneProduct(String folderName){

		MenuItemWrapper menuWrapper = new MenuItemWrapper();
		File tmp = FileLoadTools.getXmlByName(menuXml, path+folderName+SystemConstants.SPLITTER);
		Document document = Dom4jTools.getDocByFile(tmp);
		//Add the file to tracker if it changed
		FileStatus fileStatus = new FileStatus(tmp.getName(), tmp.lastModified());
		CacheHandler.fileTracker.put(folderName, fileStatus);
		//Parse the menu
		MenusParserTools.processMenuCompleter(menuWrapper, document, folderName);
		menuWrapper.setProductName(folderName);
		//cache it
		CacheHandler.menuMapper.put(folderName, menuWrapper);
	}
}
