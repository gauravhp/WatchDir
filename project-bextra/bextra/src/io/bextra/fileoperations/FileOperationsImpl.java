package io.bextra.fileoperations;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import io.bextra.events.FileOperations;

public class FileOperationsImpl implements FileOperations{
	public List<List<String>> findChangesInFile(Path filePath){
		
		System.out.format("\n Detecting file changes in the file " + filePath.getFileName().toString());
		List<List<String>> newTuplesAddedToFile = new ArrayList<List<String>>();
		// TODO: 
		// 1. read the file
		// 2. get the tuples from the database
		// 3. figure out the difference
		// 4. add these new lines in newRowsAddedToFile
		
		return newTuplesAddedToFile;
	}
	
	public List<List<String>> readFileAndReturnTuples(Path filePath){
		System.out.format("\n Reading the file: " +  filePath.getFileName().toString());
		List<List<String>> listOfTuples = new ArrayList<List<String>>();
		// TODO: 
		// 1. read the json file
		// 2. create a tuple which will be stored in List<String>
		// 3. add this to listOfTuples
		return listOfTuples;
	}
}
