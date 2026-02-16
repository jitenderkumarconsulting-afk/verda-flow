package com.org.verdaflow.rest.event;

import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class WelcomeCustomerEvent extends ApplicationEvent {
	private static final long serialVersionUID = 1L;

	private String email;
	private String mobileNumber;
	private String password;

	private String firstname;
	private String lastName;

	public WelcomeCustomerEvent(Object source) {
		super(source);
	}

	public WelcomeCustomerEvent(Object source, String email, String mobileNumber, String password, String firstname,
			String lastName) {
		super(source);
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.password = password;
		this.firstname = firstname;
		this.lastName = lastName;
	}
}
