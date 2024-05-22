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

public class CrmApiRetentionRegressionTest extends CrmAPIBaseTest {

    @Test
	public void retentionNoChangesTest() throws UnirestException {
	    retentionNoChanges();
	}

	@Test
	public void changeRetentionSalesDeskIdTest() throws UnirestException {
		changeRetentionDeskIdExisting();
	}

	@Test
	public void changeRetentionSalesRepTest() throws UnirestException {
		changeRetentionRep();
	}

	@Test
    public void changeRetentionSalesStatusTest() throws UnirestException {
        changeRetentionStatus();
    }

    @Test void changeClientPotentialTest() throws UnirestException {
    	changeClientPotential();
	}

	@Test void changeTradingStyleTest() throws UnirestException {
		changeTradingStyle();
	}

	@Test
	public void retentionFailuresTest() throws UnirestException {
	    retentionFailures();
	}

}
