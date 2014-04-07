package common;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class privdes usefull methods to handle files and contents. Ther is 
 * some application specific methods added here: saveVariableList and 
 * loadVariableList 
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import variable.Variable;

public class FileUtils {
	public static final String JAVA_FILE_EXTENSION = "js";
	public static final List<String> PHP_EXTENSION_TABLE = Arrays.asList("php", "php3", "phtml", JAVA_FILE_EXTENSION);
	public static final List<String> EXCLUSION_FILE_PATTERN = Arrays.asList("CVS", ".db");
	private static final String DATA_SEPARATOR = ";";
	private static final String VARIABLE_SEPARATOR = "\n";

	public static List<File> getFilesFromDir (String directory, List<String> exclusionPattern) {
		if (directory == null || directory.equals("")) {
			return null;
		}
		File directoryToScan = new File(directory);
		File[] dirList = directoryToScan.listFiles();
		List<File> fixedSizeResult = Arrays.asList(dirList);
		List<File> result = new ArrayList<File>(fixedSizeResult);
		for (File potentialDir : dirList) {
			if (potentialDir.isDirectory()) {
				result.addAll(getFilesFromDir(potentialDir.getAbsolutePath(), exclusionPattern));
			}
		}
		List<Integer> fileToRemove = new ArrayList<Integer>();
		for (int i = 0; i < result.size(); i++) {
			for (int j = 0; j < exclusionPattern.size(); j++) {
				if (result.get(i).getAbsolutePath().indexOf(exclusionPattern.get(j)) > 0) {
					fileToRemove.add(i);
					j = exclusionPattern.size();
				}
			}
		}
		for (int i = fileToRemove.size() - 1; i >= 0; i--) {
			result.remove(fileToRemove.get(i).intValue());			
		}
		return result;
	}
	
	public static String getFileContent(File file) {
		try {
			FileInputStream fis = new FileInputStream(file);
			int byteInt = fis.read();
			StringBuffer content = new StringBuffer();
			while (byteInt >= 0) {
				content.append((char)byteInt);
				byteInt = fis.read();
			}
			fis.close();
			return content.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}	
	
	public static int putFileContent(File file, String content) {
		try {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}			
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(content.getBytes());
			fos.flush();
			fos.close();
			return content.length();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public static int copyFile(File source, File destination) {
		try {
			FileChannel inChannel = new FileInputStream(source).getChannel();
			FileChannel outChannel = new FileOutputStream(destination).getChannel();
			long transferSize = inChannel.size();
			inChannel.transferTo(0, transferSize, outChannel);
			inChannel.close();
			outChannel.close();
			return (int)transferSize;
		} catch (IOException e) {
			System.out.println("Problem while copying " + source.getPath());
			return -1;
		}
	}
	
	public static String getFilenameExtension(String name) {
		if (name.indexOf('.') > -1) {
			return name.substring(name.lastIndexOf('.') + 1);
		}
		return "";
	}
	
	public static int saveVariableList(String fileName, List<Variable> projectVariableList) {
		String fileContent = "";
		for (Variable variable : projectVariableList) {
			String strVariable = variable.getName() + DATA_SEPARATOR
					+ variable.getReplacementName() + DATA_SEPARATOR
					+ variable.isObfuscable() + VARIABLE_SEPARATOR;
			fileContent = fileContent.concat(strVariable);
		}
		return FileUtils.putFileContent(new File(fileName), fileContent);
	}
	
	public static List<Variable> loadVariableList (String fileName) {
		List<Variable> result = new ArrayList<Variable>();
		String fileContent = getFileContent(new File(fileName));
		StringTokenizer variableTokenizer = new StringTokenizer(fileContent, VARIABLE_SEPARATOR);
		while (variableTokenizer.hasMoreTokens()) {
			String stringVariable = variableTokenizer.nextToken();
			StringTokenizer paramsTokenizer = new StringTokenizer(stringVariable, DATA_SEPARATOR);
			Variable variable = new Variable(paramsTokenizer.nextToken());
			variable.setReplacementName(paramsTokenizer.nextToken());
			variable.setObfuscate(Boolean.parseBoolean(paramsTokenizer.nextToken()));
			result.add(variable);
		}
		return result;
	}
	
	public static String getFileContent(URL url) {
		try {
			InputStream inputStream = url.openStream();
			int byteInt = inputStream.read();
			StringBuffer content = new StringBuffer();
			while (byteInt >= 0) {
				content.append((char)byteInt);
				byteInt = inputStream.read();
			}
			inputStream.close();
			return content.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
