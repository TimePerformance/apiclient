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
<<<<<<< HEAD
	 * name of your account
	 */
	private String account;
	
	/**
	 * your API login
	 */
	private String login;
=======
	 * your API login
	 */
	private String apiUser;
>>>>>>> internal/master
	
	/**
	 * your API secret key
	 */
<<<<<<< HEAD
	private String password;
=======
	private String apiSecretKey;
>>>>>>> internal/master
	
	/**
	 * Default constructor with hardcoded URL of the service
	 * 
	 * @param account
	 * @param login API login
	 * @param password API secret key
	 */
<<<<<<< HEAD
	public APIClient(String account, String login, String password) {
		this("https://pma.timeperformance.com" + API_URL, account, login, password);
	}
	
	public APIClient(String apiBaseURL, String account, String login, String password) {
		if (!apiBaseURL.startsWith("https")) throw new IllegalArgumentException("https secure protocol required");
		
		this.apiBaseURL = apiBaseURL;
		this.account = account;
		this.login = login;
		this.password = password;
=======
	public APIClient(String apiUser, String password) {
		this("https://pma.timeperformance.com" + API_URL, apiUser, password);
	}
	
	public APIClient(String apiBaseURL, String apiUser, String password) {
		if (!apiBaseURL.startsWith("https")) throw new IllegalArgumentException("https secure protocol required");
		
		this.apiBaseURL = apiBaseURL;
		this.apiUser = apiUser;
		this.apiSecretKey = password;
>>>>>>> internal/master
	}
	
	/**
	 * 
	 * @param projectName
	 * @return project progress report
	 * @throws Exception
	 */
	public APIResponse getProjectProgressReport(String projectName, boolean withDeliverables) throws Exception {
		String id = getProjectId(projectName);
		StringBuffer path = new StringBuffer("projects/" + id + "/progressReport.json");
		addQueryParam(path, "legacy", "0");
		if (withDeliverables) addQueryParam(path, "withDeliverables", "1");
<<<<<<< HEAD
		return doRequest(path.toString(), false);
	}
	
	/**
	 * 
	 * @param projectName
	 * @return project progress report in deprecated format
	 * @throws Exception
	 */
	@Deprecated
	public APIResponse getDeprecatedProjectProgressReport(String projectName,
														  boolean withDeliverables,
														  boolean formatNumber)
			throws Exception {
		String id = getProjectId(projectName);
		StringBuffer path = new StringBuffer("projects/" + id + "/progressReport.json");
		addQueryParam(path, "legacy", "1");
		if (formatNumber) addQueryParam(path, "formatting", "1");
		if (withDeliverables) addQueryParam(path, "withDeliverables", "1");
		return doRequest(path.toString(), false);
=======
		return doRequest(path.toString());
>>>>>>> internal/master
	}
	
	/**
	 * 
	 * @param projectName
	 * @return project baseline history, current plan and forecast
	 * @throws Exception
	 */
	public APIResponse getProjectBaselineReport(String projectName) throws Exception {
		String id = getProjectId(projectName);
		StringBuffer path = new StringBuffer("projects/" + id + "/baselineReport.json");
<<<<<<< HEAD
		return doRequest(path.toString(), false);
=======
		return doRequest(path.toString());
>>>>>>> internal/master
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
<<<<<<< HEAD
		return doRequest(path.toString(), false);
=======
		addQueryParam(path, "legacy", "0");
		return doRequest(path.toString());
>>>>>>> internal/master
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
<<<<<<< HEAD
		return doRequest(path, false);
=======
		return doRequest(path);
>>>>>>> internal/master
	}
	
	/**
	 * 
	 * @param projectName
	 * @return timeseries for actual cost and actual progress of the project
	 * @throws Exception
	 */
	public APIResponse getProjectActualsTimeseries(String projectName) throws Exception {
		String id = getProjectId(projectName);
		String path = "projects/" + id + "/actualsTimeseries.json";
<<<<<<< HEAD
		return doRequest(path, false);
=======
		return doRequest(path);
>>>>>>> internal/master
	}
	
	/**
	 * API calls require id. This method provides a way to get the id from the name of the project
	 * 
	 * @param projectName
	 * @return project id
	 * @throws Exception
	 */
	public String getProjectId(String projectName) throws Exception, APIException {
<<<<<<< HEAD
		String id = doRequest("projects/getIdFromName.txt?name=" + URLEncoder.encode(projectName, "utf-8"),
							  false).content;
=======
		String id = doRequest("projects/getIdFromName.txt?name=" + URLEncoder.encode(projectName, "utf-8")).content;
>>>>>>> internal/master
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
<<<<<<< HEAD
		return doRequest(path, false);
=======
		return doRequest(path);
>>>>>>> internal/master
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#projects_expenses">Online Documentation</a>
	 * 
	 * @param projectName
	 * @return project expenses
	 * @throws Exception
	 */
	public APIResponse getProjectExpenses(String projectName) throws Exception {
		String id = getProjectId(projectName);
		StringBuffer path = new StringBuffer("projects/" + id + "/expenses.json");
<<<<<<< HEAD
		return doRequest(path.toString(), false);
=======
		return doRequest(path.toString());
>>>>>>> internal/master
	}
	
	/**
	 * @return list of not archived projects that one have access in JSON
	 */
	public APIResponse getProjectList() throws Exception {
		return doRequest("projects.json");
	}
	
	/**
	 * 
<<<<<<< HEAD
	 * @param userLogin
=======
	 * @param userId
>>>>>>> internal/master
	 * @param firstDay (format yyyy-MM-dd)
	 * @param lastDay (format yyyy-MM-dd)
	 * @param hoursPerDay
	 * @param halfDayThreshold
	 * @return user's time report
	 * @throws Exception
	 */
<<<<<<< HEAD
	public APIResponse getUserTimeReport(String userLogin,
=======
	public APIResponse getUserTimeReport(Long userId,
>>>>>>> internal/master
										 String firstDay,
										 String lastDay,
										 Double hoursPerDay,
										 Double halfDayThreshold)
			throws Exception {
<<<<<<< HEAD
		String id = getUserIdFromLogin(userLogin);
		String url = "users/" + id + "/timeReport.json";
=======
		String url = "users/" + userId + "/timeReport.json";
>>>>>>> internal/master
		
		url = addTimeReportParameters(url, firstDay, lastDay, hoursPerDay, halfDayThreshold);
		
		return doRequest(url);
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_timesheet">Online Documentation</a>
	 * 
<<<<<<< HEAD
	 * @param userLogin
=======
	 * @param userId
>>>>>>> internal/master
	 * @param firstDay (format yyyy-MM-dd)
	 * @param lastDay (format yyyy-MM-dd)
	 * @param hoursPerDay
	 * @param halfDayThreshold
	 * @return user's detailed timesheet
	 * @throws Exception
	 */
<<<<<<< HEAD
	public APIResponse getUserTimesheet(String userLogin,
=======
	public APIResponse getUserTimesheet(Long userId,
>>>>>>> internal/master
										String firstDay,
										String lastDay,
										Double hoursPerDay,
										Double halfDayThreshold)
			throws Exception {
<<<<<<< HEAD
		String id = getUserIdFromLogin(userLogin);
		String url = "users/" + id + "/timesheet.json";
=======
		String url = "users/" + userId + "/timesheet.json";
>>>>>>> internal/master
		
		url = addTimeReportParameters(url, firstDay, lastDay, hoursPerDay, halfDayThreshold);
		
		return doRequest(url);
	}
	
	public static String addTimeReportParameters(String baseURL,
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
<<<<<<< HEAD
	 * @param userLogin
=======
	 * @param userId
>>>>>>> internal/master
	 * @param projectName (optionnal filter)
	 * @return
	 * @throws Exception
	 */
<<<<<<< HEAD
	public APIResponse getUserTodoList(String userLogin, String projectName) throws Exception {
		String id = getUserIdFromLogin(userLogin);
		
		StringBuffer url = new StringBuffer("users/" + id + "/todolist.json");
=======
	public APIResponse getUserTodoList(Long userId, String projectName) throws Exception {
		
		StringBuffer url = new StringBuffer("users/" + userId + "/todolist.json");
>>>>>>> internal/master
		
		if (projectName != null) addQueryParam(url, "project", getProjectId(projectName));
		
		return doRequest(url.toString());
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_tasks">Online Documentation</a>
	 * 
<<<<<<< HEAD
	 * @param userLogin
=======
	 * @param userId
>>>>>>> internal/master
	 * @param periodStart
	 * @param periodEnd
	 * @param notClosed
	 * @return
	 * @throws Exception
	 */
<<<<<<< HEAD
	public APIResponse getUserTasks(String userLogin, String periodStart, String periodEnd, boolean notClosed)
			throws Exception {
		String id = getUserIdFromLogin(userLogin);
		
		StringBuffer url = new StringBuffer("users/" + id + "/todolist.json");
=======
	public APIResponse getUserTasks(Long userId, String periodStart, String periodEnd, boolean notClosed)
			throws Exception {
		
		StringBuffer url = new StringBuffer("users/" + userId + "/todolist.json");
>>>>>>> internal/master
		
		addQueryParam(url, "periodStart", periodStart);
		addQueryParam(url, "periodEnd", periodEnd);
		addQueryParam(url, "notClosed", notClosed ? 1 : null);
		
		return doRequest(url.toString());
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_assignments">Online Documentation</a>
	 * 
<<<<<<< HEAD
	 * @param userLogin
=======
	 * @param userId
>>>>>>> internal/master
	 * @param periodStart
	 * @param lastDay
	 * @return
	 * @throws Exception
	 */
<<<<<<< HEAD
	public APIResponse getUserAssignments(String userLogin, String firstDay, String lastDay) throws Exception {
		String id = getUserIdFromLogin(userLogin);
		
		StringBuffer url = new StringBuffer("users/" + id + "/assignments.json");
=======
	public APIResponse getUserAssignments(Long userId, String firstDay, String lastDay) throws Exception {
		
		StringBuffer url = new StringBuffer("users/" + userId + "/assignments.json");
>>>>>>> internal/master
		
		addQueryParam(url, "firstDay", firstDay);
		addQueryParam(url, "lastDay", lastDay);
		
		return doRequest(url.toString());
	}
	
	/**
	 * Utility: appends a parameter to the URL
	 * 
	 * @param url
	 * @param paramName
	 * @param paramValue
	 */
	public static void addQueryParam(StringBuffer url, String paramName, Object paramValue) {
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
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#projects_team">Online Documentation</a>
	 * 
	 * @param projectName
	 * @return
	 * @throws Exception
	 */
	public APIResponse getProjectTeam(String projectName) throws Exception {
		String id = getProjectId(projectName);
		
		StringBuffer url = new StringBuffer("projects/" + id + "/team.json");
		
		return doRequest(url.toString());
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#projects_assignments">Online Documentation</a>
	 * 
	 * @param projectName
	 * @return
	 * @throws Exception
	 */
	public APIResponse getProjectAssignments(String projectName) throws Exception {
		String id = getProjectId(projectName);
		
		StringBuffer url = new StringBuffer("projects/" + id + "/assignments.json");
		
		return doRequest(url.toString());
	}
	
	/**
	 * @return list of not archived portfolios that one have access
	 */
	public APIResponse getPortfolioList() throws Exception {
		return doRequest("portfolios.json");
	}
	
	/**
	 * API calls require id. This method provides a way to get the id from the name of the portfolio
	 * 
	 * @param name
	 * @return portfolio id
	 * @throws Exception
	 */
	public String getPortfolioId(String name) throws Exception, APIException {
<<<<<<< HEAD
		String id = doRequest("portfolios/getIdFromName.txt?name=" + URLEncoder.encode(name, "utf-8"), false).content;
=======
		String id = doRequest("portfolios/getIdFromName.txt?name=" + URLEncoder.encode(name, "utf-8")).content;
>>>>>>> internal/master
		if (id.length() == 0) throw new Exception("portfolio not found: " + name);
		return id;
	}
	
	/**
	 * 
	 * @param portfolioName
	 * @return portfolio progress report
	 * @throws Exception
	 */
	public APIResponse getPortfolioProgressReport(String portfolioName) throws Exception {
		String id = getPortfolioId(portfolioName);
		StringBuffer path = new StringBuffer("portfolios/" + id + "/progressReport.json");
		addQueryParam(path, "legacy", "0");
<<<<<<< HEAD
		return doRequest(path.toString(), false);
	}
	
	/**
	 * 
	 * @param portfolioName
	 * @return deprecated portfolio progress report
	 * @throws Exception
	 */
	@Deprecated
	public APIResponse getDeprecatedPortfolioProgressReport(String portfolioName) throws Exception {
		String id = getPortfolioId(portfolioName);
		StringBuffer path = new StringBuffer("portfolios/" + id + "/progressReport.json");
		addQueryParam(path, "legacy", "1");
		return doRequest(path.toString(), false);
	}
	
	public String getUserIdFromLogin(String userLogin) throws Exception {
		String url = "users/getIdFromLogin.txt?login=" + userLogin;
		return doRequest(url).content;
=======
		return doRequest(path.toString());
>>>>>>> internal/master
	}
	
	public APIResponse getUserList() throws Exception {
		return doRequest("users.json");
	}
	
	public DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 
	 * @param path request path relative to the API base URL
<<<<<<< HEAD
	 * @param addAuthenticationParameters (false for basic authentication, true for authentication
	 *            passing credentials as parameters)
	 * @return
	 */
	String buildURL(String path, boolean addAuthenticationParameters) {
		StringBuffer urlString = new StringBuffer(getApiBaseURL() + path);
		if (addAuthenticationParameters) {
			addQueryParam(urlString, "apiuser", getAPILogin());
			addQueryParam(urlString, "apikey", password);
		}
		return urlString.toString();
	}
	
	public String getAPILogin() {
		return login + "@" + account;
=======
	 * @return
	 */
	public String buildURL(String path) {
		return getApiBaseURL() + path;
>>>>>>> internal/master
	}
	
	/**
	 * do the request and return the response content
	 * 
	 * @param path request path relative to the API base URL
	 * @return response body content
	 * @throws Exception in case something got wrong
	 */
<<<<<<< HEAD
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
=======
	public APIResponse doRequest(String path) throws APIException, IOException {
		
		URL url = new URL(buildURL(path));
>>>>>>> internal/master
		
		LOGGER.info("Requesting " + url);
		
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");
		connection.setDefaultUseCaches(false);
		connection.setRequestProperty("Accept", "application/json");
		
<<<<<<< HEAD
		if (!authenticationInPath) {
			connection.setRequestProperty("Authorization",
										  "Basic " + Utils.buildEncodedAuthenticationString(getAPILogin(), password));
		}
=======
		connection.setRequestProperty("Authorization",
									  "Basic " + Utils.buildEncodedAuthenticationString(apiUser, apiSecretKey));
>>>>>>> internal/master
		
		APIResponse response = APIResponse.parse(connection);
		response.throwExceptionIfError();
		return response;
	}
	
	public String getApiBaseURL() {
		return apiBaseURL;
	}
	
<<<<<<< HEAD
=======
	public String getApiUser() {
		return apiUser;
	}
	
>>>>>>> internal/master
}
