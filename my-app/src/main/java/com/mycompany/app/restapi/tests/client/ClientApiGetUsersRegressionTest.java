package com.mycompany.app.restapi.tests.client;

import com.antelope.af.restapi.src.ClientApiBaseTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ClientApiGetUsersRegressionTest extends ClientApiBaseTests {

    @BeforeClass
    public void init() throws Exception {
        registeredEmail = postUsersRegister();
    }

    @Test
    public void getUsersSuccessTest() throws Exception {
      getUsersSuccess();
    }

    @Test
    public void getUsersFailuresTest() throws Exception {
      getUsersFailures();
    }
}
