package main;

import gui.GuiUtils;
import gui.IMainContainer;
import gui.ProgressPanel;
import gui.ProjectPanel;
import gui.VariablePanel;
import gui.dialog.AboutDialog;
import gui.dialog.MessageDialog;
import gui.dialog.SettingsDialog;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import obfuscation.Obfuscator;
import variable.Variable;

import common.FileUtils;
import common.MessageUtils;
import common.ThreadInterface;

public class MainApplicationApplet extends Applet implements IMainContainer {
	private static final long serialVersionUID = 1L;

    private JTabbedPane tabbedPanel = new JTabbedPane();
    private ProjectPanel projectPanel = new ProjectPanel(this);
    private VariablePanel variablePanel = new VariablePanel(this);
    private ProgressPanel progressPanel = new ProgressPanel(this);
    
    private JToolBar toolBar;
    private String sourceDirectory = "";
    private String destinationDirectory = "";

    private static final String PROJECT_GROUP_NAME = "Volatile Project";
	private static final String APPLICATION_NAME = "Php Obfuscator";
	private static final String APPLICATION_VERSION = "1.0";
	public static final String COMPLETE_NAME = PROJECT_GROUP_NAME + " " + APPLICATION_NAME + " " + APPLICATION_VERSION;

	public static final Integer DISPLAY_PROJECT_VARIABLES = 0;

	public static final Integer DISPLAY_DESTINATION_FOLDER = 1;
	
	// Control
	private Obfuscator obfuscator;
	
	public void init () {
		this.obfuscator = new Obfuscator();
		this.setLayout(new BorderLayout());
		this.setSize(550, 680);
		this.setLocation(GuiUtils.getCenterOfScreen(this));
		initGuiElements();
		initTabs();
		this.add(tabbedPanel, BorderLayout.CENTER);
		this.add(progressPanel, BorderLayout.SOUTH);
		this.setVisible(true);
	}

	private void initGuiElements () {
		this.toolBar = buildToolBar();
		this.add(toolBar, BorderLayout.NORTH);
	}
	
	private void initTabs () {
		tabbedPanel.add(MessageUtils.getMessage("TAB_PROJECT"), projectPanel);
		tabbedPanel.add(MessageUtils.getMessage("TAB_VARIABLES"), variablePanel);
	}
	
	public ActionListener buildGetVariablesActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getProjectVariables();
			}
		};
	}

	public ActionListener buildExitActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		};
	}
	
	public ActionListener buildHomeActionListener () {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPanel.setSelectedComponent(projectPanel);
			}
		};
	}

	public ActionListener buildSaveActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveFile();
			}
		};
	}

	public ActionListener buildSettingsActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displaySettingsPanel();
			}
		};
	}
	
	public ActionListener buildProceedObfuscationActionListener () {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				obfuscateProject();
			}
		};
	}
	
	public ActionListener buildCreateDefaultDesinationDirectoryActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createDefaultDesinationDir();
			}
		};
	}

	public ActionListener buildSourceDirActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillPanelFields(true);
			}
		};
	}

	public ActionListener buildDestDirActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fillPanelFields(false);
			}
		};
	}
	
	public ActionListener buildHelpActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayHelpPanel();
			}
		};
	}
	
	protected void displaySettingsPanel() {
		SettingsDialog.displaySettingsDialog(new JFrame(), obfuscator);	
	}
	
	protected void displayHelpPanel() {
		AboutDialog.displayHelpDialog(new JFrame(), MessageUtils.getMessage("ABOUT") + COMPLETE_NAME);	
	}

	protected void createDefaultDesinationDir() {
		if (!sourceDirectory.trim().equals("")) {
			String sourceDirectoryName = new File(sourceDirectory).getName();
			destinationDirectory = sourceDirectoryName.concat("_obfuscated");
			destinationDirectory = sourceDirectory.substring(0,
					sourceDirectory.lastIndexOf(sourceDirectoryName)).concat(
					destinationDirectory);
			setDestinationDir(destinationDirectory);
		} else {
			MessageDialog.displayMessageDialog(new JFrame(), MessageUtils.getMessage("INFORMATION_DIALOG"), MessageUtils.getMessage("CHOOSE_SRC_DIR"));
		}
	}
	
	protected void saveFile() {
		List<Variable> variableList = obfuscator.getProjectVariableList();
		if (variableList != null) {
			String fileName = saveFileDialog();
			if (fileName != null) {
				int fileSize = FileUtils.saveVariableList(fileName, variableList);			
				MessageDialog.displayMessageDialog(new JFrame(), MessageUtils.getMessage("INFORMATION_DIALOG"), MessageUtils.getMessage("FILE_SAVED", fileName, fileSize));				
			}
		} else {
			MessageDialog.displayMessageDialog(new JFrame(), MessageUtils.getMessage("INFORMATION_DIALOG"), MessageUtils.getMessage("NO_VAR_TO_SAVE"));								
		}
	}
	
	protected void fillPanelFields(boolean sourceField) {
		String selectedDir = openDirDialog();
		if (selectedDir != null) {
			if (sourceField) {
				setSourceDir(selectedDir);
				obfuscator.getFileFromSourceDir();
				List<File> fileList = obfuscator.getProjectFiles();
				projectPanel.repaintFileList(fileList);
			} else {
				setDestinationDir(selectedDir);
			}
		}
	}
	
	protected String openDirDialog() {
		JFileChooser jfc = new JFileChooser(new File("C:/xampp/htdocs/"));
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfc.showOpenDialog(this);
		File selectedFile = jfc.getSelectedFile();
		if (selectedFile != null) {
			return selectedFile.getPath();
		}
		return null;
	}

	protected String saveFileDialog() {
		JFileChooser jfc = new JFileChooser(new File("C:/xampp/htdocs/"));
		jfc.setFileSelectionMode(JFileChooser.SAVE_DIALOG);
		jfc.showOpenDialog(this);
		File selectedFile = jfc.getSelectedFile();
		if (selectedFile != null) {
			return selectedFile.getPath();
		}
		return null;
	}
	
	private void setActionButtonsEnabled (boolean enable) {
		variablePanel.setButtonsEnabled(enable);
		variablePanel.setVariableListEnabled(enable);
		for (Component component : toolBar.getComponents()) {
			component.setEnabled(enable);
		}
	}
	
	public void obfuscateProject() {
		if (variablePanel.getVariableListSize() == 0) {
			MessageDialog.displayMessageDialog(new JFrame(), MessageUtils.getMessage("INFORMATION_DIALOG"), MessageUtils.getMessage("NO_VAR_TO_OBF"));
			return;
		}
		if (!destinationDirectory.equals("")) {
			setActionButtonsEnabled(false);
			tabbedPanel.setSelectedComponent(variablePanel);
			ThreadInterface threadInterface = new ThreadInterface(2);
			threadInterface.setMethod(0, obfuscator, "obfuscateProject", (Class<Object>[])null, (Object[])null);
			threadInterface.setMethod(1, this, "displayDestinationFolder", (Class<Object>[])null, (Object[])null);
			threadInterface.runMethods("getProjectVariables Thread");
		} else {
			tabbedPanel.setSelectedComponent(projectPanel);
			MessageDialog.displayMessageDialog(new JFrame(), MessageUtils.getMessage("INFORMATION_DIALOG"), MessageUtils.getMessage("CHOOSE_DES_DIR"));
			fillPanelFields(false);
		}
	}
		
	public void displayDestinationFolder() {
		setActionButtonsEnabled(true);
		MessageDialog.displayMessageDialog(new JFrame(), MessageUtils.getMessage("INFORMATION_DIALOG"), MessageUtils.getMessage("OBF_COMPLETED"));
	}
	
	public void getProjectVariables() {
		if (!sourceDirectory.trim().equals("")) {
			tabbedPanel.setSelectedComponent(variablePanel);
			setActionButtonsEnabled(false);
			ThreadInterface threadInterface = new ThreadInterface(2);
			threadInterface.setMethod(0, obfuscator, "parseAndGetVariables", (Class<Object>[])null, (Object[])null);
			threadInterface.setMethod(1, this, "displayProjectVariables", (Class<Object>[])null, (Object[])null);
			threadInterface.runMethods("getProjectVariables Thread");
		} else {
			tabbedPanel.setSelectedComponent(projectPanel);
			MessageDialog.displayMessageDialog(new JFrame(), MessageUtils.getMessage("INFORMATION_DIALOG"), MessageUtils.getMessage("CHOOSE_SRC_DIR"));
			fillPanelFields(true);
		}
	}
	
	public void displayProjectVariables() {
		List<Variable> variableList = obfuscator.getProjectVariableList();
		if (variableList != null) {
			variablePanel.repaintVariableList(variableList);			
		}
		setActionButtonsEnabled(true);
	}
	
	public void notify(Object[] objects) {
		Integer value = (Integer)objects[0];
		if (value.equals(DISPLAY_PROJECT_VARIABLES)) {
			displayProjectVariables();
		} else if (value.equals(DISPLAY_DESTINATION_FOLDER)) {
			displayDestinationFolder();
		}
	}
	
	public void setSourceDir(String selectedDir) {
		sourceDirectory = selectedDir;
		projectPanel.setSourceDirectory(selectedDir);
		variablePanel.repaintVariableList(new ArrayList<Variable>());
		obfuscator.setSourceDir(selectedDir);
	}
	
	public void setDestinationDir(String selectedDir) {
		destinationDirectory = selectedDir;
		projectPanel.setDestinationDirectory(selectedDir);
		obfuscator.setDestinationDir(selectedDir);	
	}
	
    private JToolBar buildToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);

        AbstractButton button;
        
        button = GuiUtils.createToolBarButton(GuiUtils.EXIT_ICON);
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_EXIT"));
        button.addActionListener(buildExitActionListener());
        toolBar.add(button);
        button = GuiUtils.createToolBarButton(GuiUtils.HOME_ICON);
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_HOME"));
        button.addActionListener(buildHomeActionListener());
        toolBar.add(button);
        button = GuiUtils.createToolBarButton(GuiUtils.SAVE_ICON);;
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_SAVE"));
        button.addActionListener(buildSaveActionListener());
        toolBar.add(button);
        button = GuiUtils.createToolBarButton(GuiUtils.OPEN_ICON);;
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_OPEN"));
        toolBar.add(button);
        toolBar.addSeparator();

        button = GuiUtils.createToolBarButton(GuiUtils.SETTINGS_ICON);
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_SETTINGS"));
        button.addActionListener(buildSettingsActionListener());
        toolBar.add(button);
        button = GuiUtils.createToolBarButton(GuiUtils.VARIABLES_ICON);
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_GET_VAR"));
        button.addActionListener(buildGetVariablesActionListener());
        toolBar.add(button);
        button = GuiUtils.createToolBarButton(GuiUtils.OBFUSCATE_ICON);
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_OBF"));
        button.addActionListener(buildProceedObfuscationActionListener());
        toolBar.add(button);

        toolBar.addSeparator();

        button = GuiUtils.createToolBarButton(GuiUtils.HELP_ICON);
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_ABOUT", APPLICATION_NAME, APPLICATION_VERSION));
        button.addActionListener(buildHelpActionListener());
        toolBar.add(button);

        toolBar.add(Box.createGlue());

        return toolBar;
    }

	public void setTexts() {
		// TODO Auto-generated method stub
		
	}
}