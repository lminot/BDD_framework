package com.ticketmsater.bdd.stepdefs;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.ticketmaster.bdd.util.GridFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SeleniumSteps {
  private List<byte[]> screenGrabs = new ArrayList<byte[]>();
  private WebDriver driver;
  private String website;

  @Given("^that I have loaded \"([^\"]*)\" in a \"([^\"]*)\"$")
  public void that_I_have_loaded_in_a(String website, String browser) throws Throwable {
    WebDriver driver = null;
    this.website = website;
    if (browser.toLowerCase().equals("firefox")) {
      driver = GridFactory.getFirefoxInstance();
    } else if (browser.toLowerCase().equals("chrome")) {
      driver = GridFactory.getChromeInstance();
    } else if (browser.toLowerCase().equals("phantomjs")) {
      driver = GridFactory.getPhantomJSInstance();
    } else if (browser.toLowerCase().equals("internet explorer")) {
      driver = GridFactory.getInternetExplorerInstance();
    }
    this.driver = new Augmenter().augment(driver);
  }

  @When("^I load a page")
  public void search_for_the_term() throws Exception {
    try {
      this.driver.get("http://" + website);
    } catch (Exception e) {
      byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      screenGrabs.add(screenshot);
      TestCase.assertTrue(false);
    }
  }

  @Then("^search for the term \"([^\"]*)\"$")
  public void search_for_the_term(String arg1) throws Exception {
    try {
      this.driver.get("http://" + website);

      WebElement element = driver.findElement(By.name("q"));
      for (char c : arg1.toCharArray()) {
        element.sendKeys(String.valueOf(c));
      }
      element.submit();
      Thread.sleep(2000);
      byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      screenGrabs.add(screenshot);
    } catch (Exception e) {
      byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
      screenGrabs.add(screenshot);
      TestCase.assertTrue(false);
    }
  }

  @After
  public void embedScreenshot(Scenario scenario) {
   
    for (byte[] screenshot : screenGrabs) {
      scenario.embed(screenshot, "image/png");
    }
   
//    String embedHtml = "<video width='640' height='480' preload='none' controls='controls'><source src='http://10.1.210.52/videos/" + getSessionId() + ".mp4' type='video/mp4; codecs=&quot;theora, vorbis&quot;' autostart='false'></video>";
//    scenario.write(embedHtml);
    this.driver.close();
  }
  
  private String getSessionId(){
    return ((RemoteWebDriver) this.driver).getSessionId().toString();
  }
}
