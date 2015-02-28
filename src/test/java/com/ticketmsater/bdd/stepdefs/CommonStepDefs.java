package com.ticketmsater.bdd.stepdefs;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.openqa.selenium.WebDriver;


import com.ticketmaster.bdd.util.DriverConfig;
import com.ticketmaster.bdd.util.TSD_Injector;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
//import cucumber.api.java.en.Then;
//import cucumber.api.java.en.When;

public class CommonStepDefs 
{
	Logger logger = Log.getLogger(CommonStepDefs.class);
	private static DriverConfig driverConfig = new DriverConfig();
	private List<byte[]> screenGrabs = new ArrayList<byte[]>();
	
	public static WebDriver driver;
	public static String website;
	
	//public static Integer stepsPassed = 0;
		
	@Given("^that I have (?:loaded|navigated to) \"([^\"]*)\" in \"([^\"]*)\"$")
	public void that_I_have_loaded_in_a(String website, String browser) throws Throwable 
	{
		CommonStepDefs.website = website;
		logger.info("Getting a new browser");
		long current = System.currentTimeMillis();
		logger.info("Current time: " + current);

		driver = driverConfig.driverConfig(driver, browser);
		
		//CommonStepDefs.driver = new Augmenter().augment(driver);
		long current2 = System.currentTimeMillis();
		logger.info("Current time: " + current2);
		
	    long time = current2 - current;
		
		TSD_Injector.stepsPassed++;
		TSD_Injector.postBrowserCallTimeToTSD(time, browser);
	}
	
	@And(value = "^I load (?:a|the) page", timeout = 60000) 
	public void search_for_the_term() throws Exception 
	{
		try {
			logger.info("Retrieving webpage");
			driver.get("http://" + website);
			logger.info("Webpage returned");
			TSD_Injector.stepsPassed++;
		} catch (Exception e) {
			TestCase.assertTrue(false);
			logger.info("Webpage failed");
		}
	}
		
	 @After
	  public void embedScreenshot(Scenario scenario) throws Exception {
	    TSD_Injector.postStepsPassingToTSD(TSD_Injector.stepsPassed);
	    for (byte[] screenshot : screenGrabs) {
	      scenario.embed(screenshot, "image/png");
	    }
	    if (driver != null){
	      driver.close();
	      driver.quit();
	    }
	  }
	 
   }
