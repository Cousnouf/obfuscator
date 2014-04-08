package obfuscation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsing.ParsingContext;
import variable.VariableAppearance;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @See Obfuscator.java
 * @Purpose
 * This structure contains the content of a file, its name, a list of
 * ParsingContexts and a list of VariableAppearances.
 *
 */
public class FileObfuscationStructure {
	private String fileContent;
	private String fileName;
	private List<ParsingContext> fileContexts;
	private List<VariableAppearance> fileVariablesAppearances = new ArrayList<VariableAppearance>();
	
	public String getFileContent() {
		return fileContent;
	}
	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}
	public List<ParsingContext> getFileContexts() {
		return fileContexts;
	}
	public void setFileContexts(List<ParsingContext> fileContexts) {
		this.fileContexts = fileContexts;
	}

    /**
	 * @return the variable appearances for the file.
	 */
	public List<VariableAppearance> getFileVariablesAppearances() {
		return fileVariablesAppearances;
	}

    /**
	 * @param fileVariablesAppearances
	 */
	public void addFileVariablesAppearances(List<VariableAppearance> fileVariablesAppearances) {
		fileVariablesAppearances = setFileToAppearanceList(fileVariablesAppearances);
		for (VariableAppearance variableAppearance : fileVariablesAppearances) {
			if (!this.fileVariablesAppearances.contains(variableAppearance)){
				this.fileVariablesAppearances.add(variableAppearance);
			}
		}
		Collections.sort(this.fileVariablesAppearances);
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public List<VariableAppearance> setFileToAppearanceList (List<VariableAppearance> fileVariablesAppearances) {
		for (VariableAppearance appearance : fileVariablesAppearances) {
			appearance.setFilePath(fileName);
		}
		return fileVariablesAppearances;
	}
}
