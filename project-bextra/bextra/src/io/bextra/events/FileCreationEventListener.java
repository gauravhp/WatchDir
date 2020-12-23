package io.bextra.events;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileCreationEventListener implements EventListener {
	TableOperations tableOperations;
	FileOperations fileOperations;

	public FileCreationEventListener(TableOperations tableOperations, FileOperations fileOperations) {
		this.tableOperations = tableOperations;
		this.fileOperations = fileOperations;
	}

	@Override
	public void update(Path filePath) {
		System.out.format("\nFile creation even received for: " + filePath.toString());
		List<List<String>> newTuples = new ArrayList<List<String>>();
		newTuples = fileOperations.readFileAndReturnTuples(filePath);
		tableOperations.createTable(filePath.getFileName().toString(), newTuples);
	}

}
