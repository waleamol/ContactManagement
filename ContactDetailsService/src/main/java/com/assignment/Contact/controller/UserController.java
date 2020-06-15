package com.assignment.Contact.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.Contact.Response;
import com.assignment.Contact.dto.UserDetailsDto;
import com.assignment.Contact.modal.UserDetails;
import com.assignment.Contact.service.UserService;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/users")
public class UserController {

	Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	UserService userService;

	@PostMapping(value = "/login")
	public ResponseEntity<Response> login(@RequestBody UserDetailsDto userDto,
			@CookieValue(name = "accessToken", required = false) String accessToken,
			@CookieValue(name = "refreshToken", required = false) String refreshToken) {
		UserDetails user = new UserDetails();
		BeanUtils.copyProperties(userDto, user);
		return userService.login(user, accessToken, refreshToken);
	}

	@PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> refreshToken(
			@CookieValue(name = "accessToken", required = false) String accessToken,
			@CookieValue(name = "refreshToken", required = false) String refreshToken) {
		return userService.refreshToken(accessToken, refreshToken);
	}

	@PostMapping(value = "/logout", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> logout(
			@CookieValue(name = "refreshToken", required = false) String refreshToken) {
		return userService.logout(refreshToken);
	}

	
	
}
