package io.bextra.events;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileUpdateEventListener implements EventListener {
	TableOperations tableOperations;
	FileOperations fileOperations;
	
	
	public FileUpdateEventListener(TableOperations tableOperations, FileOperations fileOperations) {
		super();
		this.tableOperations = tableOperations;
		this.fileOperations = fileOperations;
	}


	@Override
	public void update(Path filePath) {
		System.out.format("\nFile update even received for: " + filePath.toString());
		List<List<String>> changedTuples = new ArrayList<List<String>>();
		changedTuples = fileOperations.findChangesInFile(filePath);
		tableOperations.updateTable(filePath.getFileName().toString(), changedTuples);
	}



}
