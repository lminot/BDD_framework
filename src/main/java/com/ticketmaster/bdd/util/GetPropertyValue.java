package com.ticketmaster.bdd.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;


public class GetPropertyValue {

    private static final Logger log = Logger.getLogger(GetPropertyValue.class.getName());
	private static InputStream input = null;
	private static Properties prop = new Properties();
	
    /**
     * loads the property file and returns the value of the input key
     * @param filePath
     * @param key
     * @return
     */
    public static String getValueFromPropertyFile(String propertyFilePath , String key){
		String value = null;
		try {
			input = new FileInputStream(propertyFilePath);
			try {
				prop.load(input);
				value = prop.getProperty(key);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
    	return value.trim();
    }
}
