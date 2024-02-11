package com.codefromscratch.http;

import java.util.Hashtable;

public class HttpRequestTarget {
   private String path;
   private Hashtable<String, String> query;
   private String fragments;

   public HttpRequestTarget(String requestTarget) throws HttpParsingException{
        if (requestTarget.length() == 0){
            this.path = "";
            this.query = null;
            this.fragments = null;
            return;
        }
        this.path = parsePath(requestTarget);
        this.query = parseQuery(requestTarget);
        this.fragments = parseFragments(requestTarget);
    }

    private String parsePath(String requestTarget) {
        int querySeparatorIndex = requestTarget.indexOf('?');
        if (querySeparatorIndex == -1){
            return requestTarget;
        }
        return requestTarget.substring(0, querySeparatorIndex);

    }

    private Hashtable<String, String> parseQuery(String requestTarget) throws HttpParsingException {
        int startIndex = requestTarget.indexOf('?');
        if (startIndex == -1){
            return null;
        }
        int endIndex;
        if (requestTarget.contains("#")){
            endIndex = requestTarget.indexOf('#');
        }else{
            endIndex = requestTarget.length();
        }
        String queryString = requestTarget.substring(startIndex, endIndex);
        int ampIdx = queryString.indexOf("&");
        int colIdx = queryString.indexOf(";");
        if (ampIdx > -1 && colIdx > -1){
            throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
        }
        String[] queryList;
        if (ampIdx != -1){
            queryList = queryString.split("&");
        }else{
            queryList = queryString.split(";");
        }
        Hashtable<String, String> map = new Hashtable<>();
        for (String query : queryList){
            String[] key_val = query.split("=");
            var out = map.putIfAbsent(key_val[0], key_val[1]);
            if(out != null){
                map.put(key_val[0], (map.get(key_val[0]) + ", "+ key_val[1]));
            } 
        }
        return map;
    }

    private String parseFragments(String requestTarget) {
        int startIndex = requestTarget.indexOf("#");
        if (startIndex == -1){
            return null;
        }
        return requestTarget.substring(startIndex, requestTarget.length());
    }
}
