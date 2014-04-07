package gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Panel that displays the variable list and their properties. 
 * 
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import variable.Variable;

import common.MessageUtils;
import common.gui.VPButton;
import common.gui.VPLabel;

@SuppressWarnings("serial")
public class VariablePanel extends JPanel {
	private static String LABEL_VARIABLE_TABLE = MessageUtils.getMessage("VARIABLE_TABLE");

	private IMainContainer parentFrame;
    
	private VPLabel lblVariableList = new VPLabel(LABEL_VARIABLE_TABLE);
	private JTable tabVariableList = new JTable();
	private JScrollPane scrollVariableList = new JScrollPane(tabVariableList);

	private VPButton btnGetProjectVariables = new VPButton("VARIABLE_GET_VAR");
	private VPButton btnProceedObfuscation = new VPButton("VARIABLE_OBF");
	
	public VariablePanel(IMainContainer mainPanel) {
		this.parentFrame = mainPanel;
		this.setLayout(new GridBagLayout());
		initGuiElements();
		placeGuiElements();
		addListeners();
	}
	
	private void initGuiElements() {
		btnGetProjectVariables.setIcon(GuiUtils.readImageIcon(GuiUtils.VARIABLES_ICON));
		btnProceedObfuscation.setIcon(GuiUtils.readImageIcon(GuiUtils.OBFUSCATE_ICON));
	}

	private void placeGuiElements() {
		int gbcAnchor = GridBagConstraints.CENTER;
		int gbcFill = GridBagConstraints.NONE;
		Insets insets = new Insets(5, 5, 5, 5);
		GuiUtils.addItem(this, lblVariableList, 		0, 0, 1, 1, 0, 0, insets, gbcAnchor, GridBagConstraints.WEST);
		GuiUtils.addItem(this, scrollVariableList, 		0, 1, 1, 15, 1, 1, insets, gbcAnchor, GridBagConstraints.BOTH);
		GuiUtils.addItem(this, btnGetProjectVariables, 	0, 16, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(this, btnProceedObfuscation, 	0, 17, 1, 1, 0, 0, insets, gbcAnchor, gbcFill);
	}
	
	protected void setTexts() {
		LABEL_VARIABLE_TABLE = MessageUtils.getMessage("VARIABLE_TABLE");
		lblVariableList.setText(LABEL_VARIABLE_TABLE);
		btnGetProjectVariables.setText("VARIABLE_GET_VAR");
		btnProceedObfuscation.setText("VARIABLE_OBF");
	}
	
	public void repaintVariableList (List<Variable> variableList) {
		TableModel model = new VariableTableModel(variableList);
		lblVariableList.setText(LABEL_VARIABLE_TABLE + "(" + variableList.size() + ")");
		tabVariableList.setModel(model);
		tabVariableList.repaint();
		scrollVariableList.setViewportView(tabVariableList);
	}

	public void resetVariableList() {
		repaintVariableList(new ArrayList<Variable>());
	}
	
	public void setParentFrame(IMainContainer parentFrame) {
		this.parentFrame = parentFrame;
	}
	
	public int getVariableListSize() {
		return tabVariableList.getRowCount();
	}

	public void setVariableListEnabled(boolean enabled) {
		tabVariableList.setEnabled(enabled);
	}
	
	public void setButtonsEnabled (boolean enable) {
		btnGetProjectVariables.setEnabled(enable);
		btnProceedObfuscation.setEnabled(enable);
	}
	
	private void addListeners() {
		btnGetProjectVariables.addActionListener(parentFrame.buildGetVariablesActionListener());
		btnProceedObfuscation.addActionListener(parentFrame.buildProceedObfuscationActionListener());
	}

	public void reset() {
		resetVariableList();
		this.repaint();
	}
}
@SuppressWarnings("serial")
class VariableTableModel extends AbstractTableModel {
	
	List<Variable> variableList;
	
	public VariableTableModel(List<Variable> variableList) {
		this.variableList = variableList;
	}

	String columnNames[] = { 
			MessageUtils.getMessage("VARIABLE_TABLE_NAME"), 
			MessageUtils.getMessage("VARIABLE_TABLE_REPL_NAME"), 
			MessageUtils.getMessage("VARIABLE_TABLE_OBF")};

	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public int getRowCount() {
		return variableList.size();
	}

	public Object getValueAt(int row, int column) {
		Variable variable = variableList.get(row);
		if (column == 0) {
			return variable.getName();
		} else if (column == 1) {
			return variable.getReplacementName();
		} else if (column == 2) {
			return new Boolean(variable.isObfuscable());
		}
		return null;
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		Variable variableToChange = variableList.get(row);
		if (column == 1) {
			variableToChange.setReplacementName((String)value);
		} else if (column == 2) {
			variableToChange.setObfuscate((Boolean)value);
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Class getColumnClass(int column) {
		return (getValueAt(0, column).getClass());
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return (column != 0);
	}
}

