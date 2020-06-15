package com.assignment.Contact.constants;

public enum ErrorMessageEnum {

	SUCCESS("SUCCESS"),
	ADD_SUCCESS("Contact Added Successfully"),
	ADD_FAILED("Failed to add Contact!!"),
	UPDATE_SUCCESS("Contact Updated Successfully"),
	UPDATE_FAILED("Failed to update Contact!!"),
	DELETE_SUCCESS("Contact Deleted Successfully"),
    DELETE_FAILED("Failed to delete Contact!!"),
	INVALID_PARAMETERS("Invalid Parameters"), 
	INVALID_REQUEST("Invalid Request");

	private String message;

	ErrorMessageEnum(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}