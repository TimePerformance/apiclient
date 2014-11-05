package com.timeperformance.apiclient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

/**
 * Simple wrapper for a response code and content
 */
public class APIResponse {
    public int responseCode;
    
    public String content;
    
    public APIResponse(int responseCode) {
        this.responseCode = responseCode;
    }
    
    public boolean isError() {
        return responseCode != HttpURLConnection.HTTP_OK;
    }
    
    public void throwExceptionIfError() throws APIException {
        if (!isError()) return;
        throw new APIException(responseCode, content);
    }
    
    @Override
    public String toString() {
        return content;
    }
    
    public static APIResponse parse(HttpsURLConnection connection) throws IOException {
        APIResponse apiResponse = new APIResponse(connection.getResponseCode());
        
        if (apiResponse.isError()) {
            apiResponse.content = getContent(connection, connection.getErrorStream());
        }
        else {
            apiResponse.content = getContent(connection, connection.getInputStream());
        }
        
        return apiResponse;
    }
    
    static String getContent(HttpsURLConnection connection, InputStream responseStream) throws IOException {
        try (BufferedInputStream in = new BufferedInputStream(responseStream);
                ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            for (int b; (b = in.read()) != -1;) {
                out.write(b);
            }
            return out.toString("utf-8");
        }
    }
}
