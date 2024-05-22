package com.mycompany.app.restapi.tests.client;

import com.antelope.af.restapi.src.ClientApiBaseTests;
import org.testng.annotations.Test;

public class ClientApiRegisterUsersRegressionTest extends ClientApiBaseTests {

    @Test
   public void registerUserSuccessWithMandatoryFieldsTest() throws Exception {
        registerUserSuccess();
    }
}
