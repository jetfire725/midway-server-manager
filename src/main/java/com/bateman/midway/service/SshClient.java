package com.bateman.midway.service;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

import org.slf4j.LoggerFactory;

import org.slf4j.Logger;
import java.io.BufferedReader;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.optional.ssh.Scp;



public class SshClient {
	Logger log = LoggerFactory.getLogger(SshClient.class);
    String user = "admin";
    String password = "password";
    String host = getDefaultGateway();
    int port = 22;
    String remoteFile = "/tmp/router-script.sh";
	
	
	//FIND SCRIPT FILE 
	public File createScript() {
		File script = new File ("src/main/resources/router-script.sh");
		try {
			script.createNewFile();
			return script;
		} catch (IOException e) {
			log.error("Could not create script file: "+e.getMessage());
			return null;
		}
	}
	
	//GET DEFAULT GATEWAY ADDRESS
	public String getDefaultGateway() {
		String ipAddress = null;
		try {
        	Process process = Runtime.getRuntime().exec("ipconfig");
        	BufferedReader bufferedReader = 
        			new BufferedReader(new InputStreamReader(process.getInputStream())); 
        	String line;
        	while ((line = bufferedReader.readLine()) != null) {
        		if (line.trim().startsWith("Default Gateway")) {
        			ipAddress = line.substring(line.indexOf(":") + 1).trim();
        		}
        	}		
        	return ipAddress;
        	} catch (Exception e) {
        		log.error("Failed to obtain default gateway address.." + e.getMessage());
        		return null;
        	}
	}
	
	//COPY SCRIPT FILE TO ROUTER
	public  boolean copyScriptFile(File script) {
			try{
				Scp scp = new Scp();
				Project p = new Project();
				p.init();
				scp.setProject( p );
				scp.setTrust(true);
				scp.setPort(port);
				scp.setFile(script.getAbsolutePath());
				scp.setTodir(user+"@"+host+":"+remoteFile);
				scp.setPassword(password);
				scp.execute();
				log.info("Script file copied successfully.");
				return true;
			} catch (Exception e) {
				log.error(e.getMessage());
				return false;
			}
}

	//  EXECUTE SCRIPT FILE 
	public  void executePortForward() {
        try {
        	
        	//Establish session
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            log.info("Establishing Connection...");
            session.connect();
            log.info("Connection established: "+ session.isConnected());
            
            //Establish channel
            Channel channel=session.openChannel("exec");
            InputStream in=channel.getInputStream();
            log.info("Attempting to execute shell script...");
	        ((ChannelExec)channel).setCommand("chmod +x /tmp/router-script.sh && sh /tmp/router-script.sh");
            //((ChannelExec)channel).setCommand("echo $SSH_CLIENT");
	        ((ChannelExec)channel).setErrStream(System.err);	   
	        channel.connect();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	        String line;
        	while ((line = reader.readLine()) != null) {
        		log.info(line);
        	}
        	
        	while (!channel.isClosed()) {
        		Thread.sleep(250);
        	}
        	
        	log.info("Exit status: "+ channel.getExitStatus());


	        channel.disconnect();
	        session.disconnect();
        } catch (Exception e) {
        	e.printStackTrace();
            //log.error(e.getMessage());
        } 
    }
}
