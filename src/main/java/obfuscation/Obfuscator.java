package obfuscation;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Class that gather all the elements in order to obfuscate properly a project 
 * located in a specified folder. 
 * 
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import parsing.ParsingContext;
import parsing.ParsingContextHandler;
import parsing.ParsingContextType;
import parsing.PhpParser;
import variable.GlobalContext;
import variable.Variable;
import variable.VariableAppearance;
import variable.VariableUtils;

import common.FileUtils;

public class Obfuscator  {
	private String sourceDir = null;
	private String destinationDir = null;
	private List<File> projectFiles = null;
	private List<Variable> projectVariableList = null;
	private List<FileObfuscationStructure> projectFileObfuscationStructureList = null;

	private PhpParser parser;
	private ParsingContextHandler handler;
	
	private boolean copyOnlySource = false;
	private boolean variableFinder = true;
	private boolean systemVariableFinder = true;
	private boolean anchorFinder = true;
	private boolean inputFinder = true;
	private boolean javaScriptFinder = true;
	private boolean functionFinder = true;
	private boolean commentRemover = true;
	private boolean whiteSpacesRemover = true;
	
	public Obfuscator() {
		Thread.currentThread().setName("Treatment Thread");
		Thread.currentThread().setPriority(Thread.NORM_PRIORITY);
		handler = new ParsingContextHandler();
		handler.buildStringAnalyzers();
		parser = new PhpParser();
		setVariableFinder(variableFinder);	
		setSystemVariableFinder(systemVariableFinder);	
		setAnchorFinder(anchorFinder);	
		setInputFinder(inputFinder);	
		setJavaScriptFinder(javaScriptFinder);	
		setFunctionFinder(functionFinder);	
	}
	
	public Obfuscator(String sourceDir) {
		this();
		setSourceDir(sourceDir);
	}
	
	public void reset () {
		sourceDir = null;
		destinationDir = null;
		projectFileObfuscationStructureList = null;
		projectVariableList = null;
		projectFiles = null;
	}
	
	public boolean isCopyOnlySource() {
		return copyOnlySource;
	}

	public void setCopyOnlySource(boolean copyOnlySource) {
		this.copyOnlySource = copyOnlySource;
	}

	public boolean isCommentRemover() {
		return commentRemover;
	}

	public void setCommentRemover(boolean commentRemover) {
		this.commentRemover = commentRemover;
	}

	public boolean isWhiteSpacesRemover() {
		return whiteSpacesRemover;
	}

	public void setWhiteSpacesRemover(boolean whiteSpacesRemover) {
		this.whiteSpacesRemover = whiteSpacesRemover;
	}

	public boolean isVariableFinder() {
		return variableFinder;
	}

	public void setVariableFinder(boolean variableFinder) {
		this.variableFinder = variableFinder;
		handler.setAnalyzerActive(GlobalContext.Variable, variableFinder);
	}

	public boolean isSystemVariableFinder() {
		return systemVariableFinder;
	}

	public void setSystemVariableFinder(boolean systemVariableFinder) {
		this.systemVariableFinder = systemVariableFinder;
		handler.setAnalyzerActive(GlobalContext.SystemVariable, systemVariableFinder);
	}

	public boolean isFunctionFinder() {
		return functionFinder;
	}

	public void setFunctionFinder(boolean functionFinder) {
		this.functionFinder = functionFinder;
		handler.setAnalyzerActive(GlobalContext.Function, functionFinder);
	}

	public boolean isAnchorFinder() {
		return anchorFinder;
	}

	public void setAnchorFinder(boolean anchorFinder) {
		this.anchorFinder = anchorFinder;
		handler.setAnalyzerActive(GlobalContext.Anchor, anchorFinder);
	}

	public boolean isInputFinder() {
		return inputFinder;
	}

	public void setInputFinder(boolean inputFinder) {
		this.inputFinder = inputFinder;
		handler.setAnalyzerActive(GlobalContext.Input, inputFinder);
	}

	public boolean isJavaScriptFinder() {
		return javaScriptFinder;
	}

	public void setJavaScriptFinder(boolean javaScriptFinder) {
		this.javaScriptFinder = javaScriptFinder;
		handler.setAnalyzerActive(GlobalContext.JavaScript, javaScriptFinder);
	}

	public void setDestinationDir(String projectDestinationDir) {
		this.destinationDir = projectDestinationDir;
	}
	
	public String getDestinationDir() {
		return destinationDir;
	}
	
	public void setSourceDir (String sourceDir) {
		this.sourceDir = sourceDir;
	}
	
	public List<Variable> getProjectVariableList() {
		return projectVariableList;
	}

	public void setProjectVariableList(List<Variable> variableList) {
		this.projectVariableList = variableList;		
	}
	
	public List<File> getProjectFiles() {
		return projectFiles;
	}
	
	public int getObfuscationFileStructureNumber () {
		if (projectFileObfuscationStructureList != null) {
			return projectFileObfuscationStructureList.size();
		}
		return -1;
	}
	
	public int getFileNumber () {
		if (projectFiles != null) {
			return projectFiles.size();			
		}
		return -1;
	}
	
	public int getVariableAppearanceNumber () {
		int result = 0;
		for (FileObfuscationStructure fileObfuscationStructure : projectFileObfuscationStructureList) {
			result += fileObfuscationStructure.getFileVariablesAppearances().size();			
		}
		return result;
	}
	
	public void getFileFromSourceDir () {
		projectFiles = FileUtils.getFilesFromDir(sourceDir, FileUtils.EXCLUSION_FILE_PATTERN );			
		projectFileObfuscationStructureList = null;
		projectVariableList = null;
	}

	public void parseAndGetVariables () {
		projectFileObfuscationStructureList = generateProjectFileObfuscationList();
		projectVariableList = VariableUtils
		.getVariablesFromFileObfuscationStructure(projectFileObfuscationStructureList);	
		disableSpecialVariables();
	}
	
	public void obfuscateProject() {
		String obfuscatedContent;
		int totalSizeTransfert = 0;
		int numberOfFiles = 0;
		int fileSizeTransfert;
		
		createDestinationDirectories();
		
		for (FileObfuscationStructure structure : projectFileObfuscationStructureList) {		
			File obfuscatedFile = new File(destinationDir + "\\"
					+ structure.getFileName());
			obfuscatedContent = handler.replaceVariables(structure, commentRemover, whiteSpacesRemover);
			fileSizeTransfert = FileUtils.putFileContent(obfuscatedFile, obfuscatedContent);
			totalSizeTransfert += fileSizeTransfert;
			numberOfFiles++;
		}
		if (!copyOnlySource) {
			for (File file : projectFiles) {
				String fileExtension = FileUtils.getFilenameExtension(file.getName());
				if (!FileUtils.PHP_EXTENSION_TABLE.contains(fileExtension) && file.isFile()) {
					File newFile = new File(destinationDir
							+ "\\"
							+ file.getAbsolutePath().substring(
									sourceDir.length()));
					fileSizeTransfert = FileUtils.copyFile(file, newFile);
					numberOfFiles++;
					totalSizeTransfert += fileSizeTransfert;
				}
			}			
		}
	}
	
	public void unobfuscateProject() {
		String obfuscatedContent;
		int totalSizeTransfert = 0;
		int numberOfFiles = 0;
		int fileSizeTransfert;
		
		createDestinationDirectories();
		List<File> codeFiles = new ArrayList<File>();
		
		for (File file : projectFiles) {
			String fileExtension = FileUtils.getFilenameExtension(file.getName());
			if (FileUtils.PHP_EXTENSION_TABLE.contains(fileExtension) ||
				fileExtension.equals(FileUtils.JAVA_FILE_EXTENSION)) {
				codeFiles.add(file);
			}
		}
				
		for (FileObfuscationStructure structure : projectFileObfuscationStructureList) {		
			File obfuscatedFile = new File(destinationDir + "\\"
					+ structure.getFileName());
			obfuscatedContent = handler.replaceVariables(structure, commentRemover, whiteSpacesRemover);
			fileSizeTransfert = FileUtils.putFileContent(obfuscatedFile, obfuscatedContent);
			totalSizeTransfert += fileSizeTransfert;
			numberOfFiles++;
		}
		
		if (!copyOnlySource) {
			for (File file : projectFiles) {
				String fileExtension = FileUtils.getFilenameExtension(file.getName());
				if (!FileUtils.PHP_EXTENSION_TABLE.contains(fileExtension) && file.isFile()) {
					File newFile = new File(destinationDir
							+ "\\"
							+ file.getAbsolutePath().substring(
									sourceDir.length()));
					fileSizeTransfert = FileUtils.copyFile(file, newFile);
					numberOfFiles++;
					totalSizeTransfert += fileSizeTransfert;
				}
			}			
		}
	}

	public void createDestinationDirectories() {
		File projectObfuscatedDir = new File(destinationDir);
		projectObfuscatedDir.mkdir();
		for (File directory : projectFiles) {
			if (directory.isDirectory()) {
				String n = destinationDir + "/" + directory.getAbsolutePath().substring(sourceDir.length());
				File newDirectory = new File(n);
				newDirectory.mkdir();
			}
		}
	}
	
	private List<FileObfuscationStructure> generateProjectFileObfuscationList () {
		List<FileObfuscationStructure> result = new ArrayList<FileObfuscationStructure>();

		for (File file : projectFiles) {
			if (file.isFile()) {
				String fileExtension = FileUtils.getFilenameExtension(file.getName());

				if (FileUtils.PHP_EXTENSION_TABLE.contains(fileExtension)) {
					FileObfuscationStructure structure = new FileObfuscationStructure();
					String fileContent = FileUtils.getFileContent(file);

					List<ParsingContext> fileContexts = new ArrayList<ParsingContext>();
					if (!fileExtension.equals(FileUtils.JAVA_FILE_EXTENSION)) {
						fileContexts = parser.parsePhpContent(fileContent);						
					} else {
						ParsingContext javaScriptParsingContext = 
							new ParsingContext(ParsingContextType.JavaScript, 0, fileContent.length(), fileContent);
						fileContexts.add(javaScriptParsingContext);
					}

					String relativeFileName = file.getAbsolutePath();
					relativeFileName = relativeFileName
							.substring(sourceDir.length());
					structure.setFileName(relativeFileName);
					structure.setFileContent(fileContent);
					structure.setFileContexts(fileContexts);

					List<VariableAppearance> fileVariablesAppearances = handler
							.getVariableAppearances(structure, true);
					/**
					 * It's very important to sort the Appearances patterns
					 * frome the bigger to the smaller. In this order, no
					 * replacement confusion can be done.
					 * 
					 * <b>Example which can create problem:</b> 
					 * 
					 * $i = 0; 
					 * $in = 100; 
					 * $index = 21;
					 * 
					 * The $i pattern must be replaced in last.
					 * 
					 */
					Collections.sort(fileVariablesAppearances);
					structure.addFileVariablesAppearances(fileVariablesAppearances);
					result.add(structure);
				}
			}
		}
		if (handler.mustProceedSecondScan()) {
			for (FileObfuscationStructure structure : result) {
				structure.addFileVariablesAppearances(handler.getVariableAppearances(structure, false));
			}
		}
		return result;
	}

	private void disableSpecialVariables () {
		for (Variable variable : projectVariableList) {
			if (VariableUtils.mustBeSetToFalse(variable)) {
				variable.setObfuscate(false);
			}
		}
	}
}
