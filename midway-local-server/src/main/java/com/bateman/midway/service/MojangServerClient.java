package com.bateman.midway.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class MojangServerClient {
    static Logger log = LoggerFactory.getLogger(MojangServerClient.class);
    static HashMap<String, String> versions = getServerVersions();

    public static HashMap getServerVersions(){
        log.info("FETCHING SERVER VERSIONS..");
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.getForObject("https://launchermeta.mojang.com/mc/game/version_manifest.json", String.class);
            JSONObject resultObject = new JSONObject(result);
            JSONArray versionsObject = resultObject.getJSONArray("versions");
            HashMap versions = new HashMap<String, String>();
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

    public static void getSpeficVersion(String id){
        RestTemplate restTemplate = new RestTemplate();
        try {
            String result = restTemplate.getForObject(versions.get(id), String.class);
            JSONObject resultObject = new JSONObject(result).getJSONObject("downloads").getJSONObject("server");;
            String url = resultObject.getString("url");
            log.info(url);
        } catch (Exception e){
            //return null;
        }
    }
}
