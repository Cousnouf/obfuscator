package common.gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Child class of JButton which implements some features of MessageUtils class 
 * 
 */

import javax.swing.JButton;

import common.MessageUtils;

public class VPButton extends JButton {
	private static final long serialVersionUID = -8852745830720744579L;
	private String messageName;
	
	public VPButton() {
	}
	
	public VPButton(String messageName) {
		setText(messageName);
	}

	@Override
	public void setText(String text) {
		this.messageName = text;
		text = MessageUtils.getMessage(this.messageName);
		super.setText(text);
	}
	
	public String getMessageName () {
		return messageName;
	}
}
