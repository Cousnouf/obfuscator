package variable;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class provides some methods in order to do operations on Variable 
 * instances.
 * 
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import obfuscation.FileObfuscationStructure;
import parsing.PhpParserConstant;

public class VariableUtils {
	/*
	 * Create the Variable Object list, referencing only once each variable and indexing
	 * the appearences.
	 * Then, the Variable Object list is sorted from the longer variable name to the shorter
	 * in order to execute a correct string replacement in the files.
	 * */
	public static List<Variable> getVariablesFromFileObfuscationStructure (List<FileObfuscationStructure> fileInstances) {
		List<Variable> result = new ArrayList<Variable>();
		for (FileObfuscationStructure file : fileInstances) {
			for (VariableAppearance appearance : file.getFileVariablesAppearances()) {
				String name = appearance.getOutOfContextName();
				Variable variable = new Variable(name);	
				int index = existsIn(variable, result);
				if (index >= 0) {
					variable = result.get(index);
					variable.addAppearance(appearance);
				} else {
					variable.addAppearance(appearance);
					result.add(variable);
				}
				appearance.setVariableRelated(variable);
			}
		}
		Collections.sort(result);
		return result;
	}
	
	private static int existsIn(Variable variable, List<Variable> existingVariables) {
		int cpt = 0;
		for (Variable existing : existingVariables) {
			if (existing.equals(variable)) {
				return cpt;
			}
			cpt++;
		}
		return -1;
	}

	public static boolean mustBeSetToFalse(Variable variable) {
		 return 
			 nameIsOnlyUpperCase(variable) || 
			 PhpParserConstant.PREDEFINED_VARIABLES.contains(variable.getName());
	}
	
	public static boolean nameIsOnlyUpperCase(Variable variable) {
		String name = variable.getName();
		for (int i = 0; i < name.length(); i++) {
			if (!Character.isUpperCase(name.charAt(i)) && name.charAt(i) != '_') {
				return false;				
			}
		}
		return true;
	}
}
