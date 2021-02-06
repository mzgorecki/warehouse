package com.demo.warehouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// @formatter:off
		        registry.addMapping("/**")
        	        .allowedOrigins("*")
            	    .allowedHeaders("*")
                	.allowedMethods("*")
                	.allowCredentials(true);
				// @formatter:on
			}
		};
	}
}