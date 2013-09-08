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

import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

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
	private Hashtable<Integer, FpTree> childs;
	
	
	public FpTree() {
		childs = new Hashtable<Integer,FpTree>();
		brotherNode = null;
	}
	/* 
	 * Generate fp-tree
	 */
	public void generateTree(TransactionTable ttable,FrequencyTable ftable){
		
		for(int i=0;i<ttable.getCount(); i++){
			FpTree current = this; // assume this as tree root
			FpTree prev = null;
			TransactionItem transaction = ttable.getItem(i); // a transaction example: 3 5 6
			
			for(int j=0;j<transaction.getCount();j++){
				int node = transaction.getItem(j);  // item of a transaction. example: 3 from (3 5 6)
				FpTree tmp;
				if( current.childs.containsKey(node) ) // if childs hash contains this node that means we don't need to create new node.
				{
					tmp = current.childs.get(node);
					tmp.parent = prev;
					tmp.count++;
					current.childs.put(node, tmp);
					prev = current ;
					current = tmp;
				}
				else{
					tmp = new FpTree();
					tmp.nodeName = node;
					tmp.count = 1;
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
	
	public void generateConditionalPBase(FrequencyTable ftable, int supportCount){
		List<FreqItem> freqList = ftable.getFreqTable();
		System.out.println("\n=========Frequent Patterns========\n");
		
		for(int i=freqList.size()-1;i>=0;i--){ // frequency increasing order [ 2 3 6 7 ..]
			int item = freqList.get(i).getItemName();
			FpTree startNode = ftable.startNode.get(item); // traverse tree, start by saved node in frequency table. [2 or 5 or 4 etc] order by frequency 
			System.out.println("NODE START: " + item);
			Hashtable<String,Integer> freqPatterns = new Hashtable<String,Integer>();
			
			while(startNode !=null){ // each node parents. 
				FpTree currentNode = startNode.parent;
				Vector<Integer> itemCount = new Vector<Integer>();
				while(currentNode != null){
					itemCount.add(currentNode.nodeName);
					currentNode = currentNode.parent;
				}
				/*
				 * combination
				 */
				ICombinatoricsVector<Integer> initialVector = Factory.createVector(itemCount );
				for(int pSize=1;pSize<=itemCount.size();pSize++){
					Generator<Integer> gen = Factory.createSimpleCombinationGenerator(initialVector, pSize);
					
					for (ICombinatoricsVector<Integer> combination : gen) {
						String key="";
						key += item;
						for(int v=0;v<combination.getVector().size();v++){
							int vItem = combination.getValue(v);
							key+= ", " + vItem;
							
						}
						if(freqPatterns.containsKey(key)){
							int fcount = freqPatterns.get(key);
							fcount+= startNode.count;
							freqPatterns.put(key, fcount);
						}
						else{
							freqPatterns.put(key, startNode.count);
						}
					}
				}
				
				startNode = startNode.brotherNode;
			}
			for(String key : freqPatterns.keySet()){
				if(freqPatterns.get(key) >= supportCount)
					System.out.println("{ " + key + " : " + freqPatterns.get(key)+" }");
			}
			System.out.println("NODE END: " + item + "\n");
		}
	}
}
