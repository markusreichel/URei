package org.reichel.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SaveLog {

	private File file;
	
	private final String path;
	
	public SaveLog(String path){
		if(path == null){
			throw new IllegalArgumentException("Parameter path cannot be null.");
		}
		if(!path.endsWith(Character.toString(File.separatorChar))){
			path = path + File.separatorChar;
		}
		File f = new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		this.path = path;
	}
	
	public static SaveLog save(String path, String log){
		return new SaveLog(path).save(log);
	}
	
	public SaveLog save(String log){
		getLogFile();
		write(log);
		return this;
	}

	private void write(String log) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(this.file);
			fw.write(log);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void getLogFile() {
		int counter = 0;
		File f = null;
		while(f == null){
			counter ++;
			f = new File(this.path + new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_" + counter + ".log");
			if(f.exists()){
				f = null;
			} else {
				this.file = f;
			}
		}
	}

	public File getFile() {
		return file;
	}

	public String getPath() {
		return path;
	}
	
}
