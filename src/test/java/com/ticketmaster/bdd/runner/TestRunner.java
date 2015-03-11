package com.ticketmaster.bdd.runner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(
		format={"pretty", "html:target/results", "json:target/results.json"},
		features="src/test/resources/com/ticketmaster/bdd",
		glue={"com.ticketmaster.bdd"},
		tags={"@am-rest-login"}
		)
		//@tm360-login, @am-rest-login
public class TestRunner {
}
