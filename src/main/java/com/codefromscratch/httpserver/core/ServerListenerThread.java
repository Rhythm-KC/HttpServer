package com.codefromscratch.httpserver.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerListenerThread extends Thread{

    
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) throws IOException
    {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
    }
   @Override
   public void run(){
        try {
            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = this.serverSocket.accept();
                LOGGER.info("Connection accepted from " + socket.getInetAddress());
                new HttpConnectionWorkerThread(socket).start();
            }

        } catch (IOException e) {
            LOGGER.error("Problem with setting socket ", e);
        }
        finally{
            try {
                serverSocket.close();
            } catch (IOException e) {}
        }
   } 
}
