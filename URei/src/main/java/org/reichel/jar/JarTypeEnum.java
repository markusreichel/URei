package org.reichel.jar;

public enum JarTypeEnum {

	JAR("jar"), JAR_EXPLODED("jar:exploded");
	
	private final String type;
	
	private JarTypeEnum(String type){
		this.type = type;
	}
	
	public static JarTypeEnum fromType(String type){
		for(JarTypeEnum jarTypeEnum : values()){
			if(jarTypeEnum.getType().equals(type)){
				return jarTypeEnum;
			}
		}
		return null;
	}
	
	public String getType() {
		return type;
	}

}
