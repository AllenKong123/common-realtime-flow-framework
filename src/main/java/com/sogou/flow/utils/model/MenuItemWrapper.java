package com.sogou.flow.utils.model;

import java.util.ArrayList;
import java.util.List;

/**
 * The bean of the menu, items will be wrapped in. The subMenus are considered in together.
 * @author hequn
 * 2014-8-19 14:09:41
 */
public class MenuItemWrapper {

	//The productName will used as the api token
	private String productName;
	//As the name of the element
	private String menuName;
	//schema file name
	private String dataSchemaFile;
	//head menu
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();

	//a list to be set dimensions
	private List<String> dimensions = new ArrayList<String>();
	
	public MenuItemWrapper(){
		
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getMenuName() {
		return menuName;
	}
	
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	
	public List<MenuItem> getMenuItems() {
		return menuItems;
	}

	public List<String> getDimensions() {
		return dimensions;
	}
	
	public String getDataSchemaFile() {
		return dataSchemaFile;
	}
	
	public void setDataSchemaFile(String dataSchemaFile) {
		this.dataSchemaFile = dataSchemaFile;
	}
}
