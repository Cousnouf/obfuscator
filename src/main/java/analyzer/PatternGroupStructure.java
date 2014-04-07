package analyzer;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class contains the pattern to identify a variable type and also 
 * has the RegEx group number in order to extract the variable name. It has
 * the RegEx group number to extract the appearance pattern too. Usually, this
 * one is set to 0 in order to assign the whole string to the appearance pattern.
 * 
 */

import java.util.regex.Pattern;

public class PatternGroupStructure {
	private Pattern pattern;
	private int appearanceGroup;
	private int nameGroup;
	
	public Pattern getPattern() {
		return pattern;
	}
	
	public PatternGroupStructure() {
		this(0, 0, "");
	}
	
	public PatternGroupStructure(int nameGroup, Pattern pattern) {
		this (0, nameGroup, pattern);
	}
	
	public PatternGroupStructure(int nameGroup, String regexp) {
		this(0, nameGroup, Pattern.compile(regexp));
	}
	
	public PatternGroupStructure(int appearanceGroup, int nameGroup, String regexp) {
		this(appearanceGroup, nameGroup, Pattern.compile(regexp));		
	}
	
	public PatternGroupStructure(int appearanceGroup, int nameGroup, Pattern pattern) {
		this.appearanceGroup = appearanceGroup;
		this.nameGroup = nameGroup;
		this.pattern = pattern;
	}
	
	public void setPattern(Pattern pattern) {
		this.pattern = pattern;
	}

	public void setPattern(String regexp) {
		this.pattern = Pattern.compile(regexp);
	}
		
	public int getAppearanceGroup() {
		return appearanceGroup;
	}

	public void setAppearanceGroup(int appearanceGroup) {
		this.appearanceGroup = appearanceGroup;
	}

	public int getNameGroup() {
		return nameGroup;
	}
	
	public void setNameGroup(int nameGroup) {
		this.nameGroup = nameGroup;
	}
}
