package com.fp.tree.main;

import java.io.File;
import java.io.FileNotFoundException;

import com.fp.tree.node.FpTree;
import com.fp.tree.transaction.FrequencyTable;
import com.fp.tree.transaction.TransactionTable;

public class Main {
	public static void main(String[] args) {
		
		new Main().Fptree();
	}
	
	public void Fptree(){
		TransactionTable ttable = new TransactionTable(2);
		FrequencyTable ftable;
		FpTree tree = new FpTree();
		try {
			ttable.generateTable(new File("res/chess.dat"));
//			ttable.print();
			ftable = new FrequencyTable(ttable);
			ftable.print();
			ttable.sort(ftable);
			ttable.print();
			tree.generateTree(ttable, ftable);
			tree.generateConditionalPBase(ftable,2);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
