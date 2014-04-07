package common.gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Child class of JLabel which implements some features of MessageUtils class 
 * 
 */

import javax.swing.JLabel;

import common.MessageUtils;

public class VPLabel extends JLabel {
	private static final long serialVersionUID = -2365913079820613157L;
	private String messageName;
	
	public VPLabel() {
	}
	
	public VPLabel(String messageName) {
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
