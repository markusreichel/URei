package org.reichel.config;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {

	private Properties property = new Properties();
	
	public Config(){
		try {
			this.property.load(Config.class.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			System.out.println("Erro ao carregar propriedade 'config.properties'. " + e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public String get(String key) {
		return (String) this.property.get(key);
	}

	public String get(String key, String... args){
		return MessageFormat.format(get(key), (Object[]) args);
	}
	
	public List<String> getKeys(){
		List<String> result = new ArrayList<String>();
		for(Object o : this.property.keySet()){
			result.add((String) o );
		}
		return result;
	}
	
	public static void main(String[] args) {
		Config config = new Config();
		System.out.println(config.get("projetos"));
	}
	
}
