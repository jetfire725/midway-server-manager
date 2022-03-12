package com.bateman.midway.service;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.llbit.nbt.*;

public class FileProcessor {
	static Logger log = LoggerFactory.getLogger(FileProcessor.class);
	public static Properties properties = new Properties();

	
	//UPDATE PROPERTY IN SERVER.PROPERTIES FILE
	public static synchronized boolean setServerProperty(String property, String value) {
		String propertiesPath = properties.getProperty("serverDir") + "server.properties";
		File properties = new File (propertiesPath);
		if (!properties.exists()) {
			log.error("Property file "+ propertiesPath + "not found.");
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
		String propertiesPath = properties.getProperty("serverDir") + "server.properties";
		File properties = new File (propertiesPath);
		if (!properties.exists()) {
			log.error("Property file "+ propertiesPath + "not found.");
			return null;
		}
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		
        try (Stream<String> stream = Files.lines(Paths.get(propertiesPath), StandardCharsets.UTF_8)) {
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


	public static String updateClientServerAddress(String serverDatPath, String targetName) {
		String address = IPService.pollForIp(targetName) + ":" + properties.getProperty("serverPort");
		File serverDatFile = new File (serverDatPath);
		if (!serverDatFile.exists()){
			return "Game Directory not found!";
		}
		log.info("ATTEMPTING TO UPDATE GAME CLIENT SERVERS -> "+ targetName + " : " + address);
		CompoundTag serverDat;
		try {
			//LOCATE servers.dat FILE AND CONVERT TO COMPOUND TAG
			FileInputStream fileInputStream = new FileInputStream(serverDatPath);
			serverDat = CompoundTag.read(new DataInputStream(fileInputStream)).asCompound();
			fileInputStream.close();
		} catch (Exception e){
			log.error("Error updating game client server addresses: " + e.getMessage());
			return e.getMessage();
		}

		serverDat = addServerEntryToTag(serverDat, targetName, address).asCompound();

		//WRITE ROOT TAG TO FILE
		try {
			FileOutputStream fileOutputStream = new FileOutputStream("C:/Users/Ethan/AppData/Roaming/.minecraft/servers.dat");
			serverDat.write(new DataOutputStream(fileOutputStream));
			fileOutputStream.close();

			log.info("GAME CLIENT SERVERS UPDATED");
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return "Game servers updated!";
	}
	private static Tag addServerEntryToTag(Tag serverDat, String targetName, String address){
		//OBTAIN LIST TAG FROM COMPOUND,
		ListTag serverList = serverDat.get("").get("servers").asList();

		//CREATE NEW COMPOUND TAG
		CompoundTag compound = new CompoundTag();
		compound.add(new NamedTag("ip",new StringTag(address) ));
		compound.add(new NamedTag("name", new StringTag(targetName)));

		//IF EMPTY, CREATE NEW LIST OBJECT WITH COMPOUND TAG
		if (serverList.size() == 0){
			ListTag newList = new ListTag(10, new ArrayList<CompoundTag>());
			newList.add(compound);
			serverDat.get("").asCompound().add("servers", newList);
			return serverDat;
		}

		//IF SERVER NAME EXISTS, UPDATE IP
		for (SpecificTag entry : serverList) {
			if (entry.get("name").same(targetName)){
				entry.asCompound().add("ip", new StringTag(address));
				return serverDat;
			}
		}

		//NONZERO LENGTH LIST, AND ENTRY DOES NOT EXIST -> APPEND COMPOUND TAG TO LIST
		serverList.add(compound);
		return serverDat;
	}

	public static boolean downloadServerJar(File file, String url){
		if (url == null) {
			return false;
		}
		log.info("DOWNLOADING SERVER JAR : " + url);
		InputStream in;
		try {
			in = new URL(url).openStream();
			Files.copy(in, Paths.get(file.getAbsolutePath()), StandardCopyOption.REPLACE_EXISTING);
			log.info("DOWNLOAD COMPLETE");
			return true;
		} catch (IOException e) {
			log.error("UNABLE TO DOWNLOAD SERVER JAR : "+ e.getMessage());
			return false;
		}

	}
}
