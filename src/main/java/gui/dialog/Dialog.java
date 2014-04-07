package gui.dialog;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Parent class which provides some common methods and force some implementations. 
 * 
 */

import gui.GuiUtils;
import gui.IMainContainer;

import java.awt.Dimension;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import common.MessageUtils;

@SuppressWarnings("serial")
abstract class Dialog extends JDialog {
	protected JPanel panel = new JPanel();
	protected JButton btnOk = new JButton("OK");
	protected Dimension defaultSize = new Dimension(400, 150);
	protected IMainContainer owner;
	
	public Dialog(JFrame owner, String key) {
		super(owner, MessageUtils.getMessage(key), true);
		this.owner = (IMainContainer)owner;
	}
	
	@Override
	public void setTitle(String key) {
		String title = MessageUtils.getMessage(key);
		super.setTitle(title);
	}

	public void displayDialog () {
		this.setSize(defaultSize);
		this.setLocation(GuiUtils.getCenterOfScreen(this));
		initGuiElements();
		panel.setLayout(new GridBagLayout());
		placeGuiElements();
		this.add(panel);
		panel.setVisible(true);
		this.setVisible(true);
	}
	
	abstract protected void initGuiElements();
	
	abstract protected void placeGuiElements();
	
	abstract protected void setTexts();

	protected void closeDialog() {
		this.dispose();
	}
}
