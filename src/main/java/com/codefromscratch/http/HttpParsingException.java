package com.codefromscratch.http;

public class HttpParsingException extends Exception{

    public HttpParsingException(HttpStatusCodes code) {
        super(code.msg);
    }
    
}
