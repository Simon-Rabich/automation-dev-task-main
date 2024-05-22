package com.mycompany.app.restapi;

import com.antelope.af.bugmanipulator.Asserter;
import com.antelope.af.logging.SelTestLog;
import com.antelope.af.utilities.*;
import com.antelope.af.utilities.testpparameters.UserParamsThreadLocal;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.time.Instant;
import java.util.*;

import static com.antelope.af.testconfig.testPatterns.commons.BaseSeleniumTest.*;
import static com.antelope.af.utilities.ExtendedReporter.ColorReport.RED;
import static com.antelope.af.utilities.ExtendedReporter.ColorReport.YELLOW;

/**
 * UniRest HTTP request client library
 *
 */
public class UniRestUsersController {

	Boolean isDemo;

	private static HashMap<String, String> headersMap = null;

	private static HashMap<String, Object> paramsMap = null;

	private static String password = "Testtest123!"; //was: testtest123

	private static String smKenyaPassword = "Testtest123@";


	public static String generateNewAuthToken(String baseUrl, String email, String password) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("email", email);
		bodyOptions.put("password", password);

		ExtendedReporter.log("email:" + email, RED);
		ExtendedReporter.log("password" + password, RED);
		ExtendedReporter.log("baseUrl" + baseUrl, RED);
		ExtendedReporter.log("headersMap" + headersMap, RED);

//		https://api.btcfutureshub.com/SignalsCRM/operators/authenticate-leaderboard

		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/operators/authenticate-leaderboard").headers(headersMap).body(bodyOptions.toString())
					.asJson();

			System.out.println("response" + response);
			ExtendedReporter.log("response" + response, RED);

		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed generating a new authToken");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
			return null;
		}
		return response.getBody().getObject().getJSONObject("result").getJSONObject("tokenResponse").get("token").toString();
	}

	public static String getClientAuthToken(String baseUrl, String email, String password) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("email", email);
		paramsMap.put("password", password);
		try {
			response = Unirest.post(baseUrl + "/SignalsServer/users/authenticate").headers(headersMap).queryString(paramsMap)
					.asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed to authenticate user");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
			return null;
		}
		return response.getBody().getObject().getJSONObject("result").get("authToken").toString();
	}

	public static void approveGeneratedAuthTokenTotp(String baseUrl, String authToken, String verificationCode) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		headersMap.put("auth_token", authToken);
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("verificationCode", verificationCode);
		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/operators/totp").headers(headersMap).queryString(paramsMap).asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed approving the new authToken totp");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
			return;
		}
	}

	public static HttpResponse<JsonNode> disableUserNinjaAndSalesDesks(String baseUrl, String authToken, long userID)
			throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/x-www-form-urlencoded");
		JSONObject bodyOptions = new JSONObject();
		//bodyOptions.put("userId", userID);
		// bodyOptions.put("ninjaDeskId", 0);
		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/users/salesstatus?userId=" + userID + "&ninjaDeskId=0&salesDeskId=0").headers(headersMap).body(bodyOptions.toString())
					.asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed converting user with id " + userID + " To user with no ninja desk!!!!");
			Assert.fail();
			return null;
		}
		return response;

	}

	/**
	 * @param baseUrl
	 * @param authToken
	 * @param userID
	 * @return this method converts user into test and sets his ninja desk status to none
	 * @throws UnirestException
	 */
	public static HttpResponse<JsonNode> convertToTestUser(String baseUrl, String authToken, int userID, String brand)
			throws UnirestException {
		//if in dev area don't convert to test
		if(!baseUrl.contains("dev") && baseUrl.contains("stg")) {
			HttpResponse<JsonNode> response;
			headersMap = new HashMap<>();
			headersMap.put("auth_token", authToken);
			headersMap.put("Content-Type", "application/json");
			HashMap<String, Object> paramsMap = new HashMap<>();
			paramsMap.put("userId", userID);
			paramsMap.put("isTest", true);
			paramsMap.put("isDemo", true);

			System.out.println("userId" + userID);
			System.out.println("headersMap" + headersMap);
			System.out.println("paramsMap" + paramsMap);
			System.out.println("baseUrl" + baseUrl);

			try {
				response = Unirest.put(baseUrl + "/SignalsCRM/user/toggle/test").headers(headersMap).queryString(paramsMap)
						.asJson();
				System.out.println("response:  " + response);
				System.out.println("baseUrl:  " + baseUrl);
				System.out.println("response.getBody():  " + response.getBody());

			} catch(Exception e) {
				SelTestLog.error(e.getMessage());
				ExtendedReporter.log(Unirest.put(baseUrl + "/SignalsCRM/user/toggle/test").headers(headersMap).queryString(paramsMap).asString().getBody(), RED);

				System.out.println("baseUrl" + baseUrl);
				System.out.println("EP" + "/SignalsCRM/user/toggle/test");

				throw e;
			}
			if(!response.getBody().getObject().get("success").toString().equals("true")) {
				SelTestLog.warn("Failed converting user with id " + userID + " To Test user!!!!");
				ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
				Assert.fail();
				return null;
			}
			//converting user ninja desk to none
			if(!brand.equals("NsxCoin") && !brand.equals("KryptoKnights")) {
				try {
					response = UniRestUsersController.disableUserNinjaAndSalesDesks(baseUrl, authToken, userID);
				} catch(Exception e) {
					SelTestLog.error(e.getMessage());
					throw e;
				}
			}

			if(brand.equals("KryptoKnights")) {
				try {
					UniRestUsersController.changeUserAssignedDesks(baseUrl, authToken, userID, 8, 0, 0);
				} catch(Exception e) {
					SelTestLog.error(e.getMessage());
					throw e;
				}
			}

			if(!response.getBody().getObject().get("success").toString().equals("true")) {
				SelTestLog.warn("Failed converting user with id " + userID + " To Test user with no ninja desk!!!!");
				Assert.fail();
				return null;
			}
			return response;
		}
		return null;
	}

	public static HttpResponse<JsonNode> convertTestUserToReal(String baseUrl, String authToken, int userID)
			throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userId", userID);
		paramsMap.put("isTest", false);
		paramsMap.put("isDemo", false);
		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/user/toggle/test").headers(headersMap).queryString(paramsMap)
					.asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			ExtendedReporter.log(Unirest.put(baseUrl + "/SignalsCRM/user/toggle/test").headers(headersMap).queryString(paramsMap).asString().getBody(), RED);
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed converting user with id " + userID + " To real user!!!!");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
			return null;
		}
		return response;
	}

	public static HttpResponse<JsonNode> bypassNexmo(String baseUrl, String authToken, int userID)
			throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userId", userID);
		paramsMap.put("isBypassNexmo", true);
		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/user/toggle/bypassNexmo").headers(headersMap).queryString(paramsMap)
					.asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			ExtendedReporter.log(Unirest.put(baseUrl + "/SignalsCRM/user/toggle/bypassNexmo").headers(headersMap).queryString(paramsMap).asString().getBody(), RED);
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed to bypass nexmo for user " + userID);
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
			return null;
		}
		return response;
	}

	public static void updateClientLanguageIso(String baseURL, String authToken, int userId, String languageIso)
			throws Exception {
		HashMap<String, Long> _dataHashMap = new HashMap<>();
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("id", userId);
		bodyOptions.put("languageIso", languageIso);
		try {
			response = Unirest.put(url + "/SignalsServer/users").headers(headersMap)
					.body(bodyOptions.toString()).asJson();

		} catch(Exception e) {
			try {
				WebdriverUtils.sleep(5000);
				response = Unirest.put(url + "/SignalsServer/users").headers(headersMap)
						.body(bodyOptions.toString()).asJson();
			} catch(Exception e1) {
				ExtendedReporter.log("response body: " + Unirest.put(url + "/SignalsServer/users").headers(headersMap)
						.body(bodyOptions.toString()).asString(), RED);
				SelTestLog.warn(e1.getMessage());
				throw e1;
			}
		}
		try {
			Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
					"Expected to successfully update the languageIso to " + languageIso,
					"Validating that the client languageIso changed successfully to " + languageIso);
		}
		catch(AssertionError e) {
			ExtendedReporter.log("Failed to updated language iso", YELLOW);
		}
	}

	public static HttpResponse<JsonNode> convertToPhoneValid(String baseUrl, String authToken, int userID)
			throws UnirestException {
		//if in dev area don't convert to test
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("id", userID);
		bodyOptions.put("telephoneValid", true);
		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/users/").headers(headersMap).body(bodyOptions.toString())
					.asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed converting user with id " + userID + " to phone valid user!!!!");
			Assert.fail();
			return null;
		}
		return response;
	}

	public static void changeUserAssignedDesks(String baseUrl, String authToken, int userId, int newSalesDeskId, int newRetentionDeskId, int newNinjaDeskId) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userId", userId);
		paramsMap.put("salesDeskId", newSalesDeskId);
		paramsMap.put("retentionDeskId", newRetentionDeskId);
		paramsMap.put("ninjaDeskId", newNinjaDeskId);

		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/users/salesstatus").headers(headersMap).queryString(paramsMap).asJson();
			Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true")
							&& Integer.valueOf(response.getBody().getObject().getJSONObject("result").get("salesDeskId").toString()) == newSalesDeskId
							&& Integer.valueOf(response.getBody().getObject().getJSONObject("result").get("ninjaDeskId").toString()) == newNinjaDeskId,
					"Expected sales desk id to change to " + newSalesDeskId + "and ninja desk id to change to " + newNinjaDeskId,
					"Validating that sales desk id was changed to " + newSalesDeskId + "and ninja desk id was changed to " + newNinjaDeskId);

			response = Unirest.post(baseUrl + "/SignalsCRM/users/retentionstatus").headers(headersMap).queryString(paramsMap).asJson();
			Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true")
							&& Integer.valueOf(response.getBody().getObject().getJSONObject("result").get("retentionDeskId").toString()) == newRetentionDeskId,
					"Expected retention desk id to change to " + newRetentionDeskId,
					"Validating that sales desk id was changed to" + newRetentionDeskId);
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
	}

	public static void allowDeposit(String baseUrl, String authToken, int userID) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("id", userID);
		bodyOptions.put("allowDeposit", true);
		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/users/").headers(headersMap).body(bodyOptions.toString())
					.asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getStatus() != 200) {
			SelTestLog
					.warn("Failed converting user with id " + userID + " To Test user which is allowed to deposit!!!!");
		}
	}

	public static HttpResponse<JsonNode> convertToTestPhoneValid(String baseUrl, String authToken, int userID)
			throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("id", userID);
		bodyOptions.put("telephoneValid", true);
		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/users/").headers(headersMap).body(bodyOptions.toString())
					.asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getStatus() != 200) {
			SelTestLog.warn("Failed converting user with id " + userID + " To Test user with phone valid!!!!");
			return null;
		}
		return response;
	}


	public static HttpResponse<JsonNode> userInvestment(String baseUrl, String authToken, int userID, boolean approveInvestment) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userId", userID);
		paramsMap.put("isInvestmentManagementEnabled", approveInvestment);
		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/user/toggle/investment-management-enabled").headers(headersMap).queryString(paramsMap)
					.asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getStatus() != 200) {
			SelTestLog
					.warn("Failed converting user with id " + userID + " To Test user with blocked communication!!!!");
			return null;
		}
		return response;
	}

	public static HttpResponse<JsonNode> blockAllCommunications(String baseUrl, String authToken, int userID, boolean blockCommunicationsStatus)
			throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		System.out.println("userId:  " + userID);

		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userId", userID);

		if(blockCommunicationsStatus) {
			paramsMap.put("isBlockCommunications", true);
		} else {
			paramsMap.put("isBlockCommunications", false);
		}

		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/user/toggle/block-communications").headers(headersMap).queryString(paramsMap).asJson();
			System.out.println("response.getBody() of /SignalsCRM/user/toggle/block-communications:  " + response.getBody());
		} catch(Exception e) {
			throw e;
		}
		if(response.getStatus() != 200) {
			SelTestLog
					.warn("Failed converting user with id " + userID + " To Test user with blocked communication!!!!");
			return null;
		}
		return response;
	}

	public static long getPumpingDetails(String baseUrl, String authToken, int id) throws UnirestException {
		Long pumpingUpdateTime = (long) 0;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		HttpResponse<JsonNode> response = Unirest.get(baseUrl + "/SignalsCRM/brokers/users/" + id).headers(headersMap)
				.asJson();
		System.out.println("response" + response.getBody());
		if(response.getStatus() != 200) {
			SelTestLog.warn("Failed fetching pumping details of user id " + id + "!!!!");
		} else {
			try {
				pumpingUpdateTime = (long) response.getBody().getObject().getJSONArray("result").getJSONObject(0)
						.get("pumpingUpdateTime");
			} catch(Exception e) {

			}
		}
		System.out.println("pumpingUpdateTime" + pumpingUpdateTime);
		return pumpingUpdateTime;
	}

//    public static void unPassQuestionnaire(String baseUrl, String authToken, int id) throws UnirestException {
//        HttpResponse<JsonNode> response;
//        headersMap = new HashMap<>();
//        headersMap.put("auth_token", authToken);
//        headersMap.put("Content-Type", "application/json");
//        JSONObject bodyOptions = new JSONObject();
//        bodyOptions.put("id", id);
//        bodyOptions.put("fnsStatus", "None");
//        try {
//            response = Unirest.put(baseUrl + "/SignalsCRM/users/").headers(headersMap).body(bodyOptions.toString())
//                    .asJson();
//        } catch (Exception e) {
//            throw e;
//        }
//        if (response.getStatus() != 200) {
//            SelTestLog.warn("Failed by passing questionnaire  with id " + id);
//        }
//    }

	public static void byPassQuestionnaire(String baseUrl, String authToken, int id) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("id", id);
//        bodyOptions.put("fnsStatus", "Approved");
		bodyOptions.put("fnsStatus", "1");
		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/users/").headers(headersMap).body(bodyOptions.toString())
					.asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getStatus() != 200) {
			SelTestLog.warn("Failed by passing questionnaire with id " + id);
		}
	}

	public static void byPassKycStatus(String baseUrl, String authToken, int id) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("userId", id);
		bodyOptions.put("kycStatus", 4);
		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/users/kycstatus").headers(headersMap).body(bodyOptions.toString())
					.asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getStatus() != 200) {
			SelTestLog.warn("Failed by passing kycStatus with id " + id);
		}
	}

	public static void createTradingAccount(String baseUrl, String authToken, int id, int brokerId) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("userId", id);
		bodyOptions.put("brokerId", brokerId);
		bodyOptions.put("currency", "USD");
		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/brokers/users").headers(headersMap).body(bodyOptions.toString())
					.asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getStatus() != 200) {
			SelTestLog.warn("Failed by passing kycStatus with id " + id);
		}
	}

	public static HashMap<String, Object> registerAffiliateUser(String baseURL, String apiKey) throws Exception {
		String generatedEmail;
		List<String> brandEnabledCountriesIso;
		HashMap<String, Object> registerdUserData = new HashMap<>();
		HttpResponse<JsonNode> response;
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("firstname", "Test");
		paramsMap.put("lastname", "Automation");
		paramsMap.put("telephone", Instant.now().toEpochMilli());
		generatedEmail = "automation+1" + WebElementUtils.getRandomNumberByRange(0, 200000000) + "@antelopesystem.com";
		paramsMap.put("email", generatedEmail);
		System.out.println("SIMON LOGGG");
		System.out.println("generatedEmail" + generatedEmail);
		if(brandName.equals("SmKenya")) {
			password = smKenyaPassword;
		}
		paramsMap.put("password", password);
		// country creation is hardcoded due to brokers issues.
		brandEnabledCountriesIso = UniRestUsersController.getBrandEnabledCountryIso(baseURL);
		int randomEnabledIso = WebElementUtils.getRandomNumberByRange(0, brandEnabledCountriesIso.size() - 1);
		String randomCountry = brandEnabledCountriesIso.get(randomEnabledIso).replaceAll("\"", "");
		if(baseURL.contains("europefx.com.au")) {
			paramsMap.put("countryiso", randomCountry);
		} else if(baseURL.contains("theaffiliapes")) {
			paramsMap.put("countryiso", "AT");
		} else if(baseURL.contains("system.fxadsystems48")) {
			paramsMap.put("countryiso", "AG");
		} else if(baseURL.contains("sminvest")) {
			paramsMap.put("countryiso", "MO");
		} else if(baseURL.contains("nsxcoin")) {
			paramsMap.put("countryiso", randomCountry);
		} else if(baseURL.contains("naffitive.com")) {
			paramsMap.put("countryiso", "GB");
		} else if(baseURL.contains("naffitive.com")) {
			paramsMap.put("countryiso", "GB");
		} else if(brandName.equals("SmKenya")) {
			paramsMap.put("countryiso", "MX");
		} else if(brandName.equals("SMPFX")) {
			paramsMap.put("countryiso", "MX");
		} else if(brandName.equals("FxMarketsXchange")) {
			paramsMap.put("countryiso", "MX");
		} else
//            paramsMap.put("countryiso", randomCountry);
		{
			paramsMap.put("countryiso", "dk");
		}
		paramsMap.put("languageIso", "US");
		paramsMap.put("apikey", apiKey);
		paramsMap.put("sc", "automation env");
		System.out.println("SIMON LOGGG");
		System.out.println("paramsMap" + paramsMap);
		try {
			response = Unirest.post(baseURL + "/SignalsServer/api/registerUser?").queryString(paramsMap).asJson();
			System.out.println("SIMON LOGGG");
			System.out.println("response" + response.getBody().toString());
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.info("Success Registering new baff user");
			registeredAffiliateApiEmail = generatedEmail;
			registeredAffiliateApiPassword = password;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			SelTestLog.error("Failed Registering new baff user");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
		}
		registerdUserData.put("firstName", response.getBody().getObject().getJSONObject("result").get("firstName"));
		registerdUserData.put("lastName", response.getBody().getObject().getJSONObject("result").get("lastName"));
		registerdUserData.put("telephone", response.getBody().getObject().getJSONObject("result").get("telephone"));
		registerdUserData.put("countryiso", response.getBody().getObject().getJSONObject("result").get("countryIso"));
		registerdUserData.put("userId", response.getBody().getObject().getJSONObject("result").getInt("userId"));
		registerdUserData.put("email", generatedEmail);
		registerdUserData.put("password", password);
		registerdUserData.put("brokerLoginUrl",
				response.getBody().getObject().getJSONObject("result").get("brokerLoginUrl"));
		registerdUserData.put("brokerName", response.getBody().getObject().getJSONObject("result").get("brokerName"));
		registerdUserData.put("offerType", response.getBody().getObject().getJSONObject("result").get("offerType"));
		registerdUserData.put("brokerSalesStatus",
				response.getBody().getObject().getJSONObject("result").get("brokerSalesStatus"));
		registerdUserData.put("deviceType", response.getBody().getObject().getJSONObject("result").get("deviceType"));

		registeredAffiliateApiId = Integer.valueOf(registerdUserData.get("userId").toString());
		UserParamsThreadLocal.setUserID(registeredAffiliateApiId);
		newApiUserForceChangePasswordStatus.put(UserParamsThreadLocal.getUserID(), false);

		return registerdUserData;
	}

	public static int fetchUserId(String baseURL, String email, String token) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		Gson gson = new Gson();
		JsonObject innerObject = new JsonObject();
		innerObject.addProperty("start", 0);
		innerObject.addProperty("limit", 50);
		innerObject.addProperty("limit", 50);
		innerObject.addProperty("orderBy", "id");
		innerObject.addProperty("orderDesc", true);
		innerObject.addProperty("status", "Registered");
		innerObject.addProperty("returnColumn", "id");
		String jsonBody = gson.toJson(innerObject);
		String val = " \"emails\":";
		jsonBody = jsonBody.replace("}", "," + val + "[" + '"' + email + '"' + "]}");

		response = Unirest.post(baseURL + "/SignalsCRM/users/search").header("Content-Type", "application/json")
				.header("auth_token", token).body(jsonBody).asJson();
		return Integer.valueOf(
				response.getBody().getObject().get("result").toString().replace("[", "").replace("]", "").trim());
	}

	public static Object getAffiliateUserByCreationTime(String signalServerBaseApi, String apiKey, String startDate, String endDate, String authToken) throws Exception {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("cache", "no-cache");
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("api_key", apiKey);
		paramsMap.put("from", startDate);
		paramsMap.put("to", endDate);
		try {
			response = Unirest.get(signalServerBaseApi.replace("https", "http") + "/SignalsServer/api/getUsers")
					.headers(headersMap).queryString(paramsMap).asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed fetching affilite user data by creation time");
		}
		return response.getBody().getObject().get("result");
	}

	public static HashMap<Object, Object> getUserDetails(String baseURL, String authToken) throws UnirestException {
		HashMap<Object, Object> userData = new HashMap<>();
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		try {
			if(!baseURL.contains("http") || !baseURL.contains("https")) {
				baseURL = "http://" + baseURL;
			}
			HttpResponse<JsonNode> response = Unirest.get(baseURL + "/SignalsServer/client/api/users/")
					.headers(headersMap).asJson();
			String[] responseList = response.getBody().getObject().getJSONObject("result").toString().split(",");
			for(String s : responseList) {
				userData.put(s.split(":")[0].trim().replaceAll("\"", ""), s.split(":")[1].trim().replaceAll("\"", ""));
			}
		} catch(Exception e) {
			userData = null;
		}
		return userData;
	}

	public static JSONObject getUsersDetailsFromSignalsCRM(String signalsCRM, String authToken, long userId) throws UnirestException {
		JSONObject jsonObject;
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("cache", "no-cache");
		headersMap.put("auth_token", authToken);

		try {
			response = Unirest.get(signalsCRM + "/SignalsCRM/users/" + userId)
					.headers(headersMap).asJson();
			jsonObject = response.getBody().getObject().getJSONObject("result");
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed fetching user data from signalsCRM");
		}
		return jsonObject;
	}

	public static String getUsersDetailsFromSignalsServer(String signalsServer, String apiKey, String userId, String requestedField) throws UnirestException {
		JSONObject jsonObject;
		HttpResponse<JsonNode> response;
		paramsMap = new HashMap<>();
		paramsMap.put("userid", userId);
		paramsMap.put("apikey", apiKey);

		try {
			response = Unirest.get(signalsServer + "/SignalsServer/api/getUser")
					.queryString(paramsMap).asJson();
			jsonObject = response.getBody().getObject().getJSONObject("result");
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed fetching user data from signalsServer");
		}
		return jsonObject.get(requestedField).toString();
	}

	public static void updateUserDetails(String signalsCRM, JSONObject userJsonObject, String authToken) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		try {
			response = Unirest.put(signalsCRM + "/SignalsCRM/users")
					.headers(headersMap).body(userJsonObject).asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			ExtendedReporter.log("response body: " + "Failed updating user data", YELLOW);
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
//            Assert.fail("Failed updating user data");
		}
	}

	public static List<String> getBrandDisabledCountryIso(String signalServerBaseApi) throws Exception {
		ObjectMapper mapper;
		org.codehaus.jackson.JsonNode rootNode;
		List<String> allCountriesIso = new ArrayList<>();
		Iterator<?> it = null;
		List<String> disabledCountriesIso = new ArrayList<>();
		List<String> enabledCountriesIso = new ArrayList<>();
		HttpResponse<JsonNode> response = null;
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("appId", 1);
		try {
			response = Unirest.get(signalServerBaseApi + "/SignalsServer/client/api/system/countries").queryString(paramsMap).asJson();
		} catch(Exception e) {
			throw e;
		}

		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed fetching affilite user data by creation time");
		}
		mapper = new ObjectMapper();
		rootNode = mapper.readTree(response.getRawBody());
		it = rootNode.get("result").getElements();

		while(it.hasNext()) {
			Object obj = (Object) it.next();
			allCountriesIso.add(mapper.readTree(obj.toString()).get("iso").toString());
		}

		paramsMap.put("appId", 1);
		paramsMap.put("removeBlocked", true);
		try {
			response = Unirest.get(signalServerBaseApi + "/SignalsServer/client/api/system/countries").queryString(paramsMap).asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed fetching affilite user data by creation time");
		}
		mapper = new ObjectMapper();
		rootNode = mapper.readTree(response.getRawBody());
		it = rootNode.get("result").getElements();

		while(it.hasNext()) {
			Object obj = (Object) it.next();
			enabledCountriesIso.add(mapper.readTree(obj.toString()).get("iso").toString());
		}

		allCountriesIso.removeAll(enabledCountriesIso);
		disabledCountriesIso = allCountriesIso;
		return disabledCountriesIso;
	}

	public static List<String> getBrandEnabledCountryIso(String signalServerBaseApi) throws Exception {
		Iterator<?> it = null;
		List<String> enabledCountriesIso = new ArrayList<>();
		HttpResponse<JsonNode> response = null;
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("appId", 1);
		paramsMap.put("removeBlocked", true);
		try {
			response = Unirest.get(signalServerBaseApi + "/SignalsServer/client/api/system/countries").queryString(paramsMap).asJson();
		} catch(Exception e) {
			throw e;
		}
		if(response.getBody().getObject().get("success").toString().equals("false")) {
			Assert.fail("Failed fetching affiliate user data by creation time");
		}
		ObjectMapper mapper = new ObjectMapper();
		org.codehaus.jackson.JsonNode rootNode = mapper.readTree(response.getRawBody());
		it = rootNode.get("result").getElements();

		while(it.hasNext()) {
			Object obj = (Object) it.next();
			enabledCountriesIso.add(mapper.readTree(obj.toString()).get("iso").toString());
		}

		return enabledCountriesIso;
	   }

	public static void updateRolePermissions(String baseUrl, String authToken, int roleId, String[] permissionsToAdd, String[] permissionsToRemove) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		Map<String, String> headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		JSONObject requestBody = new JSONObject();
		requestBody.put("id", roleId);

		JSONArray addPermissionsArray = new JSONArray();
		if (permissionsToAdd != null) {
			for (String permission : permissionsToAdd) {
				JSONObject permissionObj = new JSONObject();
				permissionObj.put("permission", permission);
				permissionObj.put("scope", "Self");
				addPermissionsArray.put(permissionObj);
			}
		}
		requestBody.put("addPermissions", addPermissionsArray);

		JSONArray removePermissionsArray = new JSONArray();
		if (permissionsToRemove != null) {
			for (String permission : permissionsToRemove) {
				JSONObject permissionObj = new JSONObject();
				permissionObj.put("permission", permission);
				permissionObj.put("scope", "Self");
				removePermissionsArray.put(permissionObj);
			}
		}
		requestBody.put("removePermissions", removePermissionsArray);

		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/roles/")
					.headers(headersMap)
					.body(requestBody.toString())
					.asJson();

			System.out.println("Response: " + response.getBody().toString());

		} catch (Exception e) {
			throw new UnirestException(e);
		}

		if (response.getStatus() != 200) {
			SelTestLog.warn("Failed updating permissions for role id " + roleId);
		}
	}

//	public static void updateRolePermissions(String baseUrl, String authToken, int roleId, String[] permissionsToAdd, String[] permissionsToRemove) throws UnirestException {
//
//		System.out.println("baseUrl : " + baseUrl);
//		System.out.println("authToken : " + authToken);
//		System.out.println("roleId : " + roleId);
//		System.out.println("permissionsToAdd : " + permissionsToAdd);
//		System.out.println("permissionsToRemove : " + permissionsToRemove);
//
//		HttpResponse<JsonNode> response = null;
//		headersMap = new HashMap<>();
//		headersMap.put("auth_token", authToken);
//		headersMap.put("Content-Type", "application/json");
//
//		String bodyOptions = "{\"id\":" + String.valueOf(roleId) + ",";
//		bodyOptions = bodyOptions + "\"addPermissions\":[";
//		if(permissionsToAdd != null) {
//			for(int i = 0; i < permissionsToAdd.length; i++) {
//				bodyOptions = bodyOptions + "{\"permission\":" + "\"" + permissionsToAdd[i] + "\"," + "\"scope\":\"Self\"" + "}";
//				if(i < permissionsToAdd.length - 1) {
//					bodyOptions = bodyOptions + ",";
//				}
//			}
//		}
//		bodyOptions = bodyOptions + "],";
//
//		bodyOptions = bodyOptions + "\"removePermissions\":[";
//		if(permissionsToRemove != null) {
//			for(int i = 0; i < permissionsToRemove.length; i++) {
//				bodyOptions = bodyOptions + "{\"permission\":" + "\"" + permissionsToRemove[i] + "\"," + "\"scope\":\"Self\"" + "}";
//				if(i < permissionsToRemove.length - 1) {
//					bodyOptions = bodyOptions + ",";
//				}
//			}
//		}
//		bodyOptions = bodyOptions + "]}";
//
//		System.out.println("bodyOptions : " + bodyOptions);
//		System.out.println("headersMap : " + headersMap);
//		System.out.println("bodyOptions :  " + baseUrl);
//
//		try {
//			response = Unirest.put(baseUrl + "/SignalsCRM/roles/").headers(headersMap).body(bodyOptions).asJson();
//
//			System.out.println("response : " + response);
//
//		} catch(Exception e) {
//			throw e;
//		}
//		if(response.getStatus() != 200) {
//			SelTestLog.warn("Failed updating permissions for role id " + roleId);
//		}
//	}

	public static void depositFromCrmAPI(String baseURL, String authToken, int brokerId, int amount)
			throws Exception {
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "https://" + baseURL;
		}
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("brokerUserId", brokerId);
		bodyOptions.put("amount", amount * 100);
		bodyOptions.put("method", "CreditCard");
		bodyOptions.put("pspTransactionId", "Test");
		bodyOptions.put("commentForUser", "Test");
		bodyOptions.put("comment", "Test");

		System.out.println("response of brokerId :" + brokerId);
		System.out.println("response of authToken :" + authToken);


		try {
			response = Unirest.post(url + "/SignalsCRM/brokers/bankings/deposit/manual").headers(headersMap)
					.body(bodyOptions.toString()).asJson();

			System.out.println("response of depositFromCrmAPI :" + response.getBody().toString());

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected to deposit successfully using api call",
				"Depositing using api call");
	}

	public static int withdrawFromCrmApi(String baseURL, String authToken, int brokerId, int amount, int fee)
			throws UnirestException {
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") && !baseURL.contains("https")) {
			url = "https://" + baseURL;
		}
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("brokerUserId", brokerId);
		bodyOptions.put("amount", amount * 100);
		bodyOptions.put("method", "CreditCard");
		bodyOptions.put("pspTransactionId", "Test");
		bodyOptions.put("commentForUser", "Test");
		bodyOptions.put("comment", "Test");
		bodyOptions.put("fee", fee);
		try {
			response = Unirest.post(url + "/SignalsCRM/brokers/bankings/withdrawal").headers(headersMap)
					.body(bodyOptions.toString()).asJson();
			System.out.println("response of withdrawFromCrmApi :" + response.getBody().toString());

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected to withdraw successfully using api call, response body: " + response.getBody().toString(),
				"Withdrawing using api call");

		return Integer.parseInt(response.getBody().getObject().getJSONObject("result").get("id").toString());
	}

	public static void approveWithdrawalApi(String baseURL, String authToken, int brokerBankingId)
			throws UnirestException {
		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") && !baseURL.contains("https")) {
			url = "https://" + baseURL;
		}
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("brokerBankingId", brokerBankingId);
		bodyOptions.put("method", "CreditCard");
		bodyOptions.put("comment", "Test");
		try {
			response = Unirest.post(url + "/SignalsCRM/brokers/bankings/withdrawal/approve").headers(headersMap)
					.body(bodyOptions.toString()).asJson();
			System.out.println("response of approveWithdrawalApi :" + response.getBody().toString());

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected to approve withdrawal successfully using api call, response body: " + response.getBody().toString(),
				"Approving withdrawal using api call");
	}

	public static HttpResponse<JsonNode> updateUserSalesStatus(String signalsCRMBaseURL, String authToken, long userId, int salesStatusIndex) throws Exception {
		headersMap = new HashMap<>();
		paramsMap = new HashMap<>();
		HttpResponse<JsonNode> response;
		headersMap.put("Content-Type", "application/json");
		headersMap.put("auth_token", authToken);
		paramsMap.put("userId", userId);
		paramsMap.put("salesStatus", salesStatusIndex);
		try {
			response = Unirest.post(signalsCRMBaseURL + "/SignalsCRM/users/salesstatus").headers(headersMap)
					.queryString(paramsMap).asJson();
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
		return response;
	}


	//WARN  2024-03-08 16:19:44,991 automatedtests_-_null [main] [com.antelope.af.restapi.UniRestUsersController.clearAllCachesApi] java.lang.RuntimeException: java.lang.RuntimeException: org.json.JSONException: A JSONArray text must start with '[' at 1 [character 2 line 1]
	public static void clearAllCachesApi(String baseURL, String authToken) throws UnirestException {
		HttpResponse<JsonNode> response;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "https://" + baseURL;
		}
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		String bodyOptions = "[\"activityLogCache\",\"affiliateCache\",\"aggregatedTradingDetailsCache\",\"appCache\",\"brokerCache\",\"callLogCache\",\"callbackCache\",\"credentialsCache\",\"currencyCache\"," +
				"\"currencyNormalizationRateCache\",\"customFiltersCache\",\"dashboardCache\",\"departmentCache\",\"deskCache\",\"entityCache\",\"extendedTotpValidationCache\",\"filterCache\",\"kycCache\"," +
				"\"leaderboardCache\",\"manualCallCache\",\"menuItemCache\",\"officeCache\",\"operatorCache\",\"operatorCallbackCache\",\"operatorLastLogonCache\",\"operatorsCache\",\"parameterCache\"," +
				"\"platformTypeCache\",\"pollingCache\",\"questionnaireCache\",\"quickMessagesCache\",\"reportCache\",\"roleCache\",\"scheduledReportCache\",\"securityAttemptsCache\",\"selectionCache\"," +
				"\"stringMessageCache\",\"tokenCache\",\"tradesCache\",\"userPlatformDataCache\",\"userTotalsCache\",\"userTradingDetailsCache\",\"userTypeCache\",\"visitCache\",\"widgetsCache\"]";

		try {
			response = Unirest.post(url + "/SignalsCRM/caches/clear").headers(headersMap)
					.body(bodyOptions).asJson();
			System.out.println("RES clearAllCachesApi: " + response.getBody().getObject());
			if(Boolean.parseBoolean(response.getBody().getObject().get("success").toString()) == false) {
				ExtendedReporter.log("response body: " + response.getBody().getObject().get("error"), RED);
			}
		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected to clear all caches successfully using api call",
				"Clearing all caches using api call");
//		return response.getBody().getObject().get("success").toString();
	}

	public static boolean deleteTradingAccountViaApi(int accountId, String baseURL, String authToken)
			throws Exception {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("brokerUserId", accountId);

		try {
			response = Unirest.delete(baseURL + "/SignalsCRM/brokers/users").headers(headersMap).queryString(paramsMap).asJson();

			System.out.println("res : " + response.getBody().toString());

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed deleting trading account: " + accountId + "!!!!");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			return false;
		}
		return true;
	}

	public static HttpResponse<JsonNode> getUserTradingAccountsDetails(String baseUrl, String authToken, String userID)
			throws UnirestException {
		HttpResponse<JsonNode> response;

		System.out.println("UID  :" + userID);
		System.out.println("baseUrl  :" + baseUrl + "/SignalsCRM/brokers/users/" + userID);

		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");
		try {
			response = Unirest.get(baseUrl + "/SignalsCRM/brokers/users/" + userID).headers(headersMap).asJson();

			System.out.println("response of getUserTradingAccountsDetails :" + response.getBody().toString());

		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed getting accounts for user with id " + userID + "!!!!");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
			return null;
		}

		return response;
	}

	public static HashMap<Integer, String> getUserTradingAccountsCurrencies(String baseUrl, String authToken, String userID) throws UnirestException {
		HashMap<Integer, String> accountsCurrencies = new HashMap<>();

		HttpResponse<JsonNode> response = getUserTradingAccountsDetails(baseUrl, authToken, userID);

		System.out.println("LOHGGGGGGGGG getUserTradingAccountsCurrencies:" + response.getBody().toString());

		JSONArray accounts = response.getBody().getObject().getJSONArray("result");

		for(int i = 0; i < accounts.length(); i++) {
			accountsCurrencies.put((Integer) ((JSONObject) accounts.get(i)).get("id"), ((JSONObject) accounts.get(i)).get("currency").toString());
			System.out.println("LOGHGGGGG accountsCurrencies 2222 :" + accountsCurrencies);
		}

		return accountsCurrencies;
	}

	public static HttpResponse<JsonNode> moveUserToRetentionApi(String baseUrl, String authToken, long userID) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/x-www-form-urlencoded");
		JSONObject bodyOptions = new JSONObject();

		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/users/acquisitionstatus?userId=" + userID + "&acquisitionStatus=Retention").headers(headersMap).body(bodyOptions.toString()).asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed moving user with id " + userID + " to retention acquisition status!!!!");
			Assert.fail();
			return null;
		}
		return response;
	}

	public static HttpResponse<JsonNode> moveUserToSalesApi(String baseUrl, String authToken, long userID) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/x-www-form-urlencoded");
		JSONObject bodyOptions = new JSONObject();

		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/users/acquisitionstatus?userId=" + userID + "&acquisitionStatus=Sales").headers(headersMap).body(bodyOptions.toString()).asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed moving user with id " + userID + " to sales acquisition status!!!!");
			Assert.fail();
			return null;
		}
		return response;
	}

	public static HttpResponse<JsonNode> assignUserSalesDeskAndRepApi(String baseUrl, String authToken, long userID, long deskId, long repId) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/x-www-form-urlencoded");
		JSONObject bodyOptions = new JSONObject();

		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/users/salesstatus?userId=" + userID + "&salesDeskId=" + deskId + "&salesRep=" + repId).headers(headersMap).body(bodyOptions.toString()).asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed assigning user with id " + userID + " to sales desk with id " + deskId + " and/or sales rep with id " + repId + "!!!!");
			Assert.fail();
			return null;
		}
		return response;
	}

	public static HttpResponse<JsonNode> setOperatorGoalsApi(String baseURL, String authToken, long operatorId, int dailyGoal, int weeklyGoal, int monthlyGoal, int skyGoal) throws UnirestException {
		System.out.println("baseURL :" + baseURL);
		System.out.println("authToken :" + authToken);
		System.out.println("operatorId :" + operatorId);
		System.out.println("dailyGoal :" + dailyGoal);
		System.out.println("weeklyGoal :" + weeklyGoal);
		System.out.println("monthlyGoal :" + monthlyGoal);
		System.out.println("skyGoal :" + skyGoal);

		HttpResponse<JsonNode> response = null;
		String url = baseURL;
		if(!baseURL.contains("http") || !baseURL.contains("https")) {
			url = "https://" + baseURL;
		}
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		String bodyOptions = "{\"id\":" + operatorId + ",\"dailyGoal\":" + dailyGoal * 100 + ",\"weeklyGoal\":" + weeklyGoal * 100 + ",\"monthlyGoal\":" + monthlyGoal * 100 + ",\"monthlySkyGoal\":" + skyGoal * 100 + ",\"appId\":1}";

		System.out.println("headersMap : " + headersMap);
		System.out.println("url : " + url + "/SignalsCRM/operators");
		System.out.println("bodyOptions : " + bodyOptions);

		try {
			response = Unirest.put(url + "/SignalsCRM/operators").headers(headersMap)
					.body(bodyOptions).asJson();

			System.out.println("response setOperatorGoalsApi:" + response.getBody().toString());

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed setting operator goals for operator with id " + operatorId + "!!!!");
			Assert.fail();
			return null;
		}
		return response;
	}

	public static void changeUserPassword(String signalsCrmBaseURL, String authToken, int userId, String newPassword)
			throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", authToken);
		headersMap.put("Content-Type", "application/json");

		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("userId", userId);
		paramsMap.put("newPassword", newPassword);

		try {
			response = Unirest.post(signalsCrmBaseURL + "/SignalsCRM/users/changepassword").headers(headersMap).queryString(paramsMap).asJson();

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		if(!response.getBody().getObject().get("success").toString().equals("true") && !response.getBody().toString().contains("Row was updated or deleted by another transaction")) {
			SelTestLog.warn("Failed changing password for user with id " + userId);
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
		}
	}

	public static void changeUserPasswordByUserAuthToken(String signalsServerBaseURL, String userAuthToken, String newPassword)
			throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("auth_token", userAuthToken);
		headersMap.put("Content-Type", "application/json");

		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("newPassword", newPassword);

		try {
			response = Unirest.post(signalsServerBaseURL + "/SignalsServer/users/change-expired-password").headers(headersMap).queryString(paramsMap).asJson();

		} catch(Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}

		if(!response.getBody().getObject().get("success").toString().equals("true") && !response.getBody().toString().contains("The new password matches the current password")) {
			SelTestLog.warn("Failed changing password for user with authToken " + userAuthToken);
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
		}

//        if(response.getBody().toString().contains("The new password matches the current password")){
//            String newGeneratedPassword = newPassword.replaceAll("new123", "") + WebElementUtils.getRandomNumberByRange(0, 100000);
//            paramsMap.replace("newPassword", newGeneratedPassword);
//            try {
//                response = Unirest.post(signalsServerBaseURL + "/SignalsServer/users/change-expired-password").headers(headersMap).queryString(paramsMap).asJson();
//
//            } catch (Exception e) {
//                if (!response.getBody().getObject().get("success").toString().equals("true")) {
//                    SelTestLog.warn("Failed changing password for user with authToken " + userAuthToken);
//                    ExtendedReporter.log( "newGeneratedPassword = " + newGeneratedPassword + "\n" + "response body: " + response.getBody().toString(), RED);
//                    Assert.fail();
//                }
//            }
//        }
	}

	public static String getUserDetailViaApi(String baseURL, String token, String userEmail, String requestedField) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		Gson gson = new Gson();
		JsonObject innerObject = new JsonObject();
		innerObject.addProperty("start", 0);
		innerObject.addProperty("limit", 50);
		innerObject.addProperty("orderBy", "id");
		innerObject.addProperty("orderDesc", true);
		innerObject.addProperty("status", "Registered");
		innerObject.addProperty("returnColumn", requestedField);
		String jsonBody = gson.toJson(innerObject);
		String val = " \"emails\":";
		jsonBody = jsonBody.replace("}", "," + val + "[" + '"' + userEmail + '"' + "]}");

		response = Unirest.post(baseURL + "/SignalsCRM/users/search").header("Content-Type", "application/json")
				.header("auth_token", token).body(jsonBody).asJson();
		return response.getBody().getObject().get("result").toString().replace("[", "").replace("]", "").replace("\"", "").trim();
	}

	public static String getUserDetailViaApi(String baseURL, String token, int userId, String requestedField) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		Gson gson = new Gson();
		JsonObject innerObject = new JsonObject();
		innerObject.addProperty("start", 0);
		innerObject.addProperty("limit", 50);
		innerObject.addProperty("orderBy", "id");
		innerObject.addProperty("orderDesc", true);
		innerObject.addProperty("status", "Registered");
		innerObject.addProperty("returnColumn", requestedField);
		String jsonBody = gson.toJson(innerObject);
		String val = " \"ids\":";
		jsonBody = jsonBody.replace("}", "," + val + "[" + '"' + userId + '"' + "]}");

		response = Unirest.post(baseURL + "/SignalsCRM/users/search").header("Content-Type", "application/json")
				.header("auth_token", token).body(jsonBody).asJson();
		return response.getBody().getObject().get("result").toString().replace("[", "").replace("]", "").replace("\"", "").trim();
	}

	public static String updateUserDetailViaApi(String baseURL, String token, int userId, String filedToUpdate, String value) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		Gson gson = new Gson();
		JsonObject innerObject = new JsonObject();
		innerObject.addProperty("id", userId);
		innerObject.addProperty(filedToUpdate, value);
		String jsonBody = gson.toJson(innerObject);

		response = Unirest.put(baseURL + "/SignalsServer/users").header("Content-Type", "application/json")
				.header("auth_token", token).body(jsonBody).asJson();
		return response.getBody().getObject().get("result").toString().replace("[", "").replace("]", "").replace("\"", "").trim();
	}

	public static void logoutFromCRM(String baseUrl, String authToken) throws UnirestException {
		HttpResponse<JsonNode> response;
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		headersMap.put("auth_token", authToken);

		try {
			response = Unirest.post(baseUrl + "/SignalsCRM/operators/logout").headers(headersMap).asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		if(!response.getBody().getObject().get("success").toString().equals("true")) {
			SelTestLog.warn("Failed to logout from operator");
			ExtendedReporter.log("response body: " + response.getBody().toString(), RED);
			Assert.fail();
		}
	}

}
