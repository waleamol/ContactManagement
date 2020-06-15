package com.assignment.Contact.constants;

public enum SequenceEnum {

	CONTACT_DETAILS_SEQ_KEY("contactDetails");

	private String sequenceName;

	SequenceEnum(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	public String getSequenceName() {
		return sequenceName;
	}

}