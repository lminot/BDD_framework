package com.ticketmaster.bdd.util;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.openqa.selenium.WebDriver;

public class DriverConfig 
{
	Logger logger = Log.getLogger(DriverConfig.class);
	private static GridFactory gridFactory = new GridFactory();
	private static LocalBrowser localBrowser = new LocalBrowser();
	
	private static final String configLocatorFilePath = "src/test/resources/locators.properties";
	private String driverLocation = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "location");
	
	public WebDriver driverConfig(WebDriver driver, String browser) throws Exception
	{
		if (browser.toLowerCase().equals("firefox")) 
		{
			if(driverLocation.matches("grid") || driverLocation.matches("localgrid"))
				driver = gridFactory.getFirefoxInstance();
			else if(driverLocation.matches("local"))
				driver = localBrowser.getFirefoxInstance();
		
			logger.info("Returning instance of a firefox browser");
		} 
		else if (browser.toLowerCase().equals("chrome")) 
		{
			if(driverLocation.matches("grid" ) || driverLocation.matches("chromeGrid") || driverLocation.matches("localgrid") )
				driver = gridFactory.getChromeInstance();
			else if(driverLocation.matches("local"))
				driver = localBrowser.getChromeInstance();
		
			logger.info("Returning instance of a chrome browser");
		} 
		else if (browser.toLowerCase().equals("ie")) 
		{
			if(driverLocation.matches("grid") || driverLocation.matches("localgrid"))
				driver = gridFactory.getInternetExplorerInstance();
			else if(driverLocation.matches("local"))
				driver = localBrowser.getInternetExplorerInstance();
		
			logger.info("Returning instance of a internet explorer browser");
		}
		
		return driver;
	}
}
