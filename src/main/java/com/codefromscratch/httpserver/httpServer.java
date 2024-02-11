package com.codefromscratch.httpserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codefromscratch.httpserver.config.ConfigurationManager;
import com.codefromscratch.httpserver.core.ServerListenerThread;


public class httpServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(httpServer.class);
    public static void main(String[] args){
        LOGGER.info("starting HTTP server.....");
        ConfigurationManager.getInstance().loadConfigFile("src/main/resources/http.json");
        var conf = ConfigurationManager.getInstance().getCurrentConfig();
        LOGGER.info("Using port " + conf.getport());
        LOGGER.info("Using webroot " + conf.getwebroute());
        
        ServerListenerThread serverThread;
        try {
            serverThread = new ServerListenerThread(conf.getport(), conf.getwebroute());
            serverThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
