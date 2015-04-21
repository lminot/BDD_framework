package com.ticketmaster.bdd.util;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.openqa.selenium.WebDriver;

public class CommonUtil {
	private static final Logger log = Logger.getLogger(CommonUtil.class.getName());
	
	public static void sleepForXMinutes(int x) throws Throwable {
        log.info("Sleeping For "+ x +" minute(s)");
        TimeUnit.MINUTES.sleep(x);
        log.info("Waking Up After "+ x +" minute(s)");
    }
	
	public static void killSession(WebDriver driver) throws Throwable {
		if (driver != null){
		      driver.close();
		      driver.quit();
		    }	
	}
	
}
