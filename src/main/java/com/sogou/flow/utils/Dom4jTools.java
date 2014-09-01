package com.sogou.flow.utils;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * Common tools to get xml and analyse them into Document({@link org.dom4j.Document})
 * @author hequn
 * 2014-8-19 12:38:19
 */
public class Dom4jTools {
	
	public static Logger logger = Logger.getLogger(Dom4jTools.class);
	/**
	 * Parse the file to document using by Menuloader.
	 * @param file the xml file to be loaded
	 * @return the dom4j document
	 */
	public static Document getDocByFile(File file){
		SAXReader reader = new SAXReader();
		try {
			Document document = reader.read(file);
			return document;
		} catch (DocumentException e) {
			e.printStackTrace();
			logger.error("File parse failed");
			return null;
		}
	}
	
	/**
	 * Parse the files to document using by Menuloader.
	 * @param files the files to be transformed to documents
	 * @return the list of the dom4j documents
	 */
	public static List<Document> getDocsByPathAndName(File[] files){
		SAXReader reader = new SAXReader();
		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < files.length; i++) {
		
			try {
				Document document = reader.read(files[i]);
				documents.add(document);
			} catch (DocumentException e) {
				e.printStackTrace();
				logger.error("File parse failed");
				return null;
			}
		}
		return documents;
	}
	
	/**
	 * Set the value from the xml to instance by reflection(Element level).
	 * @param <T> any type to be reflected to
	 * @param instance the specified instance
	 * @param ele the current element
	 */
	public static <T> void setValueToInstanceByMethod(T instance , Element ele){
		
		Element element = ele;
		@SuppressWarnings("unchecked")
		List<Attribute> attributes = element.attributes();
		for (Attribute attribute : attributes) {
			String field = attribute.getName();
			StringBuilder builder = new StringBuilder();
			builder.append("set")
						.append(field.substring(0, 1)
								.toUpperCase())
									.append(field.substring(1, field.length()));
			try {
				Method method = instance.getClass().getMethod(builder.toString(), String.class);
				method.invoke(instance, attribute.getValue());
			}catch(NoSuchMethodException e){
				continue;
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
