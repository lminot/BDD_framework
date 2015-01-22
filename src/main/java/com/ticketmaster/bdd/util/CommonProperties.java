package com.ticketmaster.bdd.util;

import org.openqa.selenium.WebDriver;

public class CommonProperties 
{	
	private WebDriver driver;
	
	public void setDriver(WebDriver driver)
	{
		this.driver = driver;
	}
	
	public WebDriver getDriver()
	{
		return driver;
	}
}
