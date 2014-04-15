package com.ticketmaster.bdd.acceptance;

import org.junit.runner.RunWith;

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
		format={"pretty", "html:target/results"},
		features="src/test/resources/com/ticketmaster/bdd/acceptance",
		glue={"com.ticketmsater.bdd.stepdefs"},
		tags={"@smoke"}
		)
public class TestRunner {

}
