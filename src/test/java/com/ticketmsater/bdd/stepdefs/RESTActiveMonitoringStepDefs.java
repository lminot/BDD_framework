package com.ticketmsater.bdd.stepdefs;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import wslite.http.HTTPResponse;
import junit.framework.Assert;

import com.sun.jersey.api.client.ClientResponse;
import com.ticketmaster.bdd.util.GetPropertyValue;
import com.ticketmaster.bdd.util.RestClient;

import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RESTActiveMonitoringStepDefs 
{
	Logger logger = Log.getLogger(RESTActiveMonitoringStepDefs.class);
	private static Map<String, String> reqHeader = new HashMap<String, String>();
	
	private static final String configConfigFilePath = "src/test/resources/environmental.properties";
	private String baseURL = GetPropertyValue.getValueFromPropertyFile(configConfigFilePath, "prodPage");
	private String userCreds = GetPropertyValue.getValueFromPropertyFile(configConfigFilePath, "luccred");
	
	public static ClientResponse response=null;
	public static HTTPResponse resp = null;
	
	@Given("^I execute a POST to login to tm360$")
	public void i_execute_a_POST_to_the_ticketmaster_login() throws Throwable 
	{
		setHeaders();
		response = RestClient.post(userCreds, reqHeader, "https://" + baseURL + "/login/check-login");
	}
	
	@SuppressWarnings("deprecation")
	@When("^I attempt to logout$")
	public void i_return_to_tickmaster_login() throws Throwable 
	{
		resp = RestClient.get("https://" + baseURL + "/logout", reqHeader);
		int returnedCode = resp.getStatusCode();
		Assert.assertEquals(200, returnedCode);
	}
	
	public void setHeaders()
	{
		reqHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		reqHeader.put("Accept-Encoding", "gzip, deflate");
	}
}
