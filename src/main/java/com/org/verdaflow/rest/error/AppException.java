package com.org.verdaflow.rest.error;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	String errorCode; // NOSONAR
	String exceptionCat; // NOSONAR

	public AppException(String errorCode, String exceptionCat) {
		this.errorCode = errorCode;
		this.exceptionCat = exceptionCat;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getExceptionCat() {
		return exceptionCat;
	}
}
