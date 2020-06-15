package com.assignment.Contact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.assignment.Contact.Response;
import com.assignment.Contact.modal.UserDetails;
import com.assignment.Contact.security.CookieUtil;
import com.assignment.Contact.security.SecurityCipher;
import com.assignment.Contact.security.Token;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	TokenProvider tokenProvider;

	@Autowired
	private CookieUtil cookieUtil;

	@Value("${spring.tomcat.https}")
	private boolean isSecured;

	@Override
	public ResponseEntity<Response> login(UserDetails user, String accessToken, String refreshToken) {
		Response response = new Response();

		if (user != null && user.getUserName() != null && user.getPassword() != null) {
			if (user.getUserName().equals("sysadmin") && user.getPassword().equals("password")) {
				response.errorCode = 1;
				response.errorMessage = "Login Successfully";
				response.resultData = user.getUserName();
				HttpHeaders responseHeaders = new HttpHeaders();
				String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
				String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
				Boolean accessTokenValid = tokenProvider.validateToken(decryptedAccessToken);
				Boolean refreshTokenValid = tokenProvider.validateToken(decryptedRefreshToken);
				Token newAccessToken;
				Token newRefreshToken;
				if (Boolean.FALSE.equals(accessTokenValid) && Boolean.FALSE.equals(refreshTokenValid)) {
					newAccessToken = tokenProvider.generateAccessToken(user.getUserName(), "sysadmin");
					newRefreshToken = tokenProvider.generateRefreshToken(user.getUserName(), "sysadmin");
					addAccessTokenCookie(responseHeaders, newAccessToken);
					addRefreshTokenCookie(responseHeaders, newRefreshToken);
				}

				if (!accessTokenValid && refreshTokenValid) {
					newAccessToken = tokenProvider.generateAccessToken(user.getUserName(), "sysadmin");
					addAccessTokenCookie(responseHeaders, newAccessToken);
				}
				if (accessTokenValid && refreshTokenValid) {
					newAccessToken = tokenProvider.generateAccessToken(user.getUserName(), "sysadmin");
					newRefreshToken = tokenProvider.generateRefreshToken(user.getUserName(), "sysadmin");
					addAccessTokenCookie(responseHeaders, newAccessToken);
					addRefreshTokenCookie(responseHeaders, newRefreshToken);
				}
				return new ResponseEntity<>(response, responseHeaders, HttpStatus.OK);
			} else {
				response.errorCode = -1;
				response.errorMessage = "Invalid Credentials";
				response.resultData = null;
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
		} else {
			response.errorCode = -11;
			response.errorMessage = "Invalid Parameters";
			response.resultData = null;
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
	}

	@Override
	public ResponseEntity<Response> refreshToken(String accessToken, String refreshToken) {
		Token newAccessToken;
		String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
		String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
		if (Boolean.FALSE.equals(tokenProvider.validateToken(decryptedRefreshToken))) {
			throw new IllegalArgumentException("Refresh Token is invalid!");
		}
		String currentUserEmail = tokenProvider.getUsernameFromToken(decryptedAccessToken);
		newAccessToken = tokenProvider.generateAccessToken(currentUserEmail, "sysadmin");
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil
				.createAccessTokenCookie(newAccessToken.getTokenValue(), newAccessToken.getDuration(), isSecured)
				.toString());
		Response response = new Response();
		response.errorCode = 0;
		response.errorMessage = "Token Refreshed Successfully!!";
		return ResponseEntity.ok().headers(responseHeaders).body(response);
	}

	@Override
	public ResponseEntity<Response> logout(String refreshToken) {
		String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
		if (Boolean.FALSE.equals(tokenProvider.validateToken(decryptedRefreshToken))) {
			throw new IllegalArgumentException("Refresh Token is invalid!");
		}
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add(HttpHeaders.SET_COOKIE, cookieUtil.deleteAccessTokenCookie(isSecured).toString());
		Response response = new Response();
		response.errorCode = 0;
		response.errorMessage = "Token Invalidate Successfully!!";
		return ResponseEntity.ok().headers(responseHeaders).body(response);
	}

	private void addAccessTokenCookie(HttpHeaders httpHeaders, Token token) {
		httpHeaders.add(HttpHeaders.SET_COOKIE,
				cookieUtil.createAccessTokenCookie(token.getTokenValue(), token.getDuration(), isSecured).toString());
	}

	private void addRefreshTokenCookie(HttpHeaders httpHeaders, Token token) {
		httpHeaders.add(HttpHeaders.SET_COOKIE,
				cookieUtil.createRefreshTokenCookie(token.getTokenValue(), token.getDuration(), isSecured).toString());
	}

}
