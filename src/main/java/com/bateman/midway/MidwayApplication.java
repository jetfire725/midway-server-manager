package com.bateman.midway;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bateman.midway.service.SshClient;

@SpringBootApplication
public class MidwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MidwayApplication.class, args);
			SshClient ssh = new SshClient();
			File script = ssh.createScript();
			ssh.copyScriptFile(script);
			ssh.executePortForward();
	}

}
