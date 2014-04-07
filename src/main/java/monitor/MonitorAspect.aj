package monitor;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Aspect that catches event in order to display the relevant data in the 
 * ProgressPanel. 
 * 
 */

import gui.ProgressPanel;

import java.io.File;

import obfuscation.Obfuscator;

import common.MessageUtils;

public aspect MonitorAspect {
	private static String VARIABLE_DETECTION = null;
	private static String PROJECT_OBFUSCATION = null;
	private static String FILE_STRUCTURE_GENERATION = null;
	private static String FUNCTION_DETECTION = null;
	private static String VARIABLE_TABLE_GENERATION = null;
	private static String DONE = null;
	private static String CREATING_DIRS = null;
	private static String FILE_OBFUSCATION = null;
	private static String FILE_CREATION = null;
	
	int obfuscationFileNumber;
	int fileNumber;
	Obfuscator obfuscator;
	ProgressPanel progressPanel;
	
	before(): execution(gui.MainFrame.new(..)) || execution(* gui.MainFrame.setTexts(..)) {
		setProgressTexts();
	}
	
	public static void setProgressTexts () {
		VARIABLE_DETECTION = MessageUtils.getMessage("MONITOR_GET_VAR");
		PROJECT_OBFUSCATION = MessageUtils.getMessage("MONITOR_OBF");
		FILE_STRUCTURE_GENERATION = MessageUtils.getMessage("MONITOR_FS_GEN");
		FUNCTION_DETECTION = MessageUtils.getMessage("MONITOR_FUNC_DET");
		VARIABLE_TABLE_GENERATION = MessageUtils.getMessage("MONITOR_VT_GEN");
		DONE = MessageUtils.getMessage("MONITOR_DONE");
		CREATING_DIRS = MessageUtils.getMessage("MONITOR_CREATING_DIR");
		FILE_OBFUSCATION = MessageUtils.getMessage("MONITOR_FILE_OBF");
		FILE_CREATION = MessageUtils.getMessage("MONITOR_FILE_CR");
	}

	pointcut obfuscatorCreation() : call(obfuscation.Obfuscator.new(..));
	pointcut mustProceedSecondScan() : call(* parsing.ParsingContextHandler.mustProceedSecondScan(..));
	pointcut generateList() : call(* obfuscation.Obfuscator.generateProjectFileObfuscationList(..));
	pointcut variableList() : call(* variable.VariableUtils.getVariablesFromFileObfuscationStructure(..));
	pointcut inObfuscator() : within(obfuscation.Obfuscator);

	Object around() : obfuscatorCreation () {
		this.obfuscator = (Obfuscator) proceed();
		return this.obfuscator;
	}

	Object around() : call(gui.ProgressPanel.new(..)) {
		Object object = proceed();
		this.progressPanel = (ProgressPanel) object;
		return this.progressPanel;
	}
	
	after(): execution(* obfuscation.Obfuscator.getFileFromSourceDir(..)){
		fileNumber = obfuscator.getFileNumber();
	}
	
	before(): variableList() {
		obfuscationFileNumber = obfuscator.getObfuscationFileStructureNumber();
	}

	// Monitoring for Populate List action.
	int step;
	int cptTotalAction;
	/**
	 *  projectFileObfuscationStructureList = generateProjectFileObfuscationStructure();
	 *  proceedFunctionDetection(projectFileObfuscationStructureList);
	 * */
	before() : generateList() {
		step = 1;
		cptTotalAction = fileNumber;
		progressPanel.resetValue();
		progressPanel.setStatusLabel(VARIABLE_DETECTION);
		progressPanel.setMaximumValue(cptTotalAction);
	}
	
	before() : call(* java.io.File.isFile(..)) && inObfuscator() {
		if (step == 1) {
			progressPanel.setStatusLabel(VARIABLE_DETECTION + FILE_STRUCTURE_GENERATION);
			progressPanel.incrementValue();			
		}
	}
	
	before() : mustProceedSecondScan() {
		step = 2;
		progressPanel.resetValue();
		progressPanel.setMaximumValue(fileNumber);
		progressPanel.setStatusLabel(VARIABLE_DETECTION + FUNCTION_DETECTION);
	}
	
	before() : execution(* obfuscation.FileObfuscationStructure.addFileVariablesAppearances(..)) {
		if (step == 2) {
			progressPanel.incrementValue();			
		}
	}
	/**
	 * 	projectVariableList = VariableManager
	 *	.getVariablesFromFileObfuscationStructures(projectFileObfuscationStructureList);	
	 *	setUpperCaseVariablesToFalse();	
	 * */
	
	before(): variableList() {
		if (step == 2) {
			cptTotalAction = obfuscator.getVariableAppearanceNumber();
			progressPanel.resetValue();
			progressPanel.setMaximumValue(cptTotalAction);
			progressPanel.setStatusLabel(VARIABLE_DETECTION + VARIABLE_TABLE_GENERATION);			
		}
	}
	
	before(): execution(variable.Variable.new(..)) {
		if (step == 2) {
			progressPanel.incrementValue();			
		}
	}
	
	after() : execution(* obfuscation.Obfuscator.disableSpecialVariables(..)) {
		if (step == 2) {
			progressPanel.setStatusLabel(VARIABLE_DETECTION + DONE);
			step = 0;			
		}
	}
	
	/**
	 * Obfuscation
	 * 
	 * */
	before(): execution(* obfuscation.Obfuscator.createDestinationDirectories(..)) {
		step = 3;
		progressPanel.setStatusLabel(PROJECT_OBFUSCATION);
		progressPanel.resetValue();
		if (obfuscator.isCopyOnlySource()) {
			progressPanel.setMaximumValue(obfuscationFileNumber);			
		} else {
			progressPanel.setMaximumValue(fileNumber);			
		}
	}
	
	before(): call(* java.io.File.mkdir(..)) && inObfuscator() {
		if (step == 3) {
			progressPanel.incrementValue();
			progressPanel.setStatusLabel(PROJECT_OBFUSCATION + CREATING_DIRS);			
		}
	}
	
	Object around(): call(* obfuscation.FileObfuscationStructure.getFileName(..)) && inObfuscator() {
		String fileName = (String)proceed();
		if (step == 3) {
			progressPanel.incrementValue();
			progressPanel.setStatusLabel(PROJECT_OBFUSCATION + FILE_OBFUSCATION + fileName);			
		}
		return (Object)fileName;		
	}
	
	before(): call(* common.FileUtils.copyFile(..)) && inObfuscator() {
		if (step == 3) {
			String fileName = ((File)thisJoinPoint.getArgs()[1]).getPath();
			progressPanel.incrementValue();
			progressPanel.setStatusLabel(PROJECT_OBFUSCATION + FILE_CREATION + fileName);			
		}
	}
	
	after(): execution(* obfuscation.Obfuscator.obfuscateProject(..)) {
		if (step == 3) {
			progressPanel.setStatusLabel(PROJECT_OBFUSCATION + DONE);			
		}
	}
}