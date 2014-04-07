package analyzer.impl;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class contains the methods in order to get appearances and to replace
 * variables according to a specific parsing context. In this application,
 * they are instanciated in the class ParsingContextHandler, 
 * method buildStringAnalyzers 
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import parsing.ParsingContext;
import parsing.ParsingContextType;
import variable.GlobalContext;
import variable.VariableAppearance;
import analyzer.IStringAnalyser;
import analyzer.PatternGroupStructure;

public class StringAnalyser implements IStringAnalyser{
	private List<ParsingContextType> parsingContexts = new ArrayList<ParsingContextType>();
	private List<PatternGroupStructure> patterns = new ArrayList<PatternGroupStructure>();
	private List<String> detectedVariableNames;
	private List<String> altPrefixRegexps = new ArrayList<String>();
	private List<String> altSuffixRegexps = new ArrayList<String>();
	
	private GlobalContext globalContext;
	
	private boolean outOfContextAnalysis = false;
	private boolean twoScansAnalyzer = false;
	private boolean active = true;

	public StringAnalyser() {
	}
	
	public StringAnalyser(GlobalContext context) {
		super();
		this.globalContext = context;
	}
	
	public boolean isOutOfContextAnalysis() {
		return outOfContextAnalysis;
	}

	public void setOutOfContextAnalysis(boolean outOfContextAnalysis) {
		this.outOfContextAnalysis = outOfContextAnalysis;
	}

	public boolean isTwoScansAnalyzer() {
		return twoScansAnalyzer;
	}

	public void setTwoScansAnalyzer(boolean twoScansAnalyzer) {
		this.twoScansAnalyzer = twoScansAnalyzer;
		if (twoScansAnalyzer) {
			detectedVariableNames = new ArrayList<String>();
		}
	}

	public GlobalContext getGlobalContext() {
		return globalContext;
	}

	public void setGlobalContext(GlobalContext context) {
		this.globalContext = context;
	}

	public boolean belongsToAnalyzer(VariableAppearance appearance) {
		return appearance.getGlobalContext().equals(globalContext);
	}
	
	public void addAltPrefixRegexp (String regexp) {
		altPrefixRegexps.add(regexp);
	}
	
	public void addAltSuffixRegexp (String regexp) {
		altSuffixRegexps.add(regexp);
	}

	public void replaceVariables(VariableAppearance appearance) {
			ParsingContext parsingContext = appearance.getParsingContext();
			parsingContext.setContent(replaceVariables(appearance, parsingContext.getContent()));
	}
	
	public String replaceVariables(VariableAppearance appearance, String content) {
		if (belongsToAnalyzer(appearance)) {
			String toReplace = appearance.getAppearancePattern();
			String replacementString = toReplace.replace(appearance
					.getOutOfContextName(), appearance.getVariableRelated()
					.getReplacementName());
            String inContextWrapperReplacement = content.replace(
                toReplace, replacementString);
			
			return inContextWrapperReplacement;
		} else {
			return content;			
		}
	}
	
	public List<VariableAppearance> getAppearances(ParsingContext context) {
		List<VariableAppearance> result = new ArrayList<VariableAppearance>();
		for (PatternGroupStructure pattern : patterns) {
			Matcher matcher = pattern.getPattern().matcher(context.getContent());
			while (matcher.find()) {
				String variableName = matcher.group(pattern.getNameGroup());
				String appearancePattern = matcher.group(pattern.getAppearanceGroup());
				result.add(new VariableAppearance(appearancePattern, variableName, globalContext, context, ""));
				if (twoScansAnalyzer && !detectedVariableNames.contains(variableName)) {
					detectedVariableNames.add(variableName);
				}
			}			
		}
		return result;
	}
	
	public List<VariableAppearance> getAltAppearances(ParsingContext context) {
		List<VariableAppearance> result = new ArrayList<VariableAppearance>();
		for (String variableName : detectedVariableNames) {
			for (int i = 0; i < altPrefixRegexps.size(); i++) {
				String completeRegexp = altPrefixRegexps.get(i) + variableName + altSuffixRegexps.get(i);
				Matcher matcher = Pattern.compile(completeRegexp).matcher(context.getContent());
				while (matcher.find()) {
					result.add(new VariableAppearance(matcher.group(), variableName, globalContext, context, ""));
				}				
			}
		}
		return result;
	}

	public boolean supportParsingContext(ParsingContext parsingContext) {
		return parsingContexts.contains(parsingContext.getContext());
	}
	
	public void setParsingContexts(List<ParsingContextType> parsingContexts) {
		this.parsingContexts = parsingContexts;
	}
	
	public void addParsingContext(ParsingContextType context) {
		this.parsingContexts.add(context);
	}

	public void setPatterns(List<PatternGroupStructure> patterns) {
		this.patterns = patterns;
	}

	public void addPatternGroupStrucure(PatternGroupStructure structure) {
		patterns.add(structure);
	}
	
	public List<VariableAppearance> getAppearancesFromPattern(Pattern pattern,
			ParsingContext parsingContext) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean arg) {
		this.active  = arg;
	}
}
