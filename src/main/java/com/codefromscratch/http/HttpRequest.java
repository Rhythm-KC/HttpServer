package com.codefromscratch.http;

import java.util.Hashtable;

public class HttpRequest  extends HttpMessage{
   
    private HttpMethod method;

    private HttpRequestTarget requestTarget;

    private String originalhttpLiteral;
    
    private HttpVersion httpVersion;
    
    private Hashtable<String, String> http_headers = new Hashtable<>();

    private int[] body;



    HttpRequest(){
    }

    public HttpMethod getMethod() {
        return method;
    }
    public void setMethod(String httpMethod) throws HttpParsingException {
        System.out.println(httpMethod);
        for (HttpMethod method: HttpMethod.values()){
            if (httpMethod.equals(method.name())){
                this.method = method;
                return;
            }
        }
        throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_501_NOT_IMPLEMENTED);

    }

    public HttpRequestTarget getRequestTarget() {
        return requestTarget;
    }
    public void setRequestTarget(String requestTarget) throws HttpParsingException {
        System.out.println(requestTarget);
        if (requestTarget == null || requestTarget.length() <=0){
            throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_500_INTERNAL_SERVER_ERROR);
        }
        this.requestTarget = new HttpRequestTarget(requestTarget) ;
    }


    public void setHttpVersion(String httpVersion) throws HttpParsingException {
        this.originalhttpLiteral = httpVersion;
        this.httpVersion = HttpVersion.getBestCompatibleHttpVersion(httpVersion);
        if (httpVersion == null){
            throw new HttpParsingException(HttpStatusCodes.SERVER_ERROR_505_HTTP_VERSION_NOT_SUPPORTED);
        }
    }

    public void addNewHeaders(String http_headers) {
        String[] keyValPair =  http_headers.split(":");
        this.http_headers.put(keyValPair[0], keyValPair[1]);
        System.out.println(http_headers);
    }

    public String getHeaderValue(String HeaderName){
        return this.http_headers.get(HeaderName);
    }

    public void setBody(int[] body) {
        this.body = body;
    }

    public int[] getBody() {
        return this.body;
    }

}
