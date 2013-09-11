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
	
	private void conditionalTreeTraverse(FpTree root, int supportCount,String str,int startItem, long count){
		if(root == null ) return;
		
		if(str.trim().split(" ").length > 2 && count >= supportCount){ //
			System.out.println("{ " + startItem + str + " : " + count + " }");
		}
		for(int skey : root.childs.keySet()){
			FpTree stmp = root.childs.get(skey);
			if(stmp.count >= supportCount){
				
				if(itemCount.cnt.containsKey(stmp.nodeName)){
					int tcount = itemCount.cnt.get(stmp.nodeName) + stmp.count;
					itemCount.cnt.put(stmp.nodeName,tcount);
				}
				else{
					itemCount.name.add(stmp.nodeName);
					itemCount.cnt.put(stmp.nodeName,stmp.count);
				}
				
			}
			conditionalTreeTraverse(stmp, supportCount, str + ", " + stmp.nodeName,startItem, Math.min(count, stmp.count));	
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
	
	private void fpTreeTraverse(FpTree currentNode,int cnt){
		if(currentNode == null) return ;
		if(currentNode.parent !=null)
			fpTreeTraverse(currentNode.parent,cnt);
		
		if(curSTree.childs.containsKey(currentNode.nodeName)){ // generating conditional tree
			FpTree fttmp = curSTree.childs.get(currentNode.nodeName);
			fttmp.count += cnt;
			curSTree.childs.put(currentNode.nodeName, fttmp);
			curSTree = fttmp;
		}
		else{
			FpTree stmp = new FpTree();
			stmp.nodeName = currentNode.nodeName;
			stmp.count = cnt;
			curSTree.childs.put(stmp.nodeName, stmp);
			curSTree = stmp;
		}
		
//		System.out.println(currentNode.nodeName + " " + curSTree.count);
		
		
	}
	private FpTree curSTree;
	private NodeVector itemCount;
	public void generateConditionalPBase(FrequencyTable ftable, int supportCount){
		List<FreqItem> freqList = ftable.getFreqTable();
		System.out.println("\n=========Frequent Patterns========\n");
		
		for(int i=freqList.size()-1;i>=0;i--){ // frequency increasing order [ 2 3 6 7 ..]
			int item = freqList.get(i).getItemName();
			
			FpTree startNode = ftable.startNode.get(item); // traverse tree, start by saved node in frequency table. [2 or 5 or 4 etc] order by frequency
			int itemValue = startNode.count;
			System.out.println("NODE START: " + item);
			// after a loop all tree complete for a single node
			FpTree secondaryTree = new FpTree();
			itemCount = new NodeVector();
			while(startNode !=null){ // each node parents. 
				curSTree = secondaryTree;
				
				fpTreeTraverse(startNode.parent,itemValue);
				startNode = startNode.brotherNode;
				if(startNode !=null) continue;
				// all tree traverse for 5
				conditionalTreeTraverse(secondaryTree, supportCount,"",item,9999999999999999l);
//				System.out.println("TREE : " + itemCount.name);
				/*
				 * combination
				 */
				ICombinatoricsVector<Integer> initialVector = Factory.createVector(itemCount.name );
				Generator<Integer> gen = Factory.createSimpleCombinationGenerator(initialVector, 1);
					
				for (ICombinatoricsVector<Integer> combination : gen) {
					String key="";
					long min_cnt = 9999999999999999l;
					key += item;
					for(int v=0;v<combination.getVector().size();v++){
						int vItem = combination.getValue(v);
						key+= ", " + vItem;
						min_cnt = Math.min(min_cnt, itemCount.cnt.get(vItem));
					}
					System.out.println("{ " + key + " : " + min_cnt+" }");
	
				}
			}
			System.out.println("NODE END: " + item + "\n");
		}
		System.out.println("\n=========END========\n");
	}
}
