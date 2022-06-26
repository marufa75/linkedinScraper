package com.example.demo2.selenium;

import com.example.demo2.company.model.Company;

import java.util.Optional;


public interface LinkedInScraper {
    void shutDownDriver();
    boolean login(String email, String password);
    boolean moveToURL(String postURL);
    Optional<Company> scrapeCompanyAndSave(String url);

}
