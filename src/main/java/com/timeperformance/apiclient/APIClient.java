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
	 * your API login
	 */
	private String apiUser;
	
	/**
	 * your API secret key
	 */
	private String apiSecretKey;
	
	/**
	 * Default constructor with hardcoded URL of the service
	 * 
	 * @param account
	 * @param login API login
	 * @param password API secret key
	 */
	public APIClient(String apiUser, String password) {
		this("https://pma.timeperformance.com" + API_URL, apiUser, password);
	}
	
	public APIClient(String apiBaseURL, String apiUser, String password) {
		if (!apiBaseURL.startsWith("https")) throw new IllegalArgumentException("https secure protocol required");
		
		this.apiBaseURL = apiBaseURL;
		this.apiUser = apiUser;
		this.apiSecretKey = password;
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
		return doRequest(path.toString());
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
		return doRequest(path.toString());
	}
	
	/**
	 * 
	 * @param projectName
	 * @return project roadmap
	 * @throws Exception
	 */
	public APIResponse getProjectRoadmap(String projectName, boolean withTasks) throws Exception {
		String id = getProjectId(projectName);
		StringBuffer path = new StringBuffer("projects/" + id + "/roadmap.json");
		if (withTasks) addQueryParam(path, "tasks", "1");
		return doRequest(path.toString());
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
		return doRequest(path);
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
		return doRequest(path);
	}
	
	/**
	 * API calls require id. This method provides a way to get the id from the name of the project
	 * 
	 * @param projectName
	 * @return project id
	 * @throws Exception
	 */
	public String getProjectId(String projectName) throws Exception, APIException {
		String id = doRequest("projects/getIdFromName.txt?name=" + URLEncoder.encode(projectName, "utf-8")).content;
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
		return doRequest(path);
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
		return doRequest(path.toString());
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#projects_deliverables">Online
	 * Documentation</a>
	 * 
	 * @param projectName
	 * @return project deliverable hierarchy
	 * @throws Exception
	 */
	public APIResponse getProjectDeliverables(String projectName) throws Exception {
		String id = getProjectId(projectName);
		StringBuffer path = new StringBuffer("projects/" + id + "/deliverables.json");
		return doRequest(path.toString());
	}
	
	/**
	 * @return list of not archived projects that one have access in JSON
	 */
	public APIResponse getProjectList() throws Exception {
		return doRequest("projects.json");
	}
	
	/**
	 * 
	 * @param userId
	 * @param firstDay (format yyyy-MM-dd)
	 * @param lastDay (format yyyy-MM-dd)
	 * @param hoursPerDay
	 * @param halfDayThreshold
	 * @return user's time report
	 * @throws Exception
	 */
	public APIResponse getUserTimeReport(Long userId,
										 String firstDay,
										 String lastDay,
										 Double hoursPerDay,
										 Double halfDayThreshold)
			throws Exception {
		String url = "users/" + userId + "/timeReport.json";
		
		url = addTimeReportParameters(url, firstDay, lastDay, hoursPerDay, halfDayThreshold);
		
		return doRequest(url);
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_timesheet">Online Documentation</a>
	 * 
	 * @param userId
	 * @param firstDay (format yyyy-MM-dd)
	 * @param lastDay (format yyyy-MM-dd)
	 * @param hoursPerDay
	 * @param halfDayThreshold
	 * @return user's detailed timesheet
	 * @throws Exception
	 */
	public APIResponse getUserTimesheet(Long userId,
										String firstDay,
										String lastDay,
										Double hoursPerDay,
										Double halfDayThreshold)
			throws Exception {
		String url = "users/" + userId + "/timesheet.json";
		
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
	 * @param userId
	 * @param projectName (optionnal filter)
	 * @return
	 * @throws Exception
	 */
	public APIResponse getUserTodoList(Long userId, String projectName) throws Exception {
		
		StringBuffer url = new StringBuffer("users/" + userId + "/todolist.json");
		
		if (projectName != null) addQueryParam(url, "project", getProjectId(projectName));
		
		return doRequest(url.toString());
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_tasks">Online Documentation</a>
	 * 
	 * @param userId
	 * @param periodStart
	 * @param periodEnd
	 * @param notClosed
	 * @return
	 * @throws Exception
	 */
	public APIResponse getUserTasks(Long userId, String periodStart, String periodEnd, boolean notClosed)
			throws Exception {
		
		StringBuffer url = new StringBuffer("users/" + userId + "/todolist.json");
		
		addQueryParam(url, "periodStart", periodStart);
		addQueryParam(url, "periodEnd", periodEnd);
		addQueryParam(url, "notClosed", notClosed ? 1 : null);
		
		return doRequest(url.toString());
	}
	
	/**
	 * <a href="http://pma.timeperformance.com/apidoc#users_assignments">Online Documentation</a>
	 * 
	 * @param userId
	 * @param periodStart
	 * @param lastDay
	 * @return
	 * @throws Exception
	 */
	public APIResponse getUserAssignments(Long userId, String firstDay, String lastDay) throws Exception {
		
		StringBuffer url = new StringBuffer("users/" + userId + "/assignments.json");
		
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
		String id = doRequest("portfolios/getIdFromName.txt?name=" + URLEncoder.encode(name, "utf-8")).content;
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
		return doRequest(path.toString());
	}
	
	public APIResponse getUserList() throws Exception {
		return doRequest("users.json");
	}
	
	public DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	
	/**
	 * 
	 * @param path request path relative to the API base URL
	 * @return
	 */
	public String buildURL(String path) {
		return getApiBaseURL() + path;
	}
	
	/**
	 * do the request and return the response content
	 * 
	 * @param path request path relative to the API base URL
	 * @return response body content
	 * @throws Exception in case something got wrong
	 */
	public APIResponse doRequest(String path) throws APIException, IOException {
		
		URL url = new URL(buildURL(path));
		
		LOGGER.info("Requesting " + url);
		
		HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");
		connection.setDefaultUseCaches(false);
		connection.setRequestProperty("Accept", "application/json");
		
		connection.setRequestProperty("Authorization",
									  "Basic " + Utils.buildEncodedAuthenticationString(apiUser, apiSecretKey));
		
		APIResponse response = APIResponse.parse(connection);
		response.throwExceptionIfError();
		return response;
	}
	
	public String getApiBaseURL() {
		return apiBaseURL;
	}
	
	public String getApiUser() {
		return apiUser;
	}
	
}
