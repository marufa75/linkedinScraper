package com.example.demo2.scraper;

import com.example.demo2.company.model.Company;

import java.util.List;


public interface ScrapeAccountService {

    List<Company> scrapeAccountsByUrl(List<String> urls);


}
