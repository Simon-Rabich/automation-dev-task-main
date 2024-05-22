package com.mycompany.app.restapi.src;

import org.json.JSONObject;

/**
 * General API Response
 **
 */

public class APIResponse extends ClientAPITestsController {

	private Boolean success;
	private String error;
	private JSONObject result;

	public APIResponse() {

	}

	public APIResponse(Boolean success, String error, JSONObject result, Object paging) {
		this.success = success;
		this.error = error;
		this.result = result;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public JSONObject getResult() {
		return result;
	}

	public void setResult(JSONObject result) {
		this.result = result;
	}

}
