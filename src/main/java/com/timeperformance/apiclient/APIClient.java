package com.timeperformance.apiclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

/**
 * APIClient: sample client to communicate with TimePerformance project management online service
 */
public class APIClient {
    
    private static final Logger LOGGER = Logger.getGlobal();
    
    /**
     * Base url for API calls
     */
    private String apiBaseURL;
    
    /**
     * name of your account
     */
    private String account;
    
    /**
     * your API login
     */
    private String login;
    
    /**
     * your API secret key
     */
    private String password;
    
    /**
     * Default constructor with hardcoded URL of the service
     * 
     * @param account
     * @param login
     * @param password
     */
    public APIClient(String account, String login, String password) {
        this("https://pma.timeperformance.com/api/v1/", account, login, password);
    }
    
    public APIClient(String apiBaseURL, String account, String login, String password) {
        if (!apiBaseURL.startsWith("https")) throw new IllegalArgumentException("https secure protocol required");
        if (!apiBaseURL.endsWith("/api/v1/")) throw new IllegalArgumentException("apiBaseURL must end with /api/v1/");
        
        this.apiBaseURL = apiBaseURL;
        this.account = account;
        this.login = login;
        this.password = password;
    }
    
    /**
     * 
     * @param projectName
     * @return project progress monitoring report
     * @throws Exception
     */
    public APIResponse getProjectProgressReport(String projectName, boolean formatNumber) throws Exception {
        String id = getProjectId(projectName);
        String path = "projects/" + id + "/progressReport.json";
        if (formatNumber) path += "?formatting=1";
        return get(path, false);
    }
    
    /**
     * API calls require id. This method provides a way to get the id from the name of the project
     * 
     * @param projectName
     * @return project id
     * @throws Exception
     */
    public String getProjectId(String projectName) throws Exception, APIException {
        String id = get("projects/getIdFromName.txt?name=" + projectName, false).content;
        if (id.length() == 0) throw new Exception("project not found: " + projectName);
        LOGGER.log(Level.FINE, projectName + " project id = " + id);
        return id;
    }
    
    /**
     * @return list of not archived projects that one have access in JSON
     */
    public APIResponse listProjects() throws Exception {
        return get("projects.json");
    }
    
    /**
     * 
     * @param userLogin
     * @param durationInMonths indicates how long in past and in future for the schedule (default 12
     *            months
     * @return schedule in iCal format
     * @throws Exception
     */
    public APIResponse getSchedule(String userLogin, int durationInMonths) throws Exception {
        String id = getUserIdFromLogin(userLogin);
        return get("users/" + id + "/schedule.ics?month=" + durationInMonths);
    }
    
    /**
     * 
     * @param userLogin
     * @param firstDay (format yyyy-MM-dd)
     * @param lastDay (format yyyy-MM-dd)
     * @param hoursPerDay
     * @param halfDayThreshold
     * @return the
     * @throws Exception
     */
    public APIResponse getTimeReport(String userLogin,
                                     String firstDay,
                                     String lastDay,
                                     Double hoursPerDay,
                                     Double halfDayThreshold) throws Exception {
        String id = getUserIdFromLogin(userLogin);
        String url = "users/" + id + "/timeReport.json";
        url += "?firstDay=" + firstDay + "&lastDay=" + lastDay;
        if (hoursPerDay != null) {
            url += "&hoursPerDay=" + hoursPerDay;
            if (halfDayThreshold != null) {
                url += "&halfDayThreshold=" + halfDayThreshold;
            }
        }
        return get(url);
    }
    
    public String getUserIdFromLogin(String userLogin) throws Exception {
        String url = "users/getIdFromLogin.txt?login=" + userLogin;
        return get(url).content;
    }
    
    public DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * 
     * @param path request path relative to the API base URL
     * @param addAuthenticationParameters (false for basic authentication, true for authentication
     *            passing credentials as parameters)
     * @return
     */
    String buildURL(String path, boolean addAuthenticationParameters) {
        try {
            String urlString = apiBaseURL + path;
            if (addAuthenticationParameters) {
                urlString += path.contains("?") ? "&" : "?";
                urlString += "apiuser=" + URLEncoder.encode(getAPILogin(), "utf-8");
                urlString += "&apikey=" + URLEncoder.encode(password, "utf-8");
            }
            return urlString;
        }
        catch (UnsupportedEncodingException ex) {
            throw new AssertionError("unexpected", ex);
        }
    }
    
    public String getAPILogin() {
        return login + "@" + account;
    }
    
    /**
     * do the request and return the response content
     * 
     * @param path request path relative to the API base URL
     * @return response body content
     * @throws Exception in case something got wrong
     */
    public APIResponse get(String path) throws Exception {
        return get(path, false);
    }
    
    /**
     * do the request and return the response content
     * 
     * @param path request path relative to the API base URL
     * @param authenticationInPath (false for basic authentication, true for authentication passing
     *            credentials as parameters)
     * @return response body content
     * @throws Exception in case something got wrong
     */
    public APIResponse get(String path, boolean authenticationInPath) throws APIException, IOException {
        
        URL url = new URL(buildURL(path, authenticationInPath));
        
        LOGGER.info("Requesting " + url);
        
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        
        connection.setRequestMethod("GET");
        connection.setDefaultUseCaches(false);
        connection.setRequestProperty("Accept", "application/json");
        
        if (!authenticationInPath) {
            connection.setRequestProperty("Authorization",
                                          "Basic " + Utils.buildEncodedAuthenticationString(getAPILogin(), password));
        }
        
        APIResponse response = APIResponse.parse(connection);
        response.throwExceptionIfError();
        return response;
    }
    
    public String getApiBaseURL() {
        return apiBaseURL;
    }
    
}
