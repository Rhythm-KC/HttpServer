package com.codefromscratch.http;

public enum HttpMethod {
    GET, HEAD;
    public static final int MAXLENGTH;
    static{
        int tempMAXLENGTH = -1;
        for (HttpMethod method: HttpMethod.values()){
            tempMAXLENGTH = Math.max(method.name().length(), tempMAXLENGTH);
        }
        MAXLENGTH=tempMAXLENGTH;
    }
}
