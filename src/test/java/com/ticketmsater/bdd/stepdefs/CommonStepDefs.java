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
import org.junit.Assert;
import org.openqa.selenium.WebDriver;

import com.google.common.collect.ImmutableMap;
import com.ticketmaster.bdd.util.DriverConfig;
import com.ticketmaster.testclient.TestClient;

//import cucumber.api.PendingException;
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
	
	public static Integer stepsPassed = 0;
		
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
		
		stepsPassed++;
		postBrowserCallTimeToTSD(time, browser);
	}
	
	@And(value = "^I load (?:a|the) page", timeout = 60000) 
	public void search_for_the_term() throws Exception 
	{
		try {
			logger.info("Retrieving webpage");
			driver.get("http://" + website);
			logger.info("Webpage returned");
			stepsPassed++;
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
	
	public void postStepsPassingToTSD(Integer stepsPassed) {
		    long timestamp = System.currentTimeMillis() / 1000;
		    ObjectMapper om = new ObjectMapper();
		    Map<String, Object> metric = new HashMap<String, Object>();
		    metric.put("metric", "chrome.grid.steps.passed");
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

		  private void postBrowserCallTimeToTSD(long millis, String browser) {
		    long timestamp = System.currentTimeMillis() / 1000;

		    ObjectMapper om = new ObjectMapper();
		    Map<String, Object> metric = new HashMap<String, Object>();
		    metric.put("metric", "chrome.grid.browser.instantiation.time");
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
	
	 @After
	  public void embedScreenshot(Scenario scenario) {
	    postStepsPassingToTSD(stepsPassed);
	    for (byte[] screenshot : screenGrabs) {
	      scenario.embed(screenshot, "image/png");
	    }
	    if (driver != null){
	      driver.close();
	      driver.quit();
	    }
	  }
	 
   }
