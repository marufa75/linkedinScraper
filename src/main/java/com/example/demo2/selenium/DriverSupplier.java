package com.example.demo2.selenium;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Optional;

public interface DriverSupplier {
    Optional<WebElement> findElementByxPath(WebDriver driver, String xPath);
    Optional<String> findElementByxPathGetText(WebDriver driver, String xPath);
}
