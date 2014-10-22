package com.ticketmaster.bdd.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class GridFactory {

  private Logger logger = Log.getLogger(GridFactory.class);
  private static final String HUB_URL_ALPHA = "http://10.1.210.77:4444/wd/hub";
  private static final String HUB_URL_BETA = "http://10.1.203.143:4444/wd/hub";
  private static final Integer TIMEOUT_SECONDS = 120;

  public GridFactory() {}

  private WebDriver getBrowser(DesiredCapabilities capability) {
    WebDriver driver = null;
    ExecutorService executor = Executors.newCachedThreadPool();
    String hubUrl = HUB_URL_BETA;
    Callable<Object> task = new BrowserCreate(capability, hubUrl);
    Future<Object> future = executor.submit(task);

    try {
      driver = (WebDriver) future.get(GridFactory.TIMEOUT_SECONDS, TimeUnit.SECONDS);
    } catch (TimeoutException ex) {
      logger.warn("Timed out");
    } catch (InterruptedException e) {
      logger.warn(e.getMessage());
    } catch (ExecutionException e) {
      logger.warn(e.getMessage());
    }

    if (driver == null) {
      logger.info("Browser is null, switch to backup Grid. Alerting team.");
      hubUrl = HUB_URL_ALPHA;
      task = new BrowserCreate(capability, hubUrl);
      future = executor.submit(task);

      try {
        driver = (WebDriver) future.get(GridFactory.TIMEOUT_SECONDS, TimeUnit.SECONDS);
      } catch (TimeoutException ex) {
        logger.warn("Timed out");
      } catch (InterruptedException e) {
        logger.warn(e.getMessage());
      } catch (ExecutionException e) {
        logger.warn(e.getMessage());
      }
    }
    driver.manage().window().maximize();
    //driver = new Augmenter().augment(driver);
    return driver;
  }

  public WebDriver getInternetExplorerInstance() throws Exception {

    DesiredCapabilities capability = DesiredCapabilities.internetExplorer();
    capability.setCapability("takeScreenshot", true);
    capability.setPlatform(Platform.WINDOWS);
    capability.setBrowserName("internet explorer");

    capability.setCapability(
        InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
    capability.setCapability("takeScreenshot", true);

    return getBrowser(capability);
  }

  public WebDriver getFirefoxInstance() throws Exception {

    DesiredCapabilities capability = DesiredCapabilities.firefox();
    capability.setCapability("takeScreenshot", true);
    return getBrowser(capability);

  }

  public WebDriver getChromeInstance() throws Exception {

    DesiredCapabilities capability = DesiredCapabilities.chrome();

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--start-maximized");

    capability.setCapability(ChromeOptions.CAPABILITY, options);
    capability.setCapability("takeScreenshot", true);
    return getBrowser(capability);
  }

  public class BrowserCreate implements Callable<Object> {
    private final DesiredCapabilities capability;
    private final String hubUrl;

    public BrowserCreate(DesiredCapabilities capability, String hubUrl) {
      this.capability = capability;
      this.hubUrl = hubUrl;
    }

    public Object call() throws MalformedURLException {
      return new RemoteWebDriver(new URL(hubUrl), capability);
    }

  }
}
