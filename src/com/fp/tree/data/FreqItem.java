/*
 * Copyright (C) 2013 shantanu saha <shantanucse18@gmail.com>
 *
 * You can distribute, modify this project. 
 * But you can't use it as academic project or assignment without a good amount of modification.
 */
package com.fp.tree.data;

public class FreqItem{
	int itemName;
	int itemValue;
	
	public FreqItem(int itemName, int itemValue) {
		super();
		this.itemName = itemName;
		this.itemValue = itemValue;
	}

	public int getItemName() {
		return itemName;
	}

	public void setItemName(int itemName) {
		this.itemName = itemName;
	}
	
	public int getItemValue() {
		return itemValue;
	}
	
	public void setItemValue(int itemValue) {
		this.itemValue = itemValue;
	}
}