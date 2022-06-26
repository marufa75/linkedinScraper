package com.example.demo2.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DriverSupplierImpl implements  DriverSupplier {

    @Override
    public Optional<WebElement> findElementByxPath(WebDriver driver, String xPath) {
        WebElement element = driver
                .findElement(By.xpath(xPath));
        if (element==null) {
            return Optional.empty();
        }
        return Optional.of(element);
    }

    @Override
    public Optional<String> findElementByxPathGetText(WebDriver driver, String xPath) {
        Optional<WebElement> optElement = findElementByxPath(driver, xPath);
        return optElement.map(WebElement::getText);
    }
}
