package variable;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Class that contains information about a Variable like:
 * -Its Appearances
 * -Its name
 * -Its replacement name
 * -Whether the Obfuscator should obfuscate it or not
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Variable implements Comparable<Variable> {
	private boolean obfuscate = true;
	private String name;
	private List<VariableAppearance> appearances = new ArrayList<VariableAppearance>();
	private String replacementName;
	
	private final String RANDOM_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public Variable(String name) {
		setName(name);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		replacementName = getRandomString(10) + ((Long)System.nanoTime()).toString();
	}
	
	public void addAppearance(VariableAppearance appearance) {
		if (!appearances.contains(appearance)) {
			appearances.add(appearance);			
		}
	}
	
	public List<VariableAppearance> getAppearances() {
		return appearances;
	}
	
	public String getReplacementName() {
		return replacementName;
	}
	
	public void setReplacementName(String replacementName) {
		this.replacementName = replacementName;
	}
	
	public boolean isObfuscable() {
		return obfuscate;
	}
	public void setObfuscate(boolean obfuscate) {
		this.obfuscate = obfuscate;
	}
	
	private String getRandomString(int characterNumber) {
		Random random = new Random();
		//random.setSeed(characterNumber);
		random.setSeed(random.nextLong());
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < characterNumber; i++) {
			result.append(RANDOM_STRING.charAt(random.nextInt(RANDOM_STRING.length())));
		}
		return result.toString();
	}
	
	public int compareTo(Variable o) {
		return name.compareTo(o.getName());
	}
	
	@Override
	public boolean equals(Object obj) {
		Variable variable = (Variable)obj;
		return name.equals(variable.getName());
	}
}
