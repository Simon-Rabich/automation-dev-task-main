package com.mycompany.app.restapi.tests.CRM;

import com.antelope.af.restapi.src.CrmAPIBaseTest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.annotations.Test;

/**
 * Client API Sanity Test
 *
 * @author Micheal Shamshoum
 *
 */

public class CrmApiKycRegressionTest extends CrmAPIBaseTest {

    @Test
	public void kycNoChangesTest() throws UnirestException {
	    kycNoChanges();
	}

	@Test
	public void changeKycRepTest() throws UnirestException {
		changeKycRep();
	}

	@Test
	public void changeKycStatusTest() throws UnirestException {
		changeKycStatus();
	}

	@Test
	public void changeFnsStatusTest() throws UnirestException {
		changeFnsStatus();
	}

	@Test
	public void addKycNoteTest() throws UnirestException {
		addKycNote();
	}

	@Test
	public void changeKycWorkflowStatusTest() throws UnirestException {
		changeKycWorkflowStatus();
	}

	@Test
	public void changePepSanctionsTest() throws UnirestException {
		changePepSanctions();
	}

	@Test
	public void changeOriginOfFundsTest() throws UnirestException {
		changeOriginOfFunds();
	}

	/*  operatorId is not used according to @Ofek  */

	@Test
	public void kycFailuresTest() throws UnirestException {
	    kycFailures();
	}

}
