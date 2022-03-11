package com.bateman.midway;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import com.bateman.midway.service.MojangServerClient;
import com.bateman.midway.view.ViewController;
import com.dosse.upnp.UPnP;
import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.bateman.midway.service.FileProcessor;
import com.bateman.midway.service.IPService;
import com.bateman.midway.service.SshClient;

import static java.lang.Integer.parseInt;

@SpringBootApplication
public class MidwayApplication {

	public static void main(String[] args) {
		FileProcessor.properties.setProperty("serverPort", "25565");

		//**WEBSERVER CURRENTLY DISABLED IN APPLICATION.PROPERTIES**

		//SET HEADLESS MODE TO FALSE SO WE CAN UTILIZE AWT LIB FOR OPENING LINKS
		SpringApplicationBuilder builder = new SpringApplicationBuilder(MidwayApplication.class);
		builder.headless(false).run(args);

		//MojangServerClient.getSpeficVersion("1.18.2");
		//FORWARD
		IPService.enableUPNP();
		//LAUNCH JAVA FX APPLICATION
		Application.launch(ViewController.class, args);


//		FileProcessor.setServerDatPath("C:/Users/Ethan/AppData/Roaming/.minecraft/servers.dat");
//		FileProcessor.setPropertiesPath("src/main/resources/server.properties");
//
//		SshClient ssh = new SshClient();
//		File script = ssh.createScript();
//		ssh.copyScriptFile(script);
//		ssh.executePortForward();
//
//		IPService.updateExternalIp("theatrejesus");
//		FileProcessor.updateClientServerAddress("theatrejesus", IPService.pollForIp("theatrejesus"));

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
