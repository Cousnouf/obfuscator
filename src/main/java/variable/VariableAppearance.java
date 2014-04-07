package variable;
/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Class that contains information about a Variable like:
 * -How the variable appears in the file (appearancePattern)
 * -Global name (out of context name)
 * -The file path
 * -The global context (Php Context)
 * -The parsing context (Text context)
 * -The variable attached (redundance of the out of context name)
 * 
 */

import parsing.ParsingContext;

public class VariableAppearance implements Comparable<VariableAppearance> {
	private String appearancePattern;
	private String outOfContextName;
	private String filePath;
	private GlobalContext globalContext;
	private ParsingContext parsingContext;
	private Variable variableRelated;

	public Variable getVariableRelated() {
		return variableRelated;
	}

	public void setVariableRelated(Variable variableRelated) {
		this.variableRelated = variableRelated;
	}

	public VariableAppearance() {

	}

	public VariableAppearance(String appearancePattern,
			String outOfContextName, GlobalContext globalContext,
			ParsingContext parsingContext, String filePath) {
		this.appearancePattern = appearancePattern;
		this.outOfContextName = outOfContextName;
		this.globalContext = globalContext;
		this.parsingContext = parsingContext;
		this.filePath = filePath;
	}

	public String getAppearancePattern() {
		return appearancePattern;
	}

	public void setAppearancePattern(String appearance) {
		this.appearancePattern = appearance;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public GlobalContext getGlobalContext() {
		return globalContext;
	}

	public void setGlobalContext(GlobalContext context) {
		this.globalContext = context;
	}

	public ParsingContext getParsingContext() {
		return parsingContext;
	}

	public void setParsingContext(ParsingContext parsingContext) {
		this.parsingContext = parsingContext;
	}

	@Override
	public boolean equals(Object obj) {
		VariableAppearance appearance = (VariableAppearance) obj;
		return (appearancePattern.equals(appearance.getAppearancePattern())
				&& filePath.equals(appearance.getFilePath()) && parsingContext
				.equals(appearance.getParsingContext()));
	}

	public String getOutOfContextName() {
		return outOfContextName;
	}

	public void setOutOfContextName(String outOfContextName) {
		this.outOfContextName = outOfContextName;
	}

	public int compareTo(VariableAppearance variableAppearance) {
		return -1 * this.getAppearancePattern().compareTo(variableAppearance.getAppearancePattern());
	}
}
