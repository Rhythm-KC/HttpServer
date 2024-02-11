package com.codefromscratch.httpserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codefromscratch.http.HttpParser;
import com.codefromscratch.http.HttpParsingException;

public class HttpConnectionWorkerThread extends Thread{

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private Socket socket;

    public HttpConnectionWorkerThread(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run(){

        InputStream inputStream = null;
        OutputStream outputStream = null;
        HttpParser requestParser = new HttpParser();
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            // TODO reading from the socet

            // TODO: writing from the socket
            String html = "<html><head><title>Java server</title></head><body><h1> Testing the html server in java</h1></body></html>";
            final String CRLF = "\n\r";
            String httpResponse =
                "HTTP/1.1 OK 200" + CRLF +
                "Content-Length: " + html.getBytes().length + CRLF +
                CRLF +
                html +
                CRLF + CRLF;
            requestParser.parseHttpRequest(inputStream);
            outputStream.write(httpResponse.getBytes());
            LOGGER.info("Thread processing finished");
        } catch (IOException e) {
            LOGGER.error("Problem with communications: ", e);
        } catch (HttpParsingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            try{
                if (inputStream!= null)
                {
                    inputStream.close();
                }
                if (outputStream != null)
                {
                    outputStream.close();
                }
                socket.close();
            }catch(IOException e){}

        }
    }
    
}
