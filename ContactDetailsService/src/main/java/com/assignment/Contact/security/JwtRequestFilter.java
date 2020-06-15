package com.assignment.Contact.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.assignment.Contact.service.JwtUserDetailsServiceImpl;
import com.assignment.Contact.service.TokenProvider;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUserDetailsServiceImpl jwtUserDetailsServiceImpl;

	@Autowired
	TokenProvider tokenProvider;

	@Value("${jwt.authentication.accessTokenCookieName}")
	private String accessTokenCookieName;

	@Value("${jwt.authentication.refreshTokenCookieName}")
	private String refreshTokenCookieName;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		try {
			String jwt = getJwtToken(request, true);
			if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
				String username = tokenProvider.getUsernameFromToken(jwt);
//				String roleType = tokenProvider.getAudienceFromToken(jwt);
//				String rolePlusUserName = roleType + "," + username;
				UserDetails userDetails = jwtUserDetailsServiceImpl.loadUserByUsername(username);
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		chain.doFilter(request, response);
	}

	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			String accessToken = bearerToken.substring(7);
			if (accessToken == null)
				return null;
			return SecurityCipher.decrypt(accessToken);
		}
		return null;
	}

	private String getJwtFromCookie(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (accessTokenCookieName.equals(cookie.getName())) {
					String accessToken = cookie.getValue();
					if (accessToken == null)
						return null;
					return SecurityCipher.decrypt(accessToken);
				}
			}
		}
		return null;
	}

	private String getJwtToken(HttpServletRequest request, boolean fromCookie) {
		if (fromCookie)
			return getJwtFromCookie(request);

		return getJwtFromRequest(request);
	}
}