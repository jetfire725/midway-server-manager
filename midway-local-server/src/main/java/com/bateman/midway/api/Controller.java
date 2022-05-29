package com.bateman.midway.api;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.bateman.midway.service.FileProcessor;
@RestController
public class Controller {
	Logger log = LoggerFactory.getLogger(Controller.class);
	
	@PostMapping(path = "setProperty")
	public static boolean setProperty(@RequestBody String body) {
		JSONObject response = null;
		try {
			response = new JSONObject(body);
			return FileProcessor.setServerProperty(response.getString("property"), response.getString("value"))? true: false;
		} catch (JSONException e) {
			throw new RuntimeException(e);
		}
	}
	@GetMapping(path= "getProperties")
	public static Map<String, String> getProperties() {
		System.out.println("Called me!");
		Map map =  new HashMap<>();
		map.put("result", "string");
		return map;
		//return FileProcessor.propertiesToMap();
	}
}
