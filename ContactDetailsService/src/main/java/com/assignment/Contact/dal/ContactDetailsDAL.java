package com.assignment.Contact.dal;

import java.util.List;

import com.assignment.Contact.modal.ContactDetails;

public interface ContactDetailsDAL {
	
	short ACTIVE_STATUS=1;
	short SUSPENDED_STATUS=-1;
	
	ContactDetails add(ContactDetails contactDetails);

	ContactDetails delete(String contactId);

	ContactDetails edit(ContactDetails contactDetails);

	List<ContactDetails> getAll();

	ContactDetails updateStatus(String contactId, short status);
}
