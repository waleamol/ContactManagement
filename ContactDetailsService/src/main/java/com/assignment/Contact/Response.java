package com.assignment.Contact;

public class Response {
	public int errorCode = -1;
	public String errorMessage = "error";
	public Object resultData = null;

	public Response(int errorCode, String errorMessage, Object resultData) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.resultData = resultData;
	}

	public Response() {
		super();
	}

	public Response(int errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

}
