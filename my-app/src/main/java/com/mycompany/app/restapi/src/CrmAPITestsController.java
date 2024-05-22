package com.mycompany.app.restapi.src;

import com.antelope.af.restapi.UniRestUsersController;
import com.antelope.af.utilities.ExtendedReporter;
import com.antelope.af.utilities.WebdriverUtils;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;

public class CrmAPITestsController extends APITestsController{

    protected static String crmApiMasterAuthToken = "84f22283-1c0c-475b-92f6-e598b0eb1c5b"; // Can be obtained from crm_api_token table in DB
    protected static String crmApiTokenName = "CrmAPI - Staging CRM API (ID: 4)";
    private static String CRMEmail = "automation@antelopesystem.com";
    private static String CRMPassword = "!@2Ubrf@e5J7y";
    protected long userId;
    protected JSONObject userDetails;
    public JSONObject originalUser;

    @BeforeClass
    protected void init() throws Exception {
        getOperatorAuthToken();
        getNewUserId();
        getUserDetails();
        }

    public String getOperatorAuthToken() throws UnirestException {
        globalBodyJSONObject.put("email", CRMEmail);
        globalBodyJSONObject.put("password", CRMPassword);
        response = postOperatorsAuthenticate(globalBodyJSONObject);
        operatorAuthToken = getToken((JSONObject) response.getResult().get("tokenResponse"));
        return operatorAuthToken;
    }

    public long getNewUserId() throws Exception {
        ExtendedReporter.step("Creating new user");
        ClientAPITestsController clientAPITestsController = new ClientAPITestsController();
        String email = WebdriverUtils.generateEmail();
        globalBodyJSONObject.put("email", email);
        globalBodyJSONObject.put("password", "testtest123");
        globalBodyJSONObject.put("firstName", "Test");
        globalBodyJSONObject.put("lastName", "Automation");
        globalBodyJSONObject.put("countryIso", getRandomCountryIso());
        globalBodyJSONObject.put("languageIso", "EN");
        globalBodyJSONObject.put("telephone", "123456489");

        response = clientAPITestsController.postUsersRegister(globalBodyJSONObject);
//        userId = Long.parseLong(response.getResult().get("id").toString());  //TODO: Get userId...
        userId = Long.parseLong(UniRestUsersController.getUserDetailViaApi(crmApiBaseUrl,operatorAuthToken, email, "id"));
        ExtendedReporter.step("User was created with ID = " + userId);
        ExtendedReporter.log("User was created with ID = " + userId);
        clearBodyJSON();
        return userId;
    }

    public JSONObject getUserDetails() throws UnirestException {
        userDetails =  UniRestUsersController.getUsersDetailsFromSignalsCRM(apiSignalsCrm.split("\\/SignalsCRM/")[0], operatorAuthToken, userId);
        return userDetails;
    }

    public JSONObject getUserDetails(long userId) throws UnirestException {
        userDetails =  UniRestUsersController.getUsersDetailsFromSignalsCRM(apiSignalsCrm.split("\\/SignalsCRM/")[0], operatorAuthToken, userId);
        return userDetails;
    }

    public APIResponse postOperatorsAuthenticate(JSONObject body) throws UnirestException {
        return apiCall("post", apiSignalsCrm + "operators/authenticate-leaderboard", "no token", body);
    }

    public String getToken(JSONObject tokens) {
        return tokens.get("token").toString();
    }

    public APIResponse putUserSales(String authToken, HashMap<String, Object> params) throws UnirestException {
        return apiCall("put", apiSignalsCrm + "crm-api/sales", authToken, params);
    }

    public APIResponse putUserNinja(String authToken, HashMap<String, Object> params) throws UnirestException {
        return apiCall("put", apiSignalsCrm + "crm-api/ninja", authToken, params);
    }

    public APIResponse putUserRetention(String authToken, HashMap<String, Object> params) throws UnirestException {
        return apiCall("put", apiSignalsCrm + "crm-api/retention", authToken, params);
    }

    public APIResponse putUserKyc(String authToken, JSONObject body) throws UnirestException {
        return apiCall("put", apiSignalsCrm + "crm-api/kyc", authToken, body);
    }

}
