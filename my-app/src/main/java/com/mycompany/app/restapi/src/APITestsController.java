package com.mycompany.app.restapi.src;

import com.antelope.af.restapi.UniRestUsersController;
import com.antelope.af.testconfig.testPatterns.commons.CRMSystemBaseTest;
import com.antelope.af.utilities.WebElementUtils;
import com.jcraft.jsch.JSchException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;

import java.util.HashMap;
import java.util.List;

/**
 * Client API Tests Controller
 **/

public class APITestsController {

    protected HashMap<String, String> headersMap = null;
    protected HashMap<String, Object> globalParamsHashMap = new HashMap<>();
    protected JSONObject globalBodyJSONObject = new JSONObject();

    protected String clientAuthToken;
    protected static String operatorAuthToken;
    public static String clientApiBaseUrl, crmApiBaseUrl, apiSignalsCrm;


    protected HttpResponse<JsonNode> httpResponse;
    protected APIResponse response;

    @BeforeClass
    public void initApiUrls() throws JSchException {

        if(brandName.equals("Antelope_Staging")) {
            clientApiBaseUrl = "https://api-staging-all.antelopesystem.com";
            crmApiBaseUrl = "https://apicrm-staging-all.antelopesystem.com";
            apiSignalsCrm = "https://apicrm-staging-all.antelopesystem.com/SignalsCRM/";
        } else if(brandName.equals("Antelope_Automation")) {
            clientApiBaseUrl = "https://ant-automation-fe-api.antelopesystem.com";
            crmApiBaseUrl = "https://ant-automation-apicrm.antelopesystem.com";
            apiSignalsCrm = "https://ant-automation-apicrm.antelopesystem.com/SignalsCRM/";
        }
    }

    private void initToken(String authToken) {
//        String callerClass = Thread.currentThread().getStackTrace()[3].getClassName().substring(Thread.currentThread().getStackTrace()[3].getClassName().lastIndexOf(".") + 1);
        String tokenKeyName = Thread.currentThread().getStackTrace()[3].getClassName().contains("CrmAPITestsController") ? "x-crm-api-token" : "auth_token";
        headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        if(authToken == null)
            headersMap.put(tokenKeyName, null);
        else if(!authToken.equalsIgnoreCase("No Token"))
            headersMap.put(tokenKeyName, authToken);
    }

    public APIResponse apiCall(String method, String apiUrl, String authToken, HashMap<String, Object> params) throws UnirestException {
        initToken(authToken);
        switch (method.toLowerCase()) {
            case "post":
                try {
                    httpResponse = Unirest.post(apiUrl).headers(headersMap).queryString(params).asJson();
                } catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.post(apiUrl).headers(headersMap).queryString(params).asString().getBody());
                }
                break;

            case "get":
                try {
                    httpResponse = Unirest.get(apiUrl).headers(headersMap).queryString(params).asJson();
                } catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.get(apiUrl).headers(headersMap).queryString(params).asString().getBody());
                }
                break;

            case "put":
                try {
                    httpResponse = Unirest.put(apiUrl).headers(headersMap).queryString(params).asJson();
                } catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.put(apiUrl).headers(headersMap).queryString(params).asString().getBody());
                }
                break;

            case "delete":
                try {
                    httpResponse = Unirest.delete(apiUrl).headers(headersMap).queryString(params).asJson();
                } catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.delete(apiUrl).headers(headersMap).queryString(params).asString().getBody());
                }
                break;

            case "options":
                try {
                    httpResponse = Unirest.options(apiUrl).headers(headersMap).queryString(params).asJson();
                } catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.options(apiUrl).headers(headersMap).queryString(params).asString().getBody());
                }
                break;
        }

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public APIResponse apiCall(String method, String apiUrl, String authToken, JSONObject body) throws UnirestException {
        initToken(authToken);

        switch (method.toLowerCase()) {
            case "post":
                try {
                    httpResponse = Unirest.post(apiUrl).headers(headersMap).body(body.toString()).asJson();
                }
                catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.post(apiUrl).headers(headersMap).body(body.toString()).asString().getBody());
                }
                break;

            case "put":
                try {
                    httpResponse = Unirest.put(apiUrl).headers(headersMap).body(body.toString()).asJson();
                }
                catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.put(apiUrl).headers(headersMap).body(body.toString()).asString().getBody());
                }
                break;

            case "delete":
                try {
                    httpResponse = Unirest.delete(apiUrl).headers(headersMap).body(body.toString()).asJson();
                }
                catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.delete(apiUrl).headers(headersMap).body(body.toString()).asString().getBody());
                }
                break;

            case "options":
                try {
                    httpResponse = Unirest.options(apiUrl).headers(headersMap).body(body.toString()).asJson();
                }
                catch (UnirestException e) {
                    return convertFailedHttpResponseToAPIResponse(Unirest.options(apiUrl).headers(headersMap).body(body.toString()).asString().getBody());
                }
                break;
        }

        return convertHttpResponseToAPIResponse(httpResponse);
    }

    public static APIResponse convertHttpResponseToAPIResponse(HttpResponse<JsonNode> httpResponse){
        APIResponse apiResponse = new APIResponse();
        Boolean success;
        String error;
        JSONObject result = null;
        Object paging;

        success = (Boolean) httpResponse.getBody().getObject().get("success");
        try {
            error = httpResponse.getBody().getObject().getJSONObject("error").get("errorDetails").toString();
        }
        catch (Exception e){
            error = httpResponse.getBody().getObject().get("error").toString();
        }

        if(success.toString().equalsIgnoreCase("null"))
            success = null;
        if(error.toString().equalsIgnoreCase("null"))
            error = null;

        try {
            result = httpResponse.getBody().getObject().getJSONObject("result");
        }
        catch (JSONException e){
            if(httpResponse.getBody().getObject().get("result").toString().equalsIgnoreCase("null"))
                result = null;
        }

        apiResponse.setSuccess(success);
        apiResponse.setError(error);
        apiResponse.setResult(result);

        return apiResponse;
    }

    public static APIResponse convertFailedHttpResponseToAPIResponse(String error){
        APIResponse apiResponse = new APIResponse();
        apiResponse.setSuccess(false);
        apiResponse.setResult(null);
        apiResponse.setError(error);
        return apiResponse;
    }

    public void clearBodyJSON() {
        while(globalBodyJSONObject.length()>0)
            globalBodyJSONObject.remove(globalBodyJSONObject.keys().next());
    }

    public String getRandomCountryIso() throws Exception {
        List<String> brandEnabledCountriesIso = UniRestUsersController.getBrandEnabledCountryIso("https://api-staging-all.antelopesystem.com");
        int randomEnabledIso = WebElementUtils.getRandomNumberByRange(0, brandEnabledCountriesIso.size() - 1);
        return brandEnabledCountriesIso.get(randomEnabledIso).replaceAll("\"", "");
    }

}
