package com.ewha.back.global.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
			// .allowedOrigins("*")
			.allowedOrigins("http://localhost:3000")
			.allowedMethods("*")
			.exposedHeaders("Authorization")
			.allowCredentials(true)
			.maxAge(3000);
	}
}
