/*
 * Copyright (C) 2013 shantanu saha <shantanucse18@gmail.com>
 *
 * You can distribute, modify this project. 
 * But you can't use it as academic project or assignment without a good amount of modification.
 */
package com.fp.tree.main;

import java.io.File;
import java.io.FileNotFoundException;

import com.fp.tree.node.FpTree;
import com.fp.tree.transaction.FrequencyTable;
import com.fp.tree.transaction.TransactionTable;

public class Main {
	private final int supportCount = 2700;
	public static void main(String[] args) {
		
		new Main().Fptree();
	}
	
	public void Fptree(){
		
		TransactionTable ttable = new TransactionTable(supportCount);
		FrequencyTable ftable;
		FpTree tree = new FpTree();
		try {
			/*generate transaction table from file */
			ttable.generateTable(new File("res/chess.dat"));
			// generate frequency table from transaction table
			ftable = new FrequencyTable(ttable);
			// sort transaction table order by frequent item
			ttable.sort(ftable);
			ttable.print();
			ftable.print();
			// generate fp-tree structure
			tree.generateTree(ttable, ftable);
			// calculate frequent pattern 
			tree.generateConditionalPBase(ftable,supportCount);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
