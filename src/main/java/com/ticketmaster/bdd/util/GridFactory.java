package com.ticketmaster.bdd.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;


public class GridFactory
{

	private static final String configPropertyFilePath = "src/test/resources/config.properties";
	private Logger logger = Log.getLogger(GridFactory.class);
	
	private static final String HUB_URL_PRIMARY = GetPropertyValue.getValueFromPropertyFile(configPropertyFilePath, "local");
	private static final String HUB_URL_SECONDARY = System.getProperty(configPropertyFilePath, "grid");
	
	
	private static final Integer TIMEOUT_SECONDS = 120;

	public GridFactory()
	{
	}

	private WebDriver getBrowser(DesiredCapabilities capability)
	{
		System.out.println(HUB_URL_PRIMARY);
		WebDriver driver = null;
		ExecutorService executor = Executors.newCachedThreadPool();
		String hubUrl = HUB_URL_PRIMARY;
		Callable<Object> task = new BrowserCreate(capability, hubUrl);
		Future<Object> future = executor.submit(task);

		try
		{
			driver = (WebDriver) future.get(GridFactory.TIMEOUT_SECONDS, TimeUnit.SECONDS);
		}
		catch (TimeoutException ex)
		{
			logger.warn("Timed out");
		}
		catch (InterruptedException e)
		{
			logger.warn(e.getMessage());
		}
		catch (ExecutionException e)
		{
			logger.warn(e.getMessage());
		}

		if (driver == null)
		{
			logger.info("Browser is null, switch to backup Grid. Alerting team.");
			hubUrl = HUB_URL_SECONDARY;
			task = new BrowserCreate(capability, hubUrl);
			future = executor.submit(task);

			try
			{
				driver = (WebDriver) future.get(GridFactory.TIMEOUT_SECONDS, TimeUnit.SECONDS);
			}
			catch (TimeoutException ex)
			{
				logger.warn("Timed out");
			}
			catch (InterruptedException e)
			{
				logger.warn(e.getMessage());
			}
			catch (ExecutionException e)
			{
				logger.warn(e.getMessage());
			}
		}
		driver.manage().window().maximize();
		return driver;
	}

	public WebDriver getInternetExplorerInstance() throws Exception
	{

		DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
		capability.setCapability("takeScreenshot", true);
		capability.setPlatform(Platform.WINDOWS);
		capability.setBrowserName("internet explorer");

		capability.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		capability.setCapability("takeScreenshot", true);

		return getBrowser(capability);
	}

	public WebDriver getFirefoxInstance() throws Exception
	{

		return getFirefoxInstance(null);

	}
	
	/**
	 * Return a WebDriver for FireFox with pProfile
	 * If the pProfile is null set the capability with a new empty Profile
	 * @param pProfile (FirefoxProfile)
	 * @return (WebDriver)
	 * @throws Exception
	 * @author Danny.Paradis
	 */
	public WebDriver getFirefoxInstance(FirefoxProfile pProfile) throws Exception
	{
		FirefoxProfile profile = new FirefoxProfile();
		if (pProfile != null)
		{
			profile = pProfile;
		}
		DesiredCapabilities capability = DesiredCapabilities.firefox();
		capability.setCapability("takeScreenshot", true);
		capability.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true); //TODO DPA move to default capability
		capability.setCapability(FirefoxDriver.PROFILE, profile);
		return getBrowser(capability);

	}

	public WebDriver getChromeInstance() throws Exception
	{
		return getChromeInstance(null);
	}

	/**
	 * Return a WebDriver for Chrome with pOption
	 * If pOtions is null set the capability with a new empty Options
	 * @param pOtions (ChromeOptions)
	 * @return (WebDriver)
	 * @throws Exception
	 * @author Danny.Paradis
	 */
	public WebDriver getChromeInstance(ChromeOptions pOtions) throws Exception
	{
		DesiredCapabilities capability = DesiredCapabilities.chrome();
		ChromeOptions options = new ChromeOptions();
		if (pOtions != null)
		{
			options = pOtions;
		}
				
		options.addArguments("--start-maximized");

		capability.setCapability(ChromeOptions.CAPABILITY, options);
		capability.setCapability("takeScreenshot", true);
		return getBrowser(capability);
	}

	public WebDriver getPhantomJSInstance() throws Exception
	{
		DesiredCapabilities capability = DesiredCapabilities.phantomjs();
		return getBrowser(capability);
	}

	public class BrowserCreate implements Callable<Object>
	{
		private final DesiredCapabilities capability;
		private final String hubUrl;

		public BrowserCreate(DesiredCapabilities capability, String hubUrl)
		{
			this.capability = capability;
			this.hubUrl = hubUrl;
		}

		public Object call() throws MalformedURLException
		{
			return new RemoteWebDriver(new URL(hubUrl), capability);
		}

	}
}
