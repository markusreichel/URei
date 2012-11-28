package org.reichel.jar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Classe utilitária para lidar com arquivos jar.
 * @author Markus Reichel
 * <pre>
 * History: 28/11/2012 - Markus Reichel - Criação da classe
 * </pre>
 */
public class JarUtils {

	/**
	 * Extrai todos os arquivos de um arquivo jar para um diretório.
	 * Exemplo de utilização:
	 * <pre>
	 *  try {
     *    JarUtils.extractFiles("target/URei.jar", "target/extract");
	 *  } catch (IOException e) {
	 *    System.out.println(e.getMessage());
	 *  }
	 * </pre>
	 * @param jarFilePath caminho do arquivo jar a ser extraído ex: config\ambienteconfig.jar
	 * @param targetFolder caminho do diretório raiz onde os arquivos serão extraídos ex: config\extract
	 * @throws IOException quando houver problemas ao ler e/ou escrever arquivos
	 */
	public static void extractFiles(String jarFilePath, String targetFolder) throws IOException {
		if(jarFilePath == null || "".equals(jarFilePath)){
			throw new IllegalArgumentException("Parametro jarFilePath não pode ser vazio ou nulo.");
		}
		if(targetFolder == null){
			throw new IllegalArgumentException("Parametro jarFilePath não pode ser nulo.");
		}

		if(!targetFolder.endsWith(Character.toString(File.separatorChar))){
			targetFolder += File.separatorChar;
		}
		
		File targetFile = null;
		JarFile jarFile = new JarFile(new File(jarFilePath));
		Enumeration<JarEntry> jarEntries = jarFile.entries();
		while(jarEntries.hasMoreElements()){
			JarEntry jarEntry = jarEntries.nextElement();
			String name = jarEntry.getName();
			targetFile = new File(targetFolder + name);
			createDirectories(targetFolder, targetFile, jarEntry, name);
			doExtractFile(jarFile, jarEntry, targetFile);
		}
	}

	private static void doExtractFile(JarFile jarFile, JarEntry jarEntry, File targetFile) throws IOException{
		InputStream is = jarFile.getInputStream(jarEntry);
		FileOutputStream fos = new FileOutputStream(targetFile);
		byte[] buffer = new byte[4096];
		int bytesRead = 0;
        while ((bytesRead = is.read(buffer)) != -1) {
        	 fos.write(buffer, 0, bytesRead);
        }
        is.close();
        fos.flush();
        fos.close();
	}
	
	private static void createDirectories(String targetFolder, File targetFile, JarEntry jarEntry, String name) {
		if(jarEntry.isDirectory() && !targetFile.exists()){
			if(!targetFile.mkdirs()){
				throw new UnsupportedOperationException("Não foi possível criar diretórios:'" + targetFile.getAbsolutePath() + "'");
			}
		} else {
			File file = new File(targetFolder + name.substring(0,name.lastIndexOf("/")));
			if(!file.exists()){
				if(!file.mkdirs()){
					throw new UnsupportedOperationException("Não foi possível criar diretórios:'" + file.getAbsolutePath() + "'");
				}
			}
		}
	}
}
