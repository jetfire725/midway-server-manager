package com.bateman.midway.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import com.dosse.upnp.UPnP;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static java.lang.Integer.parseInt;

@Service
public class IPService {
	static Logger log = LoggerFactory.getLogger(SshClient.class);
	private static String webServerHost = "http://localhost:3000";
	
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
	
	//SEND UPDATE TO SERVER THAT DYNAMIC IP HAS CHANGED
	public static boolean updateExternalIp(String serverId) {
		String externalIp = getExternalIp();
		if (externalIp.equals(pollForIp(serverId))){
			return true;
		}
		log.info("UPDATING WEBSERVER EXTERNAL IP...");
		RestTemplate restTemplate = new RestTemplate();
		HashMap<String, String> request = new HashMap<String, String>();
		request.put("serverId", serverId);
		request.put("ip", externalIp);
		try {
			String result = restTemplate.postForObject(webServerHost+"/update/ip", request, String.class);
			return true;
		} catch (Exception e) {
			log.error(e.getMessage());
			return false;
		}
		
	}

	public static String pollForIp(String serverId){
		log.info("CHECKING WEBSERVER EXTERNAL IP...");
		RestTemplate restTemplate = new RestTemplate();
		HashMap<String, String> request = new HashMap<String, String>();
		request.put("serverId", serverId);
		try {
			String result = restTemplate.postForObject(webServerHost + "/get/ip", request, String.class);
			return result;
		} catch (Exception e) {
			return "False";
		}
	}

	public static boolean enableUPNP(){
		String portString = FileProcessor.properties.getProperty("serverPort");
		return UPnP.openPortTCP(parseInt(portString));

	}
	
}
