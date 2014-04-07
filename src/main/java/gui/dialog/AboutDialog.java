package gui.dialog;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Panel that displays the "about" information. 
 * 
 */

import gui.GuiUtils;
import gui.MainFrame;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import common.FileUtils;

@SuppressWarnings("serial")
public class AboutDialog extends Dialog {	
	private JLabel lblImage = new JLabel(GuiUtils.readImageIcon(GuiUtils.ABOUT_PICTURE)); 
	private JTextArea textInfo = new JTextArea(); 
	private JScrollPane scrollText = new JScrollPane();
	private JLabel lblApplication = new JLabel();

	public static void displayHelpDialog(JFrame owner, String title) {
		AboutDialog helpPanel = new AboutDialog(owner, title);
		helpPanel.displayDialog();
	}
	
	public AboutDialog(JFrame owner, String title) {
		super(owner, title);
		defaultSize.setSize(500, 600);
	}

	protected void placeGuiElements() {
		Insets insets = new Insets(5, 5, 5, 5);
		GuiUtils.addItem(panel, lblImage, 0, 0, 1, 1, 1, 0, insets,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		GuiUtils.addItem(panel, lblApplication, 0, 1, 1, 1, 1, 0, insets,
				GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		GuiUtils.addItem(panel, scrollText, 0, 2, 1, 1, 1, 1, insets,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH);
		GuiUtils.addItem(panel, btnOk, 0, 3, 1, 1, 1, 0, insets,
				GridBagConstraints.CENTER, GridBagConstraints.NONE);
	}

	protected void initGuiElements() {
		lblApplication.setText(MainFrame.COMPLETE_NAME);
		lblApplication.setFont(new Font("Verdana", Font.BOLD, 14));
		textInfo.setEditable(false);
		textInfo.setDisabledTextColor(Color.black);
		textInfo.setWrapStyleWord(true);
		textInfo.setLineWrap(true);
		textInfo.setFont(new Font("courier new", Font.PLAIN, 14));
		textInfo.setText(getHelpText());
		// To display the top of the text.
		textInfo.select(0, 0);
		scrollText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollText.setViewportView(textInfo);
		scrollText.repaint();
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				closeDialog();
			}			
		});
	}
	
	private String getHelpText () {
		URL url = this.getClass().getClassLoader().getResource("config/about.txt");
		return FileUtils.getFileContent(url);
	}

	@Override
	protected void setTexts() {
		// TODO Auto-generated method stub
		
	}
}
