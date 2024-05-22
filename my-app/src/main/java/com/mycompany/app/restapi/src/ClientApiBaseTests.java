package com.mycompany.app.restapi.src;

import com.antelope.af.bugmanipulator.Asserter;
import com.antelope.af.utilities.ExtendedReporter;
import com.antelope.af.utilities.WebdriverUtils;
import com.mashape.unirest.http.exceptions.UnirestException;

public class ClientApiBaseTests extends ClientAPITestsController {
    protected String registeredEmail;
    protected String userId;
    protected String newClientAuthToken = "c11c80c6-1403-424c-a193-25eb8c8eb955";

    /* Authenticate tests */

    protected void authenticateSuccess() throws UnirestException {
        // Call register method
        ExtendedReporter.step("New user's email is: \"" + registeredEmail + "\"");
        // Current response values
        getCurrentResponseValues();

        // Post authenticate
        ExtendedReporter.step("Authenticating user");
        globalParamsHashMap.put("email", registeredEmail);
        globalParamsHashMap.put("password", "testtest123");
        response = postUsersAuthenticate(globalParamsHashMap);

        //Assertions
        userResponseAssertions();
        Asserter.assertTrue(!response.getResult().isNull("authToken"), "Expected response to contain authToken, but found: " + response.getResult().get("authToken").toString(),
                "Verified that authToken exists with value");
    }

    protected void authenticateFailures() throws UnirestException {
        globalParamsHashMap.clear();
        ExtendedReporter.step("Authenticating user without email");
        globalParamsHashMap.put("password", "testtest123");
        response = postUsersAuthenticate(globalParamsHashMap);

        //Assertions
        Asserter.assertTrue(response.getError().equals("Required String parameter 'email' is not present"),
                "Expected error to be: \"Required String parameter 'email' is not present\", but found: " + response.getError(),
                "Verified error is \"Required String parameter 'email' is not present\"");

        ExtendedReporter.step("Authenticating user with invalid email address");
        globalParamsHashMap.clear();
        globalParamsHashMap.put("email", "aaaaaaa");
        globalParamsHashMap.put("password", "testtest123");
        response = postUsersAuthenticate(globalParamsHashMap);

        //Assertions
        Asserter.assertTrue(response.getError().equals("Invalid email and/or password"),
                "Expected error to be: \"Invalid email and/or password\", but found: " + response.getError(),
                "Verified error is \"Invalid email and/or password\"");


        ExtendedReporter.step("Authenticating user with non-existing email address");
        globalParamsHashMap.clear();
        globalParamsHashMap.put("email", "aaaaaaa@bbbbbb.co");
        globalParamsHashMap.put("password", "testtest123");
        response = postUsersAuthenticate(globalParamsHashMap);

        //Assertions
        Asserter.assertTrue(response.getError().equals("Invalid email and/or password"),
                "Expected error to be: \"Invalid email and/or password\", but found: " + response.getError(),
                "Verified error is \"Invalid email and/or password\"");

        ExtendedReporter.step("Authenticating user without password]");
        globalParamsHashMap.clear();
        globalParamsHashMap.put("email", registeredEmail);
        response = postUsersAuthenticate(globalParamsHashMap);

        //Assertions
        Asserter.assertTrue(response.getError().equals("Required String parameter 'password' is not present"),
                "Expected error to be: \"Required String parameter 'password' is not present\", but found: " + response.getError(),
                "Verified error is \"Required String parameter 'password' is not present\"");

        ExtendedReporter.step("Authenticating user with invalid password");
        globalParamsHashMap.clear();
        globalParamsHashMap.put("email", registeredEmail);
        globalParamsHashMap.put("password", "aaaa");
        response = postUsersAuthenticate(globalParamsHashMap);

        //Assertions
        Asserter.assertTrue(response.getError().equals("Invalid email and/or password"),
                "Expected error to be: \"Invalid email and/or password\", but found: " + response.getError(),
                "Verified error is \"Invalid email and/or password\"");

        ExtendedReporter.step("Authenticating user with empty values");
        globalParamsHashMap.clear();
        response = postUsersAuthenticate(globalParamsHashMap);

        //Assertions
        Asserter.assertTrue(response.getError().equals("Required String parameter 'email' is not present"),
                "Expected error to be: \"Required String parameter 'email' is not present\", but found: " + response.getError(),
                "Verified error is \"Required String parameter 'email' is not present\"");
    }

    /* Change password tests */

    protected void postUsersChangePasswordSuccess() throws Exception {
        ExtendedReporter.step("Registered new user");
        String newPassword = "testtestnew123";
        globalParamsHashMap.clear();
        globalParamsHashMap.put("oldPassword", "testtest123");
        globalParamsHashMap.put("newPassword", newPassword);
        ExtendedReporter.step("Changing password");
        response = postUsersChangePassword(clientAuthToken, globalParamsHashMap);

        Asserter.assertTrue(response.getSuccess().equals(true), "Expected success=true, but found: " + response.getSuccess(), "Verfied success=true");

        //Login using new password
        globalParamsHashMap.clear();
        globalParamsHashMap.put("email", registeredEmail);
        globalParamsHashMap.put("password", newPassword);

        ExtendedReporter.step("Verifying authentication with new password");
        response = postUsersAuthenticate(globalParamsHashMap);
        Asserter.assertTrue(response.getSuccess().equals(true), "Expected success=true, but found: " + response.getSuccess(), "Verfied success=true after authenticating with new password");

        // Change password back
        changePassword();
    }

    protected void postUsersChangePasswordFailures() throws UnirestException {
        ExtendedReporter.step("Registered new user");

        globalParamsHashMap.clear();
        ExtendedReporter.step("Change password without parameters");
        response = postUsersChangePassword(clientAuthToken, globalParamsHashMap);

        Asserter.assertTrue(response.getSuccess().equals(false), "Expected success=false, but found: " + response.getSuccess(), "Verfied success=false");
        Asserter.assertTrue(response.getError().equals("Required String parameter 'oldPassword' is not present"),
                "Expected error to be: \"Required String parameter 'oldPassword' is not present\", but found: " + response.getError(),
                "Verified error is \"Required String parameter 'oldPassword' is not present\"");

        ExtendedReporter.step("Change password without old password");
        globalParamsHashMap.put("newPassword", "testtest123");
        response = postUsersChangePassword(clientAuthToken, globalParamsHashMap);

        Asserter.assertTrue(response.getSuccess().equals(false), "Expected success=false, but found: " + response.getSuccess(), "Verfied success=false");
        Asserter.assertTrue(response.getError().equals("Required String parameter 'oldPassword' is not present"),
                "Expected error to be: \"Required String parameter 'oldPassword' is not present\", but found: " + response.getError(),
                "Verified error is \"Required String parameter 'oldPassword' is not present\"");


        ExtendedReporter.step("Change password without new password");
        globalParamsHashMap.clear();
        globalParamsHashMap.put("oldPassword", "testtestnew123");
        response = postUsersChangePassword(clientAuthToken, globalParamsHashMap);

        Asserter.assertTrue(response.getSuccess().equals(false), "Expected success=false, but found: " + response.getSuccess(), "Verfied success=false");
        Asserter.assertTrue(response.getError().equals("Required String parameter 'newPassword' is not present"),
                "Expected error to be: \"Required String parameter 'newPassword' is not present\", but found: " + response.getError(),
                "Verified error is \"Required String parameter 'newPassword' is not present\"");

        ExtendedReporter.step("Change password with wrong oldPassword");
        globalParamsHashMap.clear();
        globalParamsHashMap.put("oldPassword", "testtess123");
        globalParamsHashMap.put("newPassword", "tesstess123");
        response = postUsersChangePassword(clientAuthToken, globalParamsHashMap);

        Asserter.assertTrue(response.getSuccess().equals(false), "Expected success=false, but found: " + response.getSuccess(), "Verfied success=false");
        Asserter.assertTrue(response.getError().equals("Old password does not match current password."),
                "Expected error to be: \"Old password does not match current password\", but found: " + response.getError(),
                "Verified error is \"Old password does not match current password\"");

        ExtendedReporter.step("Change password with invalid new password");
        globalParamsHashMap.clear();
        globalParamsHashMap.put("oldPassword", "testtest123");
        globalParamsHashMap.put("newPassword", "t");
        response = postUsersChangePassword(clientAuthToken, globalParamsHashMap);

        Asserter.assertTrue(response.getSuccess().equals(false), "Expected success=false, but found: " + response.getSuccess(), "Verfied success=false");
        Asserter.assertTrue(response.getError().equals("Invalid password"),
                "Expected error to be: \"Invalid password\", but found: " + response.getError(),
                "Verified error is Invalid password");

        ExtendedReporter.step("Change password with invalid token");
        globalParamsHashMap.clear();
        globalParamsHashMap.put("oldPassword", "testtest123");
        globalParamsHashMap.put("newPassword", "testtestnew123");

        response = postUsersChangePassword(newClientAuthToken, globalParamsHashMap);
        // Assertions
        invalidTokenAssertions();
    }


    /* Get users */
    protected void getUsersSuccess() throws Exception {
        // Call register method
        ExtendedReporter.step("New user's email is: \"" + registeredEmail + "\"");
        getCurrentResponseValues();

        // Get user
        ExtendedReporter.step("Get user");
        globalParamsHashMap.clear();
        response = getUsers(clientAuthToken,globalParamsHashMap);

        // Assertions
        userResponseAssertions();
        Asserter.assertTrue(response.getResult().isNull("authToken"), "Expected response to contain authToken = null, but found: " + response.getResult().get("authToken").toString(),
                "Verified that authToken=null");
    }

    protected void getUsersFailures() throws Exception {
        ExtendedReporter.step("Get user with invalid token");
        globalParamsHashMap.clear();
        response = getUsers(newClientAuthToken,globalParamsHashMap);
        invalidTokenAssertions();
    }

    /* put users tests */
    protected void putUsersSuccessChangeExistingValue() throws UnirestException {
        getCurrentResponseValues();
        firstName = "newFirstName";
        lastName = "newLAstName";

        // Put user
        ExtendedReporter.step("Put user");
        clearBodyJSON();
        //Editable value
        globalBodyJSONObject.put("firstName", firstName);
        globalBodyJSONObject.put("lastName",lastName);
        //Non-editable values
        globalBodyJSONObject.put("password", "testtestnew1234");
        globalBodyJSONObject.put("id", "1234567");
        globalBodyJSONObject.put("email", "testemail@test.test");
        globalBodyJSONObject.put("kycStatus", "dummyKycStatus");
        globalBodyJSONObject.put("fnsStatus", "dummyFnsStatus");
        globalBodyJSONObject.put("authToken", "dummyToken");
        globalBodyJSONObject.put("visitId", "111");
        response = putUsers(clientAuthToken,globalBodyJSONObject);

        //Assertions
        userResponseAssertions();
        Asserter.assertTrue(response.getResult().isNull("authToken"), "Expected response to contain authToken = null, but found: " + response.getResult().get("authToken").toString(),
                "Verified that authToken=null");
    }

    protected void putUsersSuccessChangeNonExistingValue() throws UnirestException {
        String newCity = "TelAviv";
        // Call register method
        ExtendedReporter.step("New user's email is: \"" + registeredEmail + "\"");

        // Put user
        ExtendedReporter.step("Put user");
        globalBodyJSONObject.put("city", newCity);
        response = putUsers(clientAuthToken,globalBodyJSONObject);

        // Response parameters
        String city = response.getResult().get("city").toString();

        //Assertions
        Asserter.assertTrue(city.equals(newCity), "Expected city to be: " + newCity + " but found: " + city,
                "Verified that city is: " + newCity);
    }

    protected void putUsersSendNonExistingKey() throws UnirestException {

        // Call register method
        ExtendedReporter.step("New user's email is: \"" + registeredEmail + "\"");

        // Put user
        ExtendedReporter.step("Put user");
        globalBodyJSONObject.put("nonExistingKey", "nonExistingKey");
        response = putUsers(clientAuthToken,globalBodyJSONObject);

        //Assertions
        Asserter.assertTrue(response.getResult().isNull("nonExistingKey"), "Expected \"nonExistingKey\" not to be found",
                "Verified that \"nonExistingKey\" is not found");
    }

    protected void putUsersFailures() throws UnirestException {
        ExtendedReporter.step("put user with non-valid token");
        response = putUsers(newClientAuthToken,globalBodyJSONObject);
        // Assertions
        invalidTokenAssertions();

        ExtendedReporter.step("put user without token");
        response = putUsers("No Token",globalBodyJSONObject);

        Asserter.assertTrue(response.getSuccess().equals(false), "Expected success=false, but found: " + response.getSuccess(), "Verfied success=false");
        Asserter.assertTrue(response.getError().equals("User not found"),
                "Expected error to be: \"User not found\", but found: " + response.getError(),
                "Verified error: \"User not found\"");

        ExtendedReporter.step("put user with non-valid value");
        clearBodyJSON();
        globalBodyJSONObject.put("telephone", "testtest");
        response = putUsers(clientAuthToken,globalBodyJSONObject);

        Asserter.assertTrue(response.getSuccess().equals(false), "Expected success=false, but found: " + response.getSuccess(), "Verfied success=false");
        Asserter.assertTrue(response.getError().equals("[telephone] Not valid. Invalid telephone number."),
                "Expected error to be: \"[telephone] Not valid. Invalid telephone number.\", but found: " + response.getError(),
                "Verified error: \"[telephone] Not valid. Invalid telephone number.\"");
    }

    /* Register users */
    protected void registerUserSuccess() throws Exception {
        clearBodyJSON();
        registeredEmail = WebdriverUtils.generateEmail();
        firstName = "Test";
        lastName = "Automation";
        countryIso = getRandomCountryIso();
        langIso = "EN";
        globalBodyJSONObject.put("email", registeredEmail);
        globalBodyJSONObject.put("password", "testtest123");
        globalBodyJSONObject.put("firstName", firstName);
        globalBodyJSONObject.put("lastName", lastName);
        globalBodyJSONObject.put("telephone", telephone);
        globalBodyJSONObject.put("telephonePrefix", telephonePrefix);

        globalBodyJSONObject.put("countryIso", countryIso);
        globalBodyJSONObject.put("languageIso", langIso);

        response = postUsersRegister(globalBodyJSONObject);
        userId = response.getResult().get("id").toString();
        //Assertions
        userResponseAssertions();
        Asserter.assertTrue(!response.getResult().isNull("authToken"), "Expected response to contain authToken, but found: " + response.getResult().get("authToken").toString(),
                "Verified that authToken exists with value");
    }

    //TODO: Register failures

    private void userResponseAssertions() {

        String newId = response.getResult().get("id").toString();
        String newLangISO = response.getResult().get("languageIso").toString();
        String newCountryISO = response.getResult().get("countryIso").toString();
        String newEmail = response.getResult().get("email").toString();
        String newFirstName = response.getResult().get("firstName").toString();
        String newLastName = response.getResult().get("lastName").toString();
        String newTelephone = response.getResult().get("telephone").toString();
        String newTelephonePrefix = response.getResult().get("telephonePrefix").toString();

        Asserter.assertTrue(newId.equals(userId), "Expected id to be: " + userId + ", but found " + newId,
                "Verified that id equals to " + userId);
        Asserter.assertTrue(!response.getResult().isNull("creationTime"), "Expected response to contain creationTime, but not found",
                "Verified that creationTime exists in response");
        Asserter.assertTrue(!response.getResult().isNull("lastUpdateTime"), "Expected response to contain lastUpdateTime, but not found",
                "Verified that lastUpdateTime exists in response");
        Asserter.assertTrue(newEmail.equals(registeredEmail), "Expected email to be: " + registeredEmail + ", but found: " + newEmail,
                "Verified that email equals to " + registeredEmail);
        Asserter.assertTrue(response.getResult().isNull("password"), "Expected response to contain password=null, but found: " + response.getResult().get("password").toString(),
                "Verified that password is null");
        Asserter.assertTrue(newFirstName.equals(firstName), "Expected firstName to be: " + firstName + ", but found: " + newFirstName,
                "Verified that firstName equals to " + firstName);
        Asserter.assertTrue(newLastName.equals(lastName), "Expected lastName to be: " + lastName + ", but found: " + newLastName,
                "Verified that lastName equals to " + lastName);
		Asserter.assertTrue(newTelephone.equals(telephone), "Expected telephone to be: " + telephone + "but found: " + response.getResult().get("telephone").toString(),
				"Verified that telephone is: " + telephone);
        Asserter.assertTrue(newTelephonePrefix.equals(telephonePrefix), "Expected telephonePrefix to be: " + telephonePrefix + ", but found: " + newTelephonePrefix,
                "Verified that telephonePrefix equals to " + telephonePrefix);
        Asserter.assertTrue(newCountryISO.equals(countryIso), "Expected languageIso to be: " + countryIso + ", but found: " + newCountryISO,
                "Verified that languageIso equals to " + countryIso);
        Asserter.assertTrue(newLangISO.equals(langIso), "Expected languageIso to be: " + langIso + ", but found: " + newLangISO,
                "Verified that languageIso equals to " + langIso);
        Asserter.assertTrue(response.getResult().isNull("address"), "Expected response to contain address=null, but found: " + response.getResult().get("address").toString(),
                "Verified that address is null");
        Asserter.assertTrue(response.getResult().isNull("poBox"), "Expected response to contain poBox=null, but found: " + response.getResult().get("poBox").toString(),
                "Verified that poBox is null");
        Asserter.assertTrue(response.getResult().isNull("city"), "Expected response to contain city=null, but found: " + response.getResult().get("city").toString(),
                "Verified that city is null");
        Asserter.assertTrue(response.getResult().isNull("state"), "Expected response to contain state=null, but found: " + response.getResult().get("state").toString(),
                "Verified that state is null");
        Asserter.assertTrue(response.getResult().isNull("zip"), "Expected response to contain zip=null, but found: " + response.getResult().get("zip").toString(),
                "Verified that zip is null");
        Asserter.assertTrue(response.getResult().get("kycStatus").equals("NoKyc"), "Expected response to contain kycStatus with value \"NoKyc\", but not found",
                "Verified that kycStatus exists in response with value \"NoKyc\"");
        Asserter.assertTrue(response.getResult().get("fnsStatus").toString().equals("0"), "Expected response to contain fnsStatus with value \"0\", but not found",
                "Verified that fnsStatus exists in response with value \"0\"");
        Asserter.assertTrue(response.getResult().get("visitId").equals(0), "Expected response to contain visitId with value 0, but not found",
                "Verified that visitId exists in response with value 0");

    }

    private void invalidTokenAssertions() {
        Asserter.assertTrue(response.getSuccess().equals(false), "Expected success=false, but found: " + response.getSuccess(), "Verfied success=false");
        Asserter.assertTrue(response.getError().equals("Invalid authentication token"),
                "Expected error to be: \"Invalid authentication token\", but found: " + response.getError(),
                "Verified error: \"Invalid authentication token\"");
    }

    protected void changePassword() throws Exception {
        globalParamsHashMap.clear();
        globalParamsHashMap.put("oldPassword", "testtestnew123");
        globalParamsHashMap.put("newPassword", "testtest123");
        response = postUsersChangePassword(clientAuthToken, globalParamsHashMap);
    }

    private void getCurrentResponseValues() {
        userId = response.getResult().get("id").toString();
        firstName = response.getResult().get("firstName").toString();
        lastName = response.getResult().get("lastName").toString();
        langIso = response.getResult().get("languageIso").toString();
        countryIso = response.getResult().get("countryIso").toString();
        telephone = response.getResult().get("telephone").toString();
        telephonePrefix = response.getResult().get("telephonePrefix").toString();
    }

    public String authenticateClient(String email, String password) throws UnirestException {
        ExtendedReporter.step("Authenticating user");
        globalParamsHashMap.put("email", email);
        globalParamsHashMap.put("password", password);
        response = postUsersAuthenticate(globalParamsHashMap);
        return response.getResult().get("authToken").toString();
    }



}
