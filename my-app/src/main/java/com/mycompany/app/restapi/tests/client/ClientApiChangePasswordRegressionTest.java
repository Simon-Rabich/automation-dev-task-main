package com.mycompany.app.restapi.tests.client;

import com.antelope.af.restapi.src.ClientApiBaseTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ClientApiChangePasswordRegressionTest extends ClientApiBaseTests {

    @BeforeClass
    public void init () throws Exception {
        registeredEmail = postUsersRegister();
    }

    @Test
    public void postUsersChangePasswordSuccessTest() throws Exception {
        postUsersChangePasswordSuccess();
    }

    @Test
    public void postUsersChangePasswordFailuresTest() throws Exception {
        postUsersChangePasswordFailures();
    }
}
