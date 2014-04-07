package gui;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * Class that provides some methods to handle images and to position items 
 * in panels. The constants are application-specific 
 * 
 */

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class GuiUtils {
	public static final String EXIT_ICON = "exit.png";
	public static final String HOME_ICON = "home.png";
	public static final String RESET_ICON = "reset.png";
	public static final String SAVE_ICON = "save.png";
	public static final String OPEN_ICON = "open.png";
	public static final String HELP_ICON = "help.png";
	public static final String SETTINGS_ICON = "settings.png";
	public static final String OBFUSCATE_ICON = "obfuscate.png";
	public static final String UNOBFUSCATE_ICON = "unobfuscate.png";
	public static final String VARIABLES_ICON = "variables.png";
	public static final String FOLDER_ICON = "folder.png";
	public static final String APPLICATION_LOGO = "applicationLogo.png";
	
	public static final String INFORMATION_PICTURE = "information.png";
	public static final String ABOUT_PICTURE = "volatileProject.png";
	public static final String IMAGE_FOLDER = "images/images/";
	
	public static AbstractButton createToolBarButton(String iconName) {
		JButton button = new JButton(readImageIcon(iconName));
		button.setFocusable(false);
		return button;
	}
	
	public static ImageIcon readImageIcon(String filename) {
		URL url = GuiUtils.class.getClassLoader().getResource(
				IMAGE_FOLDER + filename);
		return new ImageIcon(url);
	}

	public static Image readImage(String filename) {
		URL url = GuiUtils.class.getClassLoader().getResource(
				IMAGE_FOLDER + filename);
		Image image = null;
		try {
			image = ImageIO.read(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public static void addItem(Container container, JComponent component, int x, int y, int width,
			int height, float weightX, float weightY, Insets insets, int align, int fill) {
		GridBagConstraints gridBagConstraint = new GridBagConstraints();
		gridBagConstraint.gridx = x;
		gridBagConstraint.gridy = y;
		gridBagConstraint.gridwidth = width;
		gridBagConstraint.gridheight = height;
		gridBagConstraint.weightx = weightX;
		gridBagConstraint.weighty = weightY;
		gridBagConstraint.insets = insets;
		gridBagConstraint.anchor = align;
		gridBagConstraint.fill = fill;
		container.add(component, gridBagConstraint);
	}
	
	public static Point getCenterOfScreen(Container container) {
		Dimension screenSize, frameSize;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frameSize = container.getSize();
        return new Point(
        	(screenSize.width - frameSize.width) / 2,
            (screenSize.height - frameSize.height) / 2);
	}
}