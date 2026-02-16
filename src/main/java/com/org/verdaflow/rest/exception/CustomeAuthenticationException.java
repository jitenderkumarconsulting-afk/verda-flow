package com.org.verdaflow.rest.exception;

import org.springframework.security.core.AuthenticationException;

public class CustomeAuthenticationException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public CustomeAuthenticationException(String msg) {
		super(msg);
	}
}
