package com.bateman.midway.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class MojangServerClient {
    static Logger log = LoggerFactory.getLogger(MojangServerClient.class);
    public static HashMap<String, String> versions = getServerVersions();

    public static HashMap<String, String> getServerVersions(){
        log.info("FETCHING SERVER VERSIONS..");
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.getForObject("https://launchermeta.mojang.com/mc/game/version_manifest.json", String.class);
            JSONObject resultObject = new JSONObject(result);
            JSONArray versionsObject = resultObject.getJSONArray("versions");
            HashMap<String, String> versions = new HashMap<>();
            for (int i=0; i< versionsObject.length(); i++){
                String id = versionsObject.getJSONObject(i).getString("id");
                String url = versionsObject.getJSONObject(i).getString("url");
                if (versionsObject.getJSONObject(i).getString("type").equals("release")){
                    versions.put(id, url);
                }
            }
            return versions;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getSpecificVersionUrl(String id){
        log.info("FETCHING SERVER "+ id + " URL FROM: " + versions.get(id));
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.getForObject(versions.get(id), String.class);
            JSONObject resultObject = new JSONObject(result).getJSONObject("downloads");
            if (resultObject.has("server")){
                return resultObject.getJSONObject("server").getString("url");
            } else {
                log.error("NO SERVER FOUND FOR " + id);
                return null;
            }
        } catch (Exception e){
            return null;
        }
    }
}
