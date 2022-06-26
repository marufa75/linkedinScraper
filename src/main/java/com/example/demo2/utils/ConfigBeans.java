package com.example.demo2.utils;

import com.example.demo2.selenium.ChromeScraper;
import com.example.demo2.selenium.ChromeScraperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;

@Configuration
@PropertySource("classpath:application.properties")
public class ConfigBeans {

    @Bean
    @Scope("prototype")
    public ChromeScraper chromeScraper() {
        return new ChromeScraperImpl();
    }
}

