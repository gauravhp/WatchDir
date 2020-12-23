package io.bextra.dboperations;

import java.util.List;

import io.bextra.events.TableOperations;

public class TableOperationsImpl implements TableOperations {

	public boolean createTable(String tableName, List<List<String>> tuples) {
		System.out.format("\n Creating new table: " + tableName + " with tuples ");
		for(List<String> tuple : tuples) {
			for(String value : tuple) {
				System.out.format(value + ",");
			}
		}
		// TODO: 
		// 1. Create table in the DB
		// 2. Insert tuples into the newly created table
		
		return true;
	}

	public boolean updateTable(String tableName, List<List<String>> tuples) {
		
		System.out.format("\n Updating table: " + tableName + " with tuples ");
		for(List<String> tuple : tuples) {
			for(String value : tuple) {
				System.out.format(value + ",");
			}
		}
		
		// TODO: 
		// 1. Figure out if the table exists, if not call createTable method to create it
		// 2. Insert provided tuples into this table		
		return true;
	}
	
	private boolean insertRow(String tableName, List<List<String>> tuples) {
		// TODO:
		// 1. For Each tuple - insert the tuple into the table tableName
		return true;
	}
}
