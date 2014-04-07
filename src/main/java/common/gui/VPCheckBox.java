package common.gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Child class of JCheckBox which implements some features of MessageUtils class 
 * 
 */

import javax.swing.JCheckBox;

import common.MessageUtils;

public class VPCheckBox extends JCheckBox {
	private static final long serialVersionUID = -8852745830720744579L;
	private String messageName;
	
	public VPCheckBox() {
	}
	
	public VPCheckBox(String messageName) {
		setText(messageName);
	}
	
	public VPCheckBox(String messageName, boolean checked) {
		setText(messageName);
		super.setSelected(checked);
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
