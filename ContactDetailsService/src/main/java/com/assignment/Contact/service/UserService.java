package com.assignment.Contact.service;

import org.springframework.http.ResponseEntity;

import com.assignment.Contact.Response;
import com.assignment.Contact.modal.UserDetails;

public interface UserService {
	
	ResponseEntity<Response> login(UserDetails user, String accessToken, String refreshToken);
	
	ResponseEntity<Response> refreshToken(String accessToken, String refreshToken);

	ResponseEntity<Response> logout(String refreshToken);
}
