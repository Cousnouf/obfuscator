package gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Panel that displays the project folder and the files in it. 
 * 
 */

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import common.MessageUtils;
import common.gui.VPButton;
import common.gui.VPLabel;

@SuppressWarnings("serial")
public class ProjectPanel extends JPanel {
	private static String LABEL_FILE_TABLE = MessageUtils.getMessage("PROJECT_FILE_TABLE");

	private IMainContainer parentFrame;

	private VPLabel lblProjectSourcePath = new VPLabel("PROJECT_SRC_DIR");
	private JTextField txtProjectSourcePath = new JTextField();
	private VPButton btnChooseProjectSourceDir = new VPButton();
	private VPLabel lblProjectDestinationPath = new VPLabel("PROJECT_DEST_DIR");
	private JTextField txtProjectDestinationPath = new JTextField();
	private VPButton btnChooseProjectDestinationDir = new VPButton();
	private VPButton btnCreateDefault = new VPButton("PROJECT_CREATE_DEF");

	private VPLabel lblFileTable = new VPLabel(LABEL_FILE_TABLE);
	private JTable tabFileList = new JTable();
	private JScrollPane scrollFileList = new JScrollPane(tabFileList);

	public ProjectPanel(IMainContainer mainPanel) {
		this.parentFrame = mainPanel;
		this.setLayout(new GridBagLayout());
		this.setVisible(true);
		initGuiElements();
		placeGuiElements();
		addListeners();
		this.repaint();
	}

	private void initGuiElements() {
		btnChooseProjectSourceDir
				.setIcon(GuiUtils.readImageIcon(GuiUtils.FOLDER_ICON));
		btnChooseProjectSourceDir.setSize(btnChooseProjectDestinationDir.getSize());
		btnChooseProjectDestinationDir.
				setIcon(GuiUtils.readImageIcon(GuiUtils.FOLDER_ICON));
		txtProjectSourcePath.setEnabled(false);
		txtProjectSourcePath.setDisabledTextColor(Color.black);
		txtProjectDestinationPath.setEnabled(false);
		txtProjectDestinationPath.setDisabledTextColor(Color.black);
	}

	private void placeGuiElements() {
		int gbcAnchor = GridBagConstraints.FIRST_LINE_START;
		int gbcAnchorLeft = GridBagConstraints.WEST;
		int gbcFill = GridBagConstraints.HORIZONTAL;
		int gbcFillNone = GridBagConstraints.NONE;
		Insets insets = new Insets(5, 5, 5, 5);
		GuiUtils.addItem(this, lblProjectSourcePath, 			0, 0, 1, 1, 0, 0, insets, gbcAnchor, gbcFillNone);
		GuiUtils.addItem(this, txtProjectSourcePath, 			0, 1, 3, 1, 1, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(this, btnChooseProjectSourceDir, 		3, 1, 1, 1, 0, 0, insets, gbcAnchorLeft, gbcFillNone);
		GuiUtils.addItem(this, lblProjectDestinationPath, 		0, 2, 1, 1, 0, 0, insets, gbcAnchor, gbcFillNone);
		GuiUtils.addItem(this, txtProjectDestinationPath, 		0, 3, 3, 1, 1, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(this, btnChooseProjectDestinationDir,	3, 3, 1, 1, 0, 0, insets, gbcAnchorLeft, gbcFillNone);
		GuiUtils.addItem(this, btnCreateDefault,		 		3, 4, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(this, lblFileTable, 					0, 5, 1, 1, 0, 0, insets, gbcAnchor, gbcFillNone);
		GuiUtils.addItem(this, scrollFileList, 					0, 6, 4, 1, 2, 2, insets, gbcAnchor, GridBagConstraints.BOTH);
	}
	
	protected void setTexts() {
		LABEL_FILE_TABLE = MessageUtils.getMessage("PROJECT_FILE_TABLE");
		lblProjectSourcePath.setText("PROJECT_SRC_DIR");
		lblProjectDestinationPath.setText("PROJECT_DEST_DIR");
		btnCreateDefault.setText("PROJECT_CREATE_DEF");
	}

	public void repaintFileList(List<File> fileList) {
		if (fileList != null) {
			TableModel model = new FileTableModel(fileList);
			lblFileTable.setText(LABEL_FILE_TABLE + " (" + fileList.size() + ")");
			tabFileList.setModel(model);
			tabFileList.repaint();
			scrollFileList.setViewportView(tabFileList);
			scrollFileList.repaint();			
		}
	}
	
	public void resetFileList () {
		repaintFileList(new ArrayList<File>());
	}

	public void setParentFrame(IMainContainer mainPanel) {
		this.parentFrame = mainPanel;
	}
	
	public void setSourceDirectory(String dir) {
		txtProjectSourcePath.setText(dir);
	}
	
	public void setDestinationDirectory(String dir) {
		txtProjectDestinationPath.setText(dir);
	}

	private void addListeners() {
		btnChooseProjectSourceDir.addActionListener(parentFrame.buildSourceDirActionListener());
		btnChooseProjectDestinationDir.addActionListener(parentFrame.buildDestDirActionListener());
		btnCreateDefault.addActionListener(parentFrame.buildCreateDefaultDesinationDirectoryActionListener());
	}

	public String getSourceDir () {
		return txtProjectSourcePath.getText();
	}

	public void reset() {
		setSourceDirectory("");
		setDestinationDirectory("");
		resetFileList();
	}
}

@SuppressWarnings("serial")
class FileTableModel extends AbstractTableModel {
	private List<File> fileList;

	public static final int KILO_LIMIT = 1024;
	public static final int MEGA_LIMIT = KILO_LIMIT * KILO_LIMIT;

	public FileTableModel(List<File> fileList) {
		this.fileList = fileList;
	}

	String columnNames[] = { 
			"File name", "File folder", "File size" };
	
	public List<File> getFileList() {
		return fileList;
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getRowCount() {
		return fileList.size();
	}

	public Object getValueAt(int row, int column) {
		File file = fileList.get(row);
		if (column == 0) {
			return file.getName();
		} else if (column == 1) {
			return file.getParent();
		} else if (column == 2) {
			return getFormatedFileSize(file);
		}
		return null;
	}

	private String getFormatedFileSize(File file) {
		String byteWord = "bytes";
		String[] unitArray = {byteWord, "K" + byteWord, "M" + byteWord};
		double size = (double)file.length();
		int unitIndex = 0;
		DecimalFormat decimalFormat = new DecimalFormat();
		decimalFormat.setMaximumFractionDigits(3);
		if (size > KILO_LIMIT && size < MEGA_LIMIT) {
			unitIndex = 1;
			size /= (double)KILO_LIMIT;
		} else if (size > MEGA_LIMIT) {
			unitIndex = 2;
			size /= (double)MEGA_LIMIT;
		}
		return decimalFormat.format(size) + " " + unitArray[unitIndex];
	}

	@Override
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int column) {
		return (getValueAt(0, column).getClass());
	}
}