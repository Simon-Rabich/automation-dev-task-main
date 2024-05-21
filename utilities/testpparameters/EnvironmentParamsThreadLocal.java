package com.antelope.af.utilities.testpparameters;

import elasticsearch.ElasticSearchJavaHttpRestClient;

public class EnvironmentParamsThreadLocal {

    public enum EnvironmentEnum {
        SYSTEM, CLIENT_EMAIL, CLIENT_PASSWORD, CLIENT_ENV_LINK, ANTELOPE_CRM_EMAIL, ANTELOPE_CRM_ENV_LINK, ANTELOPE_CRM_PASSWORD, TEST_NAME, FAILED_IMAGE_NAME, MEHTOD_ORDER, CRM_TAB, CLIENT_TAB, SPECIFIC_CRM_CLIENT_TAB, LEADERBOARD, BASE_SIGNALS_CRM_API_URL, BASE_SIGNALS_SERVERS_API_URL, ANTELOPE_AUTH, BAFF_CRM_LINK, BAFF_EMAIL, BAFF_PASSWORD, BAFF_BASE_API, BAFF_SIGNAL_SERVERS_API, BAFF_SIGNAL_CRM_API, BAFF_AUTH, API_KEY, MT4, CIQ, OSYS, ELK_CLIENT;

        public String getName() {
            return this.name().replaceAll("_", " ");
        }
    }

    public static void setElkClient(ElasticSearchJavaHttpRestClient elkClient) {
        TestParamsThreadPool.put(EnvironmentEnum.ELK_CLIENT.getName(), elkClient);
    }

    public static ElasticSearchJavaHttpRestClient getElkClient() {
        return (ElasticSearchJavaHttpRestClient) TestParamsThreadPool.get(EnvironmentEnum.ELK_CLIENT.getName());
    }

    public static void setBrandSystem(String brandSystem) {
        TestParamsThreadPool.put(EnvironmentEnum.SYSTEM.getName(), brandSystem);
    }

    public static String getBrandSystem() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.SYSTEM.getName());
    }

    // mt4 trading platform
    public static boolean isMT4Brand() {
        return (boolean) TestParamsThreadPool.get(EnvironmentEnum.MT4.getName());
    }

    public static void setMT4Brand(boolean isMT4) {
        TestParamsThreadPool.put(EnvironmentEnum.MT4.getName(), isMT4);
    }

    // xyx ciq trading platform
    public static boolean isCiqBrand() {
        return (boolean) TestParamsThreadPool.get(EnvironmentEnum.CIQ.getName());
    }

    public static void setCiqBrand(boolean isCiq) {
        TestParamsThreadPool.put(EnvironmentEnum.CIQ.getName(), isCiq);
    }

    // Osys cyrptncy trading platform
    public static boolean isOsysBrand() {
        return (boolean) TestParamsThreadPool.get(EnvironmentEnum.OSYS.getName());
    }

    public static void setOsysBrand(boolean isOsys) {
        TestParamsThreadPool.put(EnvironmentEnum.OSYS.getName(), isOsys);
    }

    public static String getAntelopeSignalsCRMAPI() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BASE_SIGNALS_CRM_API_URL.getName());
    }

    public static void setAntelopeSignalSCRMApi(String api) {
        TestParamsThreadPool.put(EnvironmentEnum.BASE_SIGNALS_CRM_API_URL.getName(), api);
    }

    public static String getAntelopeSignalServersAPI() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BASE_SIGNALS_SERVERS_API_URL.getName());
    }

    public static void setAntelopeSignalSeversApi(String api) {
        TestParamsThreadPool.put(EnvironmentEnum.BASE_SIGNALS_SERVERS_API_URL.getName(), api);
    }

    public static String getAntelopeAPIKey() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.API_KEY.getName());
    }

    public static void setAntelopeAPIKey(String apikey) {
        TestParamsThreadPool.put(EnvironmentEnum.API_KEY.getName(), apikey);
    }

    public static String getBaffAuthToken() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BAFF_AUTH.getName());
    }

    public static void setBaffAuthToken(String auth) {
        TestParamsThreadPool.put(EnvironmentEnum.BAFF_AUTH.getName(), auth);
    }

    public static String getBaffSignalsCrmApiKey() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BAFF_SIGNAL_CRM_API.getName());
    }

    public static void setBaffSignalsCrmApiKey(String apiKey) {
        TestParamsThreadPool.put(EnvironmentEnum.BAFF_SIGNAL_CRM_API.getName(), apiKey);
    }

    public static String getBaffSignalServersApiKey() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BAFF_SIGNAL_SERVERS_API.getName());
    }

    public static void setBaffSignalServersApiKey(String apiKey) {
        TestParamsThreadPool.put(EnvironmentEnum.BAFF_SIGNAL_SERVERS_API.getName(), apiKey);
    }

    public static String getAntelopeAuthToken() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.ANTELOPE_AUTH.getName());
    }

    public static void setAntelopeAuthToken(String auth) {
        TestParamsThreadPool.put(EnvironmentEnum.ANTELOPE_AUTH.getName(), auth);
    }

    public static String getBaffBaseAPI() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BAFF_BASE_API.getName());
    }

    public static void setBaffBaseAPI(String baseAPI) {
        TestParamsThreadPool.put(EnvironmentEnum.BAFF_BASE_API.getName(), baseAPI);
    }

    public static String getAntelopeBaseAPI() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BASE_SIGNALS_CRM_API_URL.getName());
    }

    public static void setAntelopeBaseAPI(String baseAPI) {
        TestParamsThreadPool.put(EnvironmentEnum.BASE_SIGNALS_CRM_API_URL.getName(), baseAPI);
    }

    public static String getLeaderboardLink() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.LEADERBOARD.getName());
    }

    public static void setLeaderboard(String leaderboard) {
        TestParamsThreadPool.put(EnvironmentEnum.LEADERBOARD.getName(), leaderboard);
    }

    public static String getClientEmail() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.CLIENT_EMAIL.getName());
    }

    public static void setClientEmail(String clientEmail) {
        TestParamsThreadPool.put(EnvironmentEnum.CLIENT_EMAIL.getName(), clientEmail);
    }

    public static String getClientPassword() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.CLIENT_PASSWORD.getName());
    }

    public static void setClientPassword(String clientPassword) {
        TestParamsThreadPool.put(EnvironmentEnum.CLIENT_PASSWORD.getName(), clientPassword);
    }

    public static String getClientEnvironmentLink() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.CLIENT_ENV_LINK.getName());
    }

    public static void setClientEnvironmentLink(String clientEnvLink) {
        TestParamsThreadPool.put(EnvironmentEnum.CLIENT_ENV_LINK.getName(), clientEnvLink);
    }

    public static String getBaffCRMEmail() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BAFF_EMAIL.getName());
    }

    public static void setBaffCRMEmail(String email) {
        TestParamsThreadPool.put(EnvironmentEnum.BAFF_EMAIL.getName(), email);
    }

    public static String getAntelopeCRMEmail() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.ANTELOPE_CRM_EMAIL.getName());
    }

    public static void setAntelopeCRMEmail(String email) {
        TestParamsThreadPool.put(EnvironmentEnum.ANTELOPE_CRM_EMAIL.getName(), email);
    }

    public static String getBaffCRMEnvironmentLink() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BAFF_CRM_LINK.getName());
    }

    public static void setBaffCRMEnvironmentLink(String envLink) {
        TestParamsThreadPool.put(EnvironmentEnum.BAFF_CRM_LINK.getName(), envLink);
    }

    public static String getAntelopeCRMEnvironmentLink() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.ANTELOPE_CRM_ENV_LINK.getName());
    }

    public static void setAntelopeCRMEnvironmentLink(String envLink) {
        TestParamsThreadPool.put(EnvironmentEnum.ANTELOPE_CRM_ENV_LINK.getName(), envLink);
    }

    public static String getBaffPassword() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.BAFF_PASSWORD.getName());
    }

    public static void setBaffPassword(String password) {
        TestParamsThreadPool.put(EnvironmentEnum.BAFF_PASSWORD.getName(), password);
    }

    public static String getAntelopeCRMPassword() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.ANTELOPE_CRM_PASSWORD.getName());
    }

    public static void setAntelopeCRMPassword(String password) {
        TestParamsThreadPool.put(EnvironmentEnum.ANTELOPE_CRM_PASSWORD.getName(), password);
    }

    public static String getTestName() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.TEST_NAME.getName());
    }

    public static void setTestName(String testName) {
        TestParamsThreadPool.put(EnvironmentEnum.TEST_NAME.getName(), testName);
    }

    public static String getFailedImageName() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.FAILED_IMAGE_NAME.getName());
    }

    public static void setFailedImageName(String failedImageName) {
        TestParamsThreadPool.put(EnvironmentEnum.FAILED_IMAGE_NAME.getName(), failedImageName);
    }

    public static Object getMethodOrder() {
        return TestParamsThreadPool.get(EnvironmentEnum.MEHTOD_ORDER.getName());
    }

    public static void setMethodOrder(Object methodOrder) {
        TestParamsThreadPool.put(EnvironmentEnum.MEHTOD_ORDER.getName(), methodOrder);
    }

    public static String getCrmWindowHandle() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.CRM_TAB.getName());
    }

    public static void setCrmWindowHandle(Object crmWindowHandle) {
        TestParamsThreadPool.put(EnvironmentEnum.CRM_TAB.getName(), crmWindowHandle);
    }

    public static String getClientWindowHandle() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.CLIENT_TAB.getName());
    }

    public static void setClientWindowHandle(Object clientWindowHandle) {
        TestParamsThreadPool.put(EnvironmentEnum.CLIENT_TAB.getName(), clientWindowHandle);
    }

    public static String getSpecificCrmClientWindowHandle() {
        return (String) TestParamsThreadPool.get(EnvironmentEnum.SPECIFIC_CRM_CLIENT_TAB.getName());
    }

    public static void setSpecificCrmClientWindowHandle(Object specificClientWindowHandle) {
        TestParamsThreadPool.put(EnvironmentEnum.SPECIFIC_CRM_CLIENT_TAB.getName(), specificClientWindowHandle);
    }
}
