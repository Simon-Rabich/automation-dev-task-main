package com.mycompany.app.restapi.src;

import com.antelope.af.antelope.components.crm.antelopecrm.AntelopeGridTable;
import com.antelope.af.antelope.components.crm.antelopecrm.CRMLeftMenu;
import com.antelope.af.antelope.components.crm.antelopecrm.CRMNavigatorTab;
import com.antelope.af.antelope.components.crm.antelopecrm.NgxGridTable;
import com.antelope.af.antelope.components.crm.antelopecrm.forms.AntelopeComplianceInformationForm;
import com.antelope.af.antelope.components.crm.antelopecrm.forms.FormDropdown;
import com.antelope.af.antelope.containers.crm.common.ServerPageContainer;
import com.antelope.af.bugmanipulator.Asserter;
import com.antelope.af.utilities.ExtendedReporter;
import com.antelope.af.utilities.WebdriverUtils;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;


public class CrmAPIBaseTest extends CrmAPITestsController{

    private int expectedSalesDesk = 70;
    private String expectedSalesDeskDisplay = "CRM API Sales Desk [Do not edit]";
    private int expectedSalesRep = 581;
    private String expectedSalesRepDisplay = "Sales1 Rep";
    private int expectedSalesStatus = 3;
    private String expectedSalesStatusDisplay = "CallBack";

    private int expectedRetentionDesk = 71;
    private String expectedRetentionDeskDisplay = "CRM API Retention Desk [Do not edit]";
    private int expectedRetentionRep = 594;
    private String expectedRetentionRepDisplay = "Ret1 Rep";
    private int expectedRetentionStatus = 3;
    private String expectedRetentionStatusDisplay = "No Answer";
    private int expectedClientPotential = 1;
    private String expectedClientPotentialDisplay = "Basic";
    private int expectedTradingStyle = 2;
    private String expectedTradingStyleDisplay = "Semi-Automatic";

    private int expectedNinjaDesk = 72;
    private String expectedNinjaDeskDisplay = "CRM API Ninja Desk [Do not edit]";

    private int expectedKycRep = 675;
    private String expectedKycRepDisplay = "Automation Support [Do not edit]";
    private int expectedKycStatus = 4;
    private String expectedKycStatusDisplay = "Approved";
    private String expectedKycNote = "Automation - CRM API test";
    private int expectedKycWorkflowStatus = 2;
    private String expectedKycWorkflowStatusDisplay = "Closed";
    private int expectedFnsStatus = 1;
    private String expectedFnsStatusDisplay = "Accepted";
    private int expectedPepSanctions = 5;
    private String expectedPepSanctionsDisplay = "Sanctions";
    private int expectedOriginOfFunds = 2;
    private String expectedOriginOfFundsDisplay = "Bank Robbery";


    protected void salesNoChanges() throws UnirestException {
        ExtendedReporter.step("Calling Sales endpoint with userId only");
        originalUser = userDetails;
        globalParamsHashMap.put("userId", userId);
        putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        verifyNoChange();
    }

    protected void retentionNoChanges() throws UnirestException {
        ExtendedReporter.step("Calling Retention endpoint with userId only");
        originalUser = userDetails;
        globalParamsHashMap.put("userId", userId);
        putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        verifyNoChange();
    }

    protected void ninjaNoChanges() throws UnirestException {
        ExtendedReporter.step("Calling Ninja endpoint with userId only");
        originalUser = userDetails;
        globalParamsHashMap.put("userId", userId);
        putUserNinja(crmApiMasterAuthToken, globalParamsHashMap);
        verifyNoChange();
    }

    protected void kycNoChanges() throws UnirestException {
        ExtendedReporter.step("Calling KYC endpoint with userId only");
        originalUser = userDetails;
        globalBodyJSONObject.put("userId", userId);
        putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        verifyNoChange();
    }

    protected void verifyNoChange() {
        ExtendedReporter.step("Verifying none of the fields changed");
        /* Sales */
        Asserter.assertTrue(originalUser.get("salesDeskId").equals(userDetails.get("salesDeskId")),
                "Expected user's \"salesDeskId\" to remain " + originalUser.get("salesDeskId") + ", but found " + userDetails.get("salesDeskId") + " instead!",
                "Verified user's \"salesDeskId\" remains unchanged");
        Asserter.assertTrue(originalUser.get("salesRep").equals(userDetails.get("salesRep")),
                "Expected user's \"salesRep\" to remain " + originalUser.get("salesRep") + ", but found " + userDetails.get("salesRep") + " instead!",
                "Verified user's \"salesRep\" remains unchanged");
        Asserter.assertTrue(originalUser.get("salesStatus").equals(userDetails.get("salesStatus")),
                "Expected user's \"salesDeskId\" to remain " + originalUser.get("salesStatus") + ", but found " + userDetails.get("salesStatus") + " instead!",
                "Verified user's \"salesStatus\" remains unchanged");

        /* Retention */
        Asserter.assertTrue(originalUser.get("retentionDeskId").equals(userDetails.get("retentionDeskId")),
                "Expected user's \"retentionDeskId\" to remain " + originalUser.get("retentionDeskId") + ", but found " + userDetails.get("retentionDeskId") + " instead!",
                "Verified user's \"retentionDeskId\" remains unchanged");
        Asserter.assertTrue(originalUser.get("retentionRep").equals(userDetails.get("retentionRep")),
                "Expected user's \"retentionRep\" to remain " + originalUser.get("retentionRep") + ", but found " + userDetails.get("retentionRep") + " instead!",
                "Verified user's \"retentionRep\" remains unchanged");
        Asserter.assertTrue(originalUser.get("retentionStatus").equals(userDetails.get("retentionStatus")),
                "Expected user's \"retentionStatus\" to remain " + originalUser.get("retentionStatus") + ", but found " + userDetails.get("retentionStatus") + " instead!",
                "Verified user's \"retentionStatus\" remains unchanged");
        Asserter.assertTrue(originalUser.get("clientPotential").equals(userDetails.get("clientPotential")),
                "Expected user's \"clientPotential\" to remain " + originalUser.get("clientPotential") + ", but found " + userDetails.get("clientPotential") + " instead!",
                "Verified user's \"clientPotential\" remains unchanged");
        Asserter.assertTrue(originalUser.get("tradingStyle").equals(userDetails.get("tradingStyle")),
                "Expected user's \"tradingStyle\" to remain " + originalUser.get("tradingStyle") + ", but found " + userDetails.get("tradingStyle") + " instead!",
                "Verified user's \"tradingStyle\" remains unchanged");

        /* Ninja */
        Asserter.assertTrue(originalUser.get("ninjaDeskId").equals(userDetails.get("ninjaDeskId")),
                "Expected user's \"ninjaDeskId\" to remain " + originalUser.get("ninjaDeskId") + ", but found " + userDetails.get("ninjaDeskId") + " instead!",
                "Verified user's \"ninjaDeskId\" remains unchanged");

        /* KYC */
        Asserter.assertTrue(originalUser.get("kycRep").equals(userDetails.get("kycRep")),
                "Expected user's \"kycRep\" to remain " + originalUser.get("kycRep") + ", but found " + userDetails.get("kycRep") + " instead!",
                "Verified user's \"kycRep\" remains unchanged");
        Asserter.assertTrue(originalUser.get("kycStatus").equals(userDetails.get("kycStatus")),
                "Expected user's \"kycStatus\" to remain " + originalUser.get("kycStatus") + ", but found " + userDetails.get("kycStatus") + " instead!",
                "Verified user's \"kycStatus\" remains unchanged");
        Asserter.assertTrue(originalUser.get("kycNote").equals(userDetails.get("kycNote")),
                "Expected user's \"kycNote\" to remain " + originalUser.get("kycNote") + ", but found " + userDetails.get("kycNote") + " instead!",
                "Verified user's \"kycNote\" remains unchanged");
        Asserter.assertTrue(originalUser.get("kycWorkflowStatus").equals(userDetails.get("kycWorkflowStatus")),
                "Expected user's \"kycWorkflowStatus\" to remain " + originalUser.get("kycWorkflowStatus") + ", but found " + userDetails.get("kycWorkflowStatus") + " instead!",
                "Verified user's \"kycWorkflowStatus\" remains unchanged");
        Asserter.assertTrue(originalUser.get("fnsStatus").equals(userDetails.get("fnsStatus")),
                "Expected user's \"fnsStatus\" to remain " + originalUser.get("fnsStatus") + ", but found " + userDetails.get("fnsStatus") + " instead!",
                "Verified user's \"fnsStatus\" remains unchanged");
        Asserter.assertTrue(originalUser.get("pepSanctions").equals(userDetails.get("pepSanctions")),
                "Expected user's \"pepSanctions\" to remain " + originalUser.get("pepSanctions") + ", but found " + userDetails.get("pepSanctions") + " instead!",
                "Verified user's \"pepSanctions\" remains unchanged");
        Asserter.assertTrue(originalUser.get("originOfFunds").equals(userDetails.get("originOfFunds")),
                "Expected user's \"originOfFunds\" to remain " + originalUser.get("originOfFunds") + ", but found " + userDetails.get("originOfFunds") + " instead!",
                "Verified user's \"originOfFunds\" remains unchanged");
    }

    protected void changeSalesDeskIdExisting() throws UnirestException {
        ExtendedReporter.step("Changing salesDeskId to an existing desk ID (" + expectedSalesDesk + ")");
        globalParamsHashMap.put("salesDeskId", expectedSalesDesk);
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("salesDeskId").equals(expectedSalesDesk),
                "Expected salesDeskId to change to " + expectedSalesDesk + ", but found salesDeskId = " + userDetails.get("salesDeskId") + " instead!",
                "User's salesDeskId was changed successfully");
        Asserter.assertTrue(userDetails.get("salesDeskName").equals(expectedSalesDeskDisplay),
                "Expected salesDeskName to change to \"" + expectedSalesDeskDisplay + "\" but found \"" + userDetails.get("salesDeskName") + "\" instead!",
                "User's salesDeskName changed to \"" + expectedSalesDeskDisplay + "\" as expected");
    }

    protected void changeRetentionDeskIdExisting() throws UnirestException {
        ExtendedReporter.step("Changing retentionDeskId to an existing desk ID (" + expectedRetentionDesk +")");
        globalParamsHashMap.put("retentionDeskId", expectedRetentionDesk);
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("retentionDeskId").equals(expectedRetentionDesk),
                "Expected retentionDeskId to change to " + expectedRetentionDesk +", but found salesDeskId = " + userDetails.get("retentionDeskId") + " instead!",
                "User's retentionDeskId was changed successfully");
        Asserter.assertTrue(userDetails.get("retentionDeskName").equals(expectedRetentionDeskDisplay),
                "Expected retentionDeskName to change to \"" + expectedRetentionDeskDisplay + "\" but found \"" + userDetails.get("retentionDeskName") + "\" instead!",
                "User's retentionDeskName changed to \"" + expectedRetentionDeskDisplay + "\" as expected");
    }

    protected void changeNinjaDeskIdExisting() throws UnirestException {
        ExtendedReporter.step("Changing ninjaDeskId to an existing desk ID (" + expectedNinjaDesk + ")");
        globalParamsHashMap.put("ninjaDeskId", expectedNinjaDesk);
        response = putUserNinja(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("ninjaDeskId").equals(expectedNinjaDesk),
                "Expected ninjaDeskId to change to " + expectedNinjaDesk + ", but found ninjaDeskId = " + userDetails.get("ninjaDeskId") + " instead!",
                "User's ninjaDeskId was changed successfully");
        Asserter.assertTrue(userDetails.get("ninjaDeskName").equals(expectedNinjaDeskDisplay),
                "Expected ninjaDeskName to change to \"" + expectedNinjaDeskDisplay + "\" but found \"" + userDetails.get("ninjaDeskName") + "\" instead!",
                "User's ninjaDeskName changed to \"" + expectedNinjaDeskDisplay + "\" as expected");
    }

    protected void changeSalesDeskIdNonExisting() throws UnirestException {
        ExtendedReporter.step("Changing salesDeskId to a non existing desk ID (9899)");
        globalParamsHashMap.put("salesDeskId", 9899);
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("salesDeskId").toString().equals("9899"),
                "Expected salesDeskId to change to 9899, but found salesDeskId = " + userDetails.get("salesDeskId") + " instead!",
                "User's salesDeskId was changed successfully to 9899");
        Asserter.assertTrue(userDetails.get("salesDeskName").equals("None"),
                "Expected salesDeskName to change to \"None\" but found \"" + userDetails.get("salesDeskName") + "\" instead!",
                "User's salesDeskName changed to \"None\" as expected");
        System.out.println("Break point");
    }

    protected void changeSalesRep() throws UnirestException {
        ExtendedReporter.step("Changing salesRep to a Rep ID that is part of the user's assigned desk (" + expectedSalesRep + ")");
        globalParamsHashMap.put("salesRep", expectedSalesRep);
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("salesRep").equals(expectedSalesRep),
                "Expected salesRep to be " + expectedSalesRep + ", but found " + userDetails.get("salesRep") + " instead!",
                "User's salesRep has changed to " + expectedSalesRep + " successfully");
        Asserter.assertTrue(userDetails.get("salesRepFullName").equals(expectedSalesRepDisplay),
                "Expected salesRepFullName to change to be \"" + expectedSalesRepDisplay + "\" but found " + userDetails.get("salesRepFullName") + "\" instead!",
                "User's salesRepFullName changed to \"" + expectedSalesRepDisplay + "\" as expected");
    }

    protected void changeRetentionRep() throws UnirestException {
        ExtendedReporter.step("Changing retentionRep to a Rep ID that is part of the user's assigned desk (" + expectedRetentionRep + ")");
        globalParamsHashMap.put("retentionRep", expectedRetentionRep);
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("retentionRep").equals(expectedRetentionRep),
                "Expected retentionRep to be " + expectedRetentionRep + ", but found " + userDetails.get("retentionRep") + " instead!",
                "User's retentionRep has changed to " + expectedRetentionRep + " successfully");
        Asserter.assertTrue(userDetails.get("retentionRepFullName").equals(expectedRetentionRepDisplay),
                "Expected retentionRepFullName to change to be \"" + expectedRetentionRepDisplay + "\" but found \"" + userDetails.get("retentionRepFullName") + "\" instead!",
                "User's retentionRepFullName changed to \"" + expectedRetentionRepDisplay + "\" as expected");
    }

    protected void changeKycRep() throws UnirestException {
        ExtendedReporter.step("Changing kycRep to a Rep ID to " + expectedKycRep + " (" + expectedKycRepDisplay + ")");
        globalBodyJSONObject.put("kycRep", expectedKycRep);
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("kycRep").equals(expectedKycRep),
                "Expected kycRep to be " + expectedKycRep + ", but found " + userDetails.get("kycRep") + " instead!",
                "User's kycRep has changed to " + expectedKycRep + " successfully");
        Asserter.assertTrue(userDetails.get("kycRepFullName").equals(expectedKycRepDisplay),
                "Expected kycRepFullName to change to be \"" + expectedKycRepDisplay + "\" but found " + userDetails.get("kycRepFullName") + "\" instead!",
                "User's kycRepFullName changed to \"" + expectedKycRepDisplay + "\" as expected");
    }

    protected void changeSalesStatus() throws UnirestException {
        ExtendedReporter.step("Changing salesStatus to \"" + expectedSalesStatusDisplay + "\" (" + expectedSalesStatus + ")");
        globalParamsHashMap.put("salesStatus", expectedSalesStatus);
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("salesStatus").equals(expectedSalesStatus),
                "Expected salesStatus to be " + expectedSalesStatus + ", but found " + userDetails.get("salesStatus") + " instead!",
                "User's salesStatus has changed to " + expectedSalesStatus + " successfully");
        Asserter.assertTrue(userDetails.get("salesStatusDisplay").equals(expectedSalesStatusDisplay),
                "Expected salesStatusDisplay to change to be \"" + expectedSalesStatusDisplay + "\" but found " + userDetails.get("salesStatusDisplay") + "\" instead!",
                "User's salesStatusDisplay changed to \"" + expectedSalesStatusDisplay + "\" as expected");
    }

    protected void changeRetentionStatus() throws UnirestException {
        globalParamsHashMap.clear();
        globalParamsHashMap.put("userId", userId);
        ExtendedReporter.step("Changing retentionStatus to \"" + expectedRetentionStatusDisplay + "\" (" + expectedRetentionStatus + ")");
        globalParamsHashMap.put("retentionStatus", expectedRetentionStatus);
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("retentionStatus").equals(expectedRetentionStatus),
                "Expected retentionStatus to be " + expectedRetentionStatus + ", but found " + userDetails.get("retentionStatus") + " instead!",
                "User's retentionStatus has changed to " + expectedRetentionStatus + " successfully");
        Asserter.assertTrue(userDetails.get("retentionStatusDisplay").equals(expectedRetentionStatusDisplay),
                "Expected retentionStatusDisplay to change to be \"" + expectedRetentionStatusDisplay + "\" but found " + userDetails.get("retentionStatusDisplay") + "\" instead!",
                "User's retentionStatusDisplay changed to \"" + expectedRetentionStatusDisplay + "\" as expected");
    }

    protected void changeKycStatus() throws UnirestException {
        ExtendedReporter.step("Changing kycStatus to \"" + expectedKycStatusDisplay + "\" (" + expectedKycStatus + ")");
        globalBodyJSONObject.remove("kycRep");
        globalBodyJSONObject.put("kycStatus", expectedKycStatus);
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("kycStatus").equals(expectedKycStatus),
                "Expected kycStatus to be " + expectedKycStatus + ", but found " + userDetails.get("kycStatus") + " instead!",
                "User's kycStatus has changed to " + expectedKycStatus + " successfully");
        Asserter.assertTrue(userDetails.get("kycStatusDisplay").equals(expectedKycStatusDisplay),
                "Expected kycStatusDisplay to change to be \"" + expectedKycStatusDisplay + "\" but found " + userDetails.get("kycStatusDisplay") + "\" instead!",
                "User's kycStatusDisplay changed to \"" + expectedKycStatusDisplay + "\" as expected");
    }

    protected void salesFailures() throws UnirestException {
        /* Auth token fails */
//		globalParamsHashMap.clear();
        ExtendedReporter.step("Testing x-crm-api-token = null");
        response = putUserSales(null, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        ExtendedReporter.step("Testing missing x-crm-api-token");
        response = putUserSales("no token", globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token parameter is missing"),
                "Expected error to be: \"CRM token parameter is missing\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token parameter is missing\"");

        ExtendedReporter.step("Testing x-crm-api-token empty");
        response = putUserSales(null, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        ExtendedReporter.step("Testing invalid x-crm-api-token");
        response = putUserSales("12345-67890-123456778-1234", globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        /* userId fails */
        ExtendedReporter.step("Testing missing mandatory field userId");
        globalParamsHashMap.clear();
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("Required long parameter 'userId' is not present"),
                "Expected error to be: \"Required long parameter 'userId' is not present\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Required long parameter 'userId' is not present\"");

        ExtendedReporter.step("Testing mandatory field userId = null");
        globalParamsHashMap.clear();
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("Required long parameter 'userId' is not present"),
                "Expected error to be: \"Required long parameter 'userId' is not present\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Required long parameter 'userId' is not present\"");

        ExtendedReporter.step("Testing wrong type (String) in mandatory field userId");
        globalParamsHashMap.put("userId", "Test");
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().contains("Failed to convert value of type"),
                "Expected error to be: \"Failed to convert value of type...\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Failed to convert value of type...\"");

        ExtendedReporter.step("Testing non existing userId");
        globalParamsHashMap.put("userId", "0");
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().contains("User with id [0] not found"),
                "Expected error to be: \"User with id [0] not found\", but found: \"" + response.getError() + "\"",
                "Verified error is \"User with id [0] not found\"");
    }

    protected void retentionFailures() throws UnirestException {
        /* Auth token fails */
//		globalParamsHashMap.clear();
        ExtendedReporter.step("Testing x-crm-api-token = null");
        response = putUserRetention(null, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        ExtendedReporter.step("Testing missing x-crm-api-token");
        response = putUserRetention("no token", globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token parameter is missing"),
                "Expected error to be: \"CRM token parameter is missing\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token parameter is missing\"");

        ExtendedReporter.step("Testing x-crm-api-token empty");
        response = putUserRetention(null, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        ExtendedReporter.step("Testing invalid x-crm-api-token");
        response = putUserRetention("12345-67890-123456778-1234", globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        /* userId fails */
        ExtendedReporter.step("Testing missing mandatory field userId");
        globalParamsHashMap.clear();
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("Required long parameter 'userId' is not present"),
                "Expected error to be: \"Required long parameter 'userId' is not present\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Required long parameter 'userId' is not present\"");

        ExtendedReporter.step("Testing mandatory field userId = null");
        globalParamsHashMap.clear();
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("Required long parameter 'userId' is not present"),
                "Expected error to be: \"Required long parameter 'userId' is not present\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Required long parameter 'userId' is not present\"");

        ExtendedReporter.step("Testing wrong type (String) in mandatory field userId");
        globalParamsHashMap.put("userId", "Test");
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().contains("Failed to convert value of type"),
                "Expected error to be: \"Failed to convert value of type...\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Failed to convert value of type...\"");

        ExtendedReporter.step("Testing non existing userId");
        globalParamsHashMap.put("userId", "0");
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().contains("User with id [0] not found"),
                "Expected error to be: \"User with id [0] not found\", but found: \"" + response.getError() + "\"",
                "Verified error is \"User with id [0] not found\"");
    }

    protected void ninjaFailures() throws UnirestException {
        /* Auth token fails */
//		globalParamsHashMap.clear();
        ExtendedReporter.step("Testing x-crm-api-token = null");
        response = putUserNinja(null, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        ExtendedReporter.step("Testing missing x-crm-api-token");
        response = putUserNinja("no token", globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token parameter is missing"),
                "Expected error to be: \"CRM token parameter is missing\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token parameter is missing\"");

        ExtendedReporter.step("Testing x-crm-api-token empty");
        response = putUserNinja(null, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        ExtendedReporter.step("Testing invalid x-crm-api-token");
        response = putUserNinja("12345-67890-123456778-1234", globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        /* userId fails */
        ExtendedReporter.step("Testing missing mandatory field userId");
        globalParamsHashMap.clear();
        response = putUserNinja(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("Required long parameter 'userId' is not present"),
                "Expected error to be: \"Required long parameter 'userId' is not present\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Required long parameter 'userId' is not present\"");

        ExtendedReporter.step("Testing mandatory field userId = null");
        globalParamsHashMap.clear();
        response = putUserNinja(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().equals("Required long parameter 'userId' is not present"),
                "Expected error to be: \"Required long parameter 'userId' is not present\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Required long parameter 'userId' is not present\"");

        ExtendedReporter.step("Testing wrong type (String) in mandatory field userId");
        globalParamsHashMap.put("userId", "Test");
        response = putUserNinja(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().contains("Failed to convert value of type"),
                "Expected error to be: \"Failed to convert value of type...\", but found: \"" + response.getError() + "\"",
                "Verified error is \"Failed to convert value of type...\"");

        ExtendedReporter.step("Testing non existing userId");
        globalParamsHashMap.put("userId", "0");
        response = putUserNinja(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getError().contains("User with id [0] not found"),
                "Expected error to be: \"User with id [0] not found\", but found: \"" + response.getError() + "\"",
                "Verified error is \"User with id [0] not found\"");
    }

    protected void kycFailures() throws UnirestException {
        /* Auth token fails */
//		globalParamsHashMap.clear();
        ExtendedReporter.step("Testing x-crm-api-token = null");
        response = putUserKyc(null, globalBodyJSONObject);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        ExtendedReporter.step("Testing missing x-crm-api-token");
        response = putUserKyc("no token", globalBodyJSONObject);
        Asserter.assertTrue(response.getError().equals("CRM token parameter is missing"),
                "Expected error to be: \"CRM token parameter is missing\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token parameter is missing\"");

        ExtendedReporter.step("Testing x-crm-api-token empty");
        response = putUserKyc(null, globalBodyJSONObject);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        ExtendedReporter.step("Testing invalid x-crm-api-token");
        response = putUserKyc("12345-67890-123456778-1234", globalBodyJSONObject);
        Asserter.assertTrue(response.getError().equals("CRM token value is not valid"),
                "Expected error to be: \"CRM token value is not valid\", but found: \"" + response.getError() + "\"",
                "Verified error is \"CRM token value is not valid\"");

        /* userId fails */
        ExtendedReporter.step("Testing missing mandatory field userId");
        clearBodyJSON();
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getError().equals("User with id [0] not found"),
                "Expected error to be: \"User with id [0] not found\", but found: \"" + response.getError() + "\"",
                "Verified error is \"User with id [0] not found\"");

        ExtendedReporter.step("Testing mandatory field userId = null");
        clearBodyJSON();
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getError().equals("User with id [0] not found"),
                "Expected error to be: \"ReqUser with id [0] not foundUser with id [0] not found\", but found: \"" + response.getError() + "\"",
                "Verified error is \"ReqUser with id [0] not foundUser with id [0] not found\"");

        ExtendedReporter.step("Testing wrong type (String) in mandatory field userId");
        globalBodyJSONObject.put("userId", "Test");
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getError().contains("JSON parse error: Cannot deserialize value of type"),
                "Expected error to be: \"JSON parse error: Cannot deserialize value of type...\", but found: \"" + response.getError() + "\"",
                "Verified error is \"JSON parse error: Cannot deserialize value of type...\"");

        ExtendedReporter.step("Testing non existing userId");
        globalBodyJSONObject.put("userId", "0");
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getError().contains("User with id [0] not found"),
                "Expected error to be: \"User with id [0] not found\", but found: \"" + response.getError() + "\"",
                "Verified error is \"User with id [0] not found\"");
    }

    protected void changeClientPotential() throws UnirestException {
        globalParamsHashMap.remove("retentionStatus");
        ExtendedReporter.step("Changing clientPotential to \"" + expectedClientPotentialDisplay + "\" (" + expectedClientPotential + ")");
        globalParamsHashMap.put("clientPotential", expectedClientPotential);
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("clientPotential").equals(expectedClientPotential),
                "Expected clientPotential to be " + expectedClientPotential + ", but found " + userDetails.get("clientPotential") + " instead!",
                "User's clientPotential has changed to " + expectedClientPotential + " successfully");
        Asserter.assertTrue(userDetails.get("clientPotentialDisplay").equals(expectedClientPotentialDisplay),
                "Expected clientPotentialDisplay to change to be \"" + expectedClientPotentialDisplay + "\" but found " + userDetails.get("clientPotentialDisplay") + "\" instead!",
                "User's clientPotentialDisplay changed to \"" + expectedClientPotentialDisplay + "\" as expected");
    }

    protected void changeTradingStyle() throws UnirestException {
        ExtendedReporter.step("Changing tradingStyle to \"" + expectedTradingStyleDisplay + "\" (" + expectedTradingStyle + ")");
        globalParamsHashMap.put("tradingStyle", expectedTradingStyle);
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("tradingStyle").equals(expectedTradingStyle),
                "Expected tradingStyle to be " + expectedTradingStyle + ", but found " + userDetails.get("tradingStyle") + " instead!",
                "User's tradingStyle has changed to " + expectedTradingStyle + " successfully");
        Asserter.assertTrue(userDetails.get("tradingStyleDisplay").equals(expectedTradingStyleDisplay),
                "Expected tradingStyleDisplay to change to be \"" + expectedTradingStyleDisplay + "\" but found " + userDetails.get("tradingStyleDisplay") + "\" instead!",
                "User's tradingStyleDisplay changed to \"" + expectedTradingStyleDisplay + "\" as expected");
    }

    protected void changeFnsStatus() throws UnirestException {
        ExtendedReporter.step("Changing fnsStatus to \"" + expectedFnsStatusDisplay + "\" (" + expectedFnsStatus + ")");
        globalBodyJSONObject.put("fnsStatus", expectedFnsStatus);
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("fnsStatus").equals(expectedFnsStatus),
                "Expected fnsStatus to be " + expectedFnsStatus + ", but found " + userDetails.get("fnsStatus") + " instead!",
                "User's fnsStatus has changed to " + expectedFnsStatus + " successfully");
        Asserter.assertTrue(userDetails.get("fnsStatusDisplay").equals(expectedFnsStatusDisplay),
                "Expected fnsStatusDisplay to change to be \"" + expectedFnsStatusDisplay + "\" but found \"" + userDetails.get("fnsStatusDisplay") + "\" instead!",
                "User's fnsStatusDisplay changed to \"" + expectedFnsStatusDisplay + "\" as expected");
    }

    protected void addKycNote() throws UnirestException {
        ExtendedReporter.step("Adding kycNote (\"" + expectedKycNote +"\")");
        clearBodyJSON();
        globalBodyJSONObject.put("userId", userId);
        globalBodyJSONObject.put("kycNote", expectedKycNote );
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("kycNote").equals(expectedKycNote),
                "Expected kycNote to be \"" + expectedKycNote + "\", but found " + userDetails.get("kycNote") + " instead!",
                "User's kycNote has changed to \"" + expectedKycNote + "\" successfully");
    }

    protected void changeKycWorkflowStatus() throws UnirestException {
        ExtendedReporter.step("Changing kycWorkflowStatus to \"" + expectedKycWorkflowStatusDisplay + "\" (" + expectedKycWorkflowStatus + ")");
        globalBodyJSONObject.remove("kycNote");
        globalBodyJSONObject.put("kycWorkflowStatus", expectedKycWorkflowStatus);
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("kycWorkflowStatus").equals(expectedKycWorkflowStatus),
                "Expected kycWorkflowStatus to be " + expectedKycWorkflowStatus + ", but found " + userDetails.get("kycWorkflowStatus") + " instead!",
                "User's kycWorkflowStatus has changed to " + expectedKycWorkflowStatus + " successfully");
        Asserter.assertTrue(userDetails.get("kycWorkflowStatusDisplay").toString().equals(expectedKycWorkflowStatusDisplay),
                "Expected kycWorkflowStatusDisplay to be \"" + expectedKycWorkflowStatusDisplay + "\", but found \"" + userDetails.get("kycWorkflowStatusDisplay") + "\" instead!",
                "User's kycWorkflowStatusDisplay has changed to \"" + expectedKycWorkflowStatusDisplay + "\" successfully");
    }

    protected void changePepSanctions() throws UnirestException {
        ExtendedReporter.step("Changing pepSanctions to \"" + expectedPepSanctionsDisplay + "\" (" + expectedPepSanctions + ")");
        globalBodyJSONObject.put("pepSanctions", expectedPepSanctions);
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("pepSanctions").equals(expectedPepSanctions),
                "Expected pepSanctions to be " + expectedPepSanctions + ", but found " + userDetails.get("pepSanctions") + " instead!",
                "User's pepSanctions has changed to " + expectedPepSanctions + " successfully");
        Asserter.assertTrue(userDetails.get("pepSanctionsDisplay").toString().equals(expectedPepSanctionsDisplay),
                "Expected pepSanctionsDisplay to be \"" + expectedPepSanctionsDisplay + "\", but found \"" + userDetails.get("pepSanctionsDisplay") + "\" instead!",
                "User's pepSanctionsDisplay has changed to \"" + expectedPepSanctionsDisplay + "\" successfully");
    }

    protected void changeOriginOfFunds() throws UnirestException {
        ExtendedReporter.step("Changing originOfFunds to \"" + expectedOriginOfFundsDisplay + "\" (" + expectedOriginOfFunds + ")");
        globalBodyJSONObject.put("originOfFunds", expectedOriginOfFunds);
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        userDetails = getUserDetails();

        Asserter.assertTrue(userDetails.get("originOfFunds").equals(expectedOriginOfFunds),
                "Expected originOfFunds to be " + expectedOriginOfFunds + ", but found " + userDetails.get("originOfFunds") + " instead!",
                "User's originOfFunds has changed to " + expectedOriginOfFunds + " successfully");
        Asserter.assertTrue(userDetails.get("originOfFundsDisplay").toString().equals(expectedOriginOfFundsDisplay),
                "Expected originOfFundsDisplay to be \"" + expectedOriginOfFundsDisplay + "\", but found \"" + userDetails.get("originOfFundsDisplay") + "\" instead!",
                "User's originOfFundsDisplay has changed to \"" + expectedOriginOfFundsDisplay + "\" successfully");
    }

    public void callAllEndpoints(int clientId) throws UnirestException {
        ExtendedReporter.step("Setting Sales Desk, Rep and Status via API");
        globalParamsHashMap.put("userId", clientId);
        globalParamsHashMap.put("salesDeskId", expectedSalesDesk);
        globalParamsHashMap.put("salesRep", expectedSalesRep);
        globalParamsHashMap.put("salesStatus", expectedSalesStatus);
        response = putUserSales(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        globalParamsHashMap.clear();

        ExtendedReporter.step("Setting Retention Desk and Status, Client Potential and Trading Style via API");
        globalParamsHashMap.put("userId", clientId);
        globalParamsHashMap.put("retentionRep", expectedRetentionRep);
        globalParamsHashMap.put("retentionDeskId", expectedRetentionDesk);
        globalParamsHashMap.put("retentionStatus", expectedRetentionStatus);
        globalParamsHashMap.put("clientPotential", expectedClientPotential);
        globalParamsHashMap.put("tradingStyle", expectedTradingStyle);
        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        globalParamsHashMap.clear();

        ExtendedReporter.step("Setting Ninja Desk via API");
        globalParamsHashMap.put("userId", clientId);
        globalParamsHashMap.put("ninjaDeskId", expectedNinjaDesk);
        response = putUserNinja(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        globalParamsHashMap.clear();

        ExtendedReporter.step("Setting KYC  via API");
        globalBodyJSONObject.put("userId", clientId);
        globalBodyJSONObject.put("kycRep", expectedKycRep);
        globalBodyJSONObject.put("kycNote", expectedKycNote);
        globalBodyJSONObject.put("kycStatus", expectedKycStatus);
        globalBodyJSONObject.put("kycWorkflowStatus", expectedKycWorkflowStatus);
        globalBodyJSONObject.put("fnsStatus", expectedFnsStatus);
        globalBodyJSONObject.put("pepSanctions", expectedPepSanctions);
        globalBodyJSONObject.put("originOfFunds", expectedOriginOfFunds);
        response = putUserKyc(crmApiMasterAuthToken, globalBodyJSONObject);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");

        ExtendedReporter.step("Setting Retention Rep and Client Potential via API");
        globalParamsHashMap.put("userId", clientId);

        response = putUserRetention(crmApiMasterAuthToken, globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess(),"Expected success=true, but found: " + response.getSuccess(), "Verified success=true");
        globalParamsHashMap.clear();
    }

    public void uiVerification() {
        CRMNavigatorTab navigatorTab = new CRMNavigatorTab();
        ExtendedReporter.step("Verifying all Sales Information dropdowns have correct values", () -> {
            FormDropdown dropdown = new FormDropdown();
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("SALES DESK").contentEquals(expectedSalesDeskDisplay),
                    "Expected \"SALES DESK\" dropdown option to be \"" + expectedSalesDeskDisplay + "\" but found \"" +  dropdown.selectizeDropdownSelectGetCurrentValue("SALES DESK") + "\"",
                    "SALES DESK: " + dropdown.selectizeDropdownSelectGetCurrentValue("SALES DESK") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("SALES REP").contentEquals(expectedSalesRepDisplay),
                    "Expected \"SALES REP\" dropdown option to be \"" + expectedSalesRepDisplay + "\" but found \"" +  dropdown.selectizeDropdownSelectGetCurrentValue("SALES REP") + "\"",
                    "SALES REP: " + dropdown.selectizeDropdownSelectGetCurrentValue("SALES REP") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("INTERNAL SALES STATUS").contentEquals(expectedSalesStatusDisplay),
                    "Expected \"INTERNAL SALES STATUS\" dropdown option to be \"" + expectedSalesStatusDisplay + "\" but found \"" +  dropdown.selectizeDropdownSelectGetCurrentValue("INTERNAL SALES STATUS") + "\"",
                    "INTERNAL SALES STATUS: " + dropdown.selectizeDropdownSelectGetCurrentValue("INTERNAL SALES STATUS") + " is correct");
        });

        ExtendedReporter.step("Verifying Ninja Desk dropdown have correct value", () -> {
            FormDropdown dropdown = new FormDropdown();
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("NINJA DESK").contentEquals(expectedNinjaDeskDisplay),
                    "Expected \"NINJA DESK\" dropdown option to be \"" + expectedNinjaDeskDisplay + "\" but found \"" + dropdown.selectizeDropdownSelectGetCurrentValue("NINJA DESK") + "\"",
                    "NINJA DESK: " + dropdown.selectizeDropdownSelectGetCurrentValue("NINJA DESK") + " is correct");
        });

        ExtendedReporter.step("Verifying all Retention Information dropdowns have correct values", () -> {
            FormDropdown dropdown = new FormDropdown();
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("RETENTION DESK").contentEquals(expectedRetentionDeskDisplay),
                    "Expected \"RETENTION DESK\" dropdown option to be \"" + expectedRetentionDeskDisplay + "\" but found \"" +  dropdown.selectizeDropdownSelectGetCurrentValue("RETENTION DESK") + "\"",
                    "RETENTION DESK: " + dropdown.selectizeDropdownSelectGetCurrentValue("RETENTION DESK") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("RETENTION REP").contentEquals(expectedRetentionRepDisplay),
                    "Expected \"RETENTION REP\" dropdown option to be \"" + expectedRetentionRepDisplay + "\" but found \"" +  dropdown.selectizeDropdownSelectGetCurrentValue("RETENTION REP") + "\"",
                    "RETENTION REP: " + dropdown.selectizeDropdownSelectGetCurrentValue("RETENTION REP") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("INTERNAL RETENTION STATUS").contentEquals(expectedRetentionStatusDisplay),
                    "Expected \"INTERNAL RETENTION STATUS\" dropdown option to be \"" + expectedRetentionStatusDisplay + "\" but found \"" +  dropdown.selectizeDropdownSelectGetCurrentValue("INTERNAL RETENTION STATUS") + "\"",
                    "INTERNAL RETENTION STATUS: " + dropdown.selectizeDropdownSelectGetCurrentValue("INTERNAL RETENTION STATUS") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("CLIENT POTENTIAL").contentEquals(expectedClientPotentialDisplay),
                    "Expected \"SALES REP\" dropdown option to be \"" + expectedClientPotentialDisplay + "\" but found \"" +  dropdown.selectizeDropdownSelectGetCurrentValue("CLIENT POTENTIAL") + "\"",
                    "CLIENT POTENTIAL: " + dropdown.selectizeDropdownSelectGetCurrentValue("CLIENT POTENTIAL") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("TRADING STYLE").contentEquals(expectedTradingStyleDisplay),
                    "Expected \"INTERNAL SALES STATUS\" dropdown option to be \"" + expectedTradingStyleDisplay + "\" but found \"" +  dropdown.selectizeDropdownSelectGetCurrentValue("TRADING STYLE") + "\"",
                    "TRADING STYLE: " + dropdown.selectizeDropdownSelectGetCurrentValue("TRADING STYLE") + " is correct");
        });

        ExtendedReporter.step("Verifying all Compliance Information dropdowns have correct values", () -> {
            navigatorTab.clickOnNavigationTab("Compliance");
            AntelopeComplianceInformationForm complianceInformationForm = new AntelopeComplianceInformationForm();
            WebElement kycNoteTextBox = complianceInformationForm.getKycStatusSection().findElement(By.cssSelector("textarea"));
            FormDropdown dropdown = new FormDropdown();
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("KYC REP").contentEquals(expectedKycRepDisplay),
                    "Expected \"KYC REP\" dropdown option to be \"" + expectedKycRepDisplay + "\" but found \"" + dropdown.selectizeDropdownSelectGetCurrentValue("KYC REP") + "\"",
                    "KYC REP: " + dropdown.selectizeDropdownSelectGetCurrentValue("KYC REP") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("KYC STATUS").contentEquals(expectedKycStatusDisplay),
                    "Expected \"KYC STATUS\" dropdown option to be \"" + expectedKycStatusDisplay + "\" but found \"" + dropdown.selectizeDropdownSelectGetCurrentValue("KYC STATUS") + "\"",
                    "KYC STATUS: " + dropdown.selectizeDropdownSelectGetCurrentValue("KYC STATUS") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("KYC WORKFLOW STATUS").contentEquals(expectedKycWorkflowStatusDisplay),
                    "Expected \"KYC WORKFLOW STATUS\" dropdown option to be \"" + expectedKycWorkflowStatusDisplay + "\" but found \"" + dropdown.selectizeDropdownSelectGetCurrentValue("KYC WORKFLOW STATUS") + "\"",
                    "KYC WORKFLOW STATUS: " + dropdown.selectizeDropdownSelectGetCurrentValue("KYC WORKFLOW STATUS") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("FNS STATUS").contentEquals(expectedFnsStatusDisplay),
                    "Expected \"FNS STATUS\" dropdown option to be \"" + expectedFnsStatusDisplay + "\" but found \"" + dropdown.selectizeDropdownSelectGetCurrentValue("FNS STATUS") + "\"",
                    "KYC FNS STATUS: " + dropdown.selectizeDropdownSelectGetCurrentValue("FNS STATUS") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("PEP / SANCTIONS").contentEquals(expectedPepSanctionsDisplay),
                    "Expected \"PEP / SANCTIONS\" dropdown option to be \"" + expectedPepSanctionsDisplay + "\" but found \"" + dropdown.selectizeDropdownSelectGetCurrentValue("PEP / SANCTIONS") + "\"",
                    "PEP / SANCTIONS: " + dropdown.selectizeDropdownSelectGetCurrentValue("PEP / SANCTIONS") + " is correct");
            Asserter.assertTrue(dropdown.selectizeDropdownSelectGetCurrentValue("ORIGIN OF FUNDS").contentEquals(expectedOriginOfFundsDisplay),
                    "Expected \"ORIGIN OF FUNDS\" dropdown option to be \"" + expectedOriginOfFundsDisplay + "\" but found \"" + dropdown.selectizeDropdownSelectGetCurrentValue("ORIGIN OF FUNDS") + "\"",
                    "ORIGIN OF FUNDS: " + dropdown.selectizeDropdownSelectGetCurrentValue("ORIGIN OF FUNDS") + " is correct");
            Asserter.assertTrue(kycNoteTextBox.getAttribute("value").equals(expectedKycNote),
                    "Expected \"KYC NOTE\" text box to have \"" + expectedKycNote + "\" but found \"" + kycNoteTextBox.getAttribute("value") + "\"",
                    "KYC NOTE: " + kycNoteTextBox.getText() + " is correct");
        });

        ExtendedReporter.step("Verifying CRM API calls are recorded in Activity Log tab", () -> {
            navigatorTab.clickOnNavigationTab("Activity Log");

            Asserter.assertTrue(validateExpectedPerformerInActivityLog(), "Expected 5 rows of " + crmApiTokenName + " but not found",
                    "Validating there are 5 rows of expected performer");
        });
    }

    public int NavigateToAndGetCrmApiTokenUsageCount() {
        CRMLeftMenu crmLeftMenu = new CRMLeftMenu();
        crmLeftMenu.openSecondaryMenuItem(ServerPageContainer.CRMPrimaryMenuItem.SYSTEM, ServerPageContainer.CRMSecondaryMenuItem.CRM_API_Token);
        Asserter.assertTrue(crmLeftMenu.isPrimaryMenuItemExpanded(ServerPageContainer.CRMPrimaryMenuItem.SYSTEM),
                "Expected System menu item to be expanded", "Validating that System menu item is expanded");

//        AntelopeGridTable crmApiTokenGridTable = new AntelopeGridTable();
        NgxGridTable crmApiTokenGridTable = new NgxGridTable();
        crmApiTokenGridTable.setFilterValue("Token", crmApiMasterAuthToken);
        WebdriverUtils.sleep(2000);

        int count = Integer.parseInt(crmApiTokenGridTable.getTableValue(1, "Usage Count"));
//        int count = Integer.parseInt(crmApiTokenGridTable.getTableValue(1, "Usage Count"));

//        crmApiTokenGridTable.clickOnEditRow(1);
//        WebdriverUtils.sleep(2000);
//        AntelopeCrmApiTokenForm apiTokenForm = new AntelopeCrmApiTokenForm();
//        Asserter.assertTrue(apiTokenForm.isDisplayed(), "Expected CRM API Token form to be visible," +
//                "Verified CRM API Token form is visible");
//
//        CRMNavigatorTab crmNavigatorTab = new CRMNavigatorTab();
//        crmNavigatorTab.clickOnNavigationTab("Activity Log");
//        ExtendedReporter.step("Waiting for Activity Log grid to load", () -> {
//            apiTokenForm.waitForSpinner(120);
//        });
//
//        NgxGridTable activityLogGridTable = new NgxGridTable(1);
//        int count = Integer.parseInt(activityLogGridTable.getTableValue(2, "To").split("\\.")[0]);

        ExtendedReporter.step("Current Token Usage Count = " + count);
        return count;
    }

    public void NavigateToAndVerifyCrmApiTokenUsageCountIncrease(int previousCount) {
        int count = NavigateToAndGetCrmApiTokenUsageCount();
        Asserter.assertTrue(count > previousCount,
                "Expected current (" + count + ") Token Usage Count to be higher than previous count (" + previousCount + ")",
                "Current Token Usage Count (" + count + ") has increased as expected (Was " + previousCount + ")");
    }

    public boolean validateExpectedPerformerInActivityLog() {
        AntelopeGridTable antelopeGridTable = new AntelopeGridTable(1);

        int expectedPerformer = 0;
        for (int i=1;  i < antelopeGridTable.getTableTotalRows() -1; i++) {
            if (antelopeGridTable.getTableValue(i, "Performer").contentEquals(crmApiTokenName)) {
                expectedPerformer +=1;
                continue;
            }
            if (expectedPerformer == 5)
                break;

        }
        if (expectedPerformer >= 5)
            return true;
        else
            return false;
    }

}
