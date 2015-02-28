package com.ticketmsater.bdd.stepdefs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import wslite.http.HTTPResponse;

import org.junit.Assert;

import com.sun.jersey.api.client.ClientResponse;
import com.ticketmaster.bdd.util.GetPropertyValue;
import com.ticketmaster.bdd.util.RestClient;
import com.ticketmaster.bdd.util.TSD_Injector;

import cucumber.api.java.en.*;


public class RESTActiveMonitoringStepDefs 
{
	Logger logger = Log.getLogger(RESTActiveMonitoringStepDefs.class);
	private static Map<String, String> reqHeader = new HashMap<String, String>();
	
	private static final String configConfigFilePath = "src/test/resources/environmental.properties";
	private String baseURL = GetPropertyValue.getValueFromPropertyFile(configConfigFilePath, "prodPage");
	private String userCreds = GetPropertyValue.getValueFromPropertyFile(configConfigFilePath, "luccred");
	
	public static ClientResponse response=null;
	public static HTTPResponse HTTPresponse = null;
	public long currentTime = 0;
	public long newCurrentTime = 0;
	
	
	@Given("^I execute a POST to login to tm360$")
	public void i_execute_a_POST_to_the_ticketmaster_login() throws Throwable 
	{
		setHeaders();
		currentTime = System.currentTimeMillis();
		response = RestClient.post(userCreds, reqHeader, "https://" + baseURL + "/login/check-login");
		newCurrentTime = System.currentTimeMillis();
		long timeDif = newCurrentTime - currentTime;
		logger.info("response time: "+timeDif);
		TSD_Injector.postResponseTimesToTSD(timeDif);
	}
	
	@When("^I attempt to logout$")
	public void i_return_to_tickmaster_login() throws Throwable 
	{   
		currentTime = System.currentTimeMillis();
		//HTTPresponse = RestClient.get("https://" + baseURL + "/logout", reqHeader);
		response = RestClient.getXML("https://access.ticketmaster.com/logout");
		newCurrentTime = System.currentTimeMillis();
		long timeDif = newCurrentTime - currentTime;
		TSD_Injector.postResponseTimesToTSD(timeDif);
	}
	
	@And("^the response code is success (\\d+) (?:Accepted|OK)$")
	public void successResponseAccepted(int statusCode) throws Throwable 
	{
		int returnedCode = RESTActiveMonitoringStepDefs.response.getStatus();		
	    Assert.assertEquals(statusCode, returnedCode);
	}
	
	public void setHeaders()
	{
		reqHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		reqHeader.put("Accept-Encoding", "gzip, deflate");
	}
}
