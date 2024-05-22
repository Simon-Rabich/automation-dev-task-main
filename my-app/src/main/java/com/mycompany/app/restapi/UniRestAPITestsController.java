package com.mycompany.app.restapi;

import com.antelope.af.bugmanipulator.Asserter;
import com.antelope.af.logging.SelTestLog;
import com.antelope.af.utilities.ExtendedReporter;
import com.antelope.af.utilities.WebElementUtils;
import com.antelope.af.utilities.testpparameters.UserParamsThreadLocal;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.entity.ContentType;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.File;
import java.io.FileInputStream;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;

import static com.antelope.af.testconfig.popertiesinit.PropertiesGetter.instance;
import static com.antelope.af.testconfig.testPatterns.commons.BaseSeleniumTest.brandName;
import static com.antelope.af.testconfig.testPatterns.commons.BaseSeleniumTest.newApiUserForceChangePasswordStatus;
import static com.antelope.af.utilities.ExtendedReporter.ColorReport.RED;


/**
 * UniRest HTTP request client library for antelope api sanity tests
 *
 */
public class UniRestAPITestsController {

	private static HashMap<String, String> headersMap = null;

	private static String password = "testtest123";

	private static String smKenyaPassword = "Testtest123@";

	public static HashMap<String, String> registerNewRealThroughApiTest(String baseURL, String apiKey) throws Exception {
		List<String> brandEnabledCountriesIso;
		HashMap<String, String> userInfoList = new HashMap<String, String>();
		String url = baseURL;
		HttpResponse<JsonNode> response;
		HashMap<String, Object> paramsMap = new HashMap<>();
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");

		paramsMap.put("firstName", "Test");
		paramsMap.put("lastName", "Automation");
		paramsMap.put("email",
				"automation+1" + WebElementUtils.getRandomNumberByRange(0, 1000000000) + "@antelopesystem.com");
		if(brandName.equals("SmKenya")) {
			password = smKenyaPassword;
		}

		if(brandName.equals("Intertrader") || brandName.equals("Highness") || brandName.equals("Antelope_Staging")) {
			password = "Aa123456!";
		}

		paramsMap.put("password", password);
		String telephone = String.valueOf(Instant.now().toEpochMilli());
		paramsMap.put("telephone", telephone);
		paramsMap.put("telephonePrefix", "972");
		paramsMap.put("sc", "automation env");
		paramsMap.put("fullAddress", "HaArba'a St 21, Tel Aviv-Yafo, Israel");
		paramsMap.put("address", "HaArba'a St 21, Tel Aviv-Yafo, Israel");
		brandEnabledCountriesIso = UniRestUsersController.getBrandEnabledCountryIso(baseURL);
		int randomEnabledIso = WebElementUtils.getRandomNumberByRange(0, brandEnabledCountriesIso.size() - 1);
		String randomCountry = brandEnabledCountriesIso.get(randomEnabledIso).replaceAll("\"", "");
		paramsMap.put("countryiso", randomCountry);
		// paramsMap.put("languageIso", "US");
		paramsMap.put("visitId", 0);
		paramsMap.put("block_communications", 1);
		paramsMap.put("apikey", apiKey);

		System.out.println("brandEnabledCountriesIso"+ brandEnabledCountriesIso );
		System.out.println("userInfoList"+ userInfoList);
		System.out.println("url"+url);
		System.out.println("headersMap"+ headersMap );
		System.out.println("paramsMap"+paramsMap);

		try {
			response = Unirest.post(url + "/SignalsServer/api/registerUser").headers(headersMap).queryString(paramsMap)
					.asJson();
			System.out.println("url"+url + "/SignalsServer/api/registerUser");
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			ExtendedReporter.log(Unirest.post(url + "/SignalsServer/api/registerUser").headers(headersMap).queryString(paramsMap).asString().getBody(), RED);
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected registering new user using api call to be successful, response body is"
						+ response.getBody().toString(),
				"Validating that registering new user using api call is successful");

		userInfoList.put("userId", response.getBody().getObject().getJSONObject("result").get("userId").toString());
		userInfoList.put("email", response.getBody().getObject().getJSONObject("result").get("email").toString());
		userInfoList.put("password", password);
		userInfoList.put("telephone", telephone);
		userInfoList.put("authToken", UniRestUsersController.getClientAuthToken(baseURL, userInfoList.get("email"), password));
		System.out.println("userInfoList"+userInfoList);

		return userInfoList;
	}

	public static HashMap<String, String> registerNewUserThroughApiTest(String baseURL, String apiKey, boolean isQuickOnboarding, boolean skipPopup)
			throws Exception {
		List<String> brandEnabledCountriesIso;
		HashMap<String, String> userInfoList = new HashMap<String, String>();
		String url = baseURL;
		HttpResponse<JsonNode> response;
		HashMap<String, Object> paramsMap = new HashMap<>();
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");

		paramsMap.put("firstName", "Test");
		paramsMap.put("lastName", "Automation");
		paramsMap.put("email", "automation+2" + WebElementUtils.getRandomNumberByRange(0, 2000000000) + "@antelopesystem.com"); //1000000000
		if(brandName.equals("SmKenya")) {
			password = smKenyaPassword;
		}
		if(brandName.equals("Intertrader") || brandName.equals("IntertraderStaging") || brandName.equals("Antelope_Staging") || brandName.equals("Highness") || brandName.equals("StgMysql8PropTrade") || brandName.equals("Maleyat") || brandName.equals("RazeMarkets")) {
			password = "Aa123456!";
		}
		if(brandName.equals("4AI")) {
			password = "Aa123456";
		}

		paramsMap.put("password", password);
		String telephone = String.valueOf(Instant.now().toEpochMilli());
		paramsMap.put("telephone", telephone);
		paramsMap.put("telephonePrefix", "972");
		paramsMap.put("sc", "automation env");
		paramsMap.put("fullAddress", "HaArba'a St 21, Tel Aviv-Yafo, Israel");
		paramsMap.put("address", "HaArba'a St 21, Tel Aviv-Yafo, Israel");
		brandEnabledCountriesIso = UniRestUsersController.getBrandEnabledCountryIso(baseURL);
		int randomEnabledIso = WebElementUtils.getRandomNumberByRange(0, brandEnabledCountriesIso.size() - 1);
		String randomCountry = brandEnabledCountriesIso.get(randomEnabledIso).replaceAll("\"", "");
		paramsMap.put("countryiso", randomCountry);
		// paramsMap.put("languageIso", "US");
		paramsMap.put("visitId", 0);
		paramsMap.put("block_communications", 1);
		paramsMap.put("apikey", apiKey);
		if(isQuickOnboarding) {
			paramsMap.put("quickonboarding", "true");
		}

		if(skipPopup) {
			paramsMap.put("skip_popup", "true");
		}

		try {

			System.out.println("PAYLOAD" + paramsMap);
			System.out.println("BASE URL" + url + "/SignalsServer/api/registerUser");

			response = Unirest.post(url + "/SignalsServer/api/registerUser").headers(headersMap).queryString(paramsMap)
					.asJson();

			System.out.println("response registerNewUserThroughApiTest :" + response.getBody().toString());

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
//			ExtendedReporter.log(Unirest.post(url + "/SignalsServer/api/registerUser").headers(headersMap).queryString(paramsMap).asString().getBody(), RED);
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected registering new user using api call to be successful, response body is"
						+ response.getBody().toString(),
				"Validating that registering new user using api call is successful");

		userInfoList.put("userId", response.getBody().getObject().getJSONObject("result").get("userId").toString());
		userInfoList.put("email", response.getBody().getObject().getJSONObject("result").get("email").toString());
		userInfoList.put("password", password);
		userInfoList.put("telephone", telephone);
		userInfoList.put("authToken", UniRestUsersController.getClientAuthToken(baseURL, userInfoList.get("email"), password));

		UserParamsThreadLocal.setUserAsMarkedToTest(0);
		UserParamsThreadLocal.setUserID(Integer.valueOf(userInfoList.get("userId")));
		newApiUserForceChangePasswordStatus.put(UserParamsThreadLocal.getUserID(), false);
		return userInfoList;
	}

	public static HashMap<String, String> registerNewUserThroughApiTest(String baseURL, String apiKey, boolean isQuickOnboarding) throws Exception {
		return registerNewUserThroughApiTest(baseURL, apiKey, true, false);
	}

	public static HashMap<String, String> registerNewUserThroughApiTest(String baseURL, String apiKey) throws Exception {
		return registerNewUserThroughApiTest(baseURL, apiKey, false, false);
	}

	public static HashMap<String, String> registerNewUserThroughApiTest(String baseURL, String apiKey,
			int randomEmailNumber) throws Exception {
		List<String> brandEnabledCountriesIso;
		HashMap<String, String> userInfoList = new HashMap<String, String>();
		String url = baseURL;
		HttpResponse<JsonNode> response;
		HashMap<String, Object> paramsMap = new HashMap<>();
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");

		paramsMap.put("firstName", "test");
		paramsMap.put("lastName", "automation");

		if(randomEmailNumber == 0)
			paramsMap.put("email", "automation@antelopesystem.com");
		else
			paramsMap.put("email", "automation+" + randomEmailNumber + "@antelopesystem.com");

		if(brandName.equals("SmKenya")) {
			password = smKenyaPassword;
		}
		if(brandName.equals("Intertrader")) {
			password = "Aa123456!";
		}
		paramsMap.put("password", password);
		String telephone = String.valueOf(Instant.now().toEpochMilli());
		paramsMap.put("telephone", telephone);
		paramsMap.put("telephonePrefix", "972");
		paramsMap.put("sc", "automation env");
		paramsMap.put("fullAddress", "HaArba'a St 21, Tel Aviv-Yafo, Israel");
		paramsMap.put("address", "HaArba'a St 21, Tel Aviv-Yafo, Israel");
		brandEnabledCountriesIso = UniRestUsersController.getBrandEnabledCountryIso(baseURL);
		int randomEnabledIso = WebElementUtils.getRandomNumberByRange(0, brandEnabledCountriesIso.size() - 1);
		String randomCountry = brandEnabledCountriesIso.get(randomEnabledIso).replaceAll("\"", "");
		paramsMap.put("countryiso", randomCountry);
		paramsMap.put("languageIso", "US");
		paramsMap.put("visitId", 0);
		paramsMap.put("block_communications", 1);
		paramsMap.put("apikey", apiKey);
		try {
			response = Unirest.post(url + "/SignalsServer/api/registerUser").headers(headersMap).queryString(paramsMap)
					.asJson();
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected registering new user using api call to be successful, response body is"
						+ response.getBody().toString(),
				"Validating that registering new user using api call is successful");

		userInfoList.put("userId", response.getBody().getObject().getJSONObject("result").get("userId").toString());
		userInfoList.put("email", response.getBody().getObject().getJSONObject("result").get("email").toString());
		userInfoList.put("password", password);
		userInfoList.put("telephone", telephone);

		UserParamsThreadLocal.setUserAsMarkedToTest(0);
		UserParamsThreadLocal.setUserID(Integer.valueOf(userInfoList.get("userId")));
		return userInfoList;
	}


	public static String crmLoginAuthentication(String email, String pass, String baseURL) throws Exception {
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}

		HttpResponse<JsonNode> response;
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("email", email);
		paramsMap.put("password", pass);
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		try {
			response = Unirest.post(url + "/SignalsCRM/operators/authenticate").headers(headersMap)
					.queryString(paramsMap).asJson();
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected login authentication to be successful",
				"Validating that login authentication is successful");


		SelTestLog.info("Simon log"+response.getBody().getObject().getJSONObject("result").get("authToken").toString());


		return response.getBody().getObject().getJSONObject("result").get("authToken").toString();
	}

	public static String loginAuthentication(String email, String pass, String baseURL, boolean correctAuthincation)
			throws Exception {
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}

		HttpResponse<JsonNode> response;
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("email", email);
		paramsMap.put("password", pass);
		System.out.println("pass:::::: " + pass);
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		try {
			response = Unirest.post(url + "/SignalsServer/client/api/users/authenticate").headers(headersMap)
					.queryString(paramsMap).asJson();
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
		if(correctAuthincation) {
			System.out.println("response:::::: " + response.getBody().getObject().toString());
			Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
					"Expected login authentication to be successful",
					"Validating that login authentication is successful");
		} else {
			Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("false"),
					"Expected login authentication to be unsuccessful",
					"Validating that login authentication is unsuccessful");
		}

		return response.getBody().getObject().getJSONObject("result").get("authToken").toString();
	}

	public static HashMap<String, Long> createTradingAccount(String baseURL, String authToken, boolean isDemo)
			throws Exception {
		HashMap<String, Long> _dataHashMap = new HashMap<>();
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("currency", "USD");
		bodyOptions.put("isDemo", isDemo);
		try {
			response = Unirest.post(url + "/SignalsServer/client/api/brokers/users/register").headers(headersMap)
					.body(bodyOptions.toString()).asJson();

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected creating new account using api call to be successful",
				"Validating that creating new account using api call is successful");

		_dataHashMap.put("id", response.getBody().getObject().getJSONObject("result").getLong("id"));
		_dataHashMap.put("extrnalId", response.getBody().getObject().getJSONObject("result").getLong("externalId"));
		return _dataHashMap;
	}

	public static HashMap<String, Long> createTradingAccountWithCurrency(String baseURL, String authToken, String userId, String currency, boolean isDemo)
			throws Exception {
		HashMap<String, Long> _dataHashMap = new HashMap<>();
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("userId", userId);
		bodyOptions.put("currency", currency);
		bodyOptions.put("brokerId", "129"); //Was 83

		System.out.println("bodyOptions :" + bodyOptions);
		System.out.println("url :" + url + "/SignalsCRM/brokers/users");

		try {
			response = Unirest.post(url + "/SignalsCRM/brokers/users").headers(headersMap)
					.body(bodyOptions.toString()).asJson();

			System.out.println("LOgGGGGGG createTradingAccountWithCurrency :" + response.getBody().toString());

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed creating a new account for user: " + userId + "!!!!");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
			return null;
		}

		_dataHashMap.put("id", response.getBody().getObject().getJSONObject("result").getLong("id"));
		_dataHashMap.put("extrnalId", response.getBody().getObject().getJSONObject("result").getLong("externalId"));
		return _dataHashMap;
	}

	public static void depositAPITest(String baseURL, String authToken, String amount, long traderAccountID)
			throws Exception {
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}

		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/x-www-form-urlencoded");

		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("brokerUserId", traderAccountID);
		paramsMap.put("amount", amount);

		ExtendedReporter.log("brokerUserId::::" + traderAccountID, RED);
		ExtendedReporter.log("amount of Deposit::" + amount, RED);

		try {
			response = Unirest.post(url + "/SignalsServer/client/api/wallet/deposit").headers(headersMap)
					.queryString(paramsMap).getHttpRequest().asJson();
			if(response.getBody().getObject().get("success").toString().equals("false")) {
				response = Unirest.post(url + "/SignalsServer/client/api/wallet/deposit").queryString(paramsMap)
						.headers(headersMap).asJson();
			}

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected deposit request to be successful but got the following response "
						+ response.getBody().toString(),
				"Validating that deposit request is successful");

		ExtendedReporter.log("response after Deposit::" + response.getBody().toString(), RED);

	}

	public static void uploadDocuments(String baseURL, String authToken, File file, String fileName) throws Exception {
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}

		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		// headersMap.put("Content-Type", "application/x-www-form-urlencoded");

		try {
			response = Unirest.post(url + "/SignalsServer/client/api/documents/upload").headers(headersMap)
					.field("file", new FileInputStream(file), ContentType.APPLICATION_OCTET_STREAM, fileName)
					.field("type", "Passport").asJson();

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected document to be successfuly uploaded ", "Validating that document is successfuly uploaded");
	}

	public static void withDrawApiTest(String baseURL, String authToken, long newlyRigsterdUserTraderAccountID,
			String amount) throws Exception {
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}

		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/x-www-form-urlencoded");

		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("brokerUserId", newlyRigsterdUserTraderAccountID);
		paramsMap.put("amount", amount);
		paramsMap.put("userWithdrawalReason", "amount");

		ExtendedReporter.log(" headers11111111111111: " + headersMap, RED);

		ExtendedReporter.log(" params11111111111111: " + paramsMap, RED);

		try {
			response = Unirest.post(url + "/SignalsServer/client/api/wallet/withdrawal").headers(headersMap)
					.queryString(paramsMap).headers(headersMap).asJson();

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		ExtendedReporter.log("response body11111111111111: " + response.getBody().toString(), RED);

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected withdraw request to be successful but got the following response"
						+ response.getBody().toString(),
				"Validating that withdraw request is successful");
	}

	public static long getClientsGridApiRequestTime(String baseURL, String authToken, int start, int limit, String crmSecondaryMenuItemName) throws Exception {
		HttpResponse<JsonNode> response = null;
		long totalTime = 0, timeBeforeRequest = 0, timeAfterRequest = 0;
		String concatenatedUrl = new String();
		String requestType = "post";
		Gson gson = new Gson();
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("start", start);
		paramsMap.put("limit", limit);
		JsonObject innerObject = new JsonObject();
		innerObject.addProperty("start", start);
		innerObject.addProperty("limit", limit);

		switch(crmSecondaryMenuItemName) {
			case "Clients":
			case "Users":
				innerObject.addProperty("status", "Registered");
				concatenatedUrl = "/SignalsCRM/users/search";
				break;
			case "Clients / Advanced":
			case "Users / Advanced":
				concatenatedUrl = "/SignalsCRM/users/aggregated/search";
				break;
			case "Trading Accounts":
				concatenatedUrl = "/SignalsCRM/brokers/users/search";
				break;
			case "Partials":
				innerObject.addProperty("status", "PartialRegistration");
				concatenatedUrl = "/SignalsCRM/users/search";
				break;
			case "Leads":
				if(instance().retrieveAntelopeProperties().getEnvironmentType().equals("antelope")) {
					innerObject.addProperty("status", "PartialRegistration");
					concatenatedUrl = "/SignalsCRM/users/search";
				} else {
					concatenatedUrl = "/SignalsCRM/brokers/users/search";
				}
			case "Callbacks":
				concatenatedUrl = "/SignalsCRM/callbacks/search";
				break;
			case "Messages":
				concatenatedUrl = "/SignalsCRM/messages/search";
				break;
			case "Transactions":
				concatenatedUrl = "/SignalsCRM//brokers/bankings/search";
				break;
			case "KYC Documents":
				concatenatedUrl = "/SignalsCRM/kyc/documents";
				requestType = "get";
				break;
			case "Operators":
				concatenatedUrl = "/SignalsCRM/operators/search";
				break;
			case "Roles":
				concatenatedUrl = "/SignalsCRM/roles";
				requestType = "get";
				break;
			case "Inbox":
				concatenatedUrl = "/SignalsCRM/operators/communicationhistory/search";
				break;
		}


		String jsonBody = gson.toJson(innerObject);

		System.out.println("jsonBody::::::::" + jsonBody);

		try {
			if(requestType.equals("post")) {
				timeBeforeRequest = System.currentTimeMillis();
				System.out.println("lhlhkhllhhhlURI:::::" + baseURL + concatenatedUrl);
				System.out.println("lhlhkhllhhhlHEADER:::::" + headersMap);
				System.out.println("lhlhkhllhhhlBODY:::::" + jsonBody);

				response = Unirest.post(baseURL + concatenatedUrl).headers(headersMap).body(jsonBody).asJson();
//				System.out.println("response of post::::::::" + response.getBody());
				timeAfterRequest = System.currentTimeMillis();
				totalTime = timeAfterRequest - timeBeforeRequest;
				System.out.println("timeAfterRequest of post::::::::" + timeAfterRequest);
				System.out.println("totalTime of post::::::::" + totalTime);

			}
			if(requestType.equals("get")) {
				System.out.println("getgetgetgetgetgetgetget");

				timeBeforeRequest = System.currentTimeMillis();

				System.out.println("data::::" + baseURL + concatenatedUrl + headersMap + paramsMap);

				response = Unirest.get(baseURL + concatenatedUrl).headers(headersMap).queryString(paramsMap).asJson();

//				response = Unirest.get("https://apicrm.ai-globalgroup.com/SignalsCRM/roles?limit=50&start=0")

				System.out.println("response of get::::::::" + response.getBody());
				timeAfterRequest = System.currentTimeMillis();
				totalTime = timeAfterRequest - timeBeforeRequest;
			}
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected to get all the requested clients",
				"Validating that we got all the requested clients");

		return totalTime;
	}

	public static void sendEmailTemplatesToGmailViaApi(String baseURL, String apiKey, String htmlBody, String eventName, String templateLanguage, String brandName)
			throws Exception {
		List<String> brandEnabledCountriesIso;
		HashMap<String, String> userInfoList = new HashMap<String, String>();
		String url = baseURL;
		HttpResponse<JsonNode> response;
		HashMap<String, Object> paramsMap = new HashMap<>();
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		headersMap.put("Accept", "application/json");
		headersMap.put("X-Postmark-Server-Token", apiKey);

		JSONObject bodyOptions = new JSONObject();

		bodyOptions.put("From", "support_test@antelopesystem.com");
//        bodyOptions.put("To", "qaxsites@antelopesystem.com, QA@xsitesit.com");
//        bodyOptions.put("To", "automation@antelopesystem.com, ronix@xsites.co.il");
		bodyOptions.put("To", "simon.r@xsites.co.il"); //("To", "automation@antelopesystem.com, qaxsites@antelopesystem.com, micheal@xsites.co.il");
		bodyOptions.put("Subject", brandName + " " + templateLanguage + " " + "Templates");
		bodyOptions.put("HtmlBody", htmlBody);
		if(htmlBody.isEmpty()) {
			bodyOptions.put("HtmlBody", "<b>" + eventName + ":" + "</b>" + "<br>" + "Html body is empty");
		}
		try {
			response = Unirest.post(url).headers(headersMap).body(bodyOptions.toString()).asJson();

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
		Asserter.assertTrue(response.getStatusText().equals("OK"),
				"Expected html template to be sent."
						+ response.getBody().toString(),
				"Validating that html template was sent to email through api.");
	}

	public static HashMap<String, Object> getSystemParameterValuesViaApi(String baseURL, String authToken, String parameterName) throws Exception {
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}

		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		String bodyOptions = "{\"filterFields\":[{\"dataType\":\"String\",\"operation\":\"Equal\",\"values\":[\"" + parameterName + "\"],\"fieldName\":\"name\"}]}";

		try {
			response = Unirest.post(url + "/SignalsCRM/parameters/search").headers(headersMap).body(bodyOptions).asJson();

			System.out.println("res of /SignalsCRM/parameters/search :" + response.getBody().getObject().toString());

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected getting system parameter " + parameterName + " values using api call to be successful, response body is" + response.getBody().toString(),
				"got system parameter " + parameterName + " using api call successfully");

        /*
        HashMap<String, String> dataHashMap = new HashMap<>();
        dataHashMap.put("id", response.getBody().getObject().getJSONObject("result").getString("id"));
        dataHashMap.put("creationTime", response.getBody().getObject().getJSONObject("result").getString("creationTime"));
        dataHashMap.put("name", response.getBody().getObject().getJSONObject("result").getString("name"));
        dataHashMap.put("value", response.getBody().getObject().getJSONObject("result").getString("value"));
        dataHashMap.put("description", response.getBody().getObject().getJSONObject("result").getString("description"));
        dataHashMap.put("group", response.getBody().getObject().getJSONObject("result").getString("group"));
        dataHashMap.put("type", response.getBody().getObject().getJSONObject("result").getString("type"));
        dataHashMap.put("minValue", response.getBody().getObject().getJSONObject("result").getString("minValue"));
        dataHashMap.put("maxValue", response.getBody().getObject().getJSONObject("result").getString("maxValue"));
        dataHashMap.put("isPublished", response.getBody().getObject().getJSONObject("result").getString("isPublished"));
        dataHashMap.put("readOnly", response.getBody().getObject().getJSONObject("result").getString("readOnly"));
        dataHashMap.put("hidden", response.getBody().getObject().getJSONObject("result").getString("hidden"));

        */

		return new Gson().fromJson(response.getBody().getObject().getJSONArray("result").get(0).toString(), HashMap.class);
	}

	public static void setSystemParameterValuesViaApi(String baseURL, String authToken, String parameterName, HashMap<String, Object> paramValues) throws Exception {
		String parameterId = getSystemParameterValuesViaApi(baseURL, authToken, parameterName).get("id").toString().split("\\.")[0];

		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "http://" + baseURL;
		}

		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		String bodyOptions = new JSONObject(paramValues).toString();

		try {
			response = Unirest.put(url + "/SignalsCRM/parameters/" + parameterId).headers(headersMap).body(bodyOptions).asJson();
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected setting system parameter " + parameterName + " values using api call to be successful, response body is" + response.getBody().toString(),
				"set system parameter " + parameterName + " using api call successfully");
	}

	public static String getVersionUsingSignalsCRM(String baseUrl) throws Exception {
		HttpResponse<JsonNode> response;
		String url = baseUrl + "/SignalsCRM/system/version";
		try {
			response = Unirest.get(url).asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed to get version from: " + url);
		}
		return response.getBody().getObject().getJSONObject("result").get("version").toString();
	}

	public static String getVersionUsingSignalsServer(String baseUrl) throws Exception {
		HttpResponse<JsonNode> response;
		String url = baseUrl + "/SignalsServer/system/version";
		try {
			response = Unirest.get(url).asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed to get version from: " + url);
		}
		return response.getBody().getObject().getJSONObject("result").get("version").toString();
	}

	public static String getSignalsServerSystemIP(String baseUrl) throws UnirestException {
		HttpResponse<JsonNode> response;
		String url = baseUrl + "/SignalsServer/system/ip";
		try {
			response = Unirest.get(url).asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed to get IP from: " + url);
		}
		return response.getBody().getObject().get("result").toString();
	}

}
