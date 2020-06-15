package com.assignment.Contact.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.Contact.Response;
import com.assignment.Contact.dto.ContactDetailsDto;
import com.assignment.Contact.modal.ContactDetails;
import com.assignment.Contact.service.ContactDetailsService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/contacts")
public class ContactController {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	ContactDetailsService contactDetailsService;

	@PostMapping
	public ResponseEntity<Response> create(@RequestBody ContactDetailsDto contactDetailsDto) {
		log.info("Adding contacts .");
		ContactDetails contactDetails = new ContactDetails();
		BeanUtils.copyProperties(contactDetailsDto, contactDetails);
		return new ResponseEntity<>(contactDetailsService.add(contactDetails), HttpStatus.OK);
	}

	@DeleteMapping
	public ResponseEntity<Response> delete(@RequestParam String contactId) {
		log.info("delete contacts .");
		return new ResponseEntity<>(contactDetailsService.delete(contactId), HttpStatus.OK);
	}

	@GetMapping
	public List<ContactDetails> getAll() {
		log.info("get all contacts .");
		return contactDetailsService.getAll();
	}

	@PostMapping("/update")
	public ResponseEntity<Response> update(@RequestBody ContactDetailsDto contactDetailsDto) {
		log.info("update contacts .");
		ContactDetails contactDetails = new ContactDetails();
		BeanUtils.copyProperties(contactDetailsDto, contactDetails);
		return new ResponseEntity<>(contactDetailsService.edit(contactDetails), HttpStatus.OK);
	}

	@PatchMapping("/updatestatus/{contactId}/{status}")
	public ResponseEntity<Response> updateStatus(@PathVariable String contactId, @PathVariable short status) {
		log.info("update status .");
		return new ResponseEntity<>(contactDetailsService.updateStatus(contactId, status), HttpStatus.OK);
	}
}
