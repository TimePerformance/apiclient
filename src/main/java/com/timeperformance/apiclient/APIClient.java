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
	
	private static final String API_URL = "/api/v2/";
	
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
		this("https://pma.timeperformance.com" + API_URL, account, login, password);
	}
	
	public APIClient(String apiBaseURL, String account, String login, String password) {
		if (!apiBaseURL.startsWith("https")) throw new IllegalArgumentException("https secure protocol required");
		if (!apiBaseURL.matches(".*/api/v\\d+/"))
			throw new IllegalArgumentException("apiBaseURL must end with " + API_URL);
		
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
	public APIResponse getProjectProgressReport(String projectName, boolean withDeliverables, boolean formatNumber)
			throws Exception {
		String id = getProjectId(projectName);
		StringBuffer path = new StringBuffer("projects/" + id + "/progressReport.json");
		if (formatNumber) addQueryParam(path, "formatting", "1");
		if (withDeliverables) addQueryParam(path, "withDeliverables", "1");
		return doRequest(path.toString(), false);
	}
	
	/**
	 * 
	 * @param projectName
	 * @return project roadmap
	 * @throws Exception
	 */
	public APIResponse getProjectRoadmap(String projectName, boolean withTasks, boolean formatNumber) throws Exception {
		String id = getProjectId(projectName);
		StringBuffer path = new StringBuffer("projects/" + id + "/roadmap.json");
		if (formatNumber) addQueryParam(path, "formatting", "1");
		if (withTasks) addQueryParam(path, "tasks", "1");
		return doRequest(path.toString(), false);
	}
	
	/**
	 * 
	 * @param projectName
	 * @return project workload plan by resource profile
	 * @throws Exception
	 */
	public APIResponse getProjectWorkloadPlan(String projectName) throws Exception {
		String id = getProjectId(projectName);
		String path = "projects/" + id + "/workloadplan.json";
		return doRequest(path, false);
	}
	
	/**
	 * API calls require id. This method provides a way to get the id from the name of the project
	 * 
	 * @param projectName
	 * @return project id
	 * @throws Exception
	 */
	public String getProjectId(String projectName) throws Exception, APIException {
		String id = doRequest("projects/getIdFromName.txt?name=" + projectName, false).content;
		if (id.length() == 0) throw new Exception("project not found: " + projectName);
		LOGGER.log(Level.FINE, projectName + " project id = " + id);
		return id;
	}
	
	/**
	 * 
	 * @param projectName
	 * @return project full description (summary)
	 * @throws Exception
	 */
	public APIResponse getProjectSummary(String projectName) throws Exception {
		String id = getProjectId(projectName);
		String path = "projects/" + id + "/summary.json";
		return doRequest(path, false);
	}
	
	/**
	 * @return list of not archived projects that one have access in JSON
	 */
	public APIResponse getProjectList() throws Exception {
		return doRequest("projects.json");
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_assignment">Online Documentation</a>
	 * 
	 * @param userLogin
	 * @param durationInMonths indicates how long in past and in future for the schedule (default 12
	 *            months
	 * @return schedule in iCal format
	 * @throws Exception
	 */
	public APIResponse getUserSchedule(String userLogin, int durationInMonths) throws Exception {
		String id = getUserIdFromLogin(userLogin);
		return doRequest("users/" + id + "/schedule.ics?month=" + durationInMonths);
	}
	
	/**
	 * 
	 * @param userLogin
	 * @param firstDay (format yyyy-MM-dd)
	 * @param lastDay (format yyyy-MM-dd)
	 * @param hoursPerDay
	 * @param halfDayThreshold
	 * @return user's time report
	 * @throws Exception
	 */
	public APIResponse getUserTimeReport(String userLogin,
										 String firstDay,
										 String lastDay,
										 Double hoursPerDay,
										 Double halfDayThreshold)
			throws Exception {
		String id = getUserIdFromLogin(userLogin);
		String url = "users/" + id + "/timeReport.json";
		
		url = addTimeReportParameters(url, firstDay, lastDay, hoursPerDay, halfDayThreshold);
		
		return doRequest(url);
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_timesheet">Online Documentation</a>
	 * 
	 * @param userLogin
	 * @param firstDay (format yyyy-MM-dd)
	 * @param lastDay (format yyyy-MM-dd)
	 * @param hoursPerDay
	 * @param halfDayThreshold
	 * @return user's detailed timesheet
	 * @throws Exception
	 */
	public APIResponse getUserTimesheet(String userLogin,
										String firstDay,
										String lastDay,
										Double hoursPerDay,
										Double halfDayThreshold)
			throws Exception {
		String id = getUserIdFromLogin(userLogin);
		String url = "users/" + id + "/timesheet.json";
		
		url = addTimeReportParameters(url, firstDay, lastDay, hoursPerDay, halfDayThreshold);
		
		return doRequest(url);
	}
	
	static String addTimeReportParameters(String baseURL,
										  String firstDay,
										  String lastDay,
										  Double hoursPerDay,
										  Double halfDayThreshold) {
		StringBuffer url = new StringBuffer(baseURL);
		
		addQueryParam(url, "firstDay", firstDay);
		addQueryParam(url, "lastDay", lastDay);
		addQueryParam(url, "hoursPerDay", hoursPerDay);
		addQueryParam(url, "halfDayThreshold", halfDayThreshold);
		
		return url.toString();
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_todolist">Online Documentation</a>
	 * 
	 * @param userLogin
	 * @param projectName (optionnal filter)
	 * @return
	 * @throws Exception
	 */
	public APIResponse getUserTodoList(String userLogin, String projectName) throws Exception {
		String id = getUserIdFromLogin(userLogin);
		
		StringBuffer url = new StringBuffer("users/" + id + "/todolist.json");
		
		if (projectName != null) addQueryParam(url, "project", getProjectId(projectName));
		
		return doRequest(url.toString());
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_tasks">Online Documentation</a>
	 * 
	 * @param userLogin
	 * @param periodStart
	 * @param periodEnd
	 * @param notClosed
	 * @return
	 * @throws Exception
	 */
	public APIResponse getUserTasks(String userLogin, String periodStart, String periodEnd, boolean notClosed)
			throws Exception {
		String id = getUserIdFromLogin(userLogin);
		
		StringBuffer url = new StringBuffer("users/" + id + "/todolist.json");
		
		addQueryParam(url, "periodStart", periodStart);
		addQueryParam(url, "periodEnd", periodEnd);
		addQueryParam(url, "notClosed", notClosed ? 1 : null);
		
		return doRequest(url.toString());
	}
	
	/**
	 * Utility: appends a parameter to the URL
	 * 
	 * @param url
	 * @param paramName
	 * @param paramValue
	 */
	static void addQueryParam(StringBuffer url, String paramName, Object paramValue) {
		if (paramValue == null) return;
		
		try {
			url.append(url.indexOf("?") > 0 ? "&" : "?");
			url.append(URLEncoder.encode(paramName, "UTF-8"));
			url.append("=");
			url.append(URLEncoder.encode(paramValue.toString(), "UTF-8"));
		}
		catch (UnsupportedEncodingException ex) {
			throw new AssertionError("utf-8 not supported ??", ex);
		}
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#projects_tasks">Online Documentation</a>
	 * 
	 * @param projectName
	 * @param periodStart
	 * @param periodEnd
	 * @param notClosed
	 * @return
	 * @throws Exception
	 */
	public APIResponse getProjectTasks(String projectName, String periodStart, String periodEnd, boolean notClosed)
			throws Exception {
		String id = getProjectId(projectName);
		
		StringBuffer url = new StringBuffer("projects/" + id + "/tasks.json");
		
		addQueryParam(url, "periodStart", periodStart);
		addQueryParam(url, "periodEnd", periodEnd);
		addQueryParam(url, "notClosed", notClosed ? 1 : null);
		
		return doRequest(url.toString());
	}
	
	public String getUserIdFromLogin(String userLogin) throws Exception {
		String url = "users/getIdFromLogin.txt?login=" + userLogin;
		return doRequest(url).content;
	}
	
	public APIResponse getUserList() throws Exception {
		return doRequest("users.json");
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
		StringBuffer urlString = new StringBuffer(apiBaseURL + path);
		if (addAuthenticationParameters) {
			addQueryParam(urlString, "apiuser", getAPILogin());
			addQueryParam(urlString, "apikey", password);
		}
		return urlString.toString();
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
	public APIResponse doRequest(String path) throws Exception {
		return doRequest(path, false);
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
	public APIResponse doRequest(String path, boolean authenticationInPath) throws APIException, IOException {
		
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
