package main;

/**
 * @Author Marc Cappelletti
 * @Version 1.0
 * @Date December 2008
 * @Purpose
 * public static void main class. 
 * 
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gui.MainFrame;

public class MainApplication {

    private static Logger logger = LoggerFactory.getLogger(MainApplication.class.getName());

	public static void main(String[] args) {
        logger.info("Launching Mcbc Obfuscator");
		MainFrame mainGui = new MainFrame();
        mainGui.displayPanel();
    }
}