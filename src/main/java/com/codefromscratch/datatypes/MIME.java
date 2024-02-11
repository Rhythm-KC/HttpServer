package com.codefromscratch.datatypes;

import java.io.InputStreamReader;

import com.codefromscratch.http.HttpParsingException;
import com.codefromscratch.http.HttpRequest;

public abstract class MIME {
   
    protected static final int SP = 0x20;
    protected static final int CR = 0x0D;
    protected static final int LF = 0x0A;

    abstract void read_data(InputStreamReader dataStream, HttpRequest data_to_read) throws HttpParsingException; 

    abstract String strigify(String data);
}
