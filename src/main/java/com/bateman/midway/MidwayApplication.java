package com.bateman.midway;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bateman.midway.service.FileProcessor;
import com.bateman.midway.service.SshClient;

@SpringBootApplication
public class MidwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MidwayApplication.class, args);
//			SshClient ssh = new SshClient();
//			File script = ssh.createScript();
//			ssh.copyScriptFile(script);
//			ssh.executePortForward();
		FileProcessor fp = new FileProcessor("src/main/resources");
		fp.setServerProperty("server-ip", "123.456.678.345");
	}

}
