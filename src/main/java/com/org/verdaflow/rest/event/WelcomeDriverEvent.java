package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WelcomeDriverEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String email;
	private String mobileNumber;
	private String password;

	private String name;

	public WelcomeDriverEvent(Object source) {
		super(source);
	}

	public WelcomeDriverEvent(Object source, String email, String mobileNumber, String password, String name) {
		super(source);
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.name = name;
	}
}
