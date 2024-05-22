package com.mycompany.app.restapi;

import com.antelope.af.bugmanipulator.Asserter;
import com.antelope.af.logging.SelTestLog;
import com.antelope.af.utilities.WebElementUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;


public class UniRestContollerUtilities {

	private static HashMap<String, String> headersMap;
	private static String password = "testtest123";

	public static void unsuccessfulRegisterAffiliateUserUsingExistingEmail(String baseURL, String apiKey,
																		   String existingEmail) throws Exception {
		HttpResponse<JsonNode> response = null;
		HashMap<String, Object> paramsMap = new HashMap<>();
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		paramsMap.put("firstname", "test");
		paramsMap.put("lastname", "automation");
		paramsMap.put("telephone", Instant.now().toEpochMilli());
		paramsMap.put("email", existingEmail);
		paramsMap.put("password", password);
		paramsMap.put("countryiso", "dk");
		paramsMap.put("apikey", apiKey);
		paramsMap.put("sc", "automation env");
		try {
			response = Unirest.post(baseURL + "/SignalsServer/api/registerUser").headers(headersMap)
					.queryString(paramsMap).asJson();
		} catch (Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("false"),
				"Expected registering using existing email to be unsuccessful",
				"Validating that registering user using exisiting email is unsuccesfful");
	}

	public static void unsuccessfulRegisterAffiliateUserUsingDisabledCountry(String baseURL, String apiKey)
			throws Exception {
		String genratedEmail = "";
		HttpResponse<JsonNode> response = null;
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("firstname", "test");
		paramsMap.put("lastname", "automation");
		paramsMap.put("telephone", Instant.now().toEpochMilli());
		genratedEmail = "automation+1" + WebElementUtils.getRandomNumberByRange(0, 10000) + "@antelopesystem.com";
		paramsMap.put("email", genratedEmail);
		paramsMap.put("password", password);

		List<String> brandDisabledCountriesIso = UniRestUsersController.getBrandDisabledCountryIso(baseURL);
		brandDisabledCountriesIso.remove("\"XX\"");
		if (!brandDisabledCountriesIso.isEmpty()) {
			int randomEnabledIso = WebElementUtils.getRandomNumberByRange(0, brandDisabledCountriesIso.size() - 1);
			String randomDisabledCountry = brandDisabledCountriesIso.get(randomEnabledIso).replaceAll("\"", "");
			paramsMap.put("countryiso", randomDisabledCountry);
			paramsMap.put("apikey", apiKey);
			paramsMap.put("sc", "automation env");
			try {
				response = Unirest.post(baseURL + "/SignalsServer/api/registerUser?").queryString(paramsMap).asJson();
			} catch (Exception e) {
				SelTestLog.warn(e.getMessage());
				throw e;
			}
			Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("false"),
					"Expected registering using disabled country to be unsuccessful",
					"Validating that registering user using disabled country is unsuccesfful");
		}
	}

	public static void unsuccessfulRegisterAffiliateUserUsingWrongPassword(String baseURL, String apiKey)
			throws Exception {
		String genratedEmail = "";
		HttpResponse<JsonNode> response = null;
		HashMap<String, Object> paramsMap = new HashMap<>();
		paramsMap.put("firstname", "test");
		paramsMap.put("lastname", "automation");
		paramsMap.put("telephone", Instant.now().toEpochMilli());
		genratedEmail = "automation+1" + WebElementUtils.getRandomNumberByRange(0, 10000) + "@antelopesystem.com";
		paramsMap.put("email", genratedEmail);
		paramsMap.put("password", "1!@@@xw");
		paramsMap.put("countryiso", "dk");
		paramsMap.put("apikey", apiKey);
		paramsMap.put("sc", "automation env");
		try {
			response = Unirest.post(baseURL + "/SignalsServer/api/registerUser?").queryString(paramsMap).asJson();
		} catch (Exception e) {
			SelTestLog.warn(e.getMessage());
			throw e;
		}
		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("false"),
				"Expected registering using wrong password to be unsuccessful",
				"Validating that registering user using wrong password is unsuccesfful");
	}

	public static void changeAppSwitches(String baseUrl, String authToken, int appId, String key, boolean value) throws UnirestException {
		HttpResponse<JsonNode> response = null;
		HashMap<String, Object> paramsMap = new HashMap<>();
		headersMap = new HashMap<>();
		headersMap.put("Content-Type", "application/json");
		headersMap.put("auth_token", authToken);
		JSONObject bodyOptions = new JSONObject();
		bodyOptions.put("id", appId);
		bodyOptions.put(key, value);
		try {
			response = Unirest.put(baseUrl + "/SignalsCRM/apps").headers(headersMap).body(bodyOptions.toString())
					.asJson();
		} catch(Exception e) {
			SelTestLog.error(e.getMessage());
			throw e;
		}
		Asserter.assertTrue(response.getBody().getObject().get("success").toString().equals("true"),
				"Expected changing \"" + key + "\" to \""+ value + "\" to be successful",
				"Changed \"" + key + "\" to \""+ value + "\"successfully");
	}
}
