package common;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class provides useful methods to handle files and contents. There is
 * some application specific methods added here: saveVariableList and 
 * loadVariableList 
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import variable.Variable;

public class McbcFileUtils {
    private static Logger logger = LoggerFactory.getLogger(McbcFileUtils.class.getName());

    private static final String DATA_SEPARATOR = ";";
    private static final String VARIABLE_SEPARATOR = "\n";

	public static List<File> getFilesFromDir (String directory, List<String> exclusionPattern) {
		if (directory == null || directory.equals("")) {
			return null;
		}
		File directoryToScan = new File(directory);
		File[] dirList = directoryToScan.listFiles();
		List<File> fixedSizeResult = Arrays.asList(dirList);
		List<File> result = new ArrayList<>(fixedSizeResult);
		for (File potentialDir : dirList) {
			if (potentialDir.isDirectory()) {
				result.addAll(getFilesFromDir(potentialDir.getAbsolutePath(), exclusionPattern));
			}
		}
		List<Integer> fileToRemove = new ArrayList<>();
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
			return FileUtils.readFileToString(file);
		} catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
			return null;
		} catch (IOException e) {
            logger.error(e.getMessage(), e);
			return null;
		}
	}

	public static int putFileContent(File file, String content)  {
        try {
            FileUtils.write(file, content);
            return (int) FileUtils.sizeOf(file);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return -1;
    }

	public static int copyFile(File source, File destination) {
        try {
            FileUtils.copyFile(source, destination);
			return (int) FileUtils.sizeOf(destination);
		} catch (IOException e) {
            logger.error("Problem while copying " + source.getPath(), e);
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
		return McbcFileUtils.putFileContent(new File(fileName), fileContent);
	}

	public static List<Variable> loadVariableList (String fileName) {
		List<Variable> result = new ArrayList<>();
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
            return FileUtils.readFileToString(new File(url.getFile()));
		} catch (IOException e) {
            logger.error(e.getMessage(), e);
			return null;
		}
	}
}
