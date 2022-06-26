package com.example.demo2.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


public interface ConfigManager {

     String getLinkedinEmail();
     String getLinkedinPassword();

}

