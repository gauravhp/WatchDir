package io.bextra.events;

import java.util.List;

public interface TableOperations {
	public boolean createTable(String tableName, List<List<String>> tuples);
	public boolean updateTable(String tableName, List<List<String>> tuples);
}
