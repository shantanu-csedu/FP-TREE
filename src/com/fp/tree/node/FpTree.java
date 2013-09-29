/*
 * Copyright (C) 2013 shantanu saha <shantanucse18@gmail.com>
 *
 * You can distribute, modify this project. 
 * But you can't use it as academic project or assignment without a good amount of modification.
 */
package com.fp.tree.node;

import java.util.Hashtable;
import java.util.List;
import java.util.Vector;
import com.fp.tree.data.FreqItem;
import com.fp.tree.transaction.FrequencyTable;
import com.fp.tree.transaction.TransactionItem;
import com.fp.tree.transaction.TransactionTable;

public class FpTree {
	/*
	 * FpTree use as a node. Each node contains parent (previous node)
	 * brotherNode (next node with same name in other forest)
	 * count
	 * childs (a hash of childs)
	 */
	private FpTree parent,brotherNode;
	private int nodeName;
	private int count;
	private int supportCount;
	private FrequencyTable orgFrequencyTable;
	
	public void setSupportCount(int supportCount) {
		this.supportCount = supportCount;
	}
	
	public void setOrgFreqTable(FrequencyTable orgFrequencyTable){
		this.orgFrequencyTable = orgFrequencyTable;
	}

	private Hashtable<Integer, FpTree> childs;
	
	
	public FpTree() {
		childs = new Hashtable<Integer,FpTree>();
		brotherNode = null;
	}
	/* 
	 * Generate fp-tree
	 */
	public void generateTree(TransactionTable ttable,FrequencyTable ftable,FpTree root){
		
		for(int i=0;i<ttable.getCount(); i++){
			FpTree current = root; // assume this as tree root
			current.nodeName = -1;// not valid node
			FpTree prev = null;
			TransactionItem transaction = ttable.getItem(i); // a transaction example: 3 5 6
			
			for(int j=0;j<transaction.getSize();j++){
				int node = transaction.getItem(j);  // item of a transaction. example: 3 from (3 5 6)
				if(node ==0){
					System.out.println("");
				}
				FpTree tmp;
				if( current.childs.containsKey(node) ) // if childs hash contains this node that means we don't need to create new node.
				{
					tmp = current.childs.get(node);
					tmp.parent = prev;
					tmp.count += transaction.getCount();
					current.childs.put(node, tmp);
					prev = current ;
					current = tmp;
				}
				else{
					tmp = new FpTree();
					tmp.nodeName = node;
					tmp.count = transaction.getCount();
					tmp.parent = current;
					current.childs.put(node, tmp);
					
					if(!ftable.startNode.containsKey(node)){ // Frequency table contains a fp-tree node which indicate the first in fp-tree
						ftable.startNode.put(node, tmp); 
					}
					else{
						FpTree nextNode = ftable.startNode.get(node); // If the node occurs several time then save instance on last item. see fp-tree documentation
						while(nextNode.brotherNode !=null){
							nextNode = nextNode.brotherNode;
						}
						nextNode.brotherNode = tmp;
					}
				}
				prev = current ;
				current = tmp;
			}
		}
	}
	
	public class NodeVector{
		Vector<Integer> name;
		Hashtable<Integer,Integer> cnt;
		public NodeVector() {
			name = new Vector<Integer>();
			cnt = new Hashtable<Integer,Integer> ();
		}
	}
	
	
	private void generateTIFromFpTree(FpTree startNode,TransactionItem tItem){
		if(startNode == null) return ;
		if(startNode.parent !=null)
			generateTIFromFpTree(startNode.parent,tItem);
		if(startNode.nodeName != -1)
			tItem.addItem(startNode.nodeName);
	}
	
	public void findFreqItems(FrequencyTable ftable,FpTree root,int level,String str){ //
		List<FreqItem> freqList = ftable.getFreqTable();
		if(level == 1 ) System.out.println("\n=========Frequent Patterns========\n");
		
		for(int i=freqList.size()-1;i>=0;i--){ // frequency increasing order [ 2 3 6 7 ..]
			int item = freqList.get(i).getItemName();
			FpTree startNode = ftable.startNode.get(item); // traverse tree, start by saved node in frequency table. [2 or 5 or 4 etc] order by frequency
			int itemValue = startNode.count;
			TransactionTable tt = new TransactionTable(supportCount);
			
			while(startNode != null){
				TransactionItem tItem = new TransactionItem();
				generateTIFromFpTree(startNode.parent, tItem);
				tItem.setCount(itemValue);
				if(tItem.getSize() > 0)
					tt.addItem(tItem);
				startNode = startNode.brotherNode;
			}
			if(tt.getCount() == 0 && level > 1){
			//	System.out.println("1{" + str + " " + ftable.startNode.get(item).nodeName + ": " + itemValue + " }");
				continue;
			}
			FrequencyTable ft = new FrequencyTable(tt);
			if(level == 1){
				for(int fi=0;fi<ft.getSize();fi++){
					System.out.println("{ " + ftable.startNode.get(item).nodeName + " "+ ft.getFreqTable().get(fi).getItemName() + " : "+ ft.getFreqTable().get(fi).getItemValue() +" }");
				}
			}
			if(ft.getSize() > 1){
				tt.sort(orgFrequencyTable);
				FpTree ftree = new FpTree();
				generateTree(tt, ft, ftree);
				//System.out.print(ftable.startNode.get(item).nodeName + " ");
				findFreqItems(ft, ftree, level+1,str + " " + ftable.startNode.get(item).nodeName);
			}
			else if(ft.getSize() == 1 && level>1){
				System.out.println("{" + str + " " + ftable.startNode.get(item).nodeName+ " "+ ft.getFreqTable().get(0).getItemName() + " : "+ ft.getFreqTable().get(0).getItemValue() +" }");
			}
		}
	}
	
}
