package com.sogou.flow.loader.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
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
 *	<p>Load the products' menus and together bootstrap the cache, one to one.</p>
 * @author Qun He
 * @Creat Time : 2014-8-26 上午10:21:31
 * @ProductLoader
 */
public class ProductLoader implements Loader
{

	private String path = SystemConstants.properties.getProperty(SystemConstants.PRODUCTS_LOCATION);
	private String menuXml = SystemConstants.properties.getProperty(SystemConstants.MENU_XML);
	
	private Logger logger = Logger.getLogger(ProductLoader.class);
	
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
			File file = FileLoadTools.getFileByName(key, path);
			
			if(file.isDirectory()){
				createOneProduct(file.getName());
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

		File tmpVersion = FileLoadTools.getFileByName(SystemConstants.VERSION_FILE,
																path+folderName+SystemConstants.SPLITTER);
		String number = null;
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(tmpVersion));
			number = bufferedReader.readLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if(number == null){
			logger.error("Create product failed, product name is "+
									folderName+", VERSION file number not right!");
			return;
		}
		
		int numberInt = Integer.valueOf(number);
		
		//regardless of the -1 VERSION number, represents the unload or not load
		if(numberInt == SystemConstants.REGUARDLESS_LOADING) {
			logger.debug("Product "+folderName+" is not loaded because the VERSION is regardless!");
			return;
		}
		
		MenuItemWrapper menuWrapper = new MenuItemWrapper();
		File tmpXml = FileLoadTools.getFileByName(menuXml, path+folderName+SystemConstants.SPLITTER);
		Document document = Dom4jTools.getDocByFile(tmpXml);
		//Add the file to tracker if it changed
		FileStatus fileStatus = new FileStatus(
				tmpVersion.getName(), tmpVersion.lastModified() ,numberInt
		);
		CacheHandler.fileTracker.put(folderName, fileStatus);
		//Parse the menu
		MenusParserTools.processMenuCompleter(menuWrapper, document, folderName);
		menuWrapper.setProductName(folderName);
		//cache it
		CacheHandler.menuMapper.put(folderName, menuWrapper);
	}
}
