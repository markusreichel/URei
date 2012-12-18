package org.reichel.file;

import java.io.File;
import java.io.FileFilter;

public class ExtensionFileFilter implements FileFilter {

	private final String extension;
	
	private boolean ignoreCase = true;
	
	public ExtensionFileFilter(String extension) {
		this.extension = extension;
	}
	
	public ExtensionFileFilter setIgnoreCase(boolean ignoreCase){
		this.ignoreCase = ignoreCase;
		return this;
	}

	@Override
	public boolean accept(File pathname) {
		if(pathname != null && pathname.isFile()){
			String absolutePath = pathname.getAbsolutePath();
			if(this.ignoreCase){
				absolutePath = absolutePath.toLowerCase();
			}
			return (absolutePath.endsWith(extension.toLowerCase()));
		}
		return false;
	}

}
