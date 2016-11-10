package com.kb.system.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * Spring MVC 設定
 * 
 * @author KB
 * @version 1.0
 */
@Configuration
@EnableWebMvc
@ComponentScan("com.kb.controller")
public class SpringMvcConfig extends WebMvcConfigurerAdapter {

	// 開啟靜態資源處理
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	// 視圖解析器設定
	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver result = new InternalResourceViewResolver();
		result.setPrefix("resource/app/");
		result.setSuffix(".html");
		return result;
	}
}
