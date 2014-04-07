package analyzer;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This interface is implemented by every string analyzer in order to detect
 * its appropriate type of variable.
 * 
 */

import java.util.List;
import java.util.regex.Pattern;

import parsing.ParsingContext;

import variable.GlobalContext;
import variable.VariableAppearance;

public interface IStringAnalyser {
	public void addPatternGroupStrucure(PatternGroupStructure structure);
	
	public boolean belongsToAnalyzer(VariableAppearance appearance);
	
	public boolean supportParsingContext(ParsingContext parsingContext);
	
	public List<VariableAppearance> getAppearances(ParsingContext parsingContext);
	
	public List<VariableAppearance> getAltAppearances(ParsingContext parsingContext);
	
	public List<VariableAppearance> getAppearancesFromPattern(Pattern pattern, ParsingContext parsingContext);
	
	public void replaceVariables(VariableAppearance appearance);
	
	public boolean isOutOfContextAnalysis();
	
	public boolean isTwoScansAnalyzer();
	
	public GlobalContext getGlobalContext();
	
	public void setActive(boolean arg);
	
	public boolean isActive();

	public String replaceVariables(VariableAppearance appearance, String content);
}
