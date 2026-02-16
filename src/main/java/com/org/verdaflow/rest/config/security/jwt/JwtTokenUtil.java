package com.org.verdaflow.rest.config.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.org.verdaflow.rest.config.common.AppConst;
import com.org.verdaflow.rest.config.common.StringConst;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	public static final Logger logger = LoggerFactory.getLogger(JwtTokenUtil.class);
	private static final long serialVersionUID = -3301605591108950415L;
	private static final String CLAIM_KEY_USERNAME = "sub";
	private static final String CLAIM_KEY_AUDIENCE = "audience";
	private static final String CLAIM_KEY_PASS = "pass";
	private static final String CLAIM_KEY_CREATED = "created";
	private static final String CLAIM_DEVICE_KEY = "SINGLE_USER_LOGIN";
	private static final String CLAIM_ROLE_KEY = "ROLE_ID";

	public static final String USERNAME_ROLEID_JOINING_SYMBOL = ":::&&:::";

	public String getUsernameFromToken(String token) {
		String username;

		try {
			final Claims claims = getClaimsFromToken(token);

			if (claims == null) {
				logger.debug(StringConst.USERNAME_NOT_FOUND_HYPHEN);
				return null;
			}

			if (claims.getSubject() == null) {
				logger.debug(StringConst.USERNAME_NOT_FOUND_HYPHEN);
				return null;
			}

			username = claims.getSubject();
		} catch (Exception e) {
			logger.error(StringConst.USERNAME_NOT_FOUND_HYPHEN, e);
			username = null;
		}

		return username;
	}

	public Date getExpirationDateFromToken(String token) {
		Date expiration = null;

		try {
			final Claims claims = getClaimsFromToken(token);

			if (null != claims)
				expiration = claims.getExpiration();
		} catch (Exception e) {
			logger.error(StringConst.DATE_PARSING_PROBLEM_HYPHEN, e);
		}

		return expiration;
	}

	public String getAudienceFromToken(String token) {
		String audience = null;

		try {
			final Claims claims = getClaimsFromToken(token);

			if (null != claims)
				audience = (String) claims.get(CLAIM_KEY_AUDIENCE);
		} catch (Exception e) {
			logger.error(StringConst.AUDIENCE_PARSING_PROBLEM_HYPHEN, e);
		}

		return audience;
	}

	public String getPasswordFromToken(String token) {
		String pass = null;

		try {
			final Claims claims = getClaimsFromToken(token);

			if (null != claims)
				pass = (String) claims.get(CLAIM_KEY_PASS);
		} catch (Exception e) {
			logger.error(StringConst.PASWORD_PARSING_PROBLEM_HYPHEN, e);
		}

		return pass;
	}

	public int getDeviceIdFromToken(String token) {
		int pass = 0;

		try {
			final Claims claims = getClaimsFromToken(token);

			if (null != claims)
				pass = (int) claims.get(CLAIM_DEVICE_KEY);
		} catch (Exception e) {
			logger.error(StringConst.PASWORD_PARSING_PROBLEM_HYPHEN, e);
		}

		return pass;
	}

	public int getRoleIdFromToken(String token) {
		int pass = 0;

		try {
			final Claims claims = getClaimsFromToken(token);

			if (null != claims)
				pass = (int) claims.get(CLAIM_ROLE_KEY);
		} catch (Exception e) {
			logger.error(StringConst.PASWORD_PARSING_PROBLEM_HYPHEN, e);
		}

		return pass;
	}

	private Claims getClaimsFromToken(String token) {
		Claims claims;

		try {
			claims = Jwts.parser().setSigningKey(StringConst.SECRET).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			logger.error(StringConst.CLAIMS_PARSING_PROBLEM_HYPHEN, e);
			claims = null;
		}

		return claims;
	}

	private Date generateExpirationDate() {
		return new Date(System.currentTimeMillis() + AppConst.EXPIRATION * 1000);
	}

	public String generateToken(UserDetails userDetails) {
		JwtUser user = (JwtUser) userDetails;

		Map<String, Object> claims = new HashMap<>();
		claims.put(CLAIM_KEY_USERNAME, user.getUsername());
		claims.put(CLAIM_KEY_AUDIENCE, StringConst.WEB);
		claims.put(CLAIM_KEY_CREATED, new Date());
		claims.put(CLAIM_DEVICE_KEY, user.getDeviceId());
		claims.put(CLAIM_ROLE_KEY, user.getRoleId());

		return generateToken(claims);
	}

	private String generateToken(Map<String, Object> claims) {
		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, StringConst.SECRET).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails, HttpServletResponse servletResponse) {
		JwtUser user = (JwtUser) userDetails;
		final String username = getUsernameFromToken(token);
		final int deviceId = getDeviceIdFromToken(token);

		if (user.getDeviceId() != deviceId)
			servletResponse.addHeader(StringConst.INVALID_DEVICE, StringConst.EMPTY_STRING);

		return username.equals(user.getUsername()) && user.getDeviceId() == deviceId;
	}
}
