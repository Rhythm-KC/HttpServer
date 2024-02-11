package com.codefromscratch.datatypes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.codefromscratch.http.HttpParsingException;
import com.codefromscratch.http.HttpRequest;
import com.codefromscratch.http.HttpStatusCodes;

public class Text  extends MIME{

    @Override
    public void read_data(InputStreamReader dataStream, HttpRequest request) throws HttpParsingException {
        String cl = request.getHeaderValue("Content-Length").strip();
        String encod = request.getHeaderValue("Transfer-Encoding");
        try{
            if (encod != null){
                request.setBody(read_chuncked_data(dataStream));
            }
            request.setBody(read_cl_data(dataStream, Integer.parseInt(cl)));
        }catch(IOException e){
            throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }

        
    }

    private int[] read_cl_data(InputStreamReader dataStream, int content_length) throws IOException, HttpParsingException {
        int[] sb = new int[content_length];
        int count = 0;
        int _byte;
        while ((count < content_length) && ((_byte = dataStream.read()) >= 0)){
            if (count >= content_length){
                throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
            }
            sb[count] = (int) _byte;
            count++;
        }
        return sb;
    }

    private int[] read_chuncked_data(InputStreamReader dataStream) throws IOException, HttpParsingException {
        boolean reading = true;
        ArrayList<Integer> dataBuffer = new ArrayList<>();
        while (reading) {
            int data_size = get_dataSize(dataStream);
            if (data_size == 0){
                read_last_CRLF(dataStream);
                reading = false;
            }else{
                dataBuffer.addAll(read_data_body(dataStream, data_size));
                if (dataBuffer.size() != data_size){
                    throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
        }
        return dataBuffer.stream().mapToInt(i-> i).toArray();
    }


    private ArrayList<Integer> read_data_body(InputStreamReader dataStream, int data_size) throws IOException, HttpParsingException {
        int count = 0;
        int byte_data;
        ArrayList<Integer> databuf = new ArrayList<>(); 
        while (count < data_size){
            byte_data = dataStream.read();
            databuf.add(byte_data);
            count++;
        }
        read_last_CRLF(dataStream);
        return databuf;
    }

    private void read_last_CRLF(InputStreamReader dataStream) throws IOException, HttpParsingException {
        int _byte;
        while ((_byte = dataStream.read()) >=0 ) {
            if(_byte == Text.CR){
                _byte = dataStream.read();
                if(_byte == Text.LF){
                    return;
                }
                else{
                    throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
                }
            }
            throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
        }
    }

    private int get_dataSize(InputStreamReader dataStream) throws IOException {
        int data_byte;
        StringBuilder buffer = new StringBuilder();

        while ((data_byte = dataStream.read()) >= 0) {
            if (data_byte==Text.CR){
                if (data_byte == Text.LF){
                    break;
                }
        
            }else{
                buffer.append((char)data_byte);
            }
        }
        return Integer.parseInt(buffer.toString(), 16);
    }

    @Override
    public String strigify(String data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'strigify'");
    }
    
}
