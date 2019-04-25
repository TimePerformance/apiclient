package com.timeperformance.apiclient;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple wrapper for a response code and content
 */
public class APIResponse {
	public int responseCode;
	
	/**
	 * Response body
	 */
	public String content;
	
	public APIResponse(int responseCode) {
		this.responseCode = responseCode;
	}
	
	/**
	 * Generic parsing returning Arrays and HashMaps for Objects
	 * 
	 * @return an Array or a Map
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public Object parseJsonResponse() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(content, Object.class);
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
		
		InputStream in = apiResponse.isError() ? connection.getErrorStream() : connection.getInputStream();
<<<<<<< HEAD
		apiResponse.content = getContent(connection, in);
=======
		apiResponse.content = getContent(in);
>>>>>>> internal/master
		
		return apiResponse;
	}
	
<<<<<<< HEAD
	static String getContent(HttpsURLConnection connection, InputStream responseStream) throws IOException {
=======
	static String getContent(InputStream responseStream) throws IOException {
		if (responseStream == null) return "";
		
>>>>>>> internal/master
		try (BufferedInputStream in = new BufferedInputStream(responseStream);
				ByteArrayOutputStream out = new ByteArrayOutputStream();) {
			for (int b; (b = in.read()) != -1;) {
				out.write(b);
			}
			return out.toString("utf-8");
		}
	}
}
