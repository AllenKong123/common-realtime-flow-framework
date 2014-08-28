package com.sogou.flow.utils;

import java.io.File;
import java.io.FileFilter;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.log4j.Logger;

/**
 * Load files by path, basic tools.
 * @author hequn
 *2014-8-20 12:36:34
 */
public class FileLoaderTools {

	public static Logger logger = Logger.getLogger(FileLoaderTools.class);
	
	/**
	 * Get the xml files in the folder.
	 * @param path
	 * @return all the files in the folder
	 */
	public static File[] getFilesByDirectory(String path){
		Class<?> clazz = FileLoaderTools.class;
		URLClassLoader loader = (URLClassLoader) clazz.getClassLoader();
		URL url = null;
		url = loader.getResource(path);
		if(url == null) return null;	
		File file = null;
		try {
			file = new File(url.getFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(file.isDirectory()==false) {
			logger.error("File is null in getFilesByDirectory and path is "+path);
			return null;
		}
		File[] files = file.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				boolean result = false;
				if(pathname.getName().endsWith(".xml")) result = true;
				return result;
			}
		});
		return files;
	}
	
	/**
	 * Get the folders in the folder.
	 * @param path the parent folder path
	 * @return all the folders in the current folder
	 */
	public static File[] getFoldersByDirectory(String path){
		Class<?> clazz = FileLoaderTools.class;
		URLClassLoader loader = (URLClassLoader) clazz.getClassLoader();
		URL url = null;
		url = loader.getResource(path);
		if(url == null) return null;	
		File file = null;
		try {
			file = new File(url.getFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(file.isDirectory()==false) {
			logger.error("File is null in getFilesByDirectory and path is "+path);
			return null;
		}
		File[] files = file.listFiles();
		return files;
	}
	
	/**
	 * Get the xml file from the classpath.
	 * @param fileName the file name
	 * @param path the path where the xml file located in
	 * @return the file specified
	 */
	public static File getXmlByName(String fileName,String path) {
		
		Class<?> clazz = FileLoaderTools.class;
		URLClassLoader loader = (URLClassLoader) clazz.getClassLoader();
		URL url = null;
		url = loader.getResource(path+fileName);
		if(url == null) {
			logger.error("Xml File read failed!");
			return null;	
		}
		File file = null;
		try {
			file = new File(url.getFile());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
}
