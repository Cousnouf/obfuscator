package parsing;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class build the StringAnalyzers and is the interface between the main 
 * Obfuscator object and the "workers" StringAnalyzers. 
 * 
 */

import java.util.ArrayList;
import java.util.List;

import obfuscation.FileObfuscationStructure;
import variable.GlobalContext;
import variable.VariableAppearance;
import analyzer.IStringAnalyser;
import analyzer.impl.StringAnalyser;

public class ParsingContextHandler {
	private List<IStringAnalyser> stringAnalysers = new ArrayList<IStringAnalyser>();
	
	public void buildStringAnalyzers () {
		StringAnalyser analyzer;
		analyzer = new StringAnalyser(GlobalContext.Variable);
		analyzer.addPatternGroupStrucure(PhpParserConstant.VARIABLE_STRUCTURE);
		analyzer.addPatternGroupStrucure(PhpParserConstant.VARIABLE_STRUCTURE2);
		analyzer.setParsingContexts(PhpParserConstant.VARIABLE_PARSING_CONTEXT);
		stringAnalysers.add(analyzer);
		
		analyzer = new StringAnalyser(GlobalContext.SystemVariable);
		analyzer.addPatternGroupStrucure(PhpParserConstant.SYSTEM_VARIABLE_STRUCTURE);
		analyzer.setParsingContexts(PhpParserConstant.SYSTEM_VARIABLE_PARSING_CONTEXT);
		stringAnalysers.add(analyzer);
		
		analyzer = new StringAnalyser(GlobalContext.SpecialVariable);
		analyzer.addPatternGroupStrucure(PhpParserConstant.SPECIAL_VARIABLE_STRUCTURE);
		analyzer.setParsingContexts(PhpParserConstant.SPECIAL_VARIABLE_PARSING_CONTEXT);
		analyzer.setOutOfContextAnalysis(true);
		stringAnalysers.add(analyzer);
		
		analyzer = new StringAnalyser(GlobalContext.JavaScript);
		analyzer.addPatternGroupStrucure(PhpParserConstant.JAVASCRIPT_STRUCTURE1);
		analyzer.addPatternGroupStrucure(PhpParserConstant.JAVASCRIPT_STRUCTURE2);
		analyzer.setParsingContexts(PhpParserConstant.JAVASCRIPT_PARSING_CONTEXT);
		stringAnalysers.add(analyzer);
		
		analyzer = new StringAnalyser(GlobalContext.JavaScript);
		analyzer.addPatternGroupStrucure(PhpParserConstant.JAVASCRIPT_FUNCTION_STRUCTURE);
		analyzer.setParsingContexts(PhpParserConstant.JAVASCRIPT_FUNCTION_PARSING_CONTEXT);
		analyzer.addAltPrefixRegexp(PhpParserConstant.JAVASCRIPT_FUNCTION_ALT_PREFIX);
		analyzer.addAltSuffixRegexp(PhpParserConstant.JAVASCRIPT_FUNCTION_ALT_SUFFIX);
		analyzer.setTwoScansAnalyzer(true);
		stringAnalysers.add(analyzer);
		
		analyzer = new StringAnalyser(GlobalContext.Function);
		analyzer.addPatternGroupStrucure(PhpParserConstant.FUNCTION_STRUCTURE);
		analyzer.setParsingContexts(PhpParserConstant.FUNCTION_PARSING_CONTEXT);
		analyzer.addAltPrefixRegexp(PhpParserConstant.FUNCTION_ALT_PREFIX);
		analyzer.addAltSuffixRegexp(PhpParserConstant.FUNCTION_ALT_SUFFIX);
		analyzer.addAltPrefixRegexp(PhpParserConstant.FUNCTION_ALT_PREFIX2);
		analyzer.addAltSuffixRegexp(PhpParserConstant.FUNCTION_ALT_SUFFIX2);
		analyzer.addAltPrefixRegexp(PhpParserConstant.FUNCTION_ALT_PREFIX3);
		analyzer.addAltSuffixRegexp(PhpParserConstant.FUNCTION_ALT_SUFFIX3);
		analyzer.setTwoScansAnalyzer(true);
		stringAnalysers.add(analyzer);
		
		analyzer = new StringAnalyser(GlobalContext.Input);
		analyzer.addPatternGroupStrucure(PhpParserConstant.INPUT_STRUCTURE);
		analyzer.setParsingContexts(PhpParserConstant.INPUT_PARSING_CONTEXT);
		stringAnalysers.add(analyzer);
		
		analyzer = new StringAnalyser(GlobalContext.Anchor);
		analyzer.addPatternGroupStrucure(PhpParserConstant.ANCHOR_STRUCTURE);
		analyzer.setParsingContexts(PhpParserConstant.ANCHOR_PARSING_CONTEXT);
		stringAnalysers.add(analyzer);
		
		analyzer = new StringAnalyser(GlobalContext.Class);
		analyzer.addPatternGroupStrucure(PhpParserConstant.CLASS_STRUCTURE);
		analyzer.setParsingContexts(PhpParserConstant.CLASS_PARSING_CONTEXT);
		analyzer.addAltPrefixRegexp(PhpParserConstant.CLASS_ALT_PREFIX);
		analyzer.addAltSuffixRegexp(PhpParserConstant.CLASS_ALT_SUFFIX);
		analyzer.addAltPrefixRegexp(PhpParserConstant.CLASS_ALT_PREFIX2);
		analyzer.addAltSuffixRegexp(PhpParserConstant.CLASS_ALT_SUFFIX2);
		analyzer.addAltPrefixRegexp(PhpParserConstant.CLASS_ALT_PREFIX3);
		analyzer.addAltSuffixRegexp(PhpParserConstant.CLASS_ALT_SUFFIX3);
		analyzer.addAltPrefixRegexp(PhpParserConstant.CLASS_ALT_PREFIX4);
		analyzer.addAltSuffixRegexp(PhpParserConstant.CLASS_ALT_SUFFIX4);
		analyzer.setTwoScansAnalyzer(true);
		stringAnalysers.add(analyzer);
	}
	
	public String replaceVariables(FileObfuscationStructure structure,
			boolean commentRemover, boolean whiteSpacesRemover) {
		for (IStringAnalyser analyzer : stringAnalysers) {
			if (analyzer.isActive() && !analyzer.isOutOfContextAnalysis()) {
				for (VariableAppearance appearance : structure.getFileVariablesAppearances()) {
					if (appearance.getVariableRelated().isObfuscable()) {
						analyzer.replaceVariables(appearance);
					}
				}
			}
		}
		String fileContent = "";
		for (ParsingContext parsingContext : structure.getFileContexts()) {
			if (commentRemover && 
			(parsingContext.getContext().equals(ParsingContextType.Comment) || 
			parsingContext.getContext().equals(ParsingContextType.LineComment))) {
				fileContent = fileContent.concat("\n");
			} else {
				fileContent = fileContent.concat(parsingContext.getContent());
			}
		}
		for (IStringAnalyser analyzer : stringAnalysers) {
			if (analyzer.isOutOfContextAnalysis()) {
				for (VariableAppearance appearance : structure.getFileVariablesAppearances()) {
					if (appearance.getParsingContext().getContext() == ParsingContextType.EntireFile) {
						fileContent = analyzer.replaceVariables(appearance,	fileContent);
					}
				}
			}
		}
		if (whiteSpacesRemover) {
			fileContent = fileContent.replaceAll("(\\t|\\r|\\n|\\r\\n)", " ");
		}
		return fileContent;
	}
	
	public void addAnalyzer(IStringAnalyser analyzer) {
		stringAnalysers.add(analyzer);
	}

	public void removeAnalyzer(IStringAnalyser variableStringAnalyser) {
		stringAnalysers.remove(variableStringAnalyser);
	}
	
	
	public List<VariableAppearance> getVariableAppearances(
			FileObfuscationStructure structure, boolean firstScan) {
		List<VariableAppearance> result = new ArrayList<VariableAppearance>();
		for (IStringAnalyser analyzer : stringAnalysers) {
			if (firstScan || (!firstScan && analyzer.isTwoScansAnalyzer())) {
				if (analyzer.isOutOfContextAnalysis()) {
					String content = structure.getFileContent(); 
					ParsingContext entireFileContext =
						new ParsingContext(ParsingContextType.EntireFile, 0, content.length(), content);

					if (firstScan) {
						result.addAll(analyzer.getAppearances(entireFileContext));						
					} else {
						result.addAll(analyzer.getAltAppearances(entireFileContext));												
					}
				} else {
					for (ParsingContext context : structure.getFileContexts()) {
						if (analyzer.supportParsingContext(context)) {
							if (firstScan) {
								result.addAll(analyzer.getAppearances(context));						
							} else {
								result.addAll(analyzer.getAltAppearances(context));												
							}			
						}
					}
				}			
			}
		}
		return result;
	}
	
	public void setAnalyzerActive (GlobalContext context, boolean arg) {
		for (IStringAnalyser analyzer : stringAnalysers) {
			if (context == analyzer.getGlobalContext()) {
				analyzer.setActive(arg);
			}
		}
	}
	
	public boolean mustProceedSecondScan () {
		for (IStringAnalyser analyzer : stringAnalysers) {
			if (analyzer.isTwoScansAnalyzer()) {
				return true;
			}
		}
		return false;
	}
}
