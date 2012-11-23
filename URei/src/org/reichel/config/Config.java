package org.reichel.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Config {

	private Properties property = new Properties();

	public Config(Charset charset, String configPath){
		if(charset == null){
			throw new IllegalArgumentException("Parameter charset cannot be null.");
		}
		if(configPath == null || "".equals(configPath)){
			throw new IllegalArgumentException("Parameter configPath cannot be null.");
		}
		File fileConfig = new File(configPath);
		if(!fileConfig.exists()){
			throw new IllegalArgumentException("Configuração não encontrada: " + fileConfig.getAbsolutePath());
		}
		try {
			
			this.property.load(new InputStreamReader(new FileInputStream(fileConfig),charset));
		} catch (IOException e) {
			System.out.println("Erro ao carregar propriedade 'config.properties'. " + e.getClass().getName() + ":" + e.getMessage());
		}
	}
	
	public Config(){
		this(Charset.forName("UTF-8"), "config" + File.separatorChar + "config.properties");
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
	
}
