package com.codefromscratch.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codefromscratch.datatypes.Text;

public class HttpParser {
    private final static Logger LOGGER = LoggerFactory.getLogger(HttpParser.class);
    private final int SP = 0x20;
    private final int CR = 0x0D;
    private final int LF = 0x0A;

    public HttpRequest parseHttpRequest(InputStream stream) throws IOException, HttpParsingException{
        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.US_ASCII);
        HttpRequest request = new HttpRequest();
        parseRequestLine(reader, request);
        parseHeaders(reader, request);
        parseBody(reader, request);
        return request;
    }

    private void parseRequestLine(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        int _byte;
        StringBuilder processDataBuffer = new StringBuilder();
        boolean methodFlag = false;
        boolean requestTargetFlag = false;
        while ((_byte = reader.read()) >=0){
            if (_byte == CR){
                _byte = reader.read();
                if (_byte == LF){
                    LOGGER.debug("Processing Data Buffer" + processDataBuffer.toString());
                    if (!methodFlag || !requestTargetFlag){
                        throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                    }
                    request.setHttpVersion(processDataBuffer.toString());
                    return;
                }else{
                    throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            if (_byte == SP){
                if (!methodFlag){
                    LOGGER.debug("Parsing HTTP method" + processDataBuffer.toString());
                    request.setMethod(processDataBuffer.toString());
                    processDataBuffer.delete(0, processDataBuffer.toString().length());
                    methodFlag = true;
                }
                else if(!requestTargetFlag){
                    LOGGER.debug("Parsing HTTP request target");
                    request.setRequestTarget(processDataBuffer.toString());
                    processDataBuffer.delete(0, processDataBuffer.toString().length());
                    requestTargetFlag = true;
                }
                else{
                    throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                }

            }
            else{
                processDataBuffer.append((char)_byte);
                if (!methodFlag){
                    if (processDataBuffer.toString().length() > HttpMethod.MAXLENGTH){
                        throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);
                    }
                }
            }
        }    
    }

    private void parseHeaders(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        // TODO Auto-generated method stub
        int _byte;
        StringBuilder processDataBuffer = new StringBuilder();
        boolean parsedHeader = false;
        while ((_byte = reader.read()) >=0){
            if (_byte==CR){
                _byte= reader.read();
                if (_byte==LF && parsedHeader){
                    request.addNewHeaders(processDataBuffer.toString());
                    processDataBuffer.delete(0, processDataBuffer.length());
                    parsedHeader = false;
                }
                else if (_byte==LF && !parsedHeader){
                    return;
                }else{
                    throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
                }
            }else{
                processDataBuffer.append((char) _byte);
                parsedHeader = true;
            }
        }

    }

    private void parseBody(InputStreamReader reader, HttpRequest request) throws IOException, HttpParsingException {
        String transfer_encoding = request.getHeaderValue("Transfer-Encoding");
        String content_length = request.getHeaderValue("Content-Length");
        // Imporve this by improving all the encoding that are being stored\
        var text_parser = new Text();
        if (transfer_encoding != null){
            String[] encodingList = transfer_encoding.split(",");
            if (encodingList[encodingList.length -1].startsWith("Chunked")){
                text_parser.read_data(reader, request);
            }else{
                throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
            }
        }
        else if (content_length != null){
            text_parser.read_data(reader, request);
        }
        else{
            request.setBody(new int[0]);
        }
        LOGGER.debug("Parsed body: " + request.getBody().toString());
    }


}
