/*
 * Copyright (C) 2013 shantanu saha <shantanucse18@gmail.com>
 *
 * You can distribute, modify this project. 
 * But you can't use it as academic project or assignment without a good amount of modification.
 */
package com.fp.tree.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;

import com.fp.tree.node.FpTree;
import com.fp.tree.transaction.FrequencyTable;
import com.fp.tree.transaction.TransactionTable;

public class Main {
	private final int supportCount = 2;
	public static void main(String[] args) {
		
		new Main().Fptree();
	}
	
	public void Fptree(){
		
		TransactionTable ttable = new TransactionTable(supportCount);
		FrequencyTable ftable;
		FpTree tree = new FpTree();
		tree.setSupportCount(supportCount);
		try {
			long startTime = new Date().getTime();
			/*generate transaction table from file */
			ttable.generateTable(new File("res/chess.dat"));
			// generate frequency table from transaction table
			ftable = new FrequencyTable(ttable);
			tree.setOrgFreqTable(ftable);
			// sort transaction table order by frequent item
			ttable.sort(ftable);
			ttable.print();
			ftable.print();
			// generate fp-tree structure
			tree.generateTree(ttable, ftable, tree);
			// calculate frequent pattern 
			tree.findFreqItems(ftable,tree,1,"");
			long endTime = new Date().getTime();
			System.out.println("Support count: " + supportCount);
			System.out.println("Execution time(including print): " + (double) ((endTime - startTime)/1000.00) + " secs");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
