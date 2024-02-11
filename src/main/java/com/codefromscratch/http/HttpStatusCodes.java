package com.codefromscratch.http;

public enum HttpStatusCodes {
    CLIENT_ERROR_400_BAD_REQUEST(400, "Bad Request"),
    CLIENT_ERROR_401_METHOD_NOT_ALLOWED(401, "Method Not Allowed"),
    CLIENT_ERROR_414_URI_TOO_LONG(414, "URI TOO LONG"),

    SERVER_ERROR_500_INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVER_ERROR_501_NOT_IMPLEMENTED(501, "Not Implemented"),
    SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED(505, "Http Version Not Supported"),

    ;
    public final int statusCode;
    public final String msg;

    HttpStatusCodes(int statusCode, String msg){
        this.statusCode = statusCode;
        this.msg = msg;
    }
}
