package com.bateman.midway.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IPService {
	static Logger log = LoggerFactory.getLogger(SshClient.class);
	
	//GET EXTERNAL IP FROM AWS SERVICE
	public static String getExternalIp() {
		URL whatismyip;
		try {
			whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
			String ip = in.readLine();
			return ip;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		} 
		
	}
	
	//SEND UPDATE TO SERVER THAT DNAMIC IP HAS CHANGED
	public static boolean updateExternalIp() {
		RestTemplate restTemplate = new RestTemplate();
		
		String result = restTemplate.postForObject("https://httpbin.org/anything", "ip", String.class);
		System.out.println(result);
		return true;
	}
	
}
