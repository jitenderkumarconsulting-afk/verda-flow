package com.org.verdaflow.rest.dto;

import lombok.Data;

@Data
public class ResponseEnvelope<T> {

	private T data;
	private boolean success;
	private String message;
	private Integer responseCode;

	public ResponseEnvelope(boolean success, String message, Integer responseCode) {
		this.success = success;
		this.message = message;
		this.responseCode = responseCode;
	}

	public ResponseEnvelope(T data, boolean success, String message) {
		this.data = data;
		this.success = success;
		this.message = message;

	}

	public ResponseEnvelope(T data, boolean success, String message, Integer responseCode) {
		this.data = data;
		this.success = success;
		this.message = message;
		this.responseCode = responseCode;

	}
}
