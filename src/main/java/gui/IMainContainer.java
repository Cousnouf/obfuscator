package gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This interface is not that useful but provides the possibility to implement 
 * another kind of main panel. 
 * 
 */

import java.awt.event.ActionListener;

public interface IMainContainer {

	public abstract void setTexts();
	
	public abstract ActionListener buildGetVariablesActionListener();

	public abstract ActionListener buildExitActionListener();

	public abstract ActionListener buildHomeActionListener();

	public abstract ActionListener buildSaveActionListener();

	public abstract ActionListener buildSettingsActionListener();

	public abstract ActionListener buildProceedObfuscationActionListener();

	public abstract ActionListener buildCreateDefaultDesinationDirectoryActionListener();

	public abstract ActionListener buildSourceDirActionListener();

	public abstract ActionListener buildDestDirActionListener();

	public abstract ActionListener buildHelpActionListener();

}