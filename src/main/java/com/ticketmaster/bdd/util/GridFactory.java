package com.ticketmaster.bdd.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class GridFactory {

	private static final String hubURL = "http://pmp.tm.tmcs:3306/wd/hub";
	
	private GridFactory() {
	}
	
	public static WebDriver getInternetExplorer8Instance() throws Exception {
		return getInternetExplorerInstance("8");
	}
	
	public static WebDriver getInternetExplorer9Instance() throws Exception {
		return getInternetExplorerInstance("9");
	}

	public static WebDriver getInternetExplorer10Instance() throws Exception {
		return getInternetExplorerInstance("10");
	}
	
	public static WebDriver getInternetExplorerInstance() throws Exception {
		return getInternetExplorerInstance("10");
	}

	public static WebDriver getInternetExplorerInstance(String version) throws Exception {
		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
		capability.setCapability("takeScreenshot", true);
		capability.setPlatform(Platform.WINDOWS);
		capability.setBrowserName("internet explorer");

		capability
				.setCapability(
						InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS,
						true);

		WebDriver driver;
		try {
			driver = new RemoteWebDriver(new URL(hubURL), capability);
		} catch (MalformedURLException me) {
			// Change to logger
			System.out
					.println("Please check "
							+ hubURL
							+ " as it is specified to be the Selenium Hub URL but it's not responding correctly.");
			throw new Exception("Cannot connect to the Grid.");
		}

		driver = new Augmenter().augment(driver);
		return driver;
	}

	public static WebDriver getFirefoxInstance() throws Exception {
		DesiredCapabilities capability = DesiredCapabilities.firefox();
		capability.setCapability("takeScreenshot", true);

		WebDriver driver = null;

		try {
			driver = new RemoteWebDriver(new URL(hubURL), capability);
		} catch (MalformedURLException me) {
			throw new Exception("Cannot connect to the Grid.");
		}

		driver = new Augmenter().augment(driver);
		return driver;
	}
	
	public static WebDriver getChromeInstance() throws Exception {
		DesiredCapabilities capability = DesiredCapabilities.chrome();

		ChromeOptions options = new ChromeOptions();
		options.addArguments("--start-maximized");

		capability.setCapability(ChromeOptions.CAPABILITY, options);
		capability.setCapability("takeScreenshot", true);

		WebDriver driver = null;

		try {
			driver = new RemoteWebDriver(new URL(hubURL), capability);
		} catch (MalformedURLException me) {
			// Change to logger
			System.out
					.println("Please check "
							+ hubURL
							+ " as it is specified to be the Selenium Hub URL but it's not responding correctly.");
			throw new Exception("Cannot connect to the Grid.");
		}

		driver = new Augmenter().augment(driver);
		return driver;
	}
	
	public static WebDriver getPhantomJSInstance() throws Exception {
		DesiredCapabilities capability = DesiredCapabilities.phantomjs();

		WebDriver driver = null;
		try {
			driver = new RemoteWebDriver(new URL(hubURL), capability);
		} catch (MalformedURLException me) {
			// Change to logger
			System.out
					.println("Please check "
							+ hubURL
							+ " as it is specified to be the Selenium Hub URL but it's not responding correctly.");
			throw new Exception("Cannot connect to the Grid.");
		}

		driver = new Augmenter().augment(driver);
		return driver;
	}
}
