package gui.dialog;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Panel that displays messages. 
 * 
 */

import gui.GuiUtils;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JLabel;

import common.MessageUtils;

@SuppressWarnings("serial")
public class MessageDialog extends Dialog {
	
	private JLabel lblMessage = new JLabel();
	private JLabel lblImage = new JLabel(GuiUtils.readImageIcon(GuiUtils.INFORMATION_PICTURE)); 
	
	public static void displayMessageDialog (JFrame owner, String titleKey, String message, Object...messageArgs) {
		message = MessageUtils.getMessage(message, messageArgs);
		MessageDialog messageDialog = new MessageDialog(owner, titleKey, message);
		messageDialog.displayDialog();
	}
	
	public MessageDialog(JFrame owner, String key, String message) {
		super(owner, key);
		lblMessage.setText(message);
	}
	
	protected void placeGuiElements() {
		Insets insets = new Insets(5, 5, 5, 5);
		GuiUtils.addItem(panel, lblImage, 0, 0, 1, 1, 0, 0, insets,
				GridBagConstraints.WEST, GridBagConstraints.NONE);
		GuiUtils.addItem(panel, lblMessage, 1, 0, 1, 1, 1, 0, insets,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		GuiUtils.addItem(panel, btnOk, 0, 1, 2, 1, 0, 0, insets,
				GridBagConstraints.CENTER, GridBagConstraints.NONE);
	}

	protected void initGuiElements() {
		btnOk.addActionListener(new ActionListener () {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}		
		});
	}

	@Override
	protected void setTexts() {
		// TODO Auto-generated method stub
		
	}
}