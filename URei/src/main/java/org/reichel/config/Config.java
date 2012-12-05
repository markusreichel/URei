package org.reichel.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.reichel.properties.SortedProperties;

public class Config {

	private Logger logger = Logger.getLogger(Config.class);
	
	private SortedProperties property = new SortedProperties();
	
	private final String configPath;
	
	private final Charset charset;

	public Config(Charset charset, String configPath){
		if(charset == null){
			logger.error("Parameter charset cannot be null.");
			throw new IllegalArgumentException("Parameter charset cannot be null.");
		}
		if(configPath == null || "".equals(configPath)){
			logger.error("Parameter configPath cannot be null.");
			throw new IllegalArgumentException("Parameter configPath cannot be null.");
		}
		File fileConfig = new File(configPath);
		this.configPath = configPath;
		this.charset = charset;
		if(!fileConfig.exists()){
			logger.error("Configuração não encontrada: " + fileConfig.getAbsolutePath());
			throw new IllegalArgumentException("Configuração não encontrada: " + fileConfig.getAbsolutePath());
		}
		loadProperty(fileConfig);
	}

	private void loadProperty(File fileConfig) {
		try {
			this.property.load(new InputStreamReader(new FileInputStream(fileConfig),this.charset));
		} catch (IOException e) {
			logger.error("Erro ao carregar propriedade '" + this.configPath + "'. " + e.getClass().getName() + ":" + e.getMessage());
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
	
	public void save(){
		try {
			this.property.store(new OutputStreamWriter(new FileOutputStream(this.configPath), this.charset), "Última atualização");
		} catch (FileNotFoundException e) {
			logger.error("Erro ao salvar propriedade '" + this.configPath + "'. " + e.getClass().getName() + ":" + e.getMessage());
		} catch (IOException e) {
			logger.error("Erro ao carregar propriedade '" + this.configPath + "'. " + e.getClass().getName() + ":" + e.getMessage());		
		}
	}
	
	public void put(String key, String value){
		this.property.setProperty(key, value);
	}
	
	public void reload(){
		this.property.clear();
		loadProperty(new File(this.configPath));
	}
	
	public void saveAndReload(){
		save();
		reload();
	}
	
}
