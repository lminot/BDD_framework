package com.ticketmaster.bdd;
import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.Augmenter;

import com.ticketmaster.bdd.util.GridFactory;

public class HubDemo implements Runnable {

	private static final String IMAGE_OUTPUT_LOCATION = "/tmp/";
	
	public static int BROWSER_CHROME = 0;
	public static int BROWSER_FIREFOX = 1;
	public static int BROWSER_INTERNET_EXPLORER = 2;
	public static int BROWSER_PHANTOMJS = 3;

	private int browser = 0;

	public HubDemo(int browser) {
		this.browser = browser;
	}

	public static void main(String[] args) throws Exception {

		// Number of concurrent browsers of each type
		Integer testThreads = 1;
		
		for (int threads = 0; threads < testThreads; threads++) {
			Thread t1 = new Thread(new HubDemo(HubDemo.BROWSER_CHROME));
			t1.start();
//			Thread t2 = new Thread(new HubDemo(HubDemo.BROWSER_FIREFOX));
//			t2.start();
//			Thread t3 = new Thread(new HubDemo(HubDemo.BROWSER_INTERNET_EXPLORER));
//			t3.start();
//			Thread t4 = new Thread(new HubDemo(HubDemo.BROWSER_PHANTOMJS));
//			t4.start();
			Thread.sleep(500);
		}
	}

	private void runInternetExplorer(String screenshot) throws Exception {

		WebDriver driver = GridFactory.getInternetExplorer9Instance();

		driver = new Augmenter().augment(driver);

		driver.get("http://www.google.com");
		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys("Cheese!");
		element.submit();
		Thread.sleep(2000);
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);

		FileUtils
				.copyFile(scrFile, new File( IMAGE_OUTPUT_LOCATION + screenshot + ".png"));
		
		driver.quit();		
	}

	private void runFirefox(String screenshot) throws Exception {

		WebDriver driver = GridFactory.getFirefox18Instance();
		driver = new Augmenter().augment(driver);

		driver.get("http://www.google.com");
		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys("Cheese!");
		element.submit();
		Thread.sleep(2000);
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);
		FileUtils
				.copyFile(scrFile, new File( IMAGE_OUTPUT_LOCATION + screenshot + ".png"));
		
		driver.quit();
	}

	private void runChrome(String screenshot) throws Exception {

		
		WebDriver driver = GridFactory.getChrome27Instance();
		driver = new Augmenter().augment(driver);

		driver.get("http://www.google.com");
		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys("Cheese!");
		element.submit();
		Thread.sleep(3000);
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);

		FileUtils
				.copyFile(scrFile, new File( IMAGE_OUTPUT_LOCATION + screenshot + ".png"));
		
		driver.quit();
	}

	private void runChrome2(String screenshot) throws Exception {

		
		WebDriver driver = GridFactory.getChrome28Instance();
		driver = new Augmenter().augment(driver);

		driver.get("http://www.google.com");
		WebElement element = driver.findElement(By.name("q"));
		element.sendKeys("Cheese!");
		element.submit();
		Thread.sleep(3000);
		File scrFile = ((TakesScreenshot) driver)
				.getScreenshotAs(OutputType.FILE);

		FileUtils
				.copyFile(scrFile, new File( IMAGE_OUTPUT_LOCATION + screenshot + ".png"));
		
		driver.quit();
	}
	
	private void runPhantomJS(String screenshot) throws Exception {

		WebDriver driver = GridFactory.getPhantomJSInstance();

		driver.get("http://www.google.com");
		try{
			WebElement element = driver.findElement(By.name("q"));
			element.sendKeys("Cheese!");
			element.submit();
			Thread.sleep(3000);
			
			File scrFile = ((TakesScreenshot) driver)
					.getScreenshotAs(OutputType.FILE);
	
			FileUtils
				.copyFile(scrFile, new File( IMAGE_OUTPUT_LOCATION + screenshot + ".png"));			
						
			driver.quit();
		}catch(WebDriverException wde){
			System.out.println("retry attempt.");
			runPhantomJS("recovery");
		}
	}

	@Override
	public void run() {
		try {
			if (browser == BROWSER_CHROME) {
				runChrome2("chrome" + System.currentTimeMillis());
			} else if (browser == BROWSER_FIREFOX) {
				runFirefox("firefox" + System.currentTimeMillis());
			} else if (browser == BROWSER_INTERNET_EXPLORER) {
				runInternetExplorer("internet_explorer"
						+ System.currentTimeMillis());
			} else if (browser == BROWSER_PHANTOMJS) {
				runPhantomJS("phantomjs" + System.currentTimeMillis());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
