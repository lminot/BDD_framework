package com.ticketmsater.bdd.stepdefs;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;

import com.ticketmaster.bdd.util.CommonProperties;
import com.ticketmaster.bdd.util.DriverConfig;

import cucumber.api.java.en.Given;

public class CommonStepDefs 
{
	Logger logger = Log.getLogger(CommonStepDefs.class);
	CommonProperties comProp = new CommonProperties();	//Practice: Only common step defs should write to common properties
	ActiveMonitoringStepDefs aMSD = new ActiveMonitoringStepDefs();
	
	public WebDriver driver;
	private static DriverConfig driverConfig = new DriverConfig();
		
	@Given("^that I have loaded \"([^\"]*)\" in \"([^\"]*)\"$")
	public void that_I_have_loaded_in_a(String website, String browser) throws Throwable 
	{
		logger.info("Getting a new browser");
		long current = System.currentTimeMillis();
		logger.info("Current time: " + current);

		this.driver = driverConfig.driverConfig(driver, browser);
		comProp.setDriver(this.driver);
		
		this.driver = new Augmenter().augment(driver);
		long current2 = System.currentTimeMillis();
		logger.info("Current time: " + current2);
	}
}
