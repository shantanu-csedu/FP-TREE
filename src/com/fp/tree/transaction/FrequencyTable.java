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
import java.util.Hashtable;
import java.util.List;

import com.fp.tree.data.FreqItem;
import com.fp.tree.node.FpTree;

public class FrequencyTable {
	

	Hashtable<Integer, Integer> hashTable;// item --> freq
	List<FreqItem> freqTable;
	public Hashtable<Integer,FpTree> startNode;
	
	public FrequencyTable(TransactionTable table) {
		hashTable = new Hashtable<Integer,Integer>();
		freqTable = new ArrayList<FreqItem>();
		startNode = new Hashtable<Integer,FpTree>();
		
		for(int i=0;i<table.getCount();i++){
			TransactionItem item = table.getItem(i);
			
			for(int j=0;j<item.getSize();j++){
				if(hashTable.containsKey(item.getItem(j))){ //already exits. just inc 1 count
					hashTable.put(item.getItem(j), item.getCount() + hashTable.get(item.getItem(j)) );
				}
				else{
					hashTable.put(item.getItem(j),item.getCount());
				}
			}
		}
		
		for(int key : hashTable.keySet()){
			if(hashTable.get(key) >= table.getSupportCount())
				freqTable.add(new FreqItem(key, hashTable.get(key)));
		}
		
		Collections.sort(freqTable, new Comparator<FreqItem>() {

			@Override
			public int compare(FreqItem o1, FreqItem o2) {
				if(o1.getItemValue() == o2.getItemValue()){
					return o1.getItemName() - o2.getItemName();
				}
				
				return o2.getItemValue() - o1.getItemValue();
			}
			
		});
	}
	
	public Hashtable<Integer, Integer> getHashTable() {
		return hashTable;
	}

	public List<FreqItem> getFreqTable() {
		return freqTable;
	}
	
	public int getSize(){
		return freqTable.size();
	}
	
	public void print(){
		System.out.println("\n========Frequncy table========\n");
		for(int i=0;i<freqTable.size(); i++){
			System.out.println("item: " + freqTable.get(i).getItemName() + " ----> freq: " + freqTable.get(i).getItemValue() );
		}
	}
}
