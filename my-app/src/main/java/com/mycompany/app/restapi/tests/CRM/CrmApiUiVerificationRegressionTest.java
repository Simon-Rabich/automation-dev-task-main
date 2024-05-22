package com.mycompany.app.restapi.tests.CRM;

import com.antelope.af.restapi.src.CrmAPIBaseTest;
import com.antelope.af.testconfig.testPatterns.crm.antelopecrm.clients.AntelopeClientsBaseTest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.annotations.Test;

/**
 * Client API Sanity Test
 *
 * @author Micheal Shamshoum
 *
 */

public class CrmApiUiVerificationRegressionTest extends AntelopeClientsBaseTest {

	CrmAPIBaseTest crmApi = new CrmAPIBaseTest();
	int usageCount;

	@Test
	public void crmLoginTest() throws UnirestException {
		goToAntelopeCRMAndLoginSuccessfully(false);
	}

	@Test(dependsOnMethods = "crmLoginTest")
	public void NavigateToAndGetCrmApiTokenUsageCountTest(){
		usageCount = crmApi.NavigateToAndGetCrmApiTokenUsageCount();
	}

	@Test(dependsOnMethods = "NavigateToAndGetCrmApiTokenUsageCountTest")
	public void prepareUserForUiVerificationTest() throws UnirestException {
		crmApi.callAllEndpoints(baseUserId);
	}

	@Test(dependsOnMethods = "prepareUserForUiVerificationTest")
	public void goToClientsAndOpenPreparedUserTest() {
		searchForClientInCrmTable(baseClientUser);
	}

	@Test(dependsOnMethods = "goToClientsAndOpenPreparedUserTest")
	public void crmApiUiVerificationTest() {
		crmApi.uiVerification();
	}

	@Test(dependsOnMethods = "crmApiUiVerificationTest")
	public void NavigateToAndVerifyCrmApiTokenUsageCountIncreaseTest(){
		crmApi.NavigateToAndVerifyCrmApiTokenUsageCountIncrease(usageCount);
	}

}
