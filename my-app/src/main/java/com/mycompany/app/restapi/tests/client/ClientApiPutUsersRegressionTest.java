package com.mycompany.app.restapi.tests.client;

import com.antelope.af.restapi.src.ClientApiBaseTests;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ClientApiPutUsersRegressionTest extends ClientApiBaseTests {

    @BeforeClass
    public void init() throws Exception {
        registeredEmail = postUsersRegister();
    }

    @Test
    public void putUsersSuccessChangeExistingValueTest() throws UnirestException {
        putUsersSuccessChangeExistingValue();
    }

    @Test
    public void putUsersSuccessChangeNonExistingValueTest() throws UnirestException {
        putUsersSuccessChangeNonExistingValue();
    }

    @Test
    public void putUsersSendNonExistingKeyTest() throws UnirestException {
        putUsersSendNonExistingKey();
    }

    @Test
    public void putUsersFailuresTest() throws UnirestException {
        putUsersFailures();
    }

    //TODO: add non-valid values to put tests (should open bugs about first and last name, relate to qa-improvement and write that it will cause ui issue that operator wont be able to edit)
    //TODO: Add/Edit  ExtendedReporter.step where needed
    //TODO: Run all client test sets
}
