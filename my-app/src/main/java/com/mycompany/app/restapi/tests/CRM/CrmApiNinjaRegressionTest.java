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

public class CrmApiNinjaRegressionTest extends CrmAPIBaseTest {

    @Test
	public void ninjaNoChangesTest() throws UnirestException {
	    ninjaNoChanges();
	}

	@Test
	public void changeNinjaDeskIdTest() throws UnirestException {
		changeNinjaDeskIdExisting();
	}

	@Test
	public void ninjaFailuresTest() throws UnirestException {
	    ninjaFailures();
	}

}
