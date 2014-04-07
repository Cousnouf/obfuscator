package gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Panel that displays the progress bar and the informations concerning it. 
 * 
 */

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.TitledBorder;

import common.MessageUtils;

@SuppressWarnings("serial")
public class ProgressPanel extends JPanel {
	private JLabel lblCurrentObjectStatus = new JLabel();
	private JProgressBar barCurrentObjectStatus = new JProgressBar();
	int maximum = 1;
	int value;

	public ProgressPanel(IMainContainer mainPanel) {
		this.setLayout(new GridBagLayout());
		this.setVisible(true);
		initGuiElements();
		placeGuiElements();
		setTexts ();
	}

	public void setMaximumValue(int maximum) {
		barCurrentObjectStatus.setMaximum(maximum);
		this.maximum = maximum;
	}

	public void setCurrentValue(int value) {
		this.value = value;
		barCurrentObjectStatus.setValue(value);
		barCurrentObjectStatus.repaint();
	}
	
	public void resetValue () {
		setCurrentValue(0);			
	}
	
	public void incrementValue () {
		setCurrentValue(++value);	
	}

	public void setStatusLabel(String value) {
		lblCurrentObjectStatus.setText(value);
	}

	private void initGuiElements() {
		lblCurrentObjectStatus.setText(" ");
	}

	private void placeGuiElements() {
		int gbcAnchor = GridBagConstraints.CENTER;
		int gbcFill = GridBagConstraints.HORIZONTAL;
		Insets insets = new Insets(5, 5, 5, 5);
		GuiUtils.addItem(this, lblCurrentObjectStatus, 0, 0, 1, 1, 0, 0,
				insets, gbcAnchor, gbcFill);
		GuiUtils.addItem(this, barCurrentObjectStatus, 0, 1, 1, 1, 1, 1,
				insets, gbcAnchor, gbcFill);
	}
	
	protected void setTexts () {
		this.setBorder(new TitledBorder(MessageUtils.getMessage("PROGRESS_STATUS")));
	}

	public int getValue() {
		return value;
	}

	public void reset() {
		resetValue();
		setStatusLabel("");
	}
}