package org.reichel.download;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.charset.Charset;

import org.apache.log4j.Logger;
import org.reichel.command.output.Output;

public class DownloadFile {

	private static final Logger logger = Logger.getLogger(DownloadFile.class);
	
	private final String remoteTargetFolder;
	
	private URL url;
	
	private URLConnection connection;
	
	private final Output<Integer> output;
	
	private Integer fileLength;
	
	private Boolean connected = false;
	
	private String fileName;
	
	private File downloadedFile;
	
	private Integer timeout = 1000;
	
	public DownloadFile(Output<Integer> output, String path, Charset charset) throws UnsupportedEncodingException{
		this.output = output;
		this.remoteTargetFolder = URLDecoder.decode(path, charset.name());
	}
	
	public DownloadFile(Output<Integer> output, String remoteTargetFolder) throws UnsupportedEncodingException{
		this(output, remoteTargetFolder, Charset.forName("UTF-8"));
	}
	
	public DownloadFile connect(String fileName){
		if(fileName == null || "".equals(fileName)){
			throw new IllegalArgumentException("fileName n�o pode ser vazio ou nulo.");
		}
		this.fileName = fileName;
		if(isURL(fileName)){
			if(isConnectionOpened()){
				this.connection.setReadTimeout(this.timeout);
				this.connection.setUseCaches(false);
				if(isConnected()){
					this.fileLength = this.connection.getContentLength();
				}
			}
		}
		return this;
	}

	public DownloadFile timeout(Integer timeout){
		this.timeout = timeout;
		return this;
	}
	
	private boolean isURL(String fileName) {
		try {
			this.url = new URL(this.remoteTargetFolder + "/" + fileName);
			return true;
		} catch (MalformedURLException e) {
			logger.error("Problemas ao montar URL: " + e.getMessage());
		}
		return false;
	}

	private boolean isConnected() {
		try {
			this.connection.connect();
			if(this.connection instanceof HttpURLConnection){
				if(HttpURLConnection.HTTP_OK == ((HttpURLConnection) this.connection).getResponseCode()){
					this.connected = true;
				} else {
					this.connected = false;
				}
			} else {
				this.connected = true;
			}
			return true;
		} catch (IOException e) {
			this.connected = false;
			logger.error("Problemas ao conectar-se '" + this.connection.getURL() + "': " + e.getMessage());
		}
		return false;
	}

	private boolean isConnectionOpened() {
		try {
			this.connection = this.url.openConnection();
			return true;
		} catch (IOException e) {
			logger.error("Problemas ao abrir conex�o '" + this.connection.getURL() + "': " + e.getMessage());
		}
		return false;
	}
	
	/**
	 * Aten��o ao utilizar este m�todo a variavel connected n�o representar� o real estado, desde que 
	 * a conex�o fecha assim que o inputStream ou outputStream � fechado atrav�s do m�todo close()
	 * @param fileName caminho do arquivo
	 * @return InputStream com o stream pronto para ser usado
	 * @throws IOException se algum problema ocorrer ao criar o InputStream
	 */
	public InputStream getInputStream(String fileName) throws IOException{
		connect(fileName);
		return this.connection.getInputStream();
	}
	
	public DownloadFile download(String fileName, String targetFolderPath) throws IOException{
		if(this.connected){
			if(!this.fileName.equals(fileName)){
				throw new IllegalArgumentException("fileName: '" + fileName + "' n�o � o mesmo que this.fileName: '" + this.fileName + "' utilize o m�todo connect para atualizar o fileName.");
			}
		} else {
			connect(fileName);
		}
		saveFile(prepareTargetFolder(fileName, targetFolderPath));
		this.connected = false;
		return this;
	}

	public DownloadFile disconnect() throws IOException{
		if(this.connected){
			try {
				if(this.connection != null && this.connection.getInputStream() != null){
					this.connection.getInputStream().close();
				}
			} catch(SocketTimeoutException e){
				logger.error("Imposs�vel desconectar url: " + this.url);
			} catch(SocketException e){
				logger.error("Imposs�vel desconectar url: " + this.url);
			}
			this.connected = false;
		}
		return this;
	}
	
	/**
	 * Este m�todo deve ser chamado ap�s o m�todo connect para que funcione corretamente.
	 * exemplo:
	 * <pre>
	 * new DownloadFile(new SystemOutPrintOutputIntegerImp(), "file:///c:/temp")
	 *     .connect("temp.txt")
	 *     .download("d:\\temp");
	 * </pre>
	 * @param targetFolderPath
	 * @return A instancia de DownloadFile
	 * @throws IOException caso algum problema ocorra ao utilizar connection.getInputStream
	 */
	public DownloadFile download(String targetFolderPath) throws IOException{
		if(this.connected){
			saveFile(prepareTargetFolder(this.fileName, targetFolderPath));
		}
		return this;
	}
	
	private String prepareTargetFolder(String fileName, String targetFolderPath) {
		String targetFilePath = normalizeFilePath(targetFolderPath + File.separatorChar + fileName);
		File targetFolder = new File(targetFilePath.substring(0,targetFilePath.lastIndexOf(File.separatorChar)));
		if(!targetFolder.exists()){
			targetFolder.mkdirs();
		}
		this.downloadedFile = new File(targetFilePath);
		return targetFilePath;
	}

	private String normalizeFilePath(String targetFilePath) {
		return targetFilePath.replace("\\", Character.toString(File.separatorChar)).replace("/", Character.toString(File.separatorChar));
	}

	private void saveFile(String targetFilePath) throws FileNotFoundException, IOException {
		FileOutputStream fos = new FileOutputStream(new File(targetFilePath));
		BufferedInputStream bufferedInputStream = new BufferedInputStream(this.connection.getInputStream());
		byte[] buffer = new byte[4096];
		Integer bytes;
		while((bytes = bufferedInputStream.read(buffer)) != -1){
			fos.write(buffer, 0, bytes);
			this.output.output(bytes);
		}
		fos.close();
		disconnect();
	}
	
	public Integer getFileLength() {
		return fileLength;
	}

	public Boolean getConnected() {
		return connected;
	}

	public File getDownloadedFile() {
		return downloadedFile;
	}

	public String getRemoteTargetFolder() {
		return remoteTargetFolder;
	}
	
	public URL getUrl() {
		return url;
	}
}
