package com.example.demo2.controller;

import com.example.demo2.company.model.Company;
import com.example.demo2.scraper.ScrapeAccountService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/scrape")
@AllArgsConstructor
public class ScrapeAccountController {
    ScrapeAccountService scrapeAccountService;


    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public List<Company> scrapeAccount(@RequestBody @Validated URLSWrapper data) {
        List<String> urls = data.getUrls();
        if (urls.stream().anyMatch(el -> el.contains("/"))) {
            throw new IllegalStateException("the url should be the name part: like verveventures");
        }
        return scrapeAccountService.scrapeAccountsByUrl(urls.stream().map(el -> "https://www.linkedin.com/company/".concat(el + "/")).collect(Collectors.toList()));
    }


}
