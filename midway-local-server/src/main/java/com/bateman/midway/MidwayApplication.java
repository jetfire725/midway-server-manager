package com.bateman.midway;

import java.util.Arrays;

import com.bateman.midway.view.PrimaryView;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.bateman.midway.service.FileProcessor;
import com.bateman.midway.service.IPService;


@SpringBootApplication
public class MidwayApplication {

	public static void main(String[] args) {
		FileProcessor.properties.setProperty("serverPort", "25565");

		//**TOMCAT WEBSERVER CURRENTLY DISABLED IN APPLICATION.PROPERTIES**

		//SET HEADLESS MODE TO FALSE SO WE CAN UTILIZE AWT LIB FOR OPENING LINKS
		SpringApplicationBuilder builder = new SpringApplicationBuilder(MidwayApplication.class);
		builder.headless(false).run(args);

		//OPEN TCP PORT ON STARTUP
		Thread upnpThread = new Thread(()->{
			IPService.enableUPNP();
		});
		upnpThread.start();

		//LAUNCH JAVA FX APPLICATION
		Application.launch(PrimaryView.class, args);

		//CLOSE TCP PORT ON SHUTDOWN
		Runtime.getRuntime().addShutdownHook(new Thread(IPService::disableUPNP));


//
//		SshClient ssh = new SshClient();
//		File script = ssh.createScript();
//		ssh.copyScriptFile(script);
//		ssh.executePortForward();
//


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
