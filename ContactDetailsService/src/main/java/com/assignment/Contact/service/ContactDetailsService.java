package com.assignment.Contact.service;

import java.util.List;

import com.assignment.Contact.Response;
import com.assignment.Contact.modal.ContactDetails;

public interface ContactDetailsService {
	
	
	Response add(ContactDetails contactDetails);

	Response delete(String contactId);

	Response edit(ContactDetails contactDetails);

	List<ContactDetails> getAll();

	Response updateStatus(String contactId, short status);
}
