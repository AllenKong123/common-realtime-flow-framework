package com.sogou.flow.utils.model;

import java.util.ArrayList;
import java.util.List;

import com.sogou.flow.constants.XmlVocabulary;

/**
 * The items entity, it contains the information of the record and its submenus. 
 * @author hequn
 *
 */
public class MenuItem implements Comparable<MenuItem>{

	private String dimension = XmlVocabulary.DEFAULT_DEMENSION;
	private String itemName;
	private Integer order;

	private List<MenuItem> childrenItems = new ArrayList<MenuItem>();//the sub menus

	@Override
	public int compareTo(MenuItem o) {
		int result = this.order>o.getOrder()?1:-1;
		return result;
	}

	public MenuItem() {

	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
	
	public List<MenuItem> getChildrenItems() {
		return childrenItems;
	}

	public void setChildrenItems(List<MenuItem> childrenItems) {
		this.childrenItems = childrenItems;
	}
		
	public String getDimension() {
		return dimension;
	}

	public void setDimension(String dimension) {
		this.dimension = dimension;
	}

}
