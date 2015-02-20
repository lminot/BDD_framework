package com.ticketmaster.bdd.runner;

import org.junit.AfterClass;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import com.ticketmsater.bdd.stepdefs.CommonStepDefs;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

/**
 * This class defines the glue that connects the feature files, the java Step Definitions and the configurations for the Cucumber
 * Tags and Output format
 * 
 * @author eric.lee
 *
 */

@RunWith(Cucumber.class)
@CucumberOptions(
		format={"pretty", "html:target/results", "json:target/results.json"},
		features="src/test/resources/com/ticketmaster/bdd/features",
		glue={"com.ticketmsater.bdd.stepdefs"},
		tags={"@tm360-login"}
		)
		//@tm360-login, @am-rest-login
public class TestRunner {
}
