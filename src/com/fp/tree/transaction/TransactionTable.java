package com.fp.tree.transaction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TransactionTable {
	
	private int supportCount;
	private List<TransactionItem> transactionTable;
	
	public TransactionTable(int supportCount) {
		this.supportCount = supportCount;
		transactionTable = new ArrayList<TransactionItem>();
	}
	
	public int getSupportCount() {
		return supportCount;
	}

	@SuppressWarnings("resource")
	public void generateTable(File file) throws FileNotFoundException{
		Scanner scanner = new Scanner(file);
		
		while(scanner.hasNext()){
			String line = scanner.nextLine().trim();
			transactionTable.add(new TransactionItem(line));
		}
	}
	
	public void print(){
		for(int i=0;i<transactionTable.size();i++){
			TransactionItem item = transactionTable.get(i);
			item.print();
			System.out.println("");
		}
	}
	
	public int getCount(){
		return transactionTable.size();
	}
	
	public TransactionItem getItem(int pos){
		return transactionTable.get(pos);
	}

	public void sort(FrequencyTable ftable) {
		for(int i=0;i<transactionTable.size();i++){
			TransactionItem item = transactionTable.get(i);
			item.sort(ftable);
			transactionTable.set(i, item);
		}
	}
}
