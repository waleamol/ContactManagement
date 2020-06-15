package com.assignment.Contact.service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.assignment.Contact.security.Token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Service
public class TokenProviderImpl implements TokenProvider {

	@Value("${jwt.authentication.tokenSecret}")
	private String tokenSecret;

	@Value("${jwt.authentication.tokenExpirationMsec}")
	private Long tokenExpirationMsec;

	@Value("${jwt.authentication.refreshTokenExpirationMsec}")
	private Long refreshTokenExpirationMsec;

	public final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public Token generateAccessToken(String subject, String audience) {
		Date now = new Date();
		Long duration = now.getTime() + tokenExpirationMsec;
		Date expiryDate = new Date(duration);
		String token = Jwts.builder().setSubject(subject).setAudience(audience).setIssuedAt(now)
				.setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
		return new Token(Token.TokenType.ACCESS, token, duration,
				LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
	}

	@Override
	public Token generateRefreshToken(String subject, String audience) {
		Date now = new Date();
		Long duration = now.getTime() + refreshTokenExpirationMsec;
		Date expiryDate = new Date(duration);
		String token = Jwts.builder().setSubject(subject).setAudience(audience).setIssuedAt(now)
				.setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, tokenSecret).compact();
		return new Token(Token.TokenType.REFRESH, token, duration,
				LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
	}

	@Override
	public String getUsernameFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
		return claims.getSubject();
	}

	@Override
	public String getAudienceFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
		return claims.getAudience();
	}

	@Override
	public LocalDateTime getExpiryDateFromToken(String token) {
		Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
		return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
	}

	@Override
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(tokenSecret).parse(token);
			return true;
		} catch (SignatureException ex) {
			log.error("SignatureException ", ex.getMessage());
		} catch (MalformedJwtException ex) {
			log.error("MalformedJwtException ", ex.getMessage());
		} catch (ExpiredJwtException ex) {
			log.error("ExpiredJwtException ", ex.getMessage());
		} catch (UnsupportedJwtException ex) {
			log.error("UnsupportedJwtException ", ex.getMessage());
		} catch (IllegalArgumentException ex) {
			log.error("IllegalArgumentException ", ex.getMessage());
		}
		return false;
	}
}
