package parsing;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This interface contains constants and defines Php contexts: 
 * -RegEx definition for each Php context.
 * -Variables that must not be obfuscated
 * -Alternative RegEx (prefixes or suffixes)
 * 
 */

import java.util.Arrays;
import java.util.List;

import analyzer.PatternGroupStructure;

public interface PhpParserConstant {
	// To update this list, please see:
	// http://www.php.net/manual/en/reserved.variables.php
	List<String> PREDEFINED_VARIABLES = Arrays.asList(
			"this", "userfile", "userfile_name", "userfile_size", 
			"php_errormsg", "http_response_header", "argc", "argv",
			"load"
			);
	
	PatternGroupStructure ANCHOR_STRUCTURE = 
		new PatternGroupStructure(1, "[?|&](\\w+)[=]");
	List<ParsingContextType> ANCHOR_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.Text, 
			ParsingContextType.PhpText, 
			ParsingContextType.PhpDynamicText);

	PatternGroupStructure INPUT_STRUCTURE = 
		new PatternGroupStructure(3, 6, "(" + "input|textarea|select" + "){1}(.)+((" + "name=" + ")(\")?(\\w+))");
	List<ParsingContextType> INPUT_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.Text, 
			ParsingContextType.PhpText, 
			ParsingContextType.PhpDynamicText);
	
	PatternGroupStructure FUNCTION_STRUCTURE = 
		new PatternGroupStructure(3, "(?i)(" + "function" + "{1})(\\s+)(\\w+)(\\s*)(\\({1})");
	List<ParsingContextType> FUNCTION_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.Php);
	String FUNCTION_ALT_PREFIX = "";
	String FUNCTION_ALT_SUFFIX = "(\\s*)\\(";
	String FUNCTION_ALT_PREFIX2 = "->(\\s*)";
	String FUNCTION_ALT_SUFFIX2 = "(\\s*)\\(";
	String FUNCTION_ALT_PREFIX3 = "::(\\s*)";
	String FUNCTION_ALT_SUFFIX3 = "(\\s*)\\(";

	PatternGroupStructure JAVASCRIPT_STRUCTURE1 = 
		new PatternGroupStructure(2, "(\\W*)(\\w+)(\\[(\\w+)\\])?(\\.{1})(value{1}|checked{1})");
	PatternGroupStructure JAVASCRIPT_STRUCTURE2 = 
		new PatternGroupStructure(2, "(?i)(getElementsByName\\(\\W?){1}(\\w+)\\W?\\)");
	List<ParsingContextType> JAVASCRIPT_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.JavaScript, 
			ParsingContextType.Text, 
			ParsingContextType.PhpText, 
			ParsingContextType.PhpDynamicText);
	
	PatternGroupStructure JAVASCRIPT_FUNCTION_STRUCTURE = 
		new PatternGroupStructure(3, "(?i)(" + "function" + "{1})(\\s+)(\\w+)(\\s*)(\\({1})");
	List<ParsingContextType> JAVASCRIPT_FUNCTION_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.JavaScript, 
			ParsingContextType.Text, 
			ParsingContextType.PhpText, 
			ParsingContextType.PhpDynamicText);
	String JAVASCRIPT_FUNCTION_ALT_PREFIX = "";
	String JAVASCRIPT_FUNCTION_ALT_SUFFIX = "(\\s*)\\(";
	
	PatternGroupStructure SYSTEM_VARIABLE_STRUCTURE = 
		new PatternGroupStructure(3, "(\\${1})(_{1}\\w+)\\[{1}'?(\\w+)'?\\]{1}");
	List<ParsingContextType> SYSTEM_VARIABLE_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.Php);
	
	PatternGroupStructure SPECIAL_VARIABLE_STRUCTURE = 
		new PatternGroupStructure(4, "(session_){1}(\\w)+\\({1}('|\")?(\\w+)('|\")?\\){1}");
	List<ParsingContextType> SPECIAL_VARIABLE_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.EntireFile);
	
	PatternGroupStructure VARIABLE_STRUCTURE = 
		new PatternGroupStructure(2, "(\\${1})([a-z0-9-]{1}(\\w)*)");
	PatternGroupStructure VARIABLE_STRUCTURE2 = 
		new PatternGroupStructure(3, "(->{1})(\\s*)([a-z0-9-]{1}(\\w*))(\\s*)[\\W&&[^\\(]]");
	List<ParsingContextType> VARIABLE_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.Php, 
			ParsingContextType.PhpDynamicText);
	
	PatternGroupStructure CLASS_STRUCTURE = 
		new PatternGroupStructure(3, "(?i)(class){1}(\\s+)(\\w+)(\\s*)(\\{|extends){1}");
	List<ParsingContextType> CLASS_PARSING_CONTEXT = Arrays.asList(
			ParsingContextType.Php);
	String CLASS_ALT_PREFIX = "new(\\s+)";
	String CLASS_ALT_SUFFIX = "";
	String CLASS_ALT_PREFIX2 = "";
	String CLASS_ALT_SUFFIX2 = "(\\s*)->";
	String CLASS_ALT_PREFIX3 = "";
	String CLASS_ALT_SUFFIX3 = "(\\s*)::";
	String CLASS_ALT_PREFIX4 = "(?i)extends(\\s+)";
	String CLASS_ALT_SUFFIX4 = "(\\s+)(\\{)";
}
