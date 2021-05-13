package com.bateman.midway.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProcessor {
	
	public FileProcessor(String path){
		this.path = path;
		properties = new File(path+"/server.properties");
	}
	String path;
	File properties;
	Logger log = LoggerFactory.getLogger(FileProcessor.class);
	
	//UPDATE PROPERTY IN SERVER.PROPERTIES FILE
	public boolean setServerProperty(String property, String value) {
		BufferedReader reader = null;;
		FileWriter writer= null;;
		try {
			reader = new BufferedReader(new FileReader(properties));
			String line = reader.readLine();
			String data="";

			while (line != null) {
				if (line.startsWith(property)) {
					line = property+"="+value;
				}
				data = data + line + System.lineSeparator();
				line = reader.readLine();
			}
			reader.close();
			writer = new FileWriter(properties);
			writer.write(data);
			writer.close();
			return true;
			
		} catch (IOException e) {
			log.error("ERROR UPDATING SERVER PROPERTY: "+e.getMessage());
			return false;
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
				if (writer != null){
					writer.close();
				} 
			} catch (Exception e) {
				log.error("ERROR CLOSING READER, WRITER: "+e.getMessage());
			}				
		}
	}	
}
