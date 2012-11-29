package org.reichel.jar;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Classe que representa uma versão de software.
 * major: Melhorias
 * minor: Pequenas melhorias
 * maintenance: Correções de bugs
 * @author Markus Reichel
 * <pre>
 * History: 28/11/2012 - Markus Reichel - Criação da classe
 * </pre>
 * 
 */
public class JarVersion implements Comparable<JarVersion>{

	private final Integer major;
	
	private final Integer minor;
	
	private final Integer maintenance;

	public JarVersion(String fullVersion){
		Pattern pattern = Pattern.compile("(\\d+)\\.(\\d+)\\.(\\d+)");
		Matcher matcher = pattern.matcher(fullVersion);
		if(!matcher.matches()){
			throw new IllegalArgumentException("Versão: '" + fullVersion + "' não é uma versão válida, exemplo de versão válida: 1.5.33");
		}
		this.major = Integer.valueOf(matcher.group(1));
		this.minor = Integer.valueOf(matcher.group(2));
		this.maintenance = Integer.valueOf(matcher.group(3));
	}
	
	public Integer getMajor() {
		return major;
	}

	public Integer getMinor() {
		return minor;
	}

	public Integer getMaintenance() {
		return maintenance;
	}

	@Override
	public String toString(){
		return this.major + "." + this.minor + "." + maintenance;
	}

	@Override
	public int compareTo(JarVersion o) {
		if(o == null){
			return 1;
		}
		
		int major = this.getMajor().compareTo(o.getMajor());
		if(major != 0){
			return major;
		}
		
		int minor = this.getMinor().compareTo(o.getMinor());
		if(minor != 0){
			return minor;
		}
		
		return this.getMaintenance().compareTo(o.getMaintenance());
	}
}
