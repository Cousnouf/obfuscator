package common;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * This class handle the ResourceBundle to manage internationalization. It also
 * seeks for *.properties files which would contain the language data. 
 * 
 */

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class MessageUtils {
	private static final String CONFIG_DIR = "config/";
	private static final String MESSAGE_BASENAME = "message";
	private static final String MESSAGE_PATH = CONFIG_DIR + MESSAGE_BASENAME;
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGE_PATH);
	
	public static List<String> getAvailableLanguages () {
		List<String> result = new ArrayList<String>();
		result.add("");
		for (String language : Locale.getISOLanguages()) {
			String fileName = MESSAGE_BASENAME + "_" + language + ".properties";
			URL url = MessageUtils.class.getClassLoader().getResource(CONFIG_DIR + fileName);
			try {
				url.getFile();
				result.add(new Locale(language).getLanguage());
			} catch (NullPointerException e) {
				// Do nothing
			}
		}
		return result; 
	}

	public static void printMessage(String messageName) {
		try {
			System.out.println(resourceBundle.getString(messageName));			
		} catch (MissingResourceException e) {
			System.out.println(messageName);
		}
	}

	public static String getMessage(String messageName, Object... args) {
		try {
			String message = resourceBundle.getString(messageName);
			return MessageFormat.format(message, args);
		} catch (MissingResourceException e) {
			return messageName;
		}
	}

	public static String getMessage(String messageName) {
		try {
			return resourceBundle.getString(messageName);
		} catch (MissingResourceException e) {
			return messageName;
		}
	}
	
	public static void setLocale(Locale locale) {
		resourceBundle = ResourceBundle.getBundle(MESSAGE_PATH, locale);
	}
	
	public static Locale getLocale () {
		return resourceBundle.getLocale();
	}
}
