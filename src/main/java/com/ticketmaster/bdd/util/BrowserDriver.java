package com.ticketmaster.bdd.util;

import java.io.File;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

/**
 * Initializing local machine browser driver
 * In Active Monitoring, local drivers are included in project
 * @author Derick.Cornejo
 */
public class BrowserDriver
{
	Logger logger = Log.getLogger(BrowserDriver.class);
	
	private WebDriver driver;
	
	public void initLocalChrome() throws Exception {

		DesiredCapabilities capability = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");
		
		// Validates which chromedriver to use based on mac or windows OS
			if (OSValidator.isWindows()) {
				System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
				logger.info("Using windows chromedriver driver.");
			}
			else if(OSValidator.isMac()) {
				System.setProperty("webdriver.chrome.driver", "chromedriver");
				logger.info("Using mac chromedriver driver.");
			}
		
		capability.setCapability(ChromeOptions.CAPABILITY, options);
		capability.setCapability("takeScreenshot", true);
		driver = new ChromeDriver(capability);	
	} 
	
	public void initLocalFireFox() throws Exception
	{
		driver = new FirefoxDriver();
		driver.manage().window().maximize();
	}
	
	public void initLocalIE() throws Exception
	{
		DesiredCapabilities cap = DesiredCapabilities.internetExplorer();
		cap.setCapability("ignoreProtectedModeSettings", true);
		File file = new File("IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
		driver = new InternetExplorerDriver(cap);
		driver.manage().window().maximize();
	}
	
	public WebDriver getChromeInstance() throws Exception
	{
		initLocalChrome();
		return driver;
	}
	
	public WebDriver getFirefoxInstance() throws Exception
	{
		initLocalFireFox();
		return driver;
	}
	
	public WebDriver getInternetExplorerInstance() throws Exception
	{
		initLocalIE();
		return driver;
	}
}
