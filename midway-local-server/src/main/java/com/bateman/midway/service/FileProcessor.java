package com.bateman.midway.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileProcessor {
	
	private static String path;
	static Logger log = LoggerFactory.getLogger(FileProcessor.class);
	
	//UPDATE PROPERTY IN SERVER.PROPERTIES FILE
	public static synchronized boolean setServerProperty(String property, String value) {
		File properties = new File (path);
		if (!properties.exists()) {
			log.error("Property file "+ path+ "not found.");
			return false;
		}
		BufferedReader reader = null;
		FileWriter writer= null;
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
	
	//USE STREAM TO CONVERT PROPERTY FILE TO MAP
	public static synchronized Map<String, String> propertiesToMap() {
		
		File properties = new File (path);
		if (!properties.exists()) {
			log.error("Property file "+ path+ "not found.");
			return null;
		}
		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		
        try (Stream<String> stream = Files.lines(Paths.get(path), StandardCharsets.UTF_8)) {
            stream.map(s -> s.split("=")).forEach(s -> {
            	if (s.length >1) { 
            		map.put(s[0], s[1]);
            	} else { 
            		map.put(s[0], "");
            	}
            });
        }
        catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
        return map;
	}
	
	public static void setPropertyPath(String path ) {
		FileProcessor.path = path;
	}
}
