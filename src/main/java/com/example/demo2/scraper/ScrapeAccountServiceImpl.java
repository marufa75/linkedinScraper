package com.example.demo2.scraper;

import com.example.demo2.company.model.Company;
import com.example.demo2.selenium.LinkedInScraper;
import com.example.demo2.utils.ConfigManager;
import lombok.AllArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

@Service
@AllArgsConstructor
@Slf4j
public class ScrapeAccountServiceImpl implements ScrapeAccountService {

    private final ConfigManager configManager;


    private final LinkedInScraper linkedInScraper;

    @Synchronized
    public List<Company> scrapeAccountsByUrl(List<String> urls) {
        if (linkedInScraper.login(configManager.getLinkedinEmail(), configManager.getLinkedinPassword())) {
            List<Company> companies = urls.stream().map(this::scrapeAccountByUrl).filter((Optional::isPresent)).map(Optional::get).collect(Collectors.toList());
            log.info("downloaded {}", companies.size());
            linkedInScraper.shutDownDriver();
            return companies;
        }

        return Collections.emptyList();
    }

    private Optional<Company> scrapeAccountByUrl(String url) {
        log.info("...... SCRAPING {}", url);
        if (linkedInScraper.moveToURL(url)) {
            return linkedInScraper.scrapeCompanyAndSave(url);
        }

        return Optional.empty();
    }

    @PostConstruct
    public void test() {

        List<Company> companies = scrapeAccountsByUrl(asList("https://www.linkedin.com/company/evenito/", "https://www.linkedin.com/company/qumea/"));

    }
}
