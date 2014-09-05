package com.sogou.flow.constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Properties;

/**
 * The constants to be used in the project , the properties will contain the value,
 * so the key should not be kept the same with the Strings like MENU_PATH/DATA_STRUCTURE_PATH.
 * @author hequn
 * 2014-8-19 13:59:30
 */
public class SystemConstants {

	//the properties file loaded by the system
	public static final String resource = "SystemConstants.properties";
	
	//the properties
	public static Properties properties = new Properties();
	
	//the constant String in the properties file
	public static final String MENU_XML = "MENU_XML";
	public static final String PRODUCTS_LOCATION = "PRODUCTS_LOCATION";
	public static final String DATA_STRUCTURE_PATH = "DATA_STRUCTURE_PATH";
	public static final String DATA_DIMENSION_PATH = "DATA_DIMENSION_PATH";
	public static final String HBASE_ZOOKEEPER_QUORUM = "HBASE_ZOOKEEPER_QUORUM";
	
	//the constants of the system
	public static final String SPLITTER = "/";
	public static final String ZOOKEEPER = "hbase.zookeeper.quorum";
	public static final String PRODUCT_NAME = "productName";
	public static final String CURRENT_DIMENSION = "currentDimension";
	public static final String CONTENT_TYPE = "text/html; charset=utf-8";
	public static final String TIME_BEGIN = "timeBegin";
	public static final String TIME_END = "timeEnd";
	public static final String DEPTH = "depth";
	public static final String VERSION_FILE = "VERSION";
	public static final int REGUARDLESS_LOADING = -1;
	
	//load the key value into properties
	static {
		Class<?> clazz = SystemConstants.class;
		URLClassLoader classLoader = (URLClassLoader) clazz.getClassLoader();
		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream(
					new File(classLoader.getResource(resource).getFile())
			);
			properties.load(fileInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
