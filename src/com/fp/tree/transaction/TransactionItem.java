/*
 * Copyright (C) 2013 shantanu saha <shantanucse18@gmail.com>
 *
 * You can distribute, modify this project. 
 * But you can't use it as academic project or assignment without a good amount of modification.
 */
package com.fp.tree.transaction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.fp.tree.data.FreqItem;


public class TransactionItem {
	private List<Integer> transactionItem;
	private int count=1;
	
	public void setCount(int count) {
		this.count = count;
	}
	
	public int getCount(){
		return this.count;
	}

	public TransactionItem(){
		transactionItem = new ArrayList<Integer>();
	}
	
	public TransactionItem(List<Integer> item) {
		this.transactionItem = item;
	}
	
	public TransactionItem(String items){ // seperate by space.1 2 3 ..etc
		transactionItem = new ArrayList<Integer>();
		String item[] = items.split(" ");
		for(int i=0;i<item.length;i++){
			try{
				transactionItem.add(Integer.parseInt(item[i].trim()));
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	public void addItem(int item){
		if(!transactionItem.contains(item))
			transactionItem.add(item);
	}
	public void sort(FrequencyTable ft){
		List<FreqItem> tmp = new ArrayList<FreqItem>();
		for(int i=0;i<transactionItem.size();i++){
			int key = transactionItem.get(i);
			int value = ft.getHashTable().get(key);
			tmp.add( new FreqItem(key, value) );
		}
		
		Collections.sort(tmp, new Comparator<FreqItem>() {

			@Override
			public int compare(FreqItem o1, FreqItem o2) {
				if(o1.getItemValue() == o2.getItemValue()){
					return o1.getItemName() - o2.getItemName();
				}
				
				return o2.getItemValue() - o1.getItemValue();
			}
			
		});
		for(int i=0;i<transactionItem.size();i++){
			int transItem = tmp.get(i).getItemName();
			transactionItem.set(i, transItem);
		}
	}
	
	public int getSize(){
		return transactionItem.size();
	}
	
	public Integer getItem(int pos){
		return transactionItem.get(pos);
	}
	
	public void print(){
		for(int i=0;i<transactionItem.size();i++){
			if(i>0) System.out.print(" ");
			System.out.print(transactionItem.get(i));
		}
	}
}
