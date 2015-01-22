package com.ticketmaster.bdd.util;

import java.io.File;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class LocalBrowser
{
	private static ChromeDriverService service;
	private WebDriver driver;
	
	public void initLocalChrome() throws Exception
	{
		service = new ChromeDriverService.Builder().usingDriverExecutable(new File("chromedriver.exe")).usingAnyFreePort().build();
		service.start();
		driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
		driver.manage().window().maximize();
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
