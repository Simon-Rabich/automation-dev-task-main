package com.mycompany.app;

import com.beust.jcommander.Parameters;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;

import java.net.InetAddress;
import java.util.HashMap;

public class BaseSeleniumTest {

    /********************************************************
     * Before/After Annotations
     *
     * @throws Exception
     *
     *************************************************************/
    @BeforeSuite
    protected void beforeSuiteInits(@Optional("") String suiteFile, @Optional("") String testPropertiesFileName, @Optional("true") Boolean createNewUser, ITestContext context)
            throws Exception {
        Boolean createNewUserGlobalParameter = createNewUser;
        final String currentHost = InetAddress.getLocalHost().getHostName();

    }
}