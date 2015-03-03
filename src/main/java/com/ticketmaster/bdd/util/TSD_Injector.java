package com.ticketmaster.bdd.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.google.common.collect.ImmutableMap;
import com.ticketmaster.testclient.TestClient;

public class TSD_Injector {
	
	static Logger logger = Log.getLogger(TSD_Injector.class);
	public static int stepsPassed = 0;
	public long currentTime = 0;
	public long newCurrentTime = 0;
	
	public static void postStepsPassingToTSD(Integer stepsPassed) {
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
	      logger.info("TSD json:"+json);
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

	  public static void postBrowserCallTimeToTSD(long millis, String browser) {
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
	      logger.info("TSD json:"+json);
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
	  
	  public static void postResponseTimesToTSD(long millis) {
		    long timestamp = System.currentTimeMillis() / 1000;

		    ObjectMapper om = new ObjectMapper();
		    Map<String, Object> metric = new HashMap<String, Object>();
		    metric.put("metric", "chrome.grid.response.time");
		    metric.put("timestamp", timestamp);
		    metric.put("value", millis);
		    metric.put("tags", ImmutableMap.of("host", "access.ticketmaster.com"));
		    
		    String json;
		    try {
		      json = om.writeValueAsString(metric);
		      logger.info("TSD json:"+json);
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

}
