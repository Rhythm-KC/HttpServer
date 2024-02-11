package com.codefromscratch.http;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum HttpVersion {

    HTTP_1_1("HTTP/1.1", 1, 1);

    public final String LITERAL;
    public final int MAJOR;
    public final int MINOR;

    HttpVersion(String LITERAL, int MAJOR, int MINOR){
        this.LITERAL = LITERAL;
        this.MAJOR = MAJOR;
        this.MINOR = MINOR;
    }

    private static final Pattern httpVersionRegexPatter = Pattern.compile("^HTTP/(?<major>\\d+).(?<minor>\\d+)");

    public static HttpVersion getBestCompatibleHttpVersion(String stringLiteral) throws HttpParsingException{
        Matcher matcher = httpVersionRegexPatter.matcher(stringLiteral);
        System.out.println(stringLiteral);
        if (!matcher.find() || matcher.groupCount() !=2){
            throw new HttpParsingException(HttpStatusCodes.CLIENT_ERROR_400_BAD_REQUEST);
        }
        int major = Integer.parseInt(matcher.group("major"));
        int minor = Integer.parseInt(matcher.group("minor"));
        HttpVersion compatibleVersion = null;
        for (HttpVersion version : HttpVersion.values()){
            if (stringLiteral.equals(version.name())){
                return version;
            }
            if (version.MAJOR == major){
                if (version.MINOR < minor){
                    compatibleVersion = version;
                }
            }
        }
        return compatibleVersion;
    }
    
}
