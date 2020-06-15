package com.assignment.Contact.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
	@Value("${jwt.authentication.accessTokenCookieName}")
	private String accessTokenCookieName;

	@Value("${jwt.authentication.refreshTokenCookieName}")
	private String refreshTokenCookieName;

	public HttpCookie createAccessTokenCookie(String token, Long duration, boolean secured) {
		String encryptedToken = SecurityCipher.encrypt(token);
		return ResponseCookie.from(accessTokenCookieName, encryptedToken).maxAge(duration).httpOnly(true)
				.secure(secured).path("/").build();
	}

	public HttpCookie createRefreshTokenCookie(String token, Long duration, boolean secured) {
		String encryptedToken = SecurityCipher.encrypt(token);
		return ResponseCookie.from(refreshTokenCookieName, encryptedToken).maxAge(duration).httpOnly(true)
				.secure(secured).path("/").build();
	}

	public HttpCookie deleteAccessTokenCookie(boolean secured) {
		return ResponseCookie.from(accessTokenCookieName, "").maxAge(0).httpOnly(true).secure(secured).path("/")
				.build();
	}
}
