package com.assignment.Contact.dal;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.assignment.Contact.modal.ContactDetails;

@Repository
public class ContactDetailsDALImpl implements ContactDetailsDAL {

	@Autowired
	public MongoTemplate mongoTemplate;

	@Override
	public ContactDetails add(ContactDetails contactDetails) {
		contactDetails.setStatus(ContactDetailsDAL.ACTIVE_STATUS);
		contactDetails.setCreatedOn(new Date());
		contactDetails.setUpdatedOn(new Date());
		return mongoTemplate.save(contactDetails);
	}

	@Override
	public ContactDetails delete(String contactId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("contactId").is(contactId));
		return mongoTemplate.findAndRemove(query, ContactDetails.class);
	}

	@Override
	public ContactDetails edit(ContactDetails contactDetails) {
		Query query = new Query();
		query.addCriteria(Criteria.where("contactId").is(contactDetails.getContactId()));
		Update update = new Update();
		update.set("firstName", contactDetails.getFirstName());
		update.set("lastName", contactDetails.getLastName());
		update.set("phoneNumber", contactDetails.getPhoneNumber());
		update.set("dateOfBirth", contactDetails.getDateOfBirth());
		update.set("updatedOn", new Date());
		return mongoTemplate.findAndModify(query, update, ContactDetails.class);
	}
	
	@Override
	public List<ContactDetails> getAll() {
		return mongoTemplate.findAll(ContactDetails.class);
	}

	@Override
	public ContactDetails updateStatus(String contactId, short status) {
		Query query = new Query();
		query.addCriteria(Criteria.where("contactId").is(contactId));
		Update update = new Update();
		update.set("status", status);
		update.set("updatedOn", new Date());
		return mongoTemplate.findAndModify(query, update, ContactDetails.class);
	}

}
