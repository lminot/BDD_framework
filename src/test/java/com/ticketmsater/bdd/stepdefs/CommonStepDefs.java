package com.ticketmsater.bdd.stepdefs;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import com.ticketmaster.bdd.util.DriverConfig;

import cucumber.api.PendingException;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class CommonStepDefs 
{
	Logger logger = Log.getLogger(CommonStepDefs.class);
	private static DriverConfig driverConfig = new DriverConfig();
	private List<byte[]> screenGrabs = new ArrayList<byte[]>();
	
	public static WebDriver driver;
	public static String website;
		
	@Given("^that I have (?:loaded|navigated to) \"([^\"]*)\" in \"([^\"]*)\"$")
	public void that_I_have_loaded_in_a(String website, String browser) throws Throwable 
	{
		CommonStepDefs.website = website;
		logger.info("Getting a new browser");
		long current = System.currentTimeMillis();
		logger.info("Current time: " + current);

		this.driver = driverConfig.driverConfig(driver, browser);
		
		//CommonStepDefs.driver = new Augmenter().augment(driver);
		long current2 = System.currentTimeMillis();
		logger.info("Current time: " + current2);
	}
	
	@And(value = "^I load (?:a|the) page", timeout = 60000) 
	public void search_for_the_term() throws Exception 
	{
		try {
			logger.info("Retrieving webpage");
			this.driver.get("http://" + website);
			logger.info("Webpage returned");
		} catch (Exception e) {
			TestCase.assertTrue(false);
			logger.info("Webpage failed");
		}
	}
	
	@And("^the response code is success (\\d+) (?:Accepted|OK)$")
	public void successResponseAccepted(int statusCode) throws Throwable 
	{
		int returnedCode = RESTActiveMonitoringStepDefs.response.getStatus();		
	    Assert.assertEquals(statusCode, returnedCode);
	}
	
	 @After
	  public void embedScreenshot(Scenario scenario) {
	   // postStepsPassingToTSD(stepsPassed);
	    for (byte[] screenshot : screenGrabs) {
	      scenario.embed(screenshot, "image/png");
	    }
	    if (driver != null){
	      this.driver.close();
	      this.driver.quit();
	    }
	  }
   }
