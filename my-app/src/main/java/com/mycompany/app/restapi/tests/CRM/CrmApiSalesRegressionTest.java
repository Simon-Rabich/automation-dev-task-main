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

public class CrmApiSalesRegressionTest extends CrmAPIBaseTest {

    @Test
	public void salesNoChangesTest() throws UnirestException {
	    salesNoChanges();
	}

	@Test
	public void changeSalesDeskIdTest() throws UnirestException {
		changeSalesDeskIdExisting();
//		changeSalesDeskIdNonExisting(); //TODO: <-- Move to a separate @Test
	}

	@Test
	public void changeSalesRepTest() throws UnirestException {
		changeSalesRep();
	}

	@Test
    public void changeSalesStatusTest() throws UnirestException {
        changeSalesStatus();
    }

	@Test
	public void salesFailuresTest() throws UnirestException {
	    salesFailures();
	}

}
