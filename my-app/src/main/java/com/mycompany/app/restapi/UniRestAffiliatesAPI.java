package com.mycompany.app.restapi;

import com.antelope.af.logging.SelTestLog;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;

import java.util.HashMap;

public class UniRestAffiliatesAPI {

    private static HashMap<String, String> headersMap;
    private static HashMap<String, Object> paramsMap;

    public static HttpResponse<JsonNode> getUser(String signalsServerBaseURL, String apiKey, long userId) throws Exception {
        headersMap = new HashMap<>();
        paramsMap = new HashMap<>();
        HttpResponse<JsonNode> response;
        headersMap.put("Content-Type", "application/json");
        paramsMap.put("userid", userId);
        paramsMap.put("apikey", apiKey);
        try {
            response = Unirest.get(signalsServerBaseURL + "/SignalsServer/api/getUser").headers(headersMap)
                    .queryString(paramsMap).asJson();
        } catch (Exception e) {
            SelTestLog.warn(e.getMessage());
            throw e;
        }
        return response;
    }

    public static HttpResponse<JsonNode> getUsers(String signalsServerBaseURL, String apiKey, int limit, String from, String to) throws Exception {
        headersMap = new HashMap<>();
        paramsMap = new HashMap<>();
        HttpResponse<JsonNode> response;
        headersMap.put("Content-Type", "application/json");
        paramsMap.put("apikey", apiKey);
        paramsMap.put("limit", limit);
        paramsMap.put("from", from);
        paramsMap.put("to", to);
        try {
            response = Unirest.get(signalsServerBaseURL + "/SignalsServer/api/getUsers").headers(headersMap)
                    .queryString(paramsMap).asJson();
        } catch (Exception e) {
            SelTestLog.warn(e.getMessage());
            throw e;
        }
        return response;
    }

    public static HttpResponse<JsonNode> getAffiliates(String signalsServerBaseURL, String apiKey) throws Exception {
        HttpResponse<JsonNode> response;
        headersMap = new HashMap<>();
        paramsMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        paramsMap.put("apikey", apiKey);
        try {
            response = Unirest.get(signalsServerBaseURL + "/SignalsServer/api/getAffiliates").headers(headersMap)
                    .queryString(paramsMap).asJson();
        } catch (Exception e) {
            SelTestLog.warn(e.getMessage());
            throw e;
        }
        return response;
    }

    public static HttpResponse<JsonNode> getWithdrawals(String signalsServerBaseURL, String apiKey, String userId) throws Exception {
        HttpResponse<JsonNode> response;
        headersMap = new HashMap<>();
        paramsMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");
        paramsMap.put("apikey", apiKey);
        paramsMap.put("userid", userId);

        try {
            response = Unirest.get(signalsServerBaseURL + "/SignalsServer/api/getWithdrawals").headers(headersMap)
                    .queryString(paramsMap).asJson();
        } catch (Exception e) {
            SelTestLog.warn(e.getMessage());
            throw e;
        }
        return response;
    }


    public static HttpResponse<JsonNode> getDeposits(String signalsServerBaseURL, String apiKey, long userId, String from, String to) throws Exception {
        headersMap = new HashMap<>();
        paramsMap = new HashMap<>();
        HttpResponse<JsonNode> response;
        headersMap.put("Content-Type", "application/json");
        paramsMap.put("apikey", apiKey);
        paramsMap.put("userid", userId);
        paramsMap.put("from", from);
        paramsMap.put("to", to);
        try {
            response = Unirest.get(signalsServerBaseURL + "/SignalsServer/api/getDeposits").headers(headersMap)
                    .queryString(paramsMap).asJson();

            System.out.println("response of getDeposits :" + response.getBody().toString());


        } catch (Exception e) {
            SelTestLog.warn(e.getMessage());
            throw e;
        }
        return response;
    }

    public static HttpResponse<JsonNode> getSalesStatuses(String signalsServerBaseURL, String apiKey, String from, String to) throws Exception {
        headersMap = new HashMap<>();
        paramsMap = new HashMap<>();
        HttpResponse<JsonNode> response;
        headersMap.put("Content-Type", "application/json");
        paramsMap.put("apikey", apiKey);
        paramsMap.put("from", from);
        paramsMap.put("to", to);
        try {
            response = Unirest.get(signalsServerBaseURL + "/SignalsServer/api/getSalesStatuses").headers(headersMap)
                    .queryString(paramsMap).asJson();
        } catch (Exception e) {
            SelTestLog.warn(e.getMessage());
            throw e;
        }
        return response;
    }

}
