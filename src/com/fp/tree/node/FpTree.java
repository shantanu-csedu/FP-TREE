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
	private FpTree parent,brotherNode;
	private int nodeName;
	private int count;
	private Hashtable<Integer, FpTree> childs;
	
	
	public FpTree() {
		childs = new Hashtable<Integer,FpTree>();
		brotherNode = null;
	}
	
	public void generateTree(TransactionTable ttable,FrequencyTable ftable){
		
		for(int i=0;i<ttable.getCount(); i++){
			FpTree current = this;
			FpTree prev = null;
			TransactionItem transaction = ttable.getItem(i);
			
			for(int j=0;j<transaction.getCount();j++){
				int node = transaction.getItem(j);
				FpTree tmp;
				if( current.childs.containsKey(node) )
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
					
					if(!ftable.startNode.containsKey(node)){
						ftable.startNode.put(node, tmp);
					}
					else{
						FpTree nextNode = ftable.startNode.get(node);
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
		
		for(int i=freqList.size()-1;i>=0;i--){
			int item = freqList.get(i).getItemName();
			FpTree startNode = ftable.startNode.get(item);
			System.out.println("NODE START: " + item);
			Hashtable<Integer, Integer> itemCount = new Hashtable<Integer,Integer>();
			while(startNode !=null){
				FpTree currentNode = startNode.parent;
				//itemCount.put(startNode.nodeName, startNode.count);
				while(currentNode != null){
					
					if(itemCount.containsKey(currentNode.nodeName)){
						int curCount = itemCount.get(currentNode.nodeName);
						itemCount.put(currentNode.nodeName, startNode.count+curCount);
					}
					else{
						itemCount.put(currentNode.nodeName, startNode.count);
					}
					currentNode = currentNode.parent;
				}
				
				startNode = startNode.brotherNode;
			}
			Vector<Integer> itemVec = new Vector<Integer>();
			for(int key : itemCount.keySet()){
				if(itemCount.get(key) >= supportCount){
					System.out.print(key + ":" + itemCount.get(key) + ",");
					itemVec.add(key);
				}
			}
			System.out.println("");
			ICombinatoricsVector<Integer> initialVector = Factory.createVector(itemVec );
			for(int pSize=1;pSize<=itemVec.size();pSize++){
				Generator<Integer> gen = Factory.createSimpleCombinationGenerator(initialVector, pSize);
				// Print all possible combinations
				
				for (ICombinatoricsVector<Integer> combination : gen) {
					int min=655214;
//					min = Math.min(min, ftable.startNode.get(item).count);
					System.out.print("{"+item);
					for(int v=0;v<combination.getVector().size();v++){
						int vItem = combination.getValue(v);
						min = Math.min(min, itemCount.get(vItem));
						System.out.print(", " + vItem);
					}
					System.out.println(" : " + min+"}");
//					System.out.println(item + ", "+combination.getVector());
				}
			}
			System.out.println("");
			System.out.println("NODE END: " + item);
		}
	}
}
