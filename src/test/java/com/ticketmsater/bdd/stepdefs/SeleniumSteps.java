package com.ticketmsater.bdd.stepdefs;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

import com.ticketmaster.bdd.util.GridFactory;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class SeleniumSteps {
	private List<byte[]> screenGrabs = new ArrayList<byte[]>();
	private WebDriver driver;
	
	@Given("^that I have loaded ticketmaster.com in a \"([^\"]*)\"$")
	public void that_I_have_loaded_ticketmaster_com_in_a(String browser) throws Throwable {
	    
		WebDriver driver = null; 
		if(browser.toLowerCase().equals("firefox")){
			driver = GridFactory.getFirefoxInstance();
		}else if(browser.toLowerCase().equals("chrome")){
			driver = GridFactory.getChromeInstance();
		}
		this.driver = new Augmenter().augment(driver);
	}

	@When("^search for the term \"([^\"]*)\"$")
	public void search_for_the_term(String arg1) throws Exception {
		driver.get("http://www.ticketmaster.com/");
		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys(arg1);
		element.submit();
		Thread.sleep(5000);
		byte[] screenshot = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.BYTES);
		screenGrabs.add(screenshot);
	}

	@Then("^I should get a page that looks like this$")
	public void I_should_get_a_page_that_looks_like_this() {
		driver.close();
		driver.quit();
	}
	
	@After
	public void embedScreenshot(Scenario scenario) {
	    for(byte[] screenshot : screenGrabs){
	        scenario.embed(screenshot, "image/png");
	    }
	}
}
