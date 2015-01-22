package com.ticketmsater.bdd.stepdefs;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ticketmaster.bdd.util.CommonProperties;
import com.ticketmaster.bdd.util.GetPropertyValue;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ActiveMonitoringStepDefs 
{	
	private List<byte[]> screenGrabs = new ArrayList<byte[]>();
	
	Logger logger = Log.getLogger(ActiveMonitoringStepDefs.class);
	CommonProperties comProp = new CommonProperties();
//	CommonStepDefs common = new CommonStepDefs();

	private static final String configLocatorFilePath = "src/test/resources/locators.properties";
	private static final String configEnvironmentFilePath = "src/test/resources/environmental.properties";

	private String environment = GetPropertyValue.getValueFromPropertyFile(configEnvironmentFilePath, "env");
	private String prodUser = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUsernameCss");
	private String prodPass = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUserPassCss");
	private String prodLogin = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdSubmitCss");
	private String userDrop = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUserDrop");
	private String signOut = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdSignOut");
	
	private WebDriver driver;
	private String layout;
	
	@And(value = "^I load a page", timeout = 60000)
	public void search_for_the_term() throws Exception 
	{
		String page= null;
		this.driver = comProp.getDriver();
		
		if(environment.matches("prodPage"))
			page = GetPropertyValue.getValueFromPropertyFile(configEnvironmentFilePath, "prodPage");
			
		try {
			logger.info("Retrieving webpage");
			this.driver.get("http://" + page);
			logger.info("Webpage returned");
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
	
//	public void setDriver(WebDriver driver)
//	{
//		this.driver = driver;
//	}
}