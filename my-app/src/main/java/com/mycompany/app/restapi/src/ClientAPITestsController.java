package com.mycompany.app.restapi.src;

import com.antelope.af.utilities.WebdriverUtils;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Client API Tests Controller
 *
 * */
public class ClientAPITestsController extends APITestsController{
    protected String firstName = "Test";
    protected String lastName = "Automation";
    protected String langIso = "EN";
    protected String countryIso;
    protected String telephonePrefix = "972";
    protected String telephone = "12345678";

    public APIResponse postUsersAuthenticate(HashMap<String, Object> params) throws UnirestException {
        return apiCall("post", clientApiBaseUrl + "/SignalsServer/users/authenticate", "No Token", params);
    }

    public APIResponse postUsersChangePassword(String authToken, HashMap<String, Object> params) throws UnirestException {
            return apiCall("post", clientApiBaseUrl + "/SignalsServer/users/changepassword", authToken, params);
    }

    public APIResponse getUsersForgotPassword(String authToken, HashMap<String, Object> params) throws UnirestException {
        return apiCall("get", clientApiBaseUrl + "/SignalsServer/users/forgotpassword", authToken, params);
    }

//    public APIResponse getUsersForgotPasswordReset(String authToken, HashMap<String, Object> params) throws UnirestException {
//        headersMap = new HashMap<>();
//        headersMap.put("Content-Type", "application/json");
//        headersMap.put("auth_token", authToken);
//
//        httpResponse = Unirest.get(clientAPISignalsServer + "/SignalsServer/users/forgotpassword/reset").headers(headersMap).queryString(params)
//                .asJson();
//
//        return convertHttpResponseToAPIResponse(httpResponse);
//    }8

    public APIResponse getUsers(String authToken, HashMap<String, Object> params) throws UnirestException {
        return apiCall("get", clientApiBaseUrl + "/SignalsServer/client/api/users/", authToken, params);
    }

    public APIResponse putUsers(String authToken, JSONObject body) throws UnirestException {
        return apiCall("put", clientApiBaseUrl + "/SignalsServer/client/api/users", authToken, body);
    }

    public APIResponse postUsersRegister(JSONObject body) throws UnirestException {
        return apiCall("post", clientApiBaseUrl + "/SignalsServer/client/api/users/register","No Token",body);
    }

    public APIResponse postUsersRegisterDemo(JSONObject body) throws UnirestException {
        return apiCall("post", clientApiBaseUrl + "/SignalsServer/users/register/demo","No Token",body);
    }

    public APIResponse getSystemCountries(String authToken, HashMap<String, Object> params) throws UnirestException {
        return apiCall("get", clientApiBaseUrl + "/SignalsServer/system/countries",authToken,params);
    }

    public APIResponse getTrackingVisit(String authToken, HashMap<String, Object> params) throws UnirestException {
        return apiCall("get", clientApiBaseUrl + "/SignalsServer/tracking/visit", authToken, params);
    }

    public APIResponse deleteDocuments(String authToken, HashMap<String, Object> params) throws UnirestException {
        return apiCall("delete", clientApiBaseUrl + "/SignalsServer/tracking/visit", authToken, params);
    }

    public APIResponse getDocuments(String authToken, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.get(clientApiBaseUrl + "/SignalsServer/documents").headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse postDocumentsUpload(String authToken, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.post(clientApiBaseUrl + "/SignalsServer/documents/upload").headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse getBrokersUsers(String authToken, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.get(clientApiBaseUrl + "/SignalsServer/brokers/users").headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse postBrokersUsersRegister(String authToken, JSONObject body) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.post(clientApiBaseUrl + "/SignalsServer/brokers/users/register").headers(headersMap).body(body.toString())
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse getBrokersUsersOpenTrades(String authToken, String tradingAccountId, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.get(clientApiBaseUrl + "/SignalsServer/brokers/users/" + tradingAccountId).headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse getBrokersUsersTradingHistory(String authToken, String tradingAccountId, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.get(clientApiBaseUrl + "/SignalsServer/brokers/users/" + tradingAccountId).headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse getWalletTransactions(String authToken, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.get(clientApiBaseUrl + "/SignalsServer/wallet/transactions").headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse postWalletDeposit(String authToken, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.post(clientApiBaseUrl + "/SignalsServer/wallet/deposit").headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse postWalletTransfer(String authToken, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.post(clientApiBaseUrl + "/SignalsServer/wallet/transfer").headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse deleteWalletWithdrawal(String authToken, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.delete(clientApiBaseUrl + "/SignalsServer/wallet/withdrawal").headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse postWalletWithdrawal(String authToken, HashMap<String, Object> params) throws UnirestException {
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        headersMap.put("auth_token", authToken);

        httpResponse = Unirest.post(clientApiBaseUrl + "/SignalsServer/wallet/withdrawal").headers(headersMap).queryString(params)
                .asJson();

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public String postUsersRegister() throws Exception {

        String email = WebdriverUtils.generateEmail();
        globalBodyJSONObject.put("email", email);
        globalBodyJSONObject.put("password", "testtest123");
        globalBodyJSONObject.put("firstName", firstName);
        globalBodyJSONObject.put("lastName", lastName);
        globalBodyJSONObject.put("countryIso", getRandomCountryIso());
        globalBodyJSONObject.put("languageIso", langIso);
        globalBodyJSONObject.put(  "telephone", telephone);
        globalBodyJSONObject.put(  "telephonePrefix", telephonePrefix);


        response = postUsersRegister(globalBodyJSONObject);
        clientAuthToken = response.getResult().get("authToken").toString();
        return email;
    }

    public void clientApiAuthenticateUser() throws UnirestException {
        globalParamsHashMap.clear();
        globalParamsHashMap.put("email", "automation+12546858@antelopesystem.com");
        globalParamsHashMap.put("password", "testtest123");

        response = postUsersAuthenticate(globalParamsHashMap);
        clientAuthToken = response.getResult().get("authToken").toString();
    }

}
