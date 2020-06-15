package com.assignment.Contact.service;

import java.time.LocalDateTime;

import com.assignment.Contact.security.Token;


public interface TokenProvider {
	Token generateAccessToken(String subject, String audience);

	Token generateRefreshToken(String subject, String audience);

	String getUsernameFromToken(String token);

	String getAudienceFromToken(String token);

	LocalDateTime getExpiryDateFromToken(String token);

	boolean validateToken(String token);
}
