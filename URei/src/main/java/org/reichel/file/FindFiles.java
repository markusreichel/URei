package org.reichel.file;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindFiles {

	private Integer deep = null;
	
	private boolean ignoreCase = true;
	
	public List<File> findFileByExtension(String rootFolder, String extension){
		List<File> result = new ArrayList<File>();
		File root = new File(rootFolder);
		if(root.isDirectory()){
			result = findFile(root, new ExtensionFileFilter(extension).setIgnoreCase(ignoreCase), new ArrayList<File>(), 0);
		}
		return result;
	}

	public FindFiles ignoreCase(boolean ignoreCase){
		this.ignoreCase = ignoreCase;
		return this;
	}
	
	public FindFiles deep(Integer deep){
		this.deep = deep;
		return this;
	}
	
	private List<File> findFile(File root, FileFilter fileFilter, List<File> result, Integer deep) {
		File[] listFiles = root.listFiles(fileFilter);
		if(listFiles != null && listFiles.length > 0){
			result.addAll(Arrays.asList(listFiles));
		}
		
		if(this.deep == null || deep < this.deep){
			listFiles = root.listFiles(new DirectoryFileFilter());
			if(listFiles != null && listFiles.length > 0){
				for(File dir : listFiles){
					result = findFile(dir, fileFilter, result, deep + 1);
				}
			}
		}
		return result;
	}
	
}
