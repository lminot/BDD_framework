package com.ticketmsater.bdd.stepdefs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import com.ticketmaster.bdd.util.GridFactory;
import com.ticketmaster.bdd.util.LocalBrowser;
import com.ticketmaster.testclient.TestClient;
import com.ticketmaster.bdd.util.GetPropertyValue;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ActiveMonitoringStepDefs 
{
	private static GridFactory gridFactory = new GridFactory();
	private static LocalBrowser localBrowser = new LocalBrowser();
	
	private List<byte[]> screenGrabs = new ArrayList<byte[]>();
	private WebDriver driver;
	private String website;
	Logger logger = Log.getLogger(ActiveMonitoringStepDefs.class);

	private static final String configLocatorFilePath = "src/test/resources/locators.properties";
	private static final String configEnvironmentFilePath = "src/test/resources/environmental.properties";
	private static final String configPropertyFilePath = "src/test/resources/config.properties";
	
	private String driverLocation = GetPropertyValue.getValueFromPropertyFile(configPropertyFilePath, "location");
	private String prodUser = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUsernameCss");
	private String prodPass = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUserPassCss");
	private String prodLogin = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdSubmitCss");
	private String userDrop = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUserDrop");
	private String signOut = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdSignOut");
	
	private Integer stepsPassed = 0;
	private String layout;

	@Given("^that I have loaded \"([^\"]*)\" in \"([^\"]*)\"$")
	public void that_I_have_loaded_in_a(String website, String browser) throws Throwable 
	{
		logger.info("Getting a new browser");
		this.website = website;
		long current = System.currentTimeMillis();
		logger.info("Current time: " + current);

		if (browser.toLowerCase().equals("firefox")) 
		{
			if(driverLocation.matches("grid"))
				this.driver = gridFactory.getFirefoxInstance();
			else if(driverLocation.matches("local"))
				this.driver = localBrowser.getFirefoxInstance();
			
			logger.info("Returning instance of a firefox browser");
		} 
		else if (browser.toLowerCase().equals("chrome")) 
		{
			if(driverLocation.matches("grid"))
				this.driver = gridFactory.getChromeInstance();
			else if(driverLocation.matches("local"))
				this.driver = localBrowser.getChromeInstance();
			
			logger.info("Returning instance of a chrome browser");
		} 
		else if (browser.toLowerCase().equals("ie")) 
		{
			if(driverLocation.matches("grid"))
				this.driver = gridFactory.getInternetExplorerInstance();
			else if(driverLocation.matches("local"))
				this.driver = localBrowser.getInternetExplorerInstance();
			
			logger.info("Returning instance of a internet explorer browser");
		}
		
		this.driver = new Augmenter().augment(driver);
		long current2 = System.currentTimeMillis();
		logger.info("Current time: " + current2);
//		long time = current2 - current;
//    	postBrowserCallTimeToTSD(time, browser);
		stepsPassed++;
	}

	@And(value = "^I load a page", timeout = 60000)
	public void search_for_the_term() throws Exception 
	{
		String page = "www.google.com"; //page defaulted to
		
		if(website.matches("prodPage"))
			page = GetPropertyValue.getValueFromPropertyFile(configEnvironmentFilePath, "prodPage");
			
		try {
			logger.info("Retrieving webpage");
			this.driver.get("http://" + page);
			logger.info("Webpage returned");
			stepsPassed++;
		} catch (Exception e) {
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			screenGrabs.add(screenshot);
			TestCase.assertTrue(false);
			logger.info("Webpage failed");
		}
	}
  
	@When("^I have logged in with \"(.*?)\" and \"(.*?)\"$")
	public void that_I_have_logged_in_with_and(String username, String password) throws Throwable 
	{	
		WebElement elementUser = driver.findElement(By.cssSelector(prodUser));
		WebElement elementPass = driver.findElement(By.cssSelector(prodPass));
		WebElement elementSign = driver.findElement(By.cssSelector(prodLogin));
		
		elementUser.sendKeys(username);
		elementPass.sendKeys(password);
		
		elementSign.click();
		/*
		 * Check for layout format to determine future functionality
		 */
		try {
			driver.findElement(By.cssSelector("#navigation-items"));
			layout = "sideNav";
		} catch (NoSuchElementException e)
		{
			logger.warn(e.getMessage());
		}
		try {
			driver.findElement(By.cssSelector("#portal-nav"));
			layout = "topNav";
		} catch (NoSuchElementException e)
		{
			logger.warn(e.getMessage());
		}
	}
	
	@Then("^I am on the login page$")
	public void i_am_on_the_login_page() throws Throwable 
	{
		WebDriverWait wait = new WebDriverWait(driver, 30);
		WebElement elementLogDrop = null;
		WebElement elementSignOut = null;

		if( layout.matches("topNav") )
		{
			elementLogDrop = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(userDrop)));
			
			elementLogDrop = driver.findElement(By.xpath(userDrop));
			elementSignOut = driver.findElement(By.xpath(signOut));
		}
		
//		Code me!
//		else if( layout.matches("sideNav") ) {}
		
		elementLogDrop.click();
		elementSignOut.click();
	}

	@Then(value = "^search for the term \"([^\"]*)\"$", timeout = 60000)
	public void search_for_the_term(String arg1) throws Exception 
	{
		try {
			logger.info("Submitting search " + arg1);
			WebDriverWait wait = new WebDriverWait(driver, 60);
			WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
			for (char c : arg1.toCharArray()) {
				element.sendKeys(String.valueOf(c));
			}
			element.submit();
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pages")));
			logger.info("Search submitted");
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			screenGrabs.add(screenshot);
			stepsPassed++;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			screenGrabs.add(screenshot);
			TestCase.assertTrue(false);
		} finally {
			logger.info("Done search");
		}
	}

	private void postStepsPassingToTSD(Integer stepsPassed) 
	{
		long timestamp = System.currentTimeMillis() / 1000;
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> metric = new HashMap<String, Object>();
		metric.put("metric", "beta.grid.steps.passed");
		metric.put("timestamp", timestamp);
		metric.put("value", stepsPassed);
		metric.put("tags", ImmutableMap.of("host", "selenium.grid.beta"));

		String json;
		try {
			json = om.writeValueAsString(metric);
			logger.info(json);
			TestClient.post("http://tsd.dev.cloudsys.tmcs/api/put", json, "application/json");
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void postBrowserCallTimeToTSD(long millis, String browser) 
	{
		long timestamp = System.currentTimeMillis() / 1000;

		ObjectMapper om = new ObjectMapper();
		Map<String, Object> metric = new HashMap<String, Object>();
		metric.put("metric", "beta.grid.browser.instantiation.time");
		metric.put("timestamp", timestamp);
		metric.put("value", millis);
		metric.put("tags", ImmutableMap.of("host", "selenium.grid.beta"));
		metric.put("tags", ImmutableMap.of("browser", browser.replaceAll("\\s+", ".")));

		String json;
		try {
			json = om.writeValueAsString(metric);
			logger.info(json);
			TestClient.post("http://tsd.dev.cloudsys.tmcs/api/put", json, "application/json");
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void embedScreenshot(Scenario scenario) 
	{
//    	postStepsPassingToTSD(stepsPassed);
		for (byte[] screenshot : screenGrabs) {
			scenario.embed(screenshot, "image/png");
		}
		if (driver != null)
			this.driver.close();
		this.driver.quit();
	}
}