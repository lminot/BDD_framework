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

import com.ticketmaster.bdd.util.GetPropertyValue;
import com.ticketmaster.bdd.util.TSD_Injector;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class ActiveMonitoringStepDefs 
{
	private List<byte[]> screenGrabs = new ArrayList<byte[]>();
	private WebDriver driver = CommonStepDefs.driver;
	Logger logger = Log.getLogger(ActiveMonitoringStepDefs.class);

	private static final String configLocatorFilePath = "src/test/resources/locators.properties";
	
	private String prodUser = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUsernameCss");
	private String prodPass = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUserPassCss");
	private String prodLogin = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdSubmitCss");
	private String userDrop = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdUserDrop");
	private String signOut = GetPropertyValue.getValueFromPropertyFile(configLocatorFilePath, "ProdSignOut");
	
	private String layout;
  
	@When("^I have logged in with \"(.*?)\" and \"(.*?)\"$")
	public void that_I_have_logged_in_with_and(String username, String password) throws Throwable 
	{	
		Thread.sleep(5000);
		WebElement elementUser = driver.findElement(By.cssSelector(prodUser));
		WebElement elementPass = driver.findElement(By.cssSelector(prodPass));
		WebElement elementSign = driver.findElement(By.cssSelector(prodLogin));
		
		elementUser.sendKeys(username);
		elementPass.sendKeys(password);
		
		elementSign.click();
		TSD_Injector.stepsPassed++;
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
	
	@And("^I attempt to log out$")
	public void i_attempt_to_log_out() throws Throwable 
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
		
		//TODO code for SideNav layout
		
		elementLogDrop.click();
		elementSignOut.click();
		TSD_Injector.stepsPassed++;
	}
	
	@Then("^I am on the login page$")
	public void i_am_on_the_login_page() throws Throwable 
	{
		//embedScreenshot(null);
		TSD_Injector.stepsPassed++;
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
			TSD_Injector.stepsPassed++;
		} catch (Exception e) {
			logger.warn(e.getMessage());
			byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
			screenGrabs.add(screenshot);
			TestCase.assertTrue(false);
		} finally {
			logger.info("Done search");
		}
	}
}