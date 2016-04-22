package com.drol.pintersest;

import java.util.HashMap;
import java.util.Map;

import com.drol.utils.QTool;
import com.drol.utils.http.ApiJsonClient;

public class PintersestApi {

	public static String clientId;
	public static String clientSecret;
	public static String accessToken;
	public static String redirectUri = "https://mulitiuplodpin.sinaapp.com/pinterestBatchUploader.php";
	
	protected static final String API_URL_BASE = "https://api.pinterest.com/";
	protected static final String API_VERSION = "v1";
	
	public static void grantAuthorization() {
		StringBuffer urlStr = new StringBuffer();
		urlStr.append(API_URL_BASE);
		urlStr.append("oauth/");
		urlStr.append("?response_type=code");
		urlStr.append("&scope=read_public,write_public");
		urlStr.append("&state=768uyFys");
		urlStr.append(String.format("&client_id=%s", clientId));
		urlStr.append(String.format("&redirect_uri=%s", redirectUri));
		QTool.openBrowse(urlStr.toString());
	}
	
	public static void requestOuthToken(String code) {
		
		String oauthToken = "/oauth/token";
		String apiOauthToken = String.format("%s%s%s", API_URL_BASE, API_VERSION, oauthToken);
		
		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("grant_type", "authorization_code");
		parameters.put("client_id", clientId);
		parameters.put("client_secret", clientSecret);
		parameters.put("code", code);
		
		ApiJsonClient apiJsonClient = ApiJsonClient.getInstance();
		Map<String, Object> result = apiJsonClient.post(apiOauthToken, parameters);
		
		accessToken = QTool.toStr(result.get("access_token"));
		
	}
	
	protected Map<String, Object> doPost(String apiName, Map<String, String> parameters) {
		if(parameters == null) parameters = new HashMap<String, String>();
		Map<String, Object> result = new HashMap<String, Object>();
		String api = String.format("%s%s%s", API_URL_BASE, API_VERSION, apiName);
		ApiJsonClient apiJsonClient = ApiJsonClient.getInstance();
		apiJsonClient.setHeader("Authorization", accessToken);
		result = apiJsonClient.post(api, parameters);
		return result;
	}
	
	protected Map<String, Object> doGet(String apiName) {
		return doGet(apiName, null);
	}
	
	protected Map<String, Object> doGet(String apiName, Map<String, String> parameters) {
		if(parameters == null) parameters = new HashMap<String, String>();
		Map<String, Object> result = new HashMap<String, Object>();
		String api = String.format("%s%s%s", API_URL_BASE, API_VERSION, apiName);
		parameters.put("access_token", accessToken);
		ApiJsonClient apiJsonClient = ApiJsonClient.getInstance();
		result = apiJsonClient.get(api, parameters);
		return result;
	}
	
	protected Map<String, Object> doUpload(String apiName, Map<String, Object> parameters) {
		Map<String, Object> result = new HashMap<String, Object>();
		String api = String.format("%s%s%s", API_URL_BASE, API_VERSION, apiName);
		ApiJsonClient apiJsonClient = ApiJsonClient.getInstance();
		apiJsonClient.setHeader("Authorization", accessToken);
		parameters.put("access_token", accessToken);
		result = apiJsonClient.upload(api, parameters);
		return result;
	}
	
}
