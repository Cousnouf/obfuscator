package gui.dialog;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Panel that offers the possibility to change the settings of the obfuscator. 
 * 
 */

import gui.GuiUtils;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import obfuscation.Obfuscator;

import common.MessageUtils;
import common.gui.VPCheckBox;

public class SettingsDialog extends Dialog {
	private static final long serialVersionUID = 1L;
	
	private Obfuscator obfuscator;
	
	private JPanel generalPanel;
	private JComboBox cmbLanguages;
	private VPCheckBox chkCopyOnlySource;
	private VPCheckBox chkCommentRemover;
	private VPCheckBox chkWhiteSpaceRemover;
	
	private JPanel variablePanel;
	private VPCheckBox chkVariableFinder;
	private VPCheckBox chkSystemVariableFinder;
	private VPCheckBox chkAnchorFinder;
	private VPCheckBox chkInputFinder;
	private VPCheckBox chkJavaScriptFinder;
	
	private JPanel functionPanel;
	private VPCheckBox chkFunctionFinder;
	
	private int gbcAnchor = 21;//GridBagConstraints.WEST;
	private int gbcFill = 0;//GridBagConstraints.BOTH;
	private Insets insets = new Insets(0, 0, 0, 0);
	private GridBagLayout gridBagLayout = new GridBagLayout();
	
	public static void displaySettingsDialog(JFrame owner, Obfuscator obfuscator) {
		SettingsDialog settingsDialog = new SettingsDialog(owner, obfuscator);
		settingsDialog.displayDialog();
	}

	public SettingsDialog(JFrame owner, Obfuscator obfuscator) {
		super(owner, "SETTINGS_TITLE");
		this.obfuscator = obfuscator;
		defaultSize.setSize(350, 400);
	}

	protected void initGuiElements() {
		buildGeneralPanel();
		buildVariablePanel();
		buildFunctionPanel();
		addListeners();
		setTexts();
	}

	protected void placeGuiElements() {
		GuiUtils.addItem(panel, generalPanel, 0, 0, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(panel, variablePanel, 0, 1, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(panel, functionPanel, 0, 2, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(panel, btnOk, 0, 3, 1, 1, 0, 0, insets, GridBagConstraints.CENTER, GridBagConstraints.NONE);
	}

	private void addListeners() {
		cmbLanguages.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MessageUtils.setLocale(new Locale((String)cmbLanguages.getSelectedItem()));
				setTexts();
				owner.setTexts();
			}
		});
		chkCopyOnlySource.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setCopyOnlySource(((VPCheckBox)e.getSource()).isSelected());
			}
		});
		chkCommentRemover.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setCommentRemover(((VPCheckBox)e.getSource()).isSelected());
			}		
		});
		chkWhiteSpaceRemover.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setWhiteSpacesRemover(((VPCheckBox)e.getSource()).isSelected());
			}
		});
		chkVariableFinder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setVariableFinder(((VPCheckBox)e.getSource()).isSelected());
			}		
		});
		chkSystemVariableFinder.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setSystemVariableFinder(((VPCheckBox)e.getSource()).isSelected());
			}
		});
		chkAnchorFinder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setAnchorFinder(((VPCheckBox)e.getSource()).isSelected());
			}		
		});
		chkInputFinder.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setInputFinder(((VPCheckBox)e.getSource()).isSelected());
			}
		});
		chkJavaScriptFinder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setJavaScriptFinder(((VPCheckBox)e.getSource()).isSelected());
			}		
		});
		chkFunctionFinder.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				obfuscator.setFunctionFinder(((VPCheckBox)e.getSource()).isSelected());
			}
		});
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}			
		});
	}

	protected void setTexts() {
		this.setTitle("SETTINGS_TITLE");
		generalPanel.setBorder(BorderFactory.createTitledBorder(MessageUtils.getMessage("SETTINGS_GENERAL")));
		chkCopyOnlySource.setText("SETTINGS_SRC_DEST");
		chkCommentRemover.setText("SETTINGS_RM_COMM");
		chkWhiteSpaceRemover.setText("SETTINGS_RM_WS");	
		variablePanel.setBorder(BorderFactory.createTitledBorder(MessageUtils.getMessage("SETTINGS_VARIABLE")));
		chkVariableFinder.setText("SETTINGS_NORMAL_VAR");
		chkSystemVariableFinder.setText("SETTINGS_SYS_VAR");
		chkAnchorFinder.setText("SETTINGS_ANCHOR_VAR");
		chkInputFinder.setText("SETTINGS_INPUT_VAR");
		chkJavaScriptFinder.setText("SETTINGS_JAVAS_VAR");
		functionPanel.setBorder(BorderFactory.createTitledBorder(MessageUtils.getMessage("SETTINGS_FUNCTION")));
		chkFunctionFinder.setText("SETTINGS_DR_FUNCTION");	
	}

	private void buildGeneralPanel() {
		generalPanel = new JPanel(gridBagLayout);
		generalPanel.setBorder(BorderFactory.createTitledBorder(MessageUtils.getMessage("SETTINGS_GENERAL")));
		cmbLanguages = new JComboBox(MessageUtils.getAvailableLanguages().toArray());
		cmbLanguages.setSelectedItem(MessageUtils.getLocale().getLanguage());
		//cmbLanguages.setMinimumSize(new Dimension(120, 20));
		chkCopyOnlySource = new VPCheckBox("SETTINGS_SRC_DEST", obfuscator.isCopyOnlySource());
		chkCommentRemover = new VPCheckBox("SETTINGS_RM_COMM", obfuscator.isCommentRemover());
		chkWhiteSpaceRemover = new VPCheckBox("SETTINGS_RM_WS", obfuscator.isWhiteSpacesRemover());
		GuiUtils.addItem(generalPanel, cmbLanguages, 0, 0, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(generalPanel, chkCopyOnlySource, 0, 1, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(generalPanel, chkCommentRemover, 0, 2, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(generalPanel, chkWhiteSpaceRemover, 0, 3, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
	}

	private void buildVariablePanel() {
		variablePanel = new JPanel(gridBagLayout);		
		variablePanel.setBorder(BorderFactory.createTitledBorder(MessageUtils.getMessage("SETTINGS_VARIABLE")));
		chkVariableFinder = new VPCheckBox("SETTINGS_NORMAL_VAR", obfuscator.isVariableFinder());
		chkSystemVariableFinder = new VPCheckBox("SETTINGS_SYS_VAR", obfuscator.isSystemVariableFinder());
		chkAnchorFinder = new VPCheckBox("SETTINGS_ANCHOR_VAR", obfuscator.isAnchorFinder());
		chkInputFinder = new VPCheckBox("SETTINGS_INPUT_VAR", obfuscator.isInputFinder());
		chkJavaScriptFinder = new VPCheckBox("SETTINGS_JAVAS_VAR", obfuscator.isJavaScriptFinder());
		GuiUtils.addItem(variablePanel, chkVariableFinder, 0, 0, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(variablePanel, chkSystemVariableFinder, 0, 1, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(variablePanel, chkAnchorFinder, 0, 2, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(variablePanel, chkInputFinder, 0, 3, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(variablePanel, chkJavaScriptFinder, 0, 4, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
	}

	private void buildFunctionPanel() {
		functionPanel = new JPanel(gridBagLayout);
		functionPanel.setBorder(BorderFactory.createTitledBorder(MessageUtils.getMessage("SETTINGS_FUNCTION")));
		chkFunctionFinder = new VPCheckBox("SETTINGS_DR_FUNCTION", obfuscator.isFunctionFinder());
		GuiUtils.addItem(functionPanel, chkFunctionFinder, 0, 0, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
	}
}