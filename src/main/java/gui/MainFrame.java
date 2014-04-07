package gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class displays the program main frame. It contains the toolbox and 
 * the two tab panels.
 * 
 */

import gui.dialog.AboutDialog;
import gui.dialog.MessageDialog;
import gui.dialog.SettingsDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import obfuscation.Obfuscator;
import variable.Variable;

import common.FileUtils;
import common.MessageUtils;
import common.ThreadInterface;

public class MainFrame extends JFrame implements IMainContainer {
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

	private static final String DEFAULT_DIR = "C:/xampp/htdocs/";
	
	// Control
	private Obfuscator obfuscator;

	private List<File> fileList  = new ArrayList<File>();

	private List<Variable> variableList = new ArrayList<Variable>();

	
	public MainFrame() {
		resetObfuscator();
	}
	
	private void resetObfuscator() {
		this.obfuscator = new Obfuscator();		
	}

	public void displayPanel () {
		this.setTitle(COMPLETE_NAME);
		this.setIconImage(GuiUtils.readImage(GuiUtils.APPLICATION_LOGO));
		this.setLayout(new BorderLayout());
		this.setSize(550, 680);
		this.setLocation(GuiUtils.getCenterOfScreen(this));
		initGuiElements();
		initTabs();
		this.add(tabbedPanel, BorderLayout.CENTER);
		this.add(progressPanel, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	private void initGuiElements () {
		this.setJMenuBar(buildMenuBar());
		this.toolBar = buildToolBar();
		this.add(toolBar, BorderLayout.NORTH);
	}
	
	private void initTabs () {
		tabbedPanel.add(MessageUtils.getMessage("TAB_PROJECT"), projectPanel);
		tabbedPanel.add(MessageUtils.getMessage("TAB_VARIABLES"), variablePanel);
	}
	
	public void setTexts() {
		tabbedPanel.setTitleAt(0, MessageUtils.getMessage("TAB_PROJECT"));
		tabbedPanel.setTitleAt(1, MessageUtils.getMessage("TAB_VARIABLES"));
		projectPanel.setTexts();
		projectPanel.repaintFileList(fileList);
		variablePanel.setTexts();
		variablePanel.repaintVariableList(variableList);
		progressPanel.setTexts ();
		this.remove(toolBar);
		this.toolBar = buildToolBar();
		this.add(toolBar, BorderLayout.NORTH);
		this.setJMenuBar(buildMenuBar());
		this.repaint();
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

	private ActionListener buildResetActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetProject();
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

	public ActionListener buildOpenActionListener() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				loadFile();
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

	public ActionListener buildProceedUnobfuscationActionListener () {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				unobfuscateProject();
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
		SettingsDialog.displaySettingsDialog(this, obfuscator);	
	}
	
	protected void displayHelpPanel() {
		AboutDialog.displayHelpDialog(this, MessageUtils.getMessage("ABOUT") + COMPLETE_NAME);	
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
			MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "CHOOSE_SRC_DIR");
		}
	}
	
	protected void saveFile() {
		List<Variable> variableList = obfuscator.getProjectVariableList();
		if (variableList != null) {
			String fileName = saveFileDialog();
			if (fileName != null) {
				int fileSize = FileUtils.saveVariableList(fileName, variableList);			
				MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "FILE_SAVED", fileName, fileSize);				
			}
		} else {
			MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "NO_VAR_TO_SAVE");								
		}
	}
	
	protected void loadFile() {
		String fileName = openFileDialog();
		if (fileName != null) {
			List<Variable> variableList = FileUtils.loadVariableList(fileName);
			obfuscator.setProjectVariableList(variableList);
			variablePanel.repaintVariableList(variableList);	
			tabbedPanel.setSelectedComponent(variablePanel);
		}
	}
	
	protected void fillPanelFields(boolean sourceField) {
		String selectedDir = openDirDialog();
		if (selectedDir != null) {
			if (sourceField) {
				setSourceDir(selectedDir);
			} else if (!projectPanel.getSourceDir().equals(selectedDir)) {
				setDestinationDir(selectedDir);
			} else {
				MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "DIR_IDENTICAL");
			}
		}
	}

	protected String openDirDialog() {
		return openDialog(JFileChooser.DIRECTORIES_ONLY);
	}
	
	protected String openFileDialog() {
		return openDialog(JFileChooser.FILES_ONLY);
	}
		

	protected String saveFileDialog() {
		return openDialog(JFileChooser.SAVE_DIALOG);
	}
	
	protected String openDialog(int mode) {
		JFileChooser jfc = new JFileChooser(new File(DEFAULT_DIR));
		jfc.setFileSelectionMode(mode);
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
		for (Component component :getJMenuBar().getComponents()) {
			component.setEnabled(enable);
		}
		for (Component component : toolBar.getComponents()) {
			component.setEnabled(enable);
		}
	}
	
	public void obfuscateProject() {
		if (variablePanel.getVariableListSize() == 0) {
			MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "NO_VAR_TO_OBF");
			return;
		}
		if (!destinationDirectory.equals("")) {
			setActionButtonsEnabled(false);
			tabbedPanel.setSelectedComponent(variablePanel);
			ThreadInterface threadInterface = new ThreadInterface(2);
			threadInterface.setMethod(0, obfuscator, "obfuscateProject", (Class<Object>[])null, (Object[])null);
			threadInterface.setMethod(1, this, "displayDestinationFolder", (Class<Object>[])null, (Object[])null);
			threadInterface.runMethods("obfuscate Thread");
		} else {
			tabbedPanel.setSelectedComponent(projectPanel);
			MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "CHOOSE_DES_DIR");
			fillPanelFields(false);
		}
	}
	
	public void unobfuscateProject() {
		if (variablePanel.getVariableListSize() == 0) {
			MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "NO_VAR_TO_UNOBF");
			return;
		}
		if (!destinationDirectory.equals("")) {
			setActionButtonsEnabled(false);
			tabbedPanel.setSelectedComponent(variablePanel);
			ThreadInterface threadInterface = new ThreadInterface(2);
			threadInterface.setMethod(0, obfuscator, "unobfuscateProject", (Class<Object>[])null, (Object[])null);
			threadInterface.setMethod(1, this, "displayDestinationFolder", (Class<Object>[])null, (Object[])null);
			threadInterface.runMethods("unobfuscate Thread");
		} else {
			tabbedPanel.setSelectedComponent(projectPanel);
			MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "CHOOSE_DES_DIR");
			fillPanelFields(false);
		}
	}
		
	public void displayDestinationFolder() {
		setActionButtonsEnabled(true);
		MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "OBF_COMPLETED");
	}
	
	protected void resetProject() {
		obfuscator.reset();
		projectPanel.reset();
		variablePanel.reset();
		progressPanel.reset();
		setSourceDir("");
		setDestinationDir("");
		fileList = new ArrayList<File>();
		variableList = new ArrayList<Variable>();
		//projectPanel.resetFileList();
		this.repaint();
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
			MessageDialog.displayMessageDialog(this, "INFORMATION_DIALOG", "CHOOSE_SRC_DIR");
			fillPanelFields(true);
		}
	}
	
	public void displayProjectVariables() {
		variableList = obfuscator.getProjectVariableList();
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
		obfuscator.setSourceDir(selectedDir);
		variablePanel.resetVariableList();
		obfuscator.getFileFromSourceDir();
		fileList = obfuscator.getProjectFiles();
		projectPanel.repaintFileList(fileList);
	}
	
	public void setDestinationDir(String selectedDir) {
		destinationDirectory = selectedDir;
		projectPanel.setDestinationDirectory(selectedDir);
		obfuscator.setDestinationDir(selectedDir);	
	}
	
	private JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
	    
		JMenu mnuFile = new JMenu(MessageUtils.getMessage("MENU_FILE"));
	    JMenuItem menuItem; 
	    menuItem = new JMenuItem(MessageUtils.getMessage("MENU_HOME"));
	    menuItem.setMnemonic(menuItem.getText().codePointAt(0));
	    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, Event.CTRL_MASK));
	    menuItem.addActionListener(buildHomeActionListener());
	    mnuFile.add(menuItem);
//	    menuItem = new JMenuItem(MessageUtils.getMessage("MENU_SAVE"));
//	    menuItem.setMnemonic(menuItem.getText().codePointAt(0));
//	    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, Event.CTRL_MASK));
//	    menuItem.addActionListener(buildSaveActionListener());
//	    mnuFile.add(menuItem);
	    mnuFile.addSeparator();
	    menuItem = new JMenuItem(MessageUtils.getMessage("MENU_EXIT"));
	    menuItem.setMnemonic(menuItem.getText().codePointAt(0));
	    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, Event.CTRL_MASK));
	    menuItem.addActionListener(buildExitActionListener());
	    mnuFile.add(menuItem);

	    JMenu mnuTool = new JMenu(MessageUtils.getMessage("MENU_TOOLS"));
	    menuItem = new JMenuItem(MessageUtils.getMessage("MENU_GET_VAR"));
	    menuItem.setMnemonic(menuItem.getText().codePointAt(0));
	    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G, Event.CTRL_MASK));
	    menuItem.addActionListener(buildGetVariablesActionListener());
	    mnuTool.add(menuItem);
	    menuItem = new JMenuItem(MessageUtils.getMessage("MENU_OBF"));
	    menuItem.setMnemonic(menuItem.getText().codePointAt(0));
	    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, Event.CTRL_MASK));
	    menuItem.addActionListener(buildProceedObfuscationActionListener());
	    mnuTool.add(menuItem);
//	    menuItem = new JMenuItem(MessageUtils.getMessage("MENU_UNOBF"));
//	    menuItem.setMnemonic(menuItem.getText().codePointAt(0));
//	    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, Event.CTRL_MASK));
//	    menuItem.addActionListener(buildProceedUnobfuscationActionListener());
//	    mnuTool.add(menuItem);
	    mnuTool.addSeparator();
	    menuItem = new JMenuItem(MessageUtils.getMessage("MENU_SETTINGS"));
	    menuItem.setMnemonic(menuItem.getText().codePointAt(0));
	    menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Event.CTRL_MASK));
	    menuItem.addActionListener(buildSettingsActionListener());
	    mnuTool.add(menuItem);
		
		menuBar.add(mnuFile);
		menuBar.add(mnuTool);
		
		return menuBar;
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
        button = GuiUtils.createToolBarButton(GuiUtils.RESET_ICON);
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_RESET"));
        button.addActionListener(buildResetActionListener());
        toolBar.add(button);
        toolBar.addSeparator();
        
//        button = GuiUtils.createToolBarButton(GuiUtils.SAVE_ICON);;
//        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_SAVE"));
//        button.addActionListener(buildSaveActionListener());
//        toolBar.add(button);
//        button = GuiUtils.createToolBarButton(GuiUtils.OPEN_ICON);;
//        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_OPEN"));
//        button.addActionListener(buildOpenActionListener());
//        toolBar.add(button);
//        toolBar.addSeparator();

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
//        button = GuiUtils.createToolBarButton(GuiUtils.UNOBFUSCATE_ICON);
//        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_UNOBF"));
//        button.addActionListener(buildProceedUnobfuscationActionListener());
//        toolBar.add(button);

        toolBar.addSeparator();

        button = GuiUtils.createToolBarButton(GuiUtils.HELP_ICON);
        button.setToolTipText(MessageUtils.getMessage("TOOLTIP_ABOUT", APPLICATION_NAME, APPLICATION_VERSION));
        button.addActionListener(buildHelpActionListener());
        toolBar.add(button);

        toolBar.add(Box.createGlue());

        return toolBar;
    }
}