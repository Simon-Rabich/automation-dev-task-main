package com.mycompany.app.restapi.tests.client;

import com.antelope.af.restapi.src.ClientApiBaseTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Client API Sanity Test
 *
 * @author Micheal Shamshoum
 *
 */

public class ClientApiAuthenticateRegressionTest extends ClientApiBaseTests {

	@BeforeClass
	public void init() throws Exception {
		registeredEmail = postUsersRegister();
	}

	@Test
	public void authenticateSuccessTest() throws Exception {
		authenticateSuccess();
	}

	@Test
	public void authenticateFailuresTest() throws Exception {
		authenticateFailures();
	}
}
