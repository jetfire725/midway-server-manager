package com.bateman.midway.service;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * @author World
 */
public class SshClient {
	
	public File createScript() {
		File script = new File ("src/main/resources/router-script.sh");
		try {
			script.createNewFile();
			return script;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

    public  void executePortForward() {
        String user = "admin";
        String password = "password";
        String host = "192.168.50.1";
        int port = 22;
        String remoteFile = "./test.txt";

        try {
        	
        	//Establish connection
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            
            //Create SFTP Channel
            System.out.println("Crating SFTP Channel.");
            ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect();
            System.out.println("SFTP Channel created.");
            //sftpChannel.put(input,  remote);
            InputStream inputStream = sftpChannel.get(remoteFile);

            try (Scanner scanner = new Scanner(new InputStreamReader(inputStream))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    System.out.println(line);
                }
            }
        } catch (JSchException | SftpException e) {
            e.printStackTrace();
        }
    }
}
