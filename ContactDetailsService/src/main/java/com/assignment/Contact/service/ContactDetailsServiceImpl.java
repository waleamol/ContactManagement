package com.assignment.Contact.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.Contact.Response;
import com.assignment.Contact.constants.ErrorMessageEnum;
import com.assignment.Contact.dal.ContactDetailsDAL;
import com.assignment.Contact.dal.SequenceDAL;
import com.assignment.Contact.modal.ContactDetails;

@Service
public class ContactDetailsServiceImpl implements ContactDetailsService {

	@Autowired
	ContactDetailsDAL contactDetailsDAL;

	@Autowired
	SequenceDAL sequenceDAL;

	@Override
	public Response add(ContactDetails contactDetails) {
		Response response;
		if (contactDetails != null) {
			if (contactDetailsDAL.add(contactDetails) != null) {
				response = new Response();
				response.errorCode = 1;
				response.errorMessage = ErrorMessageEnum.ADD_SUCCESS.getMessage();
			} else {
				response = new Response();
				response.errorCode = -1;
				response.errorMessage = ErrorMessageEnum.ADD_FAILED.getMessage();
			}
		} else {
			response = new Response();
			response.errorCode = 1;
			response.errorMessage = ErrorMessageEnum.INVALID_PARAMETERS.getMessage();
		}
		return response;
	}

	@Override
	public Response delete(String contactId) {
		Response response;
		if (contactDetailsDAL.delete(contactId) != null) {
			response = new Response();
			response.errorCode = 1;
			response.errorMessage = ErrorMessageEnum.ADD_SUCCESS.getMessage();
		} else {
			response = new Response();
			response.errorCode = 1;
			response.errorMessage = ErrorMessageEnum.ADD_FAILED.getMessage();
		}
		return response;
	}

	@Override
	public Response edit(ContactDetails contactDetails) {
		Response response;
		if (contactDetails != null) {
			if (contactDetailsDAL.edit(contactDetails) != null) {
				response = new Response();
				response.errorCode = 1;
				response.errorMessage = ErrorMessageEnum.ADD_SUCCESS.getMessage();
			} else {
				response = new Response();
				response.errorCode = -1;
				response.errorMessage = ErrorMessageEnum.ADD_FAILED.getMessage();
			}
		} else {
			response = new Response();
			response.errorCode = 1;
			response.errorMessage = ErrorMessageEnum.INVALID_PARAMETERS.getMessage();
		}
		return response;
	}


	@Override
	public List<ContactDetails> getAll() {
		return contactDetailsDAL.getAll();
	}

	@Override
	public Response updateStatus(String contactId, short status) {
		Response response;
		if (contactDetailsDAL.updateStatus(contactId, status) != null) {
			response = new Response();
			response.errorCode = 1;
			response.errorMessage = ErrorMessageEnum.ADD_SUCCESS.getMessage();
		} else {
			response = new Response();
			response.errorCode = 1;
			response.errorMessage = ErrorMessageEnum.ADD_FAILED.getMessage();
		}
		return response;
	}
}
