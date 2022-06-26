package com.example.demo2.selenium;

import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


@Slf4j
public class ChromeScraperImpl implements ChromeScraper {
    private  WebDriver driver;


    public ChromeScraperImpl() {
        driver = new ChromeDriver(getBrowserConfig());
    }

    public WebDriver getDriver() {
        if (driver==null) {
            driver = new ChromeDriver(getBrowserConfig());
        }
        return driver;
    }

    private ChromeOptions getBrowserConfig() {
        log.info("Start Scraper.....................................................[Success]");
        String osName = System.getProperty("os.name").toUpperCase();
        if (osName.contains("WINDOWS")) {
            System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        } else {
            System.setProperty("webdriver.chrome.driver", "chromedriver");
        }
        // configurations
        ChromeOptions browserConfig = new ChromeOptions();
			browserConfig.addArguments("--headless");
        browserConfig.addArguments("--incognito");
        browserConfig.addArguments("--max_old_space_size=4096");
        browserConfig.setPageLoadStrategy(PageLoadStrategy.EAGER);
        return browserConfig;
    }

    public void shutDownDriver() {
        driver.quit();
        driver=null;
    }

    public boolean moveToURL(String postURL, String xPathValid, String xPathError) {
        boolean isFound = false;
        try {
            log.info("Opeaning Post URL");
            driver.get(postURL);

            // wait till post loads up
            new WebDriverWait(driver, Duration.ofSeconds(60)).until(ExpectedConditions
                    .visibilityOfElementLocated(By.xpath(xPathValid)));

            // check for possible error
            if (!StringUtil.isNullOrEmpty(xPathError) && driver.findElements(By.xpath(xPathError)).size() > 0) {
                log.info("Error opening Post URL");
            }

            isFound = true;

        } catch (Exception e) {
            log.info("Error opening Post URL");
            e.printStackTrace();
        }
        return isFound;
    }

}
