package com.wipro.usermgmt.ui.model;

/**
 * @author Venkat
 * 
 * Description: to wrap response to the vo
 *
 */
public class ResponseVo {

	private int responseCode;

	private String message;

	public ResponseVo() {
	}

	public ResponseVo(int responseCode, String message) {
		super();
		this.responseCode = responseCode;
		this.message = message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
