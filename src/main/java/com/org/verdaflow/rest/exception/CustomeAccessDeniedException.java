package com.org.verdaflow.rest.exception;

import org.springframework.security.access.AccessDeniedException;

public class CustomeAccessDeniedException extends AccessDeniedException {
	private static final long serialVersionUID = 1L;

	public CustomeAccessDeniedException(String msg) {
		super(msg);
	}
}
