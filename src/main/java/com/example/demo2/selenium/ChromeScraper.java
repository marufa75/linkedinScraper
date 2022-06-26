package com.example.demo2.selenium;

import org.openqa.selenium.WebDriver;

public interface ChromeScraper {
    WebDriver getDriver();
    void shutDownDriver();
    boolean moveToURL(String postURL, String xPathValid, String xPathError);
}
