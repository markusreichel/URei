package org.reichel.comparator;

import java.util.Comparator;

import org.reichel.jar.JarVersion;

public class JarVersionFileNameComparator implements Comparator<JarVersion> {
	
	@Override
	public int compare(JarVersion o1, JarVersion o2) {
		if(o1 == null && o2 == null){
			return 0;
		}
		if(o1 == null && o2 != null){
			return -1;
		}
		if(o1 != null && o2 == null){
			return 1;
		}
		if(o1.getFileName() == null && o2.getFileName() == null){
			return 0;
		}
		if(o1.getFileName() == null && o2.getFileName() != null){
			return -1;
		}
		if(o1.getFileName() != null && o2.getFileName() == null){
			return 1;
		}
		return o1.getFileName().compareTo(o2.getFileName());
	}

}
