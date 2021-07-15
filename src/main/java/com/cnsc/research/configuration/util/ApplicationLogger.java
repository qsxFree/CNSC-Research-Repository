package com.cnsc.research.configuration.util;

import com.cnsc.research.ResearchRepositoryApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationLogger {
    @Bean
    public Logger getLogger(){
        return LoggerFactory.getLogger(ResearchRepositoryApplication.class);
    }
}
