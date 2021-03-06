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
	private static String webServerHost = "https://midway-web-server.herokuapp.com";
	
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
		log.info("ATTEMPTING TO ENABLE UPnP");
		String portString = FileProcessor.properties.getProperty("serverPort");
		if (UPnP.openPortTCP(parseInt(portString))){
			log.info("ENABLED UPnP ON PORT "+portString);
			return true;
		} else {
			log.error("COULD NOT ENABLE UPnP ON PORT " + portString );
			return false;
		}


	}

	public static boolean disableUPNP(){
		String portString = FileProcessor.properties.getProperty("serverPort");
		if (UPnP.closePortTCP(parseInt(portString))){
			log.info("CLOSED UPnP ON PORT "+portString);
			return true;
		} else {
			log.error("COULD NOT CLOSE UPnP ON PORT " + portString);
			return false;
		}
	}
	
}
