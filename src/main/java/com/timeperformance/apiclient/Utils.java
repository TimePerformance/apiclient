package com.timeperformance.apiclient;

import java.io.IOException;

import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class Utils {
    private Utils() {
    }
    
    /**
     * utility method (could be replaced in Java 8 with java.util.Base64 class)
     * 
     * @param bytes
     * @return
     */
    static public String base64Encode(byte[] bytes) {
        return DatatypeConverter.printBase64Binary(bytes);
    }
    
    /**
     * utility method encoding credentials for basic authentication
     * 
     * @param login
     * @param password
     * @return encoded credentials
     */
    static public String buildEncodedAuthenticationString(String login, String password) {
        String authStr = login + ":" + password;
        String authEncoded = base64Encode(authStr.getBytes());
        return authEncoded;
    }
    
    /**
     * Utility method to indent json
     * 
     * @param input
     * @return
     * @throws IOException
     */
    public static String indentJson(String input) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Object obj = mapper.readValue(input, Object.class);
        String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        return indented;
    }
    
}
