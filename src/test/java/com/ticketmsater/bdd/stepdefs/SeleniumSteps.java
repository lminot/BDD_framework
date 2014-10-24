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
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.ImmutableMap;
import com.ticketmaster.bdd.util.GridFactory;
import com.ticketmaster.testclient.TestClient;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SeleniumSteps {
  private List<byte[]> screenGrabs = new ArrayList<byte[]>();
  private WebDriver driver;
  private String website;
  Logger logger = Log.getLogger(SeleniumSteps.class);
  private GridFactory gridFactory = new GridFactory();

  private Integer stepsPassed = 0;

  @Given("^that I have loaded \"([^\"]*)\" in a \"([^\"]*)\"$")
  public void that_I_have_loaded_in_a(String website, String browser) throws Throwable {
    logger.info("Getting a new browser");    
    this.website = website;

    long current = System.currentTimeMillis();
    logger.info("Current time: " + current);

    if (browser.toLowerCase().equals("firefox")) {
      this.driver = gridFactory.getFirefoxInstance();
      logger.info("Returning instance of a firefox browser");
    } else if (browser.toLowerCase().equals("chrome")) {
      this.driver = gridFactory.getChromeInstance();
      logger.info("Returning instance of a chrome browser");
    } else if (browser.toLowerCase().equals("internet explorer")) {
      this.driver = gridFactory.getInternetExplorerInstance();
      logger.info("Returning instance of a internet explorer browser");
    }
    this.driver = new Augmenter().augment(driver);
    long current2 = System.currentTimeMillis();
    logger.info("Current time: " + current2);
    long time = current2 - current;

    postBrowserCallTimeToTSD(time, browser);
    stepsPassed++;
  }

  @When(value = "^I load a page", timeout = 30000)
  public void search_for_the_term() throws Exception {
    try {
      logger.info("Retrieving webpage");
      this.driver.get("http://" + website);
      logger.info("Webpage returned");
      stepsPassed++;
    } catch (Exception e) {
      byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      screenGrabs.add(screenshot);
      TestCase.assertTrue(false);
      logger.info("Webpage failed");
    }
  }

  @Then(value = "^search for the term \"([^\"]*)\"$", timeout = 30000)
  public void search_for_the_term(String arg1) throws Exception {
    try {
      logger.info("Submitting search " + arg1);
      WebDriverWait wait = new WebDriverWait(driver, 60);
      WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("q")));
      //element = driver.findElement(By.name("q"));
      for (char c : arg1.toCharArray()) {
        element.sendKeys(String.valueOf(c));
      }
      element.submit();
      Thread.sleep(4000);
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

  private void postStepsPassingToTSD(Integer stepsPassed) {
    long timestamp = System.currentTimeMillis() / 1000;
    ObjectMapper om = new ObjectMapper();
    Map<String, Object> metric = new HashMap<String, Object>();
    metric.put("metric", "alpha.grid.steps.passed");
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
    metric.put("metric", "alpha.grid.browser.instantiation.time");
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
    if (driver != null)
      this.driver.close();
      this.driver.quit();
  }

}
