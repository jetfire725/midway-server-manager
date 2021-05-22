package com.bateman.midway.api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin("*")
@RestController
public class Controller {
	
	@GetMapping(path = "start")
	public static boolean startServer() {
		return true;
	}
}