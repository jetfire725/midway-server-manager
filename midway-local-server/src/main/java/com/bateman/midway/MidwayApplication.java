package com.bateman.midway;

import java.io.File;
import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.bateman.midway.service.FileProcessor;
import com.bateman.midway.service.IPService;
import com.bateman.midway.service.SshClient;

@SpringBootApplication
public class MidwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MidwayApplication.class, args);
			IPService.updateExternalIp();
			//FileProcessor.setPropertyPath("src/main/resources/server.properties");
//			System.out.println(FileProcessor.propertiesToJson().toString());

//			SshClient ssh = new SshClient();
//			File script = ssh.createScript();
//			ssh.copyScriptFile(script);
//			ssh.executePortForward();
		//fp.setServerProperty("server-ip", "123.456.678.345");
	}
	
	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));
		corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
				"Accept", "Authorization", "Origin, Accept", "X-Requested-With",
				"Access-Control-Request-Method", "Access-Control-Request-Headers"));
		corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
				"Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
		corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
