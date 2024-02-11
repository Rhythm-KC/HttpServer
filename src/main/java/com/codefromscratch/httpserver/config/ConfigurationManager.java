package com.codefromscratch.httpserver.config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.codefromscratch.httpserver.utils.Json;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public class ConfigurationManager {
   private static ConfigurationManager manager;
   private static Configuration currentConfig;
   
   private ConfigurationManager()
   {
   }

   public static ConfigurationManager getInstance(){
    if (manager == null){
        manager = new ConfigurationManager();
    }
    return manager;
   }

   /**
    *  Used to load config file 
 * @throws HttpConfigurationException 
    **/
public void loadConfigFile(String filePath){
    FileReader fileReader;
    try {
        fileReader = new FileReader(filePath);
    } catch (FileNotFoundException e) {
        throw new HttpConfigurationException(e);
    }
    StringBuffer buffer = new StringBuffer();
    int i;
    try {
        while ((i = fileReader.read()) != -1)
        {
            buffer.append((char)i);
        }
    } catch (IOException e) {
        throw new HttpConfigurationException(e);
    }
    JsonNode conf;
    try {
        conf = Json.parse(buffer.toString());
    } catch (JsonProcessingException e) {
        throw new HttpConfigurationException("Error Parsing Json file", e);
    }
    try {
        currentConfig = Json.fromJson(conf, Configuration.class);
    } catch (JsonProcessingException | IllegalArgumentException e) {
        throw new HttpConfigurationException("Error Parsing Json file. Internal", e);
    }
   }

   /**
    *  Return current config
    **/
   public Configuration getCurrentConfig(){
    if(currentConfig == null)
    {
        throw new HttpConfigurationException("No current config found");
    }
    return currentConfig;
   } 
}
