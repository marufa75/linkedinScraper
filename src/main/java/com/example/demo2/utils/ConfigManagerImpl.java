package com.example.demo2.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class ConfigManagerImpl implements  ConfigManager{
    @Value("${linkedin.email}")
    private String linkedinEmail;
    @Value("${linkedin.password}")
    private String linkedinPassword;

    public String getLinkedinEmail() {
        return linkedinEmail;
    }

    public String getLinkedinPassword() {
        return linkedinPassword;
    }

}

