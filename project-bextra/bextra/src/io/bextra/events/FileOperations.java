package io.bextra.events;

import java.nio.file.Path;
import java.util.List;

public interface FileOperations {
	public List<List<String>> findChangesInFile(Path filePath);
	public List<List<String>> readFileAndReturnTuples(Path filePath);
}
