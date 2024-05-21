package com.antelope.af.utilities;

import com.antelope.af.restapi.UniRestUsersController;
import com.antelope.af.utilities.testpparameters.UserParamsThreadLocal;
import org.testng.annotations.Test;

import com.antelope.af.testconfig.testPatterns.crm.antelopecrm.AntelopeBaseTest;

/*
 * This class creates users for the specified brand which provided in props
 * @author :Fadi zaboura
 */
public class createUsersUtilities extends AntelopeBaseTest {

	@Test
	public void createNewUsers() throws Exception {
		XsitesUtilities.createUsers(0, 10, antelopeSignalsServersBaseUrl, antelopeApiKey, antelopeAuthToken,
				antelopeSignalsCRMBaseUrl, brandName);
	}

	@Test
	public void testFunc() throws Exception {
		XsitesUtilities.turnOnTestToggle(antelopeAuthToken);
	}

	@Test
	public void testFuncBlock() throws Exception {
		XsitesUtilities.turnOnBlock(antelopeAuthToken);
	}
}

