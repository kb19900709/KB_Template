package com.kb.system.web.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Base spring container 設定
 * 
 * @author KB
 * @version 1.0
 */
@Configuration
@ComponentScan(basePackages = "com.kb", excludeFilters = @Filter(type = FilterType.ANNOTATION, value = EnableWebMvc.class))
@PropertySource("classpath:com/kb/system/env.properties")
@Import({ DataSourceConfig.class })
public class SpringConfig {

}
